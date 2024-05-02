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
            spawnyml = Main.core.spawnConfig;

            Player p = (Player) sender;

            if (args.length == 0) {
                String worldName = p.getWorld().getName();
                if (spawnyml.contains(worldName + ".Location")) {
                    String locSerialized = spawnyml.getString(worldName + ".Location");
                    String[] locString = locSerialized.split(",");
                    double x = Double.parseDouble(locString[1]);
                    double y = Double.parseDouble(locString[2]);
                    double z = Double.parseDouble(locString[3]);
                    float yaw = Float.parseFloat(locString[4]);
                    float pitch = Float.parseFloat(locString[5]);

                    // Get the world instance
                    Location spawnLoc = new Location(Bukkit.getServer().getWorld(worldName), x, y, z, yaw, pitch);

                    // Teleport the player and set their direction
                    p.teleport(spawnLoc, PlayerTeleportEvent.TeleportCause.COMMAND);

                    // You need to update the player's location after teleporting
                    p.getLocation().setYaw(yaw);
                    p.getLocation().setPitch(pitch);

                    p.sendMessage(prefix + ChatColor.GREEN + "Teleported back to " + ChatColor.AQUA + spawnLoc.getWorld().getName()
                            + ChatColor.GREEN + "'s spawn!");

                    return true;
                } else {
                    p.sendMessage(prefix + ChatColor.RED + "No spawn-point set for this world!");
                    return true;
                }
            } else {
                p.sendMessage(prefix + ChatColor.RED + "Usage: /spawn");
                return true;
            }
        }

        return true;
    }
}
