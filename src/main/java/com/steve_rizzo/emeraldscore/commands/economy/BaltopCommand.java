package com.steve_rizzo.emeraldscore.commands.economy;

import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaltopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            p.sendMessage(ChatColor.GREEN + "---" + ChatColor.AQUA + "---["
                    + ChatColor.GREEN + "EmeraldsCash" + ChatColor.AQUA + "]---" + ChatColor.GREEN + "---");
            p.sendMessage(ChatColor.GRAY + "TOP BALANCES: ");

            EmeraldsCashAPI.returnTopBalances(p, 10);
            return true;

        }

        return true;
    }
}