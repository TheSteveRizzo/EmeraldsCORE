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

                if (p.hasPermission("se.rank")) {

                    switch (rankName.toLowerCase()) {

                        case "owner":

                            if (p.hasPermission("se.rank.owner")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;

                            }

                        case "admin":

                            if (p.hasPermission("se.rank.admin")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;

                            }

                        case "mod":

                            if (p.hasPermission("se.rank.mod")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "helper":

                            if (p.hasPermission("se.rank.helper")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "youtuber":

                            if (p.hasPermission("se.rank.youtuber")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "elite":

                            if (p.hasPermission("se.rank.elite")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "donor5":

                            if (p.hasPermission("se.rank.donor5")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "donor4":

                            if (p.hasPermission("se.rank.donor4")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "donor3":

                            if (p.hasPermission("se.rank.donor3")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "donor2":

                            if (p.hasPermission("se.rank.donor2")) {
                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "donor1":

                            if (p.hasPermission("se.rank.donor1")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "member":

                            if (p.hasPermission("se.rank.member")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "guest":

                            if (p.hasPermission("se.rank.guest")) {

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

    private void setRankPlayer(Player p, String target, String rank) {

        //Update rank
        if (serverEssentials.getServer().getPlayer(target) != null) {

            Player tp = serverEssentials.getServer().getPlayer(target);

            String command = "setgroup" + " " + target + " " + rank;
            serverEssentials.getServer().dispatchCommand(serverEssentials.getServer().getConsoleSender(), command);

            ServerJoinPlayer.setPlayerTabName(tp);

        } else {
            p.sendMessage(prefix + ChatColor.RED + "Could not find player " + ChatColor.AQUA + target + ChatColor.RED + "!");
            return;
        }

        p.sendMessage(prefix + "user " + ChatColor.RED + target
                + ChatColor.GRAY + " has been updated to group "
                + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");

        if (serverEssentials.getServer().getPlayer(target).isOnline()) {
            serverEssentials.getServer().getPlayer(target).sendMessage(prefix + "your group has been updated to group "
                    + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
        }
    }

    //SWITCH TO VAULT
    private void setRankConsole(String target, String rank) {

        //Update rank
        if (serverEssentials.getServer().getPlayer(target) != null) {
            Player tp = serverEssentials.getServer().getPlayer(target);

            String command = "setgroup" + " " + target + " " + rank;
            serverEssentials.getServer().dispatchCommand(serverEssentials.getServer().getConsoleSender(), command);
            ServerJoinPlayer.setPlayerTabName(tp);

            System.out.println(prefix + "user " + target + " has been updated to group " + rank.toUpperCase() + "!");

            if (serverEssentials.getServer().getPlayer(target).isOnline()) {
                serverEssentials.getServer().getPlayer(target).sendMessage(prefix + "your group has been updated to group "
                        + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
            }

        } else {

            System.out.println(prefix + "Could not find player " + target + "!");

        }
    }
}
