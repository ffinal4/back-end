package com.example.peeppo.domain.rating.entity;

import com.example.peeppo.domain.goods.entity.Goods;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class RatingGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingGoodsId;
    private Long sumRatingPrice = 0L;
    private Double avgRatingPrice = 0D;
    private Double nextAvgRatingPrice = 0D;
    private Long ratingCount = 0L;

    @OneToOne
    @JoinColumn(name = "goods_id", nullable = false)
    @JsonBackReference
    private Goods goods;

    @Version
    private Long version;

    public RatingGoods(Rating rating, Goods goods) {
        this.sumRatingPrice = rating.getExpectedPrice();
        this.avgRatingPrice = Double.valueOf(rating.getExpectedPrice());
        this.goods = goods;
        this.ratingCount += 1;
    }

    public void update(Long getExpectedPrice, Goods goods){
        this.sumRatingPrice += getExpectedPrice;
        this.ratingCount += 1;
        this.goods = goods;

        if(this.ratingCount == 3){
            this.avgRatingPrice = Math.round((sumRatingPrice / this.ratingCount) / 1000.0) * 1000.0;
        }
        this.nextAvgRatingPrice = Math.round((sumRatingPrice / this.ratingCount) / 1000.0) * 1000.0;

    }

}
