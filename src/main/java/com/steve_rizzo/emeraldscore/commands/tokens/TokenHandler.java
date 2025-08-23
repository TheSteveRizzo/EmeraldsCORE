package com.steve_rizzo.emeraldscore.commands.tokens;

import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TokenHandler implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (GamesAPI.isDonorOrHigher(event.getPlayer())) {
            // Check if the player has a Token account
            TokensAPI.doesTokenAccountExist(event.getPlayer().getUniqueId().toString()).thenAccept(accountExists -> {
                if (!accountExists) {
                    // Create entry for Token account
                    TokensAPI.setTokensBalance(event.getPlayer(), 0);
                }
            });
        }
    }
}