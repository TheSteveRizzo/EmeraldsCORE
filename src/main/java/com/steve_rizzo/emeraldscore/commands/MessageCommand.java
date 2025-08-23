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

            String senderName = ServerJoinPlayer.getPlayerPrefixAndName(player.getName());
            String targetName = ServerJoinPlayer.getPlayerPrefixAndName(target.getName());

            StringBuilder messageArgs = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                messageArgs.append(args[i]).append(" ");
            }

            String msgToTarget = ChatColor.GRAY + "[" + ChatColor.GREEN + "EmeraldsMC" + ChatColor.GRAY + "]: ("
                    + ChatColor.YELLOW + senderName + ChatColor.GRAY + ") -> (" + ChatColor.YELLOW + "YOU" + ChatColor.GRAY + "): "
                    + messageArgs.toString().trim();

            String msgToSender = prefix + ChatColor.GRAY + "(" + ChatColor.YELLOW + "YOU" + ChatColor.GRAY + ") -> ("
                    + ChatColor.YELLOW + targetName + ChatColor.GRAY + "): "
                    + messageArgs.toString().trim();

            target.sendMessage(msgToTarget);
            player.sendMessage(msgToSender);

            lastMessageSender.put(target.getUniqueId(), player.getUniqueId());
            lastMessageSender.put(player.getUniqueId(), target.getUniqueId());

            System.out.println(msgToTarget);
            return true;

        } else if (command.getName().equalsIgnoreCase("reply")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(prefix + ChatColor.RED + "Only players can use this command.");
                return true;
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(prefix + ChatColor.RED + "Usage: /r <message>");
                return true;
            }

            UUID replyTargetUUID = lastMessageSender.get(player.getUniqueId());

            if (replyTargetUUID == null) {
                player.sendMessage(prefix + ChatColor.RED + "Cannot find a message to reply to.");
                return true;
            }

            Player target = getServer().getPlayer(replyTargetUUID);
            if (target == null || !target.isOnline()) {
                player.sendMessage(prefix + ChatColor.RED + "The player you want to reply to is no longer online.");
                return true;
            }

            String senderName = ServerJoinPlayer.getPlayerPrefixAndName(player.getName());
            String targetName = ServerJoinPlayer.getPlayerPrefixAndName(target.getName());

            StringBuilder messageArgs = new StringBuilder();
            for (String arg : args) {
                messageArgs.append(arg).append(" ");
            }

            String msgToTarget = ChatColor.GRAY + "[" + ChatColor.GREEN + "EmeraldsMC" + ChatColor.GRAY + "]: ("
                    + ChatColor.YELLOW + senderName + ChatColor.GRAY + ") -> (" + ChatColor.YELLOW + "YOU" + ChatColor.GRAY + "): "
                    + messageArgs.toString().trim();

            String msgToSender = prefix + ChatColor.GRAY + "(" + ChatColor.YELLOW + "YOU" + ChatColor.GRAY + ") -> ("
                    + ChatColor.YELLOW + targetName + ChatColor.GRAY + "): "
                    + messageArgs.toString().trim();

            target.sendMessage(msgToTarget);
            player.sendMessage(msgToSender);

            Bukkit.getServer().getLogger().log(Level.INFO, msgToTarget);
            return true;
        }

        return true;
    }
}
