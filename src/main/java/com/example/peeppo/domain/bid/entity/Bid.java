package com.example.peeppo.domain.bid.entity;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Long bidId;

    private String title;

    private String location;

    private String goodsImg;
    //굳이 필요할까?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @ManyToOne(fetch = FetchType.EAGER) //나중에 lazy로 변환
    @JoinColumn(name = "auction_id")
    private Auction auction;

    public Bid(User user, Auction auction, Goods goods, String goodsImg) {
        this.location = user.getLocation();
        this.title = goods.getTitle();
        this.goodsImg = goodsImg;
        this.user = user;
        this.auction = auction;
        this.goods = goods;
    }

    public void update(Auction auction, User user, Goods goods, String goodsImg){
        this.location = user.getLocation();
        this.title = goods.getTitle();
        this.goodsImg = goodsImg;
        this.user = user;
        this.auction = auction;
        this.goods = goods;
    }
}
