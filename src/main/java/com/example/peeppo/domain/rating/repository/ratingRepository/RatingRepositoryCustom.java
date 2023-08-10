package com.example.peeppo.domain.rating.repository.ratingRepository;

import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.rating.entity.UserRatingRelation;

import java.util.Set;

public interface RatingRepositoryCustom {

    Rating findRandomRatingWithCountLessThanOrEqual7(Set<Long> UserRatedGoods);

    Rating goodsWithDifferentId(Long goodsId);
}
