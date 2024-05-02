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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobMenu implements Listener {

    private static final String prefix = Main.prefix;
    private static JobTasks jobTasks = null;

    // Constructor to initialize job tasks
    public JobMenu() {
        jobTasks = new JobTasks();
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

    // Inside the JobMenu class

    public static void openTaskSelectionMenu(Player player, JobAPI.JOB_TYPE jobType) {
        Inventory taskMenu = Bukkit.createInventory(null, 9, ChatColor.AQUA + jobType.toString() + ChatColor.GRAY + " Tasks");

        // Clear the map before populating it with new task items
        TaskItemManager.clear();

        // Get the randomly generated subset of tasks for the player's job type
        List<DailyTask> tasks = jobTasks.getPlayerJobTasks(player.getName(), jobType);
        if (tasks != null) {
            // Add each task to the inventory
            for (DailyTask task : tasks) {
                ItemStack taskItem = getItemStack(task, player, jobType.toString());
                TaskItemManager.addTaskItem(taskItem, task); // Store the task item and its corresponding DailyTask
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

    public static void updateTaskMenuItem(Player player, DailyTask task, String jobType) {
        String playerUUID = player.getUniqueId().toString();
        for (Map.Entry<ItemStack, DailyTask> entry : TaskItemManager.getTaskItemMap().entrySet()) {
            if (entry.getValue().equals(task)) {
                ItemStack taskItem = entry.getKey();
                ItemMeta taskMeta = taskItem.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + task.getDescription());

                if (task.isCompleted(playerUUID, jobType)) {
                    if (task.isClaimed(playerUUID, jobType)) {
                        // Task is completed and claimed, mark as bedrock and inform the player
                        taskItem.setType(Material.BEDROCK);
                        lore.add(ChatColor.GRAY + "Progress: " + ChatColor.GREEN + "Completed and Claimed");
                    } else {
                        // Task is completed but not claimed, allow the player to claim it
                        taskItem.setType(Material.EMERALD_BLOCK);
                        lore.add(ChatColor.GRAY + "Progress: " + ChatColor.YELLOW + "Completed");
                        lore.add(ChatColor.GREEN + "Right-click to claim reward");
                    }
                } else {
                    // Task is not completed yet
                    lore.add(ChatColor.GRAY + "Progress: " + ChatColor.YELLOW + task.getProgress(playerUUID, jobType) + "/" + task.getTotalProgress(playerUUID, jobType));
                }

                taskMeta.setLore(lore);
                taskItem.setItemMeta(taskMeta);
                // Update the task item in the inventory
                player.updateInventory();
                break;
            }
        }
    }

    public static void openTaskSelectorMenu(Player player) {
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null && jobPlayer.getJob() != JobAPI.JOB_TYPE.NONE) {
            openTaskSelectionMenu(player, jobPlayer.getJob());
        } else {
            player.sendMessage(ChatColor.RED + "You do not have any tasks available as you do not have a job set.");
        }
    }

    private static ItemStack getItemStack(DailyTask task, Player player, String jobType) {
        String playerUUID = player.getUniqueId().toString();
        ItemStack taskItem = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta taskMeta = taskItem.getItemMeta();
        taskMeta.setDisplayName(ChatColor.YELLOW + task.getName());

        // Set the lore to include the task description and dynamic progress
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + task.getDescription());

        if (task.isCompleted(playerUUID, jobType)) {
            if (task.isClaimed(playerUUID, jobType)) {
                // Task is completed and claimed
                taskItem.setType(Material.BEDROCK);
                lore.add(ChatColor.GRAY + "Progress: " + ChatColor.GREEN + "Completed and Claimed");
            } else {
                // Task is completed but not claimed
                taskItem.setType(Material.EMERALD_BLOCK);
                lore.add(ChatColor.GRAY + "Progress: " + ChatColor.YELLOW + "Completed");
                lore.add(ChatColor.GREEN + "Right-click to claim reward");
            }
        } else {
            // Task is not completed yet
            lore.add(ChatColor.GRAY + "Progress: " + ChatColor.YELLOW + task.getProgress(playerUUID, jobType) + "/" + task.getTotalProgress(playerUUID, jobType));
        }

        taskMeta.setLore(lore);
        taskItem.setItemMeta(taskMeta);
        return taskItem;
    }

    // Open the main job selection menu
    public static void openJobSelectionMenu(Player player) {
        Inventory jobMenu = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Job Selection");

        for (JobAPI.JOB_TYPE jobType : JobAPI.JOB_TYPE.values()) {
            // Exclude the NONE job type
            if (jobType != JobAPI.JOB_TYPE.NONE) {
                ItemStack jobItem = getJobIcon(jobType);
                jobMenu.addItem(jobItem);
            }
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

                        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());

                        if (jobPlayer == null) {
                            jobPlayer = new JobAPI.JobPlayer(player.getName(), JobAPI.JOB_TYPE.NONE);
                        }

                        JobCommands.openJobPlayerMenu(player, jobType);
                        player.sendMessage(prefix + ChatColor.DARK_AQUA + "Trying to switch to this job? Type: " + ChatColor.GRAY + "/job set " + jobType);
                        return;
                    }
                }
            }

            if (inventoryTitle.endsWith("] User Directory")) {

                event.setCancelled(true);

                // Check if the clicked item is the task button
                if (clickedItem.getType() == Material.DIAMOND && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                    openTaskSelectorMenu(player);
                    event.setCancelled(true); // Cancel the event to prevent further actions
                }


                if (clickedItem.getType() == Material.ARROW && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {

                    openJobSelectionMenu(player);
                    event.setCancelled(true);

                }

                if (clickedItem.getType() == Material.PLAYER_HEAD && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {

                    // Stats soon
                    event.setCancelled(true);

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
        String playerUUID = player.getUniqueId().toString();
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        String JOB_TYPE = jobPlayer.getJob().toString();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedInventory != null && clickedItem != null) {
            String inventoryTitle = ChatColor.stripColor(event.getView().getTitle());

            if (inventoryTitle.endsWith("Tasks")) {
                event.setCancelled(true);

                if (clickedItem.getType() == Material.EMERALD_BLOCK && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {

                    // Handle task selection logic based on the selected job type
                    String taskId = null;
                    for (Map.Entry<ItemStack, DailyTask> entry : TaskItemManager.getTaskItemMap().entrySet()) {
                        if (entry.getKey().equals(clickedItem)) {
                            taskId = String.valueOf(entry.getValue().getTaskId());
                            break;
                        }
                    }
                    DailyTask task = getTaskById(taskId);
                    if (task != null && task.isCompleted(playerUUID, JOB_TYPE) && !task.isClaimed(playerUUID, JOB_TYPE)) {
                        if (jobPlayer.getJob() != null) {
                            // Task has been completed but not yet claimed
                            // Apply rewards to the player
                            JobRewards.applyReward(player, jobPlayer.getJob(), task.getTaskId());
                            // Mark the task as claimed and completed
                            task.setClaimed(true, playerUUID, JOB_TYPE);
                            task.setCompleted(true, playerUUID, JOB_TYPE); // Mark the task as completed
                            // Update the inventory display to show the task as completed and claimed
                            updateTaskMenuItem(player, task, jobPlayer.getJob().toString());
                            // Inform the player that the task has been completed and claimed
                        } else if (task.isClaimed(playerUUID, JOB_TYPE)) {
                            // Task has already been claimed
                            player.sendMessage(prefix + "You have already claimed the reward for this task.");
                        } else {
                            // Task has not been completed
                            player.sendMessage(prefix + "You have not completed this task yet.");
                        }
                    }
                } else if (clickedItem.getType().equals(Material.ARROW)) {
                    String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                    if (itemName.equals("Back to Job Selection")) {
                        // Player clicked on the back button
                        openJobSelectionMenu(player);
                    }
                }
            }
        }
    }

    private DailyTask getTaskById(String taskId) {
        // Check if the taskId is not empty and is a valid integer
        if (taskId != null && !taskId.isEmpty()) {
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
        }
        return null; // Return null if taskId is empty or not a valid integer
    }
}
