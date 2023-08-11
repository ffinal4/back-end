package com.example.peeppo.domain.bid.controller;

import com.example.peeppo.domain.bid.dto.BidListResponseDto;
import com.example.peeppo.domain.bid.dto.BidRequestDto;
import com.example.peeppo.domain.bid.service.BidService;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                                               @Valid @RequestBody BidRequestDto bidRequestDto) {

        return bidService.bidding(userDetails.getUser(), auctionId, bidRequestDto);
    }

    @PatchMapping("/auction/{auctionId}/bid/{bidId}")
    public ResponseEntity<ResponseDto> updatebid(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PathVariable Long auctionId,
                                          @PathVariable Long bidId,
                                          @Valid @RequestBody BidRequestDto bidRequestDto) {

        return bidService.updatebid(userDetails.getUser(), auctionId, bidId, bidRequestDto);
    }

    //나중에 지울 것
    @DeleteMapping("/auction/{auctionId}/bid/{bidId}")
    public ResponseEntity<ResponseDto> deletebid(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PathVariable Long auctionId,
                                          @PathVariable Long bidId) {

        return bidService.deletebid(userDetails.getUser(), auctionId, bidId);
    }

//    @GetMapping("/auction/{auctionId}/bid")
//    public ResponseEntity<List<BidListResponseDto>> BidList(@PathVariable Long auctionId) {
//
//        return bidService.BidList(auctionId);
//    }
}
