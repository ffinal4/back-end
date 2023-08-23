package com.example.peeppo.domain.rating.contoller;

import com.example.peeppo.domain.rating.dto.RatingResponseDto;
import com.example.peeppo.domain.rating.dto.RatingRequestDto;
import com.example.peeppo.domain.rating.dto.RatingScoreResponseDto;
import com.example.peeppo.domain.rating.dto.TopRatingUserResponseDto;
import com.example.peeppo.domain.rating.service.RatingService;
import com.example.peeppo.domain.user.dto.RatingUserResponseDto;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class RatingController {
    private final RatingService ratingService;


    @GetMapping("/main")
    public ApiResponse<List<RatingUserResponseDto>> ratingTopFiveUsers(){
        return ratingService.ratingTopFiveUsers();
    }
    @GetMapping
    public ApiResponse<RatingResponseDto> randomRatingGoods(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ratingService.randomRatingGoods(userDetails.getUser().getUserId());
    }

    @PostMapping
    public ApiResponse<RatingScoreResponseDto> randomRatingGoods(@RequestBody RatingRequestDto ratingRequestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ratingService.randomRatingGoods(ratingRequestDto, userDetails.getUser().getUserId());
    }

}
