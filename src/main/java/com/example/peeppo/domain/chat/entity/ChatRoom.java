package com.example.peeppo.domain.chat.entity;

import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
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

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessage;

   // @Transient //Serialize하는 과정을 제외하고 싶은 경우 선언하는 키워드
   // private Set<WebSocketSession> sessions = new HashSet<>(); // 중복저장을 막는다

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public ChatRoom(String roomId, Goods goods, User user){
        this.roomId = roomId;
        this.goods = goods;
        this.user = user;
    }

    /*

    public void handlerActions(WebSocketSession session, ChatMessage message, ChatService chatService){
        if(message.getType().equals(ChatMessage.MessageType.ENTER)){
            sessions.add(session);
            message.setMessage(message.getSender() + "님이 입장했습니다.");
        }
        sendMessage(message, chatService);
    }

    private <T> void sendMessage(T message, ChatService chatService){
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }
    */


/*    public void addSellerId(String user) {
        this.user= user;
    }*/

    public void remove(String user) {
        this.user = null;
    }
}
