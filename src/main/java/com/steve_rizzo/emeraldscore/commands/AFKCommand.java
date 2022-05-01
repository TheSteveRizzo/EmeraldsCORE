package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AFKCommand implements CommandExecutor {

    List<String> listOfAFKPlayers;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (listOfAFKPlayers.contains(p.getName())) {
                Bukkit.broadcastMessage(Main.prefix + ChatColor.GOLD + p.getName() + ChatColor.GRAY
                        + " is " + ChatColor.GREEN + "no longer AFK" + ChatColor.GRAY + ".");
                listOfAFKPlayers.remove(p.getName());
                return true;
            } else {
                Bukkit.broadcastMessage(Main.prefix + ChatColor.GOLD + p.getName() + ChatColor.GRAY
                        + " is now " + ChatColor.RED + "AFK" + ChatColor.GRAY + ".");
                listOfAFKPlayers.add(p.getName());
                return true;
            }
        }

        return true;

    }
}
