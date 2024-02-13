package com.steve_rizzo.emeraldscore.jobs.farmer;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
import com.steve_rizzo.emeraldscore.jobs.JobMenu;
import com.steve_rizzo.emeraldscore.jobs.JobTasks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FarmerTaskListener implements Listener {

    private final JobTasks jobTasks;
    private static final String JOB_TYPE = "FARMER";

    // Constructor to initialize FarmerTaskListener with JobTasks instance
    public FarmerTaskListener(JobTasks jobTasks) {
        this.jobTasks = jobTasks;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        Player player = event.getPlayer();
        JobAPI.JOB_TYPE jobType = JobAPI.JOB_TYPE.NONE;
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null) jobType = jobPlayer.getJob();
        if (jobType == JobAPI.JOB_TYPE.FARMER) {
            if (brokenBlock.getType() == Material.WHEAT) {
                incrementAndCheckProgress(player, "Wheat Harvester");
            } else if (brokenBlock.getType() == Material.CARROTS) {
                incrementAndCheckProgress(player, "Produce Deliverer");
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        JobAPI.JOB_TYPE jobType = JobAPI.JOB_TYPE.NONE;
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null) jobType = jobPlayer.getJob();
        if (jobType == JobAPI.JOB_TYPE.FARMER) {
            if (isAnimal(event.getRightClicked().getType())) {
                incrementAndCheckProgress(player, "Animal Feeder");
            }
        }
    }

    private boolean isAnimal(EntityType entityType) {
        return entityType == EntityType.COW ||
                entityType == EntityType.CHICKEN ||
                entityType == EntityType.HORSE ||
                entityType == EntityType.WOLF;
    }

    private void incrementAndCheckProgress(Player player, String taskName) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> farmerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.FARMER);
        if (farmerTasks != null) {
            for (DailyTask task : farmerTasks) {
                if (task.getName().equals(taskName)) {
                    task.incrementProgress(1, player.getUniqueId().toString(), JOB_TYPE); // Increment progress by 1
                    if (task.getProgress(player.getUniqueId().toString(), JOB_TYPE) == task.getTotalProgress(playerUUID, JOB_TYPE)) {
                        markTaskCompleted(player, taskName, task.getTaskId());
                    }
                    // Update the task item in the menu with the new progress
                    JobMenu.updateTaskMenuItem(player, task, JOB_TYPE);

                    // Save the player's task progress
                    FarmerTasks.savePlayerTaskProgress(player.getName(), task); // This line saves the player's task progress

                    break;
                }
            }
        }
    }


    private void markTaskCompleted(Player player, String taskName, int taskId) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> farmerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.FARMER);
        if (farmerTasks != null) {
            for (DailyTask task : farmerTasks) {
                if (task.getTaskId() == taskId) {
                    // Mark the task as completed
                    task.setCompleted(true, playerUUID, JOB_TYPE);
                    player.sendMessage(Main.prefix + ChatColor.LIGHT_PURPLE + "You've completed the " + ChatColor.GRAY + taskName + ChatColor.LIGHT_PURPLE + " task! Claim your reward in " + ChatColor.AQUA + "/jobs menu" + ChatColor.LIGHT_PURPLE + "!");

                    // Save the player's task progress
                    FarmerTasks.savePlayerTaskProgress(player.getName(), task);

                    break;
                }
            }
        }
    }
}
