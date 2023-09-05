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

    private Boolean checkSameUser;
    private String nickname;
    private String message;
    private String time;


    public ChatMessageResponseDto(ChatMessage chatMessage, User messageUser) {
        this.message = chatMessage.getMessage();
        this.nickname = messageUser.getNickname();
        this.time = chatMessage.getTime();
    }

    public ChatMessageResponseDto(ChatMessage chatMessage, User messageUser, boolean checkUser) {
        this.checkSameUser = checkUser;
        this.message = chatMessage.getMessage();
        this.nickname = messageUser.getNickname();
        this.time = chatMessage.getTime();
    }
}
