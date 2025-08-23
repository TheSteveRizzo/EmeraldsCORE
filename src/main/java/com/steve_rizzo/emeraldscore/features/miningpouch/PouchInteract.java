package com.steve_rizzo.emeraldscore.features.miningpouch;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PouchInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null || !item.getItemMeta().getDisplayName().equals((new Pouch(null)).getPouch().getItemMeta().getDisplayName()))
            return;
        e.setCancelled(true);
        if (item.getAmount() != 1)
            return;

        if (player.hasPermission("emeraldsmc.pouch")) {
            Pouch sack = new Pouch(item);
            if (item.getItemMeta().getLore().size() != ((new Pouch(null)).getLore()).length) {
                ItemMeta meta = item.getItemMeta();
                List<String> newLore = meta.getLore();
                newLore.add(ChatColor.DARK_GRAY + "Dirt: 0");
                newLore.add(ChatColor.DARK_GRAY + "Sand: 0");
                newLore.add(ChatColor.DARK_GRAY + "Gravel: 0");
                newLore.add(ChatColor.DARK_GRAY + "Tuff: 0");
                newLore.add(ChatColor.WHITE + "Quartz: 0");
                newLore.add(ChatColor.DARK_RED + "Netherrack: 0");
                newLore.add(ChatColor.LIGHT_PURPLE + "Ancient Debris: 0");
                meta.setLore(newLore);
                item.setItemMeta(meta);
            }
            player.openInventory(Pouch.getInventory(sack));
        }
    }
}
