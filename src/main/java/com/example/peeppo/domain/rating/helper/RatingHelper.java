package com.example.peeppo.domain.rating.helper;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.rating.entity.RatingGoods;
import com.example.peeppo.domain.rating.entity.UserRatingRelation;
import com.example.peeppo.domain.rating.repository.ratingGoodsRepository.RatingGoodsRepository;
import com.example.peeppo.domain.rating.repository.ratingRepository.RatingRepository;
import com.example.peeppo.domain.rating.repository.userRatingRepository.UserRatingRelationRepository;
import com.example.peeppo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RatingHelper {
    private final RatingRepository ratingRepository;
    private final UserRatingRelationRepository userRatingRelationRepository;
    private final RatingGoodsRepository ratingGoodsRepository;

    @Transactional
    public List<Long> isCorrectedAndUpdateGoodsRating(User user, Goods goods, Long expectedPrice) {

        List<Long> currentPointAndCurrentCount = new ArrayList<>(2);

        RatingGoods ratingGoods = ratingGoodsRepository.findByGoodsForUpdate(goods.getGoodsId())
                .orElse(new RatingGoods());
        ratingGoods.update(expectedPrice, goods);
        ratingGoodsRepository.save(ratingGoods);

        // 레이팅 생성
        Rating rating = new Rating(expectedPrice, ratingGoods);
        ratingRepository.save(rating);

        UserRatingRelation userRatingRelation = new UserRatingRelation(user, rating);
        userRatingRelationRepository.save(userRatingRelation);

        Long currentPoint = calculate(expectedPrice, goods.getSellerPrice());
        Long currentCount = currentPoint != 2 ? user.getCurrentRatingCount() + 1 : 0;
        currentPointAndCurrentCount.add(currentPoint);
        currentPointAndCurrentCount.add(currentCount);


        user.countUpdate(currentCount, currentPoint);
        return currentPointAndCurrentCount;
    }

    public long calculate(Long expectedPrice, Long sellerPrice) {
        long score = 2L;

        long lowerBound;
        long upperBound;
        if (sellerPrice > 10000) {
            lowerBound = Math.round((sellerPrice * 0.9) / 1000.0) * 1000;
            upperBound = Math.round((sellerPrice * 1.1) / 1000.0) * 1000;
        } else {
            lowerBound = sellerPrice - 1000;
            upperBound = sellerPrice + 1000;
        }

        if (expectedPrice >= lowerBound && expectedPrice <= upperBound) {   // 입력한 금액이 +- 10% 이내일 경우
            if (sellerPrice == expectedPrice) {
                score = 10L;
            } else {
                score = 5L;
            }
        }

        return score;
    }

    @Transactional
    public void resetGoodsAvgPrice(Goods goods) {
        RatingGoods ratingGoods = ratingGoodsRepository.findByGoodsForUpdate(goods.getGoodsId())
                .orElse(null);
        if (ratingGoods == null) {
            return;
        }

        if (ratingGoods.getRatingCount() > 3
                && !ratingGoods.getAvgRatingPrice().equals(ratingGoods.getNextAvgRatingPrice())) {
            ratingGoods.setAvgRatingPrice(ratingGoods.getNextAvgRatingPrice());
            ratingGoodsRepository.save(ratingGoods);
        }

    }
}
