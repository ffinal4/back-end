package com.example.peeppo.domain.chat.controller;


import com.example.peeppo.domain.chat.dto.ChatRoomRequestDto;
import com.example.peeppo.domain.chat.dto.ChatRoomResponseDto;
import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    //채팅방
    @PostMapping("/room")
    public ChatRoomResponseDto createRoom(@RequestBody ChatRoomRequestDto chatRoomDto){
        return chatService.createRoom(chatRoomDto.getTitle());
    }

    //채팅방 전체 조회
    @GetMapping
    public List<ChatRoom> findAllRoom(){
        return chatService.findAllRoom();
    }

    //채팅방 상세 조회
    @GetMapping("/room/{chatId}")
    public List<ChatMessage> findChatRoom(@PathVariable("chatId") String roomId){
        return chatService.findMessageById(roomId);
    }

    @DeleteMapping("/room/{chatId}")
    public void deleteRoom(@PathVariable("chatId") String roomId){
        chatService.deleteChatRoom(roomId);
    }
}
