package com.steve_rizzo.emeraldscore.emeraldsgames.events;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OpenGamesGUI implements InventoryHolder, Listener {

    private final Inventory inv;

    ItemStack compass = GamesAPI.createGuiItem(Material.COMPASS, "§aEmeraldsGames Navigator",
            "§bUse this to access §cexclusive §bEmeraldsMC Games!");

    public OpenGamesGUI() {
        inv = Bukkit.createInventory(this, 9, ChatColor.BLACK + "EmeraldsGames Navigator");
        initializeItems();
    }

    public Inventory getInventory() {
        return inv;
    }

    public void initializeItems() {
        inv.addItem(GamesAPI.createGuiItem(Material.DIAMOND_SWORD, "§bMob Arena",
                "§aClick this to access the:",
                "§cMOB ARENA"));
        inv.addItem(GamesAPI.createGuiItem(Material.IRON_HELMET, "§bComing Soon",
                "§aThis EmeraldsMC Exclusive is",
                "§cCOMING SOON!"));
    }


    public void openInventory(Player p) {
        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() != inv) {
            return;
        }

        if (e.getClick().equals(ClickType.NUMBER_KEY)) {
            e.setCancelled(true);
        }

        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // Using slots click is a best option for your inventory click's
        if (e.getRawSlot() == 0) {
            p.sendMessage(Main.prefix + ChatColor.GOLD + "Sending you to... MOB ARENA!");
            p.performCommand("ma join");
            p.sendMessage(Main.prefix + ChatColor.GRAY + "You've joined the " + ChatColor.AQUA + "MOB ARENA"
                    + ChatColor.GRAY + "! Type " + ChatColor.AQUA + "/ma leave " + ChatColor.GRAY + "to leave!");

            p.closeInventory();
            return;
        }
        if (e.getRawSlot() == 1) {
            p.sendMessage(Main.prefix + ChatColor.GOLD + "This feature is COMING SOON!");
            p.closeInventory();
            return;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || (e.getAction().equals(Action.RIGHT_CLICK_AIR)
                || (e.getAction().equals(Action.RIGHT_CLICK_AIR) || (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))))) {
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
                ItemMeta im = item.getItemMeta();

                if (im.hasDisplayName()) {

                    String displayName = ChatColor.stripColor(im.getDisplayName());

                    if (displayName.toLowerCase().contains("emeraldsgames navigator")) {

                        e.getPlayer().openInventory(inv);
                    }
                }
            }
        }
    }
}
