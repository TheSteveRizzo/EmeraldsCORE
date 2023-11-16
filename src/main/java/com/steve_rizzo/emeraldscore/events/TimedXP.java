package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimedXP extends BukkitRunnable {
    private static final Map<UUID, Long> lastRewardTime = new HashMap<>();

    public static void startTask() {
        new TimedXP().runTaskTimer(Main.core, 0L, 5 * 60 * 20L); // Runs every 5 minutes
    }

    public static void endTask() {
        TimedXP.endTask();
    }
    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerId = player.getUniqueId();
            lastRewardTime.putIfAbsent(playerId, currentTime); // Add player if not in the map

            long lastReward = lastRewardTime.get(playerId);
            long timeDifference = currentTime - lastReward;
            long twentyMinutes = 20 * 60 * 1000; // 20 minutes in milliseconds

            if (timeDifference >= twentyMinutes) {
                boolean isVIP = GamesAPI.isDonorOrHigher(player);
                int nonVIPXPLevels = 1, VIPXPLevels = 2;
                int xpToAdd = (isVIP && player.hasPermission("emeraldscore.vipxp")) ? VIPXPLevels : nonVIPXPLevels;

                int cashToAdd = (isVIP && player.hasPermission("emeraldscore.vipcash")) ? 500 : 250;
                EmeraldsCashAPI.addFunds(player, cashToAdd);
                player.giveExpLevels(xpToAdd);

                player.sendMessage(Main.prefix +
                        ChatColor.GRAY + "You received " + ChatColor.GREEN + xpToAdd + " XP Levels" + ChatColor.GRAY + "!\n");
                player.sendMessage(Main.prefix +
                        ChatColor.GRAY + "You received " + ChatColor.GREEN + cashToAdd + " Emeralds Cash" + ChatColor.GRAY + "!\n");

                lastRewardTime.put(playerId, currentTime); // Update last reward time
            }
        }
    }
}
