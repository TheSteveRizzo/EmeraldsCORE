package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String message = "";
        for (String part : args) {
            if (message != "") message += " ";
            message += part;
        }

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission("emeraldsmc.bc")) {
                Bukkit.broadcastMessage(Main.prefix + ChatColor.LIGHT_PURPLE + ChatColor.translateAlternateColorCodes('&', message));
                return true;
            } else {
                p.sendMessage(Main.prefix + ChatColor.RED + "No permission!");
                return true;
            }
        } else {
            Bukkit.broadcastMessage(Main.prefix + ChatColor.LIGHT_PURPLE + ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
    }
}

