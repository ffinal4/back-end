package com.example.peeppo.domain.goods.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "goods_id")
    private Goods goods;

}
