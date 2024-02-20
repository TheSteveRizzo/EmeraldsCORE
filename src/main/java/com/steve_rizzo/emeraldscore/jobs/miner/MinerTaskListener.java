package com.steve_rizzo.emeraldscore.jobs.miner;

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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.steve_rizzo.emeraldscore.jobs.miner.MinerTasks.savePlayerTaskProgress;

public class MinerTaskListener implements Listener {

    private final JobTasks jobTasks;
    private static final String JOB_TYPE = "MINER";

    // Constructor to initialize MinerTaskListener with JobTasks instance
    public MinerTaskListener(JobTasks jobTasks) {
        this.jobTasks = jobTasks;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        Player player = event.getPlayer();
        JobAPI.JOB_TYPE jobType = JobAPI.JOB_TYPE.NONE;
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null) jobType = jobPlayer.getJob();
        if (jobType == JobAPI.JOB_TYPE.MINER) {
            if (brokenBlock.getType() == Material.COAL_ORE || brokenBlock.getType() == Material.DEEPSLATE_COAL_ORE) {
                incrementAndCheckProgress(player, "Coal Miner");
            } else if (brokenBlock.getType() == Material.IRON_ORE || brokenBlock.getType() == Material.DEEPSLATE_IRON_ORE) {
                incrementAndCheckProgress(player, "Iron Extractor");
            } else if (brokenBlock.getType() == Material.DIAMOND_ORE || brokenBlock.getType() == Material.DEEPSLATE_DIAMOND_ORE) {
                incrementAndCheckProgress(player, "Gem Collector");
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        JobAPI.JOB_TYPE jobType = JobAPI.JOB_TYPE.NONE;
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null) jobType = jobPlayer.getJob();
        if (jobType == JobAPI.JOB_TYPE.MINER &&
                event.getView().getTitle().equals("Crafting") && event.getRawSlot() == 0) {
            ItemStack craftedItem = event.getCurrentItem();
            if (craftedItem != null && craftedItem.getType() == Material.DIAMOND_PICKAXE) {
                markTaskCompleted(player, "Tool Crafter", 4);
            }
        }
    }

    private void incrementAndCheckProgress(Player player, String taskName) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> minerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.MINER);
        if (minerTasks != null) {
            for (DailyTask task : minerTasks) {
                if (task.getName().equals(taskName)) {
                    task.incrementProgress(1, playerUUID, JOB_TYPE); // Increment progress by 1
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
        List<DailyTask> minerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.MINER);
        if (minerTasks != null) {
            for (DailyTask task : minerTasks) {
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
