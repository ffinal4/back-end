package com.example.peeppo.domain.chat.dto;

import com.example.peeppo.domain.chat.entity.ChatMessage;
import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.entity.UserChatRoomRelation;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.RequestGoods;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.entity.UserImage;
import com.example.peeppo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.io.StringBufferInputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponseDto {
    private Long id;
    private String roomId;
    private String imageUrl; //물품이미지
    private String title;
    private String otherImageUrl;
    private String nickname;
    private String recentMessage;
    private String recentMessageTime;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomId = chatRoom.getRoomId();
        this.imageUrl = chatRoom.getGoods().getImage().get(0).getImageUrl();
    }



    public ChatRoomResponseDto(UserChatRoomRelation userChatRoom, ChatMessage chatMessage, UserImage otherImage, String nickname) {
        this.id = userChatRoom.getId();
        this.roomId = userChatRoom.getChatRoom().getRoomId();
        this.title = userChatRoom.getChatRoom().getGoods().getTitle();
        this.nickname = nickname;
        this.imageUrl = userChatRoom.getChatRoom().getGoods().getImage().get(0).getImageUrl();
        this.otherImageUrl = otherImage.getImageUrl();
        this.recentMessage = chatMessage.getMessage();
        this.recentMessageTime = chatMessage.getTime();
    }

    public ChatRoomResponseDto(UserChatRoomRelation userChatRoom, ChatMessage chatMessage, String nickname) {
        this.id = userChatRoom.getId();
        this.roomId = userChatRoom.getChatRoom().getRoomId();
        this.title = userChatRoom.getChatRoom().getGoods().getTitle();
        this.nickname = nickname;
        this.imageUrl = userChatRoom.getChatRoom().getGoods().getImage().get(0).getImageUrl();
        this.recentMessage = chatMessage.getMessage();
        this.recentMessageTime = chatMessage.getTime();
    }
}