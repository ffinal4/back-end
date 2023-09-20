package com.example.peeppo.domain.bid.controller;

import com.example.peeppo.domain.auction.dto.GetAuctionBidResponseDto;
import com.example.peeppo.domain.auction.dto.TestListResponseDto;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.bid.dto.*;
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

import java.util.List;
import java.util.Map;

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
    @GetMapping("/auction/{auctionId}/bid/page/{page}")
    public Page<BidResponseListDto> bidList(@PathVariable Long auctionId,
                                            @PathVariable int page) {

        return bidService.BidList(auctionId, page-1);
    }

    // 상세조회
    @GetMapping("/auction/{auctionId}/bid/{userId}")
    public ApiResponse<List<BidDetailResponseDto>> bidList(@PathVariable Long auctionId,
                                            @PathVariable Long userId) {

        return bidService.sellectBids(auctionId, userId);
    }


    //교환 요청 페이지(입찰 경매)
    @GetMapping("/bid/users/trade")
    public ResponseEntity<Page<GetAuctionBidResponseDto>> auctionTradeList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                           @RequestParam("page") int page,
                                                                           @RequestParam("size") int size,
                                                                           @RequestParam("sortBy") String sortBy,
                                                                           @RequestParam("isAsc") boolean isAsc,
                                                                           @RequestParam(value = "status", required = false) String bidStatus) {

        return bidService.bidTradeList(userDetails.getUser(), page - 1, size, sortBy, isAsc, bidStatus);
    }
}
