package com.example.peeppo.domain.auction.event;

import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.notification.enums.NotificationStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuctionEvent {
    private final Goods goods;
//    private final NotificationStatus notificationStatus;
    private final AuctionStatus auctionStatus;
}
