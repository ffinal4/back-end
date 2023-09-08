package com.example.peeppo.domain.bid.service;

import com.example.peeppo.domain.auction.dto.GetAuctionBidResponseDto;
import com.example.peeppo.domain.auction.dto.TestListResponseDto;
import com.example.peeppo.domain.auction.dto.TimeRemaining;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.bid.dto.*;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.bid.enums.BidStatus;
import com.example.peeppo.domain.bid.repository.bid.BidRepository;
import com.example.peeppo.domain.dibs.repository.DibsRepository;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.goods.repository.goods.GoodsRepository;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.notification.repository.NotificationRepository;
import com.example.peeppo.domain.rating.entity.RatingGoods;
import com.example.peeppo.domain.rating.repository.ratingGoodsRepository.RatingGoodsRepository;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.helper.UserRatingHelper;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.responseDto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private final RatingGoodsRepository ratingGoodsRepository;
    private final NotificationRepository notificationRepository;
    private final DibsRepository dibsRepository;
    private final UserRatingHelper userRatingHelper;
    private final ApplicationEventPublisher eventPublisher;

    public ResponseDto bidding(User user, Long auctionId, BidGoodsListRequestDto bidGoodsListRequestDto) throws IllegalAccessException {

        Auction auction = getAuction(auctionId);
        userRatingHelper.getUser(user.getUserId());
        List<Bid> bids = bidRepository.findByAuctionAuctionIdAndUserUserId(user.getUserId(), auctionId);

        for (Bid bid : bids) {
            if (!bids.isEmpty()) {
                throw new IllegalAccessException("이미 참여중인 경매입니다.");
            }
        }

        List<Bid> bidList = new ArrayList<>();
        Double totalPrice = 0D;

        for (Long goodsId : bidGoodsListRequestDto.getGoodsId()) {
            RatingGoods ratingGoods = ratingGoodsRepository.findByGoodsGoodsId(goodsId);
            totalPrice += ratingGoods.getAvgRatingPrice();
        }

        //경매 진행 여부
        if (!auction.getGoods().getGoodsStatus().equals(GoodsStatus.ONAUCTION) ||
                auction.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalAccessException("본인의 경매물품엔 입찰하실 수 없습니다.");
        }
        for (Long goodsId : bidGoodsListRequestDto.getGoodsId()) {
            Goods goods = getGoods(goodsId);
            String goodsImg = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goodsId).getImageUrl();

            if (goods.getIsDeleted() && !goods.getUser().getUserId().equals(user.getUserId())) {
                System.out.println(" ");
                throw new IllegalAccessException();
                //여기도 고민
            }
            if (goods.getGoodsStatus().equals(GoodsStatus.ONSALE) &&
                    (totalPrice >= auction.getLowPrice())) {
                //시작가보다 낮을 경우
                bidList.add(new Bid(user, auction, goods, goodsImg, BidStatus.BIDDING));
                goods.changeStatus(GoodsStatus.BIDDING);
            } else {
                System.out.println("3 ");
                throw new IllegalAccessException();
            }
        }

        bidRepository.saveAll(bidList);

        return new ResponseDto("입찰이 완료되었습니다.", HttpStatus.OK.value(), "OK");
    }

    // 입찰물품 전체조회
    public Page<BidResponseListDto> BidList(Long auctionId, int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<Bid> bidPage = bidRepository.findSortedBySellersPick(auctionId, pageable);

        List<BidResponseListDto> bidResponseListDtos = new ArrayList<>();
        for (Bid bid : bidPage) {
            List<Long> bidList = bidRepository.findBidIdByUserUserIdAndAuctionAuctionId(bid.getUser().getUserId(), auctionId);
            String imageUrl = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(bid.getGoods().getGoodsId()).getImageUrl();
            Long bidCount = bidRepository.countBidsByUserIdAndAuctionId(auctionId, bid.getUser().getUserId());
            bidResponseListDtos.add(new BidResponseListDto(bidList, bid, imageUrl, bidCount));
        }
        return new PageResponse<>(bidResponseListDtos, pageable, bidPage.getTotalElements());
    }

    // 입찰물품 상세조회
    public ApiResponse<List<BidDetailResponseDto>> sellectBids(Long auctionId, Long userId) {
        List<Bid> bidList = bidRepository.findByAuctionAuctionIdAndUserUserId(auctionId, userId);
        List<BidDetailResponseDto> bidDetailResponseDtos = bidList.stream()
                .map(bid -> {
                    Long dibs = dibsRepository.countByGoodsGoodsIdAndGoodsIsDeletedFalse(bid.getGoods().getGoodsId());
                    List<String> imageUrls = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAsc(bid.getGoods().getGoodsId())
                            .stream()
                            .map(Image::getImageUrl)
                            .collect(Collectors.toList());
                    return new BidDetailResponseDto(bid, dibs, imageUrls);
                })
                .collect(Collectors.toList());

        return new ApiResponse<>(true, bidDetailResponseDtos, null);
    }
    

    public ResponseEntity<Page<GetAuctionBidResponseDto>> bidTradeList(User user, int page, int size, String sortBy, boolean isAsc,
                                                                       String bidStatus1) {

        Pageable pageable = paging(page, size, sortBy, isAsc);
        Page<Auction> myAuctionPage;
        List<GetAuctionBidResponseDto> auctionResponseDtoList = new ArrayList<>();
        BidStatus bidStatus;

        if (bidStatus1 != null) {
            bidStatus = BidStatus.valueOf(bidStatus1);
            myAuctionPage = auctionRepository.findAuctionListByUserUserIdAndBidStatus(user.getUserId(), pageable, bidStatus);
        } else {
            myAuctionPage = auctionRepository.findAuctionListByUserUserId(user.getUserId(), pageable);
        }

        for (Auction auction : myAuctionPage) {
            List<Bid> bidList = bidRepository.findByAuctionAuctionIdAndUserUserId(auction.getAuctionId(), user.getUserId());

            List<BidListResponseDto> bidListResponseDtos = new ArrayList<>();
            for (Bid bid : bidList) {
                bidListResponseDtos.add(new BidListResponseDto(bid, bid.getGoodsImg()));
            }
            TimeRemaining timeRemaining = countDownTime(auction);
            Long bidCount = findBidCount(auction.getAuctionId());
            TestListResponseDto responseDto = new TestListResponseDto(auction, timeRemaining, bidCount);
            GetAuctionBidResponseDto getAuctionBidResponseDto = new GetAuctionBidResponseDto(responseDto, bidListResponseDtos);
            auctionResponseDtoList.add(getAuctionBidResponseDto);
        }

        PageResponse response = new PageResponse<>(auctionResponseDtoList, pageable, myAuctionPage.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    public Long findBidCount(Long id) {
        return auctionRepository.countByAuctionAuctionIdAndGroupByBidUserId(id);
    }

    public TimeRemaining countDownTime(Auction auction) {
        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(now, auction.getAuctionEndTime());
        long hours = ChronoUnit.HOURS.between(now, auction.getAuctionEndTime());
        long minutes = ChronoUnit.MINUTES.between(now, auction.getAuctionEndTime());
        long seconds = ChronoUnit.SECONDS.between(now, auction.getAuctionEndTime());

        return new TimeRemaining(days, hours % 24, minutes % 60, seconds % 60);
    }

    private Bid getBid(Long bidId) {
        return bidRepository.findById(bidId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 bidId 입니다."));
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
