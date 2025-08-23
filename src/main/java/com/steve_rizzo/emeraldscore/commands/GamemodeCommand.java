package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (!checkPermission(p)) {
                p.sendMessage(Main.prefix + ChatColor.RED + "No permission!");
                return true;
            }

            if (args.length == 1) {

                if (args[0].equalsIgnoreCase("c") || (args[0].equalsIgnoreCase("creative"))) {

                    p.sendMessage(Main.prefix + ChatColor.GRAY + "Set gamemode to "
                            + ChatColor.AQUA + "CREATIVE" + ChatColor.GRAY + "!");
                    p.setGameMode(GameMode.CREATIVE);

                    return true;


                } else if (args[0].equalsIgnoreCase("s") || (args[0].equalsIgnoreCase("survival"))) {

                    p.sendMessage(Main.prefix + ChatColor.GRAY + "Set gamemode to "
                            + ChatColor.AQUA + "SURVIVAL" + ChatColor.GRAY + "!");
                    p.setGameMode(GameMode.SURVIVAL);

                    return true;


                } else if (args[0].equalsIgnoreCase("a") || (args[0].equalsIgnoreCase("adventure"))) {

                    p.sendMessage(Main.prefix + ChatColor.GRAY + "Set gamemode to "
                            + ChatColor.AQUA + "ADVENTURE" + ChatColor.GRAY + "!");
                    p.setGameMode(GameMode.ADVENTURE);

                    return true;


                } else if (args[0].equalsIgnoreCase("spectator")) {

                    p.sendMessage(Main.prefix + ChatColor.GRAY + "Set gamemode to "
                            + ChatColor.AQUA + "SPECTATOR" + ChatColor.GRAY + "!");
                    p.setGameMode(GameMode.SPECTATOR);

                    return true;
                }
            }

            if (args.length == 2) {

                String gameMode = args[0].toUpperCase();
                String playerName = args[1];

                if (Bukkit.getServer().getPlayer(playerName) != null) {

                    Player tp = null;

                    tp = Bukkit.getServer().getPlayer(args[1]);

                    if (args[0].equalsIgnoreCase("c") || (args[0].equalsIgnoreCase("creative"))) {

                        p.sendMessage(Main.prefix + ChatColor.GRAY + "Set " + ChatColor.AQUA + tp.getName() + ChatColor.GRAY
                                + "'s gamemode to " + ChatColor.AQUA + "CREATIVE" + ChatColor.GRAY + "!");

                        tp.setGameMode(GameMode.CREATIVE);
                        tp.sendMessage(Main.prefix + ChatColor.GRAY + "Your gamemode has been set to " + ChatColor.AQUA
                                + "CREATIVE" + ChatColor.GRAY + "!");

                        return true;

                    } else if (args[0].equalsIgnoreCase("s") || (args[0].equalsIgnoreCase("survival"))) {

                        p.sendMessage(Main.prefix + ChatColor.GRAY + "Set " + ChatColor.AQUA + tp.getName() + ChatColor.GRAY
                                + "'s gamemode to " + ChatColor.AQUA + "SURVIVAL" + ChatColor.GRAY + "!");

                        tp.setGameMode(GameMode.SURVIVAL);
                        tp.sendMessage(Main.prefix + ChatColor.GRAY + "Your gamemode has been set to " + ChatColor.AQUA
                                + "SURVIVAL" + ChatColor.GRAY + "!");

                        return true;

                    } else if (args[0].equalsIgnoreCase("a") || (args[0].equalsIgnoreCase("adventure"))) {

                        p.sendMessage(Main.prefix + ChatColor.GRAY + "Set " + ChatColor.AQUA + tp.getName() + ChatColor.GRAY
                                + "'s gamemode to " + ChatColor.AQUA + "ADVENTURE" + ChatColor.GRAY + "!");

                        tp.setGameMode(GameMode.ADVENTURE);
                        tp.sendMessage(Main.prefix + ChatColor.GRAY + "Your gamemode has been set to " + ChatColor.AQUA
                                + "ADVENTURE" + ChatColor.GRAY + "!");

                        return true;

                    } else if (args[0].equalsIgnoreCase("spectator")) {

                        p.sendMessage(Main.prefix + ChatColor.GRAY + "Set " + ChatColor.AQUA + tp.getName() + ChatColor.GRAY
                                + "'s gamemode to " + ChatColor.AQUA + "SPECTATOR" + ChatColor.GRAY + "!");

                        tp.setGameMode(GameMode.SPECTATOR);
                        tp.sendMessage(Main.prefix + ChatColor.GRAY + "Your gamemode has been set to " + ChatColor.AQUA
                                + "SPECTATOR" + ChatColor.GRAY + "!");

                        return true;

                    }

                } else {

                    p.sendMessage(Main.prefix + ChatColor.GRAY + "Player " + ChatColor.RED + args[1] + ChatColor.GRAY
                            + " could not be found.");
                    return true;
                }
            }

            p.sendMessage(Main.prefix + ChatColor.GRAY + "Use: " + ChatColor.AQUA + "/gm <c/s/a/spectator>");
            return true;
        }

        return false;
    }


    private boolean checkPermission(Player player) {
        if (player.hasPermission("emeraldsmc.gamemode")) return true;
        return false;
    }
}
