package com.example.peeppo.domain.goods.entity;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.dibs.entity.Dibs;
import com.example.peeppo.domain.goods.enums.GoodsStatus;
import com.example.peeppo.domain.goods.dto.GoodsRequestDto;
import com.example.peeppo.domain.goods.enums.Category;
import com.example.peeppo.domain.goods.enums.RequestedStatus;
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
    @Column(nullable = false)
    private Boolean isDeleted = false;

    private Boolean ratingCheck;
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

    @Enumerated(EnumType.STRING)
    private RequestedStatus requestedStatus;

    @OneToMany(mappedBy = "goods")
    private List<Dibs> dibs;

    @OneToMany(mappedBy = "goods")
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "goods")
    private List<RequestGoods> requestGoods;

    public Goods(GoodsRequestDto requestDto, WantedGoods wantedGoods) {
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
        this.ratingCheck = requestDto.getRatingCheck();
        if (!ratingCheck) {
            sellerPrice = 0L;
        } else if (null != requestDto.getSellerPrice()) {
            this.sellerPrice = requestDto.getSellerPrice();
        } else {
            throw new IllegalArgumentException("올바르지 않은 값입니다.");
        }
        this.wantedGoods = wantedGoods;
        this.goodsStatus = goodsStatus;
        this.user = user;
    }

    public void update(GoodsRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void changeStatus(GoodsStatus goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Goods(String title, String content, String location) {
        this.title = title;
        this.content = content;
        this.location = location;
    }

    public void setRequestedStatus(RequestedStatus requestedStatus) {
        this.requestedStatus = requestedStatus;
    }

    public void changeCheck(Boolean ratingCheck) {
        this.ratingCheck = ratingCheck;
    }
}


