package com.example.peeppo.domain.bid.service;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.bid.dto.BidListResponseDto;
import com.example.peeppo.domain.bid.dto.BidRequestDto;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.bid.repository.BidRepository;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final GoodsRepository goodsRepository;
    private final ImageRepository imageRepository;

    public ResponseEntity<ResponseDto> bidding(User user, Long auctionId, BidRequestDto bidRequestDto) {
        Auction auction = getAuction(auctionId);

        Long goodsId = bidRequestDto.getGoodsId();
        Goods goods = getGoods(goodsId);
        String goodsImg = String.valueOf(imageRepository.findFirstByGoodsGoodsIdOrderByCreatedAtAsc(goodsId));

        //경매 진행 여부
//        validateAuctionPeriod(auction.getAuctionStatus());
//
//        //경매 등록한 사람이랑 같은지 확인
//        if (auction.getUser().getId().equals(userId)) {
//            throw new IllegalArgumentException();
//        }
//
//        //시작가보다 낮을 경우
//        if(auction.getGoods().getGoodsRating() > goods.getGoodsRating()){
//            return;
//        }
//        else {
        Bid bid = new Bid(user, auction, goods, goodsImg);
        bidRepository.save(bid);

        ResponseDto response = new ResponseDto("등록 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
//        }
    }

    private Auction getAuction(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 auctionId 입니다."));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 userId 입니다."));
    }

    private Goods getGoods(Long goodsId) {
        return goodsRepository.findById(goodsId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 goodsId 입니다."));
    }

    public void validateAuctionPeriod(Auction auction) {
    }

//    public void validateGoods(Goods goods) {
//        if (!goods.getGoodsStatus().equals(GoodsStatus.ONSALE.getStatus())) {
//            throw new IllegalArgumentException();
//        }
//    }

    //물품 수정
    public ResponseEntity<ResponseDto> updatebid(User user, Long auctionId, Long bidId, BidRequestDto bidRequestDto) {
        Auction auction = getAuction(auctionId);
        Bid bid = getBid(bidId);

        Long goodsId = bidRequestDto.getGoodsId();
        Goods goods = getGoods(goodsId);
        String goodsImg = String.valueOf(imageRepository.findFirstByGoodsGoodsIdOrderByCreatedAtAsc(goodsId));

        bid.update(auction, user, goods, goodsImg);

        bidRepository.save(bid);

        ResponseDto response = new ResponseDto("재등록 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    public ResponseEntity<ResponseDto> deletebid(User user, Long auctionId, Long bidId) {
        Auction auction = getAuction(auctionId);
        Bid bid = getBid(bidId);

        bidRepository.delete(bid);

        ResponseDto response = new ResponseDto("삭제 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    private Bid getBid(Long bidId) {
        return bidRepository.findById(bidId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 bidId 입니다."));
    }

//    public ResponseEntity<List<BidListResponseDto>> BidList(Long auctionId){
////        Auction auction = getAuction(auctionId);
////
////        List<BidListResponseDto> bidList = auction.getBidList().stream()
////                .map(BidListResponseDto::new)
////                .toList();//수정해야됨
//
//        Long goodsId = getAuction(auctionId).getGoods().getGoodsId();
//
//        String goodsImg = String.valueOf(imageRepository.findFirstByGoodsGoodsIdOrderByCreatedAtAsc(goodsId));
//
//        List<BidListResponseDto> bidList = bidRepository.findAllByauctionId(auctionId).stream()
//                .map(Bid -> new BidListResponseDto(Bid, goodsImg))
//                .toList();
//
//        return ResponseEntity.status(HttpStatus.OK.value()).body(bidList);
//    }
}
