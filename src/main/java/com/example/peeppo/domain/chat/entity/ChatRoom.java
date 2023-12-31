package com.example.peeppo.domain.chat.entity;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.utils.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatRoom extends Timestamped implements Serializable {

    @Serial
    private static final long serialVersionUID = 5056853071687151531L;
    //redis는 data를 hash해 저장하기 때문에, redis에 저장할 객체는 serializable를 implements 해야한다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomId; //채팅방 아이디
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    public ChatRoom(Goods goods, String roomId) {
        this.goods = goods;
        this.roomId = roomId;
    }

}
