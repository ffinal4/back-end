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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final GoodsRepository goodsRepository;
    private final AuctionRepository auctionRepository;

    public AuctionResponseDto createAuction(Long goodsId, AuctionRequestDto auctionRequestDto, User user) {
        Goods getGoods = findGoodsId(goodsId);
        GoodsResponseDto goodsResponseDto = new GoodsResponseDto(getGoods);
        LocalDateTime auctionEndTime = calAuctionEndTime(auctionRequestDto.getEndTime()); // 마감기한 계산
        log.info("{}" , auctionEndTime);
        Auction auction = new Auction(getGoods, auctionEndTime, auctionRequestDto, user); // 경매와 마감기한 생성
        auctionRepository.save(auction);
        return new AuctionResponseDto(auction, goodsResponseDto,user);
    }

    // 마감시간 계산
    public LocalDateTime calAuctionEndTime(String auctionTIme){
        String t = auctionTIme;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime daysLater = null;
        switch (t) {
            case "30분":
                System.out.println("30분");
                daysLater = now.plusMinutes(30);
                break;
            case "1시간":
                System.out.println("1시간");
                daysLater = now.plusHours(1);
                break;
            case "3시간":
                System.out.println("3시간");
                daysLater = now.plusHours(3);
                break;
        }
        return daysLater;
    }

    // 물품 찾아서 Goods 리턴
    public Goods findGoodsId(Long goodsId) {
        return goodsRepository.findById(goodsId).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 물품은 존재하지 않습니다"));
    }

    // 경매 전체 조회
    public List<AuctionListResponseDto> findAllAuction() {

            return auctionRepository.findAll().stream().map(AuctionListResponseDto::new).toList();
    }

    // 경매 상세 조회
    public AuctionResponseDto findAuctionById(Long auctionId) {
        Auction auction = findAuctionId(auctionId);
        return new AuctionResponseDto(auction, auction.getGoods());
    }

    // 경매 찾아서 Auction 리턴
    public Auction findAuctionId(Long auctionId){
       return auctionRepository.findById(auctionId).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 물품은 존재하지 않습니다"));
    }

    // 경매 삭제
    public void deleteAuction(Long auctionId, User user) {
        Auction auction = findAuctionId(auctionId);
        checkUsername(auctionId, user);
        auctionRepository.delete(auction);
    }

    // 경매 등록한 유저가 맞는지 확인
    public void checkUsername(Long id, User user){
        Auction auction = findAuctionId(id);
        if(!(auction.getUser().getUserId().equals(user.getUserId()))){
            throw new IllegalArgumentException("경매 취소는 작성자만 삭제가 가능합니다");
        }
    }
}