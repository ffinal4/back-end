package com.example.peeppo.domain.rating.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;
    private Long sellerPrice;
    private Long sumRatingPrice;
    private Long avgRatingPrice;
    private Long ratingCount;

    public Rating(Long sellerPrice){
        this.sellerPrice = sellerPrice;
        this.sumRatingPrice = 0L;
        this.avgRatingPrice = 0L;
        this.ratingCount = 0L;

    }

//    public Rating(Long ratingPrice) {
//        this.ratingCount += 1;
//        this.sumRatingPrice += ratingPrice;
//        this.avgRatingPrice = sumRatingPrice/ratingCount;
//    }
}
