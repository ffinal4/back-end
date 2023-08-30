package com.example.peeppo.domain.bid.controller;

import com.example.peeppo.domain.auction.dto.TestListResponseDto;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.bid.dto.BidGoodsListRequestDto;
import com.example.peeppo.domain.bid.dto.BidListResponseDto;
import com.example.peeppo.domain.bid.dto.BidTradeListResponseDto;
import com.example.peeppo.domain.bid.dto.ChoiceRequestDto;
import com.example.peeppo.domain.bid.enums.BidStatus;
import com.example.peeppo.domain.bid.service.BidService;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.example.peeppo.global.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BidController {

    private final BidService bidService;

    // 입찰하기
    @PostMapping("/auction/{auctionId}/bid")
    public ApiResponse<ResponseDto> bidding(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable Long auctionId,
                                            @Valid @RequestBody BidGoodsListRequestDto bidGoodsListRequestDto) throws IllegalAccessException {

        return ResponseUtils.ok(bidService.bidding(userDetails.getUser(), auctionId, bidGoodsListRequestDto));
    }

    //전체조회
    @GetMapping("/auction/{auctionId}/bid")
    public ApiResponse<Page<BidListResponseDto>> bidList(@PathVariable Long auctionId,
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size,
                                                            @RequestParam("sortBy") String sortBy,
                                                            @RequestParam("isAsc") boolean isAsc) {

        return ResponseUtils.ok(bidService.BidList(auctionId, page - 1, size, sortBy, isAsc));
    }

    @PostMapping("/auction/{auctionId}/choice/bids")
    public ApiResponse<ResponseDto> choiceBids(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @PathVariable Long auctionId,
                                                  @Valid @RequestBody ChoiceRequestDto choiceRequestDto) throws IllegalAccessException {

        return ResponseUtils.ok(bidService.choiceBids(userDetails.getUser(), auctionId, choiceRequestDto));
    }

    @PutMapping("/auction/{auctionId}/choice/bids")
    public ApiResponse<ResponseDto> choiceUpdateBids(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable Long auctionId,
                                                        @Valid @RequestBody ChoiceRequestDto choiceRequestDto) throws IllegalAccessException {

        return ResponseUtils.ok(bidService.choiceUpdateBids(userDetails.getUser(), auctionId, choiceRequestDto));
    }

    //교환 요청 페이지(입찰 경매)
    @GetMapping("/bid/users/trade")
    public ResponseEntity<Page<BidTradeListResponseDto>> auctionTradeList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @RequestParam("page") int page,
                                                                          @RequestParam("size") int size,
                                                                          @RequestParam("sortBy") String sortBy,
                                                                          @RequestParam("isAsc") boolean isAsc,
                                                                          @RequestParam(value = "bid status", required = false) BidStatus bidStatus) {

        return bidService.bidTradeList(userDetails.getUser(), page - 1, size, sortBy, isAsc, bidStatus);

    }
}
