package com.example.peeppo.domain.auction.controller;


import com.example.peeppo.domain.auction.dto.AuctionListResponseDto;
import com.example.peeppo.domain.auction.dto.AuctionRequestDto;
import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.auction.dto.TestListResponseDto;
import com.example.peeppo.domain.auction.service.AuctionService;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.example.peeppo.global.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.peeppo.global.stringCode.SuccessCodeEnum.AUCTION_DELETE_SUCCESS;
import static com.example.peeppo.global.stringCode.SuccessCodeEnum.AUCTION_END_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/{goodsId}")
    public ApiResponse<AuctionResponseDto> createAuction(@PathVariable("goodsId") Long goodsId,
                                                         @RequestBody AuctionRequestDto auctionRequestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.ok(auctionService.createAuction(goodsId, auctionRequestDto, userDetails.getUser()));
    }

    @GetMapping
    public ApiResponse<Page<AuctionListResponseDto>> allAuction(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("sortBy") String sortBy,
                                                                @RequestParam("isAsc") boolean isAsc) {

        return ResponseUtils.ok(auctionService.findAllAuction(page - 1, size, sortBy, isAsc));
    }

    @GetMapping("/{auctionId}")
    public ApiResponse<AuctionResponseDto> getAuction(@PathVariable("auctionId") Long auctionId) {
        return ResponseUtils.ok(auctionService.findAuctionById(auctionId));
    }
  
    // 경매 정상 종료 -> 우선 입찰 물품 전체 보여주기 -> 입찰 물품 선택
    @DeleteMapping("/{auctionId}/pick/{bidId}")
    public ApiResponse<?> endAuction(@PathVariable("auctionId") Long auctionId,
                                     @PathVariable("bidId") Long bidId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        auctionService.endAuction(auctionId, bidId, userDetails.getUser());
      
        return ResponseUtils.ok(ResponseUtils.okWithMessage(AUCTION_END_SUCCESS));
    }

    // 경매 비정상 종료 ( 입찰 취소 ) -> 여기에 포인트 차감 로직 추가 필요
    @DeleteMapping("/{auctionId}")
    public ApiResponse<?> deleteAuction(@PathVariable("auctionId") Long auctionId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        auctionService.deleteAuction(auctionId, userDetails.getUser());
        return ResponseUtils.okWithMessage(AUCTION_DELETE_SUCCESS);
    }

    @GetMapping("/users/trade/list")
    public ResponseEntity<Page<TestListResponseDto>> auctionTradeList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      @RequestParam("page") int page,
                                                                      @RequestParam("size") int size,
                                                                      @RequestParam("sortBy") String sortBy,
                                                                      @RequestParam("isAsc") boolean isAsc) {

        return auctionService.auctionTradeList(userDetails.getUser(), page - 1, size, sortBy, isAsc);
    }

}