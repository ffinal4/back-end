package com.example.peeppo.domain.rating.contoller;

import com.example.peeppo.domain.rating.dto.RatingResponseDto;
import com.example.peeppo.domain.rating.dto.RatingRequestDto;
import com.example.peeppo.domain.rating.service.RatingService;
import com.example.peeppo.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rating")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public ApiResponse<RatingResponseDto> randomRatingGoods() {
        return ratingService.randomRatingGoods();

    }
    @PostMapping("/{goodsId}")
    public ApiResponse<RatingResponseDto> nextRandomRatingGoods(@PathVariable Long goodsId,
                                                                @RequestBody RatingRequestDto ratingRequestDto){

        return ratingService.nextRandomRatingGoods(goodsId, ratingRequestDto);

    }

}
