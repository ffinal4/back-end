package com.example.peeppo.domain.auction.service;

import com.example.peeppo.domain.auction.dto.*;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.auction.helper.AuctionHelper;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.bid.dto.BidListResponseDto;
import com.example.peeppo.domain.bid.dto.ChoiceRequestDto;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.bid.enums.BidStatus;
import com.example.peeppo.domain.bid.repository.bid.BidRepository;
import com.example.peeppo.domain.dibs.repository.DibsRepository;
import com.example.peeppo.domain.goods.dto.GoodsResponseDto;
import com.example.peeppo.domain.goods.dto.MsgResponseDto;
import com.example.peeppo.domain.goods.dto.RequestAcceptRequestDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.enums.Category;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.goods.repository.goods.GoodsRepository;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.notification.entity.Notification;
import com.example.peeppo.domain.notification.repository.NotificationRepository;
import com.example.peeppo.domain.rating.entity.RatingGoods;
import com.example.peeppo.domain.rating.repository.ratingGoodsRepository.RatingGoodsRepository;
import com.example.peeppo.domain.user.dto.ResponseDto;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.helper.UserRatingHelper;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.responseDto.ApiResponse;
import com.example.peeppo.global.responseDto.PageResponse;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.peeppo.domain.auction.enums.AuctionStatus.*;
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
    private final DibsRepository dibsRepository;
    private final NotificationRepository notificationRepository;
    private final ImageRepository imageRepository;
    private final AuctionHelper auctionHelper;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRatingHelper userRatingHelper;

    @Transactional
    public AuctionResponseDto createAuction(Long goodsId, AuctionRequestDto auctionRequestDto, User user) {
        userRatingHelper.getUser(user.getUserId());
        checkGoodsUsername(goodsId, user);
        if (user.getUserPoint() < 10) {
            throw new IllegalArgumentException("경매 등록에는 10p가 필요합니다. 현재" + user.getUserPoint() + "포인트를 가지고 있습니다.");
        }

        Goods getGoods = findGoodsId(goodsId);
        if (getGoods.getGoodsStatus() != ONSALE) {
            throw new IllegalArgumentException("해당 물건으로는 경매를 등록할 수 없습니다");
        }
        if (getGoods.getIsDeleted()) {
            throw new IllegalArgumentException("해당 물건은 삭제된 물건입니다.");
        }

        RatingGoods ratingGoods = ratingGoodsRepository.findByGoodsGoodsId(goodsId);
        if (ratingGoods.getRatingCount() < 3) {
            throw new IllegalArgumentException("해당 물건은 3회 이상의 평가가 끝나지 않은 상태입니다.");
        }

        user.userPointSubtract(10L);
        userRepository.save(user);

        String imageUrl = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goodsId).getImageUrl();
        GoodsResponseDto goodsResponseDto = new GoodsResponseDto(getGoods, imageUrl);
        LocalDateTime auctionEndTime = calAuctionEndTime(auctionRequestDto.getEndTime()); // 마감기한 계산
        log.info("{}", auctionEndTime);
        Auction auction = new Auction(getGoods, auctionEndTime, user, ratingGoods, auctionRequestDto.getLowPrice()); // 경매와 마감기한 생성
        auction.getGoods().changeStatus(GoodsStatus.ONAUCTION);
        auction.changeAuctionStatus(AuctionStatus.AUCTION);
        auctionRepository.save(auction);
        return new AuctionResponseDto(auction, goodsResponseDto, user, auctionHelper.countDownTime(auction));
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
            case "1일":
                System.out.println("1일");
                daysLater = now.plusDays(1);
                break;
        }
        return daysLater;
    }

    // 경매 전체 조회
    public Page<AuctionListResponseDto> findAllAuction(int i, int size, String sortBy, boolean isAsc, String categoryStr, UserDetailsImpl userDetails) {
        Pageable pageable = paging(i, size, sortBy, isAsc);
        Page<Auction> auctionPage;
        if (categoryStr != null) {
            try {
                Category category = Category.valueOf(categoryStr);
                auctionPage = auctionRepository.findByGoodsCategory(category, pageable);
                for (Auction auction : auctionPage) {
                    TimeRemaining remainingTime = auctionHelper.countDownTime(auction);
                    if (!auction.getAuctionStatus().equals(CANCEL)) {
                        if (remainingTime.isExpired()) {
                            List<Bid> bid = bidRepository.findByAuctionAuctionId(auction.getAuctionId());
                            if (bid.isEmpty()) {    //체크해보세요 안먹히는거 같아요
                                auction.changeAuctionStatus(CANCEL);
                            } else {
                                auction.changeAuctionStatus(AuctionStatus.END);
                            }
                        }
                    }
                }
                auctionRepository.saveAll(auctionPage);

                return findAllAuction(auctionPage, pageable, userDetails);
            } catch (IllegalArgumentException e) {
                log.error("올바르지 않은 카테고리입니다.");
                throw new IllegalArgumentException("올바르지 않은 카테고리입니다.");
            }
        } else {
            auctionPage = auctionRepository.findAll(pageable);
            for (Auction auction : auctionPage) {
                TimeRemaining remainingTime = auctionHelper.countDownTime(auction);

                if (remainingTime.isExpired()) {
                    List<Bid> bid = bidRepository.findByAuctionAuctionId(auction.getAuctionId());
                    if (bid.isEmpty()) {
                        auction.changeAuctionStatus(CANCEL);
                    } else {
                        auction.changeAuctionStatus(AuctionStatus.END);
                    }
                }
            }
            auctionRepository.saveAll(auctionPage);

            return findAllAuction(auctionPage, pageable, userDetails);
        }
    }

    // 경매 상세 조회
    public GetAuctionResponseDto findAuctionById(Long auctionId, User user) {
        Auction auction = findAuctionId(auctionId);
        userRatingHelper.getUser(user.getUserId());
        boolean checkSameUser = auction.getUser().getUserId().equals(user.getUserId());

        List<String> imageUrls = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAsc(auction.getGoods().getGoodsId())
                .stream().map(Image::getImageUrl).collect(Collectors.toList());

        List<Auction> auctionList = auctionRepository.findTop20ByAuctionStatus(AuctionStatus.AUCTION);
        List<AuctionListResponseDto> AuctionListResponseDtos = new ArrayList<>();
        for (Auction recommendAuction : auctionList) {
            TimeRemaining timeRemaining = auctionHelper.countDownTime(recommendAuction);
            String recommendImageUrl = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(recommendAuction.getGoods().getGoodsId()).getImageUrl();
            boolean checkDibs = dibsRepository.findByUserUserIdAndGoodsGoodsId(user.getUserId(), recommendAuction.getGoods().getGoodsId()).isPresent();
            AuctionListResponseDtos.add(
                    new AuctionListResponseDto(recommendAuction,
                            recommendImageUrl,
                            timeRemaining,
                            findBidCount(recommendAuction.getAuctionId()),
                            checkDibs));
        }

        AuctionResponseDto auctionResponseDto = new AuctionResponseDto(auction, auction.getGoods(), auctionHelper.countDownTime(auction), findBidCount(auctionId), checkSameUser, imageUrls);
        return new GetAuctionResponseDto(AuctionListResponseDtos, auctionResponseDto);
    }

    // 경매 삭제 (경매 입찰 취소)
    @Transactional
    public void deleteAuction(Long auctionId, User user) {
        if (user.getUserPoint() < 10) {
            throw new IllegalArgumentException("경매 취소에는 10p가 필요합니다. 현재" + user.getUserPoint() + "포인트를 가지고 있습니다.");
        }

        userRatingHelper.getUser(user.getUserId());
        user.userPointSubtract(10L);
        userRepository.save(user);

        Auction auction = findAuctionId(auctionId);

        auction.getGoods().changeStatus(ONSALE);
        goodsRepository.save(auction.getGoods());

        auction.changeAuctionStatus(CANCEL);
        auctionRepository.save(auction);

        List<Bid> bidList = bidRepository.findBidByAuctionAuctionId(auctionId);
        for (Bid bid : bidList) {
            bid.changeBidStatus(FAIL);
            bid.getGoods().changeStatus(ONSALE);
        }
        bidRepository.saveAll(bidList);

        checkUsername(auctionId, user);
    }

    // 경매 입찰 성공 ( 경매물품과 입찰물품의 상태를 둘 다 soldout으로 변경해라 )
    @Transactional
    public void endAuction(Long auctionId, User user, ChoiceRequestDto choiceRequestDto) {
        Auction auction = findAuctionId(auctionId);
        List<Bid> bidList = bidRepository.findByAuctionAuctionId(auctionId);

        checkUsername(auctionId, user);

        for (Bid bid : bidList) {
            bid.getGoods().changeStatus(ONSALE);
            if (bid.getBidStatus().equals(SUCCESS) ||
                    bid.getBidStatus().equals(FAIL)) {
                throw new IllegalArgumentException("이미 입찰을 선택하신 경매입니다.");
            }
            bid.changeBidStatus(FAIL);
            bid.getGoods().changeStatus(ONSALE);
        }
        bidRepository.saveAll(bidList);

        List<Bid> saveBidList = choiceRequestDto.getBidId().stream().map(bidId -> {
            Bid bid = findBidId(bidId);
            bid.getGoods().changeStatus(ONSALE);
            bid.changeBidStatus(SUCCESS);
            bid.getGoods().changeStatus(GoodsStatus.BIDDING);
            return bid;
        }).collect(Collectors.toList());
        bidRepository.saveAll(saveBidList);

//            List<Notification> notificationList = notificationRepository.findByUserUserId(auction.getUser().getUserId());
//
//            for (Notification notification : notificationList) {
//                if (notification == null) {
//                    notification = new Notification();
//                    notification.setUser(user);
//                }
//
//                notification.setIsAuction(false);
//                notification.updateAuctionCount();
//                notification.Checked(false);
//
//                notificationRepository.save(notification);
//            }

        Notification notification = notificationRepository.findByUserUserId(auction.getUser().getUserId());

        if (notification == null) {
            notification = new Notification(user);
        }
        notificationRepository.save(notification);

        user.userPointAdd(10L);
        userRepository.save(user);

//        eventPublisher.publishEvent(new AuctionEvent(auction.getGoods(), auc));
        auction.changeDeleteStatus(true);
    }

    public ResponseEntity<Page<TestListResponseDto>> auctionTradeList(User user, int page, int size, String sortBy, boolean isAsc,
                                                                      String auctionStatusStr) {
        Pageable pageable = paging(page, size, sortBy, isAsc);
        Page<Auction> myAuctionPage;
        AuctionStatus auctionStatus;
        userRatingHelper.getUser(user.getUserId());

        if (auctionStatusStr != null) {
            try {
                auctionStatus = AuctionStatus.valueOf(auctionStatusStr);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("올바르지 않은 카테고리입니다: " + auctionStatusStr);
            }
            myAuctionPage = auctionRepository.findByUserUserIdAndAuctionStatus(user.getUserId(), pageable, auctionStatus);
        } else {
            myAuctionPage = auctionRepository.findByUserUserIdAndAuctionStatusIsNotNull(user.getUserId(), pageable);
        }
        Long auctionCount = auctionRepository.countByUserUserIdAndAuctionStatus(user.getUserId(), AUCTION);
        Long auctionEndCount = auctionRepository.countByUserUserIdAndAuctionStatus(user.getUserId(), END);
        List<GetAuctionBidResponseDto> auctionResponseDtoList = myAuctionPage.stream()
                .map(auction -> {
                    TimeRemaining timeRemaining = auctionHelper.countDownTime(auction);
                    Long bidCount = findBidCount(auction.getAuctionId());
                    TestListResponseDto responseDto = new TestListResponseDto(auction, timeRemaining, bidCount, auctionCount, auctionEndCount);

                    if ((auction.getAuctionStatus() == AuctionStatus.END) ||
                            (auction.getAuctionStatus() == AuctionStatus.DONE)) {
                        List<Bid> bidList = bidRepository.findByAuctionAuctionIdAndBidStatus(auction.getAuctionId(), SUCCESS);
                        List<BidListResponseDto> bidListResponseDtos = bidList.stream()
                                .map(bid -> new BidListResponseDto(bid, bid.getGoodsImg()))
                                .collect(Collectors.toList());
                        return new GetAuctionBidResponseDto(responseDto, bidListResponseDtos);
                    } else {
                        return new GetAuctionBidResponseDto(responseDto);
                    }
                })
                .collect(Collectors.toList());
        //나중에 stream 으로 처리하자, flatMap 쓰면 될듯?
        PageResponse response = new PageResponse<>(auctionResponseDtoList, pageable, myAuctionPage.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @Transactional
    public ApiResponse<?> goodsAccept(User user, ChoiceRequestDto choiceRequestDto, Long auctionId) {
        Auction auction = auctionRepository.findByAuctionId(auctionId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 경매입니다."));
        if (!user.getUserId().equals(auction.getUser().getUserId())) {
            throw new IllegalArgumentException("경매 등록자가 아닙니다.");
        }
        auction.changeAuctionStatus(AuctionStatus.DONE);
        Goods auctionGoods = goodsRepository.findByAuctionAuctionId(auction.getAuctionId());
        auctionRepository.save(auction);

        auctionGoods.changeStatus(SOLDOUT);
        goodsRepository.save(auctionGoods);

        List<Bid> bidList = choiceRequestDto.getBidId().stream().map(bidId -> {
                    Bid bid = findBidId(bidId);

                    bid.getGoods().changeStatus(SOLDOUT);
                    bid.changeBidStatus(BidStatus.DONE);
                    return bid;
                })
                .collect(Collectors.toList());
        bidRepository.saveAll(bidList);

        ResponseDto responseDto = new ResponseDto("교환수락이 완료되었습니다.", HttpStatus.OK.value(), "OK");
        return new ApiResponse<>(true, responseDto, null);
    }

    public Page<AuctionListResponseDto> findAllAuction(Page<Auction> auctionPage, Pageable pageable, UserDetailsImpl userDetails) {
        List<AuctionListResponseDto> auctionResponseDtoList = auctionPage.stream().map(auction -> {
                    TimeRemaining timeRemaining = auctionHelper.countDownTime(auction);
                    boolean checkDibs = false;
                    if (null != userDetails) {
                        checkDibs = dibsRepository.findByUserUserIdAndGoodsGoodsId(userDetails.getUser().getUserId(), auction.getGoods().getGoodsId()).isPresent();
                    }
                    String imageUrl = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(auction.getGoods().getGoodsId()).getImageUrl();
                    return new AuctionListResponseDto(auction, imageUrl, timeRemaining, findBidCount(auction.getAuctionId()), checkDibs);
                })
                .collect(Collectors.toList());

        return new PageResponse<>(auctionResponseDtoList, pageable, auctionPage.getTotalElements());
    }

    // 물품 찾아서 Goods 리턴
    public Goods findGoodsId(Long goodsId) {
        return goodsRepository.findById(goodsId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 물품은 존재하지 않습니다"));
    }

    // 경매 입찰 수
    public Long findBidCount(Long id) {
        return bidRepository.countByAuctionAuctionId(id);
    }


    // 경매 찾아서 Auction 리턴
    public Auction findAuctionId(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 물품은 존재하지 않습니다"));
    }

    public Bid findBidId(Long bidId) {
        return bidRepository.findById(bidId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 입찰 물품은 존재하지 않습니다"));
    }

    // 경매 등록한 유저가 맞는지 확인
    public void checkUsername(Long id, User user) {
        Auction auction = findAuctionId(id);
        if (!(auction.getUser().getUserId().equals(user.getUserId()))) {
            throw new IllegalArgumentException("경매 취소는 작성자만 삭제가 가능합니다");
        }
    }

    public void checkGoodsUsername(Long id, User user) {
        Goods goods = findGoodsId(id);
        if (!(goods.getUser().getUserId().equals(user.getUserId()))) {
            throw new IllegalArgumentException("경매 생성은 물품 작성자만 가능합니다");
        }
    }

    private Pageable paging(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(page, size, sort);
    }

    public ApiResponse<?> tradeCompleted(Long auctionId, RequestAcceptRequestDto requestAcceptRequestDto, UserDetailsImpl userDetails) {
        Auction auction = auctionRepository.findByAuctionId(auctionId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 경매입니다."));

        List<Bid> bidList = bidRepository.findAllById(requestAcceptRequestDto.getRequestId());

        Long userId = userDetails.getUser().getUserId();

        // 입찰자가 교환 완료를 눌렀을 시
        if (Objects.equals(userId, bidList.get(0).getUser().getUserId())) {
            for (Bid bid : bidList) {
                if (Objects.equals(userId, bid.getUser().getUserId())) {
                    throw new IllegalArgumentException("자신의 물건이 아닌 물품이 존재합니다");
                }
                if (!bid.getBidStatus().equals(BidStatus.SUCCESS)) {
                    throw new IllegalArgumentException("정상적인 접근이 아닙니다.");
                }
                bid.changeBidStatus(BidStatus.TRADING);
                bidList.add(bid);
            }
            bidRepository.saveAll(bidList);
        }

        // 경매 등록자가 교환 완료를 눌렀을 시
        else if (Objects.equals(userId, auction.getUser().getUserId())) {
            if (!auction.getAuctionStatus().equals(AuctionStatus.END)) {
                throw new IllegalArgumentException("경매가 종료된 후에 교환요청이 가능합니다.");
            }
            auction.changeAuctionStatus(AuctionStatus.TRADING);
            auctionRepository.save(auction);
        } else {
            throw new IllegalArgumentException("물품 등록자만 거래진행이 가능합니다.");
        }

        if (bidList.get(0).getBidStatus().equals(BidStatus.TRADING) &&
                auction.getAuctionStatus().equals(AuctionStatus.TRADING)) {

            auction.changeAuctionStatus(AuctionStatus.DONE);
            auctionRepository.save(auction);

            List<Goods> goodsList = bidList.stream()
                    .peek(bid -> bid.changeBidStatus(BidStatus.DONE))
                    .map(Bid::getGoods)
                    .peek(goods -> {
                        if (!goods.getGoodsStatus().equals(GoodsStatus.TRADING)) {
                            throw new RuntimeException("상품이 거래중이 아닙니다.");
                        }
                        goods.changeStatus(SOLDOUT);
                    })
                    .collect(Collectors.toList());

            goodsRepository.saveAll(goodsList);
            bidRepository.saveAll(bidList);

            return new ApiResponse<>(true, new MsgResponseDto("교환 완료!"), null);
        }
        return new ApiResponse<>(true, new MsgResponseDto("상대방의 교환완료를 기다리는중..."), null);
    }
}