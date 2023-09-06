package com.example.peeppo.domain.notification.entity;

import com.example.peeppo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    private Boolean checked = false;

    private Boolean isRequest = false;

    private Boolean isAuction = false;

    private Boolean isMessage = false;

    private Long messageCount = 0L;

    private Long auctionCount = 0L;

    private Long requestCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id")
    private User user;

    public void setIsAuction(Boolean isAuction) {
        this.isAuction = isAuction;
    }

    public void setIsRequest(Boolean isRequest) {
        this.isRequest = isRequest;
    }

    public void setIsMessage(Boolean isMessage) {
        this.isMessage = isMessage;
    }

    public void Checked(Boolean checked) {
        this.checked = checked;
    }

    public void messageRead(Long read) {
        this.messageCount = read;
    }

    public void auctionRead(Long read) {
        this.auctionCount = read;
    }

    public void requestRead(Long read) {
        this.requestCount = read;
    }

    public void updateAuctionCount() {
        this.auctionCount += 1;
    }

    public void updateRequestCount() {
        this.requestCount += 1;
    }

    public void updateMessageCount() {
        this.messageCount += 1;
    }

    public Notification(User user) {
        this.user = user;
    }
}
