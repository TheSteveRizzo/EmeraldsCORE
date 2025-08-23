package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class WardenDeathEvent implements Listener {

    @EventHandler
    public void onWardenDeath(EntityDeathEvent event) {
        if (event.getEntityType().equals(EntityType.WARDEN)) {
            Warden warden = (Warden) event.getEntity();
            if (event.getEntity().getKiller() != null) {
                Player player = event.getEntity().getKiller();
                Location dropLoc = warden.getLocation().add(0, 1, 0);
                ItemStack dropItem = new ItemStack(Material.ELYTRA);
                Objects.requireNonNull(Bukkit.getWorld(Objects.requireNonNull(dropLoc.getWorld()).getName())).dropItemNaturally(dropLoc, dropItem);
                Bukkit.getServer().broadcastMessage(Main.prefix + ServerJoinPlayer.getPlayerPrefixAndName(player) + ChatColor.LIGHT_PURPLE + " defeated a warden and was rewarded!");
            }
        }
    }
}
