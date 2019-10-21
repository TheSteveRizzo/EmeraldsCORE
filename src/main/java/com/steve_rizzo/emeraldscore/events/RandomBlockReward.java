package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class RandomBlockReward implements Listener {

    private static Permission permission = Main.perms;
    private final Main serverEssentials;
    private String prefix = Main.prefix;

    public RandomBlockReward(Main serverEssentials) {
        this.serverEssentials = serverEssentials;
    }

    /*

    EMERALD EQUIVALENCY TO USD: 1:$0.0100
    DIAMOND EQUIVALENCY TO USD: 1:$0.0010
    EMERALDSCASH EQUIVALENCY (1 is worth ~$0.0001, or roughly 1/100 emerald)

        server icon and desc,
        apply process, votifier, achievements,
        store packages, litebans web interface
     */

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getBlock().getType().equals(Material.STONE)) {

            Player p = e.getPlayer();

            if (!permission.getPrimaryGroup(p).equalsIgnoreCase("guest")) {

                String playerGroup = permission.getPrimaryGroup(p);

                // Get value for group of user for DIAMONDS
                if (new Random().nextDouble() <= getChanceByRankDiamonds(playerGroup)) {

                    Block broken = e.getBlock();

                    e.setCancelled(true);
                    broken.setType(Material.DIAMOND_BLOCK);

                    for (Player all : serverEssentials.getServer().getOnlinePlayers()) {
                        all.playSound(all.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.5F);
                    }

                    serverEssentials.getServer().broadcastMessage(prefix
                            + ChatColor.GRAY + "CONGRATS: " + ChatColor.GRAY + p.getName() + ChatColor.GRAY
                            + " has just found a random lucky " + ChatColor.AQUA + "DIAMOND BLOCK" + ChatColor.GRAY + "!");

                    return;

                }
            }

            if (!permission.getPrimaryGroup(p).equalsIgnoreCase("guest")) {

                String playerGroup = permission.getPrimaryGroup(p);

                // Get value for group of user for EMERALDS
                if (new Random().nextDouble() <= getChanceByRankEmeralds(playerGroup)) {

                    Block broken = e.getBlock();

                    e.setCancelled(true);
                    broken.setType(Material.EMERALD_BLOCK);

                    for (Player all : serverEssentials.getServer().getOnlinePlayers()) {
                        all.playSound(all.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.5F);
                    }

                    serverEssentials.getServer().broadcastMessage(prefix
                            + ChatColor.GRAY + "CONGRATS: " + ChatColor.GRAY + p.getName() + ChatColor.GRAY
                            + " has just found a random lucky " + ChatColor.GREEN + "EMERALD BLOCK" + ChatColor.GRAY + "!");


                    return;

                }
            }
        }
    }

    private double getChanceByRankEmeralds(String groupName) {

        /*
        Ranks chances:
        OWNER, ADMIN, MOD, HELPER, YOUTUBE, ELITE
            EMERALD: 1/1250
        DONOR4:
            EMERALD: 1/1750
        DONOR3:
            EMERALD: 1/2000
        DONOR2:
            EMERALD: 1/2500
        DONOR1:
            EMERALD: 1/3000
        MEMBER:
            EMERALD: 1/5000
        GUEST:
            EMERALD: N/A
        */

        if (groupName.equalsIgnoreCase("owner") || (groupName.equalsIgnoreCase("admin")) ||
                (groupName.equalsIgnoreCase("mod")) || (groupName.equalsIgnoreCase("helper")) ||
                (groupName.equalsIgnoreCase("youtuber")) || (groupName.equalsIgnoreCase("elite"))) {

            return (double) (1 / 1250);

        } else if (groupName.equalsIgnoreCase("donor4")) {

            return (double) (1 / 1750);

        } else if (groupName.equalsIgnoreCase("donor3")) {

            return (double) (1 / 2000);

        } else if (groupName.equalsIgnoreCase("donor2")) {

            return (double) (1 / 2500);

        } else if (groupName.equalsIgnoreCase("donor1")) {

            return (double) (1 / 3000);

        } else if (groupName.equalsIgnoreCase("member")) {

            return (double) (1 / 5000);

        }

        return 0.0;

    }

    private double getChanceByRankDiamonds(String groupName) {

        /*
        Ranks chances:
        OWNER, ADMIN, MOD, HELPER, YOUTUBE, ELITE
            DIAMOND: 1/750
        DONOR4:
            DIAMOND: 1/1000
        DONOR3:
            DIAMOND: 1/1250
        DONOR2:
            DIAMOND: 1/1500
        DONOR1:
            DIAMOND: 1/2000
        MEMBER:
            DIAMOND: 1/4000
        GUEST:
            DIAMOND: N/A
        */

        if (groupName.equalsIgnoreCase("owner") || (groupName.equalsIgnoreCase("admin")) ||
                (groupName.equalsIgnoreCase("mod")) || (groupName.equalsIgnoreCase("helper")) ||
                (groupName.equalsIgnoreCase("youtuber")) || (groupName.equalsIgnoreCase("elite"))) {

            return (double) (1 / 750);

        } else if (groupName.equalsIgnoreCase("donor4")) {

            return (double) (1 / 1000);

        } else if (groupName.equalsIgnoreCase("donor3")) {

            return (double) (1 / 1250);

        } else if (groupName.equalsIgnoreCase("donor2")) {

            return (double) (1 / 1500);

        } else if (groupName.equalsIgnoreCase("donor1")) {

            return (double) (1 / 2000);

        } else if (groupName.equalsIgnoreCase("member")) {

            return (double) (1 / 4000);

        }

        return 0.0;

    }
}