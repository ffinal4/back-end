package com.example.peeppo.domain.auction.controller;


import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/{goodsId}")
    public AuctionResponseDto createAuction(@PathVariable("goodsId") Long goodsId){
        return auctionService.createAuction(goodsId);
    }

    @GetMapping
    public List<AuctionResponseDto> allAuction(){
        return auctionService.findAllAuction();
    }
}