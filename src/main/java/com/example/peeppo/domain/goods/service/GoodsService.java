package com.example.peeppo.domain.goods.service;

import com.example.peeppo.domain.dibs.entity.Dibs;
import com.example.peeppo.domain.dibs.repository.DibsRepository;
import com.example.peeppo.domain.dibs.service.DibsService;
import com.example.peeppo.domain.goods.entity.RequestGoods;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.goods.dto.*;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.WantedGoods;
import com.example.peeppo.domain.goods.enums.RequestStatus;
import com.example.peeppo.domain.goods.enums.Category;
import com.example.peeppo.domain.goods.repository.goods.GoodsRepository;
import com.example.peeppo.domain.goods.repository.request.RequestRepository;
import com.example.peeppo.domain.goods.repository.wantedGoods.WantedGoodsRepository;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.entity.UserImage;
import com.example.peeppo.domain.image.helper.ImageHelper;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.image.repository.UserImageRepository;
import com.example.peeppo.domain.notification.enums.NotificationStatus;
import com.example.peeppo.domain.notification.service.NotificationService;
import com.example.peeppo.domain.rating.entity.RatingGoods;
import com.example.peeppo.domain.rating.helper.RatingHelper;
import com.example.peeppo.domain.rating.repository.ratingGoodsRepository.RatingGoodsRepository;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.helper.UserRatingHelper;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.responseDto.PageResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.peeppo.domain.goods.enums.GoodsStatus.SOLDOUT;
import static com.example.peeppo.domain.goods.enums.GoodsStatus.TRADING;
import static com.example.peeppo.domain.goods.enums.RequestStatus.DONE;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final ImageRepository imageRepository;
    private final WantedGoodsRepository wantedGoodsRepository;
    private final ImageHelper imageHelper;
    private final UserRepository userRepository;
    private final RatingGoodsRepository ratingGoodsRepository;
    private final RequestRepository requestRepository;
    private final UserImageRepository userImageRepository;
    private final UserRatingHelper userRatingHelper;

    private final RatingHelper ratingHelper;
    private final DibsService dibsService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificationService notificationService;

    private final DibsRepository dibsRepository;
    private static final String RECENT_GOODS = "goods";
    private static final int MAX_RECENT_GOODS = 4;
    //private List<Long> goodsRecent = new ArrayList<>();
    private List<String> goodsRecent = new ArrayList<>();

    @Transactional
    public ApiResponse<MsgResponseDto> goodsCreate(GoodsRequestDto goodsRequestDto, List<MultipartFile> images, WantedRequestDto wantedRequestDto, User user) {
        if (goodsRequestDto.getSellerPrice() == null && goodsRequestDto.getRatingCheck()) {
            throw new IllegalArgumentException("레이팅을 원하시면 가격을 입력해주세요.");
        }
        if (images == null) {
            throw new IllegalArgumentException("물품 이미지가 꼭 필요합니다.");
        }
        WantedGoods wantedGoods = new WantedGoods(wantedRequestDto);
        System.out.println("실행체크");
        Goods goods = new Goods(goodsRequestDto, wantedGoods, user, GoodsStatus.ONSALE);
        RatingGoods ratingGoods = new RatingGoods(goods);

        goodsRepository.save(goods);
        ratingGoodsRepository.save(ratingGoods);
        wantedGoodsRepository.save(wantedGoods);

        List<String> imageUrls = imageHelper
                .saveImagesToS3AndRepository(images, goods)
                .stream()
                .map(Image::getImageUrl)
                .toList();

        return new ApiResponse<>(true, new MsgResponseDto("게시글이 등록되었습니다."), null);
    }

    public Page<GoodsListResponseDto> allGoods(int page, int size, String sortBy, boolean isAsc, String categoryStr, UserDetailsImpl userDetails) {
        Pageable pageable = paging(page, size, sortBy, isAsc);

        if (userDetails == null) { // 비로그인시
            return allGoodsEveryone(pageable, categoryStr);
        }

        User user = userDetails.getUser();
        userRatingHelper.getUser(user.getUserId());
        Page<Goods> goodsPage;
        if (categoryStr != null) {
            try {
                Category category = Category.valueOf(categoryStr);
                goodsPage = goodsRepository.findAllByCategoryAndIsDeletedFalse(category, pageable);
                return allGoods(goodsPage, pageable, user);
            } catch (IllegalArgumentException e) {
                log.error("잘못된 category 값입니다.");
                throw new IllegalArgumentException("올바른 category 값을 입력해주세요.", e);
            }
        } else {
            goodsPage = goodsRepository.findAllByIsDeletedFalse(pageable);
            return allGoods(goodsPage, pageable, user);
        }
    }

    public Page<GoodsListResponseDto> allGoodsEveryone(Pageable pageable, String categoryStr) {
        List<GoodsListResponseDto> goodsResponseList = new ArrayList<>();
        Page<Goods> goodsPage;
        if (categoryStr != null) {
            try {
                Category category = Category.valueOf(categoryStr);
                goodsPage = goodsRepository.findAllByCategoryAndIsDeletedFalse(category, pageable);
                for (Goods goods : goodsPage.getContent()) {
                    Image image = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goods.getGoodsId());
                    Double AvgRatingPrice = ratingGoodsRepository.findByGoodsGoodsId(goods.getGoodsId()).getAvgRatingPrice();
                    goodsResponseList.add(new GoodsListResponseDto(goods, image.getImageUrl(), AvgRatingPrice));
                }
                return new PageResponse<>(goodsResponseList, pageable, goodsPage.getTotalElements());

            } catch (IllegalArgumentException e) {
                log.error("올바른 category 값을 입력해주세요.");
                throw new IllegalArgumentException("올바른 category 값을 입력해주세요.", e);
            }
        } else {
            goodsPage = goodsRepository.findAllByIsDeletedFalse(pageable);


            for (Goods goods : goodsPage.getContent()) {
                Image image = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goods.getGoodsId());
                goodsResponseList.add(new GoodsListResponseDto(goods, image.getImageUrl()));
            }

            return new PageResponse<>(goodsResponseList, pageable, goodsPage.getTotalElements());
        }
    }


    public ApiResponse<GoodsDetailResponseDto> getGoods(Long goodsId, User user) {
        userRatingHelper.getUser(user.getUserId());
        Goods goods = findGoods(goodsId);
        List<RcGoodsResponseDto> rcGoodsResponseDtoList = getSameCategoryGoodsWithUser(goods, user);
        boolean checkSameUser = goods.getUser().getUserId() == user.getUserId();
        WantedGoods wantedGoods = findWantedGoods(goodsId);
        List<String> imageUrls = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAsc(goodsId)
                .stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        if (goodsRecent.size() >= MAX_RECENT_GOODS) {
            goodsRecent.remove(0);
        }
        // goodsRecent.add(Long.toString(goods.getGoodsId())); // 조회시에 리스트에 추가 !
        Optional<Dibs> dibsGoods = dibsRepository.findByUserUserIdAndGoodsGoodsIdAndGoodsIsDeletedFalse(user.getUserId(), goodsId);
        boolean checkDibs = dibsGoods.isPresent();
        Long dibsCount = dibsRepository.countByGoodsGoodsIdAndGoodsIsDeletedFalse(goodsId);
        return new ApiResponse<>(true, new GoodsDetailResponseDto(new GoodsResponseDto(goods, imageUrls, wantedGoods, checkSameUser, checkDibs, dibsCount), rcGoodsResponseDtoList), null);
    }

    public ApiResponse<PocketResponseDto> getMyGoods(int page,
                                                     int size,
                                                     String sortBy,
                                                     boolean isAsc,
                                                     String goodsStatusStr,
                                                     Long userId) {

        Pageable pageable = paging(page, size, sortBy, isAsc);
        User user = findUser(userId);
        userRatingHelper.getUser(user.getUserId());
        GoodsStatus goodsStatus;
        if (goodsStatusStr != null) {
            try {
                goodsStatus = GoodsStatus.valueOf(goodsStatusStr);
                Page<Goods> goodsList = goodsRepository.findAllByGoodsStatusAndIsDeletedFalse(goodsStatus, pageable);
                return getMyGoods(goodsList, user, pageable);
            } catch (IllegalArgumentException e) {
                log.info("올바르지 않은 goodsStatus 입니다. {}", goodsStatusStr);
                throw new IllegalArgumentException("올바르지 않은 goodsStatus 입니다");
            }
        } else {
            Page<Goods> goodsList = goodsRepository.findAllByUserAndIsDeletedFalse(user, pageable);
            return getMyGoods(goodsList, user, pageable);
        }
    }

    @Transactional
    public ApiResponse<GoodsResponseDto> goodsUpdate(Long goodsId, GoodsRequestDto
            goodsRequestDto, List<MultipartFile> images, WantedRequestDto wantedRequestDto) {
        Goods goods = findGoods(goodsId);
        WantedGoods wantedGoods = findWantedGoods(goodsId);

        // repository 이미지 삭제
        List<Image> imageList = imageRepository.findByGoodsGoodsId(goodsId);
        imageHelper.repositoryImageDelete(imageList);

        // s3 이미지 삭제
        for (Image image : imageList) {
            imageHelper.deleteImageAmazonS3(image.getImageKey());
        }

        // 이미지 업로드
        List<String> imageUrls = imageHelper.saveImagesToS3AndRepository(images, goods)
                .stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
        goods.update(goodsRequestDto);
        wantedGoods.update(wantedRequestDto);

        return new ApiResponse<>(true, new GoodsResponseDto(goods, imageUrls, wantedGoods), null);
    }

    @Transactional
    public ApiResponse<DeleteResponseDto> deleteGoods(Long goodsId, Long userId) throws IllegalAccessException {
        Goods goods = findGoods(goodsId);
        if (Objects.equals(userId, goods.getUser().getUserId())) {
            if (goods.getGoodsStatus().equals(GoodsStatus.ONSALE) ||
                    goods.getGoodsStatus().equals(GoodsStatus.SOLDOUT)) {

                goods.changeStatus(GoodsStatus.ONSALE);
                goods.delete();
                goodsRepository.save(goods);
            } else {
                throw new IllegalArgumentException("입찰, 경매, 거래중인 상품은 삭제할 수 없습니다.");
            }
        } else {
            throw new IllegalAccessException("유저의 정보가 올바르지 않습니다.");
        }
        return new ApiResponse<>(true, new DeleteResponseDto("삭제되었습니다"), null);
    }


    public List<GoodsRecentDto> recentGoods(HttpServletResponse response) {
        List<GoodsRecentDto> goodsRecentDtos = new ArrayList<>();
        // 조회하면 리스트에 id, productname add 해주기

        Cookie goodsCookie = new Cookie(RECENT_GOODS, UriUtils.encode(String.join(",", goodsRecent), "UTF-8")); // 문자열만 저장 가능
        goodsCookie.setMaxAge(24 * 60 * 60); // 하루동안 저장
        response.addCookie(goodsCookie); // 전송

        for (String id : goodsRecent) {
            Goods goods = goodsRepository.findById(Long.parseLong(id)).orElse(null);
            GoodsRecentDto goodsRecentDto = new GoodsRecentDto(goods);
            goodsRecentDtos.add(goodsRecentDto);
        }
        return goodsRecentDtos;
    }

    public List<GoodsResponseDto> getMyGoodsWithoutPagenation(User user) {
        return getGoodsResponseDtos(user);
    }

    public ApiResponse<UrPocketResponseDto> getPocket(String nickname, UserDetailsImpl userDetails, int page,
                                                      int size, String sortBy, boolean isAsc) {
        User user = userRepository.findUserByNickname(nickname);
        userRatingHelper.getUser(user.getUserId());
        if (userDetails != null) { // 로그인 된 경우다 !!
            if (Objects.equals(user.getUserId(), userDetails.getUser().getUserId())) {
                throw new IllegalArgumentException("같은 사용자입니다 ");
            }
        }
        if (userDetails != null) { // 로그인 된 경우다 !!
            if (Objects.equals(user.getUserId(), userDetails.getUser().getUserId())) {
                throw new IllegalArgumentException("같은 사용자입니다 ");
            }
        }
        Pageable pageable = paging(page, size, sortBy, isAsc);
        Page<Goods> goodsList = goodsRepository.findAllByUserAndIsDeletedFalse(user, pageable);
        List<GoodsListResponseDto> myGoods = new ArrayList<>();
        for (Goods goods : goodsList) {
            Image firstImage = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goods.getGoodsId());

            boolean checkDibs = false;
            if (userDetails != null) {
                checkDibs = dibsService.checkDibsGoods(userDetails.getUser().getUserId(), goods.getGoodsId());
            }

            myGoods.add(new GoodsListResponseDto(goods, firstImage.getImageUrl(), checkDibs));
        }
        UserImage userImage = userImageRepository.findByUserUserId(user.getUserId()).orElse(null);
        UrPocketResponseDto urPocketResponseDto = new UrPocketResponseDto(user, myGoods, userImage);

        return new ApiResponse<>(true, urPocketResponseDto, null);
