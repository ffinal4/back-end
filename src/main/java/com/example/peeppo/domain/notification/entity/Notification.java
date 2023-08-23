package com.example.peeppo.domain.notification.entity;

import com.example.peeppo.domain.auction.entity.Auction;
import com.example.peeppo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    private Boolean checked;

    private Boolean isRequest;

    private Boolean isAuction;

    private Boolean isMessage;

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

    public void setUser(User user) {
        this.user = user;
    }
}
