package com.steve_rizzo.emeraldscore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingServer implements Listener {

    @EventHandler
    public void pingServer(ServerListPingEvent e) {

        e.setMotd("§b» §a§lWelcome to §c§lplay.emeraldsmc.com §a§lSMP! §b§l«");
        e.setMaxPlayers(100);

    }

}
