package com.example.peeppo.domain.goods.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
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
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.global.responseDto.PageResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final WantedGoodsRepository wantedGoodsRepository;
    private final ImageHelper imageHelper;
    private final RatingHelper ratingHelper;
    private final AmazonS3 amazonS3;
    private final String bucket;

    @Transactional
    public ApiResponse<GoodsResponseDto> goodsCreate(GoodsRequestDto goodsRequestDto,
                                                     List<MultipartFile> images,
                                                     WantedRequestDto wantedRequestDto,
                                                     SellerPriceRequestDto sellerPriceRequestDto) {
        WantedGoods wantedGoods = new WantedGoods(wantedRequestDto);
        Goods goods = new Goods(goodsRequestDto, wantedGoods);
        goodsRepository.save(goods);

        wantedGoodsRepository.save(wantedGoods);

        List<String> imageUuids = imageHelper
                .saveImagesToS3AndRepository(images, amazonS3, bucket, goods)
                .stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        Image image = imageHelper.getImage(imageUuids.get(0));
        ratingHelper.createRating(sellerPriceRequestDto.getSellerPrice(), goods, image);

        GoodsResponseDto responseDto = new GoodsResponseDto(goods, imageUuids, wantedGoods, sellerPriceRequestDto);
        return new ApiResponse<>(true,responseDto , null);
    }

    @CachePut(key = "#page", value = "allGoods")
    @Cacheable(key = "#page", value = "allGoods", condition = "#page == 0", cacheManager = "cacheManager")
    public Page<GoodsListResponseDto> allGoods(int page, int size, String sortBy, boolean isAsc) {
        // 페이징 되어있는 걸 쿼리DSL 사용

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


    public ApiResponse<GoodsResponseDto> getGoods(Long goodsId) {

        Goods goods = findGoods(goodsId);
        WantedGoods wantedGoods = findWantedGoods(goodsId);
        List<Image> images = imageRepository.findByGoodsGoodsId(goodsId);
        List<String> imageUrls = images.stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, new GoodsResponseDto(goods, imageUrls, wantedGoods), null);
    }

    public ApiResponse<List<GoodsListResponseDto>> getMyGoods(Long userId, int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = paging(page, size, sortBy, isAsc);
        Page<Goods> goodsList = goodsRepository.findAllByUserIdAndIsDeletedFalse(userId, pageable);
        List<GoodsListResponseDto> myGoods = new ArrayList<>();
        for (Goods goods : goodsList) {
            Image firstImage = imageRepository.findFirstByGoodsGoodsIdOrderByCreatedAtAsc(goods.getGoodsId());
            myGoods.add(new GoodsListResponseDto(goods, firstImage.getImageUrl()));
        }

        return new ApiResponse<>(true, myGoods, null);
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
    public ApiResponse<DeleteResponseDto> deleteGoods(Long goodsId) {
        Goods goods = findGoods(goodsId);
        goods.setDeleted(true);
        goodsRepository.save(goods);

        return new ApiResponse<>(true, new DeleteResponseDto("삭제되었습니다"), null);
    }

    public Goods getGoodsById(Long goodsId, Long userId) {
        // 상품 조회
        Goods goods = goodsRepository.findById(goodsId).orElse(null);
        if (goods == null) {
            throw new NotFoundException("존재하지 않는 상품입니다.");
        }

        // 사용자 조회
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            // 사용자가 존재하지 않을 경우 예외 처리
            throw new NotFoundException("User not found");
        }

        // 최근 본 목록에 상품 ID를 추가
//        List<Long> recentlyViewedGoodsIds = user.getRecentlyViewedGoodsIds();
//        if (!recentlyViewedGoodsIds.contains(goodsId)) {
//            recentlyViewedGoodsIds.add(goodsId);
//            user.setRecentlyViewedGoodsIds(recentlyViewedGoodsIds);
//            userRepository.save(user);
//        }

        return goods;
    }

    public Goods findGoods(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() ->
                new NullPointerException("해당 게시글은 존재하지 않습니다."));
        if (goods.isDeleted()) {
            throw new IllegalStateException("삭제된 게시글입니다.");
        }
        return goods;
    }

    public WantedGoods findWantedGoods(Long wantedId) {
        return wantedGoodsRepository.findById(wantedId).orElseThrow(() ->
                new NullPointerException("해당 게시글은 존재하지 않습니다."));
    }


    private Pageable paging(int page, int size, String sortBy, boolean isAsc) {
        // 정렬
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // pageable 생성
        return PageRequest.of(page, size, sort);
    }
}
