package com.example.peeppo.domain.goods.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.example.peeppo.domain.dibs.repository.DibsRepository;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.goods.dto.*;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.WantedGoods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.goods.repository.WantedGoodsRepository;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.helper.ImageHelper;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.rating.helper.RatingHelper;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.responseDto.PageResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final ImageRepository imageRepository;
    private final WantedGoodsRepository wantedGoodsRepository;
    private final ImageHelper imageHelper;
    private final AmazonS3 amazonS3;
    private final String bucket;
    private final UserRepository userRepository;

    private final RatingHelper ratingHelper;

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
        WantedGoods wantedGoods = new WantedGoods(wantedRequestDto);
        Goods goods = new Goods(goodsRequestDto, wantedGoods, user, GoodsStatus.ONSALE);
        goodsRepository.save(goods);

        wantedGoodsRepository.save(wantedGoods);

        List<String> imageUuids = imageHelper
                .saveImagesToS3AndRepository(images, amazonS3, bucket, goods)
                .stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

//        ratingHelper.createRating(sellerPriceRequestDto.getSellerPrice(), goods, image);

        return new ApiResponse<>(true, new GoodsResponseDto(goods, imageUuids, wantedGoods, user), null);
    }

    @CachePut(key = "#page", value = "allGoods")
    @Cacheable(key = "#page", value = "allGoods", condition = "#page == 0", cacheManager = "cacheManager")
    public Page<GoodsListResponseDto> allGoods(int page, int size, String sortBy, boolean isAsc) {

        Pageable pageable = paging(page, size, sortBy, isAsc);
        Page<Goods> goodsPage = goodsRepository.findAllByIsDeletedFalse(pageable);
        List<GoodsListResponseDto> goodsResponseList = new ArrayList<>();

        for (Goods goods : goodsPage.getContent()) {
            List<Image> images = imageRepository.findByGoodsGoodsId(goods.getGoodsId());
            List<String> imageUrls = new ArrayList<>();
            for (Image image : images) {
                imageUrls.add(image.getImageUrl());
            }
            goodsResponseList.add(new GoodsListResponseDto(goods, imageUrls.get(0)));
        }

        return new PageResponse<>(goodsResponseList, pageable, goodsPage.getTotalElements());
    }

//    public ApiResponse<List<GoodsResponseDto>> locationAllGoods(Long locationId) {
//        List<Goods> goodsList = goodsRepository.findAllByLocationIdAndIsDeletedFalseOrderByGoodsIdDesc(locationId);
//        List<GoodsResponseDto> goodsResponseList = responseDtoList(goodsList);
//
//        return new ApiResponse<>(true, goodsResponseList, null);
//    }


    public ApiResponse<GoodsResponseDto> getGoods(Long goodsId, User user) {

        Goods goods = findGoods(goodsId);
        boolean checkSameUser = true;
        if(goods.getUser().getUserId() != user.getUserId()){
            checkSameUser = false;
        }
        WantedGoods wantedGoods = findWantedGoods(goodsId);
        List<Image> images = imageRepository.findByGoodsGoodsId(goodsId);
        List<String> imageUrls = images.stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
        if (goodsRecent.size() >= MAX_RECENT_GOODS) {
            goodsRecent.remove(0);
        }
        goodsRecent.add(Long.toString(goods.getGoodsId())); // 조회시에 리스트에 추가 !

        return new ApiResponse<>(true, new GoodsResponseDto(goods, imageUrls, wantedGoods, checkSameUser), null);
    }

    public User findUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }


    public ApiResponse<PocketResponseDto> getMyGoods(int page,
                                                     int size,
                                                     String sortBy,
                                                     boolean isAsc,
                                                     Long userId) {

        Pageable pageable = paging(page, size, sortBy, isAsc);
        User user = findUserId(userId);
        Page<Goods> goodsList = goodsRepository.findAllByUserAndIsDeletedFalse(user, pageable);

        if(goodsList.isEmpty()){
            return  new ApiResponse<>(true, new PocketResponseDto(), null);
        }

        List<GoodsListResponseDto> myGoods = new ArrayList<>();
        for (Goods goods : goodsList) {
            Image firstImage = imageRepository.findFirstByGoodsGoodsIdOrderByCreatedAtAsc(goods.getGoodsId());
            myGoods.add(new GoodsListResponseDto(goods, firstImage.getImageUrl()));
        }

        return new ApiResponse<>(true, new PocketResponseDto(user, myGoods), null);
    }

    @Transactional
    public ApiResponse<GoodsResponseDto> goodsUpdate(Long goodsId, GoodsRequestDto goodsRequestDto, List<MultipartFile> images, WantedRequestDto wantedRequestDto) {
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
        if (userId == goods.getUser().getUserId()) {
            goods.delete();
            goodsRepository.save(goods);
        } else {
            throw new IllegalAccessException();
        }
        return new ApiResponse<>(true, new DeleteResponseDto("삭제되었습니다"), null);
    }


        // 최근 본 목록에 상품 ID를 추가
//        List<Long> recentlyViewedGoodsIds = user.getRecentlyViewedGoodsIds();
//        if (!recentlyViewedGoodsIds.contains(goodsId)) {
//            recentlyViewedGoodsIds.add(goodsId);
//            user.setRecentlyViewedGoodsIds(recentlyViewedGoodsIds);
//            userRepository.save(user);
//        }

    public Goods findGoods(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() ->
                new NullPointerException("해당 게시글은 존재하지 않습니다."));
        if (goods.isDeleted()) {
            throw new IllegalStateException("삭제된 게시글입니다.");
        }
        return goods;
    }

    public WantedGoods findWantedGoods(Long wantedId) {
        WantedGoods wantedGoods = wantedGoodsRepository.findById(wantedId).orElseThrow(() ->
                new NullPointerException("해당 게시글은 존재하지 않습니다."));
        return wantedGoods;
    }


    private Pageable paging(int page, int size, String sortBy, boolean isAsc) {
        // 정렬
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // pageable 생성
        return PageRequest.of(page, size, sort);
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

    public List<GoodsResponseDto> getPocket(String nickname, User user1) throws IllegalAccessException {
        User user = userRepository.findUserByNickname(nickname);
        if(user.equals(user1)){
            throw new IllegalAccessException();
        }
        return getGoodsResponseDtos(user);
    }

    private List<GoodsResponseDto> getGoodsResponseDtos(User user) {
        List<Goods> goodsList = goodsRepository.findAllByUserAndIsDeletedFalseAndGoodsStatus(user, GoodsStatus.ONSALE);
        List<GoodsResponseDto> goodsResponseDtos = new ArrayList<>();
        for(Goods goods : goodsList){
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto(goods);
            goodsResponseDtos.add(goodsResponseDto);
        }
        return goodsResponseDtos;
    }
}
