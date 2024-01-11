package com.steve_rizzo.emeraldscore.features;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Bukkit.getServer;

// Class for "Special Gift" promotions, which provides users with a gift for each day specified!
public class SpecialGift implements Listener {

    FileConfiguration config = Main.core.cooldownConfig;


    ////// INTENTIONAL SEPARATION BETWEEN NON-DONOR & DONOR GIFTS

    // GIFT LIST FOR (NON-DONOR USERS), FROM JAN 01 TO JAN 07
    private List<String> week1GiftList = Arrays.asList(
            "givebal {user} 500",                  // ND Cash amount
            "give {user} minecraft:emerald 1",       // ND Emerald Amount
            "give {user} minecraft:diamond 4",       // ND Emerald Amount
            "give {user} minecraft:cooked_beef 16",     // Replaced with cooked_beef 
            "give {user} minecraft:pumpkin_pie 8",   // Replaced with pumpkin pie 
            "give {user} minecraft:cake 8",   // Replaced with cake
            "give {user} minecraft:snowball 64",   // Replaced with snowball
            "give {user} minecraft:dirt 64",  // Replaced with dirt
            "give {user} minecraft:deepslate 64"   // Replaced with deepslate
    );

    // GIFT LIST FOR (NON-DONOR USERS), FROM JAN 07 TO JAN 14
    private List<String> week2GiftList = Arrays.asList(
            "givebal {user} 750",                  // ND Cash amount
            "give {user} minecraft:emerald 4",       // ND Emerald Amount
            "give {user} minecraft:diamond 8",       // ND Emerald Amount
            "give {user} minecraft:cooked_beef 32",     // Replaced with cooked_beef 
            "give {user} minecraft:pumpkin_pie 16",   // Replaced with pumpkin pie 
            "give {user} minecraft:cake 8",   // Replaced with cake
            "give {user} minecraft:snowball 128",   // Replaced with snowball
            "give {user} minecraft:dirt 128",   // Replaced with dirt
            "give {user} minecraft:deepslate 64"   // Replaced with deepslate
    );

    // GIFT LIST FOR (NON-DONOR USERS), FROM JAN OCT 21+
    private List<String> week3GiftList = Arrays.asList(
            "givebal {user} 1000",                  // ND Cash amount
            "give {user} minecraft:emerald 8",       // ND Emerald Amount
            "give {user} minecraft:diamond 12",       // ND Emerald Amount
            "give {user} minecraft:cooked_beef 48",     // Replaced with cooked_beef 
            "give {user} minecraft:pumpkin_pie 32",   // Replaced with pumpkin pie 
            "give {user} minecraft:cake 8",   // Replaced with cake
            "give {user} minecraft:snowball 128",   // Replaced with snowball
            "give {user} minecraft:dirt 128",   // Replaced with dirt
            "give {user} minecraft:deepslate 64"   // Replaced with deepslate
    );



    ////// INTENTIONAL SEPARATION BETWEEN NON-DONOR & DONOR GIFTS



    // GIFT LIST FOR (DONOR USERS), FROM JAN 01 TO JAN 07 (Same as before)
    private List<String> week1DonorGiftList = Arrays.asList(
            "givebal {user} 1500",                  // D Cash amount
            "give {user} minecraft:emerald 8",       // D Emerald Amount
            "give {user} minecraft:diamond 12",       // D Diamond Amount
            "give {user} minecraft:cooked_beef 48",     // Replaced with cooked_beef 
            "give {user} minecraft:pumpkin_pie 32",   // Replaced with pumpkin pie 
            "give {user} minecraft:netherite_ingot 1",   // Replaced with netherite ingot
            "give {user} minecraft:snowball 128",   // Replaced with snowball
            "give {user} minecraft:deepslate 128"   // Replaced with deepslate
    );

    // GIFT LIST FOR (DONOR USERS), FROM JAN 07 TO JAN 14 (Same as before)
    private List<String> week2DonorGiftList = Arrays.asList(
            "givebal {user} 2000",                  // D Cash amount
            "give {user} minecraft:emerald 12",       // D Emerald Amount
            "give {user} minecraft:diamond 16",       // D Diamond Amount
            "give {user} minecraft:cooked_beef 64",     // Replaced with cooked_beef 
            "give {user} minecraft:pumpkin_pie 64",   // Replaced with pumpkin pie 
            "give {user} minecraft:netherite_ingot 2",   // Replaced with netherite ingot
            "give {user} minecraft:snowball 128",   // Replaced with snowball
            "give {user} minecraft:deepslate 128"   // Replaced with deepslate

    );

