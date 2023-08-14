package com.example.peeppo.domain.rating.repository.ratingRepository;

import com.example.peeppo.domain.rating.entity.Rating;

import java.util.List;
import java.util.Set;

public interface RatingRepositoryCustom {

    Rating findRandomRatingWithCountLessThanOrEqual3(Set<Long> UserRatedGoods);

    List<Rating> findByRatingCountGreaterThanEqual(Long ratingCount);

}
