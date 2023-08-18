package com.example.peeppo.domain.goods.entity;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.dibs.entity.Dibs;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.goods.dto.GoodsRequestDto;
import com.example.peeppo.domain.goods.enums.Category;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;


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

    private Long sellerPrice;
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

    @OneToMany(mappedBy = "goods")
    private List<Dibs> dibs;



    public Goods(GoodsRequestDto requestDto, WantedGoods wantedGoods){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
        this.goodsCondition = requestDto.getGoodsCondition();
        this.tradeType = requestDto.getTradeType();
        this.category = requestDto.getCategory();
        this.wantedGoods = wantedGoods;
    }

    public Goods(GoodsRequestDto requestDto, WantedGoods wantedGoods, User user, GoodsStatus goodsStatus) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
        this.goodsCondition = requestDto.getGoodsCondition();
        this.tradeType = requestDto.getTradeType();
        this.category = requestDto.getCategory();
        this.sellerPrice = requestDto.getSellerPrice();
        this.wantedGoods = wantedGoods;
        this.goodsStatus = goodsStatus;
        this.user = user;
    }

    public void update(GoodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
    }

    public void changeStatus(GoodsStatus goodsStatus) {
        this.goodsStatus = goodsStatus;
    }
}


