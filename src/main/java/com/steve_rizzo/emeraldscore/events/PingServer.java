package com.steve_rizzo.emeraldscore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingServer implements Listener {

    public static String defaultMOTD = "§b§l» §a§lWelcome to §b§lplay.emeraldsmc.com §a§lSMP! §b§l«\n§b§l» §b§lNEW! §e§lNEW ITEMS, ENCHANTS, & CRATES §b§l! «";

    public static void overrideMOTD(String newMOTD) {
        defaultMOTD = newMOTD;
    }

    @EventHandler
    public void pingServer(ServerListPingEvent e) {

        e.setMotd(defaultMOTD);
        e.setMaxPlayers(100);

    }
}
