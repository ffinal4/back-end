package com.example.peeppo.domain.goods.entity;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.bid.enums.GoodsStatus;
import com.example.peeppo.domain.goods.dto.GoodsRequestDto;
import com.example.peeppo.domain.goods.enums.Category;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.utils.Timestamped;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
public class Goods extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Long goodsId;
    private String title;
    private String content;
    private String location;
    private String goodsCondition;
    private String tradeType;
    @Enumerated(EnumType.STRING) // ENUM타입을 String으로 넣음
    private Category category;

    private boolean isDeleted;
//    @OneToOne(fetch = FetchType.LAZY)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wanted_id")
    private WantedGoods wantedGoods;

    @OneToOne(mappedBy = "goods")
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @OneToMany(mappedBy = "goods")
    private List<Image> image;

    @Enumerated(EnumType.STRING)
    private GoodsStatus goodsStatus;

    public Goods(GoodsRequestDto requestDto, WantedGoods wantedGoods) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
        this.goodsCondition = requestDto.getGoodsCondition();
        this.tradeType = requestDto.getTradeType();
        this.category = requestDto.getCategory();
        this.wantedGoods = wantedGoods;
    }

    public Goods(GoodsRequestDto requestDto, WantedGoods wantedGoods, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
        this.goodsCondition = requestDto.getGoodsCondition();
        this.tradeType = requestDto.getTradeType();
        this.category = requestDto.getCategory();
        this.wantedGoods = wantedGoods;
        this.user = user;
    }

    public void update(GoodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
    }
}
