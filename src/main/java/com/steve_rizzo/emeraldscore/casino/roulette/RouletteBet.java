package com.steve_rizzo.emeraldscore.casino.roulette;

public class RouletteBet {
    private final int amount;
    private final boolean isNumber;
    private final int value;
    private final String color;

    public RouletteBet(int amount, int number) {
        this.amount = amount;
        this.isNumber = true;
        this.value = number;
        this.color = null;
    }

    public RouletteBet(int amount, String color) {
        this.amount = amount;
        this.isNumber = false;
        this.value = -1;
        this.color = color.toUpperCase();
    }

    public int getAmount() { return amount; }
    public boolean isNumber() { return isNumber; }
    public int getValue() { return value; }
    public String getColor() { return color; }

    @Override
    public String toString() {
        if (isNumber) return "Number " + value + " for " + amount;
        return "Color " + color + " for " + amount;
    }
}
