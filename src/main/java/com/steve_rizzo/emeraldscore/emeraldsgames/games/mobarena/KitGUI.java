package com.steve_rizzo.emeraldscore.emeraldsgames.games.mobarena;

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

public class KitGUI implements InventoryHolder, Listener {

    private final Inventory kitInv;

    ItemStack selector = GamesAPI.createGuiItem(Material.IRON_BLOCK, "§aEmeraldsGames MobArena Kit Selector",
            "§bUse this to access §cexclusive §bEmeraldsMC Mob Arena Kits!");

    public KitGUI() {
        kitInv = Bukkit.createInventory(this, 9, ChatColor.BLACK + "EmeraldsGames - Mob Arena Kits");
        initializeItems();
    }

    public Inventory getInventory() {
        return kitInv;
    }

    /**
     * KITS
     * <p>
     * Knight:
     * items: diamond_sword, potion:instant_heal:3, golden_apple:3
     * armor: iron_helmet, iron_chestplate, iron_leggings, iron_boots
     * Tank:
     * items: iron_sword, potion:instant_heal:2
     * armor: diamond_helmet, diamond_chestplate, diamond_leggings, diamond_boots
     * effects: slow, health_boost
     * Archer:
     * items: stone_sword, bow, arrow:256, potion:instant_heal:3, bone
     * armor: gold_helmet, gold_chestplate, gold_leggings, gold_boots
     * effects: speed
     * Chemist:
     * items: stone_sword, splash_potion:instant_damage:30, splash_potion:poison:8, splash_potion:instant_heal:20,
     * potion:instant_heal:3
     * armor: chainmail_helmet, chainmail_chestplate, chainmail_leggings, chainmail_boots
     * effects: speed:1
     * Slayer:
     * items: diamond_sword sharpness:1, potion:instant_heal:3, golden_apple:3
     * armor: diamond_helmet, diamond_chestplate, diamond_leggings, diamond_boots
     */

    public void initializeItems() {
        //0
        kitInv.addItem(GamesAPI.createGuiItem(Material.DIAMOND_SWORD, "§bKNIGHT Kit",
                "§aClick this to choose the:",
                "§cKNIGHT KIT",
                "",
                "§dINCLUDES:",
                "§7⋆DIAMOND SWORD",
                "§7⋆IRON ARMOR",
                "§7⋆3 GOLDEN APPLES",
                "§7⋆3 INSTANT HEAL POTIONS"));

        //1
        kitInv.addItem(GamesAPI.createGuiItem(Material.IRON_SWORD, "§bTANK Kit",
                "§aClick this to choose the:",
                "§cTANK KIT",
                "",
                "§dINCLUDES:",
                "§7⋆IRON SWORD",
                "§7⋆DIAMOND ARMOR",
                "§7⋆2 INSTANT HEAL POTIONS",
                "§7⋆SLOW EFFECT",
                "§7⋆HEALTH BOOST EFFECT"));

        //2
        kitInv.addItem(GamesAPI.createGuiItem(Material.BOW, "§bARCHER Kit",
                "§aClick this to choose the:",
                "§cARCHER KIT",
                "",
                "§dINCLUDES:",
                "§7⋆STONE SWORD",
                "§7⋆GOLD ARMOR",
                "§7⋆BOW",
                "§7⋆256 ARROWS",
                "§7⋆3 INSTANT HEAL POTIONS",
                "§7⋆1 BONE (FOR WOLF TAMING)",
                "§7⋆SPEED EFFECT"));

        //3
        kitInv.addItem(GamesAPI.createGuiItem(Material.POTION, "§bCHEMIST Kit",
                "§aClick this to choose the:",
                "§cCHEMIST KIT",
                "",
                "§dINCLUDES:",
                "§7⋆STONE SWORD",
                "§7⋆CHAINMAIL ARMOR",
                "§7⋆30 INSTANT DAMAGE SPLASH POTIONS",
                "§7⋆8 POISON SPLASH POTIONS",
                "§7⋆20 INSTANT HEAL SPLASH POTIONS",
                "§7⋆3 INSTANT HEAL POTIONS",
                "§7⋆SPEED EFFECT"));

        //4
        kitInv.addItem(GamesAPI.createGuiItem(Material.EMERALD_BLOCK, "§bSLAYER Kit",
                "§aClick this to choose the:",
                "§cSLAYER KIT (DONOR ONLY)",
                "",
                "§dINCLUDES:",
                "§7⋆DIAMOND SWORD, SHARPNESS 1",
                "§7⋆DIAMOND ARMOR",
                "§7⋆3 GOLDEN APPLES",
                "§7⋆3 INSTANT HEAL POTIONS",
                "§7⋆SPEED EFFECT"));

    }


    public void openInventory(Player p) {
        p.openInventory(kitInv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() != kitInv) {
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
            p.closeInventory();
            p.sendMessage(Main.prefix + ChatColor.GOLD + "You chose... " + ChatColor.AQUA + "KNIGHT KIT" + ChatColor.GOLD + "!");
            p.performCommand("ma class Knight");
            return;
        }
        if (e.getRawSlot() == 1) {
            p.closeInventory();
            p.sendMessage(Main.prefix + ChatColor.GOLD + "You chose... " + ChatColor.AQUA + "TANK KIT" + ChatColor.GOLD + "!");
            p.performCommand("ma class Tank");
            return;
        }
        if (e.getRawSlot() == 2) {
            p.closeInventory();
            p.sendMessage(Main.prefix + ChatColor.GOLD + "You chose... " + ChatColor.AQUA + "ARCHER KIT" + ChatColor.GOLD + "!");
            p.performCommand("ma class Archer");
            return;
        }
        if (e.getRawSlot() == 3) {
            p.closeInventory();
            p.sendMessage(Main.prefix + ChatColor.GOLD + "You chose... " + ChatColor.AQUA + "CHEMIST KIT" + ChatColor.GOLD + "!");
            p.performCommand("ma class Chemist");
            return;
        }
        if (e.getRawSlot() == 4) {
            if (p.hasPermission("ma.class.slayer")) {
                p.closeInventory();
                p.sendMessage(Main.prefix + ChatColor.GOLD + "You chose... " + ChatColor.AQUA + "SLAYER KIT" + ChatColor.GOLD + "!");
                p.performCommand("ma class Slayer");
            } else {
                p.closeInventory();
                p.sendMessage(Main.prefix + ChatColor.RED + "You cannot select this kit! In order to gain access, " +
                        ChatColor.AQUA + "purchase a donor rank" + ChatColor.RED + " at " +
                        ChatColor.AQUA + "shop.emeraldsmc.com" + ChatColor.RED + "!");
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || (e.getAction().equals(Action.RIGHT_CLICK_AIR)
                || (e.getAction().equals(Action.RIGHT_CLICK_AIR) || (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))))) {
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_BLOCK) {
                ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
                ItemMeta im = item.getItemMeta();

                if (im.hasDisplayName()) {

                    String displayName = ChatColor.stripColor(im.getDisplayName());

                    if (displayName.toLowerCase().contains("emeraldsgames mobarena")) {

                        openInventory(e.getPlayer());

                    }
                }
            }
        }
    }
}
