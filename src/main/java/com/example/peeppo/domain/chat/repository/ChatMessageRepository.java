package com.example.peeppo.domain.chat.repository;

import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
