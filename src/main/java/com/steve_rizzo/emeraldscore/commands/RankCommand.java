package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import com.steve_rizzo.emeraldscore.utils.Ranks;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {

    private final Main serverEssentials;
    String prefix = Main.prefix;

    Ranks ranks = Main.ranks;

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

                            if (ranks.getRank(p).equalsIgnoreCase("owner")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;

                            }

                        case "admin":

                            if (ranks.getRank(p).equalsIgnoreCase("owner") ||
                                    (ranks.getRank(p).equalsIgnoreCase("admin"))) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;

                            }

                        case "mod":

                            if (ranks.getRank(p).equalsIgnoreCase("owner") ||
                                    (ranks.getRank(p).equalsIgnoreCase("admin") ||
                                            (ranks.getRank(p).equalsIgnoreCase("mod")))) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "helper":

                        case "youtuber":

                        case "elite":

                        case "donor4":

                        case "donor3":

                        case "donor2":

                        case "donor1":

                            if (ranks.getRank(p).equalsIgnoreCase("owner") ||
                                    (ranks.getRank(p).equalsIgnoreCase("admin")) ||
                                    (ranks.getRank(p).equalsIgnoreCase("mod"))) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "member":

                        case "guest":

                            if (ranks.getRank(p).equalsIgnoreCase("owner") ||
                                    (ranks.getRank(p).equalsIgnoreCase("admin")) ||
                                    (ranks.getRank(p).equalsIgnoreCase("mod") ||
                                            (ranks.getRank(p).equalsIgnoreCase("helper")))) {

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
