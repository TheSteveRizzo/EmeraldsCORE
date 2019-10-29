package com.steve_rizzo.emeraldscore.events;

import de.myzelyam.api.vanish.PlayerHideEvent;
import de.myzelyam.api.vanish.PlayerShowEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerVanish implements Listener {

    // TODO: Check when player vanishes and remove from tab.
    // Check if this is a proper fix:
    @EventHandler
    public void onHide(PlayerHideEvent e) {
        Bukkit.getServer().broadcastMessage(ServerJoinPlayer.getPlayerPrefixAndName(e.getPlayer()) +
                ChatColor.YELLOW + " has left The Emeralds.");
    }

    @EventHandler
    public void onShow(PlayerShowEvent e) {
        Bukkit.getServer().broadcastMessage(ServerJoinPlayer.getPlayerPrefixAndName(e.getPlayer()) +
                ChatColor.YELLOW + " has joined The Emeralds.");
    }

}
