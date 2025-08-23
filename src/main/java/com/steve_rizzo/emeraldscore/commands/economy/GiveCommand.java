package com.steve_rizzo.emeraldscore.commands.economy;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission("emeraldsmc.cash.give")) {

                if (args.length == 2) {

                    String tarPlayerName = args[0];
                    int amount = 0;

                    try {
                        amount = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        // Not an integer
                        p.sendMessage(Main.prefix + ChatColor.RED + "Error: amount must be a positive number.");
                        return true;
                    }

                    if (amount > 0) {


                        Player target = Bukkit.getServer().getPlayer(tarPlayerName);

                        if (target != null) {

                            if (Bukkit.getServer().getPlayer(tarPlayerName).isOnline()) {


                                EmeraldsCashAPI.addFunds(target, amount);
                                p.sendMessage(Main.prefix + ChatColor.GRAY + "You just added $" + ChatColor.GREEN + amount + ChatColor.GRAY + " from " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + "'s balance.");
                                target.sendMessage(Main.prefix + ChatColor.GRAY + "The amount of $" + ChatColor.GREEN + amount + ChatColor.GRAY + " was given to your balance.");

                                return true;

                            } else {
                                p.sendMessage(Main.prefix + ChatColor.RED + "Error: player must be online.");
                                return true;
                            }

                        } else {
                            for (OfflinePlayer offlinePlayers : Bukkit.getServer().getOfflinePlayers()) {

                                if (tarPlayerName.equalsIgnoreCase(offlinePlayers.getName())) {

                                    EmeraldsCashAPI.addFundsToUUID(offlinePlayers.getUniqueId().toString(), amount);

                                    return true;

                                }
                            }

                            p.sendMessage(Main.prefix + ChatColor.RED + "Error: player has not played before.");
                            return true;
                        }
                    }

                } else {
                    p.sendMessage(Main.prefix + ChatColor.RED + "Usage: " + ChatColor.AQUA + "/givebal <player> <amount>");
                    return true;
                }

            } else {

                p.sendMessage(Main.prefix + ChatColor.RED + "No permission.");
                return true;

            }

        } else {
            if (args.length == 2) {

                String tarPlayerName = args[0];
                int amount = 0;

                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    // Not an integer
                    System.out.println(Main.prefix + ChatColor.RED + "Error: amount must be a positive number.");
                    return true;
                }

                if (amount > 0) {


                    Player target = Bukkit.getServer().getPlayer(tarPlayerName);

                    if (target != null) {

                        if (Bukkit.getServer().getPlayer(tarPlayerName).isOnline()) {

                            EmeraldsCashAPI.addFunds(target, amount);
                            System.out.println(Main.prefix + ChatColor.GRAY + "You just gave $" + ChatColor.GREEN + amount + ChatColor.GRAY + " to " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + "'s balance.");
                            target.sendMessage(Main.prefix + ChatColor.GRAY + "The amount of $" + ChatColor.GREEN + amount + ChatColor.GRAY + " was given to your balance.");

                            return true;

                        } else {
                            System.out.println(Main.prefix + ChatColor.RED + "Error: player must be online.");
                            return true;
                        }

                    } else {

                        for (OfflinePlayer offlinePlayers : Bukkit.getServer().getOfflinePlayers()) {

                            if (tarPlayerName.equalsIgnoreCase(offlinePlayers.getName())) {

                                EmeraldsCashAPI.addFundsToUUID(offlinePlayers.getUniqueId().toString(), amount);

                                return true;

                            }
                        }

                        System.out.println(Main.prefix + ChatColor.RED + "Error: player has not played before.");
                        return true;
                    }
                }

            } else {
                System.out.println(Main.prefix + ChatColor.RED + "Usage: " + ChatColor.AQUA + "/givebal <player> <amount>");
                return true;
            }
        }
        return true;
    }
}
