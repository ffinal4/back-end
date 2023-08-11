package com.example.peeppo.domain.auction.service;

import com.example.peeppo.domain.auction.dto.AuctionListResponseDto;
import com.example.peeppo.domain.auction.dto.AuctionRequestDto;
import com.example.peeppo.domain.auction.dto.AuctionResponseDto;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final GoodsRepository goodsRepository;
    private final AuctionRepository auctionRepository;

    public AuctionResponseDto createAuction(Long goodsId, AuctionRequestDto auctionRequestDto, User user) {
        //회원저장, 상품저장, 주문저장 ( 다 new 해주고 order 저장)
/*        Goods getGoods = findGoodsId(goodsId);
        Auction auction = new Auction(); // 부모 생성 및 저장
        AuctionList auctionList = new AuctionList(getGoods,auction); // 자식 저장
        auctionRepository.save(auctionList);*/
        Goods getGoods = findGoodsId(goodsId);
        GoodsResponseDto goodsResponseDto = new GoodsResponseDto(getGoods);
        LocalDateTime auctionEndTime = calAuctionEndTime(auctionRequestDto.getEndTime()); // 마감기한 계산
        log.info("{}" , auctionEndTime);
        Auction auction = new Auction(getGoods, auctionEndTime, auctionRequestDto, user); // 경매와 마감기한 생성
        auctionRepository.save(auction);
        return new AuctionResponseDto(auction, goodsResponseDto,user);
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
        return goodsRepository.findById(goodsId).orElseThrow(()-> new NullPointerException("해당하는 물품은 존재하지 않습니다"));
    }

    public List<AuctionListResponseDto> findAllAuction() {

            return auctionRepository.findAll().stream().map(AuctionListResponseDto::new).toList();
    }

    public AuctionResponseDto findAuctionById(Long auctionId) {
        Auction auction = findAuctionId(auctionId);
        return new AuctionResponseDto(auction, auction.getGoods());
    }

    public Auction findAuctionId(Long auctionId){
       return auctionRepository.findById(auctionId).orElseThrow(()-> new NullPointerException("해당하는 경매는 존재하지 않습니다"));
    }

    public void deleteAuction(Long auctionId, User user) {
        Auction auction = findAuctionId(auctionId);
        checkUsername(auctionId, user);
        auctionRepository.delete(auction);
    }

    public void checkUsername(Long id, User user){
        Auction auction = findAuctionId(id);
        if(!(auction.getUser().getUserId().equals(user.getUserId()))){
            throw new IllegalArgumentException("경매 취소는 작성자만 삭제가 가능합니다");
        }
    }
}