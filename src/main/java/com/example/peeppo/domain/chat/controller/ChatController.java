package com.example.peeppo.domain.chat.controller;

import com.example.peeppo.domain.chat.dto.ChatMessageRequestDto;
import com.example.peeppo.domain.chat.repository.ChatMessageRepository;
import com.example.peeppo.domain.chat.service.ChatService;
import com.example.peeppo.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController { //채팅을 수신하고 송신하기 위한 컨트롤러
    private final SimpMessageSendingOperations template;
    private final ChatService chatService;
    private final JwtUtil jwtUtil;
    private final ChatMessageRepository chatMessageRepository;
    /**
     * websocket "/pub/chatroom/{id}"로 들어오는 메시징을 처리한다.
     */

    // 메세지 보내기
    @MessageMapping("/chatroom/{chatroomId}")
    public void sendMessage(@DestinationVariable("chatroomId") String id, @Payload ChatMessageRequestDto chat,
                            @Header("AccessToken") String token) {
        log.info("CHAT {}", chat);
        chatService.saveMessage(id,chat,token);
    }
// @Header("AccessToken") String token
// @AuthenticationPrincipal UserDetailsImpl userDetails
}
