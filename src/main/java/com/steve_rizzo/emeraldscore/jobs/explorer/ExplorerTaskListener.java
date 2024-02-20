package com.steve_rizzo.emeraldscore.jobs.explorer;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
import com.steve_rizzo.emeraldscore.jobs.JobMenu;
import com.steve_rizzo.emeraldscore.jobs.JobTasks;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.steve_rizzo.emeraldscore.jobs.explorer.ExplorerTasks.savePlayerTaskProgress;

public class ExplorerTaskListener implements Listener {

    private final JobTasks jobTasks;
    private static final String JOB_TYPE = "EXPLORER";
    private final Map<String, Map<Biome, Integer>> biomeCountersMap = new HashMap<>();

    // Constructor to initialize ExplorerTaskListener with JobTasks instance
    public ExplorerTaskListener(JobTasks jobTasks) {
        this.jobTasks = jobTasks;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();

        // Check if the player is an explorer and has a job assigned
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.EXPLORER) {
            // Check for cave exploration
            if (to.getBlockY() <= 25 && from.getBlockY() > 25) {
                incrementAndCheckCaveCounter(player, "Cave Explorer");
            }

            // Check for distance traveled
            incrementAndCheckDistanceCounter(player, from, to, "Cartographer");

            // Check for biome exploration
            incrementAndCheckBiomeCounter(player, to.getBlock().getBiome(), "Archeologist");

            // Check for height achieved
            if (to.getBlockY() > 150) {
                markHeightAchieved(player, "Mountaineer");
            }
        }
    }

    private void incrementAndCheckCaveCounter(Player player, String taskName) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> explorerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.EXPLORER);
        if (explorerTasks != null) {
            for (DailyTask task : explorerTasks) {
                if (task.getName().equals(taskName)) {
                    task.incrementProgress(1, player.getUniqueId().toString(), JOB_TYPE); // Increment progress by 1
                    if (task.getProgress(player.getUniqueId().toString(), JOB_TYPE) == task.getTotalProgress(playerUUID, JOB_TYPE)) {
                        markTaskCompleted(player, taskName, task.getTaskId());
                    }
                    break;
                }
            }
        }
    }

    private void incrementAndCheckDistanceCounter(Player player, Location from, Location to, String taskName) {
        double distance = from.distance(to);
        if (distance >= 500) {
            String playerUUID = player.getUniqueId().toString();
            List<DailyTask> explorerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.EXPLORER);
            if (explorerTasks != null) {
                for (DailyTask task : explorerTasks) {
                    if (task.getName().equals(taskName)) {
                        task.incrementProgress(1, player.getUniqueId().toString(), JOB_TYPE); // Increment progress by 1
                        if (task.getProgress(playerUUID, JOB_TYPE) == task.getTotalProgress(playerUUID, JOB_TYPE) &&
                                (!task.isCompleted(playerUUID, JOB_TYPE))) {
                            markTaskCompleted(player, taskName, task.getTaskId());
                        }
                        break;
                    }
                }
            }
        }
    }

    private void incrementAndCheckBiomeCounter(Player player, Biome biome, String taskName) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> explorerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.EXPLORER);
        if (explorerTasks != null) {
            for (DailyTask task : explorerTasks) {
                if (task.getName().equals(taskName)) {
                    // Get the player's progress for biome exploration
                    int progress = task.getProgress(playerUUID, JOB_TYPE);
                    if (progress < task.getTotalProgress(playerUUID, JOB_TYPE)) {
                        // Increment progress if the biome is different from the last visited biome
                        if (!hasVisitedBiome(playerUUID, biome)) {
                            incrementBiomeCounter(playerUUID, biome);
                            // Check if the player has explored 3 different biomes
                            if (getVisitedBiomeCount(playerUUID) == 3) {
                                // Mark the task as completed
                                markTaskCompleted(player, taskName, task.getTaskId());
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private void incrementBiomeCounter(String playerUUID, Biome biome) {
        // Increment the biome counter for the player
        Map<Biome, Integer> biomeCounters = biomeCountersMap.computeIfAbsent(playerUUID, k -> new HashMap<>());
        biomeCounters.put(biome, biomeCounters.getOrDefault(biome, 0) + 1);
    }

    private boolean hasVisitedBiome(String playerUUID, Biome biome) {
        // Check if the player has visited the specified biome before
        Map<Biome, Integer> biomeCounters = biomeCountersMap.get(playerUUID);
        return biomeCounters != null && biomeCounters.containsKey(biome);
    }

    private int getVisitedBiomeCount(String playerUUID) {
        // Get the count of unique biomes visited by the player
        Map<Biome, Integer> biomeCounters = biomeCountersMap.get(playerUUID);
        return biomeCounters != null ? biomeCounters.size() : 0;
    }


    private void markHeightAchieved(Player player, String taskName) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> explorerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.EXPLORER);
        if (explorerTasks != null) {
            for (DailyTask task : explorerTasks) {
                if (task.getName().equals(taskName)) {
                    task.incrementProgress(1, player.getUniqueId().toString(), JOB_TYPE); // Increment progress by 1
                    if (task.getProgress(player.getUniqueId().toString(), JOB_TYPE) == task.getTotalProgress(playerUUID, JOB_TYPE)) {
                        markTaskCompleted(player, taskName, task.getTaskId());
                    }
                    break;
                }
            }
        }
    }

    private void markTaskCompleted(Player player, String taskName, int taskId) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> explorerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.EXPLORER);
        if (explorerTasks != null) {
            for (DailyTask task : explorerTasks) {
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