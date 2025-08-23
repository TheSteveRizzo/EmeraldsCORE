package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class AcceptCommand implements CommandExecutor {

    private final String prefix = Main.prefix;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1) {
            sender.sendMessage(prefix + ChatColor.RED + "Usage: /accept <playerName>");
            return true;
        }

        String targetName = args[0];

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("emeraldscore.accept")) {
                p.sendMessage(prefix + ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }
            setRankConsole(targetName);
            p.sendMessage(prefix + ChatColor.GREEN + "Successfully accepted " + ChatColor.DARK_GRAY + targetName + ChatColor.GREEN + "'s MEMBER application.");
        } else {
            setRankConsole(targetName);
            Bukkit.getLogger().log(Level.INFO, prefix + "Accepted " + targetName + "'s application via console.");
        }

        return true;
    }

    private void setRankConsole(String target) {
        String rank = "member";

        // Execute LuckPerms command via console
        Bukkit.getServer().dispatchCommand(
                Bukkit.getConsoleSender(),
                "lp user " + target + " parent set " + rank
        );

        // Log to console
        System.out.println(prefix + "user " + target + " has been updated to group " + rank.toUpperCase() + "!");

        // If player is online, message them and update data
        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer != null && targetPlayer.isOnline()) {
            targetPlayer.sendMessage(
                    prefix + "Your group has been updated to " + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!"
            );
            ServerJoinPlayer.ranks.updateAndSaveData(targetPlayer);
        }

        // Broadcast to server
        Bukkit.broadcastMessage(
                prefix + ChatColor.DARK_GRAY + target
                        + ChatColor.AQUA + "'s application was "
                        + ChatColor.GREEN + "accepted "
                        + ChatColor.AQUA + "and they were promoted to "
                        + ChatColor.GRAY + "MEMBER Rank" + ChatColor.AQUA + "!"
        );
    }
}
