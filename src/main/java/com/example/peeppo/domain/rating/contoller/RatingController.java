package com.example.peeppo.domain.rating.contoller;

import com.example.peeppo.domain.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rating")
public class RatingController {
    private final RatingService ratingService;
//    @GetMapping
//    public ApiResponse<GoodsResponseDto> RandomRatingGoods(){
//        return ratingService.RandomRatingGoods();
//
//    }
}
