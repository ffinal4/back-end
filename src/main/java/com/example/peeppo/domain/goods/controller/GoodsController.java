package com.example.peeppo.domain.goods.controller;

import com.example.peeppo.domain.goods.dto.DeleteResponseDto;
import com.example.peeppo.domain.goods.dto.GoodsRequestDto;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.service.GoodsService;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods")
public class GoodsController {
    private final GoodsService goodsService;

    @PostMapping
    public ApiResponse<GoodsResponseDto> goodsCreate(@RequestPart(value = "data") GoodsRequestDto requestDto,
                                                     @RequestPart(value = "images") List<MultipartFile> images,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return goodsService.goodsCreate(requestDto, images, userDetails.getUser());
    }

    @GetMapping
    public Page<GoodsResponseDto> allGoods(@RequestParam("page") int page,
                                           @RequestParam("size") int size,
                                           @RequestParam("sortBy") String sortBy) {

        return goodsService.allGoods(page - 1, size, sortBy);
    }

    @GetMapping("/{goodsId}")
    public ApiResponse<GoodsResponseDto> getGoods(@PathVariable Long goodsId) {

        return goodsService.getGoods(goodsId);
    }

    @PatchMapping("/{goodsId}")
    public ApiResponse<GoodsResponseDto> goodsUpdate(@PathVariable Long goodsId,
                                                     @RequestPart(value = "data") GoodsRequestDto requestDto,
                                                     @RequestPart(value = "images") List<MultipartFile> images) {

        return goodsService.goodsUpdate(goodsId, requestDto, images);
    }

    @DeleteMapping("/{goodsId}")
    public ApiResponse<DeleteResponseDto> deleteGoods(@PathVariable Long goodsId) {
        return goodsService.deleteGoods(goodsId);
    }
}
