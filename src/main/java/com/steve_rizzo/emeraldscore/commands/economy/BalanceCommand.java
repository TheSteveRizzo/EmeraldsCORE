package com.steve_rizzo.emeraldscore.commands.economy;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (args.length == 0) {

                int balance = EmeraldsCashAPI.getBalance(p);
                p.sendMessage(Main.prefix + ChatColor.GRAY + "Your Emeralds Cash balance is: $" + ChatColor.GREEN + balance + ChatColor.GRAY + ".");
                return true;

            } else if (args.length == 1) {

                if (Bukkit.getServer().getPlayer(args[0]) != null) {

                    Player target = Bukkit.getServer().getPlayer(args[0]);

                    if (EmeraldsCashAPI.doesPlayerAccountExist(target)) {

                        int tarBal = EmeraldsCashAPI.getBalance(target);
                        assert target != null;
                        p.sendMessage(Main.prefix + ChatColor.GRAY + "The balance of " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + " is: $" + ChatColor.GREEN + tarBal + ChatColor.GRAY + ".");
                        return true;

                    } else {
                        p.sendMessage(Main.prefix + ChatColor.RED + "Error: " + ChatColor.GRAY + "account does not exist.");
                        return true;
                    }
                } else {
                    p.sendMessage(Main.prefix + ChatColor.RED + "Error: " + ChatColor.GRAY + "player does not exist.");
                    return true;
                }

            } else {
                p.sendMessage(Main.prefix + ChatColor.RED + "Usage: " + ChatColor.AQUA + "/bal | /bal [player]");
                return true;
            }
        }

        return true;

    }
}
