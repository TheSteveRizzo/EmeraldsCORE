package com.steve_rizzo.emeraldscore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player p) {

            if (p.isOp()) {

                p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "=====" + ChatColor.GRAY + "[" + ChatColor.GREEN + ChatColor.BOLD + "EmeraldsMC" + ChatColor.GRAY + "]" + ChatColor.YELLOW + "" + ChatColor.BOLD +  "=====\n" +
                        ChatColor.GRAY + "Welcome to " + ChatColor.GREEN + "play.emeraldsmc.com" + ChatColor.GRAY + "!\nMake sure to:\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Read the " + ChatColor.RED + ChatColor.BOLD + "/rules\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Apply for Member using " + ChatColor.GREEN + ChatColor.BOLD + "/apply\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Join our Discord using " + ChatColor.GOLD + ChatColor.BOLD + "/discord\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Visit our PVP World using " + ChatColor.RED + ChatColor.BOLD + "/pvp\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Go back to Survival using " + ChatColor.DARK_AQUA + ChatColor.BOLD + "/survival\n" +
                        ChatColor.YELLOW + ChatColor.BOLD + "===== ===== =====");

            }

            return true;

        }

        return true;

    }
}
