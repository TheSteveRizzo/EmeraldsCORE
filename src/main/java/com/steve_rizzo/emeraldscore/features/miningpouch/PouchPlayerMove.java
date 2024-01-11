package com.steve_rizzo.emeraldscore.features.miningpouch;


import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public class PouchPlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (Pouch.hasPouchOpened(player))
            return;
        PlayerInventory playerInventory = player.getInventory();
        int targetSlot = Pouch.targetPouchSlot((Inventory)playerInventory);
        if (targetSlot == -1)
            return;
        if (player.hasPermission("emeraldsmc.pouch")) {
            for (Entity entity : player.getNearbyEntities(1.0D, 1.0D, 1.0D)) {
                if (entity instanceof Item) {
                    Item item = (Item) entity;
                    if (Pouch.isPouchItem(item.getItemStack().getType()) && item.getPickupDelay() < 0)
                        Pouch.addToPouch(new Pouch(playerInventory.getItem(targetSlot)), item, player);
                }
            }
        }
    }
}
