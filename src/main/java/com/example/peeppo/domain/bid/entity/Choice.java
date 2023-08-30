package com.example.peeppo.domain.bid.entity;

import com.example.peeppo.domain.auction.entity.Auction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private Long choiceId;

    private String title;

    private String location;

    private String goodsImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id")
    private Bid bid;

    public Choice(Bid bid, Auction auction) {
        this.title = bid.getTitle();
        this.goodsImg = bid.getGoodsImg();
        this.location = bid.getLocation();
        this.bid = bid;
        this.auction = auction;
    }
}
