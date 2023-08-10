package com.example.peeppo.domain.rating.service;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.helper.GoodsHelper;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.rating.dto.RatingRequestDto;
import com.example.peeppo.domain.rating.dto.RatingResponseDto;
import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.rating.entity.UserRatingRelation;
import com.example.peeppo.domain.rating.repository.ratingRepository.RatingRepository;
import com.example.peeppo.domain.rating.repository.userRatingRepository.UserRatingRelationRepository;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.helper.UserHelper;
import com.example.peeppo.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRatingRelationRepository userRatingRelationRepository;
    private final UserHelper userHelper;

    @Transactional
    public ApiResponse<RatingResponseDto> randomRatingGoods(Long userId) {

        Set<Long> UserRatedGoods = userRatingRelationRepository.findUserCheckedGoodsByUserId(userId);
        Rating rating = ratingRepository.findRandomRatingWithCountLessThanOrEqual7(UserRatedGoods);
        User user = userHelper.getUser(userId);

        Goods goods = rating.getGoods();
        Image image = rating.getImage();
        userRatingRelationRepository.save(new UserRatingRelation(user, rating));

        return new ApiResponse<>(true, new RatingResponseDto(goods, image.getImageUrl()), null);
    }

    @Transactional
    public ApiResponse<RatingResponseDto> nextRandomRatingGoods(Long userId,
                                                                Long goodsId,
                                                                RatingRequestDto ratingRequestDto) {
        calculate(goodsId, ratingRequestDto.getRatingPrice(), userId);
        Set<Long> UserRatedGoods = userRatingRelationRepository.findUserCheckedGoodsByUserId(userId);
        Rating rating = ratingRepository.findRandomRatingWithCountLessThanOrEqual7(UserRatedGoods);
        Goods goods = rating.getGoods();
        Image image = rating.getImage();
        return new ApiResponse<>(true, new RatingResponseDto(goods, image.getImageUrl()), null);
    }

    @Transactional
    public void calculate(Long GoodsId, Long ratingPrice, Long userId) {
        Rating rating = ratingRepository.findByGoodsGoodsId(GoodsId);
        rating.update(ratingPrice);

        Long sellerPrice = rating.getSellerPrice();
        long lowerBound = (long) (sellerPrice * 0.9);
        long upperBound = (long) (sellerPrice * 1.1);
        if (ratingPrice >= lowerBound && ratingPrice <= upperBound) {   // 입력한 금액이 +- 10% 이내일 경우
            if (sellerPrice == ratingPrice) {
                // 입력한 금액이 정확히 일치할 경우 기능 추가구현
            }
            User user = userHelper.getUser(userId);
            userHelper.userRatingCheck(user);
        }
    }
}
