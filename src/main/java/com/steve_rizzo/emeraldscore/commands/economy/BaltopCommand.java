package com.steve_rizzo.emeraldscore.commands.economy;

import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletionException;

public class BaltopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            EmeraldsCashAPI.sendTopBalancesMessage(p);

            return true;
        }

        return true;
    }
}