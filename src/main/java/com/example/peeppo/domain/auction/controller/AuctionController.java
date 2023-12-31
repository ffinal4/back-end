package com.example.peeppo.domain.auction.controller;


import com.amazonaws.Response;
import com.example.peeppo.domain.auction.dto.*;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.dto.AuctionListResponseDto;
import com.example.peeppo.domain.auction.dto.AuctionRequestDto;
import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.auction.dto.TestListResponseDto;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.auction.service.AuctionService;
import com.example.peeppo.domain.bid.dto.ChoiceRequestDto;
import com.example.peeppo.domain.bid.enums.BidStatus;
import com.example.peeppo.domain.goods.dto.RequestAcceptRequestDto;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.example.peeppo.global.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.peeppo.global.stringCode.SuccessCodeEnum.AUCTION_DELETE_SUCCESS;
import static com.example.peeppo.global.stringCode.SuccessCodeEnum.AUCTION_END_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/{goodsId}")
    public ApiResponse<AuctionResponseDto> createAuction(@PathVariable("goodsId") Long goodsId,
                                                         @Valid @RequestBody AuctionRequestDto auctionRequestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.ok(auctionService.createAuction(goodsId, auctionRequestDto, userDetails.getUser()));
    }

    @GetMapping
    public ApiResponse<Page<AuctionListResponseDto>> allAuction(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("sortBy") String sortBy,
                                                                @RequestParam("isAsc") boolean isAsc,
                                                                @RequestParam(value = "category", required = false) String category,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseUtils.ok(auctionService.findAllAuction(page - 1, size, sortBy, isAsc, category, userDetails));
    }

    @GetMapping("/{auctionId}")
    public ApiResponse<GetAuctionResponseDto> getAuction(@PathVariable("auctionId") Long auctionId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.ok(auctionService.findAuctionById(auctionId, userDetails.getUser()));
    }

    // 경매 낙찰하기
    @PostMapping("/{auctionId}/pick/bid/list")
    public ApiResponse<?> endAuction(@PathVariable("auctionId") Long auctionId,
                                     @Valid @RequestBody ChoiceRequestDto choiceRequestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        auctionService.endAuction(auctionId, userDetails.getUser(), choiceRequestDto);

        return ResponseUtils.ok(ResponseUtils.okWithMessage(AUCTION_DELETE_SUCCESS));
    }

    // 경매 비정상 종료 ( 입찰 취소 ) -> 여기에 포인트 차감 로직 추가 필요
    @DeleteMapping("/{auctionId}")
    public ApiResponse<?> deleteAuction(@PathVariable("auctionId") Long auctionId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        auctionService.deleteAuction(auctionId, userDetails.getUser());
        return ResponseUtils.okWithMessage(AUCTION_DELETE_SUCCESS);
    }

    //교환 요청 페이지(내 경매)
    @GetMapping("/users/trade")
    public ResponseEntity<Page<TestListResponseDto>> auctionTradeList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      @RequestParam("page") int page,
                                                                      @RequestParam("size") int size,
                                                                      @RequestParam("sortBy") String sortBy,
                                                                      @RequestParam("isAsc") boolean isAsc,
                                                                      @RequestParam(value = "status", required = false) String auctionStatus) {

        return auctionService.auctionTradeList(userDetails.getUser(), page - 1, size, sortBy, isAsc, auctionStatus);
    }

    // 교환요청 수락
    @PostMapping("/users/auction/{auctionId}/accept")
    public ApiResponse<?> goodsAccept(@PathVariable("auctionId") Long auctionId,
                                      @Valid @RequestBody ChoiceRequestDto choiceRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return auctionService.goodsAccept(userDetails.getUser(), choiceRequestDto, auctionId);
    }
    // 교환완료 요청
    @PostMapping("{auctionId}/users/accept/completed")
    public ApiResponse<?> tradeCompleted(
            @PathVariable("auctionId") Long auctionId,
            @Valid @RequestBody RequestAcceptRequestDto requestAcceptRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        return auctionService.tradeCompleted(auctionId, requestAcceptRequestDto, userDetails);
    }
}