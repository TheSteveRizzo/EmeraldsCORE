package com.steve_rizzo.emeraldscore.commands.tokens;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTokensCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.isOp()) {
                p.sendMessage(Main.prefix + ChatColor.RED + "Only the system can issue this command.");
                return true;
            } else {
                p.sendMessage(Main.prefix + ChatColor.RED + "No permission.");
                return true;
            }

        } else {
            if (args.length == 2) {

                String tarPlayerName = args[0];
                int amount = 0;

                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    // Not an integer
                    System.out.println(Main.prefix + ChatColor.RED + "Error: amount must be a positive number.");
                    return true;
                }

                if (amount >= 0) {


                    Player target = Bukkit.getServer().getPlayer(tarPlayerName);

                    if (target != null) {

                        if (Bukkit.getServer().getPlayer(tarPlayerName).isOnline()) {


                            TokensAPI.setTokensBalance(target, amount);
                            System.out.println(Main.prefix + ChatColor.GRAY + "You just set " + ChatColor.AQUA +
                                    tarPlayerName + ChatColor.GRAY + "'s balance to " + ChatColor.GREEN + amount + ChatColor.GRAY + " TOKENS.");
                            target.sendMessage(Main.prefix + ChatColor.GRAY + "Your TOKENS balance was set to: " + ChatColor.GREEN + amount + ChatColor.GRAY + ".");

                            return true;

                        } else {
                            System.out.println(Main.prefix + ChatColor.RED + "Error: player must be online.");
                            return true;
                        }

                    } else {
                        for (OfflinePlayer offlinePlayers : Bukkit.getServer().getOfflinePlayers()) {

                            if (tarPlayerName.equalsIgnoreCase(offlinePlayers.getName())) {

                                System.out.println(Main.prefix + ChatColor.GRAY + "You just set " + ChatColor.AQUA +
                                        tarPlayerName + ChatColor.GRAY + "'s balance to " + ChatColor.GREEN + amount + ChatColor.GRAY + " TOKENS.");

                                TokensAPI.setTokensBalanceUUID(offlinePlayers.getUniqueId().toString(), amount);

                                return true;

                            }
                        }

                        System.out.println(Main.prefix + ChatColor.RED + "Error: player has not played before.");
                        return true;
                    }
                }

            } else {
                System.out.println(Main.prefix + ChatColor.RED + "Usage: " + ChatColor.AQUA + "/settokens <player> <amount>");
                return true;
            }
        }
        return true;
    }
}
