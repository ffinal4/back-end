package com.example.peeppo.domain.auction.event;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.auction.enums.AuctionStatus;
import com.example.peeppo.domain.auction.repository.AuctionRepository;
import com.example.peeppo.domain.bid.entity.Bid;
import com.example.peeppo.domain.bid.repository.bid.BidRepository;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.notification.entity.Notification;
import com.example.peeppo.domain.notification.enums.NotificationStatus;
import com.example.peeppo.domain.notification.repository.NotificationRepository;
import com.example.peeppo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Async
@Transactional
@Component
@RequiredArgsConstructor
public class AuctionEventListener {

    private final NotificationRepository notificationRepository;

    //메세지도 같이 보내줘야될듯?
    @EventListener
    public void handleAuctionEvent(AuctionEvent auctionEvent) {

        //       NotificationStatus notificationStatus = auctionEvent.getNotificationStatus();
        List<Bid> bidList = auctionEvent.getBidList();
        Auction auction = auctionEvent.getAuction();

        Notification notification = notificationRepository.findByUserUserId(auction.getUser().getUserId());

        if (notification == null) {
            noticeToSeller(auction);
        } else {
            notification.Checked(false);
            notification.setIsAuction(false);
            notification.updateAuctionCount();
            notificationRepository.save(notification);
        }

        if (auction.getAuctionStatus().equals(AuctionStatus.DONE)) {
            if (notification == null) {
                noticeToBuyer(bidList);
            } else {
                notification.Checked(false);
                notification.setIsAuction(false);
                notification.updateAuctionCount();
                notificationRepository.save(notification);
            }
        }
    }

    // 판매자
    public void noticeToSeller(Auction auction) {
        Notification notification = Notification.builder()
                .user(auction.getUser())
                .checked(false)
                .isAuction(false)
                .build();

        notification.updateAuctionCount();

        notificationRepository.save(notification);
    }

    // 구매자
    public void noticeToBuyer(List<Bid> bidList) {

        Notification notification = Notification.builder()
                .user(bidList.get(0).getUser())
                .checked(false)
                .isAuction(false)
                .build();

        notification.updateAuctionCount();

        notificationRepository.save(notification);
    }
}