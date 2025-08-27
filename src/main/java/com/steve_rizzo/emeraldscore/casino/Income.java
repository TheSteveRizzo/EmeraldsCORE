package com.steve_rizzo.emeraldscore.casino;

public enum Income {

    GUEST(1000),
    MEMBER(2500),
    DONOR1(5000),
    DONOR2(10000),
    DONOR3(15000),
    DONOR4(25000),
    ELITE(30000),
    PLATINUM(50000),
    BUILDER(50000),
    BUILDER2(50000),
    YOUTUBER(50000),
    HELPER(50000),
    MOD(50000),
    ADMIN(50000),
    OWNER(50000);

    private final int amount;

    Income(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
