package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import com.steve_rizzo.emeraldscore.commands.tokens.TokensAPI;
import com.steve_rizzo.emeraldscore.utils.Ranks;
import net.milkbowl.vault.permission.Permission;
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

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Material.*;

public class RankShopCommand implements Listener, CommandExecutor {

    public static Permission perms = Main.perms;
    String title = ChatColor.AQUA + "EmeraldsMC" + ChatColor.GRAY + " - " + ChatColor.GREEN + "Rank Shop";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            openRankShop(p);
        }
        return true;
    }

    private void openRankShop(Player player) {

        Inventory rankShop = Bukkit.createInventory(player, 9, title);

        String donorRankFormat = ChatColor.GRAY + "[" + ChatColor.GREEN + "%ENTER%" + ChatColor.GRAY + "]" + ChatColor.GREEN
                + " RANK " + ChatColor.GRAY + "(" + ChatColor.GREEN + "%COST%" + ChatColor.GRAY + ")";

        String donorRankOneName = donorRankFormat.replace("%ENTER%", "$")
                .replace("%COST%", "$1,000,000"),
                donorRankTwoName = donorRankFormat.replace("%ENTER%", "$$")
                        .replace("%COST%", "$2,000,000"),
                donorRankThreeName = donorRankFormat.replace("%ENTER%", "$$$")
                        .replace("%COST%", "$3,000,000"),
                donorRankFourName = donorRankFormat.replace("%ENTER%", ChatColor.LIGHT_PURPLE + "<3" + ChatColor.GREEN + "$$$")
                        .replace("%COST%", "$4,000,000"),
                donorRankEliteName = donorRankFormat.replace("%ENTER%", "ELITE")
                        .replace("%COST%", "$5,000,000"),
                donorRankPlatinumName = donorRankFormat.replace("%ENTER%", ChatColor.DARK_AQUA + "PLATINUM")
                        .replace("%COST%", "500 EMERALD TOKENS");

        addRankItem(rankShop, COAL_BLOCK, donorRankOneName, 1000000);
        addRankItem(rankShop, IRON_BLOCK, donorRankTwoName, 2000000);
        addRankItem(rankShop, GOLD_BLOCK, donorRankThreeName, 3000000);
        addRankItem(rankShop, Material.DIAMOND_BLOCK, donorRankFourName, 4000000);
        addRankItem(rankShop, Material.EMERALD_BLOCK, donorRankEliteName, 5000000);
        addRankItem(rankShop, NETHER_STAR, donorRankPlatinumName, 500);

        player.openInventory(rankShop);

    }

    private void addRankItem(Inventory inventory, Material material, String name, int price) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);

        inventory.addItem(item);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(title)) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() != Material.AIR) {

                if (clickedItem.getType().equals(NETHER_STAR)) {

                    int tokens = getPrice(clickedItem);

                    // Check user token bal >= cost of rank
                    if (TokensAPI.returnUUIDBTokensalance(player.getUniqueId().toString()) >= tokens) {

                        // Check if user is ELITE (required for PLAT upgrade)
                        if (hasPreviousRank(player, clickedItem)) {

                            // Take the tokens
                            TokensAPI.deductTokensUUID(player.getUniqueId().toString(), tokens);

                            // Upgrade user rank
                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "lp user " + player.getName() + " parent set " + "platinum");

                            // Display message to user, close inv
                            player.sendMessage(Main.prefix + ChatColor.GREEN + "Congratulations on your new rank!");
                            player.closeInventory();

                            return;
                        }
                    }
                }

                int price = getPrice(clickedItem);

                if (price > 0) {

                    if (canPurchaseRank(player, price)) {

                        if (hasPreviousRank(player, clickedItem)) {

                            // Perform rank purchase
                            // Implement rank upgrade logic here
                            purchaseRank(player, clickedItem, price);
                            player.sendMessage(Main.prefix + ChatColor.GREEN + "Congratulations on your new rank!");
                            player.closeInventory();

                        } else {

                            player.sendMessage(Main.prefix + ChatColor.RED + "Sorry, you can only purchase the next sequential rank (in order).");

                        }
                    } else {

                        player.sendMessage(Main.prefix + ChatColor.RED + "Sorry, you have insufficient funds.");

                    }
                }
            }
            player.closeInventory();
        }
    }

    private int getPrice(ItemStack item) {
        switch (item.getType()) {
            case COAL_BLOCK:
                return 1000000;
            case IRON_BLOCK:
                return 2000000;
            case GOLD_BLOCK:
                return 3000000;
            case DIAMOND_BLOCK:
                return 4000000;
            case EMERALD_BLOCK:
                return 5000000;
            case NETHER_STAR:
                return 500;
            default:
                return 0;
        }
    }

    private boolean canPurchaseRank(Player player, int price) {
        return EmeraldsCashAPI.returnBalance(player) >= price;
    }

    private boolean hasPreviousRank(Player player, ItemStack rankItem) {

        String playerRank = perms.getPrimaryGroup(player);

        switch (rankItem.getType()) {
            case COAL_BLOCK:
                // Attempting $ rank, requires 'Member'
                return playerRank.equalsIgnoreCase("Member");
            case IRON_BLOCK:
                // Attempting $$ rank, requires '$'
                return playerRank.equalsIgnoreCase("donor1");
            case GOLD_BLOCK:
                // Attempting $$$ rank, requires '$$'
                return playerRank.equalsIgnoreCase("donor2");
            case DIAMOND_BLOCK:
                // Attempting <3$$$ rank, requires '$$$'
                return playerRank.equalsIgnoreCase("donor3");
            case EMERALD_BLOCK:
                // Attempting ELITE rank, requires '<3$$$'
                return playerRank.equalsIgnoreCase("donor4");
            case NETHER_STAR:
                return playerRank.equalsIgnoreCase("elite");
            default:
                return false;
        }
    }

    private void purchaseRank(Player player, ItemStack rankItem, int price) {

        getServer().dispatchCommand(getServer().getConsoleSender(),
                "takebal " + player.getName() + " " + price);

        switch (rankItem.getType()) {
            case COAL_BLOCK:
                // Purchasing $ rank
                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "lp user " + player.getName() + " parent set " + "donor1");
                break;
            case IRON_BLOCK:
                // Purchasing $$ rank
                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "lp user " + player.getName() + " parent set " + "donor2");
                break;
            case GOLD_BLOCK:
                // Attempting $$$ rank
                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "lp user " + player.getName() + " parent set " + "donor3");
                break;
            case DIAMOND_BLOCK:
                // Attempting <3$$$ rank
                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "lp user " + player.getName() + " parent set " + "donor4");
                break;
            case EMERALD_BLOCK:
                // Attempting ELITE rank
                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "lp user " + player.getName() + " parent set " + "elite");
                break;
        }
    }
}
