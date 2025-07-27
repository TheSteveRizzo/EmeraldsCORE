package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RulesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            String serverID = Main.serverIDName;
            Player p = (Player) sender;

            String rules = "";
            if (serverID.equalsIgnoreCase("survival")) {
                rules =
                        ChatColor.AQUA + "" + ChatColor.BOLD + "=====-x+x-[RULES]-x+x-=====" +
                                "\n" +
                                ChatColor.RED + "" + ChatColor.BOLD + "RULE #1 >> NO DESTROYING PLAYER PROPERTY.\n" + ChatColor.RESET + " " +
                                ChatColor.GOLD + "This includes ALL built structures, chests, pets, crops, and property." +
                                ChatColor.RED + "" + ChatColor.BOLD + "\nRULE #2 >> NO ADVERTISING.\n" + ChatColor.RESET + " " +
                                ChatColor.GOLD + "Please do not share any web links, server addresses, or social media links in chat please." +
                                ChatColor.RED + "" + ChatColor.BOLD + "\nRULE #3 >> KEEP CHAT CIVIL.\n" + ChatColor.RESET + " " +
                                ChatColor.GOLD + "Chat is monitored and targeted harassment, verbal abuse, and other forms of misuse may result in punishment." +
                                ChatColor.RED + "" + ChatColor.BOLD + "\nRULE #4 >> NO CHEATING.\n" + ChatColor.RESET + " " +
                                ChatColor.GOLD + "Any modifications that unfairly enhance game-play (i.e., x-ray, flying mods, etc.) are NOT permitted." +
                                ChatColor.RED + "" + ChatColor.BOLD + "\nRULE #5 >> GATHER RESOURCES IN /FARMWORLD.\n" + ChatColor.RESET + " " +
                                ChatColor.GOLD + "Please avoid farming in the main world. Use /farmworld for mining and gathering, instead!" +
                                "\n" +
                                ChatColor.AQUA + "" + ChatColor.BOLD + "=====-x+x-[/RULES]-x+x-=====";
            } else {
                rules =
                        ChatColor.AQUA + "" + ChatColor.BOLD + "=====-x+x-[RULES]-x+x-=====" +
                                "\n" +
                                ChatColor.RED + "" + ChatColor.BOLD + "RULE #1 >> NO HACKS OR UNFAIR CLIENT MODS.\n" + ChatColor.RESET + " " +
                                ChatColor.GOLD + "Any modifications that unfairly enhance game-play (i.e., x-ray, flying mods, etc.) are NOT permitted." +
                                ChatColor.RED + "" + ChatColor.BOLD + "\nRULE #2 >> NO ADVERTISING.\n" + ChatColor.RESET + " " +
                                ChatColor.GOLD + "Please do not share any web links, server addresses, or social media links in chat please." +
                                ChatColor.RED + "" + ChatColor.BOLD + "\nRULE #3 >> KEEP CHAT CIVIL.\n" + ChatColor.RESET + " " +
                                ChatColor.GOLD + "Chat is monitored and targeted harassment, verbal abuse, and other forms of misuse may result in punishment." +
                                ChatColor.RED + "" + ChatColor.BOLD + "\nRULE #4 >> RESPECT STAFF AND OTHERS.\n" + ChatColor.RESET + " " +
                                ChatColor.GOLD + "Please listen to staff member's instructions; they are here to help you. Do not disrespect staff or others." +
                                ChatColor.RED + "" + ChatColor.BOLD + "\nRULE #5 >> PLAY FAIRLY.\n" + ChatColor.RESET + " " +
                                ChatColor.GOLD + "Be helpful and assist new players, rather than trolling them." +
                                "\n" +
                                ChatColor.AQUA + "" + ChatColor.BOLD + "=====-x+x-[/RULES]-x+x-=====";
            }
            p.sendMessage(rules);

            return true;
        }

        return true;
    }
}

