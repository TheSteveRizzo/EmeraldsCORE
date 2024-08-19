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

            addTokenShopItem(29, tokenShop, OCHRE_FROGLIGHT, itemTwentySevenName, 2);
            addTokenShopItem(30, tokenShop, VERDANT_FROGLIGHT, itemTwentyEightName, 2);
            addTokenShopItem(31, tokenShop, PEARLESCENT_FROGLIGHT, itemTwentyNineName, 1);
            addTokenShopItem(32, tokenShop, SLIME_BLOCK, itemThirtyName, 1);
            addTokenShopItem(33, tokenShop, HONEY_BLOCK, itemThirtyOneName, 1);

        } else if (Main.serverIDName.equalsIgnoreCase("factions")) {



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

            addTokenShopItem(2, tokenShop, SPAWNER, itemTwoName, 10);
            addTokenShopItem(3, tokenShop, SPAWNER, itemThreeName, 10);
            addTokenShopItem(4, tokenShop, SPAWNER, itemFourName, 10);
            addTokenShopItem(5, tokenShop, SPAWNER, itemFiveName, 10);
            addTokenShopItem(6, tokenShop, SPAWNER, itemSixName, 10);

            addTokenShopItem(12, tokenShop, OBSIDIAN, itemTwelveName, 5);
            //addTokenShopItem(13, tokenShop, AIR, itemThirteenName, 0);
            addTokenShopItem(14, tokenShop, CRYING_OBSIDIAN, itemFourteenName, 10);

            addTokenShopItem(21, tokenShop, pouch.getType(), itemTwentyOneName, 10);
            addTokenShopItem(22, tokenShop, COOKED_BEEF, itemTwentyTwoName, 1);
            addTokenShopItem(23, tokenShop, trap.getType(), itemTwentyThreeName, 15);

            addTokenShopItem(29, tokenShop, OCHRE_FROGLIGHT, itemTwentySevenName, 2);
            addTokenShopItem(30, tokenShop, VERDANT_FROGLIGHT, itemTwentyEightName, 2);
            addTokenShopItem(31, tokenShop, PEARLESCENT_FROGLIGHT, itemTwentyNineName, 2);
            addTokenShopItem(32, tokenShop, SLIME_BLOCK, itemThirtyName, 1);
            addTokenShopItem(33, tokenShop, HONEY_BLOCK, itemThirtyOneName, 1);

        } else if (Main.serverIDName.equalsIgnoreCase("sky")) {

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
        } else if (Main.serverIDName.equalsIgnoreCase("factions")) {
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
        } else if (Main.serverIDName.equalsIgnoreCase("factions")) {
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
                            "buster give " + player.getName() + " large_buster 1");
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
                            "buster give " + player.getName() + " regular_buster 1");
                    return;
                default:
                    return;
            }
        } else if (Main.serverIDName.equalsIgnoreCase("sky")) {

        }
    }
}
