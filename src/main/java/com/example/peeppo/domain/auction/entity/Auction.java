package com.example.peeppo.domain.auction.entity;

import com.example.peeppo.domain.auction.dto.AuctionRequestDto;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.rating.entity.Rating;
import com.example.peeppo.domain.rating.entity.RatingGoods;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.utils.Timestamped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "auction",
        indexes = {@Index(columnList = "auction_end_time")}
)
public class Auction extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionId;

    // TODO: 2023-10-15 종료 시간을 사용하여 정렬
    @Column(name = "auction_end_time", nullable = false)
    private LocalDateTime auctionEndTime; // 경매(입찰) 종료 시간

    @Min(1000)
    private double avgPrice;
    private double lowPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "auction_status", nullable = false, length = 191)
    private AuctionStatus auctionStatus;

    @ManyToOne
    @JoinColumn(name = "usere_id", nullable = false)
    private User user;

    @ManyToOne // 부모가 삭제되어도 자식은 남아있도록
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    private boolean isDeleted;

    public Auction(Goods getGoods, LocalDateTime auctionEndTime, User user, RatingGoods ratingGoods, Double lowPrice) {
        this.goods = getGoods;
        this.isDeleted = false;
        this.auctionEndTime = auctionEndTime;
        this.avgPrice = ratingGoods.getAvgRatingPrice();
        this.lowPrice = lowPrice;
        this.user = user;
    }

    public void changeDeleteStatus(boolean b) {
        this.isDeleted = b;
    }

    public void changeAuctionStatus(AuctionStatus auctionStatus) {
        this.auctionStatus = auctionStatus;
    }
}