package com.steve_rizzo.emeraldscore.casino.roulette;

import org.bukkit.ChatColor;

public enum RouletteNumbers {
    GREEN_0(0, ChatColor.GREEN),
    RED_1(1, ChatColor.RED), BLACK_2(2, ChatColor.BLACK),
    RED_3(3, ChatColor.RED), BLACK_4(4, ChatColor.BLACK),
    RED_5(5, ChatColor.RED), BLACK_6(6, ChatColor.BLACK),
    RED_7(7, ChatColor.RED), BLACK_8(8, ChatColor.BLACK),
    RED_9(9, ChatColor.RED), BLACK_10(10, ChatColor.BLACK),
    BLACK_11(11, ChatColor.BLACK), RED_12(12, ChatColor.RED),
    BLACK_13(13, ChatColor.BLACK), RED_14(14, ChatColor.RED),
    BLACK_15(15, ChatColor.BLACK), RED_16(16, ChatColor.RED),
    BLACK_17(17, ChatColor.BLACK), RED_18(18, ChatColor.RED),
    RED_19(19, ChatColor.RED), BLACK_20(20, ChatColor.BLACK),
    RED_21(21, ChatColor.RED), BLACK_22(22, ChatColor.BLACK),
    RED_23(23, ChatColor.RED), BLACK_24(24, ChatColor.BLACK),
    RED_25(25, ChatColor.RED), BLACK_26(26, ChatColor.BLACK),
    RED_27(27, ChatColor.RED), BLACK_28(28, ChatColor.BLACK),
    BLACK_29(29, ChatColor.BLACK), RED_30(30, ChatColor.RED),
    BLACK_31(31, ChatColor.BLACK), RED_32(32, ChatColor.RED),
    BLACK_33(33, ChatColor.BLACK), RED_34(34, ChatColor.RED),
    BLACK_35(35, ChatColor.BLACK), RED_36(36, ChatColor.RED);

    private final int number;
    private final ChatColor color;

    RouletteNumbers(int number, ChatColor color) {
        this.number = number;
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public ChatColor getColor() {
        return color;
    }

    public static RouletteNumbers fromNumber(int number) {
        for (RouletteNumbers rn : values()) {
            if (rn.number == number) return rn;
        }
        return null;
    }
}
