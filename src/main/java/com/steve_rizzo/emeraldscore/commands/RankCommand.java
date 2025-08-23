package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {
    public static Permission perms = null;
    String prefix = Main.prefix;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        perms = Main.perms;

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

                        case "builder":

                        case "platinum":

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

                            setRankPlayer(p, target, rankName);
                            return true;

                        case "guest":

                            setRankPlayer(p, target, "default");
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

                if (rankName.equalsIgnoreCase("guest")) {
                    setRankConsole(target, "default");
                    return true;
                } else {
                    setRankConsole(target, rankName);
                    return true;
                }

            } else {

                System.out.println(prefix + "Incorrect usage. Use /rank <user> <rank> instead.");
                return true;
            }
        }

        return true;

    }
    //Update rank
    private void setRankPlayer(Player p, String target, String rank) {
        // Update player rank in console
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                "lp user " + target + " parent set " + rank);

        // Send message to command issuer and print to Console
        p.sendMessage(prefix + "user " + ChatColor.RED + target
                + ChatColor.GRAY + " has been updated to group "
                + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
        System.out.println(prefix + "user " + target + " has been updated to group "
                + rank.toUpperCase() + "!");

        // If target player is online, tell them that their rank has been updated
        Player targetPlayer = null;
        try {
            targetPlayer = Bukkit.getServer().getPlayer(target);
            assert targetPlayer != null;
            if (targetPlayer.isOnline()) {
                // Notify player that their rank has been updated
                targetPlayer.sendMessage(prefix + "your group has been updated to group "
                        + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
                // Set rank of player in tab
                Player tp = Bukkit.getServer().getPlayer(target);
                ServerJoinPlayer.ranks.updateAndSaveData(Bukkit.getServer().getPlayer(target));
            }
        } catch (NullPointerException ex) {
            System.out.println("[EmeraldsCore] Player doesn't exist for ranking.");
        }
    }

    // Command is issued via console
    private void setRankConsole(String target, String rank) {

        // Update player rank in console
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                "lp user " + target + " parent set " + rank);

        // Send message to Console
        System.out.println(prefix + "user " + target + " has been updated to group "
                + rank.toUpperCase() + "!");

        // If target player is online, tell them that their rank has been updated
        if ((Bukkit.getServer().getPlayer(target) != null)) {
            if (Bukkit.getServer().getPlayer(target).isOnline()) {
                Bukkit.getServer().getPlayer(target).sendMessage(prefix + "your group has been updated to group "
                        + ChatColor.AQUA + rank.toUpperCase() + ChatColor.GRAY + "!");
                ServerJoinPlayer.ranks.updateAndSaveData(Bukkit.getServer().getPlayer(target));
            }
        }
    }
}
