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

public class TimedXP {

    private static final Map<UUID, BukkitRunnable> playerTasks = new HashMap<>();

    public static void startTaskForPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        if (!playerTasks.containsKey(playerId)) {
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    boolean isVIP = GamesAPI.isDonorOrHigher(player);
                    int nonVIPXPLevels = 1, VIPXPLevels = 2;
                    int nonVIPCashAmt = 250, VIPCashAmt = 500;
                    int xpToAdd = (isVIP && player.hasPermission("emeraldscore.vipxp")) ? VIPXPLevels : nonVIPXPLevels;
                    int cashToAdd = (isVIP && player.hasPermission("emeraldscore.vipcash")) ? VIPCashAmt : nonVIPCashAmt;
                    player.giveExpLevels(xpToAdd);
                    EmeraldsCashAPI.addFunds(player, cashToAdd);
                    player.sendMessage(Main.prefix +
                            ChatColor.GRAY + "You received " + ChatColor.GREEN + xpToAdd + " XP" + ChatColor.GRAY + "!\n");
                    player.sendMessage(Main.prefix +
                            ChatColor.GRAY + "You received " + ChatColor.GREEN + cashToAdd + " Emeralds Cash" + ChatColor.GRAY + "!\n");}
            };
            task.runTaskTimer(Main.core, 0L, 24000L);
            playerTasks.put(playerId, task);
        }
    }

    public static void stopTaskForPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        BukkitRunnable task = playerTasks.get(playerId);
        if (task != null) {
            task.cancel();
            playerTasks.remove(playerId);
        }
    }

    public static void stopAllTasks() {
        for (BukkitRunnable task : playerTasks.values()) {
            task.cancel();
        }
        playerTasks.clear();
    }
}