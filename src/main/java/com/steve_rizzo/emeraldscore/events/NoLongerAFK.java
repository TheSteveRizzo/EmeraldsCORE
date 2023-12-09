package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.AFKCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class NoLongerAFK implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (AFKCommand.listOfAFKPlayers.contains(p.getName())) {
            Bukkit.broadcastMessage(Main.prefix + ChatColor.GOLD + p.getName() + ChatColor.GRAY
                    + " is " + ChatColor.GREEN + "no longer AFK" + ChatColor.GRAY + ".");
            AFKCommand.listOfAFKPlayers.remove(p.getName());
            ServerJoinPlayer.setPlayerTabAndTagName(p);
        }
    }
}
