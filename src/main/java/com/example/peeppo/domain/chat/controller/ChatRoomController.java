package com.example.peeppo.domain.chat.controller;


import com.example.peeppo.domain.chat.dto.ChatMessageResponseDto;
import com.example.peeppo.domain.chat.dto.ChatRoomDto;
import com.example.peeppo.domain.chat.dto.ChatRoomResponseDto;
import com.example.peeppo.domain.chat.service.ChatService;
import com.example.peeppo.domain.chat.dto.ChatGoodsRequestDto;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatService chatService;

    //채팅방 생성
    @PostMapping
    public ChatRoomResponseDto createRoom(@RequestBody ChatGoodsRequestDto chatGoodsRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return chatService.createRoom(chatGoodsRequestDto,  userDetails.getUser());
    }

    //채팅방 전체 조회 => 내 채팅방 전체 조회여야한다 !
    @GetMapping
    public List<ChatRoomDto> findAllRoom(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return chatService.findAllRoom(userDetails.getUser());
    }

    //채팅방 메세지 조회 ( 상세 조회 )
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
