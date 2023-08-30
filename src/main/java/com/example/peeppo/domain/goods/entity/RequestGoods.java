package com.example.peeppo.domain.goods.entity;

import com.example.peeppo.domain.goods.enums.RequestStatus;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.utils.Timestamped;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class RequestGoods extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    private Long sellerId;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    @JsonBackReference
    private Goods goods;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public RequestGoods(Long urGoodsId, User user, Goods goods, RequestStatus requestStatus) {
        this.sellerId = urGoodsId;
        this.user = user;
        this.goods = goods;
        this.requestStatus = requestStatus;
    }

    public void changeStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
