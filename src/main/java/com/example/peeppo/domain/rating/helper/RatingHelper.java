package com.example.peeppo.domain.rating.helper;

import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingHelper {
    private final RatingRepository ratingRepository;

    @Transactional
    public void createRating(Long sellerPrice) {
       Rating rating = new Rating(sellerPrice);
       ratingRepository.save(rating);

    }
}