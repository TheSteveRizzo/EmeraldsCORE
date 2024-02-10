package com.steve_rizzo.emeraldscore.jobs.explorer;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
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

public class ExplorerTaskListener implements Listener {

    private final JobTasks jobTasks;
    private final Map<String, Integer> caveCounter = new HashMap<>();
    private final Map<String, Integer> distanceCounter = new HashMap<>();
    private final Map<String, Integer> biomeCounter = new HashMap<>();
    private final Map<String, Boolean> heightAchieved = new HashMap<>();

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
                incrementAndCheckCaveCounter(player);
            }

            // Check for distance traveled
            incrementAndCheckDistanceCounter(player, from, to);

            // Check for biome exploration
            incrementAndCheckBiomeCounter(player, to.getBlock().getBiome());

            // Check for height achieved
            if (to.getBlockY() > 150) {
                markHeightAchieved(player);
            }
        }
    }

    private void incrementAndCheckCaveCounter(Player player) {
        int count = caveCounter.getOrDefault(player.getUniqueId().toString(), 0);
        caveCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 1) {
            markTaskCompleted(player, "Cave Explorer");
        }
    }

    private void incrementAndCheckDistanceCounter(Player player, Location from, Location to) {
        int distance = (int) from.distance(to);
        int totalDistance = distanceCounter.getOrDefault(player.getUniqueId().toString(), 0) + distance;
        distanceCounter.put(player.getUniqueId().toString(), totalDistance);

        if (totalDistance >= 500) {
            markTaskCompleted(player, "Cartographer");
        }
    }

    private void incrementAndCheckBiomeCounter(Player player, Biome biome) {
        int count = biomeCounter.getOrDefault(player.getUniqueId().toString(), 0);
        biomeCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 3) {
            markTaskCompleted(player, "Archeologist");
        }
    }

    private void markHeightAchieved(Player player) {
        boolean heightAchieved = this.heightAchieved.getOrDefault(player.getUniqueId().toString(), false);
        if (!heightAchieved) {
            this.heightAchieved.put(player.getUniqueId().toString(), true);
            markTaskCompleted(player, "Mountaineer");
        }
    }

    private void markTaskCompleted(Player player, String taskName) {
        List<DailyTask> explorerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.EXPLORER);
        if (explorerTasks != null) {
            for (DailyTask task : explorerTasks) {
                if (task.getName().equalsIgnoreCase(taskName)) {
                    jobTasks.markTaskCompleted(task.getName());
                    player.sendMessage(Main.prefix + ChatColor.LIGHT_PURPLE + "You've completed the " + ChatColor.GRAY + taskName + ChatColor.LIGHT_PURPLE + " task! Claim your reward in " + ChatColor.AQUA + "/jobs menu" + ChatColor.LIGHT_PURPLE + "!");
                    break;
                }
            }
        }
    }
}