package com.example.peeppo.domain.goods.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.peeppo.domain.dibs.entity.Dibs;
import com.example.peeppo.domain.dibs.repository.DibsRepository;
import com.example.peeppo.domain.dibs.service.DibsService;
import com.example.peeppo.domain.goods.dto.*;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.WantedGoods;
import com.example.peeppo.domain.goods.enums.Category;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.goods.repository.WantedGoodsRepository;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.helper.ImageHelper;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.rating.entity.RatingGoods;
import com.example.peeppo.domain.rating.helper.RatingHelper;
import com.example.peeppo.domain.rating.repository.ratingGoodsRepository.RatingGoodsRepository;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.responseDto.PageResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final ImageRepository imageRepository;
    private final WantedGoodsRepository wantedGoodsRepository;
    private final ImageHelper imageHelper;
    private final AmazonS3 amazonS3;
    private final String bucket;
    private final UserRepository userRepository;
    private final RatingGoodsRepository ratingGoodsRepository;

    private final RatingHelper ratingHelper;
    private final DibsService dibsService;

    private final DibsRepository dibsRepository;
    private static final String RECENT_GOODS = "goods";
    private static final int MAX_RECENT_GOODS = 4;
    //private List<Long> goodsRecent = new ArrayList<>();
    private List<String> goodsRecent = new ArrayList<>();

    @Transactional
    public ApiResponse<GoodsResponseDto> goodsCreate(GoodsRequestDto goodsRequestDto,
                                                     List<MultipartFile> images,
                                                     WantedRequestDto wantedRequestDto,
                                                     User user) {
        if (goodsRequestDto.getSellerPrice() == null && goodsRequestDto.getRatingCheck()) {
            throw new IllegalArgumentException("레이팅을 원하시면 가격을 입력해주세요.");
        }
        WantedGoods wantedGoods = new WantedGoods(wantedRequestDto);
        System.out.println("실행체크");
        Goods goods = new Goods(goodsRequestDto, wantedGoods, user, GoodsStatus.ONSALE);
        RatingGoods ratingGoods = new RatingGoods(goods);

        goodsRepository.save(goods);
        ratingGoodsRepository.save(ratingGoods);
        wantedGoodsRepository.save(wantedGoods);

        List<String> imageUrls = imageHelper
                .saveImagesToS3AndRepository(images, amazonS3, bucket, goods)
                .stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, new GoodsResponseDto(goods, imageUrls, wantedGoods), null);
    }

    public Page<GoodsListResponseDto> allGoods(int page, int size, String sortBy, boolean isAsc, String categoryStr, UserDetailsImpl userDetails) {
        Pageable pageable = paging(page, size, sortBy, isAsc);

        if (userDetails == null) { // 비로그인시
            return allGoodsEveryone(pageable, categoryStr);
        }

        User user = userDetails.getUser();
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
                    goodsResponseList.add(new GoodsListResponseDto(goods, image.getImageUrl()));
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


    public ApiResponse<GoodsResponseDto> getGoods(Long goodsId, User user) {
        Goods goods = findGoods(goodsId);
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
        Optional<Dibs> dibsGoods = dibsRepository.findByUserUserIdAndGoodsGoodsId(user.getUserId(), goodsId);
        boolean checkDibs = dibsGoods.isPresent();

        return new ApiResponse<>(true, new GoodsResponseDto(goods, imageUrls, wantedGoods, checkSameUser, checkDibs), null);
    }

    public ApiResponse<PocketResponseDto> getMyGoods(int page,
                                                     int size,
                                                     String sortBy,
                                                     boolean isAsc,
                                                     String goodsStatusStr,
                                                     Long userId) {

        Pageable pageable = paging(page, size, sortBy, isAsc);
        User user = findUser(userId);
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
            imageHelper.deleteFileFromS3(image.getImageKey(), amazonS3, bucket);
        }

        // 이미지 업로드
        List<String> imageUrls = imageHelper.saveImagesToS3AndRepository(images, amazonS3, bucket, goods)
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
            goods.delete();
            goodsRepository.save(goods);
        } else {
            throw new IllegalAccessException();
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
        if (userDetails != null) { // 로그인 된 경우다 !!
            if (user.getUserId() == userDetails.getUser().getUserId()) {
                throw new IllegalArgumentException("같은 사용자입니다 ");
            }
        }
        if (userDetails != null) { // 로그인 된 경우다 !!
            if (user.getUserId() == userDetails.getUser().getUserId()) {
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

        UrPocketResponseDto urPocketResponseDto = new UrPocketResponseDto(user, myGoods);

        return new ApiResponse<>(true, urPocketResponseDto, null);
//        return getGoodsResponseDtos(user);
    }

    private List<GoodsResponseDto> getGoodsResponseDtos(User user) {
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
        List<Goods> searchList = goodsRepository.findByTitleContaining(keyword);
        List<GoodsListResponseDto> goodsListResponseDtos = new ArrayList<>();
        for (Goods search : searchList) {
            goodsListResponseDtos.add(new GoodsListResponseDto(search));
        }
        return new ApiResponse<>(true, goodsListResponseDtos, null);
    }


    // 로그인 없이 조회
    public ApiResponse<GoodsResponseDto> getGoodsEveryone(Long goodsId) {
        Goods goods = findGoods(goodsId);
        WantedGoods wantedGoods = findWantedGoods(goodsId);
        List<Image> images = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAsc(goodsId);
        List<String> imageUrls = images.stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
        if (goodsRecent.size() >= MAX_RECENT_GOODS) {
            goodsRecent.remove(0);
        }
        goodsRecent.add(Long.toString(goods.getGoodsId())); // 조회시에 리스트에 추가 !
        return new ApiResponse<>(true, new GoodsResponseDto(goods, imageUrls, wantedGoods), null);
    }

    public ResponseEntity<Page<GoodsListResponseDto>> goodsTradeList(User user,
                                                                     int page,
                                                                     int size,
                                                                     String sortBy,
                                                                     boolean isAsc,
                                                                     GoodsStatus goodsStatus) {
        Pageable pageable = paging(page, size, sortBy, isAsc);
        Page<Goods> myGoodsPage;

        if (goodsStatus != null) {
            myGoodsPage = goodsRepository.findByUserUserIdAndGoodsStatus(user.getUserId(), pageable, goodsStatus);
        } else {
            myGoodsPage = goodsRepository.findByUserUserId(user.getUserId(), pageable);
        }

        List<GoodsListResponseDto> goodsListResponseDtoList = myGoodsPage.stream()
                .map(goods -> {
                    Image image = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goods.getGoodsId());
                    return new GoodsListResponseDto(goods, image.getImageUrl());
                })
                .collect(Collectors.toList());

        PageResponse response = new PageResponse<>(goodsListResponseDtoList, pageable, myGoodsPage.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    public ResponseDto goodsRequest(User user, GoodsRequestRequestDto goodsRequestRequestDto, Long urGoodsId) {
        Goods urGoods = goodsRepository.findByGoodsId(urGoodsId)
                .orElseThrow(() -> new NullPointerException("해당 상품이 존재하지 않습니다."));

        for (Long goodsId : goodsRequestRequestDto.getGoodsId()) {
            Goods goods = goodsRepository.findById(goodsId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 goodsId 입니다."));

            if (!goods.getUser().getUserId().equals(user.getUserId())) {
                if (goods.getGoodsStatus().equals(GoodsStatus.ONSALE)) {
                    goods.changeStatus(GoodsStatus.REQUEST);
                } else {
                    throw new IllegalArgumentException("해당 물품은 다른 곳에 사용되거나 판매중 상태가 아닙니다.");
                }
            } else {
                throw new IllegalArgumentException("내 물건은 교환할 수 없습니다.");
            }
        }
        urGoods.changeStatus(GoodsStatus.REQUESTED);

        return new ResponseDto("교환신청이 완료되었습니다.", HttpStatus.OK.value(), "OK");
    }


    public Page<GoodsListResponseDto> allGoods(Page<Goods> goodsPage, Pageable pageable, User user) {

        List<GoodsListResponseDto> goodsResponseList = new ArrayList<>();

        for (Goods goods : goodsPage.getContent()) {
            boolean checkSameUser = Objects.equals(goods.getUser().getUserId(), user.getUserId());
            Image image = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goods.getGoodsId());
            boolean checkDibs = dibsRepository.findByUserUserIdAndGoodsGoodsId(user.getUserId(), goods.getGoodsId())
                    .isPresent();
            goodsResponseList.add(new GoodsListResponseDto(goods, image.getImageUrl(), checkDibs, checkSameUser));
        }
        return new PageResponse<>(goodsResponseList, pageable, goodsPage.getTotalElements());
    }

    private ApiResponse<PocketResponseDto> getMyGoods(Page<Goods> goodsList, User user, Pageable pageable) {
        List<PocketListResponseDto> myGoods = goodsList.stream()
                .map(goods -> {
                    long ratingPrice = (long) ratingHelper.getAvgPriceByGoodsId(goods.getGoodsId());
                    Image firstImage = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goods.getGoodsId());
                    return new PocketListResponseDto(goods, firstImage.getImageUrl(), ratingPrice);
                }).collect(Collectors.toList());

        return new ApiResponse<>(true, new PocketResponseDto(user,
                new PageImpl<>(myGoods, pageable, goodsList.getTotalElements())), null);
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
}
