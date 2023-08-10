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

    @GetMapping("/user/{userId}")
    public ApiResponse<RatingResponseDto> randomRatingGoods(@PathVariable Long userId) {
        return ratingService.randomRatingGoods(userId);

    }
    @PostMapping("/user/{userId}/goods/{goodsId}")
    public ApiResponse<RatingResponseDto> nextRandomRatingGoods(@PathVariable(value = "userId")  Long userId,
                                                                @PathVariable(value = "goodsId") Long goodsId,
                                                                @RequestBody RatingRequestDto ratingRequestDto){

        return ratingService.nextRandomRatingGoods(userId, goodsId, ratingRequestDto);

    }

}
