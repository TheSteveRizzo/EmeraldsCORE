package com.steve_rizzo.emeraldscore.staffapps.events;

import com.steve_rizzo.emeraldscore.staffapps.StaffHandler;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoin
        implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        if (StaffHandler.hasAcceptedApp(e.getPlayer()) || StaffHandler.hasDeniedApp(e.getPlayer())) {


            if (StaffHandler.hasAcceptedApp(e.getPlayer())) {

                String rank = StaffHandler.getMain().getConfig().getString("rank_to_promote");
                StaffHandler.setRank(e.getPlayer(), rank);
                StaffHandler.setPromoted(e.getPlayer());
                e.getPlayer().sendMessage(StaffHandler.prefix + ChatColor.GREEN + "Congrats, your application was " + ChatColor.BOLD + "ACCEPTED" + ChatColor.RESET + ChatColor.GREEN + ".");
            } else if (StaffHandler.hasDeniedApp(e.getPlayer())) {

                e.getPlayer().sendMessage(StaffHandler.prefix + ChatColor.RED + "Sorry, your application was " + ChatColor.BOLD + "DENIED" + ChatColor.RESET + ChatColor.RED + ".");
            }


            StaffHandler.setViewed(e.getPlayer());
        }
    }
}
