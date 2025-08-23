package com.steve_rizzo.emeraldscore.features.miningpouch;

import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class PouchPickupItem implements Listener {
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent e) {
        LivingEntity livingEntity = e.getEntity();
        if (!(livingEntity instanceof Player player))
            return;
        Item item = e.getItem();
        if (Pouch.targetPouchSlot((Inventory) player.getInventory()) == -1 || !Pouch.isPouchItem(item.getItemStack().getType()) || Pouch.hasPouchOpened(player))
            return;
        e.setCancelled(true);
        if (player.hasPermission("emeraldsmc.pouch")) {
            PlayerInventory playerInventory = player.getInventory();
            Pouch.addToPouch(new Pouch(playerInventory.getItem(Pouch.targetPouchSlot((Inventory) playerInventory))), item, player);
        }
    }
}
