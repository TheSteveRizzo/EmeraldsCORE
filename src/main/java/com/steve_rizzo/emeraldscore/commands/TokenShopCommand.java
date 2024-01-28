package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import com.steve_rizzo.emeraldscore.features.miningpouch.Pouch;
import net.milkbowl.vault.permission.Permission;
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

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Material.*;

public class TokenShopCommand implements Listener, CommandExecutor {

    public static Permission perms = Main.perms;
    String title = ChatColor.AQUA + "" + ChatColor.BOLD + "EmeraldsMC" + ChatColor.GRAY + " - " + ChatColor.RED + "Token Shop";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            openTokenShop(p);
        }
        return true;
    }

    private void openTokenShop(Player player) {

        Inventory tokenShop = Bukkit.createInventory(player, 27, title);

        String itemFormat = ChatColor.GRAY + "[" + ChatColor.GREEN + "%ENTER%" + ChatColor.GRAY + "] - " + ChatColor.GREEN
                + ChatColor.GRAY + "(" + ChatColor.GREEN + "%COST%" + ChatColor.GRAY + ")";

        ItemStack pouch = new ItemStack(ENDER_CHEST);
        ItemStack trap = new ItemStack(BARREL);


        String itemTwoName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                        + ChatColor.GREEN + "SKELETON SPAWNER")
                .replace("%COST%", "10 TOKENS"),
                itemThreeName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                + ChatColor.GREEN + "SPIDER SPAWNER")
                        .replace("%COST%", "10 TOKENS"),
                itemFourName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                + ChatColor.GREEN + "ZOMBIE SPAWNER")
                        .replace("%COST%", "10 TOKENS"),
                itemFiveName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                + ChatColor.GREEN + "CAVE SPIDER SPAWNER")
                        .replace("%COST%", "10 TOKENS"),
                itemSixName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                + ChatColor.GREEN + "BLAZE SPAWNER")
                        .replace("%COST%", "10 TOKENS"),

                itemTwelveName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                + ChatColor.GREEN + "ITEM VOUCHER")
                        .replace("%COST%", "2 TOKENS"),

                itemThirteenName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                + ChatColor.GREEN + "NETHER VOUCHER")
                        .replace("%COST%", "2 TOKENS"),

                itemFourteenName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                + ChatColor.GREEN + "LOTTERY VOUCHER")
                        .replace("%COST%", "3 TOKENS"),


                itemTwentyOneName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                + ChatColor.GREEN + "MINING POUCH")
                        .replace("%COST%", "10 TOKENS"),

                itemTwentyTwoName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                + ChatColor.GREEN + "FOOD PACKAGE")
                        .replace("%COST%", "1 TOKEN"),


                itemTwentyThreeName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                + ChatColor.GREEN + "MOB TRAP")
                        .replace("%COST%", "15 TOKENS");


        addTokenShopItem(2, tokenShop, SPAWNER, itemTwoName, 10);
        addTokenShopItem(3, tokenShop, SPAWNER, itemThreeName, 10);
        addTokenShopItem(4, tokenShop, SPAWNER, itemFourName, 10);
        addTokenShopItem(5, tokenShop, SPAWNER, itemFiveName, 10);
        addTokenShopItem(6, tokenShop, SPAWNER, itemSixName, 10);

        addTokenShopItem(12, tokenShop, BEACON, itemTwelveName, 2);
        addTokenShopItem(13, tokenShop, NETHERITE_INGOT, itemThirteenName, 2);
        addTokenShopItem(14, tokenShop, PAPER, itemFourteenName, 3);

        addTokenShopItem(21, tokenShop, pouch.getType(), itemTwentyOneName, 10);
        addTokenShopItem(22, tokenShop, COOKED_BEEF, itemTwentyTwoName, 1);
        addTokenShopItem(23, tokenShop, trap.getType(), itemTwentyThreeName, 15);

        player.openInventory(tokenShop);

    }

    private void addTokenShopItem(int slotNum, Inventory inventory, Material material, String name, int price) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);

        inventory.setItem(slotNum, item);
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

                    if (canPurchaseItem(player, price)) {

                        // Perform item purchase
                        purchaseItem(player, clickedItem, price);
                        player.sendMessage(Main.prefix + ChatColor.GREEN + "Congratulations on your new purchase!");
                        player.closeInventory();


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
            case SPAWNER, ENDER_CHEST:
                return 10;
            case BARREL:
                return 15;
            case PAPER:
                return 3;
            case BEACON, NETHERITE_INGOT:
                return 2;
            case COOKED_BEEF:
                return 1;
            default:
                return 0;
        }
    }

    private boolean canPurchaseItem(Player player, int price) {
        if ((TokensAPI
                .returnTokensBalance(player)) >= price) return true;
        return false;
    }

    private void purchaseItem(Player player, ItemStack rankItem, int price) {

        TokensAPI.deductTokens(player, price);

        switch (rankItem.getType()) {

            case SPAWNER:

                // /silkspawners give <Player> <Type> [Amount]

                if (rankItem.getItemMeta().hasDisplayName()) {

                    if (rankItem.getItemMeta().getDisplayName().contains("SKELETON SPAWNER")) {

                        getServer().dispatchCommand(getServer().getConsoleSender(),
                                "silkspawners give " + player.getName() + " skeleton 1");

                        getServer().broadcastMessage(Main.prefix
                                + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                + ChatColor.LIGHT_PURPLE + " just purchased "
                                + ChatColor.GRAY + "(" + ChatColor.RED + "1x SKELETON SPAWNER"
                                + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                        );

                        return;

                    } else if (rankItem.getItemMeta().getDisplayName().contains("SPIDER SPAWNER") &&
                            (!rankItem.getItemMeta().getDisplayName().contains("CAVE SPIDER"))) {

                        getServer().dispatchCommand(getServer().getConsoleSender(),
                                "silkspawners give " + player.getName() + " spider 1");

                        getServer().broadcastMessage(Main.prefix
                                + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                + ChatColor.LIGHT_PURPLE + " just purchased "
                                + ChatColor.GRAY + "(" + ChatColor.RED + "1x SPIDER SPAWNER"
                                + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                        );

                        return;

                    } else if (rankItem.getItemMeta().getDisplayName().contains("ZOMBIE SPAWNER")) {

                        getServer().dispatchCommand(getServer().getConsoleSender(),
                                "silkspawners give " + player.getName() + " zombie 1");

                        getServer().broadcastMessage(Main.prefix
                                + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                + ChatColor.LIGHT_PURPLE + " just purchased "
                                + ChatColor.GRAY + "(" + ChatColor.RED + "1x ZOMBIE SPAWNER"
                                + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                        );

                        return;


                    } else if (rankItem.getItemMeta().getDisplayName().contains("CAVE SPIDER SPAWNER")) {

                        getServer().dispatchCommand(getServer().getConsoleSender(),
                                "silkspawners give " + player.getName() + " cave_spider 1");

                        getServer().broadcastMessage(Main.prefix
                                + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                + ChatColor.LIGHT_PURPLE + " just purchased "
                                + ChatColor.GRAY + "(" + ChatColor.RED + "1x CAVE SPIDER SPAWNER"
                                + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                        );

                        return;


                    } else if (rankItem.getItemMeta().getDisplayName().contains("BLAZE SPAWNER")) {

                        getServer().dispatchCommand(getServer().getConsoleSender(),
                                "silkspawners give " + player.getName() + " blaze 1");

                        getServer().broadcastMessage(Main.prefix
                                + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                + ChatColor.LIGHT_PURPLE + " just purchased "
                                + ChatColor.GRAY + "(" + ChatColor.RED + "1x BLAZE SPAWNER"
                                + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                        );

                        return;


                    }
                }

                return;

            // Give Mining Pouch
            case ENDER_CHEST:
                getServer().broadcastMessage(Main.prefix
                        + ServerJoinPlayer.getPlayerPrefixAndName(player)
                        + ChatColor.LIGHT_PURPLE + " just purchased "
                        + ChatColor.GRAY + "(" + ChatColor.RED + "1x MINING POUCH"
                        + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                        + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                );

                Pouch pouch = new Pouch(); // Create an empty pouch
                player.getInventory().addItem(pouch.getPouch()); // The pouch already has the correct metadata

                return;

            // Give NEKO Trap
            case BARREL:
                getServer().broadcastMessage(Main.prefix
                        + ServerJoinPlayer.getPlayerPrefixAndName(player)
                        + ChatColor.LIGHT_PURPLE + " just purchased "
                        + ChatColor.GRAY + "(" + ChatColor.RED + "1x MOB TRAP"
                        + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                        + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                );

                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "nekotraps give " + player.getName());

                return;

            // Give Food Package
            case COOKED_BEEF:
                getServer().broadcastMessage(Main.prefix
                        + ServerJoinPlayer.getPlayerPrefixAndName(player)
                        + ChatColor.LIGHT_PURPLE + " just purchased "
                        + ChatColor.GRAY + "(" + ChatColor.RED + "1x FOOD PACKAGE"
                        + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                        + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                );

                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "vouchers give " + player.getName() + " FoodPackage 1");

                return;

            // Give Item Voucher
            case BEACON:
                getServer().broadcastMessage(Main.prefix
                        + ServerJoinPlayer.getPlayerPrefixAndName(player)
                        + ChatColor.LIGHT_PURPLE + " just purchased "
                        + ChatColor.GRAY + "(" + ChatColor.RED + "1x ITEM VOUCHER"
                        + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                        + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                );

                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "vouchers give " + player.getName() + " ItemVoucher 1");

                return;

            // Give Nether Voucher
            case NETHERITE_INGOT:
                getServer().broadcastMessage(Main.prefix
                        + ServerJoinPlayer.getPlayerPrefixAndName(player)
                        + ChatColor.LIGHT_PURPLE + " just purchased "
                        + ChatColor.GRAY + "(" + ChatColor.RED + "1x NETHER VOUCHER"
                        + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                        + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                );

                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "vouchers give " + player.getName() + " NetherVoucher 1");

                return;

            // Give Lottery Voucher
            case PAPER:
                getServer().broadcastMessage(Main.prefix
                        + ServerJoinPlayer.getPlayerPrefixAndName(player)
                        + ChatColor.LIGHT_PURPLE + " just purchased "
                        + ChatColor.GRAY + "(" + ChatColor.RED + "1x LOTTERY VOUCHER"
                        + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                        + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                );

                getServer().dispatchCommand(getServer().getConsoleSender(),
                        "vouchers give " + player.getName() + " LotteryVoucher 1");

                return;
        }
    }
}
