package com.steve_rizzo.emeraldscore.commands;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class FloatItem implements CommandExecutor {

    public Map<String, Hologram> activeUserHolograms = new HashMap<>();
    String prefix = Main.prefix;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {

            if (sender instanceof Player) {

                Player p = (Player) sender;

                if (GamesAPI.isDonorOrHigher(p)) {

                    ItemStack itemInHand = p.getInventory().getItemInMainHand();

                    if (activeUserHolograms.containsKey(p.getUniqueId().toString())) {

                        p.sendMessage(prefix + ChatColor.RED + "You must wait for your current floating item to expire before using this!");

                        return true;
                    }

                    if (itemInHand == null || itemInHand.getType() == Material.AIR) {

                        p.sendMessage(prefix + ChatColor.RED + "You must have an item in your hand!");

                        return true;

                    } else {

                        Plugin plugin = Main.core;
                        Location where = p.getLocation().add(0, 2, 0);
                        Hologram hologram = HologramsAPI.createHologram(plugin, where);
                        ItemLine itemLine = hologram.insertItemLine(0, itemInHand);
                        TextLine textLine = hologram.insertTextLine(0, ChatColor.GRAY + p.getName() + "'s floating item");
                        activeUserHolograms.put(p.getUniqueId().toString(), hologram);

                        p.sendMessage(prefix + ChatColor.GRAY + "You just sent a " + ChatColor.LIGHT_PURPLE + "floating item " + ChatColor.GRAY + "into the air!");

                        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
                            @Override
                            public void run() {
                                p.sendMessage(prefix + ChatColor.GRAY + "Your " + ChatColor.LIGHT_PURPLE + "floating item " + ChatColor.GRAY + "has disappeared!");
                                activeUserHolograms.remove(p.getUniqueId().toString());
                                hologram.delete();
                            }
                        }, 20 * 30);

                        return true;
                    }

                } else {

                    p.sendMessage(prefix + ChatColor.RED + "You must be " + ChatColor.AQUA + "a donor" + ChatColor.RED + " to use this!");
                    p.sendMessage(prefix + ChatColor.GREEN + "Purchase a rank on " + ChatColor.AQUA + "https://store.emeraldsmc.com" + ChatColor.RED
                            + "!");

                    return true;
                }
            }
        }

        return false;

    }
}
