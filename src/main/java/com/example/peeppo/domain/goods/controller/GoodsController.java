package com.example.peeppo.domain.goods.controller;

import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.dto.GoodsRequestDto;
import com.example.peeppo.domain.goods.service.GoodsService;
import com.example.peeppo.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods")
public class GoodsController {
    private final GoodsService goodsService;

    @PostMapping
    public ApiResponse<GoodsResponseDto> goodsCreate(@RequestBody GoodsRequestDto reqeustDto) {

        return goodsService.goodsCreate(reqeustDto);

    }

    // 상세조회가 아니므로 출력값을 변경해야 함
    // image, username, title, content
    @GetMapping
    public ApiResponse<List<GoodsResponseDto>> allGoods() {

        return goodsService.allGoods();
    }

    // 상세조회가 아니므로 출력값을 변경해야 함
    @GetMapping("/location/{locationId}")
    public ApiResponse<List<GoodsResponseDto>> locationAllGoods(@PathVariable Long locationId) {

        return goodsService.locationAllGoods(locationId);
    }

    @GetMapping("/{goodsId}")
    public ApiResponse<GoodsResponseDto> getGoods(@PathVariable Long goodsId) {

        return goodsService.getGoods(goodsId);
    }

    @PatchMapping("/{goodsId}")
    public ApiResponse<GoodsResponseDto> goodsUpdate(@PathVariable Long goodsId,
    @RequestBody GoodsRequestDto requestDto) {

        return goodsService.goodsUpdate(goodsId, requestDto);
    }
}
