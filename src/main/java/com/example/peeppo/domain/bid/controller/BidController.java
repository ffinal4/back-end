package com.example.peeppo.domain.bid.controller;

import com.example.peeppo.domain.bid.dto.BidGoodsListRequestDto;
import com.example.peeppo.domain.bid.dto.BidListResponseDto;
import com.example.peeppo.domain.bid.dto.ChoiceRequestDto;
import com.example.peeppo.domain.bid.service.BidService;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.global.security.UserDetailsImpl;
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
    public ResponseEntity<ResponseDto> bidding(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable Long auctionId,
                                               @Valid @RequestBody BidGoodsListRequestDto bidGoodsListRequestDto) throws IllegalAccessException {

        return bidService.bidding(userDetails.getUser(), auctionId, bidGoodsListRequestDto);
    }

    //전체조회
    @GetMapping("/auction/{auctionId}/bid")
    public ResponseEntity<Page<BidListResponseDto>> bidList(@PathVariable Long auctionId,
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size,
                                                            @RequestParam("sortBy") String sortBy,
                                                            @RequestParam("isAsc") boolean isAsc) {

        return bidService.BidList(auctionId, page - 1, size, sortBy, isAsc);
    }

    @PostMapping("/auction/{auctionId}/choice/bids")
    public ResponseEntity<ResponseDto> choiceBids(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @PathVariable Long auctionId,
                                                  @Valid @RequestBody ChoiceRequestDto choiceRequestDto) throws IllegalAccessException {

        return bidService.choiceBids(userDetails.getUser(), auctionId, choiceRequestDto);
    }

    @PutMapping("/auction/{auctionId}/choice/bids")
    public ResponseEntity<ResponseDto> choiceUpdateBids(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable Long auctionId,
                                                        @Valid @RequestBody ChoiceRequestDto choiceRequestDto) throws IllegalAccessException {

        return bidService.choiceUpdateBids(userDetails.getUser(), auctionId, choiceRequestDto);
    }

    //나중에 전체 조회로 하는게 맞겠지?
//    @GetMapping("/auction/{auctionId}/bid/mybid")
//    public ResponseEntity<List<BidListResponseDto>> myBidList(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                              @PathVariable Long auctionId) {
//
//        return bidService.myBidList(userDetails.getUser(), auctionId);
//    }
}