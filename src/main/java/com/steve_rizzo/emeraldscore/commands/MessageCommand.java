package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;

public class MessageCommand implements CommandExecutor {

    String prefix = Main.prefix;
    private static final Map<UUID, UUID> lastMessageSender = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("message")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(prefix + ChatColor.RED + "Only players can use this command.");
                return true;
            }

            Player player = (Player) sender;

            if (args.length < 2) {
                player.sendMessage(prefix + ChatColor.RED + "Usage: " + ChatColor.AQUA + "/message <player> <message>");
                return true;
            }

            Player target = getServer().getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(prefix + ChatColor.RED + "Player not found or is not online.");
                return true;
            }

            if (player.getName().equalsIgnoreCase(target.getName())) {
                player.sendMessage(prefix + ChatColor.RED + "You cannot message yourself!");
                return true;
            }

            StringBuilder messageArgs = new StringBuilder();
            // Construct the message with the prefix
            StringBuilder message = new StringBuilder();
            message.append(ChatColor.GRAY).append("[").append(ChatColor.GREEN).append("EmeraldsMC").append(ChatColor.GRAY).append("]: (");
            message.append(ChatColor.YELLOW).append(ServerJoinPlayer.getPlayerPrefixAndName(player.getName())).append(ChatColor.GRAY).append(") -> (" + ChatColor.YELLOW + "YOU" + ChatColor.GRAY + "): ");
            for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(" ");
                messageArgs.append(args[i]).append(" ");
            }

            // Send the message to the target player
            target.sendMessage(message.toString());

            // Record the last message sender for both players
            lastMessageSender.put(target.getUniqueId(), player.getUniqueId());
            lastMessageSender.put(player.getUniqueId(), target.getUniqueId());

            // Inform the sender that the message was sent
            player.sendMessage(prefix + ChatColor.GRAY + "(" + ChatColor.YELLOW + "YOU" + ChatColor.GRAY + ") -> (" + ChatColor.YELLOW + ServerJoinPlayer.getPlayerPrefixAndName(target.getName()) + ChatColor.GRAY + "): " + messageArgs);

            // Print to console
            System.out.println(message);

            return true;

        } else if (command.getName().equalsIgnoreCase("reply")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(prefix + ChatColor.RED + "Only players can use this command.");
                return true;
            }

            Player p = (Player) sender;

            if (args.length == 0) {
                p.sendMessage(prefix + ChatColor.RED + "Usage: /r <message>");
                return true;
            }

            UUID replyTargetUUID = lastMessageSender.get(p.getUniqueId());

            if (replyTargetUUID != null) {
                Player target = getServer().getPlayer(replyTargetUUID);

                if (target != null && target.isOnline()) {
                    StringBuilder messageArgs = new StringBuilder();
                    // Construct the reply message with the prefix
                    StringBuilder reply = new StringBuilder();
                    reply.append(ChatColor.GRAY).append("[").append(ChatColor.GREEN).append("EmeraldsMC").append(ChatColor.GRAY).append("]: (");
                    reply.append(ChatColor.YELLOW).append(p.getName()).append(ChatColor.GRAY).append(") -> (" + ChatColor.YELLOW + "YOU" + ChatColor.GRAY + "): ");

                    for (String arg : args) {
                        reply.append(arg).append(" ");
                        messageArgs.append(arg).append(" ");
                    }

                    // Send the reply message to the target player
                    target.sendMessage(reply.toString().toString().trim());

                    // Inform the sender that the reply was sent
                    p.sendMessage(prefix + ChatColor.GRAY + "(" + ChatColor.YELLOW + "YOU" + ChatColor.GRAY + ") -> (" + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "): " + messageArgs.toString().trim());

                    // Print to console
                    Bukkit.getServer().getLogger().log(Level.INFO, reply.toString().trim());

                } else {
                    p.sendMessage(prefix + ChatColor.RED + "The player you want to reply to is no longer online.");
                }

                return true;
            } else {
                p.sendMessage(prefix + ChatColor.RED + "Cannot find a message to reply to.");
                return true;
            }
        }
        return true;
    }
}
