package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CasinoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;
            Location casinoLoc = new Location(Bukkit.getWorld("world"), -197, 66, 406);
            p.sendMessage(Main.prefix + ChatColor.GRAY + "Sending you to the " + ChatColor.LIGHT_PURPLE + "CASINO" + ChatColor.GRAY + "...");
            p.teleport(casinoLoc);

            return true;
        }

        return true;

    }
}
