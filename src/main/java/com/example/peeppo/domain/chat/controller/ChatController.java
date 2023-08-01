package com.example.peeppo.domain.chat.controller;


import com.example.peeppo.domain.chat.dto.ChatRoomRequestDto;
import com.example.peeppo.domain.chat.dto.ChatRoomResponseDto;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ChatRoomResponseDto createRoom(@RequestBody ChatRoomRequestDto chatRoomDto){
        return chatService.createRoom(chatRoomDto.getTitle());
    }

    @GetMapping
    public List<ChatRoom> findAllRoom(){
        return chatService.findAllRoom();
    }
}
