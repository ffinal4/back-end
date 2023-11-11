package com.example.peeppo.domain.bid.enums;

import lombok.Getter;

@Getter
public enum BidStatus {

    BIDDING("bidding"),
    FAIL("bidding fail"),
    SUCCESS("bidding success"),
    TRADING("trading"),
    DONE("trading complete"),
    ;

    private final String status;

    BidStatus(String status) {
        this.status = status;
    }
}
