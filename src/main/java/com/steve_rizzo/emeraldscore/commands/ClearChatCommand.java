package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
          Player p = (Player) sender;
          if (p.hasPermission("emeraldsmc.clearchat")) {
              for (int i = 0 ; i <= 50 ; i++) {
                  Bukkit.getServer().broadcastMessage("                            ");
              }
              Bukkit.getServer().broadcastMessage(Main.prefix + ServerJoinPlayer.getPlayerPrefixAndName(p) + ChatColor.AQUA + " has cleared the chat" + ChatColor.GRAY + ".");
              return true;
          } else {
              p.sendMessage(Main.prefix + ChatColor.RED + "No Permission.");
              return true;
          }
        } else {
            for (int i = 0 ; i <= 50 ; i++) {
                Bukkit.getServer().broadcastMessage("                            ");
            }
            Bukkit.getServer().broadcastMessage(Main.prefix + ChatColor.RED + "CONSOLE" + ChatColor.AQUA + " has cleared the chat" + ChatColor.GRAY + ".");
            return true;
        }
    }
}
