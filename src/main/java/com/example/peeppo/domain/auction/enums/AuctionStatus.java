package com.example.peeppo.domain.auction.enums;

import lombok.Getter;

@Getter
public enum AuctionStatus {
        AUCTION("is auction"),
        END("end auction"),
        TRADING("trading"),
        DONE("trading complete"),
        CANCEL("trading cancel");
        private final String status;

        AuctionStatus(String status) {
            this.status = status;
        }
}
