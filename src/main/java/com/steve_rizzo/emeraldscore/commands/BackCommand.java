package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.UUID;

public class BackCommand implements Listener, CommandExecutor {

    public static HashMap<String, Location> deathLocations = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefix = Main.prefix;

        if (sender instanceof Player) {

            Player p = (Player) sender;

            String playerUUID = p.getUniqueId().toString();

            // Check if the player has the permission and a saved death location
            if (p.hasPermission("back.use") && deathLocations.containsKey(playerUUID)) {
                Location deathLocation = deathLocations.get(playerUUID);

                // Teleport the player to their death location
                p.teleport(deathLocation);
                p.sendMessage(prefix + ChatColor.GREEN + "You have been teleported back to your death location.");

                // Optionally, clear the saved location after teleporting
                deathLocations.remove(playerUUID);
            } else {
                p.sendMessage(prefix + ChatColor.RED + "You do not have permission or no death location saved.");
            }
            return true;
        }

        return false;
    }
}
