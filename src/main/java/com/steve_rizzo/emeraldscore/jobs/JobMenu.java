package com.steve_rizzo.emeraldscore.jobs;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JobMenu implements Listener {

    static String prefix = Main.prefix;

    // Open the main job selection menu
    public static void openJobSelectionMenu(Player player) {
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null && JobAPI.isPlayerInCooldown(jobPlayer)) {
            // Player is in cooldown, notify them and prevent opening the menu
            player.sendMessage(prefix + ChatColor.RED + "You are currently in cooldown and cannot change your job yet.");
            return;
        }

        Inventory jobMenu = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Job Selection");

        for (JobAPI.JOB_TYPE jobType : JobAPI.JOB_TYPE.values()) {
            ItemStack jobItem = getJobIcon(jobType);
            jobMenu.addItem(jobItem);
        }

        player.openInventory(jobMenu);
    }

    // Get the icon for the specified job type
    private static ItemStack getJobIcon(JobAPI.JOB_TYPE jobType) {
        Material material;
        String displayName;
        switch (jobType) {
            case FARMER:
                material = Material.CARROT;
                displayName = ChatColor.YELLOW + "Farmer";
                break;
            case MINER:
                material = Material.DIAMOND_PICKAXE;
                displayName = ChatColor.YELLOW + "Miner";
                break;
            case GATHERER:
                material = Material.CHEST;
                displayName = ChatColor.YELLOW + "Gatherer";
                break;
            case HUNTER:
                material = Material.DIAMOND_SWORD;
                displayName = ChatColor.YELLOW + "Hunter";
                break;
            case EXPLORER:
                material = Material.SPYGLASS;
                displayName = ChatColor.YELLOW + "Explorer";
                break;
            case FISHER:
                material = Material.FISHING_ROD;
                displayName = ChatColor.YELLOW + "Fisher";
                break;
            default:
                material = Material.STONE;
                displayName = ChatColor.YELLOW + "Unknown Job";
                break;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    // Open the task selection menu based on the selected job type
    public static void openTaskSelectionMenu(Player player, JobAPI.JOB_TYPE jobType) {
        Inventory taskMenu = Bukkit.createInventory(null, 9, ChatColor.BLUE + jobType.toString() + " Tasks");

        for (int i = 1; i <= 3; i++) {
            ItemStack taskItem = new ItemStack(Material.GOLD_NUGGET, 1);
            taskItem.getItemMeta().setDisplayName(ChatColor.YELLOW + "Task " + i);
            taskMenu.addItem(taskItem);
        }

        ItemStack backButton = new ItemStack(Material.ARROW, 1);
        backButton.getItemMeta().setDisplayName(ChatColor.RED + "Back to Job Selection");
        taskMenu.setItem(8, backButton);

        player.openInventory(taskMenu);
    }

    // Handle inventory clicks
    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedInventory != null && clickedItem != null) {
            String inventoryTitle = ChatColor.stripColor(event.getView().getTitle());

            if (inventoryTitle.equals("Job Selection")) {
                event.setCancelled(true);

                for (JobAPI.JOB_TYPE jobType : JobAPI.JOB_TYPE.values()) {
                    if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()
                            && ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals(jobType.toString())) {
                        // Player clicked on a job type
                        openTaskSelectionMenu(player, jobType);
                        break;
                    }
                }
            } else if (inventoryTitle.endsWith("Tasks")) {
                event.setCancelled(true);

                if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                    String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

                    if (itemName.equals("Back to Job Selection")) {
                        // Player clicked on the back button
                        openJobSelectionMenu(player);
                    } else {
                        // Handle task selection logic based on the selected job type
                        player.sendMessage(prefix + "You selected job task: " + itemName);
                    }
                }
            }
        }
    }
}
