package com.example.peeppo.domain.home.controller;

import com.example.peeppo.domain.home.service.HomeService;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/home")
    public ApiResponse<?> getHome(){
        return ResponseUtils.ok(homeService.getInfoAtHome());
    }
}
