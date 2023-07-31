package com.example.peeppo.domain.goods.controller;

import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.dto.goodsRequestDto;
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
    public ApiResponse<GoodsResponseDto> goodsCreate(@RequestBody goodsRequestDto reqeustDto){
        return goodsService.goodsCreate(reqeustDto);

    }
    @GetMapping
    public ApiResponse<List<GoodsResponseDto>> allGoods(){
        return goodsService.allGoods();
    }
}
