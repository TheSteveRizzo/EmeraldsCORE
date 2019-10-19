package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private String prefix = Main.prefix;


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        e.setDeathMessage(prefix + ChatColor.DARK_AQUA + p.getName() + e.getDeathMessage());
    }
}