    // GIFT LIST FOR (DONOR USERS), FROM JAN 21+ (Same as before)
    private List<String> week3DonorGiftList = Arrays.asList(
            "givebal {user} 2500",                  // D Cash amount
            "give {user} minecraft:emerald 16",       // D Emerald Amount
            "give {user} minecraft:diamond 32",       // D Diamond Amount
            "give {user} minecraft:cooked_beef 64",     // Replaced with cooked_beef 
            "give {user} minecraft:pumpkin_pie 64",   // Replaced with pumpkin pie 
            "give {user} minecraft:netherite_ingot 3",   // Replaced with netherite ingot
            "give {user} minecraft:snowball 256",   // Replaced with snowball
            "give {user} minecraft:deepslate 128"   // Replaced with deepslate

    );

    @EventHandler
    public void onMagicLordClick(PlayerInteractEntityEvent e) {

        Entity giftCreeper;
        Player p;

        if (e.getRightClicked().getName().equalsIgnoreCase("GIFT CREEPER")) {

            giftCreeper = e.getRightClicked();
            p = e.getPlayer();
            // Current date
            Date curDate = new Date();
            LocalDate localDate = curDate.toInstant().atZone(ZoneId.of("America/New_York")).toLocalDate();
            int year = localDate.getYear(), month = localDate.getMonthValue(), day = localDate.getDayOfMonth();

            if ((year == 2024) && (month == 1)) {

                if (day > 1 && day < 14) {

                    // Run the WEEK 1 commands (DONOR)
                    if (isDonor(p)) {

                        if (isInCooldown(p)) {

                            giveDonorGift(p, 1);
                            addToCooldown(p);
                        } else {

                            p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                                    ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");
                        }

                        // Run the WEEK 1 commands (NON-DONOR)
                    } else {

                        if (isInCooldown(p)) {
                            giveRegularGift(p, 1);
                            addToCooldown(p);
                        } else {
                            p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                                    ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");
                        }
                    }

                } else if (day >= 14 && day < 21) {

                    // Run the WEEK 2 commands (DONOR)
                    if (isDonor(p)) {

                        if (isInCooldown(p)) {

                            giveDonorGift(p, 2);
                            addToCooldown(p);
                        } else {

                            p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                                    ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");
                        }

                        // Run the WEEK 2 commands (NON-DONOR)
                    } else {

                        if (isInCooldown(p)) {
                            giveRegularGift(p, 2);
                            addToCooldown(p);
                        } else {
                            p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                                    ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");
                        }
                    }

                } else if (day >= 21) {

                    // Run the WEEK 3 commands (DONOR)
                    if (isDonor(p)) {

                        if (isInCooldown(p)) {
                            giveDonorGift(p, 3);
                            addToCooldown(p);
                        } else {
                            p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                                    ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");
                        }

                        // Run the WEEK 3 commands (NON-DONOR)
                    } else {

                        if (isInCooldown(p)) {
                            giveRegularGift(p, 3);
                            addToCooldown(p);
                        } else {
                            p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                                    ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");
                        }
                    }
                }
            }
        }
    }

    private void giveRegularGift(Player player, int weekNum) {

        if (weekNum == 1) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a REGULAR gift " +
                    "&efrom the &6&lGift Creeper&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week1GiftList).replace("{user}", player.getName()));

        } else if (weekNum == 2) {

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a REGULAR gift " +
                    "&efrom the &6&lGift Creeper&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week2GiftList).replace("{user}", player.getName()));

        } else if (weekNum == 3) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a REGULAR gift " +
                    "&efrom the &6&lGift Creeper&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week3GiftList).replace("{user}", player.getName()));
        }
    }

    private void giveDonorGift(Player player, int weekNum) {

        if (weekNum == 1) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom the &6&lGift Creeper&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week1DonorGiftList).replace("{user}", player.getName()));

        } else if (weekNum == 2) {

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom the &6&lGift Creeper&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week2DonorGiftList).replace("{user}", player.getName()));

        } else if (weekNum == 3) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom the &6&lGift Creeper&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week3DonorGiftList).replace("{user}", player.getName()));
        }
    }

    private void addToCooldown(Player player) {
        config.set(player.getUniqueId().toString(), System.currentTimeMillis());
    }

    private Long getCooldownTime(Player player) {
        return (Long) config.get(player.getUniqueId().toString());
    }

    // TODO
    private boolean isInCooldown(Player player) {

        Long time = getCooldownTime(player);

        if (time != null) {
            // Checks if one day has passed.
            if (System.currentTimeMillis() - time >= TimeUnit.DAYS.toMillis(1)) {
                return true;
            }
            return false;
        }
        return true;
    }

    private String commandToRun(List givenList) {
        Random rand = new Random();
        return (String) givenList.get(rand.nextInt(givenList.size()));
    }

    private boolean isDonor(Player player) {
        return GamesAPI.isDonorOrHigher(player);
    }

    private String getRemainingTime(Player player) {

        Long time = getCooldownTime(player);

        long timeLeft = (time + TimeUnit.DAYS.toMillis(1) - System.currentTimeMillis());

        long seconds = timeLeft / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        return hours % 24 + " hours " + minutes % 60 + " minutes " + seconds % 60 + " seconds";
    }
}