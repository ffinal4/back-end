package com.example.peeppo.domain.notification.enums;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    AUCTION, // 새로운 입찰이 들어왔을때 => 새로운 입찰
    AUCTIONPICK, // 경매에서 상대방이 내 물건을 입찰한 경우 => 경매완료
    AUCTIONEND, // 경매가 종료되었을때
    REQUEST, // 교환신청
    REQUESTEND, // 교환이 수락되었을때

    REQUESTREFUSE, // 교환이 거절되었을때
    CHAT; // 채팅
}
