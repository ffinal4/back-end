package com.example.peeppo.domain.bid.enums;

import lombok.Getter;

@Getter
public enum BidStatus {


    FAIL("bidding fail"),
    SUCCESS("bidding success");

    private final String status;

    BidStatus(String status) {
        this.status = status;
    }
}
