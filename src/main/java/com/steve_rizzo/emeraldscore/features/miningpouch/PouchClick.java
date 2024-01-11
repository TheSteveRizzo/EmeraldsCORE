package com.steve_rizzo.emeraldscore.features.miningpouch;


import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PouchClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryView inventory = e.getView();
        if (!inventory.getTitle().equals((new Pouch(null)).getTitle()))
            return;
        e.setCancelled(true);
        HumanEntity humanEntity = e.getWhoClicked();
        if (!(humanEntity instanceof Player))
            return;
        Player player = (Player) humanEntity;
        if (e.getClickedInventory() == player.getInventory())
            return;

        if (player.hasPermission("emeraldsmc.pouch")) {

            ItemStack item = e.getCurrentItem();
            if (item == null)
                return;
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(ChatColor.RED + "Not enough inventory space.");
                return;
            }
            Pouch pouch = new Pouch(player.getItemInHand());
            int slot = e.getSlot();
            if (pouch.getQuantity(item.getType()) < 64) {
                player.getInventory().addItem(new ItemStack[]{new ItemStack(item.getType(), pouch.getQuantity(item.getType()))});
                pouch.setQuantity(item.getType(), 0);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName((new Pouch(null)).getLore()[slot]);
                item.setItemMeta(meta);
            } else {
                player.getInventory().addItem(new ItemStack[]{new ItemStack(item.getType(), 64)});
                pouch.setQuantity(item.getType(), pouch.getQuantity(item.getType()) - 64);
                ItemMeta meta = item.getItemMeta();
                int position = 0;
                for (; !Character.isDigit(ChatColor.stripColor(pouch.getPouch().getItemMeta().getLore().get(slot)).charAt(position)); position++)
                    ;
                meta.setDisplayName(String.valueOf((new Pouch(null)).getLore()[slot].substring(0, (new Pouch(null)).getLore()[slot].length() - 1)) + Integer.parseInt(((String) pouch.getPouch().getItemMeta().getLore().get(slot)).substring(position + 2)));
                item.setItemMeta(meta);
            }
            player.setItemInHand(pouch.getPouch());
        }
    }
}