package com.steve_rizzo.emeraldscore.emeraldsgames.commands.games;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EGCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            ItemStack compass = GamesAPI.createGuiItem(Material.COMPASS, "§aEmeraldsGames Navigator",
                    "§bUse this to access §cexclusive §bEmeraldsMC Games!");


            if (!p.getInventory().contains(compass)) {
                p.getInventory().addItem(compass);
            }

            p.sendMessage(Main.prefix + ChatColor.GRAY + "You've been given the " + ChatColor.GREEN + "EmeraldsGames Navigator" + ChatColor.GRAY + "!");

            return true;

        }

        return false;

    }
}
