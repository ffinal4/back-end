package com.example.peeppo.domain.bid.enums;

import lombok.Getter;

@Getter
public enum BidStatus {

    BIDDING("bidding"),
    DONE("trading complete"),
    FAIL("bidding fail"),
    SUCCESS("bidding success"),
    TRADING("trading");

    private final String status;

    BidStatus(String status) {
        this.status = status;
    }
}
