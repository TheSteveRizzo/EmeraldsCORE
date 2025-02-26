package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingServer implements Listener {

    public static String motd = "";
    public static void overrideMOTD(String newMOTD) {
        motd = newMOTD;
    }

    @EventHandler
    public void pingServer(ServerListPingEvent e) {

        // §b§l» §b§lWelcome to §a§lEmeraldsMC\! §b§l«      §b§l» §e§lSERVER\! §b§l «
        switch (Main.serverIDName.toLowerCase()) {
            case "smp":
                motd = "          §m――――§r §b💎 §a§lEmeraldsMC §b💎 §7[§c1.21.4§7] §r§m――――§r§r\n" +
                        "     §bLands, Pets, Gifts, Custom Enchants, + More";
                break;
            case "factions":
                motd = "§b§l» §b§lWelcome to §a§lEmeraldsMC! §b§l« §b§l» §e§lFACTIONS! §b§l«";
                break;
            case "sky":
                motd = "§b§l» §b§lWelcome to §a§lEmeraldsMC! §b§l« §b§l» §e§lSKYBLOCK! §b§l«";
                break;
            case "battle":
                motd = "§b§l» §b§lWelcome to §a§lEmeraldsMC! §b§l« §b§l» §e§lBATTLE! §b§l«";
                break;
            default:
                motd = "§b§l» §b§lWelcome to §a§lEmeraldsMC! §b§l« §b§l» §e§lHUB! §b§l«";
                break;
        }

        e.setMotd(motd);
        e.setMaxPlayers(100);

    }
}
