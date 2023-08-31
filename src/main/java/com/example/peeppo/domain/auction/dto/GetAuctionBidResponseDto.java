package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.bid.dto.BidListResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetAuctionBidResponseDto {
    private TestListResponseDto testListResponseDto;
    private List<BidListResponseDto> bidListResponseDtos;
    public GetAuctionBidResponseDto(TestListResponseDto responseDto, List<BidListResponseDto> bidListResponseDtos) {
        this.testListResponseDto = responseDto;
        this.bidListResponseDtos = bidListResponseDtos;
    }

    public GetAuctionBidResponseDto(TestListResponseDto responseDto) {
        this.testListResponseDto = responseDto;
    }
}
