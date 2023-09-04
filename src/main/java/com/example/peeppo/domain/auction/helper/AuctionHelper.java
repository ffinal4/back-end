package com.example.peeppo.domain.auction.helper;

import com.example.peeppo.domain.auction.dto.TimeRemaining;
import com.example.peeppo.domain.auction.entity.Auction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AuctionHelper {
    public TimeRemaining countDownTime(Auction auction) {
        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(now, auction.getAuctionEndTime());
        long hours = ChronoUnit.HOURS.between(now, auction.getAuctionEndTime());
        long minutes = ChronoUnit.MINUTES.between(now, auction.getAuctionEndTime());
        long seconds = ChronoUnit.SECONDS.between(now, auction.getAuctionEndTime());

        return new TimeRemaining(days, hours % 24, minutes % 60, seconds % 60);
    }
}
