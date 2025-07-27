package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import com.steve_rizzo.emeraldscore.commands.tokens.TokensAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BuyTokensCommand implements CommandExecutor, Listener {

    String title = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Buy Tokens";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;
        openTokenShop(player);
        return true;
    }

    private void openTokenShop(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, title);

        // Add token options to the inventory
        inventory.setItem(0, createTokenItem("1 Token", Material.EMERALD, 50000, 1));
        inventory.setItem(1, createTokenItem("2 Tokens", Material.EMERALD, 100000, 2));
        inventory.setItem(2, createTokenItem("3 Tokens", Material.EMERALD, 150000, 3));
        inventory.setItem(3, createTokenItem("4 Tokens", Material.EMERALD, 200000, 4));
        inventory.setItem(4, createTokenItem("5 Tokens", Material.DIAMOND_BLOCK, 250000, 5));
        inventory.setItem(5, createTokenItem("10 Tokens", Material.EMERALD_BLOCK, 500000, 10));
        inventory.setItem(6, createTokenItem("15 Tokens", Material.DIAMOND, 750000, 15));
        inventory.setItem(7, createTokenItem("20 Tokens", Material.NETHERITE_BLOCK, 1000000, 20));
        inventory.setItem(8, createTokenItem("25 Tokens", Material.NETHERITE_BLOCK, 1250000, 25));

        player.openInventory(inventory);
    }

    private ItemStack createTokenItem(String name, Material material, int cost, int tokenAmount) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        lore.add("Cost: $" + String.format("%,d", cost)); // Format with commas
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null || !event.getView().getTitle().equals(title)) return;

        event.setCancelled(true); // Prevent item from being picked up

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // Check if the clicked item is a token option
        int cost = getTokenCost(clickedItem);
        int tokenAmount = getTokenAmount(clickedItem);
        if (cost <= 0 || tokenAmount <= 0) return;

        // Check if the player has enough funds
        if (hasEnoughFunds(player, cost)) {

            EmeraldsCashAPI.deductFunds(player, cost);
            TokensAPI.addTokens(player, tokenAmount);
            player.sendMessage(Main.prefix + ChatColor.GREEN + "Your purchase of " + ChatColor.AQUA + tokenAmount + " tokens" + ChatColor.GREEN + " was successful!");

        } else {
            player.sendMessage(Main.prefix + ChatColor.RED + "You don't have enough funds to purchase this.");
        }
    }

    private int getTokenCost(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return 0;

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) return 0;

        String lastLine = lore.get(lore.size() - 1);
        String costString = lastLine.replace("Cost: $", "").replace(",", ""); // Remove commas
        try {
            return Integer.parseInt(costString);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int getTokenAmount(ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        try {
            return Integer.parseInt(name.split(" ")[0]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean hasEnoughFunds(Player player, int cost) {
        return EmeraldsCashAPI.returnBalance(player) >= cost;
    }
}
