package com.example.peeppo.domain.auction.service;

import com.example.peeppo.domain.auction.dto.*;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.bid.repository.BidRepository;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.GoodsRepository;
import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.rating.entity.RatingGoods;
import com.example.peeppo.domain.rating.repository.ratingGoodsRepository.RatingGoodsRepository;
import com.example.peeppo.domain.rating.repository.ratingRepository.RatingRepository;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.example.peeppo.domain.auction.enums.AuctionStatus.CANCEL;
import static com.example.peeppo.domain.auction.enums.AuctionStatus.REQUEST;
import static com.example.peeppo.domain.bid.enums.BidStatus.FAIL;
import static com.example.peeppo.domain.bid.enums.BidStatus.SUCCESS;
import static com.example.peeppo.domain.goods.enums.GoodsStatus.ONSALE;
import static com.example.peeppo.domain.goods.enums.GoodsStatus.SOLDOUT;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final GoodsRepository goodsRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final RatingGoodsRepository ratingGoodsRepository;

    @Transactional
    public AuctionResponseDto createAuction(Long goodsId, AuctionRequestDto auctionRequestDto, User user) {
        checkGoodsUsername(goodsId, user);
        if (user.getUserPoint() < 10) {
            throw new IllegalArgumentException("경매 등록에는 10p가 필요합니다. 현재" + user.getUserPoint() + "포인트를 가지고 있습니다.");
        }
        user.userPointSubtract(10L);
        userRepository.save(user);

        Goods getGoods = findGoodsId(goodsId);
        if(!(getGoods.getGoodsStatus()==ONSALE)){
            new IllegalArgumentException("해당 물건으로는 경매를 등록할 수 없습니다");
        }
        if(!(getGoods.isDeleted())){
            new IllegalArgumentException("해당 물건은 삭제된 물건입니다.");
        }

        RatingGoods ratingGoods = ratingGoodsRepository.findByGoodsGoodsId(goodsId);

        if(ratingGoods.getRatingCount() < 3){
            new IllegalArgumentException("해당 물건은 3회 이상의 평가가 끝나지 않은 상태입니다.");
        }

        GoodsResponseDto goodsResponseDto = new GoodsResponseDto(getGoods);
        LocalDateTime auctionEndTime = calAuctionEndTime(auctionRequestDto.getEndTime()); // 마감기한 계산
        log.info("{}", auctionEndTime);
        Auction auction = new Auction(getGoods, auctionEndTime, user, ratingGoods); // 경매와 마감기한 생성
        auction.getGoods().changeStatus(GoodsStatus.ONAUCTION);
        auctionRepository.save(auction);
        return new AuctionResponseDto(auction, goodsResponseDto, user, countDownTime(auction));
    }

    // 마감시간 계산
    public LocalDateTime calAuctionEndTime(String auctionTIme) {
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

    // 남은 시간 카운트다운 계산
    public TimeRemaining countDownTime(Auction auction) {
        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(now, auction.getAuctionEndTime());
        long hours = ChronoUnit.HOURS.between(now, auction.getAuctionEndTime());
        long minutes = ChronoUnit.MINUTES.between(now, auction.getAuctionEndTime());
        long seconds = ChronoUnit.SECONDS.between(now, auction.getAuctionEndTime());

        return new TimeRemaining(days, hours % 24, minutes % 60, seconds % 60);
    }

    // 물품 찾아서 Goods 리턴
    public Goods findGoodsId(Long goodsId) {
        return goodsRepository.findById(goodsId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 물품은 존재하지 않습니다"));
    }

    // 경매 입찰 수
    public Long findBidCount(Long id) {
        return bidRepository.countByAuctionAuctionId(id);
    }

    // 경매 전체 조회
    public Page<AuctionListResponseDto> findAllAuction(int i, int size, String sortBy, boolean isAsc) {
        Pageable pageable = paging(i, size, sortBy, isAsc);
        Page<Auction> auctionPage = auctionRepository.findAll(pageable);
        List<AuctionListResponseDto> auctionResponseDtoList = new ArrayList<>();

        for (Auction auction : auctionPage) {

            TimeRemaining timeRemaining = countDownTime(auction);

            AuctionListResponseDto auctionListResponseDto = new AuctionListResponseDto(auction, timeRemaining, findBidCount(auction.getAuctionId()));
            auctionResponseDtoList.add(auctionListResponseDto);
        }
        return new PageResponse<>(auctionResponseDtoList, pageable, auctionPage.getTotalElements());
    }

    private Pageable paging(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(page, size, sort);
    }

    // 경매 상세 조회
    public AuctionResponseDto findAuctionById(Long auctionId, User user) {
        Auction auction = findAuctionId(auctionId);
        boolean checkSameUser = true;
        if(auction.getUser().getUserId() != user.getUserId()){
            checkSameUser = false;
        }
        return new AuctionResponseDto(auction, auction.getGoods(), countDownTime(auction), findBidCount(auctionId), checkSameUser);
    }

    // 경매 찾아서 Auction 리턴
    public Auction findAuctionId(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 물품은 존재하지 않습니다"));
    }

    public Bid findBidId(Long bidId) {
        return bidRepository.findById(bidId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 입찰 물품은 존재하지 않습니다"));
    }

    // 경매 삭제 (경매 입찰 취소)
    @Transactional
    public void deleteAuction(Long auctionId, User user) {
        if (user.getUserPoint() < 10) {
            throw new IllegalArgumentException("경매 취소에는 10p가 필요합니다. 현재" + user.getUserPoint() + "포인트를 가지고 있습니다.");
        }

        user.userPointSubtract(10L);
        userRepository.save(user);

        Auction auction = findAuctionId(auctionId);
        auction.getGoods().changeStatus(ONSALE);
        auction.changeAuctionStatus(CANCEL);
        List<Bid> bidList = bidRepository.findBidByAuctionAuctionId(auctionId);
        for (Bid bid : bidList) {
            bid.changeBidStatus(FAIL);
            bid.getGoods().changeStatus(ONSALE);
        }

        checkUsername(auctionId, user);
        auctionRepository.delete(auction);
    }

    // 경매 입찰 성공 ( 경매물품과 입찰물품의 상태를 둘 다 soldout으로 변경해라 )
    @Transactional
    public void endAuction(Long auctionId, Long bidId, User user) {
        Auction auction = findAuctionId(auctionId);

        checkUsername(auctionId, user);
        Bid bid = findBidId(bidId);
        bid.changeBidStatus(SUCCESS);

        auction.changeAuctionStatus(REQUEST);
        auction.getGoods().changeStatus(SOLDOUT);

        user.userPointAdd(10L);
        userRepository.save(user);

        auction.changeDeleteStatus(true);
    }

    // 경매 등록한 유저가 맞는지 확인
    public void checkUsername(Long id, User user) {
        Auction auction = findAuctionId(id);
        if (!(auction.getUser().getUserId().equals(user.getUserId()))) {
            throw new IllegalArgumentException("경매 취소는 작성자만 삭제가 가능합니다");
        }
    }

    public ResponseEntity<Page<TestListResponseDto>> auctionTradeList(User user, int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = paging(page, size, sortBy, isAsc);
        Page<Auction> myAuctionPage = auctionRepository.findByUserUserId(user.getUserId(), pageable);

        List<TestListResponseDto> auctionResponseDtoList = new ArrayList<>();

        for (Auction auction : myAuctionPage) {

            TimeRemaining timeRemaining = countDownTime(auction);

            TestListResponseDto testListResponseDto = new TestListResponseDto(auction, timeRemaining, findBidCount(auction.getAuctionId()));
            auctionResponseDtoList.add(testListResponseDto);
        }

        PageResponse response = new PageResponse<>(auctionResponseDtoList, pageable, myAuctionPage.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);

    }

    public void checkGoodsUsername(Long id, User user) {
        Goods goods = findGoodsId(id);
        if (!(goods.getUser().getUserId().equals(user.getUserId()))) {
            throw new IllegalArgumentException("경매 생성은 물품 작성자만 가능합니다");
        }
    }

}