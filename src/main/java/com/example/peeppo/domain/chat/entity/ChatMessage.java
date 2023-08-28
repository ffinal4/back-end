package com.example.peeppo.domain.chat.entity;

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

    public void sendPerson(String nickname) {
        this.sender = nickname;
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
    private String sender; // 채팅을 보낸 사람
    private String message; //메시지

    private String time; //채팅 발송 시간

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message) {
        this.type = type;
      //  this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }
}
