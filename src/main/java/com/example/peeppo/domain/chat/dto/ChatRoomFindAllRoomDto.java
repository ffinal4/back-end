package com.example.peeppo.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomFindAllRoomDto {
    private String myImageUrl;
    private List<ChatRoomResponseDto> chatRoomResponseDtos;

    public ChatRoomFindAllRoomDto(List<ChatRoomResponseDto> chatRoomResponseDto) {
        this.chatRoomResponseDtos = chatRoomResponseDto;
    }
}
