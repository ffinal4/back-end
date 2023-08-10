package com.example.peeppo.domain.auction.service;

import com.example.peeppo.domain.auction.dto.AuctionListResponseDto;
import com.example.peeppo.domain.auction.dto.AuctionRequestDto;
import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.entity.AuctionList;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final GoodsRepository goodsRepository;
    private final AuctionRepository auctionRepository;

    public AuctionResponseDto createAuction(Long goodsId, AuctionRequestDto auctionRequestDto) {
        //회원저장, 상품저장, 주문저장 ( 다 new 해주고 order 저장)
/*        Goods getGoods = findGoodsId(goodsId);
        Auction auction = new Auction(); // 부모 생성 및 저장
        AuctionList auctionList = new AuctionList(getGoods,auction); // 자식 저장
        auctionRepository.save(auctionList);*/
        Goods getGoods = findGoodsId(goodsId);
        GoodsResponseDto goodsResponseDto = new GoodsResponseDto(getGoods);
        LocalDateTime auctionEndTime = calAuctionEndTime(auctionRequestDto.getEndTime()); // 마감기한 계산
        log.info("{}" , auctionEndTime);
        Auction auction = new Auction(getGoods, auctionEndTime, auctionRequestDto); // 경매와 마감기한 생성
        auctionRepository.save(auction);
        return new AuctionResponseDto(auction, goodsResponseDto);
    }

    public LocalDateTime calAuctionEndTime(String auctionTIme){
        String t = auctionTIme;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime daysLater = null;
        switch (t) {
            case "2일":
                System.out.println("2일");
                daysLater = now.plusDays(2);
                break;
            case "12시간":
                System.out.println("12");
                daysLater = now.plusHours(12);
                break;
            case "1일":
                System.out.println("1일");
                daysLater = now.plusDays(1);
                break;
        }
        return daysLater;
    }

    public Goods findGoodsId(Long goodsId) {
        return goodsRepository.findById(goodsId).orElse(null);
    }

    public List<AuctionListResponseDto> findAllAuction() {

            return auctionRepository.findAll().stream().map(AuctionListResponseDto::new).toList();
    }

    public AuctionResponseDto findAuctionById(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElse(null);
        return new AuctionResponseDto(auction, auction.getGoods());
    }

    public void deleteAuction(Long auctionId) {
        auctionRepository.deleteById(auctionId);
    }
}