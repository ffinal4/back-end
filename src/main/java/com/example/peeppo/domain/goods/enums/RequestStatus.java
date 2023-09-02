package com.example.peeppo.domain.goods.enums;

public enum RequestStatus {

    REQUEST("trading request"),
    CANCEL("trading cancel"),
    DONE("trading complete"),
    TRADING("trading continue");

    private final String status;

    RequestStatus(String status) {
        this.status = status;
    }
}
