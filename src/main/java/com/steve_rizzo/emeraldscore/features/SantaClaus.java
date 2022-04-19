package com.steve_rizzo.emeraldscore.features;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Bukkit.getServer;

// Class for "Santa" promotion, which provides users with a gift each day from 21 DEC (to 31)
// & (01 to) 04 JAN [w/ different gift options for each month].
public class SantaClaus implements Listener {

    FileConfiguration config = Main.core.cooldownConfig;

    // GIFT LIST FOR (NON-DONOR USERS), FROM DEC 21 TO DEC 24
    private List<String> december21to24GiftList = Arrays.asList("fe grant {user} 1000",
            "give {user} minecraft:emerald 4",
            "give {user} minecraft:diamond 8",
            "give {user} minecraft:coal 64",
            "give {user} minecraft:enchanted_golden_apple 4",
            "give {user} polar_bear_spawn_egg 1");

    // GIFT LIST FOR (NON-DONOR USERS), FROM DEC 24 TO DEC 31
    private List<String> december24to31GiftList = Arrays.asList("fe grant {user} 2000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:redstone 128",
            "give {user} minecraft:enchanted_golden_apple 8",
            "give {user} minecraft:diamond_sword 1",
            "give {user} polar_bear_spawn_egg 1");

    // GIFT LIST FOR (DONOR USERS), FROM DEC 21 TO DEC 24
    private List<String> december21to24DonorGiftList = Arrays.asList("fe grant {user} 2000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:coal 128",
            "give {user} minecraft:enchanted_golden_apple 8",
            // SHARP 1 DIA SWORD
            "give {user} diamond_sword {display:{Name:\"[{\\\"text\\\":\\\"EmeraldsMC Holiday Gift\\\",\\\"color\\\":\\\"green\\\"}]\",Lore:[\"{\\\"text\\\":\\\"A special Christmas gift\\\",\\\"color\\\":\\\"aqua\\\"}\"]},Enchantments:[{id:sharpness,lvl:1}]} 1",
            "give {user} polar_bear_spawn_egg 2");


    // GIFT LIST FOR (DONOR USERS), FROM DEC 24 TO DEC 31
    private List<String> december24to31DonorGiftList = Arrays.asList("fe grant {user} 5000",
            "give {user} minecraft:emerald 16",
            "give {user} minecraft:diamond 32",
            "give {user} minecraft:coal 256",
            "give {user} minecraft:enchanted_golden_apple 16",
            // SHARP 2 DIA SWORD
            "give {user} diamond_sword {display:{Name:\"[{\\\"text\\\":\\\"EmeraldsMC Holiday Gift\\\",\\\"color\\\":\\\"green\\\"}]\",Lore:[\"{\\\"text\\\":\\\"A special Christmas gift\\\",\\\"color\\\":\\\"aqua\\\"}\"]},Enchantments:[{id:sharpness,lvl:2}]} 1",
            "give {user} polar_bear_spawn_egg 1");


    @EventHandler
    public void onSantaClick(NPCRightClickEvent e) {

        if ((e.getNPC() == null) || (!e.getNPC().getName().equalsIgnoreCase("Santa"))) return;

        NPC santa = e.getNPC();
        Player p = e.getClicker();

        // Current date
        Date curDate = new Date();
        LocalDate localDate = curDate.toInstant().atZone(ZoneId.of("America/New_York")).toLocalDate();
        int year  = localDate.getYear(), month = localDate.getMonthValue(), day = localDate.getDayOfMonth();

        if ((year == 2019) && (month == 12)) {
            if (day <= 24) {

                // Run the 21-24 command

                if (isDonor(p)) {

                    if (!isInCooldown(p)) {
                        giveDonorGift(p, 21);
                        addToCooldown(p);
                    } else {
                        p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                                ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");
                    }

                } else {

                    if (!isInCooldown(p)) {
                        giveRegularGift(p, 21);
                        addToCooldown(p);
                    } else {
                        p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                                ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");
                    }
                }


            } else {

                // Run the 25-31 command

                if (isDonor(p)) {

                    if (!isInCooldown(p)) {
                        giveDonorGift(p, 24);
                        addToCooldown(p);
                    } else {
                        p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                                ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");

                    }

                } else {
                    if (!isInCooldown(p)) {
                        giveRegularGift(p, 24);
                        addToCooldown(p);
                    } else {

                        p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                                ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");

                    }
                }
            }
        }
    }

    private void giveRegularGift(Player player, int timeTo) {

        if (timeTo == 21) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a REGULAR gift " +
                    "&efrom &bSanta&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(december21to24GiftList).replace("{user}", player.getName()));

        } else if (timeTo == 24) {

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a REGULAR gift " +
                    "&efrom &bSanta&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(december24to31GiftList).replace("{user}", player.getName()));

        }
    }

    private void giveDonorGift(Player player, int timeTo) {

        if (timeTo == 21) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom &bSanta&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(december21to24DonorGiftList).replace("{user}", player.getName()));

        } else if (timeTo == 24) {

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&7[&aEmeraldsMC&7]: {user} &ejust claimed a &dDONOR gift " +
                    "&efrom &bSanta&e! Go claim your daily prize at /spawn!").replace("{user}", ServerJoinPlayer.getPlayerPrefixAndName(player)));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(december24to31GiftList).replace("{user}", player.getName()));

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
                return false;
            }
            return true;
        }
        return false;
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
