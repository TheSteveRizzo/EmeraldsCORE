package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.BackCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private String prefix = Main.prefix;


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        Bukkit.broadcastMessage(prefix + ChatColor.DARK_AQUA + e.getDeathMessage().replace(p.getName(), ServerJoinPlayer.getPlayerPrefixAndName(p)));

        if (Main.serverIDName.equalsIgnoreCase("bed")) return;

        e.setDeathMessage("");

        Location deathLoc = e.getEntity().getLocation();
        String uuid = p.getUniqueId().toString();
        BackCommand.deathLocations.put(uuid, deathLoc);

        // Create a clickable message for the player
        TextComponent message = new TextComponent(ChatColor.AQUA + "[" +
                ChatColor.GRAY + "Click here to return to your death location" + ChatColor.AQUA + "]");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/back"));

        p.sendMessage(ChatColor.GRAY+ "=====" + ChatColor.AQUA + "-----" + ChatColor.GRAY + "=====");
        // Send the clickable message
        p.spigot().sendMessage(message);
        p.sendMessage(ChatColor.GRAY+ "=====" + ChatColor.AQUA + "-----" + ChatColor.GRAY + "=====");

    }
}
