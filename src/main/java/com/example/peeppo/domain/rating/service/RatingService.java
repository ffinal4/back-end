package com.example.peeppo.domain.rating.service;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.rating.dto.RatingRequestDto;
import com.example.peeppo.domain.rating.dto.RatingResponseDto;
import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.rating.repository.RatingRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    @Transactional
    public ApiResponse<RatingResponseDto> randomRatingGoods() {
        Rating rating = ratingRepository.findRandomRatingWithCountLessThanOrEqual7();
        Goods goods = rating.getGoods();
        Image image = rating.getImage();
        return new ApiResponse<>(true, new RatingResponseDto(goods, image.getImageUrl()), null);
    }

    @Transactional
    public ApiResponse<RatingResponseDto> nextRandomRatingGoods(Long goodsId,
                                                                RatingRequestDto ratingRequestDto) {
        calculate(goodsId, ratingRequestDto.getRatingPrice());
        Rating rating = ratingRepository.goodsWithDifferentId(goodsId);
        Goods goods = rating.getGoods();
        Image image = rating.getImage();
        return new ApiResponse<>(true, new RatingResponseDto(goods, image.getImageUrl()), null);
    }

    @Transactional
    public void calculate(Long GoodsId, Long ratingPrice) {
        Rating rating = ratingRepository.findByGoodsGoodsId(GoodsId);
        rating.update(ratingPrice);
    }
}
