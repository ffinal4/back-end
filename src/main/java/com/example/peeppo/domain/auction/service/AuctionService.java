package com.example.peeppo.domain.auction.service;

import com.example.peeppo.domain.auction.dto.AuctionListResponseDto;
import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.entity.AuctionList;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final GoodsRepository goodsRepository;
    private final AuctionRepository auctionRepository;

    public AuctionResponseDto createAuction(Long goodsId) {
        //회원저장, 상품저장, 주문저장 ( 다 new 해주고 order 저장)
/*        Goods getGoods = findGoodsId(goodsId);
        Auction auction = new Auction(); // 부모 생성 및 저장
        AuctionList auctionList = new AuctionList(getGoods,auction); // 자식 저장
        auctionRepository.save(auctionList);*/
        Goods getGoods = findGoodsId(goodsId);
        GoodsResponseDto goodsResponseDto = new GoodsResponseDto(getGoods);
        Auction auction = new Auction(getGoods);
        auctionRepository.save(auction);
        return new AuctionResponseDto(auction, goodsResponseDto);
    }

    public Goods findGoodsId(Long goodsId) {
        return goodsRepository.findById(goodsId).orElse(null);
    }

    public List<AuctionListResponseDto> findAllAuction() {
            return auctionRepository.findAll().stream().map(AuctionListResponseDto::new).toList();
    }

}