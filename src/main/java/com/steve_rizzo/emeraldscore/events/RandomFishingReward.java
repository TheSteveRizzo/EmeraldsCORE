package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.steve_rizzo.emeraldscore.events.ServerJoinPlayer.getPlayerPrefixAndName;
import static org.bukkit.Bukkit.getServer;

public class RandomFishingReward implements Listener {

    private static Permission permission = Main.perms;
    private final Main serverEssentials;
    private String prefix = Main.prefix;

    public RandomFishingReward(Main serverEssentials) {
        this.serverEssentials = serverEssentials;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player p = event.getPlayer();
        if (!permission.getPrimaryGroup(p).equalsIgnoreCase("guest")) {

            String playerGroup = permission.getPrimaryGroup(p);

            // Get value for group of user for EMERALDS
            if (new Random().nextDouble() <= getChanceByRank(playerGroup)) {

                for (Player all : serverEssentials.getServer().getOnlinePlayers()) {
                    all.playSound(all.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.5F);
                }

                serverEssentials.getServer().broadcastMessage(prefix
                        + ChatColor.GRAY + "CONGRATS: " + getPlayerPrefixAndName(event.getPlayer()) + ChatColor.GRAY
                        + " just found " + ChatColor.GREEN + "RARE FISHING LOOT "
                        + ChatColor.LIGHT_PURPLE + "(2x XP)" + ChatColor.GRAY + " while fishing!");

                Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                        commandToRun(fishingRewards).replace("{user}", p.getName()));

                event.setExpToDrop(event.getExpToDrop()*2);

            }
        }
    }

    private List<String> fishingRewards = Arrays.asList(
            "givebal {user} 1000",                  // $1,000 Emeralds Cash
            "give {user} minecraft:beacon 1",       // 1 Beacon
            "give {user} minecraft:deepslate 64",       // 64 Deepslate
            "give {user} minecraft:cooked_beef 32",     // 32 Cooked Beef (Steak)
            "give {user} minecraft:gold_ingot 64",   // 64 Gold Ingot
            "give {user} minecraft:netherite_ingot 2",   // 2 Netherite Ingot
            "give {user} minecraft:iron_block 4",   // 4 Iron Blocks
            "give {user} minecraft:chorus_flower 1",   // 1 Chorus Flower
            "give {user} minecraft:amethyst_cluster 2",   // 2 Amethyst Clusters
            "give {user} minecraft:music_disc_mall 1",   // 1 `MALL` Music Disc
            "give {user} minecraft:netherite_ingot 2"   // 2 Netherite Ingot
    );

    private String commandToRun(List givenList) {
        Random rand = new Random();
        return (String) givenList.get(rand.nextInt(givenList.size()));
    }

    private double getChanceByRank(String groupName) {

        /*
        Ranks chances:
        OWNER, ADMIN, MOD, HELPER, YOUTUBE, ELITE
            FISH LOOT: 1/250
        DONOR4:
            FISH LOOT: 1/500
        DONOR3:
            FISH LOOT: 1/750
        DONOR2:
            FISH LOOT: 1/1000
        DONOR1:
            FISH LOOT: 1/1250
        MEMBER:
            FISH LOOT: 1/1500
        GUEST:
            FISH LOOT: N/A
        */


        if (groupName.equalsIgnoreCase("owner") || (groupName.equalsIgnoreCase("admin")) ||
                (groupName.equalsIgnoreCase("mod")) || (groupName.equalsIgnoreCase("helper")) ||
                (groupName.equalsIgnoreCase("youtuber")) || (groupName.equalsIgnoreCase("elite"))) {

            return (double) (1.0 / 250.0);

        } else if (groupName.equalsIgnoreCase("donor4")) {

            return (double) (1.0 / 500.0);

        } else if (groupName.equalsIgnoreCase("donor3")) {

            return (double) (1.0 / 750.0);

        } else if (groupName.equalsIgnoreCase("donor2")) {

            return (double) (1.0 / 1000.0);

        } else if (groupName.equalsIgnoreCase("donor1")) {

            return (double) (1.0 / 1250.0);

        } else if (groupName.equalsIgnoreCase("member")) {

            return (double) (1.0 / 1500.0);

        }

        return 0.0;

    }
}
