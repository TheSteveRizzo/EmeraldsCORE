package com.steve_rizzo.emeraldscore.casino.roulette;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RouletteCommand implements CommandExecutor {

    private final String prefix = Main.prefix;
    private final RouletteGame rouletteGame = Main.core.getRouletteGame();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(prefix + ChatColor.RED + "Usage: /roulette <amount> <number/color>");
            return true;
        }

        // Parse bet amount
        int amount;
        try {
            amount = Integer.parseInt(args[0]);
            if (amount < 1 || amount > 5000000) {
                player.sendMessage(prefix + ChatColor.RED + "Bet must be between 1 and 5,000,000.");
                return true;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(prefix + ChatColor.RED + "Invalid bet amount.");
            return true;
        }

        // Check player's balance asynchronously
        EmeraldsCashAPI.getBalance(player).thenAccept(balance -> {
            if (balance < amount) {
                player.sendMessage(prefix + ChatColor.RED + "You don't have enough coins to place this bet.");
                return;
            }

            // Prevent bets if game is already running
            if (rouletteGame.isGameActive()) {
                int countdown = rouletteGame.getCountdown();
                if (countdown < 5) {
                    player.sendMessage(prefix + ChatColor.RED + "Too late to place new bets, the wheel is about to spin!");
                    return;
                }
            }

            // Determine if bet is a number or color
            RouletteBet bet;
            try {
                int number = Integer.parseInt(args[1]);
                if (number >= 0 && number <= 36) {
                    bet = new RouletteBet(amount, number); // Number bet
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "Invalid number. Choose 0-36 or a color.");
                    return;
                }
            } catch (NumberFormatException e) {
                String color = args[1].toLowerCase();
                if (color.equals("red") || color.equals("black")) {
                    bet = new RouletteBet(amount, color); // Color bet
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "Invalid color. Choose red or black.");
                    return;
                }
            }

            // Deduct funds and place bet
            EmeraldsCashAPI.deductFunds(player, amount);
            rouletteGame.placeBet(player, bet);

            // Build display text for player/broadcast messages
            String betDisplay;
            if (bet.isNumber()) {
                ChatColor numberColor = RouletteNumbers.fromNumber(bet.getValue()).getColor();
                betDisplay = "Number " + numberColor + bet.getValue();
            } else {
                ChatColor color = bet.getColor().equalsIgnoreCase("red") ? ChatColor.RED : ChatColor.BLACK;
                betDisplay = color + bet.getColor().toUpperCase();
            }

            // Player message
            player.sendMessage(ChatColor.GREEN + "Bet placed: " + ChatColor.GRAY + betDisplay +
                    ChatColor.GRAY + " for " + ChatColor.GREEN + "$" + bet.getAmount() + ChatColor.GRAY + "!");

            // Broadcast message
            String broadcast = ServerJoinPlayer.getPlayerPrefixAndName(player) + ChatColor.GRAY + " has put a " +
                    ChatColor.GREEN + "$" + bet.getAmount() + ChatColor.GRAY + " bet on " +
                    betDisplay + ChatColor.GRAY + "! Game starts in " + ChatColor.AQUA + rouletteGame.getCountdown() + ChatColor.GRAY + "!";
            player.getServer().broadcastMessage(broadcast);

            // Start the roulette game
            rouletteGame.startGame();
        });

        return true;
    }
}
