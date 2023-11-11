package com.example.peeppo.domain.bid.entity;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.bid.enums.BidStatus;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Bid extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Long bidId;

    private String title;

    private String location;

    private String goodsImg;

    private boolean sellersPick;

    @Enumerated(EnumType.STRING)
    private BidStatus bidStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    public Bid(User user, Auction auction, Goods goods, String goodsImg, BidStatus bidStatus) {
        this.location = user.getLocation();
        this.title = goods.getTitle();
        this.goodsImg = goodsImg;
        this.user = user;
        this.auction = auction;
        this.goods = goods;
        this.bidStatus = bidStatus;
    }

    public void update(User user, Auction auction, Goods goods, String goodsImg) {
        this.location = user.getLocation();
        this.title = goods.getTitle();
        this.goodsImg = goodsImg;
        this.user = user;
        this.auction = auction;
        this.goods = goods;
    }

    public void changeBidStatus(BidStatus bidStatus) {
        this.bidStatus = bidStatus;
    }

    public void setSellersPick(){
        this.sellersPick = !this.sellersPick;
    }
}
