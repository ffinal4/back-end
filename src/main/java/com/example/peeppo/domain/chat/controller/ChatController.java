package com.example.peeppo.domain.chat.controller;

import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.repository.ChatMessageRepository;
import com.example.peeppo.domain.chat.service.ChatService;
import com.example.peeppo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController { //채팅을 수신하고 송신하기 위한 컨트롤러
    private final SimpMessageSendingOperations template;
    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;
    /**
     * websocket "/pub/chatroom/{id}"로 들어오는 메시징을 처리한다.
     */
    // id 는 userid를 의미
    @MessageMapping("/chatroom/{chatroomId}")
    public void sendMessage(@DestinationVariable("chatroomId") String id, @Payload ChatMessage chat,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("CHAT {}", chat);
        chatService.saveMessage(chat, userDetails.getUser());
    }

}
