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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRatingRelationRepository userRatingRelationRepository;
    private final UserHelper userHelper;

    @Transactional
    public ApiResponse<RatingResponseDto> randomRatingGoods(Long userId, UserDetailsImpl userDetails) {
        userCheck(userId, userDetails.getUser().getUserId());

        Set<Long> UserRatedGoods = userRatingRelationRepository.findUserCheckedGoodsByUserId(userId);
        Rating rating = ratingRepository.findRandomRatingWithCountLessThanOrEqual3(UserRatedGoods);

        User user = userHelper.getUser(userId);
        Goods goods = rating.getGoods();
        Image image = rating.getImage();

        userRatingRelationRepository.save(new UserRatingRelation(user, rating));

        RatingResponseDto ratingResponseDto = new RatingResponseDto(goods,
                image.getImageUrl(),
                rating.getSellerPrice(),
                0L);

        return new ApiResponse<>(true, ratingResponseDto, null);
    }

    @Transactional
    public ApiResponse<RatingResponseDto> nextRandomRatingGoods(Long userId,
                                                                RatingRequestDto ratingRequestDto,
                                                                UserDetailsImpl userDetails) {

        userCheck(userId, userDetails.getUser().getUserId());

        User user = userHelper.getUser(userId);

        // 이전 문제 가격 반영
        calculate(ratingRequestDto, user);

        // 이후 문제 생성
        Set<Long> UserRatedGoods = userRatingRelationRepository.findUserCheckedGoodsByUserId(userId);
        Rating rating = ratingRepository.findRandomRatingWithCountLessThanOrEqual3(UserRatedGoods);

        if (rating == null) {
            throw new IllegalStateException("평가 가능한 게시물이 없습니다.");
        }

        Goods goods = rating.getGoods();
        String imageUrl = rating.getImage().getImageUrl();
        Long ratingCount = ratingRequestDto.getRatingCount() + 1;
        RatingResponseDto ratingResponseDto = new RatingResponseDto(goods,
                imageUrl,
                rating.getSellerPrice(),
                ratingCount);
        return new ApiResponse<>(true, ratingResponseDto, null);
    }


    @Transactional
    public void calculate(RatingRequestDto ratingRequestDto, User user) {
        Long ratingCount = ratingRequestDto.getRatingCount() + 1;
        Long goodsId = ratingRequestDto.getGoodsId();
        Long ratingPrice = ratingRequestDto.getRatingPrice();
        Rating rating = ratingRepository.findByGoodsGoodsId(goodsId);

        userRatingRelationRepository.save(new UserRatingRelation(user, rating));

        rating.update(ratingPrice);

        Long sellerPrice = rating.getSellerPrice();
        Long lowerBound;
        Long upperBound;
        if (sellerPrice > 10000) {
            lowerBound = Math.round((sellerPrice * 0.9) / 1000.0) * 1000;
            upperBound = Math.round((sellerPrice * 1.1) / 1000.0) * 1000;
        } else {
            lowerBound = sellerPrice - 1000;
            upperBound = sellerPrice + 1000;
        }
        log.info("Lower{}", lowerBound);
        log.info("Upper{}", upperBound);
        if (ratingPrice >= lowerBound && ratingPrice <= upperBound) {   // 입력한 금액이 +- 10% 이내일 경우
            if (sellerPrice == ratingPrice) {
                userHelper.userRatingCheck(user, ratingCount, 10L);
            } else {
                userHelper.userRatingCheck(user, ratingCount, 5L);
            }
        } else {
            userHelper.userRatingCheck(user, ratingCount, 2L);
            throw new IllegalStateException("10%내외의 값이 아닙니다.");
        }
    }

    // 스케줄링
    @Transactional
    public void resetPrices() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        LocalTime time = now.toLocalTime();

        if (dayOfWeek == DayOfWeek.SUNDAY && time.isBefore(LocalTime.of(6, 0))) {
            List<Rating> ratingsToUpdate = ratingRepository.findByRatingCountGreaterThanEqual(3L);

            for (Rating rating : ratingsToUpdate) {
                Long sumPrice = rating.getSumRatingPrice();
                Long ratingCount = rating.getRatingCount();
                boolean auctionCheck = rating.getGoods().isAuctioned();

                if (ratingCount > 3 && auctionCheck == false) {
                    Long newAvgPrice = sumPrice / ratingCount;
                    rating.setAvgRatingPrice(newAvgPrice);
                }
            }
        }
    }

    public void userCheck(Long userId, Long userImplId) {
        if (!(userId == userImplId)) {
            throw new IllegalStateException("유저가 userIdUrl 을 임의로 조작");
        }
    }
}
