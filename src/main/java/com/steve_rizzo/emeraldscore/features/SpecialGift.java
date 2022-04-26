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

    // GIFT LIST FOR (NON-DONOR USERS), FROM APRIL 01 TO APR 07
    private List<String> week1GiftList = Arrays.asList("fe grant {user} 2000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:redstone 128",
            "give {user} minecraft:enchanted_golden_apple 8",
            "give {user} minecraft:diamond_sword 1",
            "give {user} polar_bear_spawn_egg 1");

    // GIFT LIST FOR (NON-DONOR USERS), FROM APRIL 07 TO APR 14
    private List<String> week2GiftList = Arrays.asList("fe grant {user} 2000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:redstone 128",
            "give {user} minecraft:enchanted_golden_apple 8",
            "give {user} minecraft:diamond_sword 1",
            "give {user} polar_bear_spawn_egg 1");

    // GIFT LIST FOR (NON-DONOR USERS), FROM APRIL 14 TO APR 21
    private List<String> week3GiftList = Arrays.asList("fe grant {user} 1000",
            "give {user} minecraft:emerald 4",
            "give {user} minecraft:diamond 8",
            "give {user} minecraft:coal 64",
            "give {user} minecraft:enchanted_golden_apple 4",
            "give {user} polar_bear_spawn_egg 1");

    // GIFT LIST FOR (NON-DONOR USERS), FROM APRIL 21 +
    private List<String> week4GiftList = Arrays.asList("fe grant {user} 2000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:redstone 128",
            "give {user} minecraft:enchanted_golden_apple 8",
            "give {user} minecraft:diamond_sword 1",
            "give {user} polar_bear_spawn_egg 1");


    ////// INTENTIONAL SEPARATION BETWEEN NON-DONOR & DONOR GIFTS


    // GIFT LIST FOR (DONOR USERS), FROM APRIL 01 TO APR 07
    private List<String> week1DonorGiftList = Arrays.asList("fe grant {user} 2000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:coal 128",
            "give {user} minecraft:enchanted_golden_apple 8",
            // SHARP 1 DIA SWORD
            "give {user} diamond_sword {display:{Name:\"[{\\\"text\\\":\\\"EmeraldsMC Holiday Gift\\\",\\\"color\\\":\\\"green\\\"}]\",Lore:[\"{\\\"text\\\":\\\"A special Christmas gift\\\",\\\"color\\\":\\\"aqua\\\"}\"]},Enchantments:[{id:sharpness,lvl:1}]} 1",
            "give {user} polar_bear_spawn_egg 2");

    // GIFT LIST FOR (DONOR USERS), FROM APRIL 07 TO APR 14
    private List<String> week2DonorGiftList = Arrays.asList("fe grant {user} 2000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:coal 128",
            "give {user} minecraft:enchanted_golden_apple 8",
            // SHARP 1 DIA SWORD
            "give {user} diamond_sword {display:{Name:\"[{\\\"text\\\":\\\"EmeraldsMC Holiday Gift\\\",\\\"color\\\":\\\"green\\\"}]\",Lore:[\"{\\\"text\\\":\\\"A special Christmas gift\\\",\\\"color\\\":\\\"aqua\\\"}\"]},Enchantments:[{id:sharpness,lvl:1}]} 1",
            "give {user} polar_bear_spawn_egg 2");

    // GIFT LIST FOR (DONOR USERS), FROM APRIL 14 TO APR 21
    private List<String> week3DonorGiftList = Arrays.asList("fe grant {user} 2000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:coal 128",
            "give {user} minecraft:enchanted_golden_apple 8",
            // SHARP 1 DIA SWORD
            "give {user} diamond_sword {display:{Name:\"[{\\\"text\\\":\\\"EmeraldsMC Holiday Gift\\\",\\\"color\\\":\\\"green\\\"}]\",Lore:[\"{\\\"text\\\":\\\"A special Christmas gift\\\",\\\"color\\\":\\\"aqua\\\"}\"]},Enchantments:[{id:sharpness,lvl:1}]} 1",
            "give {user} polar_bear_spawn_egg 2");


    // GIFT LIST FOR (DONOR USERS), FROM APRIL 21 +
    private List<String> week4DonorGiftList = Arrays.asList("fe grant {user} 5000",
            "give {user} minecraft:emerald 16",
            "give {user} minecraft:diamond 32",
            "give {user} minecraft:coal 256",
            "give {user} minecraft:enchanted_golden_apple 16",
            // SHARP 2 DIA SWORD
            "give {user} diamond_sword {display:{Name:\"[{\\\"text\\\":\\\"EmeraldsMC Holiday Gift\\\",\\\"color\\\":\\\"green\\\"}]\",Lore:[\"{\\\"text\\\":\\\"A special Christmas gift\\\",\\\"color\\\":\\\"aqua\\\"}\"]},Enchantments:[{id:sharpness,lvl:2}]} 1",
            "give {user} polar_bear_spawn_egg 1");

    ////// INTENTIONAL SEPARATION BETWEEN NON-DONOR & DONOR GIFTS

    @EventHandler
    public void onMagicLordClick(PlayerInteractEntityEvent e) {

        Entity magicLord;
        Player p;

        if (e.getRightClicked().getName().equalsIgnoreCase("Magic Lord")) {

            magicLord = e.getRightClicked();
            p = e.getPlayer();
            // Current date
            Date curDate = new Date();
            LocalDate localDate = curDate.toInstant().atZone(ZoneId.of("America/New_York")).toLocalDate();
            int year = localDate.getYear(), month = localDate.getMonthValue(), day = localDate.getDayOfMonth();
            
            if ((year == 2022) && (month == 4)) {

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
                    "&efrom the &b&lMagic Lord&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week1GiftList).replace("{user}", player.getName()));

        } else if (weekNum == 2) {

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a REGULAR gift " +
                    "&efrom the &b&lMagic Lord&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week2GiftList).replace("{user}", player.getName()));

        } else if (weekNum == 3) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a REGULAR gift " +
                    "&efrom the &b&lMagic Lord&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week3GiftList).replace("{user}", player.getName()));
        }
    }

    private void giveDonorGift(Player player, int weekNum) {

        if (weekNum == 1) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom the &b&lMagic Lord&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week1DonorGiftList).replace("{user}", player.getName()));

        } else if (weekNum == 2) {

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom the &b&lMagic Lord&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week2DonorGiftList).replace("{user}", player.getName()));

        } else if (weekNum == 3) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom the &b&lMagic Lord&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

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
