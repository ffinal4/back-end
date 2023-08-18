package com.example.peeppo.domain.auction.entity;

import com.example.peeppo.domain.auction.dto.AuctionRequestDto;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.rating.entity.RatingGoods;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.utils.Timestamped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Auction extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionId;

    @Column
    private LocalDateTime auctionEndTime;

    @Min(1000)
    @Column
    private double lowPrice;

    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

/*
    @OneToMany(mappedBy = "auction")
    private List<AuctionList> auctionList = new ArrayList<AuctionList>();
//두 객체중 하나의 객체만 테이블을 관리할 수 있도록 정하는 것이 MappedBy 옵션
*/
    @OneToOne(cascade = CascadeType.DETACH) // 부모가 삭제되어도 자식은 남아있도록
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @ColumnDefault("false")
    private Boolean isDeleted;

    public Auction(Goods getGoods, LocalDateTime auctionEndTime, User user, RatingGoods ratingGoods) {
        this.goods = getGoods;
        this.isDeleted = false;
        this.auctionEndTime = auctionEndTime;
        this.lowPrice = ratingGoods.getAvgRatingPrice();
        this.user = user;
    }

    public void changeDeleteStatus(boolean b) {
        this.isDeleted = b;
    }

    public void changeAuctionStatus(AuctionStatus auctionStatus) {
        this.auctionStatus = auctionStatus;
    }
}