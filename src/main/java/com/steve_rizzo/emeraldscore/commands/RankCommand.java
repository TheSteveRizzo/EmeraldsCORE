package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {
    public static Permission perms = null;
    private final Main serverEssentials;
    String prefix = Main.prefix;

    public RankCommand(Main serverEssentials) {
        this.serverEssentials = serverEssentials;
        perms = Main.perms;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (args.length == 2) {

                String target = args[0];
                String rankName = args[1];

                String rankOfIssuer = Main.perms.getPrimaryGroup(p);

                boolean isStaff = rankOfIssuer.equalsIgnoreCase("owner") ||
                        (rankOfIssuer.equalsIgnoreCase("admin")) ||
                        (rankOfIssuer.equalsIgnoreCase("mod") ||
                                (rankOfIssuer.equalsIgnoreCase("helper")));
                if (isStaff) {

                    switch (rankName.toLowerCase()) {

                        case "owner":

                            if (rankOfIssuer.equalsIgnoreCase("owner")) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;

                            }

                        case "admin":

                            if (rankOfIssuer.equalsIgnoreCase("owner") ||
                                    (rankOfIssuer.equalsIgnoreCase("admin"))) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;

                            }

                        case "mod":

                            if (rankOfIssuer.equalsIgnoreCase("owner") ||
                                    (rankOfIssuer.equalsIgnoreCase("admin") ||
                                            (rankOfIssuer.equalsIgnoreCase("mod")))) {

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

                            if (rankOfIssuer.equalsIgnoreCase("owner") ||
                                    (rankOfIssuer.equalsIgnoreCase("admin")) ||
                                    (rankOfIssuer.equalsIgnoreCase("mod"))) {

                                setRankPlayer(p, target, rankName);
                                return true;

                            } else {

                                p.sendMessage(prefix + ChatColor.RED + "No permission.");
                                return true;
                            }

                        case "member":

                        case "guest":

                            setRankPlayer(p, target, rankName);
                            return true;

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
        System.out.println("DETAILS PL TARGET: " + target + " RANK: " + rank);

        // Update player rank in console
        serverEssentials.getServer().dispatchCommand(serverEssentials.getServer().getConsoleSender(),
                "user " + target);
        serverEssentials.getServer().dispatchCommand(serverEssentials.getServer().getConsoleSender(),
                "user setgroup " + rank);

        // Send message to command issuer and print to Console
        p.sendMessage(prefix + "user " + ChatColor.RED + target
                + ChatColor.GRAY + " has been updated to group "
                + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
        System.out.println(prefix + "user " + target + " has been updated to group "
                + rank.toUpperCase() + "!");

        // If target player is online, tell them that their rank has been updated
        Player targetPlayer = null;
        try {
            targetPlayer = serverEssentials.getServer().getPlayer(target);
            assert targetPlayer != null;
            if (targetPlayer.isOnline()) {
                // Notify player that their rank has been updated
                targetPlayer.sendMessage(prefix + "your group has been updated to group "
                        + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
                // Set rank of player in tab
                Player tp = serverEssentials.getServer().getPlayer(target);
                ServerJoinPlayer.setPlayerTabName(tp);
                ServerJoinPlayer.ranks.updateAndSaveData(serverEssentials.getServer().getPlayer(target));
            }
        } catch (NullPointerException ex) {
            System.out.println("[EmeraldsCore] Player doesn't exist for ranking.");
        }
    }

    // Command is issued via console
    private void setRankConsole(String target, String rank) {

        System.out.println("DETAILS CN TARGET: " + target + " RANK: " + rank);
        // Update player rank in console
        serverEssentials.getServer().dispatchCommand(serverEssentials.getServer().getConsoleSender(),
                "user " + target);
        serverEssentials.getServer().dispatchCommand(serverEssentials.getServer().getConsoleSender(),
                "user setgroup " + rank);

        // Set rank of player in tab
        Player tp = serverEssentials.getServer().getPlayer(target);

        // Send message to Console
        System.out.println(prefix + "user " + target + " has been updated to group "
                + rank.toUpperCase() + "!");

        // If target player is online, tell them that their rank has been updated
        if ((serverEssentials.getServer().getPlayer(target) != null)) {
            if (serverEssentials.getServer().getPlayer(target).isOnline()) {
                serverEssentials.getServer().getPlayer(target).sendMessage(prefix + "your group has been updated to group "
                        + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
                ServerJoinPlayer.setPlayerTabName(tp);
                ServerJoinPlayer.ranks.updateAndSaveData(serverEssentials.getServer().getPlayer(target));
            }
        }
    }
}
