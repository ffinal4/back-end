package com.example.peeppo.domain.chat.repository;

import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
/*    ChatRoom findByRoomId(String roomId);

    List<ChatMessage> findAllByRoomId(String roomId);*/
  /*  List<ChatMessage> findAllByChatRoomAndSenderId(ChatRoom chatRoom, Long userId);*/

    List<ChatMessage> findAllByChatRoomId(Long id);

    @Query(value = "SELECT c FROM ChatMessage c WHERE c.chatRoom.id = :chatRoom_id ORDER BY c.id DESC limit 1")
    ChatMessage findByChatRoomId(@Param("chatRoom_id")Long id);
    // 충돌 List<ChatMessage> findAllByChatRoomRoomId(String roomId);
}
