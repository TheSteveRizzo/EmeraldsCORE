package com.steve_rizzo.emeraldscore.emeraldsgames.api;

import com.steve_rizzo.emeraldscore.Main;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GamesAPI {

    public static Permission perms = Main.perms;
    private Main core = Main.core;

    public static ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metalore = new ArrayList<String>();
        for (String lorecomments : lore) {
            metalore.add(lorecomments);
        }
        meta.setLore(metalore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isDonorOrHigher(Player player) {
        if (perms.getPrimaryGroup(player).equalsIgnoreCase("owner") ||
                (perms.getPrimaryGroup(player).equalsIgnoreCase("admin") ||
                        (perms.getPrimaryGroup(player).equalsIgnoreCase("mod") ||
                                (perms.getPrimaryGroup(player).equalsIgnoreCase("helper") ||
                                        (perms.getPrimaryGroup(player).equalsIgnoreCase("youtube") ||
                                                (perms.getPrimaryGroup(player).equalsIgnoreCase("elite") ||
                                                        (perms.getPrimaryGroup(player).equalsIgnoreCase("donor4") ||
                                                                (perms.getPrimaryGroup(player).equalsIgnoreCase("donor3") ||
                                                                        (perms.getPrimaryGroup(player).equalsIgnoreCase("donor2") ||
                                                                                (perms.getPrimaryGroup(player).equalsIgnoreCase("donor1"))))))))))) {
            return true;
        }

        return false;
    }
}
