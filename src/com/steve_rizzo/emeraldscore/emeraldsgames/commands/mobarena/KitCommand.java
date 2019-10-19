package com.steve_rizzo.emeraldscore.emeraldsgames.commands.mobarena;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class KitCommand implements CommandExecutor {

    MobArena ma = Main.mobarena;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            ItemStack selector = GamesAPI.createGuiItem(Material.IRON_BLOCK, "§aEmeraldsGames MobArena Kit Selector",
                    "§bUse this to access §cexclusive §bEmeraldsMC Mob Arena Kits!");

            if (!p.getInventory().contains(selector)) {

                Arena mobArena = ma.getArenaMaster().getArenaWithName("default");
                Set<Player> playersInLobby = mobArena.getPlayersInLobby();
                if (playersInLobby.contains(p)) {
                    p.getInventory().addItem(selector);
                    return true;
                } else {
                    p.sendMessage(Main.prefix + ChatColor.GOLD + "You must be in the Mob Arena to use this!");
                    return true;
                }
            }

            p.sendMessage(Main.prefix + ChatColor.GRAY + "You've been given the " + ChatColor.GREEN + "Kit Selector" + ChatColor.GRAY + "!");

            return true;

        }

        return false;

    }
}
