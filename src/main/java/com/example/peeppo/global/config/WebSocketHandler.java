package com.example.peeppo.global.config;

import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    //TextWebSocketHandler -> 채팅 서비스를 만들것이기 때문

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("{}", payload);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        // ObjectMapper를 통해서 해당 json 데이터를 chatMessage.class에 맞게 파싱해서 ChatMessage 객체로 변환하고
        // 이 json 데이터에 있는 roonId를 이용해서 해당 채팅방을 찾아 handlerAction()이라는 메서드를 실행시킨다.
        ChatRoom chatRoom = chatService.findRoomById(chatMessage.getRoomId());
        chatRoom.handlerActions(session, chatMessage, chatService);
        //handlerAction() 메서드는 이 참여자가 현재 이미 채팅방에 접속된 상태인지, 아니면 이미 채팅에 참여해있는 상태인지를 판별하여,
        //만약 채팅방에 처음 참여하는거라면 session을 연결해줌과 동시에 메시지를 보낼것이고 아니라면 메시지를 해당 채팅방으로 보내게 될 것이다.
    }



}
