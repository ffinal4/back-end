package com.example.peeppo.domain.chat.repository;

import com.example.peeppo.domain.chat.entity.UserChatRoomRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChatRoomRelationRepository extends JpaRepository<UserChatRoomRelation, Long> {
  //  List<UserChatRoomRelation> findAllBySellerIdAndBuyerId(Long userId);
 //   List<UserChatRoomRelation> findAllBySellerUserIdOrBuyerUserId(Long userId, Long id);

  List<UserChatRoomRelation> findAllByBuyerUserId(Long userId);

  UserChatRoomRelation findByChatRoomId(String roomId);
}
