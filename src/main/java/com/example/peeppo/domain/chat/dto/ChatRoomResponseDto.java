package com.example.peeppo.domain.chat.dto;

import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.image.entity.Image;
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
    private String sender; //보낸사람
    private String peopleImageUrl; //보낸사람 이미지
    private String imageUrl; //물품이미지
    private String recentMessage; //최근 대화내용

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomId = chatRoom.getRoomId();
        this.imageUrl = chatRoom.getGoods().getImage().stream().map(Image::getImageUrl).toList().get(0);
        this.createdAt = chatRoom.getCreatedAt();
        this.modifiedAt = chatRoom.getModifiedAt();
    }
}
