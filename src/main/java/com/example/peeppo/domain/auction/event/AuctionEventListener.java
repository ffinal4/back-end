package com.example.peeppo.domain.auction.event;

/*import com.example.peeppo.domain.auction.enums.AuctionStatus;
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

import java.util.Optional;

@Async
@Transactional
@Component
@RequiredArgsConstructor
public class AuctionEventListener {

    private final NotificationRepository notificationRepository;
    private final BidRepository bidRepository;

    @EventListener
    public void handleAuctionEvent(AuctionEvent auctionEvent) {

 //       NotificationStatus notificationStatus = auctionEvent.getNotificationStatus();
        AuctionStatus auctionStatus = auctionEvent.getAuctionStatus();
        Goods goods = auctionEvent.getGoods();

        noticeToSeller(goods, notificationStatus);

        if(notificationStatus.equals(NotificationStatus.SUCCESSFUL_BID)) {
            noticeToBuyer(goods, notificationStatus);
        }
    }

    // 판매자
    public void noticeToSeller(Goods goods, NotificationStatus notificationStatus) {

        saveNotification(goods.getUser(), goods, notificationStatus);
    }

    // 구매자
    public void noticeToBuyer(Goods goods, NotificationStatus notificationStatus) {

        Optional<Bid> bid = bidRepository.findByBidStatus(goods);
        if(bid.isPresent()) {
            saveNotification(bid.get().getUser(), goods, notificationStatus);
        }
    }

    public void saveNotification(User user, Goods goods, NotificationStatus notificationStatus) {

        Notification notification = Notification.builder()
                .user(user)
                .title(notificationStatus.getTitle())
                .message(goods.getTitle() + notificationStatus.getMessage())
                .details(notificationStatus.getDetails())
                .data(goods.getId())
                .checked(false)
                .build();

        notificationRepository.save(notification);
    }
}*/