//        return getGoodsResponseDtos(user);
    }

    private List<GoodsResponseDto> getGoodsResponseDtos(User user) {
        userRatingHelper.getUser(user.getUserId());
        List<Goods> goodsList = goodsRepository.findAllByUserAndIsDeletedFalseAndGoodsStatus(user, GoodsStatus.ONSALE);
        List<GoodsResponseDto> goodsResponseDtoList = new ArrayList<>();
        for (Goods goods : goodsList) {
            List<String> imageUrls = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAsc(goods.getGoodsId())
                    .stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList());
            RatingGoods ratingGoods = ratingGoodsRepository.findByGoodsGoodsId(goods.getGoodsId());
            goodsResponseDtoList.add(new GoodsResponseDto(goods, imageUrls, ratingGoods.getAvgRatingPrice()));
        }
        return goodsResponseDtoList;
    }

    public ApiResponse<List<GoodsListResponseDto>> searchGoods(String keyword) {
        List<Goods> searchList = goodsRepository.findByTitleContainingAndIsDeletedFalse(keyword);
        List<GoodsListResponseDto> goodsListResponseDtos = new ArrayList<>();
        for (Goods search : searchList) {
            goodsListResponseDtos.add(new GoodsListResponseDto(search));
        }
        return new ApiResponse<>(true, goodsListResponseDtos, null);
    }

    public List<RcGoodsResponseDto> getSameCategoryGoods(Goods goods) {
        List<Goods> goodsList = goodsRepository.findByCategoryAndIsDeletedFalse(goods.getCategory());
        List<RcGoodsResponseDto> rcGoodsResponseDtoList = new ArrayList<>();
        for (Goods goods1 : goodsList) {
            if (!Objects.equals(goods1.getGoodsId(), goods.getGoodsId())) {
                RcGoodsResponseDto rcGoodsResponseDto = new RcGoodsResponseDto(goods1);
                rcGoodsResponseDtoList.add(rcGoodsResponseDto);
            }
        }
        return rcGoodsResponseDtoList;
    }

    public List<RcGoodsResponseDto> getSameCategoryGoodsWithUser(Goods goods, User user) {
        userRatingHelper.getUser(user.getUserId());
        List<Goods> goodsList = goodsRepository.findByCategoryAndIsDeletedFalse(goods.getCategory());
        List<RcGoodsResponseDto> rcGoodsResponseDtoList = new ArrayList<>();
        for (Goods goods1 : goodsList) {
            if (((!Objects.equals(goods1.getGoodsId(), goods.getGoodsId())) && (!Objects.equals(user.getUserId(), goods1.getUser().getUserId())))) {
                boolean checkDibs = false;
                checkDibs = dibsService.checkDibsGoods(user.getUserId(), goods1.getGoodsId());
                RcGoodsResponseDto rcGoodsResponseDto = new RcGoodsResponseDto(goods1, checkDibs);
                rcGoodsResponseDtoList.add(rcGoodsResponseDto);
            }
        }
        return rcGoodsResponseDtoList;
    }

    // 로그인 없이 조회
    public ApiResponse<GoodsDetailResponseDto> getGoodsEveryone(Long goodsId) {
        Goods goods = findGoods(goodsId);
        List<RcGoodsResponseDto> rcGoodsResponseDtoList = getSameCategoryGoods(goods);
        WantedGoods wantedGoods = findWantedGoods(goodsId);
        List<Image> images = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAsc(goodsId);
        List<String> imageUrls = images.stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
        if (goodsRecent.size() >= MAX_RECENT_GOODS) {
            goodsRecent.remove(0);
        }
        goodsRecent.add(Long.toString(goods.getGoodsId())); // 조회시에 리스트에 추가 !
        return new ApiResponse<>(true, new GoodsDetailResponseDto(new GoodsResponseDto(goods, imageUrls, wantedGoods), rcGoodsResponseDtoList), null);
    }


    // 내가 보낸 교환요청
    public ResponseEntity<Page<GoodsRequestResponseDto>> requestTradeList(User user, int page, int size, String sortBy, boolean isAsc,
                                                                          String requestStatusStr) {

        Pageable pageable = paging(page, size, sortBy, isAsc);
        Page<Goods> requestGoods;
        RequestStatus requestStatus1;
        List<GoodsRequestResponseDto> goodsRequestResponseDtos = new ArrayList<>();
        if (requestStatusStr != null) {
            //1) requestgoods 테이블에서 seller goods 전부 찾아오기 => DTO 변환해주기
            requestStatus1 = RequestStatus.valueOf(requestStatusStr);
            requestGoods = requestRepository.findSellerByUserIdAndRequestStatus(user.getUserId(), requestStatus1, pageable);
        } else {
            requestGoods = requestRepository.findSellerByUserId(user.getUserId(), pageable);
        }

        //2) requestGoods 순회하면서 buyerGoods 찾아오기
        for (Goods requestGood : requestGoods) {
            Goods goods = goodsRepository.findByGoodsId(requestGood.getGoodsId()).orElse(null);// sellergoods가 뭔지 찾았다 !
            RequestGoods requestGoods1 = requestRepository.findBySellerGoodsId(goods.getGoodsId());
            RequestSingleResponseDto goodsListResponseDto = new RequestSingleResponseDto(goods);
            List<RequestGoods> buyerGoodsList = requestRepository.findAllBySellerGoodsIdAndUserId(goods.getGoodsId(), user.getUserId());
            List<RequestSingleResponseDto> goodsListResponseDtos = new ArrayList<>();
            // 3) requestGoods 순회하며 buyerGoods 물품 정보 가져오기 => dto 로 변환하기
            for (RequestGoods buyerGoods : buyerGoodsList) {
                Goods goods1 = goodsRepository.findByGoodsId(buyerGoods.getBuyer().getGoodsId()).orElse(null);// buyerGoods 찾기
                RequestSingleResponseDto goodsListResponseDto2 = new RequestSingleResponseDto(goods1);
                goodsListResponseDtos.add(goodsListResponseDto2);
            }
            goodsRequestResponseDtos.add(new GoodsRequestResponseDto(requestGoods1.getCreatedAt(), requestGoods1.getRequestStatus(), goodsListResponseDto, goodsListResponseDtos));
        }//status값 수정

        PageResponse response = new PageResponse<>(goodsRequestResponseDtos, pageable, requestGoods.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);

    }

    //내가 받은 교환요청
    public ResponseEntity<Page<GoodsRequestResponseDto>> receiveTradeList(User user, int page, int size, String sortBy, boolean isAsc, String requestStatusStr) {

        Pageable pageable = paging(page, size, sortBy, isAsc);
        RequestStatus requestStatus1;
        Page<RequestGoods> requestGoodsList;

        if (requestStatusStr != null) {
//1) requestgoods 테이블에서 seller goods 전부 찾아오기 => DTO 변환해주기
            try {
                requestStatus1 = RequestStatus.valueOf(requestStatusStr);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("올바르지 않은 상태값입니다.");
            }
            requestGoodsList = requestRepository.findBySellerGoodsIdAndUserIdGroupAndStatus(user.getUserId(), pageable, requestStatus1);

        } else {
            requestGoodsList = requestRepository.findBySellerGoodsIdAndUserIdGroup(user.getUserId(), pageable);
            }
        List<GoodsRequestResponseDto> finalResponseDto = new ArrayList<>();

//2) requestGoods 순회하면서 buyerGoods 찾아오기
        for (RequestGoods requestGoods2 : requestGoodsList) {
            LocalDateTime createAt = null;
            RequestStatus requestStatus = null;
            List<RequestGoods> buyerGoodsList = requestRepository.findBySellerGoodsIdAndGroup(user.getUserId(), requestGoods2.getSeller().getGoodsId(), requestGoods2.getBuyer().getUser().getUserId());
            RequestSingleResponseDto goodsListResponseDto = new RequestSingleResponseDto(requestGoods2.getSeller());
            List<RequestSingleResponseDto> goodsListResponseDtos = new ArrayList<>(); // 같은 물건에 요청 넣은 친구들 저장해서 넣어줌 !

            for (RequestGoods requestGoods3 : buyerGoodsList) {
                createAt = requestGoods3.getCreatedAt();
                requestStatus = requestGoods3.getRequestStatus();
                RequestSingleResponseDto goodsListResponseDtos2 = new RequestSingleResponseDto(requestGoods3.getBuyer());
                goodsListResponseDtos.add(goodsListResponseDtos2);


            }
            finalResponseDto.add(new GoodsRequestResponseDto(createAt, requestStatus, goodsListResponseDto, goodsListResponseDtos));

        }
        PageResponse response = new PageResponse<>(finalResponseDto, pageable, requestGoodsList.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    //내물건이 아니여야한다 !
    @Transactional
    public ResponseDto goodsRequest(User user, GoodsRequestRequestDto goodsRequestRequestDto, Long urGoodsId) {
        Goods urGoods = goodsRepository.findByGoodsId(urGoodsId) // sellergoods(남의 물건)
                .orElseThrow(() -> new NullPointerException("해당 상품이 존재하지 않습니다."));
        List<RequestGoods> requestGoods = new ArrayList<>();
        List<Goods> goodsList = new ArrayList<>();

        for (Long goodsId : goodsRequestRequestDto.getGoodsId()) {
            Goods goods = goodsRepository.findById(goodsId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 goodsId 입니다.")); // 내 물건
            if (!goods.getUser().getUserId().equals(user.getUserId())) {
                throw new IllegalArgumentException("자신의 물품만 교환요청 할 수 있습니다.");
            }


            if (!(urGoods.getUser().equals(goods.getUser()))) {
                if (goods.getGoodsStatus().equals(GoodsStatus.ONSALE)) {// ||goods.getRequestedStatus().equals(RequestedStatus.REQUESTED)
                    requestGoods.add(new RequestGoods(urGoods, user, goods, RequestStatus.REQUEST));
                } else {
                    throw new IllegalArgumentException("해당 물품은 다른 곳에 사용되거나 판매중 상태가 아닙니다.");
                }
            } else {
                throw new IllegalArgumentException("내 물건은 교환할 수 없습니다.");
            }
            goods.changeStatus(TRADING);
            goodsList.add(goods);
        }
        goodsRepository.saveAll(goodsList);
        requestRepository.saveAll(requestGoods);
        notificationService.send(urGoods.getUser(), NotificationStatus.REQUEST, "새로운 교환요청이 생겼습니다");
        return new ResponseDto("교환신청이 완료되었습니다.", HttpStatus.OK.value(), "OK");
    }



    public Page<GoodsListResponseDto> allGoods(Page<Goods> goodsPage, Pageable pageable, User user) {

        List<GoodsListResponseDto> goodsResponseList = new ArrayList<>();
        userRatingHelper.getUser(user.getUserId());

        for (Goods goods : goodsPage.getContent()) {
            boolean checkSameUser = Objects.equals(goods.getUser().getUserId(), user.getUserId());
            Image image = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goods.getGoodsId());
            boolean checkDibs = dibsRepository.findByUserUserIdAndGoodsGoodsIdAndGoodsIsDeletedFalse(user.getUserId(), goods.getGoodsId()).isPresent();
            goodsResponseList.add(new GoodsListResponseDto(goods, image.getImageUrl(), checkDibs, checkSameUser));
        }
        return new PageResponse<>(goodsResponseList, pageable, goodsPage.getTotalElements());
    }

    private ApiResponse<PocketResponseDto> getMyGoods(Page<Goods> goodsList, User user, Pageable pageable) {
        userRatingHelper.getUser(user.getUserId());
        List<PocketListResponseDto> myGoods = goodsList.stream()
                .map(goods -> {
                    long ratingPrice = (long) ratingHelper.getAvgPriceByGoodsId(goods.getGoodsId());
                    Image firstImage = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goods.getGoodsId());
                    return new PocketListResponseDto(goods, firstImage.getImageUrl(), ratingPrice);
                }).collect(Collectors.toList());

        String imageUrl = null;
        UserImage image = userImageRepository.findByUserUserId(user.getUserId()).orElse(null);
        if (Objects.nonNull(image)) {
            imageUrl = image.getImageUrl();
        }

        return new ApiResponse<>(true, new PocketResponseDto(user, new PageImpl<>(myGoods, pageable, goodsList.getTotalElements()), imageUrl), null);
    }


    public ResponseDto ratingCheck(User user) {
        userRatingHelper.getUser(user.getUserId());
        Goods goods = goodsRepository.findByUserUserId(user.getUserId())
                .orElseThrow(() -> new NullPointerException("존재하지 않는 상품입니다."));
        goods.changeCheck(true);
        goodsRepository.save(goods);

        ResponseDto responseDto = new ResponseDto("레이팅 체크가 가능해집니다.", HttpStatus.OK.value(), "OK");
        return responseDto;
    }

    public Goods findGoods(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() ->
                new NullPointerException("해당 게시글은 존재하지 않습니다."));
        if (goods.getIsDeleted()) {
            throw new IllegalStateException("삭제된 게시글입니다.");
        }
        return goods;
    }

    public WantedGoods findWantedGoods(Long wantedId) {
        WantedGoods wantedGoods = wantedGoodsRepository.findById(wantedId).orElseThrow(() ->
                new NullPointerException("해당 게시글은 존재하지 않습니다."));
        return wantedGoods;
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다."));
    }


    private Pageable paging(int page, int size, String sortBy, boolean isAsc) {
        // 정렬
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // pageable 생성
        return PageRequest.of(page, size, sort);
    }

    // 승인 => 교환중으로 상태 변경
    @Transactional
    public void goodsAccept(Long sellerGoodsId, RequestAcceptRequestDto requestAcceptRequestDto, User user) {
        List<RequestGoods> buyerRequest = requestRepository.findAllBySellerGoodsId(sellerGoodsId);
        List<Goods> goodsList = new ArrayList<>();

        for (RequestGoods requestGoods : buyerRequest) {
            if (!requestGoods.getSeller().getUser().getUserId().equals(user.getUserId())) {
                throw new IllegalArgumentException("물품 교환 요청 수락은 본인만 가능합니다.");
            }
            Goods goods = requestGoods.getBuyer();
            if (requestAcceptRequestDto.getRequestId().contains(requestGoods.getBuyer().getGoodsId())) {
                requestGoods.changeStatus(RequestStatus.TRADING);
            } else {
                requestGoods.changeStatus(RequestStatus.CANCEL);
                goods.changeStatus(GoodsStatus.ONSALE);
                goodsList.add(goods);
            }
        }

        requestRepository.saveAll(buyerRequest);
        goodsRepository.saveAll(goodsList);
        notificationService.send(buyerRequest.get(0).getBuyer().getUser(), NotificationStatus.REQUEST, "교환요청이 수락되었습니다");
    }


    // 요청 -> 거절
    public void goodsRefuse(RequestAcceptRequestDto requestAcceptRequestDto, User user) {
        User buyer = null;
        for (Long goodsId : requestAcceptRequestDto.getRequestId()) {
            RequestGoods requestGoods = requestRepository.findByBuyerGoodsId(goodsId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
            if (!requestGoods.getSeller().getUser().getUserId().equals(user.getUserId())) {
                throw new IllegalArgumentException("물품 교환 요청 수락은 본인만 가능합니다.");
            }
            buyer = requestGoods.getUser();
            requestGoods.changeStatus(RequestStatus.CANCEL);
            requestRepository.save(requestGoods);
        }
        notificationService.send(buyer, NotificationStatus.REQUEST, "교환요청이 거절되었습니다");
    }

    // 교환 완료 => 우선적으로 받은 쪽에서만 진행하는거로 !
    @Transactional
    public ApiResponse<MsgResponseDto> tradeCompleted(RequestAcceptRequestDto requestAcceptRequestDto, UserDetailsImpl userDetails) {
        List<Goods> buyerAndSellerList = new ArrayList<>();
        List<RequestGoods> requestGoodsList = new ArrayList<>();
        for (Long requestGoodsId : requestAcceptRequestDto.getRequestId()) {

            RequestGoods requestGoods = requestRepository.findBuyerGoodsIdAndUserId(requestGoodsId, userDetails.getUser().getUserId(), RequestStatus.TRADING);
            if (!(requestGoods.getRequestStatus().equals(RequestStatus.TRADING))) {
                throw new IllegalArgumentException("정상적인 접근이 아닙니다.");
            }
            if (!(Objects.equals(requestGoods.getSeller().getUser().getUserId(), userDetails.getUser().getUserId()))) {
                throw new IllegalArgumentException("판매 물품의 주인이 아니라면 교환 완료를 진행할 수 없습니다...");
            }
            buyerAndSellerList.add(requestGoods.getBuyer());
            buyerAndSellerList.add(requestGoods.getSeller());

            requestGoods.getBuyer().changeStatus(SOLDOUT);
            requestGoods.getSeller().changeStatus(SOLDOUT);
            requestGoods.changeStatus(DONE);

            requestGoodsList.add(requestGoods);
        }
        goodsRepository.saveAll(buyerAndSellerList);
        requestRepository.saveAll(requestGoodsList);
        return new ApiResponse<>(true, new MsgResponseDto("상대방의 교환완료를 기다리는중..."), null);
    }

    /*  @Transactional
      public ApiResponse<MsgResponseDto> tradeCompleted(RequestAcceptRequestDto requestAcceptRequestDto,
                                                        UserDetailsImpl userDetails) {
          List<RequestGoods> requestGoodsList = new ArrayList<>();
          List<Long> buyerGoodsIds = new ArrayList<>();
          Long userId = userDetails.getUser().getUserId();
          for (int i = 0; i < requestAcceptRequestDto.getRequestId().size(); i++) {
              RequestGoods requestGoods = requestRepository.findByBuyerGoodsId(requestAcceptRequestDto.getRequestId().get(i))
                      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
              if (!requestGoods.getRequestStatus().equals(RequestStatus.TRADING)) {
                  throw new IllegalArgumentException("정상적인 접근이 아닙니다.");
              }
              if(!requestGoods.getUser().getUserId().equals(userId) ||
              !requestGoods.getBuyer().getUser().getUserId().equals(userId)){
                  throw new IllegalArgumentException("물품 주인이 아니라면 거래를 진행할 수 있습니다..");
              }
              requestGoodsList.add(requestGoods);
              buyerGoodsIds.add(requestGoods.getBuyer().getGoodsId());
          }
          Goods buyerGoods = requestGoodsList.get(0).getBuyer();
          Goods sellerGoods = requestGoodsList.get(0).getSeller();

          if (buyerGoods.getUser().getUserId().equals(userId)) {
              List<Goods> buyerGoodsList = buyerGoodsListStatusChange(buyerGoodsIds, TRADING);
              goodsRepository.saveAll(buyerGoodsList);
          } else if (sellerGoods.getUser().getUserId().equals(userId)) {
              sellerGoods.changeStatus(TRADING);
              goodsRepository.save(buyerGoods);
          }

          if (buyerGoods.getGoodsStatus().equals(TRADING) &&
                  sellerGoods.getGoodsStatus().equals(TRADING)) {
              List<Goods> GoodsList = buyerGoodsListStatusChange(buyerGoodsIds, SOLDOUT);
              sellerGoods.changeStatus(SOLDOUT);
              GoodsList.add(sellerGoods);
              goodsRepository.saveAll(GoodsList);

              for (RequestGoods requestGoods : requestGoodsList) {
                  requestGoods.changeStatus(DONE);
              }
              requestRepository.saveAll(requestGoodsList);
              return new ApiResponse<>(true, new MsgResponseDto("교환 완료!"), null);
          }

          return new ApiResponse<>(true, new MsgResponseDto("상대방의 교환완료를 기다리는중..."), null);
      }
  */
    private List<Goods> buyerGoodsListStatusChange(List<Long> buyerGoodsIds, GoodsStatus goodsStatus) {
        List<Goods> buyerGoodsList = new ArrayList<>();
        for (Long goodsId : buyerGoodsIds) {
            Goods goods = findGoods(goodsId);
            goods.changeStatus(goodsStatus);
            buyerGoodsList.add(goods);
        }
        return buyerGoodsList;
    }

    // 요청 -> 수락
//    goodsStadus: soldout
//    requestStatus: done // 물품교환요청




/*    public String refuseGoods(Long goodsId, User user) {
        // 존재하는 물품의 ID인지 확인
        Goods goods = goodsRepository.findByGoodsId(goodsId)
                .orElseThrow(() -> new NullPointerException("해당 상품이 존재하지 않습니다."));
        // 물품의 상태를 ONSALE 로 변경하고 물품 교환에서 request_goods 내의 request_status 를 cancel로 변경하기


  }*/

}