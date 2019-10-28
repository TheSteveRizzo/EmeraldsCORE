package com.steve_rizzo.emeraldscore.utils.glowutils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class GlowUtil {

    public static HashMap<Player, String> glowingPlayers = new HashMap();

    public static void activateGlow(Player player, String color) {

        color = color.toLowerCase();

        glowingPlayers.put(player, color);
        toggleColor(player, color, true);
    }

    public static void disableGlow(Player player) {
        String name = (String) glowingPlayers.get(player);

        if (glowingPlayers.containsKey(player)) {
            toggleColor(player, name, false);
            glowingPlayers.remove(player);

        }
    }

    public static void toggleColor(Player player, String color, boolean status) {
        if (!status) {
            if (player.isGlowing()) {
                glowingPlayers.remove(player);
            }

            TeamUtil.setGlowing(player, status);
            TeamUtil.setTeam(player, null);
        } else if (color != "none") {
            TeamUtil.setTeam(player, getColorFromString(color.toLowerCase()));
            TeamUtil.setGlowing(player, status);
        }
    }


    public static ChatColor getColorFromString(String color) {
        if (color.equals("red"))
            return ChatColor.RED;
        if (color.equals("darkred"))
            return ChatColor.DARK_RED;
        if (color.equals("gold"))
            return ChatColor.GOLD;
        if (color.equals("yellow"))
            return ChatColor.YELLOW;
        if (color.equals("green"))
            return ChatColor.GREEN;
        if (color.equals("darkgreen"))
            return ChatColor.DARK_GREEN;
        if (color.equals("aqua"))
            return ChatColor.AQUA;
        if (color.equals("darkaqua"))
            return ChatColor.DARK_AQUA;
        if (color.equals("blue"))
            return ChatColor.BLUE;
        if (color.equals("darkblue"))
            return ChatColor.DARK_BLUE;
        if (color.equals("purple"))
            return ChatColor.DARK_PURPLE;
        if (color.equals("pink"))
            return ChatColor.LIGHT_PURPLE;
        if (color.equals("white"))
            return ChatColor.WHITE;
        if (color.equals("gray"))
            return ChatColor.GRAY;
        if (color.equals("darkgray"))
            return ChatColor.DARK_GRAY;
        if (color.equals("black")) {
            return ChatColor.BLACK;
        }
        return null;
    }

}
