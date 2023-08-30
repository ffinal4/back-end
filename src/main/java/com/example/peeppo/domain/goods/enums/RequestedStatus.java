package com.example.peeppo.domain.goods.enums;

public enum RequestedStatus {
    REQUESTED("trading requested"),
    CANCEL("trading cancel"),
    DONE("trading complete"),
    TRADING("trading continue");

    private final String status;

    RequestedStatus(String status) {
        this.status = status;
    }
}
