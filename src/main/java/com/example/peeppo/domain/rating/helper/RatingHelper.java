package com.example.peeppo.domain.rating.helper;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.rating.repository.ratingRepository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingHelper {
    private final RatingRepository ratingRepository;

    @Transactional
    public void createRating(Long sellerPrice, Goods goods, Image image) {
       Rating rating = new Rating(sellerPrice, goods, image);
       ratingRepository.save(rating);

    }
}
