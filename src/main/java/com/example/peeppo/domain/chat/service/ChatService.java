package com.example.peeppo.domain.chat.service;

import com.example.peeppo.domain.chat.dto.ChatRoomResponseDto;
import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.repository.ChatMessageRepository;
import com.example.peeppo.domain.chat.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private Map<String, ChatRoom> chatRooms;

    private final SimpMessageSendingOperations template;

    @PostConstruct // 의존성 주입이 이루어진 후 초기화 작업이 필요한 에 사용
    private void init(){
        chatRooms = new LinkedHashMap<>();
    } //순서대로 저장메서드

    public ChatRoomResponseDto createRoom(String title){
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .title(title)
                .build();
        chatRooms.put(randomId, chatRoom);
        chatRoomRepository.save(chatRoom);
        return new ChatRoomResponseDto(chatRoom);
    }

    //전체 채팅방 조회
    public List<ChatRoom> findAllRoom(){
        // 채팅방 생성 순서를 최근순으로 반환
        //List chatRoomList = new ArrayList<>(chatRooms.values());
       // Collections.reverse(chatRoomList);
        return chatRoomRepository.findAll();
    }

    //roomId 기준으로 채팅방 찾기
    public ChatRoom findRoomById(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }

    //roomId 기준으로 채팅방 메시지 내용 찾기
    public List<ChatMessage> findMessageById(String roomId) {
        return chatMessageRepository.findAllByRoomId(roomId);
    }

    //채팅방 유저 리스트에 유저 추가
    public String addUser(String roomId, String user){
        ChatRoom chatRoom = chatRooms.get(roomId);
        chatRoom.addSellerId(user);
        return user;
    }

    // 채팅방 유저 리스트 삭제
    public void delUser(String roomId, String user){
        ChatRoom room = chatRooms.get(roomId);
        room.remove(user);
    }

    public String getUserName(String roomId){
        ChatRoom room = chatRooms.get(roomId);
        return room.getUser();
    }

    public void saveMessage(ChatMessage chatMessage){
        System.out.println("메세지 발송 단계 진입");
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String dTime = formatter.format(systemTime);
        chatMessage.setTime(dTime);
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            System.out.println(chatMessage);
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");
            System.out.println(chatMessage);
        } else if (ChatMessage.MessageType.LEAVE.equals(chatMessage.getType())) {
            System.out.println(chatMessage);
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
            System.out.println(chatMessage);
        }
        chatMessageRepository.save(chatMessage);
        System.out.println("전송 요청");
        template.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        System.out.println("전송 완료");
    }


    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


    public void deleteChatRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        chatRoomRepository.delete(chatRoom);
    }
}
