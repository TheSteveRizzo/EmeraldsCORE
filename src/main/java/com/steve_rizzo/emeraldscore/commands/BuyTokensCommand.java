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

    String title = ChatColor.GREEN + "" + ChatColor.BOLD + "Buy Tokens";
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
        ItemStack oneToken = createTokenItem("1 Token", Material.EMERALD, 75000, 1);
        ItemStack twoTokens = createTokenItem("2 Tokens", Material.EMERALD, 150000, 2);
        ItemStack fourTokens = createTokenItem("4 Tokens", Material.EMERALD, 300000, 4);
        ItemStack fiveTokens = createTokenItem("5 Tokens", Material.EMERALD_BLOCK, 350000, 5);

        inventory.setItem(0, oneToken);
        inventory.setItem(1, twoTokens);
        inventory.setItem(2, fourTokens);
        inventory.setItem(3, fiveTokens);

        player.openInventory(inventory);
    }

    private ItemStack createTokenItem(String name, Material material, int cost, int tokenAmount) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        lore.add("Cost: $" + cost);
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
        String costString = lastLine.replace("Cost: $", "");
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
        if (EmeraldsCashAPI.returnBalance(player) >= cost) {
            return true;
        }
        return false;
    }
}
