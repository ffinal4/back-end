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

    @ManyToOne
    @JoinColumn(name = "sellerGoods_id", nullable = false)
    @JsonBackReference
    private Goods seller;

    @ManyToOne
    @JoinColumn(name = "buyerGoods_id", nullable = false)
    @JsonBackReference
    private Goods buyer;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private Long receiveUser;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public RequestGoods(Goods urGoods, User user, Goods goods, RequestStatus requestStatus) {
        this.seller = urGoods;
        this.receiveUser = urGoods.getUser().getUserId();
        this.user = user;
        this.buyer = goods;
        this.requestStatus = requestStatus;
    }

    public RequestGoods(User user, Goods urGoods, Goods goods, RequestStatus requestStatus) {
        this.user = user;
        this.seller = urGoods;
        this.buyer = goods;
        this.requestStatus = requestStatus;
    }

    public void changeStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }


}
