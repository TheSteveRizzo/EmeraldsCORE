package com.steve_rizzo.emeraldscore.jobs.fisher;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
import com.steve_rizzo.emeraldscore.jobs.JobTasks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class FisherTaskListener implements Listener {

    private final JobTasks jobTasks;
    private static final String JOB_TYPE = "FISHER";

    // Constructor to initialize FisherTaskListener with JobTasks instance
    public FisherTaskListener(JobTasks jobTasks) {
        this.jobTasks = jobTasks;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());

        // Check if the player is a fisher and has a job assigned
        if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.FISHER) {
            // Check if the event is a successful catch
            if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                incrementAndCheckProgress(player, "Cast a line and fish in different bodies of water", 1);
            }
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());

        // Check if the player is a fisher, has a job assigned, and consumes tropical fish
        if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.FISHER &&
                event.getItem().getType() == Material.TROPICAL_FISH) {
            incrementAndCheckProgress(player, "Eat specific types of fish from a fish market", 2);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        // Check if the player is a fisher and has a job assigned
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.FISHER) {
            // Check if the player enters a sea biome
            if (event.getTo().getBlock().getBiome() == Biome.OCEAN || event.getTo().getBlock().getBiome() == Biome.DEEP_OCEAN) {
                incrementAndCheckProgress(player, "Explore coastal areas for unique marine life", 3);
            }
        }
    }

    private void incrementAndCheckProgress(Player player, String taskName, int taskId) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> fisherTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.FISHER);
        if (fisherTasks != null) {
            for (DailyTask task : fisherTasks) {
                if (task.getName().equals(taskName)) {
                    task.incrementProgress(1, player.getUniqueId().toString(), JOB_TYPE); // Increment progress by 1
                    if (task.getProgress(playerUUID, JOB_TYPE) == task.getTotalProgress(playerUUID, JOB_TYPE) &&
                            (!task.isCompleted(playerUUID, JOB_TYPE))) {
                        markTaskCompleted(player, taskName, taskId);
                    }
                    break;
                }
            }
        }
    }

    private void markTaskCompleted(Player player, String taskName, int taskId) {
        // Mark the task as completed and notify the player
        List<DailyTask> fisherTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.FISHER);
        if (fisherTasks != null) {
            for (DailyTask task : fisherTasks) {
                if (task.getTaskId() == taskId) {
                    jobTasks.markTaskCompleted(task.getName());
                    player.sendMessage(Main.prefix + ChatColor.LIGHT_PURPLE + "You've completed the " + ChatColor.GRAY + taskName + ChatColor.LIGHT_PURPLE + " task! Claim your reward in " + ChatColor.AQUA + "/jobs menu" + ChatColor.LIGHT_PURPLE + "!");
                    break;
                }
            }
        }
    }
}