package com.example.peeppo.domain.rating.repository;

import com.example.peeppo.domain.rating.entity.Rating;

public interface RatingRepositoryCustom {
    Rating findRandomRatingWithCountLessThanOrEqual7();

    Rating goodsWithDifferentId(Long goodsId);
}
