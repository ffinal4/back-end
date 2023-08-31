package com.example.peeppo.domain.home.service;


import com.example.peeppo.domain.auction.dto.AuctionListResponseDto;
import com.example.peeppo.domain.auction.dto.TimeRemaining;
import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.auction.service.AuctionService;
import com.example.peeppo.domain.dibs.service.DibsService;
import com.example.peeppo.domain.goods.dto.GoodsListResponseDto;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.repository.goods.GoodsRepository;
import com.example.peeppo.domain.home.dto.HomeResponseDto;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.user.dto.RatingUserResponseDto;
import com.example.peeppo.domain.user.repository.UserRepository;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final GoodsRepository goodsRepository;
    private final DibsService dibsService;
    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

  //@Cacheable(value = "homeCache", cacheManager = "cacheManager")
    public HomeResponseDto peeppoHome(UserDetailsImpl user) {
        List<Goods> goodsList = goodsRepository.findTop8ByCreatedAt();
        List<GoodsListResponseDto> goodsListResponseDtos = new ArrayList<>();
        for (Goods goods : goodsList) {
            boolean checkDibs = false;
            String imageUrl = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(goods.getGoodsId()).getImageUrl();
            if(user != null) {
                checkDibs = dibsService.checkDibsGoods(user.getUser().getUserId(), goods.getGoodsId());

            }
            GoodsListResponseDto goodsListResponseDto = new GoodsListResponseDto(goods, imageUrl, checkDibs);
            goodsListResponseDtos.add(goodsListResponseDto);
        }

        List<RatingUserResponseDto> ratingUserListResponseDto = userRepository.findTopUsersByMaxRatingCount(3)
                .stream()
                .map(ratingUser -> new RatingUserResponseDto(ratingUser.getUserImg(), ratingUser.getNickname(), ratingUser.getMaxRatingCount()))
                .collect(Collectors.toList());

        List<Auction> auctionList = findTopAuctionByCount();
        List<AuctionListResponseDto> auctionResponseDtos = new ArrayList<>();
        for (Auction auction : auctionList) {
            TimeRemaining timeRemaining = auctionService.countDownTime(auction);
            boolean checkDibs = false;
            String imageUrl = imageRepository.findByGoodsGoodsIdOrderByCreatedAtAscFirst(auction.getGoods().getGoodsId()).getImageUrl();

            if(user != null) {
                checkDibs = dibsService.checkDibsGoods(user.getUser().getUserId(),auction.getGoods().getGoodsId());
            }
            AuctionListResponseDto auctionResponseDto = new AuctionListResponseDto(auction, imageUrl, timeRemaining, auctionService.findBidCount(auction.getAuctionId()), checkDibs);
            auctionResponseDtos.add(auctionResponseDto);
        }
        return new HomeResponseDto(goodsListResponseDtos, ratingUserListResponseDto, auctionResponseDtos);
    }

    public List<Auction> findTopAuctionByCount(){
        List<Auction> auctionLists = auctionRepository.findByAuctionStatus(AuctionStatus.AUCTION);
        if(auctionLists.size() <= 3) {
          return auctionRepository.findTop3Auction();
        }
        return auctionRepository.findTop3AuctionAll();
    }

}


// 마감된 경매는 나오지 않게 수정