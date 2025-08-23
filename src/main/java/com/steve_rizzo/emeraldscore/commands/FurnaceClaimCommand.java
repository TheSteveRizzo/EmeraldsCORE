package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FurnaceClaimCommand implements CommandExecutor {

    // Define a list of all smelted items
    private final List<Material> smeltedItems = Arrays.asList(
            Material.IRON_INGOT, Material.COPPER_INGOT, Material.GOLD_INGOT,
            Material.NETHERITE_SCRAP, Material.COOKED_BEEF, Material.COOKED_PORKCHOP,
            Material.COOKED_MUTTON, Material.COOKED_CHICKEN, Material.COOKED_RABBIT,
            Material.COOKED_COD, Material.COOKED_SALMON, Material.STONE,
            Material.SMOOTH_STONE, Material.CRACKED_STONE_BRICKS, Material.DEEPSLATE,
            Material.CRACKED_DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_TILES,
            Material.SMOOTH_SANDSTONE, Material.SMOOTH_RED_SANDSTONE, Material.CRACKED_NETHER_BRICKS,
            Material.SMOOTH_BASALT, Material.CRACKED_POLISHED_BLACKSTONE_BRICKS,
            Material.SMOOTH_QUARTZ, Material.TERRACOTTA, Material.BLACK_GLAZED_TERRACOTTA, Material.BLUE_GLAZED_TERRACOTTA,
            Material.BROWN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.CYAN_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA,
            Material.BROWN_GLAZED_TERRACOTTA, Material.GRAY_GLAZED_TERRACOTTA, Material.ORANGE_GLAZED_TERRACOTTA, Material.PINK_GLAZED_TERRACOTTA,
            Material.PURPLE_GLAZED_TERRACOTTA, Material.RED_GLAZED_TERRACOTTA, Material.WHITE_GLAZED_TERRACOTTA, Material.MAGENTA_GLAZED_TERRACOTTA,
            Material.YELLOW_GLAZED_TERRACOTTA, Material.LIGHT_BLUE_GLAZED_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA ,
            Material.GLASS, Material.SPONGE, Material.CHARCOAL, Material.POPPED_CHORUS_FRUIT,
            Material.LIME_DYE, Material.GREEN_DYE, Material.BRICK, Material.NETHER_BRICK
    );

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("emeraldsmc.furnaceclaim")) {
            player.sendMessage(Main.prefix + ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        int totalItems = 0;
        Map<Material, Integer> itemsAdded = new HashMap<>();

        Location playerLocation = player.getLocation();
        int radius = 10;

        for (int x = playerLocation.getBlockX() - radius; x <= playerLocation.getBlockX() + radius; x++) {
            for (int y = playerLocation.getBlockY() - radius; y <= playerLocation.getBlockY() + radius; y++) {
                for (int z = playerLocation.getBlockZ() - radius; z <= playerLocation.getBlockZ() + radius; z++) {
                    Block block = playerLocation.getWorld().getBlockAt(x, y, z);
                    if (block.getState() instanceof Furnace) {
                        Furnace furnace = (Furnace) block.getState();
                        Inventory furnaceInv = furnace.getInventory();

                        for (ItemStack item : furnaceInv.getContents()) {
                            if (item != null && isSmeltedItem(item.getType())) {
                                player.getInventory().addItem(item);
                                totalItems += item.getAmount();
                                furnaceInv.remove(item);

                                // Track the amount of each item added
                                itemsAdded.put(item.getType(), itemsAdded.getOrDefault(item.getType(), 0) + item.getAmount());
                            }
                        }
                    }
                }
            }
        }

        if (!itemsAdded.isEmpty()) {
            StringBuilder message = new StringBuilder(ChatColor.GRAY + "Transferred items to your inventory: ");
            for (Map.Entry<Material, Integer> entry : itemsAdded.entrySet()) {
                message.append(entry.getValue()).append(ChatColor.GRAY + "x ").append(ChatColor.AQUA + entry.getKey().toString()).append(ChatColor.GRAY + ", ");
            }
            message.setLength(message.length() - 2); // Remove the trailing comma and space
            player.sendMessage(Main.prefix + message.toString());
        } else {
            player.sendMessage(Main.prefix + ChatColor.YELLOW + "No smelted items found in nearby furnaces.");
        }

        return true;
    }

    // Check if the item is a smelted item
    private boolean isSmeltedItem(Material material) {
        return smeltedItems.contains(material);
    }
}
