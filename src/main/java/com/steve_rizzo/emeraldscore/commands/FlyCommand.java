package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    String prefix = Main.prefix;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefix = Main.prefix;

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (Main.serverIDName.equalsIgnoreCase("bed")) return true;

            if (p.hasPermission("emeralds.setflight.self") || (p.hasPermission("emeralds.setflight.other"))) {

                if (args.length == 0) {

                    if (p.hasPermission("emeralds.setflight.self")) {

                        if (!p.getAllowFlight()) {
                            p.setAllowFlight(true);
                            p.sendMessage(prefix + ChatColor.GREEN + "Flight enabled.");
                        } else {
                            p.setAllowFlight(false);
                            p.sendMessage(prefix + ChatColor.GREEN + "Flight disabled.");
                        }
                        return true;

                    } else {

                        p.sendMessage(prefix + ChatColor.RED + "No permissions.");
                        return true;
                    }
                }

                if (args.length == 1) {

                    if (p.hasPermission("emeralds.setflight.other")) {

                        Player tp = null;
                        try {
                            tp = Bukkit.getPlayer(args[0]);
                        } catch (NullPointerException ex) {
                            p.sendMessage(prefix + ChatColor.RED + "Error. Target could not be found.");
                            return true;
                        }

                        if ((tp != null) && (tp.isOnline())) {
                            if (!tp.getAllowFlight()) {
                                tp.setAllowFlight(true);
                                tp.sendMessage(prefix + ChatColor.GREEN + "Flight enabled.");
                                p.sendMessage(prefix + ChatColor.GREEN + "Flight enabled for " + args[0] + ChatColor.GREEN + ".");
                                return true;
                            } else {
                                tp.setAllowFlight(false);
                                tp.sendMessage(prefix + ChatColor.GREEN + "Flight disabled.");
                                p.sendMessage(prefix + ChatColor.GREEN + "Flight disabled for " + args[0] + ChatColor.GREEN + ".");
                                return true;
                            }
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + "Error. Target is not online.");
                            return true;
                        }

                    } else {

                        p.sendMessage(prefix + ChatColor.RED + "No permissions.");
                        return true;
                    }
                }

                p.sendMessage(prefix + ChatColor.AQUA + "Incorrect usage. Use /fly or /fly <user>");
                return true;

            } else {

                p.sendMessage(prefix + ChatColor.RED + "No permissions.");
                return true;
            }

        } else {

            if (args.length == 1) {

                Player tp = null;
                try {
                    tp = Bukkit.getPlayer(args[0]);
                } catch (NullPointerException ex) {
                    System.out.println(ChatColor.stripColor(prefix) + "Error. Target could not be found.");
                    return true;
                }

                if (tp != null) {
                    tp.setAllowFlight(true);
                    tp.sendMessage(prefix + ChatColor.GREEN + "Flight enabled.");
                    System.out.println(ChatColor.stripColor(prefix) + "Flight enabled for " + args[0] + ".");
                    return true;
                }

            } else {

                System.out.println(ChatColor.stripColor(prefix) + "Incorrect usage. Use /fly <user>");
                return true;

            }
        }

        return true;
    }
}
