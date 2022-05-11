package com.steve_rizzo.emeraldscore.commands.economy;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (args.length == 0) {

                EmeraldsCashAPI.displayBalance(p);

                return true;

            } else if (args.length == 1) {

                try {

                    EmeraldsCashAPI.displayBalanceUUID(args[0], getPlayerUUID(args[0]), p);

                    return true;

                } catch (Exception ex) {

                    p.sendMessage(Main.prefix + ChatColor.RED + "Error: " + ChatColor.GRAY + "player does not exist.");

                    return true;
                }

            } else {
                p.sendMessage(Main.prefix + ChatColor.RED + "Usage: " + ChatColor.AQUA + "/bal | /bal [player]");
                return true;
            }
        }

        return true;

    }

    private String getPlayerUUID(String playerName) {
        for (OfflinePlayer offlinePlayer : Bukkit.getServer().getOfflinePlayers()) {
            if (offlinePlayer.getName().equalsIgnoreCase(playerName))
                return offlinePlayer.getUniqueId().toString();
        }
        return null;
    }
}
