package com.steve_rizzo.emeraldscore.features;

import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

// Class for "Santa" promotion, which provides users with a gift each day from 21 DEC (to 31) & (01 to) 04 JAN [w/ different gift options for each month].
public class SantaClaus implements Listener {

    // GIFT LIST FOR (NON-DONOR USERS), FROM DEC 21 TO DEC 24
    private List<String> december21to24GiftList = Arrays.asList("fe grant {user} 2500",
            "give {user} minecraft:emerald 4",
            "give {user} minecraft:diamond 8",
            "give {user} minecraft:coal 256",
            "bc ",
            "give {user} minecraft:enchanted_golden_apple 6",
            "give {user} spawn_egg 1 102 {display:{Name:\"§aChristmas Event Gift §7(from Santa!)\",Lore:[\"§aThis item was gifted as a part of a §bChristmas Special§a!\"," +
                    "\"§7You may use,sell,or re-gift this item as you wish.\"]},EntityTag:{id:\"polar_bear\"}}");

    // GIFT LIST FOR (NON-DONOR USERS), FROM DEC 24 TO DEC 31
    private List<String> december24to31GiftList = Arrays.asList("fe grant {user} 5000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:redstone 256",
            "give {user} minecraft:enchanted_golden_apple 12",
            "give {user} spawn_egg 1 102 {display:{Name:\"§aChristmas Event Gift §7(from Santa!)\",Lore:[\"§aThis item was gifted as a part of a §bChristmas Special§a!\"," +
                    "\"§7You may use,sell,or re-gift this item as you wish.\"]},EntityTag:{id:\"polar_bear\"}}");


    // TODO
    // GIFT LIST FOR (DONOR USERS), FROM DEC 21 TO DEC 24
    private List<String> december21to24DonorGiftList = Arrays.asList("fe grant {user} 2500",
            "give {user} minecraft:emerald 4",
            "give {user} minecraft:diamond 8",
            "give {user} minecraft:coal 256",
            "give {user} minecraft:enchanted_golden_apple 6",
            "give {user} spawn_egg 1 102 {display:{Name:\"§aChristmas Event Gift §7(from Santa!)\",Lore:[\"§aThis item was gifted as a part of a §bChristmas Special§a!\"," +
                    "\"§7You may use,sell,or re-gift this item as you wish.\"]},EntityTag:{id:\"polar_bear\"}}");

    // GIFT LIST FOR (DONOR USERS), FROM DEC 24 TO DEC 31
    private List<String> december24to31DonorGiftList = Arrays.asList("fe grant {user} 5000",
            "give {user} minecraft:emerald 8",
            "give {user} minecraft:diamond 16",
            "give {user} minecraft:redstone 256",
            "give {user} minecraft:gold 128",
            "give {user} minecraft:enchanted_golden_apple 12",
            "give {user} minecraft:coal 256",
            "give {user} spawn_egg 2 102 {display:{Name:\"§aChristmas Event Gift §7(from Santa!)\",Lore:[\"§aThis item was gifted as a part of a §bChristmas Special§a!\"," +
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

            } else {

            }
        }

        if ((year == 2019) && (month == 10)) {
            if (day == 31) {

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&aEmeraldsMC&7]: {user} &ejust claimed a &cgift " +
                        "&efrom &bSanta&e! Go claim your daily prize at /spawn!").replace("{user}", p.getName()));

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cA random command would have been" + commandToRun(december21to24GiftList)));

            } else {

                p.sendMessage("ERROR SANTA CLAUS!");

            }
        }

    }

    private String commandToRun(List givenList) {
        Random rand = new Random();
        return (String) givenList.get(rand.nextInt(givenList.size()));
    }
}
