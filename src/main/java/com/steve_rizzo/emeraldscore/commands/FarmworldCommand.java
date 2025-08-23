package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FarmworldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            p.sendMessage(Main.prefix + ChatColor.GRAY + "Sending you to... " +
                    ChatColor.AQUA + "FarmWorld" + ChatColor.GRAY + "!");

            if (Bukkit.getWorld("farmworld") != null) {

                World farmWorld = Bukkit.getWorld("farmworld");
                if (farmWorld != null) {
                    Location spawnLoc = farmWorld.getSpawnLocation();
                    float yaw = spawnLoc.getYaw();
                    float pitch = spawnLoc.getPitch();

                    Location locWithRotation = new Location(
                            farmWorld,
                            spawnLoc.getX(),
                            spawnLoc.getY(),
                            spawnLoc.getZ(),
                            yaw,
                            pitch
                    );

                    p.teleport(locWithRotation);
                    p.sendMessage(Main.prefix + ChatColor.AQUA + "Success!");
                } else {
                    p.sendMessage(Main.prefix + ChatColor.RED + "FarmWorld not found!");
                }

                return true;

            } else {

                p.sendMessage(Main.prefix + ChatColor.RED + "This request could not be completed. Please tell an administrator.");

                return true;

            }
        }

        return false;

    }
}
