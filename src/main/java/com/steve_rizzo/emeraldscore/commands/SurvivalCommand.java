package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SurvivalCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            p.sendMessage(Main.prefix + ChatColor.GRAY + "Sending you to the... " +
                    ChatColor.AQUA + "Survival World" + ChatColor.GRAY + "!");

            if (Bukkit.getWorld("world") != null) {

                p.teleport(Bukkit.getWorld("world").getSpawnLocation());

                p.sendMessage(Main.prefix + ChatColor.AQUA + "Success!");

                return true;

            } else {

                p.sendMessage(Main.prefix + ChatColor.RED + "This request could not be completed. Please tell an administrator.");

                return true;

            }
        }

        return false;

    }
}