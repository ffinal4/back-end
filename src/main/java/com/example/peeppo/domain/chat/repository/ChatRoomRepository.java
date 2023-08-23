package com.example.peeppo.domain.chat.repository;

import com.example.peeppo.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom , Long> {

    ChatRoom findByRoomId(String roomId);
}
