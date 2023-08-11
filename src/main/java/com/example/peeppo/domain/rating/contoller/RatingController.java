package com.example.peeppo.domain.rating.contoller;

import com.example.peeppo.domain.rating.dto.RatingResponseDto;
import com.example.peeppo.domain.rating.dto.RatingRequestDto;
import com.example.peeppo.domain.rating.service.RatingService;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/{userId}/rating")
public class RatingController {
    private final RatingService ratingService;


//    @PostMapping("/ratinglist")

    @PostMapping
    public ApiResponse<RatingResponseDto> nextRandomRatingGoods(@PathVariable(value = "userId")  Long userId,
                                                                @RequestBody RatingRequestDto ratingRequestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){

        return ratingService.nextRandomRatingGoods(userId, ratingRequestDto, userDetails);

    }

//    @PostMapping("/")

    @GetMapping("/ratinglist")
    public ApiResponse<List<RatingResponseDto>> firstRatingList(@PathVariable Long userId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){

        return ratingService.randomRatingList(userId, userDetails);
    }

    @GetMapping
    public ApiResponse<RatingResponseDto> randomRatingGoods(@PathVariable Long userId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ratingService.randomRatingGoods(userId, userDetails);

    }

}
