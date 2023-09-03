package com.example.peeppo.domain.chat.dto;

import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {

    private String nickname;
    private String userImageUrl;
    private String message;

    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.message = chatMessage.getMessage();
    }

    public ChatMessageResponseDto(ChatMessage chatMessage, User messageUser) {
        this.message = chatMessage.getMessage();
 //       this.userImageUrl = messageUser.getUserImg();
        this.nickname = messageUser.getNickname();
    }
}
