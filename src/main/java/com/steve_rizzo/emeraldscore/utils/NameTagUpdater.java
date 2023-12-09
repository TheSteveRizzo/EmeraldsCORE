package com.steve_rizzo.emeraldscore.utils;

import com.comphenix.protocol.PacketType;
import org.bukkit.entity.Player;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class NameTagUpdater {
    public static void updatePlayerNameTag(Player player, String newName) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        if (protocolManager != null) {
            try {
                PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM, true);
                packet.getStrings().write(0, "emcore_playernames"); // Replace with a unique team name
                packet.getIntegers().write(0, 3); // Set mode 3 for team info update

                // Set the new display name
                WrappedChatComponent newNameComponent = WrappedChatComponent.fromText(newName);
                packet.getChatComponents().write(0, newNameComponent);

                protocolManager.sendServerPacket(player, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ProtocolManager is null. Ensure ProtocolLib is properly installed and enabled.");
        }
    }

}
