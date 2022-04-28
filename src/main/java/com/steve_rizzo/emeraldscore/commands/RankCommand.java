package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {

    private final Main serverEssentials;
    String prefix = Main.prefix;

    public RankCommand(Main serverEssentials) {
        this.serverEssentials = serverEssentials;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (args.length == 2) {

                String target = args[0];
                String rankName = args[1];

                if (p.hasPermission("emeraldsmc.rank")) {

                    switch (rankName.toLowerCase()) {

                        case "owner":

                            if (p.hasPermission("emeraldsmc.rank.owner")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;

                            }

                        case "admin":

                            if (p.hasPermission("emeraldsmc.rank.admin")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;

                            }

                        case "mod":

                            if (p.hasPermission("emeraldsmc.rank.mod")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "helper":

                            if (p.hasPermission("emeraldsmc.rank.helper")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "youtuber":

                            if (p.hasPermission("emeraldsmc.rank.youtuber")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "elite":

                            if (p.hasPermission("emeraldsmc.rank.elite")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }


                        case "donor4":

                            if (p.hasPermission("emeraldsmc.rank.donor4")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "donor3":

                            if (p.hasPermission("emeraldsmc.rank.donor3")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "donor2":

                            if (p.hasPermission("emeraldsmc.rank.donor2")) {
                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "donor1":

                            if (p.hasPermission("emeraldsmc.rank.donor1")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "member":

                            if (p.hasPermission("emeraldsmc.rank.member")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "guest":

                            if (p.hasPermission("emeraldsmc.rank.guest")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }
                    }

                } else {

                    p.sendMessage(prefix + ChatColor.RED + "No permission.");
                    return true;

                }

            } else {
                p.sendMessage(prefix + ChatColor.RED + "Incorrect usage. " + ChatColor.GRAY + "Use "
                        + ChatColor.AQUA + "/rank <user> <rank> " + ChatColor.RED + "instead" + ChatColor.GRAY + ".");
                return true;

            }

        } else {

            if (args.length == 2) {

                String target = args[0];
                String rankName = args[1];

                setRankConsole(target, rankName);

            } else {

                System.out.println(prefix + "Incorrect usage. Use /rank <user> <rank> instead.");
                return true;
            }
        }

        return true;

    }
    //Update rank
    private void setRankPlayer(Player p, String target, String rank) {

        // Grab user UUID in console
        serverEssentials.getServer().dispatchCommand(serverEssentials.getServer().getConsoleSender(), "user " + target);
        // Set user group in console
        serverEssentials.getServer().dispatchCommand(serverEssentials.getServer().getConsoleSender(), "user setgroup " + rank);
        // Set rank of player in tab
        Player tp = serverEssentials.getServer().getPlayer(target);
        ServerJoinPlayer.setPlayerTabName(tp);

        // Send message to command issuer and print to Console
        p.sendMessage(prefix + "user " + ChatColor.RED + target
                + ChatColor.GRAY + " has been updated to group "
                + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
        System.out.println(prefix + "user " + target + " has been updated to group "
                + rank.toUpperCase() + "!");

        // If target player is online, tell them that their rank has been updated
        if ((serverEssentials.getServer().getPlayer(target) != null)) {
            if (serverEssentials.getServer().getPlayer(target).isOnline()) {
                serverEssentials.getServer().getPlayer(target).sendMessage(prefix + "your group has been updated to group "
                        + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
            }
        }
    }

    // Command is issued via console
    private void setRankConsole(String target, String rank) {
        // Grab user UUID in console
        serverEssentials.getServer().dispatchCommand(serverEssentials.getServer().getConsoleSender(), "user " + target);
        // Set user group in console
        serverEssentials.getServer().dispatchCommand(serverEssentials.getServer().getConsoleSender(), "user setgroup " + rank);
        // Set rank of player in tab
        Player tp = serverEssentials.getServer().getPlayer(target);
        ServerJoinPlayer.setPlayerTabName(tp);

        // Send message to Console
        System.out.println(prefix + "user " + target + " has been updated to group "
                + rank.toUpperCase() + "!");

        // If target player is online, tell them that their rank has been updated
        if ((serverEssentials.getServer().getPlayer(target) != null)) {
            if (serverEssentials.getServer().getPlayer(target).isOnline()) {
                serverEssentials.getServer().getPlayer(target).sendMessage(prefix + "your group has been updated to group "
                        + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
            }
        }
    }
}
