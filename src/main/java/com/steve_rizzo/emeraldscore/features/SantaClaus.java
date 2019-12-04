package com.steve_rizzo.emeraldscore.features;

import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.bukkit.Bukkit.getServer;

// Class for "Santa" promotion, which provides users with a gift each day from 21 DEC (to 31) & (01 to) 04 JAN [w/ different gift options for each month].
public class SantaClaus implements Listener {

    // GIFT LIST FOR (NON-DONOR USERS), FROM DEC 21 TO DEC 24
    private List<String> december21to24GiftList = Arrays.asList("fe grant {user} 2500",
            "give {user} minecraft:emerald 4",
            "give {user} minecraft:diamond 8",
            "give {user} minecraft:coal 256",
            "give {user} minecraft:enchanted_golden_apple 6",
            "give {user} polar_bear_spawn_egg 1 {display:{Name:\"§aChristmas Event Gift §7(from Santa!)\",Lore:[\"§aThis item was gifted as a part of a §bChristmas Special§a!\"," +
                    "\"§7You may use,sell,or re-gift this item as you wish.\"]},EntityTag:{id:\"polar_bear\"}}");

    // GIFT LIST FOR (NON-DONOR USERS), FROM DEC 24 TO DEC 31
    private List<String> december24to31GiftList = Arrays.asList("fe grant {user} 5000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:redstone 256",
            "give {user} minecraft:enchanted_golden_apple 12",
            "give {user} polar_bear_spawn_egg 1 {display:{Name:\"§aChristmas Event Gift §7(from Santa!)\",Lore:[\"§aThis item was gifted as a part of a §bChristmas Special§a!\"," +
                    "\"§7You may use,sell,or re-gift this item as you wish.\"]},EntityTag:{id:\"polar_bear\"}}");


    // TODO
    // GIFT LIST FOR (DONOR USERS), FROM DEC 21 TO DEC 24
    private List<String> december21to24DonorGiftList = Arrays.asList("fe grant {user} 2500",
            "give {user} minecraft:emerald 4",
            "give {user} minecraft:diamond 8",
            "give {user} minecraft:coal 256",
            "give {user} minecraft:enchanted_golden_apple 6",
            "give {user} polar_bear_spawn_egg 1 {display:{Name:\"§aChristmas Event Gift §7(from Santa!)\",Lore:[\"§aThis item was gifted as a part of a §bChristmas Special§a!\"," +
                    "\"§7You may use,sell,or re-gift this item as you wish.\"]},EntityTag:{id:\"polar_bear\"}}");

    // GIFT LIST FOR (DONOR USERS), FROM DEC 24 TO DEC 31
    private List<String> december24to31DonorGiftList = Arrays.asList("fe grant {user} 5000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:redstone 256",
            "give {user} minecraft:gold 128",
            "give {user} minecraft:enchanted_golden_apple 12",
            "give {user} minecraft:coal 256",
            "give {user} polar_bear_spawn_egg 2 {display:{Name:\"§aChristmas Event Gift §7(from Santa!)\",Lore:[\"§aThis item was gifted as a part of a §bChristmas Special§a!\"," +
                    "\"§7You may use,sell,or re-gift this item as you wish.\"]},EntityTag:{id:\"polar_bear\"}}");

    // TODO


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

                    if (!isInCooldown(p)) giveDonorGift21(p, 21);

                } else {

                    if (!isInCooldown(p)) giveRegularGift21(p, 21);
                }


            } else {

                // Run the 25-31 command

                if (isDonor(p)) {

                    if (!isInCooldown(p)) giveDonorGift21(p, 24);

                } else {
                    if (!isInCooldown(p)) giveRegularGift21(p, 24);


                }
            }
        }
    }

    private String commandToRun(List givenList) {
        Random rand = new Random();
        return (String) givenList.get(rand.nextInt(givenList.size()));
    }

    private boolean isDonor(Player player) {
        return GamesAPI.isDonorOrHigher(player);
    }

    // TODO
    private boolean isInCooldown(Player player) {
        return false;
    }

    private void giveDonorGift21(Player player, int timeTo) {

        if (timeTo == 21) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&aEmeraldsMC&7]: {user} &ejust claimed a &cdonor gift " +
                    "&efrom &bSanta&e! Go claim your daily prize at /spawn!").replace("{user}", player.getName()));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(december21to24DonorGiftList).replace("{user}", player.getName()));

        } else if (timeTo == 24) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&aEmeraldsMC&7]: {user} &ejust claimed a &cdonor gift " +
                    "&efrom &bSanta&e! Go claim your daily prize at /spawn!").replace("{user}", player.getName()));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(december24to31DonorGiftList).replace("{user}", player.getName()));
        }

    }

    private void giveRegularGift21(Player player, int timeTo) {

        if (timeTo == 21) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&aEmeraldsMC&7]: {user} &ejust claimed a &cregular gift " +
                    "&efrom &bSanta&e! Go claim your daily prize at /spawn!").replace("{user}", player.getName()));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(december21to24GiftList).replace("{user}", player.getName()));

        } else if (timeTo == 24) {

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&aEmeraldsMC&7]: {user} &ejust claimed a &cregular gift " +
                    "&efrom &bSanta&e! Go claim your daily prize at /spawn!").replace("{user}", player.getName()));

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),
                    commandToRun(december24to31GiftList).replace("{user}", player.getName()));

        }

    }
}
