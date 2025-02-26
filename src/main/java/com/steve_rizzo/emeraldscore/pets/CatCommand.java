package com.steve_rizzo.emeraldscore.pets;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CatCommand implements CommandExecutor {

    private final String prefix = Main.prefix;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 3600 * 1000; // 1 hour in milliseconds

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UUID playerId = p.getUniqueId();
            long currentTime = System.currentTimeMillis();

            // Check cooldown
            if (cooldowns.containsKey(playerId)) {
                long lastUsed = cooldowns.get(playerId);
                if ((currentTime - lastUsed) < COOLDOWN_TIME) {
                    long remainingTime = (COOLDOWN_TIME - (currentTime - lastUsed)) / 1000;
                    p.sendMessage(prefix + ChatColor.RED + "You must wait " + remainingTime + " seconds before using this again!");
                    return true;
                }
            }

            if (!p.hasPermission("emeraldsmc.pets.cat")) {
                p.sendMessage(prefix + ChatColor.RED + "No permission!");
                return true;
            }

            Cat cat = (Cat) p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.CAT);
            cat.setLeashHolder(p);
            p.sendMessage(ChatColor.GRAY + "Enjoy your new " + ChatColor.LIGHT_PURPLE + "CAT" + ChatColor.GRAY + "!");

            // Set cooldown
            cooldowns.put(playerId, currentTime);
            return true;
        }
        return false;
    }
}