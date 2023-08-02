package com.example.peeppo.domain.chat.controller;

import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.repository.ChatMessageRepository;
import com.example.peeppo.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController { //채팅을 수신하고 송신하기 위한 컨트롤러
    private final SimpMessageSendingOperations template;
    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;

    // MessageMapping 을 통해 webSocket 로 들어오는 메시지를 발신 처리한다.
    // 이때 클라이언트에서는 /pub/chat/message 로 요청하게 되고 이것을 controller 가 받아서 처리한다.
    // 처리가 완료되면 /sub/chat/room/roomId 로 메시지가 전송된다.
    /*
    @MessageMapping("/chat/enter")
    public void enterUser(@Payload ChatMessage chat, SimpMessageHeaderAccessor headerAccessor){
        //채팅방에 유저 추가 및 유저이름반환
        String user = chatService.addUser(chat.getRoomId(), chat.getSender());
        //반환 결과를 socket session에 사용자 이름으로 저장
        headerAccessor.getSessionAttributes().put(chat.getRoomId(), chat.getRoomId());
        headerAccessor.getSessionAttributes().put(chat.getSender(), user);

        chat.setMessage(chat.getSender() + "님과의 거래가 시작되었습니다");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }
    */

    // 해당 유저
    @MessageMapping("/chatroom/{id}")
    public void sendMessage(@DestinationVariable("id") String id, @Payload ChatMessage chat) {
        log.info("CHAT {}", chat);
        chatService.saveMessage(chat);
    }
/*
    // 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 사용자와 roomId 를 확인해서 채팅방 유저와 room 에서 해당 유저를 삭제
        String user = (String) headerAccessor.getSessionAttributes().get("user");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);

        // 채팅방 유저 리스트에서 유저 닉네임 조회 및 리스트에서 유저 삭제
        String username = chatService.getUserName(roomId);
        chatService.delUser(roomId, username);

        if (username != null) {
            log.info("User Disconnected : " + username);

            // builder 어노테이션 활용
            ChatMessage chat = ChatMessage.builder()
                    .type(ChatMessage.MessageType.LEAVE)
                    .roomId(roomId)
                    .sender(username)
                    .message(username + " 님 퇴장!!")
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId, chat);
        }
    }
    */

}
