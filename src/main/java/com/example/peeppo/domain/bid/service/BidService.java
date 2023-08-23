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
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.notification.entity.Notification;
import com.example.peeppo.domain.notification.repository.NotificationRepository;
import com.example.peeppo.domain.rating.entity.RatingGoods;
import com.example.peeppo.domain.rating.repository.ratingGoodsRepository.RatingGoodsRepository;
import com.example.peeppo.domain.rating.repository.ratingRepository.RatingRepository;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    private final RatingGoodsRepository ratingGoodsRepository;
    private final NotificationRepository notificationRepository;

    public ResponseDto bidding(User user, Long auctionId, BidGoodsListRequestDto bidGoodsListRequestDto) throws IllegalAccessException {

        Auction auction = getAuction(auctionId);
        List<Bid> List = new ArrayList<>();

        //경매 진행 여부
        if (auction.getGoods().getGoodsStatus().equals(GoodsStatus.ONAUCTION) &&
                !auction.getUser().getUserId().equals(user.getUserId())) {
                for (Long goodsId : bidGoodsListRequestDto.getGoodsId()) {
                    Goods goods = getGoods(goodsId);
                    String goodsImg = String.valueOf(imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goodsId));

                    if (goods.getIsDeleted() && !goods.getUser().getUserId().equals(user.getUserId())) {
                        System.out.println(" ");
                        throw new IllegalAccessException();
                        //여기도 고민
                    }
                    if (goods.getGoodsStatus().equals(GoodsStatus.ONSALE)) {
                        RatingGoods ratingGoods = ratingGoodsRepository.findByGoodsGoodsId(goodsId);
                        //시작가보다 낮을 경우
                        if (auction.getLowPrice() > ratingGoods.getAvgRatingPrice()) {   //평균가 이상함
                            System.out.println("2 ");
                            throw new IllegalAccessException();
                        }
                        List.add(new Bid(user, auction, goods, goodsImg));
                        goods.changeStatus(GoodsStatus.BIDDING);
                    } else {
                        System.out.println("3 ");
                        throw new IllegalAccessException();
                    }
                }
        } else {
            System.out.println("4 ");
            throw new IllegalAccessException();
        }

        Notification notification = notificationRepository.findByUserUserId(auction.getUser().getUserId());

        if (notification == null) {
            notification = new Notification();
            notification.setUser(user);
        }

        notification.setIsAuction(false);
        notification.updateAuctionCount();
        notification.Checked(false);

        notificationRepository.save(notification);

        bidRepository.saveAll(List);

        return new ResponseDto("입찰이 완료되었습니다.", HttpStatus.OK.value(), "OK");
    }

    public Page<BidListResponseDto> BidList(Long auctionId, int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = paging(page, size, sortBy, isAsc);
        Page<Bid> bidPage = bidRepository.findAllByAuctionAuctionId(auctionId, pageable);

        Auction auction = getAuction(auctionId);
        Long goodsId = auction.getGoods().getGoodsId();

        String goodsImg = String.valueOf(imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goodsId));

        List<BidListResponseDto> bidList = bidPage.getContent().stream()
                .map(Bid -> new BidListResponseDto(Bid, goodsImg))
                .toList();

        return new PageResponse<>(bidList, pageable, bidPage.getTotalElements());
    }

    //경매자가 선택
    public ResponseDto choiceBids(User user, Long auctionId, ChoiceRequestDto choiceRequestDto) throws IllegalAccessException {
        Auction auction = getAuction(auctionId);
        List<Choice> bidsList = new ArrayList<>();

        if (auction.getUser().getUserId().equals(user.getUserId())) {
            getBiddingList(choiceRequestDto, auction, bidsList);
        } else {
            throw new IllegalAccessException();
        }

        return new ResponseDto("선택이 완료되었습니다.", HttpStatus.OK.value(), "OK");
    }

    //경매자가 선택 바꾸는 기능
    public ResponseDto choiceUpdateBids(User user, Long auctionId, ChoiceRequestDto choiceRequestDto) throws IllegalAccessException {
        Auction auction = getAuction(auctionId);
        List<Choice> bidsList = new ArrayList<>();
        List<Choice> findAllChoice = queryRepository.findChoice(auctionId);

        if (auction.getUser().getUserId().equals(user.getUserId())) {
            for (Choice choice : findAllChoice) {
                choiceBidRepository.delete(choice);
            }

            getBiddingList(choiceRequestDto, auction, bidsList);
        } else {
            throw new IllegalAccessException();
        }

        return new ResponseDto("재선택 되었습니다.", HttpStatus.OK.value(), "OK");
    }

    private void getBiddingList(ChoiceRequestDto choiceRequestDto, Auction auction, List<Choice> bidsList) throws IllegalAccessException {
        for (Long bidId : choiceRequestDto.getbidId()) {
            Bid bid = getBid(bidId);

            if (!auction.getAuctionId().equals(bid.getAuction().getAuctionId())) {
                throw new IllegalAccessException();
            }
            bidsList.add(new Choice(bid, auction));
        }

        choiceBidRepository.saveAll(bidsList);
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
}
