package com.example.peeppo.domain.chat.entity;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.RequestGoods;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserChatRoomRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "user_Id", nullable = false)
    private User seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id",  referencedColumnName = "user_Id", nullable = false)
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "chatRoom_id", nullable = false)
    private ChatRoom chatRoom;


    public UserChatRoomRelation(User user, ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        //this.seller = goods.getUser();
        this.buyer = user;
    }

    public UserChatRoomRelation(User enterUser, ChatRoom chatRoom, User buyerUser) {
        this.chatRoom = chatRoom;
        this.seller = enterUser;
        this.buyer = buyerUser;
    }
}
