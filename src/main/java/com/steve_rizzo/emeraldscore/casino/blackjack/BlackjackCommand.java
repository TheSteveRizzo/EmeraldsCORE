package com.steve_rizzo.emeraldscore.casino.blackjack;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.List;

public class BlackjackCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        UUID uuid = player.getUniqueId();

        switch (label.toLowerCase()) {
            case "blackjack" -> startGame(player, args);
            case "hit" -> hit(player);
            case "stand" -> stand(player);
        }

        return true;
    }

    private void startGame(Player player, String[] args) {
        UUID uuid = player.getUniqueId();

        if (args.length != 1) {
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.YELLOW + "Usage: /blackjack <bet>");
            return;
        }

        int bet;
        try { bet = Integer.parseInt(args[0]); }
        catch (NumberFormatException e) {
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.RED + "Invalid bet amount.");
            return;
        }

        if (bet <= 0) {
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.RED + "Bet must be greater than 0.");
            return;
        }

        if (bet > 5000000) {
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.RED + "Maximum bet is $5,000,000.");
            return;
        }

        EmeraldsCashAPI.getBalance(player).thenAccept(balance -> {
            if (balance < bet) {
                player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.RED + "You don't have enough money to place that bet.");
                return;
            }

            if (BlackjackManager.getGame(uuid) != null) {
                player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.RED + "You are already in a blackjack game! Use /hit or /stand.");
                return;
            }

            EmeraldsCashAPI.deductFunds(player, bet);
            BlackjackGame game = BlackjackManager.startGame(uuid, bet);

            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.GREEN + "You placed a bet of $" + bet);
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.AQUA + "Your cards: " + formatHand(game.getPlayerCards())
                    + " (Total: " + game.getPlayerTotal() + ")");

            // Dealer shows only first card + hidden card
            List<String> dealerCards = game.getDealerCards();
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.GRAY + "Dealer shows: [" + dealerCards.get(0) + "] [?]");

            if (game.getPlayerTotal() == 21) {
                player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.GOLD + "Blackjack! You win 2x your bet!");
                EmeraldsCashAPI.addFunds(player, bet * 2);
                BlackjackManager.endGame(uuid);
                return;
            }

            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.YELLOW + "Type /hit or /stand to continue.");
        });
    }

    private void hit(Player player) {
        UUID uuid = player.getUniqueId();
        BlackjackGame game = BlackjackManager.getGame(uuid);

        if (game == null) {
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.RED + "You are not in a blackjack game. Start one with /blackjack <bet>.");
            return;
        }

        game.hitPlayer();
        int total = game.getPlayerTotal();
        player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.AQUA + "Your cards: " + formatHand(game.getPlayerCards())
                + " (Total: " + total + ")");

        if (total > 21) {
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.RED + "BUST! You lose your bet of $" + game.getBet());
            BlackjackManager.endGame(uuid);
        } else if (total == 21) {
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.GREEN + "You hit 21! Type /stand to resolve the dealer's turn.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.YELLOW + "Type /hit to draw again or /stand to hold.");
        }
    }

    private void stand(Player player) {
        UUID uuid = player.getUniqueId();
        BlackjackGame game = BlackjackManager.getGame(uuid);

        if (game == null) {
            player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.RED + "You are not in a blackjack game. Start one with /blackjack <bet>.");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.GRAY + "Dealer reveals hidden card...");
        player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.GRAY + "Dealer's cards: " + formatHand(game.getDealerCards())
                + " (Total: " + game.getDealerTotal() + ")");

        int playerTotal = game.getPlayerTotal();

        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(Main.class), () -> {
            int dealerTotal = game.getDealerTotal();

            while (dealerTotal < 17) {
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                game.hitDealer();
                dealerTotal = game.getDealerTotal();

                int finalDealerTotal = dealerTotal;
                Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () ->
                        player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.GRAY + "Dealer's cards: " + formatHand(game.getDealerCards())
                                + " (Total: " + finalDealerTotal + ")")
                );
            }

            Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> {
                int betAmount = game.getBet();
                int dealerFinal = game.getDealerTotal();

                if (dealerFinal > 21 || playerTotal > dealerFinal) {
                    player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.GREEN + "You win $" + (betAmount * 2) + "!");
                    EmeraldsCashAPI.addFunds(player, betAmount * 2);
                } else if (playerTotal == dealerFinal) {
                    player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.YELLOW + "Push! Your bet is returned.");
                    EmeraldsCashAPI.addFunds(player, betAmount);
                } else {
                    player.sendMessage(ChatColor.YELLOW + "[♢] " + ChatColor.RED + "You lose your bet of $" + betAmount + "!");
                }

                BlackjackManager.endGame(uuid);
            });
        });
    }

    private String formatHand(List<String> hand) {
        return String.join(" ", hand.stream().map(card -> "[" + card + "]").toList());
    }
}
