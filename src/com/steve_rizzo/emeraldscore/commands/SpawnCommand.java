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

public class SpawnCommand implements CommandExecutor {

    private final Main serverEssentials;
    private FileConfiguration spawnyml;

    public SpawnCommand(Main serverEssentials) {
        this.serverEssentials = serverEssentials;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (sender instanceof Player) {

            String prefix = Main.prefix;
            spawnyml = serverEssentials.spawnConfig;

            Player p = (Player) sender;

            String locSerialized = spawnyml.getString(p.getWorld().getName() + ".Location");
            String[] locString = locSerialized.split(",");

            if (args.length == 0) {

                Location spawnLoc = new Location(Bukkit.getWorld(locString[0]), Double.parseDouble(locString[1]), Double.parseDouble(locString[2]),
                        Double.parseDouble(locString[3]));

                p.teleport(spawnLoc);

                p.sendMessage(prefix + ChatColor.GREEN + "Teleported back to " + ChatColor.AQUA + spawnLoc.getWorld().getName()
                        + ChatColor.GREEN + "'s spawn!");

                return true;

            } else {

                p.sendMessage(prefix + ChatColor.GREEN + "Use " + ChatColor.AQUA + "/spawn");

                return true;
            }
        }

        return true;

    }
}