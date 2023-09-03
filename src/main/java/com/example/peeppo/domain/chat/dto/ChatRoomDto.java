package com.example.peeppo.domain.chat.dto;

import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.entity.UserChatRoomRelation;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto implements Serializable {
    private Long id;
    private String roomId;
    private String nickname; // 상대방 닉네임
    private String imageUrl; //물품이미지
    //private String userimageUrl; // 사는 사람 , 사려고 하는 사람 나눠서 받아야함 => service 단에서 로직 추가 필요

    public ChatRoomDto(UserChatRoomRelation userChatRoom, User user) {
        this.id = userChatRoom.getId();
        this.roomId = userChatRoom.getChatRoom().getRoomId();
        this.imageUrl = userChatRoom.getChatRoom().getGoods().getImage().stream().map(Image::getImageUrl).toList().get(0);
        this.nickname = user.getNickname();
    }

}