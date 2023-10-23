package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PVPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.isOp()) {

                p.sendMessage(Main.prefix + ChatColor.GRAY + "Sending you to the... " +
                        ChatColor.AQUA + "PVP World" + ChatColor.GRAY + "!");

                if (Bukkit.getWorld("pvpworld") != null) {

                    p.teleport(Bukkit.getWorld("pvpworld").getSpawnLocation());

                    p.sendMessage(Main.prefix + ChatColor.AQUA + "Success!");

                    return true;

                } else {

                    p.sendMessage(Main.prefix + ChatColor.RED + "This request could not be completed. Please tell an administrator.");

                    return true;

                }
            }
        }

        return false;

    }
}