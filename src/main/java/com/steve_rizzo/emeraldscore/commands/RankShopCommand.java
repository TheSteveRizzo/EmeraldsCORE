package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import com.steve_rizzo.emeraldscore.utils.Ranks;
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

    Ranks ranks;
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
                .replace("%COST%", "$250,000"),
                donorRankTwoName = donorRankFormat.replace("%ENTER%", "$$")
                        .replace("%COST%", "$500,000"),
                donorRankThreeName = donorRankFormat.replace("%ENTER%", "$$$")
                        .replace("%COST%", "$750,000"),
                donorRankFourName = donorRankFormat.replace("%ENTER%", ChatColor.LIGHT_PURPLE + "<3" + ChatColor.GREEN + "$$$")
                        .replace("%COST", "$1,000,000"),
                donorRankEliteName = donorRankFormat.replace("%ENTER%", "ELITE")
                        .replace("%COST", "$1,500,000");


        addRankItem(rankShop, COAL_BLOCK, donorRankOneName, 250000);
        addRankItem(rankShop, IRON_BLOCK, donorRankTwoName, 500000);
        addRankItem(rankShop, GOLD_BLOCK, donorRankThreeName, 750000);
        addRankItem(rankShop, Material.DIAMOND_BLOCK, donorRankFourName, 1000000);
        addRankItem(rankShop, Material.EMERALD_BLOCK, donorRankEliteName, 1500000);

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
                int price = getPrice(clickedItem);
                if (price > 0) {
                    if (canPurchaseRank(player, price)) {

                        if (hasPreviousRank(player, clickedItem)) {

                            // Perform rank purchase
                            // Implement rank upgrade logic here
                            purchaseRank(player, clickedItem);
                            player.sendMessage(Main.prefix + ChatColor.GREEN + "Congratulations on your new rank!");
                            player.closeInventory();

                        } else {

                            player.sendMessage(Main.prefix + ChatColor.RED + "Sorry. You can only purchase the next available rank, in order.");

                        }

                    } else {

                        player.sendMessage(Main.prefix + "You have insufficient funds to purchase this rank.");

                    }
                }
            }
            player.closeInventory();
        }
    }

    private int getPrice(ItemStack item) {
        switch (item.getType()) {
            case COAL_BLOCK:
                return 250000;
            case IRON_BLOCK:
                return 500000;
            case GOLD_BLOCK:
                return 750000;
            case DIAMOND_BLOCK:
                return 1000000;
            case EMERALD_BLOCK:
                return 1500000;
            default:
                return 0;
        }
    }

    private boolean canPurchaseRank(Player player, int price) {
        if (EmeraldsCashAPI.getBalance(player) >= price) return true;
        return false;
    }

    private boolean hasPreviousRank(Player player, ItemStack rankItem) {

        String playerRank = ranks.getRank(player);

        switch (rankItem.getType()) {
            case COAL_BLOCK:
                // Attempting $ rank, requires 'Member'
                if (playerRank.equalsIgnoreCase("Member")) return true;
                return false;
            case IRON_BLOCK:
                // Attempting $$ rank, requires '$'
                if (playerRank.equalsIgnoreCase("donor1")) return true;
                return false;
            case GOLD_BLOCK:
                // Attempting $$$ rank, requires '$$'
                if (playerRank.equalsIgnoreCase("donor2")) return true;
                return false;
            case DIAMOND_BLOCK:
                // Attempting <3$$$ rank, requires '$$$'
                if (playerRank.equalsIgnoreCase("donor3")) return true;
                return false;
            case EMERALD_BLOCK:
                // Attempting ELITE rank, requires '<3$$$'
                if (playerRank.equalsIgnoreCase("donor4")) return true;
                return false;
            default:
                return false;

        }
    }

    private void purchaseRank(Player player, ItemStack rankItem) {

        getServer().dispatchCommand(getServer().getConsoleSender(),
                "user " + player.getName());

        switch (rankItem.getType()) {
            case COAL_BLOCK:
                // Purchasing $ rank
                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "user setgroup donor1");
            case IRON_BLOCK:
                // Purchasing $$ rank
                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "user setgroup donor2");
            case GOLD_BLOCK:
                // Attempting $$$ rank
                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "user setgroup donor3");
            case DIAMOND_BLOCK:
                // Attempting <3$$$ rank
                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "user setgroup donor4");
            case EMERALD_BLOCK:
                // Attempting ELITE rank
                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "user setgroup elite");
        }
    }
}
