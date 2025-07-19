package com.steve_rizzo.emeraldscore.events.skyblock;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ScubaSuit implements Listener {

    // NamespacedKey for identifying the scuba suit pieces
    private static final NamespacedKey scubaKey = new NamespacedKey(Main.getInstance(), "scuba_suit");

    // Scuba Armor pieces
    public static final ItemStack scubaHelmet = createScubaHelmet();
    public static final ItemStack scubaChestplate = createScubaChestplate();
    public static final ItemStack scubaLeggings = createScubaLeggings();
    public static final ItemStack scubaBoots = createScubaBoots();

    private static ItemStack createScubaHelmet() {
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta meta = helmet.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "💎 Scuba Helmet 💎");
            meta.getPersistentDataContainer().set(scubaKey, PersistentDataType.STRING, "scuba_helmet");
            helmet.setItemMeta(meta);
        }
        return helmet;
    }

    private static ItemStack createScubaChestplate() {
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta meta = chestplate.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "💎 Scuba Chestplate 💎");
            meta.getPersistentDataContainer().set(scubaKey, PersistentDataType.STRING, "scuba_chestplate");
            chestplate.setItemMeta(meta);
        }
        return chestplate;
    }

    private static ItemStack createScubaLeggings() {
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemMeta meta = leggings.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "💎 Scuba Leggings 💎");
            meta.getPersistentDataContainer().set(scubaKey, PersistentDataType.STRING, "scuba_leggings");
            leggings.setItemMeta(meta);
        }
        return leggings;
    }

    private static ItemStack createScubaBoots() {
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta meta = boots.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "💎 Scuba Boots 💎");
            meta.getPersistentDataContainer().set(scubaKey, PersistentDataType.STRING, "scuba_boots");
            boots.setItemMeta(meta);
        }
        return boots;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        // Check if the player is in a boat
        if (player.isInsideVehicle() && player.getVehicle() instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) player.getVehicle();
            if (vehicle.getType().name().equalsIgnoreCase("BOAT")) {
                if (isWearingScubaSuit(player)) {
                    player.setNoDamageTicks(20); // Prevent damage for 20 ticks (1 second)
                }
            }
        }

        // Check if the player is in water
        if (player.isInWater()) {
            if (isWearingScubaSuit(player)) {
                player.setNoDamageTicks(20); // Prevent damage for 20 ticks (1 second)
            }
        }
    }

    // Method to check if the player is wearing the full Scuba Suit
    private boolean isWearingScubaSuit(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        return isScubaPiece(helmet, "scuba_helmet")
                && isScubaPiece(chestplate, "scuba_chestplate")
                && isScubaPiece(leggings, "scuba_leggings")
                && isScubaPiece(boots, "scuba_boots");
    }

    // Helper method to check if an armor piece is part of the Scuba Suit using the custom PersistentData
    private boolean isScubaPiece(ItemStack item, String type) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(scubaKey, PersistentDataType.STRING)
                && meta.getPersistentDataContainer().get(scubaKey, PersistentDataType.STRING).equals(type);
    }
}