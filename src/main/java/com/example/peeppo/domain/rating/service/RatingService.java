package com.example.peeppo.domain.rating.service;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.helper.GoodsHelper;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.helper.ImageHelper;
import com.example.peeppo.domain.rating.dto.RatingResponseDto;
import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.rating.repository.RatingRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    @Transactional
    public ApiResponse<RatingResponseDto> RandomRatingGoods() {
        Rating rating = ratingRepository.findRandomRatingWithCountLessThanOrEqual7();
        Goods goods = rating.getGoods();
        Image image = rating.getImage();
        String title = goods.getTitle();
        String content = goods.getContent();
        String imageUrl = image.getImageUrl();
        return new ApiResponse<>(true, new RatingResponseDto(title, content, imageUrl), null);
    }
}
