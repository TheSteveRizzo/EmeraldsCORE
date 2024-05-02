package com.steve_rizzo.emeraldscore.jobs;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.tokens.TokensAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JobRewards {

    public static void applyReward(Player player, JobAPI.JOB_TYPE jobType, int taskId) {
        String uuid = player.getUniqueId().toString();
        Bukkit.getLogger().info("Applying reward for player: " + uuid + ", Job type: " + jobType + ", Task ID: " + taskId);

        JobTasks jobTasks = new JobTasks();
        List<DailyTask> tasks = jobTasks.getPlayerJobTasks(uuid, jobType);

        if (tasks != null) {
            for (DailyTask task : tasks) {
                if (task.getTaskId() == taskId && task.isCompleted(uuid, jobType.toString()) && !task.isClaimed(uuid, jobType.toString())) {
                    try {
                        switch (taskId) {
                            case 1:
                                applyTask1Reward(player, jobType);
                                break;
                            case 2:
                                applyTask2Reward(player, jobType);
                                break;
                            case 3:
                                applyTask3Reward(player, jobType);
                                break;
                            case 4:
                                applyTask4Reward(player, jobType);
                                break;
                            default:
                                Bukkit.getLogger().warning("Unknown task ID: " + taskId);
                                break;
                        }
                        // Mark the task as claimed after applying the reward
                        task.setClaimed(true, uuid, jobType.toString());
                        // Update the inventory display to show the task as completed and claimed
                        JobMenu.updateTaskMenuItem(player, task, jobType.toString());
                        // Inform the player that the task has been completed and claimed
                        player.sendMessage(Main.prefix + "You've completed and claimed the reward for this task!");
                    } catch (Exception e) {
                        Bukkit.getLogger().severe("Error applying reward: " + e.getMessage());
                        e.printStackTrace();
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
                TokensAPI.addTokens(player, 1);
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received an Emerald Token and 1x Diamond for completing Explorer Task 1!");
                break;
            case FARMER:
                // Apply reward for Farmer Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                TokensAPI.addTokens(player, 1);
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received an Emerald Token and 1x Diamond for completing for completing Farmer Task 1!");
                break;
            // Add cases for other job types as needed
            case MINER:
                // Apply reward for Miner Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                TokensAPI.addTokens(player, 1);
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received an Emerald Token and 1x Diamond for completing Miner Task 1!");
                break;
            case GATHERER:
                // Apply reward for Gatherer Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                TokensAPI.addTokens(player, 1);
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received an Emerald Token and 1x Diamond for completing Gatherer Task 1!");
                break;
            case HUNTER:
                // Apply reward for Hunter Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                TokensAPI.addTokens(player, 1);
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received an Emerald Token and 1x Diamond for completing Hunter Task 1!");
                break;
            case FISHER:
                // Apply reward for Fisher Task 1
                // For example:
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                TokensAPI.addTokens(player, 1);
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received an Emerald Token and 1x Diamond for completing Fisher Task 1!");
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
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 3 gold ingots as a reward for completing Explorer Task 2!");
                break;
            case FARMER:
                // Apply reward for Farmer Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.CARROT, 3));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 3 carrots as a reward for completing Farmer Task 2!");
                break;
            // Add cases for other job types as needed
            case MINER:
                // Apply reward for Miner Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 3));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 3 iron ingots as a reward for completing Miner Task 2!");
                break;
            case GATHERER:
                // Apply reward for Gatherer Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received a diamond as a reward for completing Gatherer Task 2!");
                break;
            case HUNTER:
                // Apply reward for Hunter Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 16 arrows as a reward for completing Hunter Task 2!");
                break;
            case FISHER:
                // Apply reward for Fisher Task 2
                // For example:
                player.getInventory().addItem(new ItemStack(Material.COOKED_SALMON, 3));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 3 cooked salmon as a reward for completing Fisher Task 2!");
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
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 2 emeralds as a reward for completing Explorer Task 3!");
                break;
            case FARMER:
                // Apply reward for Farmer Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.POTATO, 3));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 3 potatoes as a reward for completing Farmer Task 3!");
                break;
            case MINER:
                // Apply reward for Miner Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 2));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 2 diamonds as a reward for completing Miner Task 3!");
                break;
            case GATHERER:
                // Apply reward for Gatherer Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 2));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 2 iron ingots as a reward for completing Gatherer Task 3!");
                break;
            case HUNTER:
                // Apply reward for Hunter Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 16 arrows as a reward for completing Hunter Task 3!");
                break;
            case FISHER:
                // Apply reward for Fisher Task 3
                // For example:
                player.getInventory().addItem(new ItemStack(Material.COOKED_COD, 3));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 3 cooked cod as a reward for completing Fisher Task 3!");
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
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received an experience bottle as a reward for completing Explorer Task 4!");
                break;
            case FARMER:
                // Apply reward for Farmer Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.APPLE, 2));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 2 apples as a reward for completing Farmer Task 4!");
                break;
            case MINER:
                // Apply reward for Miner Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received an emerald as a reward for completing Miner Task 4!");
                break;
            case GATHERER:
                // Apply reward for Gatherer Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, 2));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received 2 gold nuggets as a reward for completing Gatherer Task 4!");
                break;
            case HUNTER:
                // Apply reward for Hunter Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 1));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received an experience bottle as a reward for completing Hunter Task 4!");
                break;
            case FISHER:
                // Apply reward for Fisher Task 4
                // For example:
                player.getInventory().addItem(new ItemStack(Material.NAME_TAG, 1));
                player.sendMessage(Main.prefix + ChatColor.GREEN + "You received a name tag as a reward for completing Fisher Task 4!");
                break;
            // Add cases for other job types as needed
            default:
                // Handle unknown job types
                break;
        }
    }
}
