package com.example.peeppo.domain.rating.service;

import com.example.peeppo.domain.goods.helper.GoodsHelper;
import com.example.peeppo.domain.image.helper.ImageHelper;
import com.example.peeppo.domain.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final GoodsHelper goodsHelper;
    private final ImageHelper imageHelper;
    private final RatingRepository ratingRepository;

//    @Transactional(readOnly = true)
//    public ApiResponse<> RandomRatingGoods() {
//        Rating rating = ratingRepository.findRandomRatingWithCountLessThanOrEqual7();
//        Long ratingId = rating.getRatingId();
//        Goods goods = goodsHelper.findGoods();
//    }
}
