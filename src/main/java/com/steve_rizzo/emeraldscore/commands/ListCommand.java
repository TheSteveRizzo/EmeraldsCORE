package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListCommand implements CommandExecutor {

    ArrayList<String> playerNames = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player p) {

            for (Player onlineUsers : Bukkit.getServer().getOnlinePlayers()) {
                playerNames.add(onlineUsers.getName());
            }

            p.sendMessage(Main.prefix + ChatColor.GRAY + "There are ("
                    + ChatColor.AQUA + Bukkit.getServer().getOnlinePlayers().size()
                    + ChatColor.GRAY + " / " + ChatColor.AQUA + Bukkit.getServer().getMaxPlayers()
                    + ChatColor.GRAY + ") players online:\n"
                    + ChatColor.GRAY + Arrays.asList(playerNames)
            );

            playerNames.clear();

        }
        return true;
    }
}
