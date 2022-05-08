package com.steve_rizzo.emeraldscore.commands.economy;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (args.length == 2) {

                String tarPlayerName = args[0];
                int amount = 0;

                if (tarPlayerName.equalsIgnoreCase(p.getName())) {
                    p.sendMessage(Main.prefix + ChatColor.RED + "Error: cannot send yourself funds!");
                    return true;
                }

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

                            if ((EmeraldsCashAPI.getBalance(p)) > amount) {

                                EmeraldsCashAPI.deductFunds(p, amount);
                                EmeraldsCashAPI.addFunds(target, amount);
                                p.sendMessage(Main.prefix + ChatColor.GRAY + "You just sent " + ChatColor.AQUA + tarPlayerName + ChatColor.GRAY + " $" + ChatColor.AQUA + amount + ChatColor.GRAY + " Emeralds Cash.");
                                target.sendMessage(Main.prefix + ChatColor.GRAY + "You just received " + ChatColor.GRAY + "$" + ChatColor.AQUA + amount + ChatColor.GRAY + " Emeralds Cash from " + ChatColor.AQUA + p.getName() + ChatColor.GRAY + ".");

                                return true;

                            } else {
                                p.sendMessage(Main.prefix + ChatColor.RED + "Error: insufficient funds.");
                                return true;
                            }

                        } else {
                            p.sendMessage(Main.prefix + ChatColor.RED + "Error: player must be online.");
                            return true;
                        }

                    } else {
                        p.sendMessage(Main.prefix + ChatColor.RED + "Error: player must exist.");
                        return true;
                    }


                } else {
                    p.sendMessage(Main.prefix + ChatColor.RED + "Error: amount must be a positive number.");
                    return true;
                }

            } else {

                p.sendMessage(Main.prefix + ChatColor.RED + "Usage: " + ChatColor.AQUA + "/pay <player> <amount>");

            }

        }

        System.out.println(Main.prefix + "This command must be issued by a player.");
        return false;
    }
}