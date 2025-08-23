package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String message = "";
        for (String part : args) {
            if (message != "") message += " ";
            message += part;
        }

        if (sender instanceof Player) {

            Player p = (Player) sender;
            String staffPrefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "STAFF" + ChatColor.GRAY + "] " + ServerJoinPlayer.getPlayerPrefixAndName(p) + ChatColor.GRAY + " >> ";

            if (p.hasPermission("emeraldsmc.staffchat")) {
                Bukkit.broadcast(staffPrefix + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', message), "emeraldsmc.staffchat");
                return true;

            } else {
                p.sendMessage(Main.prefix + ChatColor.RED + "No permission!");
                return true;
            }

        } else {

            String staffPrefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "STAFF" + ChatColor.GRAY + "] " + ChatColor.GOLD + "CONSOLE" + ChatColor.GRAY + " >> ";

            Bukkit.broadcast(staffPrefix + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', message), "emeraldsmc.staffchat");
            return true;
        }
    }
}

