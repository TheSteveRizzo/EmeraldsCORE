package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {

    String prefix = Main.prefix;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefix = Main.prefix;

        if (sender instanceof Player) {

            Player p = (Player) sender;

            Location hub = Bukkit.getWorld("Hub").getSpawnLocation();

            p.teleport(hub);
            p.sendMessage(prefix + ChatColor.GRAY + "You have been teleported to the " + ChatColor.AQUA + "HUB" + ChatColor.GRAY + " world");

            return true;

        }

        return false;
    }
}
