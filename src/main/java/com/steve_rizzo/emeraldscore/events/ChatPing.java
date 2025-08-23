package com.steve_rizzo.emeraldscore.events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatPing implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        String message = e.getMessage().toLowerCase();
        if (message.contains("@ ")) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (message.contains("@ " + player.getName().toLowerCase())) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2F, 1F);
                }
            });
        }
    }
}