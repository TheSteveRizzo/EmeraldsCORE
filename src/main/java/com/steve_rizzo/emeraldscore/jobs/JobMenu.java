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

import java.util.List;
import java.util.Map;
import java.util.Random;

public class JobMenu implements Listener {

    private static final String prefix = Main.prefix;
    private final JobTasks jobTasks;

    // Constructor to initialize job tasks
    public JobMenu() {
        jobTasks = new JobTasks();
        jobTasks.scheduleDailyReset(); // Schedule the daily reset of tasks
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
                displayName = ChatColor.YELLOW + "Invalid Job";
                break;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    // Open the task selection menu based on the selected job type and player
    public void openTaskSelectionMenu(Player player, JobAPI.JOB_TYPE jobType) {
        Inventory taskMenu = Bukkit.createInventory(null, 9, ChatColor.AQUA + jobType.toString() + ChatColor.GRAY + " Tasks");

        // Get the randomly generated subset of tasks for the player's job type
        List<DailyTask> tasks = jobTasks.getPlayerJobTasks(player.getName(), jobType);
        if (tasks != null) {
            // Add each task to the inventory
            for (DailyTask task : tasks) {
                ItemStack taskItem = new ItemStack(Material.GOLD_NUGGET, 1);
                ItemMeta taskMeta = taskItem.getItemMeta();
                taskMeta.setDisplayName(ChatColor.YELLOW + task.getName());
                taskItem.setItemMeta(taskMeta);
                taskMenu.addItem(taskItem);
            }
        }

        // Add back button
        ItemStack backButton = new ItemStack(Material.ARROW, 1);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "Back to Job Selection");
        backButton.setItemMeta(backMeta);
        taskMenu.setItem(8, backButton);

        // Open the inventory for the player
        player.openInventory(taskMenu);
    }


    // Open the main job selection menu
    public static void openJobSelectionMenu(Player player) {
        Inventory jobMenu = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Job Selection");

        for (JobAPI.JOB_TYPE jobType : JobAPI.JOB_TYPE.values()) {
            ItemStack jobItem = getJobIcon(jobType);
            jobMenu.addItem(jobItem);
        }

        player.openInventory(jobMenu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedInventory != null && clickedItem != null) {
            String inventoryTitle = ChatColor.stripColor(event.getView().getTitle());

            if (inventoryTitle.equals("Job Selection")) {
                event.setCancelled(true);
                // Check if the clicked item is a job icon
                for (JobAPI.JOB_TYPE jobType : JobAPI.JOB_TYPE.values()) {
                    if (clickedItem.getType() == getJobMaterial(jobType)) {
                        // Check cooldown before allowing job change
                        if (JobAPI.isPlayerInCooldown(player.getName())) {
                            player.sendMessage(ChatColor.RED + "You are currently in cooldown and cannot change your job yet.");
                            return;
                        }
                        // Save the player's job as the corresponding job type
                        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
                        if (jobPlayer != null) {
                            jobPlayer.setJob(jobType);
                            player.sendMessage(ChatColor.GREEN + "You are now a " + jobType.toString() + "!");
                        }
                        // Open task selection menu for the corresponding job type
                        openTaskSelectionMenu(player, jobType);
                        // Save cooldown data to file
                        JobAPI.saveCooldownData();
                        return;
                    }
                }
            }
        }
    }

    private Material getJobMaterial(JobAPI.JOB_TYPE jobType) {
        switch (jobType) {
            case FARMER:
                return Material.CARROT;
            case MINER:
                return Material.DIAMOND_PICKAXE;
            case GATHERER:
                return Material.CHEST;
            case HUNTER:
                return Material.DIAMOND_SWORD;
            case EXPLORER:
                return Material.SPYGLASS;
            case FISHER:
                return Material.FISHING_ROD;
            default:
                return Material.STONE;
        }
    }

    @EventHandler
    public void onInventoryItemClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedInventory != null && clickedItem != null) {
            String inventoryTitle = ChatColor.stripColor(event.getView().getTitle());

            if (inventoryTitle.endsWith("Tasks")) {
                event.setCancelled(true);

                if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                    String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

                    if (itemName.equals("Back to Job Selection")) {
                        // Player clicked on the back button
                        openJobSelectionMenu(player);
                    } else {
                        // Handle task selection logic based on the selected job type
                        String taskId = ""; // Retrieve the task ID based on the clicked item
                        DailyTask task = getTaskById(taskId);
                        if (task != null && task.isCompleted() && !task.isClaimed()) {
                            // Task has been completed but not yet claimed
                            // Apply rewards to the player
                            JobRewards.applyReward(player.getName(), jobPlayer.getJob(), task.getTaskId());
                            // Mark the task as claimed
                            task.setClaimed(true);
                            // Change the icon to BEDROCK and update display name
                            ItemStack completedItem = new ItemStack(Material.BEDROCK);
                            ItemMeta meta = completedItem.getItemMeta();
                            meta.setDisplayName(ChatColor.GREEN + "Completed and Claimed");
                            completedItem.setItemMeta(meta);
                            clickedItem.setType(Material.BEDROCK);
                            clickedItem.setItemMeta(meta);
                            // Inform the player that the task has been completed and claimed
                            player.sendMessage(prefix + "You've completed and claimed the reward for this task!");
                        } else if (task != null && task.isClaimed()) {
                            // Task has already been claimed
                            player.sendMessage(prefix + "You have already claimed the reward for this task.");
                        } else {
                            // Task has not been completed
                            player.sendMessage(prefix + "You have not completed this task yet.");
                        }
                    }
                }
            }
        }
    }

    private DailyTask getTaskById(String taskId) {
        // Check if the taskId is a valid integer
        try {
            int id = Integer.parseInt(taskId);
            // Iterate over each job type and its associated tasks
            for (Map.Entry<JobAPI.JOB_TYPE, List<DailyTask>> entry : jobTasks.getJobTasks().entrySet()) {
                // Iterate over tasks for the current job type
                for (DailyTask task : entry.getValue()) {
                    // Check if the task ID matches the specified ID
                    if (task.getTaskId() == id) {
                        return task; // Return the task if found
                    }
                }
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return null; // Return null if no task with the specified ID is found
    }
}