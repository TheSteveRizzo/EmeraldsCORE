package com.steve_rizzo.emeraldscore.features.miningpouch;


import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Pouch {
    private static final String TITLE = ChatColor.translateAlternateColorCodes('&', "&b&lMining Pouch");

    private static final String[] LORE = new String[] {

            ChatColor.DARK_GRAY + "Coal: 0",

            ChatColor.YELLOW + "Raw Copper: 0",

            ChatColor.WHITE + "Raw Iron: 0",

            ChatColor.GOLD + "Raw Gold: 0",

            ChatColor.BLUE + "Lapis Lazuli: 0",

            ChatColor.RED + "Redstone: 0",

            ChatColor.AQUA + "Diamond: 0",

            ChatColor.GREEN + "Emerald: 0",

            ChatColor.GRAY + "Cobblestone: 0",

            ChatColor.GRAY + "Cobbled Deepslate: 0",

            ChatColor.DARK_GRAY + "Dirt: 0",

            ChatColor.DARK_GRAY + "Sand: 0",

            ChatColor.DARK_GRAY + "Gravel: 0",

            ChatColor.DARK_GRAY + "Tuff: 0",

            ChatColor.WHITE + "Quartz: 0",

            ChatColor.DARK_RED + "Netherrack: 0",

            ChatColor.LIGHT_PURPLE + "Ancient Debris: 0",

    };

    private static final Material[] POUCH_ITEMS = new Material[] {

            Material.COAL,

            Material.RAW_COPPER,

            Material.RAW_IRON,

            Material.RAW_GOLD,

            Material.LAPIS_LAZULI,

            Material.REDSTONE,

            Material.DIAMOND,

            Material.EMERALD,

            Material.COBBLESTONE,

            Material.COBBLED_DEEPSLATE,

            Material.DIRT,

            Material.SAND,

            Material.GRAVEL,

            Material.TUFF,

            Material.QUARTZ,

            Material.NETHERRACK,

            Material.ANCIENT_DEBRIS


    };

    private ItemStack item;

    public static boolean isPouchItem(Material material) {
        byte b;
        int i;
        Material[] arrayOfMaterial;
        for (i = (arrayOfMaterial = POUCH_ITEMS).length, b = 0; b < i; ) {
            Material currentItem = arrayOfMaterial[b];
            if (currentItem == material)
                return true;
            b++;
        }
        return false;
    }

    public static int targetPouchSlot(Inventory inventory) {
        int targetSlot = -1;
        for (int x = 0; x < inventory.getSize(); x++) {
            ItemStack pouch = (new Pouch(null)).getPouch();
            ItemStack targetItem = inventory.getItem(x);
            if (targetItem != null && targetItem.getType() == pouch.getType() && targetItem.getItemMeta().getDisplayName().equals(pouch.getItemMeta().getDisplayName()) && targetItem.getAmount() == 1)
                targetSlot = x;
        }
        return targetSlot;
    }

    public static boolean hasPouchOpened(Player player) {
        return player.getOpenInventory().getTitle().equals((new Pouch(null)).getPouch().getItemMeta().getDisplayName());
    }

    public static void addToPouch(Pouch pouch, Item item, Player player) {
        ItemStack itemStack = item.getItemStack();
        pouch.setQuantity(itemStack.getType(), pouch.getQuantity(itemStack.getType()) + itemStack.getAmount());
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setItem(targetPouchSlot((Inventory)playerInventory), pouch.getPouch());
        item.remove();
    }

    public static Inventory getInventory(Pouch pouch) {
        Inventory inventory = Bukkit.createInventory(null, 18, TITLE);
        for (int x = 0; x < POUCH_ITEMS.length; x++) {
            ItemStack item = new ItemStack(POUCH_ITEMS[x]);
            ItemMeta meta = item.getItemMeta();
            String loreLine = pouch.getPouch().getItemMeta().getLore().get(materialToLine(POUCH_ITEMS[x]));
            int position = 0;
            for (; !Character.isDigit(ChatColor.stripColor(loreLine).charAt(position)); position++);
            meta.setDisplayName(String.valueOf(LORE[x].substring(0, LORE[x].length() - 1)) + loreLine.substring(position + 2));
            item.setItemMeta(meta);
            inventory.addItem(new ItemStack[] { item });
        }
        return inventory;
    }

    public Pouch(ItemStack item) {
        if (item != null) {
            this.item = item;
        } else {
            this.item = new ItemStack(Material.ENDER_CHEST);
            ItemMeta meta = this.item.getItemMeta();
            meta.setDisplayName(TITLE);
            List<String> lore = new ArrayList<>();
            byte b;
            int i;
            String[] arrayOfString;
            for (i = (arrayOfString = LORE).length, b = 0; b < i; ) {
                String line = arrayOfString[b];
                lore.add(line);
                b++;
            }
            meta.setLore(lore);
            this.item.setItemMeta(meta);
        }
    }

    public Pouch() {
        this.item = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(TITLE);
        List<String> lore = new ArrayList<>();
        for (String line : LORE) {
            lore.add(line);
        }
        meta.setLore(lore);
        this.item.setItemMeta(meta);
    }


    public ItemStack getPouch() {
        return this.item;
    }

    public String getTitle() {
        return TITLE;
    }

    public String[] getLore() {
        return LORE;
    }

    public int getQuantity(Material material) {
        String loreLine = this.item.getItemMeta().getLore().get(materialToLine(material));
        int position = 0;
        for (; !Character.isDigit(ChatColor.stripColor(loreLine).charAt(position)); position++);
        return Integer.parseInt(loreLine.substring(position + 2));
    }

    public void setQuantity(Material material, int quantity) {
        ItemMeta meta = this.item.getItemMeta();
        List<String> lore = new ArrayList<>();
        for (int x = 0; x < meta.getLore().size(); ) {
            lore.add(meta.getLore().get(x));
            x++;
        }
        int position = 0;
        for (; !Character.isDigit(ChatColor.stripColor(lore.get(materialToLine(material))).charAt(position)); position++);
        lore.set(materialToLine(material), String.valueOf(((String)lore.get(materialToLine(material))).substring(0, position + 2)) + quantity);
        meta.setLore(lore);
        this.item.setItemMeta(meta);
    }

    private static int materialToLine(Material material) {
        switch (material) {
            case COAL:
                return 0;
            case RAW_COPPER:
                return 1;
            case RAW_IRON:
                return 2;
            case RAW_GOLD:
                return 3;
            case LAPIS_LAZULI:
                return 4;
            case REDSTONE:
                return 5;
            case DIAMOND:
                return 6;
            case EMERALD:
                return 7;
            case COBBLESTONE:
                return 8;
            case COBBLED_DEEPSLATE:
                return 9;
            case DIRT:
                return 10;
            case SAND:
                return 11;
            case GRAVEL:
                return 12;
            case TUFF:
                return 13;
            case QUARTZ:
                return 14;
            case NETHERRACK:
                return 15;
            case ANCIENT_DEBRIS:
                return 16;

        }
        return -1;
    }
}