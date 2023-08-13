package com.example.peeppo.domain.auction.controller;


import com.amazonaws.Response;
import com.example.peeppo.domain.auction.dto.AuctionListResponseDto;
import com.example.peeppo.domain.auction.dto.AuctionRequestDto;
import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.auction.entity.AuctionList;
import com.example.peeppo.domain.auction.service.AuctionService;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import com.example.peeppo.global.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.peeppo.global.stringCode.SuccessCodeEnum.AUCTION_DELETE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/{goodsId}")
    public ApiResponse<AuctionResponseDto> createAuction(@PathVariable("goodsId") Long goodsId,
                                                        @RequestBody AuctionRequestDto auctionRequestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
            return ResponseUtils.ok(auctionService.createAuction(goodsId, auctionRequestDto, userDetails.getUser()));
    }

    @GetMapping
    public ApiResponse<Page<AuctionListResponseDto>> allAuction(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("sortBy") String sortBy,
                                                                @RequestParam("isAsc") boolean isAsc){

        return ResponseUtils.ok(auctionService.findAllAuction(page -1, size, sortBy, isAsc));
    }

    @GetMapping("/{auctionId}")
    public ApiResponse<AuctionResponseDto> getAuction(@PathVariable("auctionId")Long auctionId){
        return ResponseUtils.ok(auctionService.findAuctionById(auctionId));
    }

    @DeleteMapping("/{auctionId}")
    public ApiResponse<?> deleteAuction(@PathVariable("auctionId") Long auctionId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        auctionService.deleteAuction(auctionId, userDetails.getUser());
        return ResponseUtils.okWithMessage(AUCTION_DELETE_SUCCESS);
    }

}