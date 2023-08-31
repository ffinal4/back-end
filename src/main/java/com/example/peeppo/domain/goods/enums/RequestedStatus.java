package com.example.peeppo.domain.goods.enums;

public enum RequestedStatus {
    REQUESTED("trading requested"),// 교환요청
    CANCEL("trading cancel"), // 교환취소
    DONE("trading complete"), // 교환완료
    TRADING("trading continue"); // 교환진행중

    private final String status;

    RequestedStatus(String status) {
        this.status = status;
    }
}
