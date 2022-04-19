package com.steve_rizzo.emeraldscore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RulesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            String rules =
                    ChatColor.AQUA + "" + ChatColor.BOLD + "=====-x+x-[RULES]-x+x-=====" +
                            "\n" +
                            ChatColor.RED + "" + ChatColor.BOLD + "RULE #1 >> NO DESTROYING PLAYER PROPERTY." + ChatColor.RESET + " " +
                            ChatColor.GRAY + "This includes ALL built structures, chests, pets, crops, and property." +
                            ChatColor.RED + "" + ChatColor.BOLD + "RULE #2 >> NO ADVERTISING. " + ChatColor.RESET + " " +
                            ChatColor.GRAY + "Please do not share any web links, server addresses, or social media links in chat please." +
                            ChatColor.RED + "" + ChatColor.BOLD + "RULE #3 >> KEEP CHAT CIVIL." + ChatColor.RESET + " " +
                            ChatColor.GRAY + "Chat is monitored and targeted harassment, verbal abuse, and other forms of misuse may result in punishment." +
                            ChatColor.RED + "" + ChatColor.BOLD + "RULE #4 >> NO CHEATING." + ChatColor.RESET + " " +
                            ChatColor.GRAY + "Any modifications that unfairly enhance game-play (i.e., x-ray, flying mods, etc.) are NOT permitted." +
                            ChatColor.RED + "" + ChatColor.BOLD + "RULE #5 >> GATHER RESOURCES IN /FARMWORLD." + ChatColor.RESET + " " +
                            ChatColor.GRAY + "Please avoid farming in the main world. Use /farmworld for mining and gathering, instead!" +
                            "\n" +
                            ChatColor.AQUA + "" + ChatColor.BOLD + "=====-x+x-[/RULES]-x+x-=====";

            p.sendMessage(rules);

            return true;
        }

        return true;
    }
}

