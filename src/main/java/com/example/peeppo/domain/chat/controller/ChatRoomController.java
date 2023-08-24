package com.example.peeppo.domain.chat.controller;


import com.example.peeppo.domain.chat.dto.ChatMessageResponseDto;
import com.example.peeppo.domain.chat.dto.ChatRoomResponseDto;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.service.ChatService;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    //채팅방
    @PostMapping("/room/{goodsId}")
    public ChatRoomResponseDto createRoom(@PathVariable("goodsId") Long goodsId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return chatService.createRoom(goodsId, userDetails.getUser());
    }

    //채팅방 전체 조회 => 내 채팅방 전체 조회여야한다 !
    @GetMapping
    public List<ChatRoom> findAllRoom(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return chatService.findAllRoom(userDetails.getUser());
    }

    //채팅방 상세 조회
    @GetMapping("/room/{chatId}")
    public List<ChatMessageResponseDto> findChatRoom(@PathVariable("chatId") String roomId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        return chatService.findMessageById(roomId, userDetails.getUser());
    }

    @DeleteMapping("/room/{chatId}")
    public void deleteRoom(@PathVariable("chatId") String roomId){
        chatService.deleteChatRoom(roomId);
    }
}
