package com.steve_rizzo.emeraldscore.jobs.gatherer;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
import com.steve_rizzo.emeraldscore.jobs.JobTasks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GathererTaskListener implements Listener {

    private final JobTasks jobTasks;
    private final Map<String, Integer> herbCounter = new HashMap<>();
    private final Map<String, Integer> mushroomCounter = new HashMap<>();
    private final Map<String, Integer> flowerCounter = new HashMap<>();
    private final Map<String, Integer> honeyCounter = new HashMap<>();

    // Constructor to initialize GathererTaskListener with JobTasks instance
    public GathererTaskListener(JobTasks jobTasks) {
        this.jobTasks = jobTasks;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        Player player = event.getPlayer();

        // Check if the player is a gatherer
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.GATHERER) {
            // Check the broken block type and increment counters accordingly
            if (brokenBlock.getType() == Material.OAK_LEAVES || brokenBlock.getType() == Material.BIRCH_LEAVES ||
                    brokenBlock.getType() == Material.SPRUCE_LEAVES || brokenBlock.getType() == Material.JUNGLE_LEAVES ||
                    brokenBlock.getType() == Material.ACACIA_LEAVES || brokenBlock.getType() == Material.DARK_OAK_LEAVES ||
                    brokenBlock.getType() == Material.GRASS || brokenBlock.getType() == Material.FERN) {
                incrementAndCheckHerbCounter(player);
            } else if (brokenBlock.getType() == Material.RED_MUSHROOM || brokenBlock.getType() == Material.BROWN_MUSHROOM ||
                    brokenBlock.getType() == Material.RED_BED || brokenBlock.getType() == Material.BROWN_BED) {
                incrementAndCheckMushroomCounter(player);
            } else if (brokenBlock.getType() == Material.DANDELION || brokenBlock.getType() == Material.POPPY ||
                    brokenBlock.getType() == Material.BLUE_ORCHID || brokenBlock.getType() == Material.ALLIUM ||
                    brokenBlock.getType() == Material.AZURE_BLUET || brokenBlock.getType() == Material.RED_TULIP ||
                    brokenBlock.getType() == Material.ORANGE_TULIP || brokenBlock.getType() == Material.WHITE_TULIP ||
                    brokenBlock.getType() == Material.PINK_TULIP || brokenBlock.getType() == Material.OXEYE_DAISY ||
                    brokenBlock.getType() == Material.CORNFLOWER || brokenBlock.getType() == Material.LILY_OF_THE_VALLEY ||
                    brokenBlock.getType() == Material.SUNFLOWER || brokenBlock.getType() == Material.LILAC ||
                    brokenBlock.getType() == Material.ROSE_BUSH || brokenBlock.getType() == Material.PEONY) {
                incrementAndCheckFlowerCounter(player);
            } else if (brokenBlock.getType() == Material.BEEHIVE || brokenBlock.getType() == Material.BEE_NEST) {
                incrementAndCheckHoneyCounter(player);
            }
        }
    }

    private void incrementAndCheckHerbCounter(Player player) {
        int count = herbCounter.getOrDefault(player.getUniqueId().toString(), 0);
        herbCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 64) {
            markTaskCompleted(player, "Herb Collector", 1);
        }
    }

    private void incrementAndCheckMushroomCounter(Player player) {
        int count = mushroomCounter.getOrDefault(player.getUniqueId().toString(), 0);
        mushroomCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 32) {
            markTaskCompleted(player, "Mushroom Gatherer", 2);
        }
    }

    private void incrementAndCheckFlowerCounter(Player player) {
        int count = flowerCounter.getOrDefault(player.getUniqueId().toString(), 0);
        flowerCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 32) {
            markTaskCompleted(player, "Flower Collector", 3);
        }
    }

    private void incrementAndCheckHoneyCounter(Player player) {
        int count = honeyCounter.getOrDefault(player.getUniqueId().toString(), 0);
        honeyCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 8) {
            markTaskCompleted(player, "Honey Gatherer", 4);
        }
    }

    private void markTaskCompleted(Player player, String taskName, int taskId) {
        // Mark the task as completed and notify the player
        List<DailyTask> gathererTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.GATHERER);
        if (gathererTasks != null) {
            for (DailyTask task : gathererTasks) {
                if (task.getTaskId() == taskId) {
                    jobTasks.markTaskCompleted(taskName);
                    player.sendMessage(Main.prefix + ChatColor.LIGHT_PURPLE + "You've completed the " + taskName + " task! Claim your reward in " + ChatColor.AQUA + "/jobs menu" + ChatColor.LIGHT_PURPLE + "!");
                    break;
                }
            }
        }
    }
}