package com.steve_rizzo.emeraldscore.commands.economy;

import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BaltopCommand implements CommandExecutor {

    // ORDER INSTRUCTIONS:
    // ASC = TRUE
    // DESC = FALSE
    private static HashMap<String, Integer> sortBalances(HashMap<String, Integer> unsortMap, final boolean order) {
        List<HashMap.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(HashMap.Entry::getKey, HashMap.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

    private static void printMap(HashMap<String, Integer> map) {
        map.forEach((key, value) -> System.out.println("Key : " + key + " Value : " + value));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            p.sendMessage(ChatColor.GREEN + "---" + ChatColor.AQUA + "---["
                    + ChatColor.GREEN + "EmeraldsCash" + ChatColor.AQUA + "]---" + ChatColor.GREEN + "---");
            p.sendMessage(ChatColor.GRAY + "TOP BALANCES: ");

            HashMap<String, Integer> balTopList = EmeraldsCashAPI.returnAllBalances();
            // Sort balances
            balTopList = sortBalances(balTopList, false);

            balTopList.forEach((key, value) -> p.sendMessage(ChatColor.AQUA + key + ChatColor.GRAY + " : " + ChatColor.GREEN + "$" + value));

            p.sendMessage(ChatColor.GREEN + "---" + ChatColor.AQUA + "---["
                    + ChatColor.GREEN + "EmeraldsCash" + ChatColor.AQUA + "]---" + ChatColor.GREEN + "---");

            return true;

        }

        return true;
    }
}