package com.example.peeppo.domain.chat.entity;

import com.example.peeppo.domain.chat.service.ChatService;
import com.example.peeppo.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class ChatRoom extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomId;
    private String title;

    //private String sellerId;
    @Transient //Serialize하는 과정을 제외하고 싶은 경우 선언하는 키워드
    private Set<WebSocketSession> sessions = new HashSet<>(); // 중복저장을 막는다

    @Builder
    public ChatRoom(String roomId, String title){
        this.roomId = roomId;
        this.title = title;
    }

    public ChatRoom() {

    }

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
}
