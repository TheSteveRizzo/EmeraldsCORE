package com.steve_rizzo.emeraldscore.casino.roulette;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class RouletteGame {

    String gamePrefix = ChatColor.YELLOW + "" + ChatColor.BOLD + "[♢] " + ChatColor.RESET;

    private final Map<Player, List<RouletteBet>> playerBets = new HashMap<>();
    private boolean isRunning = false;
    private final int maxBets = 5;

    // Countdown in seconds before the roulette wheel spins
    private int countdown = 60;

    public void placeBet(Player player, RouletteBet bet) {
        playerBets.putIfAbsent(player, new ArrayList<>());
        if (playerBets.get(player).size() >= maxBets) {
            player.sendMessage(Main.prefix + ChatColor.RED + "You can only place up to " + maxBets + " bets per round.");
            return;
        }
        playerBets.get(player).add(bet);
        player.sendMessage(Main.prefix + ChatColor.AQUA + "Bet placed: " + bet);
    }

    public void startGame() {
        if (isRunning) return;
        isRunning = true;
        countdown = 60; // Reset countdown for new game

        Bukkit.broadcastMessage(gamePrefix + ChatColor.AQUA + "A roulette game has started! Place your bets! Game starts in " + countdown + " seconds.");

        new BukkitRunnable() {

            @Override
            public void run() {
                if (countdown <= 0) {
                    spinWheel();
                    cancel();
                    return;
                }
                if (countdown % 10 == 0 || countdown <= 5) { // Broadcast every 10s + last 5s countdown
                    Bukkit.broadcastMessage(gamePrefix + ChatColor.GRAY + "Place your bets! " + ChatColor.AQUA + countdown + ChatColor.GRAY + " seconds remaining.");
                }
                countdown--;
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L); // 20 ticks = 1 second
    }

    private void spinWheel() {
        Random random = new Random();
        int spin = random.nextInt(37); // 0-36
        RouletteNumbers landed = RouletteNumbers.fromNumber(spin);

        // Determine the color name for broadcast
        ChatColor colorFormat = landed.getColor();
        String colorName;
        if (spin == 0) {
            colorName = "GREEN";
        } else if (colorFormat == ChatColor.RED) {
            colorName = "RED";
        } else {
            colorName = "BLACK";
        }

        // Broadcast spin result with separate colors
        Bukkit.broadcastMessage(gamePrefix + ChatColor.AQUA + "Roulette spun!");
        Bukkit.broadcastMessage(
                gamePrefix
                        + ChatColor.AQUA + "Number: "
                        + colorFormat + spin
                        + ChatColor.GRAY + " | "
                        + ChatColor.AQUA + "Color: "
                        + colorFormat + colorName
        );

        // Pay out bets
        payOut(landed);

        // Clear bets and reset game state
        playerBets.clear();
        isRunning = false;
    }

    private void payOut(RouletteNumbers landed) {
        for (Player player : playerBets.keySet()) {
            int totalWin = 0;
            for (RouletteBet bet : playerBets.get(player)) {
                if (bet.isNumber() && bet.getValue() == landed.getNumber()) {
                    totalWin += bet.getAmount() * 35;
                } else if (!bet.isNumber() && bet.getColor().equalsIgnoreCase(landed.getColor().name())) {
                    totalWin += bet.getAmount() * 2;
                }
            }

            if (totalWin > 0) {
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You won " + ChatColor.AQUA + "$" + totalWin + ChatColor.GREEN + "!");
                EmeraldsCashAPI.addFunds(player, totalWin); // Add winnings
            } else {
                int betTotal = playerBets.get(player).stream().mapToInt(RouletteBet::getAmount).sum();
                player.sendMessage(Main.prefix + ChatColor.RED + "You lost " + ChatColor.AQUA + "$" + betTotal + ChatColor.RED + "!");
            }
        }
    }

    public boolean isGameActive() {
        return isRunning;
    }

    /**
     * Returns the current countdown in seconds.
     */
    public int getCountdown() {
        return countdown;
    }
}
