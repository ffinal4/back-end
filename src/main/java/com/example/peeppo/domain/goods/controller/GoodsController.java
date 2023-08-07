package com.example.peeppo.domain.goods.controller;

import com.example.peeppo.domain.goods.dto.*;
import com.example.peeppo.domain.goods.service.GoodsService;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods")
public class GoodsController {
    private final GoodsService goodsService;

    @PostMapping
    public ApiResponse<GoodsResponseDto> goodsCreate(@RequestPart(value = "data") GoodsRequestDto goodsRequestDto,
                                                     @RequestPart(value = "images") List<MultipartFile> images,
                                                     @RequestPart(value = "wanted")WantedRequestDto wantedRequestDto) {

        return goodsService.goodsCreate(goodsRequestDto, images, wantedRequestDto);
    }

    // 전체 게시물 조회
    @GetMapping
    public Page<GoodsListResponseDto> allGoods(@RequestParam("page") int page,
                                               @RequestParam("size") int size,
                                               @RequestParam("sortBy") String sortBy,
                                               @RequestParam("isAsc") boolean isAsc) {

        return goodsService.allGoods(page - 1, size, sortBy, isAsc);
    }

    // 게시물 상세조회
    @GetMapping("/{goodsId}")
    public ApiResponse<GoodsResponseDto> getGoods(@PathVariable Long goodsId) {

        return goodsService.getGoods(goodsId);
    }

    // 내 게시물 조회
    @GetMapping("/{userId}/pocket")
    public ApiResponse<List<GoodsListResponseDto>> getMyGoods(@PathVariable Long userId,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size,
                                                              @RequestParam("sortBy") String sortBy,
                                                              @RequestParam("isAsc") boolean isAsc){
        return goodsService.getMyGoods(userId, page - 1, size, sortBy, isAsc);
    }


    @PutMapping("/{goodsId}")
    public ApiResponse<GoodsResponseDto> goodsUpdate(@PathVariable Long goodsId,
                                                     @RequestPart(value = "data") GoodsRequestDto requestDto,
                                                     @RequestPart(value = "images") List<MultipartFile> images,
                                                     @RequestPart(value = "wanted") WantedRequestDto wantedRequestDto) {

        return goodsService.goodsUpdate(goodsId, requestDto, images, wantedRequestDto);
    }

    @DeleteMapping("/{goodsId}")
    public ApiResponse<DeleteResponseDto> deleteGoods(@PathVariable Long goodsId) {
        return goodsService.deleteGoods(goodsId);
    }
}
