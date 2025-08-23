package com.steve_rizzo.emeraldscore.features.villagersave;


import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class VillagerSaverCommands implements CommandExecutor {
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = (Player)sender;
        if (!player.isValid())
            return true;
        if (args == null || args.length <= 1)
            return false;
        switch (args[0].toUpperCase()) {
            case "BLACKLISTWORLD":
                return AddToBlacklistIfWorldExists(player, args[1]);
            case "UNBLACKLISTWORLD":
                return RemoveFromTheBlackListIfWorldExists(player, args[1]);
        }
        return false;
    }

    private static boolean AddToBlacklistIfWorldExists(Player player, String worldName) {
        Set<String> worldList = new HashSet<>();
        for (World world : Bukkit.getWorlds())
            worldList.add(world.getName());
        if (worldList.contains(worldName)) {
            if (!Main.WorldBlackList.contains(worldName)) {
                Main.WorldBlackList.add(worldName);
                SendMessageToPlayer(player, "The World '" + worldName + "' was added to the blacklist.");
            } else {
                SendMessageToPlayer(player, "The World '" + worldName + "' already is on the blacklist.");
            }
        } else {
            SendMessageToPlayer(player, "The World '" + worldName + "' does not exist.");
            String availableWorlds = worldList.stream().collect(Collectors.joining(" - "));
            SendMessageToPlayer(player, "Available Worlds: " + availableWorlds);
        }
        return Main.WorldBlackList.contains(worldName);
    }

    private static boolean RemoveFromTheBlackListIfWorldExists(Player player, String worldName) {
        if (Main.WorldBlackList.contains(worldName)) {
            Main.WorldBlackList.remove(worldName);
            SendMessageToPlayer(player, "The World '" + worldName + "' was removed from the blacklist.");
        } else {
            SendMessageToPlayer(player, "The World '" + worldName + "' is not on the blacklist.");
        }
        return true;
    }

    private static void SendMessageToPlayer(Player player, String message) {
        if (!player.isValid())
            return;
        if (player.isBanned())
            return;
        if (!player.isOnline())
            return;
        String resultingMessage = VillagerSaverVars.PluginPrefix + " " + VillagerSaverVars.PluginPrefix;
        player.sendMessage(resultingMessage);
    }
}