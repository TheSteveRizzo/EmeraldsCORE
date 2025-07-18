package com.steve_rizzo.emeraldscore.chat;

import com.steve_rizzo.emeraldscore.Main;
import github.scarsz.discordsrv.DiscordSRV;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class PrefixSender implements Listener, PluginMessageListener {

    private final Main core = Main.core;
    private final String serverID = Main.serverIDName;
    private final Chat chat = Main.chat;
    private final Set<String> processedMessages = new HashSet<>(); // Set to store unique message hashes

    public PrefixSender() {
        // Schedule a task to clear the processedMessages set every 5 minutes (6000 ticks)
        Bukkit.getScheduler().runTaskTimerAsynchronously(core, processedMessages::clear, 6000L, 6000L);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        // Skip processing if the message is a command
        if (event.getMessage().startsWith("/")) {
            return;
        }

        Player player = event.getPlayer();
        String playerName = player.getName();
        String prefix = chat.getPlayerPrefix(player).trim(); // Get prefix from Vault and trim extra spaces
        String message = event.getMessage().trim();

        // Format the message for global chat with serverID
        String formattedMessage = String.format("chat|%s|%s|%s|%s", serverID, prefix, playerName, message);

        // Send the formatted message to other servers via plugin messaging channel
        player.sendPluginMessage(core, "emeraldscore:chat", formattedMessage.getBytes(StandardCharsets.UTF_8));

        // Optionally: Broadcast the message locally
        String localFormattedMessage = String.format("&7[&b%s&7] %s%s&f: %s", serverID.toUpperCase(), prefix, playerName, message);
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', localFormattedMessage));

        // Send message through Discord SRV
        String discordMessage = String.format("**[%s] - [%s] %s »** %s", serverID.toUpperCase(), Main.perms.getPrimaryGroup(player).toUpperCase(), playerName, message);
        DiscordSRV.getPlugin().getMainGuild().getTextChannelById("1164588771392618566").sendMessage(discordMessage).queue();
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {

        // Check if the message is for the "emeraldscore:chat" channel
        if (!channel.equals("emeraldscore:chat")) {
            return;
        }

        // Decode the message
        String data = new String(bytes, StandardCharsets.UTF_8);
        String[] parts = data.split("\\|", 5); // Expecting 5 parts: type, originatingServerID, prefix, playerName, content

        // Ensure that the message format is valid
        if (parts.length < 5) {
            return;
        }

        // Decompose the message
        String type = parts[0];  // The type (should be "chat")
        String originatingServerID = parts[1]; // The server ID that sent this message
        String prefix = parts[2];
        String playerName = parts[3];
        String content = parts[4];

        // Only process if the type is "chat"
        if (type.equals("chat")) {

            // Ensure this message is not from the local server
            if (!originatingServerID.equalsIgnoreCase(serverID)) {
                // Create a unique hash for the message to avoid duplicates
                String messageHash = originatingServerID + prefix + playerName + content;

                // Check if the message has already been processed
                if (!processedMessages.contains(messageHash)) {
                    // Mark the message as processed
                    processedMessages.add(messageHash);

                    // Format and broadcast the message locally
                    String formattedMessage = String.format("&7[&b%s&7] %s%s&f: %s",
                            originatingServerID.toUpperCase(),
                            prefix,
                            playerName,
                            content);
                    // Broadcast the message to all players on this server
                    Bukkit.getScheduler().runTask(core, () -> {
                        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage));
                    });
                }
            }
        }
    }
}