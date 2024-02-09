package com.steve_rizzo.emeraldscore.jobs.farmer;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
import com.steve_rizzo.emeraldscore.jobs.JobTasks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class FarmerTaskListener implements Listener {

    private final JobTasks jobTasks;
    private final Map<String, Integer> wheatBreakCounter = new HashMap<>();
    private final Map<String, Integer> carrotBreakCounter = new HashMap<>();
    private final Map<String, Integer> animalFeedCounter = new HashMap<>();

    public FarmerTaskListener(JobTasks jobTasks) {
        this.jobTasks = jobTasks;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        Player player = event.getPlayer();

        if (JobAPI.getPlayer(player.getName()).getJob() == JobAPI.JOB_TYPE.FARMER) {
            if (brokenBlock.getType() == Material.WHEAT) {
                incrementAndCheckWheatBreakCounter(player);
            } else if (brokenBlock.getType() == Material.CARROTS) {
                incrementAndCheckCarrotBreakCounter(player);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (JobAPI.getPlayer(player.getName()).getJob() == JobAPI.JOB_TYPE.FARMER) {
            if (event.getRightClicked() instanceof Animals) {
                if (player.getInventory().getItemInMainHand().getType().equals(Material.COOKED_BEEF) ||
                        (player.getInventory().getItemInMainHand().getType().equals(Material.APPLE)) ||
                        (player.getInventory().getItemInMainHand().getType().equals(Material.BONE)) ||
                        (player.getInventory().getItemInMainHand().getType().equals(Material.WHEAT)))
                    incrementAndCheckAnimalFeedCounter(player);
            }
        }
    }

    private void incrementAndCheckWheatBreakCounter(Player player) {
        int count = wheatBreakCounter.getOrDefault(player.getUniqueId().toString(), 0);
        wheatBreakCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 128) {
            markTaskCompleted(player, "Wheat Harvester");
        }
    }

    private void incrementAndCheckCarrotBreakCounter(Player player) {
        int count = carrotBreakCounter.getOrDefault(player.getUniqueId().toString(), 0);
        carrotBreakCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 32) {
            markTaskCompleted(player, "Crop Planter");
        }
    }

    private void incrementAndCheckAnimalFeedCounter(Player player) {
        int count = animalFeedCounter.getOrDefault(player.getUniqueId().toString(), 0);
        animalFeedCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 2) {
            markTaskCompleted(player, "Animal Feeder");
        }
    }

    private void markTaskCompleted(Player player, String taskName) {
        List<DailyTask> farmerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.FARMER);
        if (farmerTasks != null) {
            for (DailyTask task : farmerTasks) {
                if (task.getName().equalsIgnoreCase(taskName)) {
                    jobTasks.markTaskCompleted(task.getName());
                    player.sendMessage(Main.prefix + ChatColor.LIGHT_PURPLE + "You've completed the " + ChatColor.GRAY + taskName + ChatColor.LIGHT_PURPLE + " task! Claim your reward in " + ChatColor.AQUA + "/jobs menu" + ChatColor.LIGHT_PURPLE + "!");
                    break;
                }
            }
        }
    }
}
