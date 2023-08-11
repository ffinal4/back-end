package com.example.peeppo.domain.rating.service;

import com.example.peeppo.domain.goods.entity.Goods;
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
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRatingRelationRepository userRatingRelationRepository;
    private final UserHelper userHelper;

    public ApiResponse<List<RatingResponseDto>> randomRatingList(Long userId, UserDetailsImpl userDetails) {
        userCheck(userId, userDetails.getUser().getUserId());

        List<Rating> ratingList = ratingRepository.getRandomRatingsFromRatingsWithCountLessThanOrEqual7(userId);

        List<RatingResponseDto> ratingResponseDtoList = new ArrayList<>();
        for (Rating rating : ratingList) {
            Goods goods = rating.getGoods();
            Image image = rating.getImage();
            RatingResponseDto responseDto = new RatingResponseDto(goods, image.getImageUrl(), rating.getRatingCount());

            ratingResponseDtoList.add(responseDto);
        }
        return new ApiResponse<>(true, ratingResponseDtoList, null);
    }

    @Transactional
    public ApiResponse<RatingResponseDto> randomRatingGoods(Long userId, UserDetailsImpl userDetails) {
        userCheck(userId, userDetails.getUser().getUserId());

        Set<Long> UserRatedGoods = userRatingRelationRepository.findUserCheckedGoodsByUserId(userId);
        Rating rating = ratingRepository.findRandomRatingWithCountLessThanOrEqual7(UserRatedGoods, userId, 0);

        User user = userHelper.getUser(userId);
        Goods goods = rating.getGoods();
        Image image = rating.getImage();

        userRatingRelationRepository.save(new UserRatingRelation(user, rating));

        RatingResponseDto ratingResponseDto = new RatingResponseDto(goods, image.getImageUrl(), 0L);

        return new ApiResponse<>(true, ratingResponseDto, null);
    }

    @Transactional
    public ApiResponse<RatingResponseDto> nextRandomRatingGoods(Long userId,
                                                                RatingRequestDto ratingRequestDto,
                                                                UserDetailsImpl userDetails) {

        userCheck(userId, userDetails.getUser().getUserId());

        // 이전 문제 가격 반영
        calculate(ratingRequestDto, userId);

        // 이후 문제 생성
        Set<Long> UserRatedGoods = userRatingRelationRepository.findUserCheckedGoodsByUserId(userId);
        Rating rating = ratingRepository.findRandomRatingWithCountLessThanOrEqual7(UserRatedGoods, userId, 0);

        User user = userHelper.getUser(userId);
        UserRatingRelation userRatingRelation = new UserRatingRelation(user, rating);
        userRatingRelationRepository.save(userRatingRelation);

        Goods goods = rating.getGoods();
        String imageUrl = rating.getImage().getImageUrl();
        Long ratingCount = ratingRequestDto.getRatingCount()+1;
        RatingResponseDto ratingResponseDto = new RatingResponseDto(goods, imageUrl, ratingCount);
        return new ApiResponse<>(true, ratingResponseDto, null);
    }


    @Transactional
    public void calculate(RatingRequestDto ratingRequestDto, Long userId) {
        Long ratingCount = ratingRequestDto.getRatingCount();
        Long goodsId = ratingRequestDto.getGoodsId();
        Long ratingPrice = ratingRequestDto.getRatingPrice();
        Rating rating = ratingRepository.findByGoodsGoodsId(goodsId);
        rating.update(ratingPrice);

        Long sellerPrice = rating.getSellerPrice();
        long lowerBound = (long) (sellerPrice * 0.9);
        long upperBound = (long) (sellerPrice * 1.1);
        if (ratingPrice >= lowerBound && ratingPrice <= upperBound) {   // 입력한 금액이 +- 10% 이내일 경우
            if (sellerPrice == ratingPrice) {
                // 입력한 금액이 정확히 일치할 경우 기능 추가구현
            }
            User user = userHelper.getUser(userId);
            userHelper.userRatingCheck(user, ratingCount+1);
        }
        else{
            throw new IllegalStateException("10%이내의 값이 아닙니다.");
        }
    }

    public void userCheck(Long userId, Long userImplId){
        if(!(userId == userImplId)){
            throw new IllegalStateException("유저가 userIdUrl 을 임의로 조작");
        }
    }

}
