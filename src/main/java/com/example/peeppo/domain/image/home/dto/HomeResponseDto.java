package com.example.peeppo.domain.image.home.dto;

import com.example.peeppo.domain.auction.dto.AuctionListResponseDto;
import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.goods.dto.GoodsListResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class HomeResponseDto {

    private List<GoodsListResponseDto> goodsListResponseDto;
    private List<AuctionListResponseDto> auctionResponseDto;


    public HomeResponseDto(List<GoodsListResponseDto> goodsListResponseDtos, List<AuctionListResponseDto> auctionResponseDtos) {
        this.goodsListResponseDto = goodsListResponseDtos;
        this.auctionResponseDto = auctionResponseDtos;
    }
}
