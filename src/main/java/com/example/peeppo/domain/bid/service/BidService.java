package com.example.peeppo.domain.bid.service;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.bid.dto.BidGoodsListRequestDto;
import com.example.peeppo.domain.bid.dto.BidListResponseDto;
import com.example.peeppo.domain.bid.dto.ChoiceRequestDto;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.bid.entity.Choice;
import com.example.peeppo.domain.bid.repository.BidRepository;
import com.example.peeppo.domain.bid.repository.ChoiceBidRepository;
import com.example.peeppo.domain.bid.repository.QueryRepository;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final GoodsRepository goodsRepository;
    private final ImageRepository imageRepository;
    private final QueryRepository queryRepository;
    private final ChoiceBidRepository choiceBidRepository;

    public ResponseEntity<ResponseDto> bidding(User user, Long auctionId, BidGoodsListRequestDto bidGoodsListRequestDto) {

        Auction auction = getAuction(auctionId);

//        Long goodsId = bidRequestDto.getGoodsId();
//        Goods goods = getGoods(goodsId);
//        String goodsImg = String.valueOf(imageRepository.findFirstByGoodsGoodsIdOrderByCreatedAtAsc(goodsId));

//        if(!goods.isDeleted()){
////            ResponseDto response = new ResponseDto("삭제된 물품입니다.", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST");
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
//            throw new IllegalArgumentException();
//        }

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

        List<Bid> List = new ArrayList<>();

        for (Long goodsId : bidGoodsListRequestDto.getGoodsId()) {
            Goods goods = getGoods(goodsId);
            String goodsImg = String.valueOf(imageRepository.findFirstByGoodsGoodsIdOrderByCreatedAtAsc(goodsId));

            if (goods.isDeleted()) {
                continue;
            }
            List.add(new Bid(user, auction, goods, goodsImg));
        }

//        Bid bid = new Bid(user, auction, goods, goodsImg);
        bidRepository.saveAll(List);

        ResponseDto response = new ResponseDto("등록 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
//        }
    }


    //물품 수정
    public ResponseEntity<ResponseDto> updateBid(User user, Long auctionId, Long bidId, BidGoodsListRequestDto bidGoodsListRequestDto) {
        Auction auction = getAuction(auctionId);
        Bid bid = getBid(bidId);

        for (Long goodsId : bidGoodsListRequestDto.getGoodsId()) {
            Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new IllegalArgumentException());
            String goodsImg = String.valueOf(imageRepository.findFirstByGoodsGoodsIdOrderByCreatedAtAsc(goodsId));

            if (goods.isDeleted()) {
                continue; //이미 삭제된 goodsId라는거 표시해주기
            }
            bid.update(user, auction, goods, goodsImg);
        }

        bidRepository.save(bid);

        ResponseDto response = new ResponseDto("재등록 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    //해당 유저, 해당 옥션에 전부를 취소
    @Transactional
    public ResponseEntity<ResponseDto> deleteBid(User user, Long auctionId) {
        Auction auction = getAuction(auctionId);
        Long userId = user.getUserId();

        List<Bid> bidList = queryRepository.findBid(user.getUserId(), auctionId);

        for (Bid bid : bidList) {
            bidRepository.delete(bid);
        }

        ResponseDto response = new ResponseDto("삭제 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    public ResponseEntity<Page<BidListResponseDto>> BidList(Long auctionId, int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = paging(page, size, sortBy, isAsc);
        Page<Bid> bidPage = bidRepository.findAllByAuctionAuctionId(auctionId, pageable);

        Auction auction = getAuction(auctionId);
        Long goodsId = auction.getGoods().getGoodsId();

        String goodsImg = String.valueOf(imageRepository.findFirstByGoodsGoodsIdOrderByCreatedAtAsc(goodsId));

        List<BidListResponseDto> bidList = bidPage.getContent().stream()
                .map(Bid -> new BidListResponseDto(Bid, goodsImg))
                .toList();

        PageResponse response = new PageResponse<>(bidList, pageable, bidPage.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    //경매자가 선택, try문을 하는게 깔끔하려나?
    public ResponseEntity<ResponseDto> choiceBids(User user, Long auctionId, ChoiceRequestDto choiceRequestDto) {
        Auction auction = getAuction(auctionId);
        List<Choice> bidsList = new ArrayList<>();

//        if (auction.getUser().getUserId() == user.getUserId()) {
//            bidRepository.saveAll(biddingList(bidsList, bidGoodsListRequestDto, user, auction));
//
//            ResponseDto response = new ResponseDto("등록 완료", HttpStatus.OK.value(), "OK");
//        }
//        else{
//            ResponseDto response = new ResponseDto("해당 경매 물품의 판매자가 아닙니다.", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST");
//        }

//        if (auction.getUser().getUserId() == user.getUserId()) {
        for (Long bidId : choiceRequestDto.getbidId()) {
            Bid bid = getBid(bidId);
            bidsList.add(new Choice(bid, auction));
        }

        choiceBidRepository.saveAll(bidsList);
//          }

        ResponseDto response = new ResponseDto("선택 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    //경매자가 선택 바꾸는 기능
    public ResponseEntity<ResponseDto> choiceUpdateBids(User user, Long auctionId, ChoiceRequestDto choiceRequestDto) {
        Auction auction = getAuction(auctionId);
        List<Choice> bidsList = new ArrayList<>();
        List<Choice> findAllChoice = queryRepository.findChoice(auctionId);

//        if (auction.getUser().getUserId() == user.getUserId()) {
        for (Choice choice : findAllChoice) {
            choiceBidRepository.delete(choice);
        }

        for (Long bidId : choiceRequestDto.getbidId()) {
            Bid bid = getBid(bidId);
            bidsList.add(new Choice(bid, auction));
        }

        choiceBidRepository.saveAll(bidsList);
//        }

        ResponseDto response = new ResponseDto("선택 수정 완료", HttpStatus.OK.value(), "OK");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    //Bid update해줌
    private List<Bid> biddingList(List<Bid> bidList, BidGoodsListRequestDto bidGoodsListRequestDto, User
            user, Auction auction) {
        for (Long goodsId : bidGoodsListRequestDto.getGoodsId()) {
            Goods goods = getGoods(goodsId);
            String goodsImg = String.valueOf(imageRepository.findFirstByGoodsGoodsIdOrderByCreatedAtAsc(goodsId));

            if (goods.isDeleted()) {
                continue;
            }
            bidList.add(new Bid(user, auction, goods, goodsImg));
        }

        return bidList;
    }


    private Bid getBid(Long bidId) {
        return bidRepository.findById(bidId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 bidId 입니다."));
    }

    private Choice getChoice(Long choiceId) {
        return choiceBidRepository.findById(choiceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 choiceId 입니다."));
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

    private Pageable paging(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(page, size, sort);
    }

    public void validateAuctionPeriod(Auction auction) {
    }

//    public void validateGoods(Goods goods) {
//        if (!goods.getGoodsStatus().equals(GoodsStatus.ONSALE.getStatus())) {
//            throw new IllegalArgumentException();
//        }
//    }
}
