package com.example.peeppo.domain.auction.dto;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.entity.AuctionList;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.WantedGoods;
import com.example.peeppo.domain.goods.enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AuctionResponseDto {
    private Long auctionId;
    private GoodsResponseDto goodsResponseDto;


    public AuctionResponseDto(Auction auction, GoodsResponseDto goods){
        this.auctionId = auction.getAuctionId();
        this.goodsResponseDto = goods;
        
    }

    public AuctionResponseDto(Auction auction) {
    }
}

