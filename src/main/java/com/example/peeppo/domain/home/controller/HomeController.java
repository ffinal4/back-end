package com.example.peeppo.domain.home.controller;

<<<<<<< HEAD
import com.example.peeppo.domain.home.dto.HomeResponseDto;
=======
>>>>>>> 7e6a0395fd7bd0c483a4b3cf71b1085f4ee6b0fa
import com.example.peeppo.domain.home.service.HomeService;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ApiResponse<HomeResponseDto> peeppoHome() {
        return ResponseUtils.ok(homeService.peeppoHome());

    }
}