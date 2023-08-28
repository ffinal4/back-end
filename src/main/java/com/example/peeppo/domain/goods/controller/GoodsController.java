package com.example.peeppo.domain.goods.controller;

import com.example.peeppo.domain.auction.dto.TestListResponseDto;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.goods.dto.*;

import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.goods.service.GoodsService;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.example.peeppo.global.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    public ApiResponse<GoodsResponseDto> goodsCreate(@RequestPart(value = "data") GoodsRequestDto goodsRequestDto,
                                                     @RequestPart(value = "images") List<MultipartFile> images,
                                                     @RequestPart(value = "wanted") WantedRequestDto wantedRequestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return goodsService.goodsCreate(goodsRequestDto, images, wantedRequestDto, userDetails.getUser());
    }

    // 전체 게시물 조회
    @GetMapping
    public Page<GoodsListResponseDto> allGoods(@RequestParam("page") int page,
                                               @RequestParam("size") int size,
                                               @RequestParam("sortBy") String sortBy,
                                               @RequestParam("isAsc") boolean isAsc,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return goodsService.allGoods(page - 1, size, sortBy, isAsc, userDetails);
    }

    // 게시물 상세조회
    @GetMapping("/{goodsId}")
    public ApiResponse<GoodsResponseDto> getGoods(@PathVariable Long goodsId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            return goodsService.getGoods(goodsId, userDetails.getUser());
        }
        return goodsService.getGoodsEveryone(goodsId);
    }

    @GetMapping("/pocket")
    public ApiResponse<PocketResponseDto> getMyGoods(@RequestParam("page") int page,
                                                     @RequestParam("size") int size,
                                                     @RequestParam("sortBy") String sortBy,
                                                     @RequestParam("isAsc") boolean isAsc,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return goodsService.getMyGoods(page - 1, size, sortBy, isAsc, userDetails.getUser().getUserId());
    }

    @GetMapping("/mypocket")
    public ApiResponse<List<GoodsResponseDto>> getMyGoodsWithoutPagenation(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.ok(goodsService.getMyGoodsWithoutPagenation(userDetails.getUser()));
    }

    @GetMapping("/pocket/{nickname}")
    public ApiResponse<UrPocketResponseDto> getPocket(@PathVariable String nickname,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int size,
                                                      @RequestParam("sortBy") String sortBy,
                                                      @RequestParam("isAsc") boolean isAsc,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return goodsService.getPocket(nickname, userDetails, page - 1, size, sortBy, isAsc);
    }

    @PutMapping("/{goodsId}")
    public ApiResponse<GoodsResponseDto> goodsUpdate(@PathVariable Long goodsId,
                                                     @RequestPart(value = "data") GoodsRequestDto requestDto,
                                                     @RequestPart(value = "images") List<MultipartFile> images,
                                                     @RequestPart(value = "wanted") WantedRequestDto wantedRequestDto) {

        return goodsService.goodsUpdate(goodsId, requestDto, images, wantedRequestDto);
    }

    @DeleteMapping("/{goodsId}")
    public ApiResponse<DeleteResponseDto> deleteGoods(@PathVariable Long goodsId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) throws IllegalAccessException {
        return goodsService.deleteGoods(goodsId, userDetails.getUser().getUserId());
    }

    @GetMapping("/recent")
    public List<GoodsRecentDto> recentGoods(HttpServletResponse response) {
        return goodsService.recentGoods(response);
    }


    @GetMapping("/search")
    public ApiResponse<List<GoodsListResponseDto>> searchGoods(@RequestParam("keyword") String keyword) {
        return goodsService.searchGoods(keyword);
    }

    @GetMapping("/users/trade")
    public ResponseEntity<Page<GoodsListResponseDto>> goodsTradeList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @RequestParam("page") int page,
                                                                     @RequestParam("size") int size,
                                                                     @RequestParam("sortBy") String sortBy,
                                                                     @RequestParam("isAsc") boolean isAsc,
                                                                     @RequestParam(value = "status", required = false) GoodsStatus goodsStatus) {

        return goodsService.goodsTradeList(userDetails.getUser(), page - 1, size, sortBy, isAsc, goodsStatus);
    }

    //교환신청
    @PostMapping("/users/{goodsId}/request")
    public ApiResponse<ResponseDto> goodsRequest(@PathVariable Long goodsId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody GoodsRequestRequestDto goodsRequestRequestDto) {

        return ResponseUtils.ok(goodsService.goodsRequest(userDetails.getUser(), goodsRequestRequestDto, goodsId));
    }
}

