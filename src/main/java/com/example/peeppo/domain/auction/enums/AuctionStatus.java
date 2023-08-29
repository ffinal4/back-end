package com.example.peeppo.domain.auction.enums;

import lombok.Getter;

@Getter
public enum AuctionStatus {
        REQUEST("trading request"),
        CANCEL("trading cancel"),
        DONE("trading complete"),
        TRADING("trading continue");

        private final String status;

        AuctionStatus(String status) {
            this.status = status;
        }
}
