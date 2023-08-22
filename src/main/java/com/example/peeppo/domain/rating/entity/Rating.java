package com.example.peeppo.domain.rating.entity;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.image.entity.Image;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Long expectedPrice;

    @ManyToOne
    @JoinColumn(name = "rating_goods_id", nullable = false)
    @JsonBackReference
    private RatingGoods ratingGoods;

    public Rating(Long expectedPrice, RatingGoods ratingGoods) {
        this.expectedPrice = expectedPrice;
        this.ratingGoods = ratingGoods;
    }
}
