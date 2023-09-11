package com.example.peeppo.domain.notification.entity;

import com.example.peeppo.domain.notification.enums.NotificationStatus;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    private String content;

    @Column(nullable = false)
    private Boolean isRead;

//    private Boolean checked = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id")
    private User user;

//    public void setIsAuction(Boolean isAuction) {
//        this.isAuction = isAuction;
//    }
//
//    public void setIsRequest(Boolean isRequest) {
//        this.isRequest = isRequest;
//    }
//
//    public void setIsMessage(Boolean isMessage) {
//        this.isMessage = isMessage;
//    }
//
//    public void Checked(Boolean checked) {
//        this.checked = checked;
//    }
//
//    public void messageRead(Long read) {
//        this.messageCount = read;
//    }
//
//    public void auctionRead(Long read) {
//        this.auctionCount = read;
//    }
//
//    public void requestRead(Long read) {
//        this.requestCount = read;
//    }
//
//    public void updateAuctionCount() {
//        this.auctionCount += 1;
//    }
//
//    public void updateRequestCount() {
//        this.requestCount += 1;
//    }
//
//    public void updateMessageCount() {
//        this.messageCount += 1;
//    }

    public Notification(User user) {
        this.user = user;
    }

    public void read() {
        this.isRead = true;
    }

    @Builder
    public Notification(User receiver, NotificationStatus notificationStatus, String content, Boolean isRead){
        this.user = receiver;
        this.notificationStatus = notificationStatus;
        this.content = content;
        this.isRead = false;
    }
}
