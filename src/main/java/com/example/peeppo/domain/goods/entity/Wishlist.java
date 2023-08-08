package com.example.peeppo.domain.goods.entity;

import jakarta.persistence.*;

@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "goods_id") // 연결할 외래키 컬럼명
    private Goods goods;

}
