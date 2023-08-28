package com.example.peeppo.domain.chat.dto;

import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.entity.UserChatRoomRelation;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponseDto {
    private Long id;
    private String roomId;
    private String nickname; // 상대방 닉네임
    private String imageUrl; //물품이미지
    private String userimageUrl; // 사는 사람 , 사려고 하는 사람 나눠서 받아야함 => service 단에서 로직 추가 필요

    public ChatRoomResponseDto(ChatRoom chatRoom, User user) {
        this.id = chatRoom.getId();
        this.roomId = chatRoom.getRoomId();
       this.nickname = user.getNickname();
        this.userimageUrl = user.getUserImg();
        this.imageUrl = chatRoom.getGoods().getImage().stream().map(Image::getImageUrl).toList().get(0);
    }

    public ChatRoomResponseDto(UserChatRoomRelation userChatRoom) {
        this.id = userChatRoom.getId();
        this.roomId = userChatRoom.getChatRoom().getRoomId();
        this.imageUrl = userChatRoom.getChatRoom().getGoods().getImage().stream().map(Image::getImageUrl).toList().get(0);
        this.nickname = userChatRoom.getChatRoom().getGoods().getUser().getNickname();
        this.userimageUrl = userChatRoom.getChatRoom().getGoods().getUser().getUserImg();

    }


}
