package com.example.peeppo.domain.dibs.entity;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Dibs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dibs_id")
    private Long dibsId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Dibs(Goods goods, User user) {
        this.goods = goods;
        this.user = user;
    }
}
