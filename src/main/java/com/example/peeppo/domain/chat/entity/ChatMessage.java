package com.example.peeppo.domain.chat.entity;

import com.example.peeppo.domain.chat.dto.ChatMessageRequestDto;
import com.example.peeppo.domain.user.entity.User;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatMessage {


    public void sendMessage(String enterMessage) {
        this.message = enterMessage;
    }

    public void sendPerson(Long id) {
        this.senderId = id;
    }

    public enum MessageType{
        ENTER, TALK, LEAVE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MessageType type; //메시지 타입
    @ManyToOne
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom; // 방번호
    private Long senderId; // 채팅을 보낸 사람
    private String message; //메시지
    private String time; //채팅 발송 시간

    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto, ChatRoom chatRoom, String dTime, User user) {
        this.type = chatMessageRequestDto.getMessageType();
        this.chatRoom = chatRoom;
        this.senderId = user.getUserId();
        this.message = chatMessageRequestDto.getMessage();
        this.time = dTime;
    }

    @Builder
    public ChatMessage(MessageType type, ChatRoom chatRoom, Long userId,String message, String createdAt) {
        this.type = type;
        this.chatRoom = chatRoom;
        this.senderId = userId;
        this.message = message;
        this.time = createdAt;
    }

}
