package com.example.peeppo.domain.chat.repository;

import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
/*    ChatRoom findByRoomId(String roomId);

    List<ChatMessage> findAllByRoomId(String roomId);*/
  /*  List<ChatMessage> findAllByChatRoomAndSenderId(ChatRoom chatRoom, Long userId);*/

    @Query(value = "SELECT c FROM ChatMessage c WHERE c.chatRoom.id = :chatRoom_id ORDER BY c.id")
    List<ChatMessage> findAllChatRoomId(@Param("chatRoom_id")Long id);

    @Query(value = "SELECT c FROM ChatMessage c WHERE c.chatRoom.id = :chatRoom_id ORDER BY c.id DESC limit 1")
    ChatMessage findChatRoomId(@Param("chatRoom_id")Long id);

    Slice<ChatMessage> findChatMessagesByChatRoomIdOrderByTimeDesc(Long id, Pageable page);
    // 충돌 List<ChatMessage> findAllByChatRoomRoomId(String roomId);
}
