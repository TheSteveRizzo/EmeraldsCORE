package com.steve_rizzo.emeraldscore.jobs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JobRewards {

    public static void applyReward(String playerUUID, JobAPI.JOB_TYPE jobType, int taskId) {
        // Get the JobTasks instance
        JobTasks jobTasks = new JobTasks();

        // Get the list of tasks for the specified job type assigned to the player
        List<DailyTask> tasks = jobTasks.getPlayerJobTasks(playerUUID, jobType);
        if (tasks != null) {
            // Iterate through the tasks to find the one with the specified task ID
            for (DailyTask task : tasks) {
                if (task.getTaskId() == taskId && task.isCompleted(playerUUID, jobType.toString())
                        && task.isClaimed(playerUUID, jobType.toString())) {
                    // Apply the reward if the task is completed and claimed
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player != null) {
                        // Apply the specific reward based on the task
                        switch (taskId) {
                            case 1:
                                // Apply reward for Task 1 of the corresponding job type
                                // For example:
                                applyTask1Reward(player, jobType);
                                break;
                            case 2:
                                // Apply reward for Task 2 of the corresponding job type
                                // For example:
                                applyTask2Reward(player, jobType);
                                break;
                            // Add cases for other task IDs as needed
                            case 3:
                                // Apply reward for Task 2 of the corresponding job type
                                // For example:
                                applyTask3Reward(player, jobType);
                            case 4:
                                // Apply reward for Task 2 of the corresponding job type
                                // For example:
                                applyTask4Reward(player, jobType);
                            default:
                                // Handle unknown task IDs
                                break;
                        }
                    }
                }
            }
        }
    }

    private static void applyTask1Reward(Player player, JobAPI.JOB_TYPE jobType) {
        switch (jobType) {
            case EXPLORER:
                // Apply reward for Explorer Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                player.sendMessage("You received a diamond as a reward for completing Explorer Task 1!");
                break;
            case FARMER:
                // Apply reward for Farmer Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.WHEAT, 1));
                player.sendMessage("You received wheat as a reward for completing Farmer Task 1!");
                break;
            // Add cases for other job types as needed
            case MINER:
                // Apply reward for Miner Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.COAL, 3));
                player.sendMessage("You received 3 coal as a reward for completing Miner Task 1!");
                break;
            case GATHERER:
                // Apply reward for Gatherer Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.STICK, 16));
                player.sendMessage("You received 16 sticks as a reward for completing Gatherer Task 1!");
                break;
            case HUNTER:
                // Apply reward for Hunter Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.BOW, 1));
                player.sendMessage("You received a bow as a reward for completing Hunter Task 1!");
                break;
            case FISHER:
                // Apply reward for Fisher Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.COD, 3));
                player.sendMessage("You received 3 raw cod as a reward for completing Fisher Task 1!");
                break;
            default:
                // Handle unknown job types
                break;
        }
    }

    private static void applyTask2Reward(Player player, JobAPI.JOB_TYPE jobType) {
        switch (jobType) {
            case EXPLORER:
                // Apply reward for Explorer Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 3));
                player.sendMessage("You received 3 gold ingots as a reward for completing Explorer Task 2!");
                break;
            case FARMER:
                // Apply reward for Farmer Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.CARROT, 3));
                player.sendMessage("You received 3 carrots as a reward for completing Farmer Task 2!");
                break;
            // Add cases for other job types as needed
            case MINER:
                // Apply reward for Miner Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 3));
                player.sendMessage("You received 3 iron ingots as a reward for completing Miner Task 2!");
                break;
            case GATHERER:
                // Apply reward for Gatherer Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                player.sendMessage("You received a diamond as a reward for completing Gatherer Task 2!");
                break;
            case HUNTER:
                // Apply reward for Hunter Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
                player.sendMessage("You received 16 arrows as a reward for completing Hunter Task 2!");
                break;
            case FISHER:
                // Apply reward for Fisher Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.COOKED_SALMON, 3));
                player.sendMessage("You received 3 cooked salmon as a reward for completing Fisher Task 2!");
                break;
            default:
                // Handle unknown job types
                break;
        }
    }

    private static void applyTask3Reward(Player player, JobAPI.JOB_TYPE jobType) {
        switch (jobType) {
            case EXPLORER:
                // Apply reward for Explorer Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.EMERALD, 2));
                player.sendMessage("You received 2 emeralds as a reward for completing Explorer Task 3!");
                break;
            case FARMER:
                // Apply reward for Farmer Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.POTATO, 3));
                player.sendMessage("You received 3 potatoes as a reward for completing Farmer Task 3!");
                break;
            case MINER:
                // Apply reward for Miner Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 2));
                player.sendMessage("You received 2 diamonds as a reward for completing Miner Task 3!");
                break;
            case GATHERER:
                // Apply reward for Gatherer Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 2));
                player.sendMessage("You received 2 iron ingots as a reward for completing Gatherer Task 3!");
                break;
            case HUNTER:
                // Apply reward for Hunter Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
                player.sendMessage("You received 16 arrows as a reward for completing Hunter Task 3!");
                break;
            case FISHER:
                // Apply reward for Fisher Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.COOKED_COD, 3));
                player.sendMessage("You received 3 cooked cod as a reward for completing Fisher Task 3!");
                break;
            // Add cases for other job types as needed
            default:
                // Handle unknown job types
                break;
        }
    }

    private static void applyTask4Reward(Player player, JobAPI.JOB_TYPE jobType) {
        switch (jobType) {
            case EXPLORER:
                // Apply reward for Explorer Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 1));
                player.sendMessage("You received an experience bottle as a reward for completing Explorer Task 4!");
                break;
            case FARMER:
                // Apply reward for Farmer Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.APPLE, 2));
                player.sendMessage("You received 2 apples as a reward for completing Farmer Task 4!");
                break;
            case MINER:
                // Apply reward for Miner Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
                player.sendMessage("You received an emerald as a reward for completing Miner Task 4!");
                break;
            case GATHERER:
                // Apply reward for Gatherer Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, 2));
                player.sendMessage("You received 2 gold nuggets as a reward for completing Gatherer Task 4!");
                break;
            case HUNTER:
                // Apply reward for Hunter Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 1));
                player.sendMessage("You received an experience bottle as a reward for completing Hunter Task 4!");
                break;
            case FISHER:
                // Apply reward for Fisher Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.NAME_TAG, 1));
                player.sendMessage("You received a name tag as a reward for completing Fisher Task 4!");
                break;
            // Add cases for other job types as needed
            default:
                // Handle unknown job types
                break;
        }
    }
}
