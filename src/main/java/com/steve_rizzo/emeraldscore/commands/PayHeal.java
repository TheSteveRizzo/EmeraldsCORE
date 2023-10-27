package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayHeal implements CommandExecutor {
    List<String> playersInConfirm = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            String prefix = Main.prefix;
            Player p = (Player) sender;

            if (args.length == 0) {

                if (EmeraldsCashAPI.getBalance(p) > 500) {

                    p.sendMessage(prefix + ChatColor.YELLOW + "This " + ChatColor.LIGHT_PURPLE + "HEAL " + ChatColor.YELLOW + "will cost " + ChatColor.GREEN + "500 Emeralds Cash" + ChatColor.YELLOW + ". To continue, use " + ChatColor.AQUA + "/pheal confirm" + ChatColor.YELLOW + ".");
                    playersInConfirm.add(p.getUniqueId().toString());
                    return true;

                } else {

                    p.sendMessage(prefix + ChatColor.YELLOW + "You must have " + ChatColor.GREEN + "500 Emeralds Cash " + ChatColor.YELLOW + "to use this feature.");
                    return true;

                }
            } else if ((args.length == 1) && (args[0].equalsIgnoreCase("confirm"))) {

                if (playersInConfirm.contains(p.getUniqueId().toString())) {

                    playersInConfirm.remove(p.getUniqueId().toString());
                    EmeraldsCashAPI.deductFunds(p, 500);
                    p.setHealth(20.0);
                    p.sendMessage(prefix + ChatColor.LIGHT_PURPLE + "You have been healed.");
                    return true;

                } else {

                    p.sendMessage(prefix + ChatColor.YELLOW + "Please use " + ChatColor.AQUA + "/pheal " + ChatColor.YELLOW + "first.");

                    return true;

                }


            } else {

                p.sendMessage(prefix + ChatColor.RED + "Incorrect usage.");

                return true;

            }
        }

        return true;
    }
}
