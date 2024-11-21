package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import com.steve_rizzo.emeraldscore.events.skyblock.ScubaSuit;
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
    String title = ChatColor.GREEN + "" + ChatColor.BOLD + "EmeraldsMC" + ChatColor.GRAY + ChatColor.BOLD + " - " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Token Shop";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            openTokenShop(p);
        }
        return true;
    }

    private void openTokenShop(Player player) {

        Inventory tokenShop = Bukkit.createInventory(player, 54, title);

        String itemFormat = ChatColor.GRAY + "[" + ChatColor.GREEN + "%ENTER%" + ChatColor.GRAY + "] - " + ChatColor.GREEN
                + ChatColor.GRAY + "(" + ChatColor.GREEN + "%COST%" + ChatColor.GRAY + ")";

        if (Main.serverIDName.equalsIgnoreCase("smp")) {


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
                            .replace("%COST%", "15 TOKENS"),

                    itemTwentySevenName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Emerald Crate Key")
                            .replace("%COST%", "2 Tokens"),

                    itemTwentyEightName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Tools Crate Key")
                            .replace("%COST%", "2 Tokens"),

                    itemTwentyNineName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Eggs Crate Key")
                            .replace("%COST%", "1 Token"),

                    itemThirtyName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Food Crate Key")
                            .replace("%COST%", "1 Token"),

                    itemThirtyOneName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Decor Crate Key")
                            .replace("%COST%", "1 Token");

            addTokenShopItem(11, tokenShop, SPAWNER, itemTwoName, 10);
            addTokenShopItem(12, tokenShop, SPAWNER, itemThreeName, 10);
            addTokenShopItem(13, tokenShop, SPAWNER, itemFourName, 10);
            addTokenShopItem(14, tokenShop, SPAWNER, itemFiveName, 10);
            addTokenShopItem(15, tokenShop, SPAWNER, itemSixName, 10);

            addTokenShopItem(21, tokenShop, BEACON, itemTwelveName, 2);
            addTokenShopItem(22, tokenShop, NETHERITE_INGOT, itemThirteenName, 2);
            addTokenShopItem(23, tokenShop, PAPER, itemFourteenName, 3);

            addTokenShopItem(30, tokenShop, pouch.getType(), itemTwentyOneName, 10);
            addTokenShopItem(31, tokenShop, COOKED_BEEF, itemTwentyTwoName, 1);
            addTokenShopItem(32, tokenShop, trap.getType(), itemTwentyThreeName, 15);

            addTokenShopItem(38, tokenShop, OCHRE_FROGLIGHT, itemTwentySevenName, 2);
            addTokenShopItem(39, tokenShop, VERDANT_FROGLIGHT, itemTwentyEightName, 2);
            addTokenShopItem(40, tokenShop, PEARLESCENT_FROGLIGHT, itemTwentyNineName, 1);
            addTokenShopItem(41, tokenShop, SLIME_BLOCK, itemThirtyName, 1);
            addTokenShopItem(42, tokenShop, HONEY_BLOCK, itemThirtyOneName, 1);

        } else if (Main.serverIDName.equalsIgnoreCase("fac")) {



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
                                    + ChatColor.GREEN + "Regular Ground Buster")
                            .replace("%COST%", "5 TOKENS"),

                    itemThirteenName = "",

                    itemFourteenName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Large Ground Buster")
                            .replace("%COST%", "10 TOKENS"),

                    itemTwentyOneName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "MINING POUCH")
                            .replace("%COST%", "10 TOKENS"),

                    itemTwentyTwoName = "",


                    itemTwentyThreeName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "MOB TRAP")
                            .replace("%COST%", "15 TOKENS"),

                    itemTwentySevenName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Emerald Crate Key")
                            .replace("%COST%", "2 Tokens"),

                    itemTwentyEightName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Tools Crate Key")
                            .replace("%COST%", "2 Tokens"),

                    itemTwentyNineName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Factions Crate Key")
                            .replace("%COST%", "2 Tokens"),

                    itemThirtyName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Egg Crate Key")
                            .replace("%COST%", "2 Tokens"),

                    itemThirtyOneName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Decor Crate Key")
                            .replace("%COST%", "1 Token");

            addTokenShopItem(11, tokenShop, SPAWNER, itemTwoName, 10);
            addTokenShopItem(12, tokenShop, SPAWNER, itemThreeName, 10);
            addTokenShopItem(13, tokenShop, SPAWNER, itemFourName, 10);
            addTokenShopItem(14, tokenShop, SPAWNER, itemFiveName, 10);
            addTokenShopItem(15, tokenShop, SPAWNER, itemSixName, 10);

            addTokenShopItem(21, tokenShop, OBSIDIAN, itemTwelveName, 5);
            //addTokenShopItem(13, tokenShop, AIR, itemThirteenName, 0);
            addTokenShopItem(23, tokenShop, CRYING_OBSIDIAN, itemFourteenName, 10);

            addTokenShopItem(30, tokenShop, pouch.getType(), itemTwentyOneName, 10);
            //addTokenShopItem(31, tokenShop, COOKED_BEEF, itemTwentyTwoName, 1);
            addTokenShopItem(32, tokenShop, trap.getType(), itemTwentyThreeName, 15);

            addTokenShopItem(38, tokenShop, OCHRE_FROGLIGHT, itemTwentySevenName, 2);
            addTokenShopItem(39, tokenShop, VERDANT_FROGLIGHT, itemTwentyEightName, 2);
            addTokenShopItem(40, tokenShop, PEARLESCENT_FROGLIGHT, itemTwentyNineName, 2);
            addTokenShopItem(41, tokenShop, SLIME_BLOCK, itemThirtyName, 1);
            addTokenShopItem(42, tokenShop, HONEY_BLOCK, itemThirtyOneName, 1);

        } else if (Main.serverIDName.equalsIgnoreCase("sky")) {


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
                                    + ChatColor.GREEN + "SMALL CHUNK LOADER")
                            .replace("%COST%", "10 TOKENS"),

                    itemThirteenName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "LARGE CHUNK LOADER")
                            .replace("%COST%", "20 TOKENS"),

                    itemFourteenName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Spawner Crate Key")
                            .replace("%COST%", "10 TOKENS"),

                    itemTwentyOneName = "",

                    itemTwentyTwoName = "",


                    itemTwentyThreeName = "",

                    itemTwentySevenName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Cobble Crate Key")
                            .replace("%COST%", "2 Tokens"),

                    itemTwentyEightName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Lava Crate Key")
                            .replace("%COST%", "2 Tokens"),

                    itemTwentyNineName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Water Crate Key")
                            .replace("%COST%", "4 Tokens"),

                    itemThirtyName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Cloud Crate Key")
                            .replace("%COST%", "4 Tokens"),

                    itemThirtyOneName = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.GREEN + "Acid Crate Key")
                            .replace("%COST%", "6 Tokens"),

                    scubaHelm = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                            + ChatColor.AQUA + "Scuba Helmet")
                    .replace("%COST%", "5 Tokens"),

                    scubaChest = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.AQUA + "Scuba Chestplate")
                            .replace("%COST%", "5 Tokens"),

                    scubaLegs = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.AQUA + "Scuba Leggings")
                            .replace("%COST%", "5 Tokens"),

                    scubaBoots = itemFormat.replace("%ENTER%", ChatColor.RED + "1x "
                                    + ChatColor.AQUA + "Scuba Boots")
                            .replace("%COST%", "5 Tokens");


            addTokenShopItem(2, tokenShop, DIAMOND_HELMET, scubaHelm, 5);
            addTokenShopItem(3, tokenShop, DIAMOND_CHESTPLATE, scubaChest, 5);
            addTokenShopItem(5, tokenShop, DIAMOND_LEGGINGS, scubaLegs, 5);
            addTokenShopItem(6, tokenShop, DIAMOND_BOOTS, scubaBoots, 5);

            addTokenShopItem(11, tokenShop, SPAWNER, itemTwoName, 10);
            addTokenShopItem(12, tokenShop, SPAWNER, itemThreeName, 10);
            addTokenShopItem(13, tokenShop, SPAWNER, itemFourName, 10);
            addTokenShopItem(14, tokenShop, SPAWNER, itemFiveName, 10);
            addTokenShopItem(15, tokenShop, SPAWNER, itemSixName, 10);
            addTokenShopItem(19, tokenShop, OBSIDIAN, itemTwelveName, 10);
            addTokenShopItem(25, tokenShop, CRYING_OBSIDIAN, itemThirteenName, 20);
            addTokenShopItem(31, tokenShop, ZOMBIE_SPAWN_EGG, itemFourteenName, 10);

            //addTokenShopItem(13, tokenShop, AIR, itemThirteenName, 0);
            //addTokenShopItem(23, tokenShop, CRYING_OBSIDIAN, itemFourteenName, 10);

            //addTokenShopItem(30, tokenShop, pouch.getType(), itemTwentyOneName, 10);
            //addTokenShopItem(31, tokenShop, COOKED_BEEF, itemTwentyTwoName, 1);
            //addTokenShopItem(32, tokenShop, trap.getType(), itemTwentyThreeName, 15);

            addTokenShopItem(38, tokenShop, COBBLESTONE, itemTwentySevenName, 2);
            addTokenShopItem(39, tokenShop, MAGMA_BLOCK, itemTwentyEightName, 2);
            addTokenShopItem(40, tokenShop, LIGHT_BLUE_GLAZED_TERRACOTTA, itemTwentyNineName, 4);
            addTokenShopItem(41, tokenShop, GRAY_GLAZED_TERRACOTTA, itemThirtyName, 4);
            addTokenShopItem(42, tokenShop, PURPLE_GLAZED_TERRACOTTA, itemThirtyOneName, 6);


        }


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
        if (Main.serverIDName.equalsIgnoreCase("smp")) {
            switch (item.getType()) {
                case SPAWNER, ENDER_CHEST:
                    return 10;
                case BARREL:
                    return 15;
                case PAPER:
                    return 3;
                case BEACON, NETHERITE_INGOT, OCHRE_FROGLIGHT, VERDANT_FROGLIGHT:
                    return 2;
                case COOKED_BEEF, PEARLESCENT_FROGLIGHT, SLIME_BLOCK, HONEY_BLOCK:
                    return 1;
                default:
                    return 0;
            }
        } else if (Main.serverIDName.equalsIgnoreCase("fac")) {
            switch (item.getType()) {
                case SPAWNER, ENDER_CHEST, CRYING_OBSIDIAN:
                    return 10;
                case BARREL:
                    return 15;
                case OBSIDIAN:
                    return 5;
                case OCHRE_FROGLIGHT, VERDANT_FROGLIGHT, PEARLESCENT_FROGLIGHT:
                    return 2;
                case SLIME_BLOCK, HONEY_BLOCK:
                    return 1;
                default:
                    return 0;
            }
        } else if (Main.serverIDName.equalsIgnoreCase("sky")) {
            switch (item.getType()) {

                case SPAWNER, OBSIDIAN, ZOMBIE_SPAWN_EGG:
                    return 10;
                case COBBLESTONE, MAGMA_BLOCK:
                    return 2;
                case LIGHT_BLUE_GLAZED_TERRACOTTA,
                        GRAY_GLAZED_TERRACOTTA:
                    return 4;
                case PURPLE_GLAZED_TERRACOTTA:
                    return 6;
                case CRYING_OBSIDIAN:
                    return 20;
                case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS:
                    return 5;
            }
        }
        return 1000;
    }

    private boolean canPurchaseItem(Player player, int price) {
        if ((TokensAPI
                .returnTokensBalance(player)) >= price) return true;
        return false;
    }

    private void purchaseItem(Player player, ItemStack purchasedItem, int price) {

        TokensAPI.deductTokens(player, price);

        if (Main.serverIDName.equalsIgnoreCase("smp")) {

            switch (purchasedItem.getType()) {

                case SPAWNER:

                    // /spawners give <Player> <Type> [Amount]

                    if (purchasedItem.getItemMeta().hasDisplayName()) {

                        if (purchasedItem.getItemMeta().getDisplayName().contains("SKELETON SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " SKELETON 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x SKELETON SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;

                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("SPIDER SPAWNER") &&
                                (!purchasedItem.getItemMeta().getDisplayName().contains("CAVE SPIDER"))) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " SPIDER 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x SPIDER SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;

                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("ZOMBIE SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " ZOMBIE 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x ZOMBIE SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;


                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("CAVE SPIDER SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " CAVE_SPIDER 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x CAVE SPIDER SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;


                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("BLAZE SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " BLAZE 1");

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

                // Give Emerald Crate key
                case OCHRE_FROGLIGHT:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Emerald Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " emerald 1");

                    return;

                // Give Tools Crate key
                case VERDANT_FROGLIGHT:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Tool Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " tool 1");
                    return;

                // Give Eggs Crate key
                case PEARLESCENT_FROGLIGHT:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Egg Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " egg 1");
                    return;

                // Give Food Crate key
                case SLIME_BLOCK:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Food Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " food 1");
                    return;

                // Give Decor Crate key
                case HONEY_BLOCK:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Decor Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " decor 1");
                    return;
                default:
                    return;
            }
        } else if (Main.serverIDName.equalsIgnoreCase("fac")) {
            switch (purchasedItem.getType()) {

                case SPAWNER:

                    // /spawners give <Player> <Type> [Amount]

                    if (purchasedItem.getItemMeta().hasDisplayName()) {

                        if (purchasedItem.getItemMeta().getDisplayName().contains("SKELETON SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " SKELETON 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x SKELETON SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;

                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("SPIDER SPAWNER") &&
                                (!purchasedItem.getItemMeta().getDisplayName().contains("CAVE SPIDER"))) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " SPIDER 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x SPIDER SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;

                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("ZOMBIE SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " ZOMBIE 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x ZOMBIE SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;


                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("CAVE SPIDER SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " CAVE_SPIDER 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x CAVE SPIDER SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;


                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("BLAZE SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " BLAZE 1");

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


                // Give Emerald Crate key
                case OCHRE_FROGLIGHT:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Emerald Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " emerald 1");

                    return;

                // Give Tools Crate key
                case VERDANT_FROGLIGHT:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Tool Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " tool 1");
                    return;

                // Give Factions Crate key
                case PEARLESCENT_FROGLIGHT:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Factions Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " factions 1");
                    return;

                // Give Egg Crate key
                case SLIME_BLOCK:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Egg Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " egg 1");
                    return;

                // Give Decor Crate key
                case HONEY_BLOCK:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Decor Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " decor 1");
                    return;

                // Give SMALL Ground Buster
                case OBSIDIAN:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x SMALL Ground Buster"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "buster give " + player.getName() + " regular_buster 1");
                    return;

                // Give LARGE Ground Buster
                case CRYING_OBSIDIAN:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x LARGE Ground Buster"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "buster give " + player.getName() + " large_buster 1");
                    return;
                default:
                    return;
            }
        } else if (Main.serverIDName.equalsIgnoreCase("sky")) {
            switch (purchasedItem.getType()) {

                case SPAWNER:

                    // /spawners give <Player> <Type> [Amount]

                    if (purchasedItem.getItemMeta().hasDisplayName()) {

                        if (purchasedItem.getItemMeta().getDisplayName().contains("SKELETON SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawner give " + player.getName() + " SKELETON 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x SKELETON SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;

                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("SPIDER SPAWNER") &&
                                (!purchasedItem.getItemMeta().getDisplayName().contains("CAVE SPIDER"))) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawner give " + player.getName() + " SPIDER 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x SPIDER SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;

                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("ZOMBIE SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawner give " + player.getName() + " ZOMBIE 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x ZOMBIE SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;


                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("CAVE SPIDER SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawner give " + player.getName() + " CAVE_SPIDER 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.LIGHT_PURPLE + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.RED + "1x CAVE SPIDER SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                            );

                            return;


                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("BLAZE SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawner give " + player.getName() + " BLAZE 1");

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

                // Give Cobble Crate key
                case COBBLESTONE:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Cobble Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " cobble 1");

                    return;

                // Give Lava Crate key
                case MAGMA_BLOCK:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Lava Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " lava 1");
                    return;

                // Give Water Crate key
                case LIGHT_BLUE_GLAZED_TERRACOTTA:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Water Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " water 1");
                    return;

                // Give Cloud Crate key
                case GRAY_GLAZED_TERRACOTTA:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Cloud Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " cloud 1");
                    return;

                // Give Acid Crate key
                case PURPLE_GLAZED_TERRACOTTA:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Acid Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " acid 1");
                    return;

                // Give small chunk loader
                case OBSIDIAN:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Small Chunk Loader"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "loader give " + player.getName() + " normal_loader 1");
                    return;

                // Give large chunk loader
                case CRYING_OBSIDIAN:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Large Chunk Loader"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "loader give " + player.getName() + " large_loader 1");
                    return;

                case ZOMBIE_SPAWN_EGG:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x Spawner Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " spawner 1");
                    return;

                case DIAMOND_HELMET:

                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x SCUBA HELMET"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    player.getInventory().addItem(ScubaSuit.scubaHelmet);

                    return;

                case DIAMOND_CHESTPLATE:

                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x SCUBA CHESTPLATE"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    player.getInventory().addItem(ScubaSuit.scubaChestplate);

                    return;

                case DIAMOND_LEGGINGS:

                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x SCUBA LEGGINGS"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    player.getInventory().addItem(ScubaSuit.scubaLeggings);

                    return;

                case DIAMOND_BOOTS:

                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.LIGHT_PURPLE + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.RED + "1x SCUBA BOOTS"
                            + ChatColor.GRAY + ")" + ChatColor.LIGHT_PURPLE + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.LIGHT_PURPLE + "!"
                    );

                    player.getInventory().addItem(ScubaSuit.scubaBoots);

                    return;
            }
        }
    }
}
