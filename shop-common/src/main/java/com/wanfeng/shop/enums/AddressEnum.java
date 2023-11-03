package com.wanfeng.shop.enums;

public enum AddressEnum {
    default_status(1),
    COMMON_STATUS(0);
    private int status;

    private AddressEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
