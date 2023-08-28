package com.example.peeppo.domain.chat.repository;

import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
/*    ChatRoom findByRoomId(String roomId);

    List<ChatMessage> findAllByRoomId(String roomId);*/
  /*  List<ChatMessage> findAllByChatRoomAndSenderId(ChatRoom chatRoom, Long userId);*/

    List<ChatMessage> findAllByChatRoomId(Long id);
}
