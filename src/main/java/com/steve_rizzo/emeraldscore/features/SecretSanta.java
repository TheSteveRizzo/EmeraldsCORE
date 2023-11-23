package com.steve_rizzo.emeraldscore.features;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.utils.InventoryTypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecretSanta implements CommandExecutor, Listener {

    static Gson gson;
    public static Map<String, Inventory> secretSantaInventories = new HashMap<>();
    public static File santaFile;

    public SecretSanta() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Inventory.class, new InventoryTypeAdapter());
        gson = gsonBuilder.create();
    }

    private void saveSantaInventories() {
        Map<String, JsonArray> serializableInventories = new HashMap<>();

        // Iterate through the secretSantaInventories and convert Inventory contents to a serializable format
        for (Map.Entry<String, Inventory> entry : secretSantaInventories.entrySet()) {
            String playerName = entry.getKey();
            Inventory inventory = entry.getValue();

            // Convert inventory contents to a JSON array
            JsonArray serializedInventory = serializeInventory(inventory);

            // Store the serialized inventory in the map
            serializableInventories.put(playerName, serializedInventory);
        }

        try (Writer writer = new FileWriter(santaFile)) {
            gson.toJson(serializableInventories, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSantaInventories() {
        try (Reader reader = new FileReader(santaFile)) {
            Type inventoryMapType = new TypeToken<Map<String, JsonArray>>() {}.getType();
            Map<String, JsonArray> loadedInventories = gson.fromJson(reader, inventoryMapType);
            if (loadedInventories != null) {
                for (Map.Entry<String, JsonArray> entry : loadedInventories.entrySet()) {
                    String playerName = entry.getKey();
                    JsonArray serializedInventory = entry.getValue();

                    Inventory inventory = Bukkit.createInventory(null, 54, "Secret Santa for " + playerName);

                    // Deserialize the inventory from the JSON array
                    deserializeInventory(serializedInventory, inventory);

                    secretSantaInventories.put(playerName, inventory);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private JsonObject serializeItemStack(ItemStack itemStack) {
        JsonObject serializedItem = new JsonObject();

        serializedItem.addProperty("type", itemStack.getType().name());
        serializedItem.addProperty("amount", itemStack.getAmount());

        // Add more properties as needed, like durability, enchantments, etc.

        return serializedItem;
    }

    private JsonArray serializeInventory(Inventory inventory) {
        JsonArray serializedInventory = new JsonArray();

        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                JsonObject serializedItem = serializeItemStack(item);
                serializedInventory.add(serializedItem);
            }
        }

        return serializedInventory;
    }

    private static ItemStack deserializeItemStack(JsonObject serializedItem) {
        Material itemType = Material.getMaterial(serializedItem.get("type").getAsString());
        int amount = serializedItem.get("amount").getAsInt();

        // Create the ItemStack based on the deserialized data
        ItemStack itemStack = new ItemStack(itemType, amount);

        // Handle additional properties here if needed

        return itemStack;
    }

    private static void deserializeInventory(JsonArray serializedInventory, Inventory inventory) {
        for (JsonElement element : serializedInventory) {
            JsonObject serializedItem = element.getAsJsonObject();
            ItemStack itemStack = deserializeItemStack(serializedItem);

            // Add the deserialized ItemStack to the inventory
            inventory.addItem(itemStack);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (secretSantaInventories.containsKey(player.getName())) {
            // Check if the closed inventory is the Secret Santa inventory
            Inventory closedInventory = event.getInventory();
            Inventory secretInventory = secretSantaInventories.get(player.getName());

            if (closedInventory.equals(secretInventory)) {
                // Save the Secret Santa inventory
                secretSantaInventories.put(player.getName(), closedInventory);
                saveSantaInventories();

                // Check if the player has a target player
                if (player.getMetadata("SecretSantaTarget").size() > 0) {
                    OfflinePlayer targetPlayer = (OfflinePlayer) player.getMetadata("SecretSantaTarget").get(0).value();
                    saveSantaInventoryForTarget(player.getName(), targetPlayer, closedInventory);
                    player.sendMessage(Main.prefix + ChatColor.AQUA + "Gifted " + ChatColor.RED + "Secret Santa " + ChatColor.AQUA + "items to " + ChatColor.GREEN + targetPlayer.getName() + ChatColor.AQUA + "!");

                }
            }
        }
    }

    private void saveSantaInventoryForTarget(String playerName, OfflinePlayer targetPlayer, Inventory closedInventory) {
        Map<String, JsonArray> serializableInventories = new HashMap<>();

        // Convert inventory contents to a JSON array
        JsonArray serializedInventory = serializeInventory(closedInventory);

        // Store the serialized inventory in the map
        serializableInventories.put(playerName, serializedInventory);

        File targetDirectory = new File(santaFile.getParentFile(), "secretsanta");
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs(); // This line creates the directory if it doesn't exist
        }
        File targetFile = new File(targetDirectory, targetPlayer.getUniqueId() + ".json");

        // Ensure the target file is a JSON file
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile(); // Create the file if it doesn't exist
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (Writer writer = new FileWriter(targetFile)) {
            gson.toJson(serializableInventories, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[EmeraldsCore] Console Notice: Updated SecretSanta Data for " + playerName);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        if (secretSantaInventories.containsKey(player.getName())) {
            Inventory secretInventory = secretSantaInventories.get(player.getName());

            if (clickedInventory != null && clickedInventory.equals(secretInventory)) {
                // Ensure the player can take items only on Christmas Day
                if (!isChristmasDay()) {
                    event.setCancelled(true);
                } else {
                    ItemStack clickedItem = event.getCurrentItem();

                    if (clickedItem != null) {
                        // Check if the clicked item is from the player's secret Santa inventory
                        if (secretInventory.contains(clickedItem)) {
                            // Allow the player to take items on Christmas Day
                            event.setCancelled(false);

                            // If the player takes all items, remove the inventory from the map
                            if (isInventoryEmpty(secretInventory)) {
                                removeSantaInventory(player.getName());
                                player.sendMessage(Main.prefix + ChatColor.GRAY + "Your Secret Santa inventory is now empty.");
                            }
                        } else {
                            // Cancel moving items that aren't from the Secret Santa inventory
                            event.setCancelled(true);
                        }
                    }
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

    // Methods to handle offline players

    private OfflinePlayer getOfflinePlayer(String playerName) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(playerName)) {
                return offlinePlayer;
            }
        }
        return null;
    }


    private void openSecretSantaInventory(Player player, OfflinePlayer targetPlayer) {
        if (targetPlayer != null) {
            player.setMetadata("SecretSantaTarget", new FixedMetadataValue(Main.core, targetPlayer));
            Inventory inventory = secretSantaInventories.getOrDefault(targetPlayer.getName(), Bukkit.createInventory(null, 54, ChatColor.AQUA + "" + ChatColor.BOLD
                    + "Secret Santa for " + ChatColor.GREEN + "" + ChatColor.BOLD + targetPlayer.getName()));
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


    public void claimGift(Player player) {
        if (isChristmasDay()) {
            if (player != null && secretSantaInventories.containsKey(player.getName())) {
                player.openInventory(secretSantaInventories.get(player.getName()));
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
                if (args[0].equalsIgnoreCase("claim")) {
                    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(player.getName());
                    claimGift(player);
                } else {
                    OfflinePlayer targetPlayer = getOfflinePlayer(args[0]);
                    openSecretSantaInventory(player, targetPlayer);
                }
            } else {
                player.sendMessage(Main.prefix + ChatColor.RED + "Usage: " + ChatColor.AQUA + "/secretsanta <playerName>" + ChatColor.RED + " or " +
                        ChatColor.AQUA + "/secretsanta claim");
            }
        }

        return true;
    }
}
