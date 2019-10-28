package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import de.myzelyam.api.vanish.PlayerHideEvent;
import org.bukkit.event.Listener;

public class PlayerVanish implements Listener {

    // TODO: Check when player vanishes and remove from tab.
    // Check if this is a proper fix:
    public void onHide(PlayerHideEvent e) {
        e.getPlayer().hidePlayer(Main.core, e.getPlayer());
    }

}
