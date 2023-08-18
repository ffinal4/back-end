package com.example.peeppo.domain.dibs.controller;

import com.example.peeppo.domain.dibs.dto.DibsRequestDto;
import com.example.peeppo.domain.dibs.service.DibsService;
import com.example.peeppo.domain.goods.dto.GoodsListResponseDto;
import com.example.peeppo.domain.user.dto.CheckResponseDto;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.example.peeppo.global.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DibsController {

    private final DibsService dibsService;

    @PostMapping("/dibs")
    public ResponseEntity<CheckResponseDto> dibsGoods(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @Valid @RequestBody DibsRequestDto dibsRequestDto) {
        return dibsService.dibsGoods(userDetails, dibsRequestDto);
    }

    @GetMapping("/users/wishlist")
    public ApiResponse<List<GoodsListResponseDto>> getDibsGoods(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseUtils.ok(dibsService.getDibsGoods(userDetails.getUser()));

    }
}
