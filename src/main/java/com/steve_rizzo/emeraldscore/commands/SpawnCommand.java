package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SpawnCommand implements CommandExecutor {

    private FileConfiguration spawnyml;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            String prefix = Main.prefix;

            Player p = (Player) sender;

            if (args.length == 0) {

                p.performCommand("mvspawn");
                return true;

            } else {
                p.sendMessage(prefix + ChatColor.RED + "Usage: /spawn");
                return true;
            }
        }

        return true;
    }
}
