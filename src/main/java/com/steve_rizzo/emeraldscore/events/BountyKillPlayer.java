package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BountyKillPlayer implements Listener {

    @EventHandler
    public void onBountyDeath(PlayerDeathEvent e) {
        Player died = e.getEntity();

        if (e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            if (EmeraldsCashAPI.getBalance(died) >= 100) {

                EmeraldsCashAPI.deductFunds(died, 100);
                EmeraldsCashAPI.addFunds(killer, 100);

                died.sendMessage(Main.prefix + ChatColor.YELLOW + "You were killed by "
                        + ServerJoinPlayer.getPlayerPrefixAndName(killer) + ChatColor.YELLOW +
                        " and they took " + ChatColor.GREEN + "$100 EmeraldsCash" + ChatColor.YELLOW + " from you.");

                Bukkit.getServer().broadcastMessage(Main.prefix + ServerJoinPlayer.getPlayerPrefixAndName(killer) + ChatColor.YELLOW + " killed "
                        + ServerJoinPlayer.getPlayerPrefixAndName(died) + ChatColor.YELLOW +
                        " and took " + ChatColor.GREEN + "$100 EmeraldsCash" + ChatColor.YELLOW + " from them.");

            } else {

                killer.sendMessage(Main.prefix + ChatColor.YELLOW + "You killed "
                        + ServerJoinPlayer.getPlayerPrefixAndName(died) + ChatColor.YELLOW +
                        " but they did not have any " + ChatColor.GREEN + "EmeraldsCash" + ChatColor.YELLOW + " to steal.");

            }
        }
    }
}
