
package com.example.peeppo.global.redis.config;


import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/*
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    //리스너에 수신된 메시지를 각 비지니스 로직을 거쳐 messaging Template을 이용해 Websocket
    //구독자들에게 메시지를 전달하는 메소드
    //Redis로 부터 온 메시지를 역직렬화하여 ChatMesageDto로 전환한 뒤에 필요한 정보와 함께 메시지를 전달한다
    @Override
    public void sendMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            if (roomMessage.getType().equals(ChatMessage.MessageType.TALK)) {
                ChatMessage chatMessageResponse = new ChatMessage(roomMessage);
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), chatMessageResponse);
            }

            ...

        } catch (Exception e) {
            throw new ChatMessageNotFoundException();
        }
    }
}
*/
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */

    public void sendMessage(String publishMessage) {
        try {

            System.out.println("sub 메세지 확인");
            // ChatMessage 객채로 맵핑
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            System.out.println("message 맵핑 완료");
            System.out.println(chatMessage);
            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChatRoom().getRoomId(), chatMessage);//수정
            System.out.println("발송 요청 완료");

        } catch (Exception e) {
            log.error("Exception {}", e);
        }

    }

}