package com.example.peeppo.domain.chat.dto;

import com.example.peeppo.domain.chat.entity.ChatRoom;
import com.example.peeppo.domain.chat.entity.UserChatRoomRelation;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.goods.entity.RequestGoods;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponseDto {
    private String roomId; // 채팅방 Id

    private Long sellerGoodsId;
    private String sellerNickname; //
    private String sellerGoodsImage; // 파는사람의 물품이미지
   // private String sellerImageUrl; // 사는 사람 , 사려고 하는 사람 나눠서 받아야함 => service 단에서 로직 추가 필요

    private Long buyerGoodsId;
    private String buyerNickname;
    private String buyerGoodsImage; //사는 사람의 물품이미지
   // private String buyerImageUrl; // 사는 사람 , 사려고 하는 사람 나눠서 받아야함 => service 단에서 로직 추가 필요



    public ChatRoomResponseDto(ChatRoom chatRoom, Goods goods, User enterUser, RequestGoods requestGoods) {
        this.roomId = chatRoom.getRoomId();
        this.sellerGoodsId = goods.getGoodsId();
        this.sellerGoodsImage = goods.getImage().stream().map(Image::getImageUrl).collect(Collectors.toList()).get(0);
        this.sellerNickname = enterUser.getNickname();
        this.buyerGoodsId = requestGoods.getBuyer().getGoodsId();
        this.buyerNickname = requestGoods.getBuyer().getUser().getNickname();
        this.buyerGoodsImage = requestGoods.getBuyer().getImage().stream().map(Image::getImageUrl).collect(Collectors.toList()).get(0);
    }


}
