package com.example.peeppo.domain.chat.dto;

import com.example.peeppo.domain.chat.entity.ChatMessage;
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
        this.nickname = chatMessage.getSender();
    }
}
