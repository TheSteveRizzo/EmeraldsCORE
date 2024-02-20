package com.steve_rizzo.emeraldscore.jobs.gatherer;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
import com.steve_rizzo.emeraldscore.jobs.JobMenu;
import com.steve_rizzo.emeraldscore.jobs.JobTasks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

import static com.steve_rizzo.emeraldscore.jobs.gatherer.GathererTasks.savePlayerTaskProgress;

public class GathererTaskListener implements Listener {

    private final JobTasks jobTasks;
    private static final String JOB_TYPE = "GATHERER";

    // Constructor to initialize GathererTaskListener with JobTasks instance
    public GathererTaskListener(JobTasks jobTasks) {
        this.jobTasks = jobTasks;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        Player player = event.getPlayer();
        JobAPI.JOB_TYPE jobType = JobAPI.JOB_TYPE.NONE;
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null) jobType = jobPlayer.getJob();
        if (jobType == JobAPI.JOB_TYPE.GATHERER) {
            if (brokenBlock.getType() == Material.OAK_LEAVES || brokenBlock.getType() == Material.BIRCH_LEAVES ||
                    brokenBlock.getType() == Material.SPRUCE_LEAVES || brokenBlock.getType() == Material.JUNGLE_LEAVES ||
                    brokenBlock.getType() == Material.ACACIA_LEAVES || brokenBlock.getType() == Material.DARK_OAK_LEAVES ||
                    brokenBlock.getType() == Material.GRASS || brokenBlock.getType() == Material.FERN) {
                incrementAndCheckProgress(player, "Herb Collector");
            } else if (brokenBlock.getType() == Material.RED_MUSHROOM || brokenBlock.getType() == Material.BROWN_MUSHROOM ||
                    brokenBlock.getType() == Material.RED_BED || brokenBlock.getType() == Material.BROWN_BED) {
                incrementAndCheckProgress(player, "Mushroom Gatherer");
            } else if (brokenBlock.getType() == Material.DANDELION || brokenBlock.getType() == Material.POPPY ||
                    brokenBlock.getType() == Material.BLUE_ORCHID || brokenBlock.getType() == Material.ALLIUM ||
                    brokenBlock.getType() == Material.AZURE_BLUET || brokenBlock.getType() == Material.RED_TULIP ||
                    brokenBlock.getType() == Material.ORANGE_TULIP || brokenBlock.getType() == Material.WHITE_TULIP ||
                    brokenBlock.getType() == Material.PINK_TULIP || brokenBlock.getType() == Material.OXEYE_DAISY ||
                    brokenBlock.getType() == Material.CORNFLOWER || brokenBlock.getType() == Material.LILY_OF_THE_VALLEY ||
                    brokenBlock.getType() == Material.SUNFLOWER || brokenBlock.getType() == Material.LILAC ||
                    brokenBlock.getType() == Material.ROSE_BUSH || brokenBlock.getType() == Material.PEONY) {
                incrementAndCheckProgress(player, "Flower Collector");
            } else if (brokenBlock.getType() == Material.BEEHIVE || brokenBlock.getType() == Material.BEE_NEST) {
                incrementAndCheckProgress(player, "Honey Gatherer");
            }
        }
    }

    private void incrementAndCheckProgress(Player player, String taskName) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> gathererTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.GATHERER);
        if (gathererTasks != null) {
            for (DailyTask task : gathererTasks) {
                if (task.getName().equals(taskName)) {
                    task.incrementProgress(1, player.getUniqueId().toString(), JOB_TYPE); // Increment progress by 1
                    if (task.getProgress(playerUUID, JOB_TYPE) == task.getTotalProgress(playerUUID, JOB_TYPE) &&
                            (!task.isCompleted(playerUUID, JOB_TYPE))) {
                        markTaskCompleted(player, taskName, task.getTaskId());
                    }
                    // Update the task item in the menu with the new progress
                    JobMenu.updateTaskMenuItem(player, task, JOB_TYPE);

                    // Save the player's task progress
                    savePlayerTaskProgress(player.getName(), task); // This line saves the player's task progress

                    break;
                }
            }
        }
    }

    private void markTaskCompleted(Player player, String taskName, int taskId) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> gathererTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.GATHERER);
        if (gathererTasks != null) {
            for (DailyTask task : gathererTasks) {
                if (task.getTaskId() == taskId) {
                    // Mark the task as completed
                    task.setCompleted(true, playerUUID, JOB_TYPE);
                    player.sendMessage(Main.prefix + ChatColor.LIGHT_PURPLE + "You've completed the " + ChatColor.GRAY + taskName + ChatColor.LIGHT_PURPLE + " task! Claim your reward in " + ChatColor.AQUA + "/jobs menu" + ChatColor.LIGHT_PURPLE + "!");

                    // Save the player's task progress
                    savePlayerTaskProgress(player.getName(), task);

                    break;
                }
            }
        }
    }
}