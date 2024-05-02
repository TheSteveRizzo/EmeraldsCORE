package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private final Main core;
    private FileConfiguration spawnyml;

    public SetSpawnCommand(Main core) {
        this.core = core;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefix = Main.prefix;
        spawnyml = core.spawnConfig;

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("emeraldsmc.setspawn")) {
                if (args.length == 0) {
                    Location loc = p.getLocation();
                    String worldName = p.getWorld().getName();
                    String locSerialized = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getPitch() + "," + p.getLocation().getYaw();

                    // Save spawn data to the configuration file
                    spawnyml.set(worldName + ".Location", locSerialized); // Store location

                    // Inform the player about the action
                    if (spawnyml.contains(worldName)) {
                        p.sendMessage(prefix + ChatColor.GREEN + "Spawnpoint for " + ChatColor.AQUA + worldName + ChatColor.GREEN + " successfully updated!");
                    } else {
                        p.sendMessage(prefix + ChatColor.GREEN + "Spawnpoint for " + ChatColor.AQUA + worldName + ChatColor.GREEN + " successfully set!");
                    }

                    core.saveConfig(); // Save changes to the config file
                    return true;
                } else {
                    p.sendMessage(prefix + ChatColor.RED + "Incorrect usage. Use " + ChatColor.AQUA + "/setspawn");
                    return true;
                }
            } else {
                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                return true;
            }
        } else {
            System.out.println("You can't do this.");
            return true;
        }
    }
}
