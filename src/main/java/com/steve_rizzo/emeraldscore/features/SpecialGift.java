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

    // GIFT LIST FOR (NON-DONOR USERS), FROM OCT 01 TO OCT 07
    private List<String> week1GiftList = Arrays.asList(
            "givebal {user} 2500",                  // ND Cash amount
            "give {user} minecraft:emerald 2",       // ND Emerald Amount
            "give {user} minecraft:diamond 6",       // ND Diamond Amount
            "give {user} minecraft:cooked_beef 16",
            "give {user} minecraft:white_wool 64",
            "give {user} minecraft:bookshelf 32",
            "give {user} minecraft:raw_iron 32",
            "give {user} minecraft:stone 32",
            "give {user} minecraft:deepslate 32",
            "give {user} minecraft:white_concrete 32",
            "give {user} minecraft:black_concrete 32",
            "give {user} minecraft:sea_lantern 8",
            "givetokens {user} 2",
            "voucher give {user} LotteryVoucher 2"

    );

    // GIFT LIST FOR (NON-DONOR USERS), FROM OCT 07 TO OCT 14
    private List<String> week2GiftList = Arrays.asList(
            "givebal {user} 5000",                  // ND Cash amount
            "give {user} minecraft:emerald 4",       // ND Emerald Amount
            "give {user} minecraft:diamond 8",       // ND Diamond Amount
            "give {user} minecraft:cooked_beef 32",
            "give {user} minecraft:bookshelf 32",
            "give {user} minecraft:white_wool 64",
            "give {user} minecraft:pumpkin_pie 16",
            "give {user} minecraft:cake 8",
            "give {user} minecraft:raw_iron 64",
            "give {user} minecraft:stone 64",
            "give {user} minecraft:deepslate 64",
            "give {user} minecraft:white_concrete 64",
            "give {user} minecraft:black_concrete 32",
            "givetokens {user} 2",
            "voucher give {user} LotteryVoucher 2"

    );

    // GIFT LIST FOR (NON-DONOR USERS), FROM OCT 21+
    private List<String> week3GiftList = Arrays.asList(
            "givebal {user} 7500",                  // ND Cash amount
            "give {user} minecraft:emerald 8",       // ND Emerald Amount
            "give {user} minecraft:diamond 12",       // ND Diamond Amount
            "give {user} minecraft:cooked_beef 48",
            "give {user} minecraft:bookshelf 32",
            "give {user} minecraft:white_wool 64",
            "give {user} minecraft:raw_iron 128",
            "give {user} minecraft:stone 256",
            "give {user} minecraft:deepslate 128",
            "give {user} minecraft:raw_gold 32",
            "give {user} minecraft:black_concrete 64",
            "givetokens {user} 4",
            "voucher give {user} LotteryVoucher 3"

    );



    ////// INTENTIONAL SEPARATION BETWEEN NON-DONOR & DONOR GIFTS



    // GIFT LIST FOR (DONOR USERS), FROM OCT 01 TO OCT 07 (Same as before)
    private List<String> week1DonorGiftList = Arrays.asList(
            "givebal {user} 7500",                  // D Cash amount
            "give {user} minecraft:emerald 8",       // D Emerald Amount
            "give {user} minecraft:diamond 12",       // D Diamond Amount
            "give {user} minecraft:cooked_beef 48",
            "give {user} minecraft:bookshelf 64",
            "give {user} minecraft:white_wool 64",
            "give {user} minecraft:netherite_ingot 2",
            "give {user} minecraft:black_concrete 16",
            "give {user} minecraft:wither_skeleton_skull 1",
            "givetokens {user} 3",
            "voucher give {user} LotteryVoucher 4"

    );

    // GIFT LIST FOR (DONOR USERS), FROM OCT 07 TO OCT 14 (Same as before)
    private List<String> week2DonorGiftList = Arrays.asList(
            "givebal {user} 10000",                  // D Cash amount
            "give {user} minecraft:emerald 12",       // D Emerald Amount
            "give {user} minecraft:diamond 16",       // D Diamond Amount
            "give {user} minecraft:cooked_beef 64",
            "give {user} minecraft:bookshelf 64",
            "give {user} minecraft:white_wool 64",
            "give {user} minecraft:netherite_ingot 4",
            "give {user} minecraft:raw_iron 128",
            "give {user} minecraft:obsidian 64",
            "give {user} minecraft:sea_lantern 16",
            "give {user} minecraft:black_concrete 64",
            "give {user} minecraft:beacon 1",
            "give {user} minecraft:wither_skeleton_skull 1",
            "givetokens {user} 4",
            "voucher give {user} LotteryVoucher 4"

    );

    // GIFT LIST FOR (DONOR USERS), FROM OCT 21+ (Same as before)
    private List<String> week3DonorGiftList = Arrays.asList(
            "givebal {user} 12500",                  // D Cash amount
            "give {user} minecraft:emerald 16",       // D Emerald Amount
            "give {user} minecraft:diamond 32",       // D Diamond Amount
            "give {user} minecraft:cooked_beef 64",
            "give {user} minecraft:bookshelf 64",
            "give {user} minecraft:white_wool 64",
            "give {user} minecraft:netherite_ingot 6",
            "give {user} minecraft:raw_iron 256",
            "give {user} minecraft:glass 128",
            "give {user} minecraft:black_concrete 64",
            "give {user} minecraft:heart_of_the_sea 1",
            "give {user} minecraft:golden_apple 2",
            "give {user} minecraft:enchanted_golden_apple 1",
            "give {user} minecraft:wither_skeleton_skull 1",
            "givetokens {user} 5",
            "voucher give {user} LotteryVoucher 6"

    );

    @EventHandler
    public void onMagicLordClick(PlayerInteractEntityEvent e) {

        Entity emeraldsQueen;
        Player p = e.getPlayer();

        if (e.getRightClicked().getName().equalsIgnoreCase("vote")) {
            p.performCommand("vote");
        }

        if (e.getRightClicked().getName().equalsIgnoreCase("kits")) {
            p.performCommand("kits");
        }

        if (e.getRightClicked().getName().equalsIgnoreCase("bounty") ||
                (e.getRightClicked().getName().equalsIgnoreCase("bounty2"))) {
            p.performCommand("bounty");
        }

        if (e.getRightClicked().getName().equalsIgnoreCase("rtp") ||
                (e.getRightClicked().getName().equalsIgnoreCase("rtp2"))) {
            p.performCommand("rtp");
        }

        if (e.getRightClicked().getName().equalsIgnoreCase("EmeraldsQueen")) {

            emeraldsQueen = e.getRightClicked();
            // Current date
            Date curDate = new Date();
            LocalDate localDate = curDate.toInstant().atZone(ZoneId.of("America/New_York")).toLocalDate();
            int year = localDate.getYear(), month = localDate.getMonthValue(), day = localDate.getDayOfMonth();

            if ((year == 2025) && (month == 2 || month == 3)) {

                if (day >= 1 && day < 14) {

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
                    "&efrom the &a&lEmeralds &d&lQueen&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week1GiftList).replace("{user}", player.getName()));

        } else if (weekNum == 2) {

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a REGULAR gift " +
                    "&efrom the &a&lEmeralds &d&lQueen&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week2GiftList).replace("{user}", player.getName()));

        } else if (weekNum == 3) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a REGULAR gift " +
                    "&efrom the &a&lEmeralds &d&lQueen&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week3GiftList).replace("{user}", player.getName()));
        }
    }

    private void giveDonorGift(Player player, int weekNum) {

        if (weekNum == 1) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom the &a&lEmeralds &d&lQueen&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week1DonorGiftList).replace("{user}", player.getName()));

        } else if (weekNum == 2) {

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom the &a&lEmeralds &d&lQueen&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(week2DonorGiftList).replace("{user}", player.getName()));

        } else if (weekNum == 3) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom the &a&lEmeralds &d&lQueen&r&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

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