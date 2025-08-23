package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyspeedCommand implements CommandExecutor {

    String prefix = Main.prefix;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefix = Main.prefix;

        if (sender instanceof Player) {

            if (Main.serverIDName.equalsIgnoreCase("bed")) return true;

            Player p = (Player) sender;

            if (p.hasPermission("emeralds.flyspeed")) {

                if (args.length == 1) {

                    String speedGiven = args[0];

                    if (speedGiven.toString().equalsIgnoreCase("off")) {
                        p.setFlySpeed(0.1f);
                        p.sendMessage(prefix + ChatColor.AQUA + "Flight speed has been " + ChatColor.RED + "RESET");

                        return true;
                    }

                    try {

                        int x = Integer.parseInt(speedGiven);
                        float flySpeed = 0.1f;

                        if (x >= 1 && x <=10)  {
                            if (x == 10) {
                                flySpeed = Float.parseFloat("0.98f");
                            } else {
                                flySpeed = Float.parseFloat("0." + speedGiven + "f");
                            }
                        }

                        p.setFlySpeed(flySpeed);
                        p.sendMessage(prefix + ChatColor.AQUA + "Flight speed set. Now flying at speed " + ChatColor.RED + speedGiven);

                        return true;

                    } catch (Exception ex) {
                        p.sendMessage(prefix + ChatColor.AQUA + "Incorrect usage. Use /flyspeed [[1(Default)-10(Max)]");
                        return true;
                    }

                } else {
                    p.sendMessage(prefix + ChatColor.AQUA + "Incorrect usage. Use /flyspeed <speed>");
                    return true;
                }

            } else {

                p.sendMessage(prefix + ChatColor.RED + "No permissions.");
                return true;
            }

        }

        return true;
    }
}
