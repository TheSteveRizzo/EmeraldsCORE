package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.PingServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaintenanceMode implements CommandExecutor {

    static boolean isInMaintMode = false;

    public static void setMaintenanceMode() {

        isInMaintMode = true;

        Main.core.getServer().broadcastMessage(Main.prefix + ChatColor.DARK_PURPLE + "SERVER HAS ENTERED MAINTENANCE MODE.");

        for (Player all : Main.core.getServer().getOnlinePlayers()) {
            if (!all.hasPermission("emeraldsmc.maintenancemode")) {
                all.kickPlayer(Main.prefix + ChatColor.DARK_PURPLE + "The server has entered MAINTENANCE MODE. Check back soon!");
            }
        }

        String maintMOTD = "§b§l» §a§lEmeraldsMC | §7§l(§5§lMAINT. MODE§7§l) §b§l«";
        Main.core.getServer().setWhitelist(true);
        PingServer.overrideMOTD(maintMOTD);

    }

    public static void endMaintenanceMode() {

        isInMaintMode = false;

        Main.core.getServer().broadcastMessage(Main.prefix + ChatColor.DARK_PURPLE + "SERVER HAS EXITED MAINTENANCE MODE.");

        Main.core.getServer().setWhitelist(false);

        String defaultMOTD = "§b§l» §a§lWelcome to §c§lplay.emeraldsmc.com §a§lSMP! §b§l«\n§b§l» §e§lMcMMO, LANDS, SHOPS, PVP, +MORE§b§l «";

        PingServer.overrideMOTD(defaultMOTD);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission("emeraldsmc.maintenancemode")) {

                if (!isInMaintMode) {
                    setMaintenanceMode();
                    return true;
                } else {
                    endMaintenanceMode();
                    return true;
                }

            } else {

                p.sendMessage(Main.prefix + ChatColor.RED + "No Permission!");
                return true;

            }

        } else {

            if (!isInMaintMode) {
                setMaintenanceMode();
                return true;
            } else {
                endMaintenanceMode();
                return true;
            }
        }
    }
}
