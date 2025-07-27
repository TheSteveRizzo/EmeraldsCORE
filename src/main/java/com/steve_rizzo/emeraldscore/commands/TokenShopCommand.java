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
    String title = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "EmeraldsMC" + ChatColor.GRAY + ChatColor.BOLD + " - " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Token Shop";

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


            String itemTwoName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                            + ChatColor.GREEN + "SKELETON SPAWNER")
                    .replace("%COST%", "25 TOKENS"),
                    itemThreeName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "SPIDER SPAWNER")
                            .replace("%COST%", "25 TOKENS"),
                    itemFourName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "ZOMBIE SPAWNER")
                            .replace("%COST%", "25 TOKENS"),
                    itemFiveName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "CAVE SPIDER SPAWNER")
                            .replace("%COST%", "25 TOKENS"),
                    itemSixName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "BLAZE SPAWNER")
                            .replace("%COST%", "25 TOKENS"),

                    itemTwelveName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "ITEM VOUCHER")
                            .replace("%COST%", "3 TOKENS"),

                    itemThirteenName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "NETHER VOUCHER")
                            .replace("%COST%", "3 TOKENS"),

                    itemFourteenName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "LOTTERY VOUCHER")
                            .replace("%COST%", "3 TOKENS"),


                    itemTwentyOneName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "MINING POUCH")
                            .replace("%COST%", "30 TOKENS"),

                    itemTwentyTwoName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "FOOD PACKAGE")
                            .replace("%COST%", "2 TOKENS"),


                    itemTwentyThreeName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "MOB TRAP")
                            .replace("%COST%", "20 TOKENS"),

                    itemTwentySevenName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "Emerald Crate Key")
                            .replace("%COST%", "15 Tokens"),

                    itemTwentyEightName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "Tool Crate Key")
                            .replace("%COST%", "20 Tokens"),

                    itemTwentyNineName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "PVP Crate Key")
                            .replace("%COST%", "25 Tokens"),

                    itemThirtyName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "Decor Crate Key")
                            .replace("%COST%", "25 Tokens"),

                    itemThirtyOneName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                                    + ChatColor.GREEN + "Money Crate Key")
                            .replace("%COST%", "25 Tokens");

            addTokenShopItem(11, tokenShop, SPAWNER, itemTwoName, 25);
            addTokenShopItem(12, tokenShop, SPAWNER, itemThreeName, 25);
            addTokenShopItem(13, tokenShop, SPAWNER, itemFourName, 25);
            addTokenShopItem(14, tokenShop, SPAWNER, itemFiveName, 25);
            addTokenShopItem(15, tokenShop, SPAWNER, itemSixName, 25);

            addTokenShopItem(21, tokenShop, BEACON, itemTwelveName, 3);
            addTokenShopItem(22, tokenShop, NETHERITE_INGOT, itemThirteenName, 3);
            addTokenShopItem(23, tokenShop, PAPER, itemFourteenName, 3);

            addTokenShopItem(30, tokenShop, pouch.getType(), itemTwentyOneName, 30);
            addTokenShopItem(31, tokenShop, COOKED_BEEF, itemTwentyTwoName, 2);
            addTokenShopItem(32, tokenShop, trap.getType(), itemTwentyThreeName, 20);

            addTokenShopItem(38, tokenShop, EMERALD_BLOCK, itemTwentySevenName, 15);
            addTokenShopItem(39, tokenShop, NETHERITE_BLOCK, itemTwentyEightName, 20);
            addTokenShopItem(40, tokenShop, VERDANT_FROGLIGHT, itemTwentyNineName, 25);
            addTokenShopItem(41, tokenShop, PEARLESCENT_FROGLIGHT, itemThirtyName, 25);
            addTokenShopItem(42, tokenShop, HONEY_BLOCK, itemThirtyOneName, 25);

        } else if (Main.serverIDName.equalsIgnoreCase("bed")) {

            String warCrateKeyName = itemFormat.replace("%ENTER%", ChatColor.AQUA + "1x "
                    + ChatColor.AQUA + "WAR CRATE KEY").replace("%COST%", "15 TOKENS");

            addTokenShopItem(2, tokenShop, TRIPWIRE_HOOK, warCrateKeyName, 15);

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
                case ENDER_CHEST:
                    return 30;
                case SPAWNER, VERDANT_FROGLIGHT, PEARLESCENT_FROGLIGHT, HONEY_BLOCK:
                    return 25;
                case BARREL, NETHERITE_BLOCK:
                    return 20;
                case BEACON, NETHERITE_INGOT, PAPER:
                    return 3;
                case EMERALD_BLOCK:
                    return 15;
                case COOKED_BEEF:
                    return 2;
                default:
                    return 0;
            }
        } else if (Main.serverIDName.equalsIgnoreCase("bed")) {
            switch (item.getType()) {
                case TRIPWIRE_HOOK:
                    return 15;
                default:
                    return 0;
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
                                    + ChatColor.YELLOW + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x SKELETON SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                            );

                            return;

                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("SPIDER SPAWNER") &&
                                (!purchasedItem.getItemMeta().getDisplayName().contains("CAVE SPIDER"))) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " SPIDER 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.YELLOW + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x SPIDER SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                            );

                            return;

                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("ZOMBIE SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " ZOMBIE 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.YELLOW + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x ZOMBIE SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                            );

                            return;


                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("CAVE SPIDER SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " CAVE_SPIDER 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.YELLOW + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x CAVE SPIDER SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                            );

                            return;


                        } else if (purchasedItem.getItemMeta().getDisplayName().contains("BLAZE SPAWNER")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "spawners give " + player.getName() + " BLAZE 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.YELLOW + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x BLAZE SPAWNER"
                                    + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                            );

                            return;


                        }
                    }

                    return;

                // Give Mining Pouch
                case ENDER_CHEST:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x MINING POUCH"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    Pouch pouch = new Pouch(); // Create an empty pouch
                    player.getInventory().addItem(pouch.getPouch()); // The pouch already has the correct metadata

                    return;

                // Give NEKO Trap
                case BARREL:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x MOB TRAP"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "nekotraps give " + player.getName());

                    return;

                // Give Food Package
                case COOKED_BEEF:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x FOOD PACKAGE"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "vouchers give " + player.getName() + " FoodPackage 1");

                    return;

                // Give Item Voucher
                case BEACON:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x ITEM VOUCHER"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "vouchers give " + player.getName() + " ItemVoucher 1");

                    return;

                // Give Nether Voucher
                case NETHERITE_INGOT:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x NETHER VOUCHER"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "vouchers give " + player.getName() + " NetherVoucher 1");

                    return;

                // Give Lottery Voucher
                case PAPER:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x LOTTERY VOUCHER"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "vouchers give " + player.getName() + " LotteryVoucher 1");

                    return;

                // Give Emerald Crate key
                case EMERALD_BLOCK:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x Emerald Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " emerald 1");

                    return;

                // Give Tools Crate key
                case NETHERITE_BLOCK:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x Tool Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " tool 1");
                    return;

                // Give PVP Crate key
                case VERDANT_FROGLIGHT:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x PVP Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " pvp 1");
                    return;
                    
                // Give Decor Crate key
                case PEARLESCENT_FROGLIGHT:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x Decor Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " decor 1");
                    return;

                // Give Money Crate key
                case HONEY_BLOCK:
                    getServer().broadcastMessage(Main.prefix
                            + ServerJoinPlayer.getPlayerPrefixAndName(player)
                            + ChatColor.YELLOW + " just purchased "
                            + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x Money Crate key"
                            + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                            + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                    );

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "ecrates key give " + player.getName() + " money 1");
                    return;
                    
                default:
                    return;
            }
        } else if (Main.serverIDName.equalsIgnoreCase("bed")) {
            switch (purchasedItem.getType()) {

                case TRIPWIRE_HOOK:
                    if (purchasedItem.getItemMeta().hasDisplayName()) {

                        if (purchasedItem.getItemMeta().getDisplayName().contains("WAR CRATE KEY")) {

                            getServer().dispatchCommand(getServer().getConsoleSender(),
                                    "ecrates key give " + player.getName() + " warcrate 1");

                            getServer().broadcastMessage(Main.prefix
                                    + ServerJoinPlayer.getPlayerPrefixAndName(player)
                                    + ChatColor.YELLOW + " just purchased "
                                    + ChatColor.GRAY + "(" + ChatColor.AQUA + "1x " + ChatColor.AQUA + "WARCRATE KEY"
                                    + ChatColor.GRAY + ")" + ChatColor.YELLOW + " from the "
                                    + ChatColor.GREEN + "Token Shop" + ChatColor.YELLOW + "!"
                            );

                            return;

                        }
                    }
                default:
                    return;
            }
        }
    }
}
