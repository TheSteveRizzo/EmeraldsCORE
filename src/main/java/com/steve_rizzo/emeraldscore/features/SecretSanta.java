package com.steve_rizzo.emeraldscore.features;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SecretSanta extends JavaPlugin implements Listener {

    private static Map<String, Inventory> secretSantaInventories = new HashMap<>();
    public static File santaFile;
    // Existing methods...

    private void saveSantaInventories() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(santaFile))) {
            oos.writeObject(secretSantaInventories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (secretSantaInventories.containsKey(player.getName())) {
            if (!isChristmasDay()) {
                event.setCancelled(true);
            } else {
                Inventory secretInventory = secretSantaInventories.get(player.getName());
                ItemStack clickedItem = event.getCurrentItem();

                // Check if the clicked item is from the player's secret Santa inventory
                if (clickedItem != null && secretInventory.contains(clickedItem)) {
                    // Allow the player to take items on Christmas Day
                    event.setCancelled(false);

                    // If the player takes all items, remove the inventory from the YAML file
                    if (isInventoryEmpty(secretInventory)) {
                        removeSantaInventory(player.getName());
                        player.sendMessage(Main.prefix + ChatColor.GRAY + "Your Secret Santa inventory is now empty.");
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean isInventoryEmpty(Inventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }

    private void removeSantaInventory(String playerName) {
        secretSantaInventories.remove(playerName);
        saveSantaInventories();
    }
    public boolean isValidItem(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    @SuppressWarnings("unchecked")
    public static void loadSantaInventories() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(santaFile))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                secretSantaInventories = (Map<String, Inventory>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Methods to handle offline players

    private OfflinePlayer getOfflinePlayer(String playerName) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(playerName)) {
                return offlinePlayer;
            }
        }
        return null;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (secretSantaInventories.containsKey(player.getName())) {
            secretSantaInventories.put(player.getName(), event.getInventory());
            saveSantaInventories();

            // Check if the player has a target player
            if (player.getMetadata("SecretSantaTarget").size() > 0) {
                OfflinePlayer targetPlayer = (OfflinePlayer) player.getMetadata("SecretSantaTarget").get(0).value();
                saveSantaInventoryForTarget(player.getName(), targetPlayer, event.getInventory());
            }
        }
    }

    private void saveSantaInventoryForTarget(String senderName, OfflinePlayer targetPlayer, Inventory inventory) {
        secretSantaInventories.put(targetPlayer.getName(), inventory);
        saveSantaInventories();
        Bukkit.getPlayer(senderName).sendMessage(Main.prefix + ChatColor.AQUA + "Secret Santa items saved for " + targetPlayer.getName());
    }

    private void openSecretSantaInventory(Player player, OfflinePlayer targetPlayer) {
        if (targetPlayer != null) {
            player.setMetadata("SecretSantaTarget", new FixedMetadataValue(this, targetPlayer));
            Inventory inventory = secretSantaInventories.getOrDefault(targetPlayer.getName(), Bukkit.createInventory(null, 27, "Secret Santa for " + targetPlayer.getName()));
            secretSantaInventories.put(targetPlayer.getName(), inventory);
            player.openInventory(inventory);
        } else {
            player.sendMessage(Main.prefix + ChatColor.RED + "Player is not found!");
        }
    }

    private boolean isChristmasDay() {
        LocalDate currentDate = LocalDate.now();
        return (currentDate.getMonthValue() == 12 && (currentDate.getDayOfMonth() == 24 || currentDate.getDayOfMonth() == 25 || currentDate.getDayOfMonth() == 26));
    }


    public void claimGift(Player player, OfflinePlayer targetPlayer) {
        if (isChristmasDay()) {
            if (targetPlayer != null && secretSantaInventories.containsKey(targetPlayer.getName())) {
                player.openInventory(secretSantaInventories.get(targetPlayer.getName()));
            } else {
                player.sendMessage(Main.prefix + ChatColor.RED + "You don't have any gifts to claim for this player.");
            }
        } else {
            player.sendMessage(Main.prefix + ChatColor.RED + "You can only claim gifts on December 24, 25, or 26.");
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.prefix + "Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("secretsanta")) {
            if (args.length == 1) {
                OfflinePlayer targetPlayer = getOfflinePlayer(args[0]);
                openSecretSantaInventory(player, targetPlayer);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("claim")) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(player.getName());
                claimGift(player, targetPlayer);
            } else {
                player.sendMessage(Main.prefix + ChatColor.RED + "Usage: " + ChatColor.AQUA + "/secretsanta <playerName>" + ChatColor.RED + " or " +
                        ChatColor.AQUA + "/secretsanta claim");
            }
        }

        return true;
    }
}
