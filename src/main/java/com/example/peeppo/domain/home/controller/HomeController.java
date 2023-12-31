package com.example.peeppo.domain.home.controller;

import com.example.peeppo.domain.home.dto.HomeResponseDto;
import com.example.peeppo.domain.home.service.HomeService;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.example.peeppo.global.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ApiResponse<HomeResponseDto> peeppoHome(@AuthenticationPrincipal UserDetailsImpl userDetails) {
/*        if(userDetails != null){ // 로그인 한 경우
            return ResponseUtils.ok(homeService.peeppoHome(userDetails.getUser()));
        }*/
/*        // 로그인 안한 경우
        return ResponseUtils.ok(homeService.peeppoHomeEveryone());*/
        return ResponseUtils.ok(homeService.peeppoHome(userDetails));
    }
}