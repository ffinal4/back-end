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
    private Long sellerPrice;
    private Long sumRatingPrice;
    private Long avgRatingPrice;
    private Long ratingCount;

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    @JsonBackReference
    private Goods goods;
    @ManyToOne
    @JoinColumn(name = "image_key", nullable = false)
    @JsonBackReference
    private Image image;

    public Rating(Long sellerPrice, Goods goods, Image image) {
        this.sellerPrice = sellerPrice;
        this.sumRatingPrice = 0L;
        this.avgRatingPrice = 0L;
        this.ratingCount = 0L;
        this.goods = goods;
        this.image = image;
    }
    public void update(Long ratingPrice){
        this.sumRatingPrice += ratingPrice;
        this.ratingCount += 1;
        this.avgRatingPrice = sumRatingPrice / this.ratingCount;
    }
}
