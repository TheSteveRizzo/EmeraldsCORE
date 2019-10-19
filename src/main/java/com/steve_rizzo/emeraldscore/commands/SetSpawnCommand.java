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

    private final Main serverEssentials;
    private FileConfiguration spawnyml;

    public SetSpawnCommand(Main serverEssentials) {
        this.serverEssentials = serverEssentials;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefix = Main.prefix;
        spawnyml = serverEssentials.spawnConfig;

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission("emeraldsmc.setspawn")) {
                if (args.length == 0) {

                    Location loc = p.getLocation();
                    String locSerialized = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getPitch() + "," + loc.getYaw();

                    spawnyml.set(p.getWorld().getName() + ".Location", locSerialized);

                    p.sendMessage(prefix + ChatColor.GREEN + "Spawnpoint for "
                            + ChatColor.AQUA + p.getWorld().getName() + ChatColor.GREEN + " successfully set!");
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
