package com.example.peeppo.domain.auction.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetAuctionResponseDto {
    private List<AuctionListResponseDto> auctionResponseDtos;
    private AuctionResponseDto auctionResponseDto;


    public GetAuctionResponseDto(List<AuctionListResponseDto> auctionResponseDtos, AuctionResponseDto auctionResponseDto) {
        this.auctionResponseDtos = auctionResponseDtos;
        this.auctionResponseDto = auctionResponseDto;
    }
}
