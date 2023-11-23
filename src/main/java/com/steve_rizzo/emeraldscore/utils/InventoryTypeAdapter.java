package com.steve_rizzo.emeraldscore.utils;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InventoryTypeAdapter implements JsonSerializer<Inventory>, JsonDeserializer<Inventory> {

    @Override
    public JsonElement serialize(Inventory src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject serializedInventory = new JsonObject();
        JsonArray itemsArray = new JsonArray();

        for (ItemStack item : src.getContents()) {
            if (item != null) {
                JsonObject serializedItem = new JsonObject();
                serializedItem.addProperty("type", item.getType().name());
                serializedItem.addProperty("amount", item.getAmount());

                // Add any other necessary properties of ItemStack

                itemsArray.add(serializedItem);
            }
        }

        serializedInventory.add("items", itemsArray);
        return serializedInventory;
    }

    @Override
    public Inventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject serializedInventory = json.getAsJsonObject();
        JsonArray itemsArray = serializedInventory.getAsJsonArray("items");

        Inventory inventory = Bukkit.createInventory(null, 54, "Inventory");

        for (JsonElement element : itemsArray) {
            JsonObject serializedItem = element.getAsJsonObject();
            String itemType = serializedItem.get("type").getAsString();
            int amount = serializedItem.get("amount").getAsInt();

            // Construct ItemStack from serialized data
            ItemStack item = new ItemStack(org.bukkit.Material.valueOf(itemType), amount);

            // Add any other necessary properties to the ItemStack

            inventory.addItem(item);
        }

        return inventory;
    }
}
