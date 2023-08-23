package com.example.peeppo.domain.notification.entity;

import com.example.peeppo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @ColumnDefault("false")
    private Boolean checked;

    @ColumnDefault("false")
    private Boolean isRequest;

    @ColumnDefault("false")
    private Boolean isAuction;

    private Long newCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id")
    private User user;

    public void setIsAuction(Boolean isAuction) {
        this.isAuction = isAuction;
    }

    public void setIsRequest(Boolean isRequest) {
        this.isRequest = isRequest;
    }

    public void read() {
        this.checked = true;
    }

    public void unread() {
        this.checked = false;
    }

    public void updateCount() {
        this.newCount += 1;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
