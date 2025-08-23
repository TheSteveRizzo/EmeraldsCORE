package com.steve_rizzo.emeraldscore.features.miningpouch;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class PouchPreCraft implements Listener {
    @EventHandler
    public void pouchPreCraft(PrepareItemCraftEvent event) {

        if (areRecipesEqual(event.getRecipe(), Main.pouchRecipe)) {

            HumanEntity human = event.getView().getPlayer();

            if (!human.hasPermission("emeraldsmc.pouch")) {
                // basically cancels the event
                event.getInventory().setResult(null);
                // need to check because it could be NPCs or something
                if (human instanceof Player) {

                    Player player = (Player) human;
                    player.sendMessage(ChatColor.RED + "You must be a donor to craft this!");
                }
            }
        }
    }

    public static boolean areRecipesEqual(Recipe recipe1, Recipe recipe2) {
        if (recipe1 instanceof ShapedRecipe && recipe2 instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe1 = (ShapedRecipe) recipe1;
            ShapedRecipe shapedRecipe2 = (ShapedRecipe) recipe2;

            // Compare recipe result
            ItemStack result1 = shapedRecipe1.getResult();
            ItemStack result2 = shapedRecipe2.getResult();

            return result1.equals(result2); // Compare ItemStacks for similarity
        }

        return false; // If recipes are not of the same type or not equal
    }
}
