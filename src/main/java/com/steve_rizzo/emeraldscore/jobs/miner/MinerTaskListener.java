package com.steve_rizzo.emeraldscore.jobs.miner;

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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinerTaskListener implements Listener {

    private final JobTasks jobTasks;
    private final Map<String, Integer> coalBreakCounter = new HashMap<>();
    private final Map<String, Integer> ironBreakCounter = new HashMap<>();
    private final Map<String, Integer> diamondBreakCounter = new HashMap<>();
    private final Map<String, Boolean> diamondPickaxeCrafted = new HashMap<>();

    // Constructor to initialize MinerTaskListener with JobTasks instance
    public MinerTaskListener(JobTasks jobTasks) {
        this.jobTasks = jobTasks;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        Player player = event.getPlayer();
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.MINER) {
            if (brokenBlock.getType() == Material.COAL_ORE) {
                incrementAndCheckCoalBreakCounter(player);
            } else if (brokenBlock.getType() == Material.IRON_ORE) {
                incrementAndCheckIronBreakCounter(player);
            } else if (brokenBlock.getType() == Material.DIAMOND_ORE) {
                incrementAndCheckDiamondBreakCounter(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.MINER &&
                event.getView().getTitle().equals("Crafting") && event.getRawSlot() == 0) {
            ItemStack craftedItem = event.getCurrentItem();
            if (craftedItem != null && craftedItem.getType() == Material.DIAMOND_PICKAXE) {
                markDiamondPickaxeCrafted(player);
            }
        }
    }

    private void incrementAndCheckCoalBreakCounter(Player player) {
        int count = coalBreakCounter.getOrDefault(player.getUniqueId().toString(), 0);
        coalBreakCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 <= 256) { // Make sure not to exceed the total progress
            List<DailyTask> minerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.MINER);
            if (minerTasks != null) {
                for (DailyTask task : minerTasks) {
                    if (task.getName().equals("Coal Miner")) {
                        task.incrementProgress(1); // Increment progress by 1
                        break;
                    }
                }
            }
        }
    }

    private void incrementAndCheckIronBreakCounter(Player player) {
        int count = ironBreakCounter.getOrDefault(player.getUniqueId().toString(), 0);
        ironBreakCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 <= 128) { // Make sure not to exceed the total progress
            List<DailyTask> minerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.MINER);
            if (minerTasks != null) {
                for (DailyTask task : minerTasks) {
                    if (task.getName().equals("Iron Extractor")) {
                        task.incrementProgress(1); // Increment progress by 1
                        break;
                    }
                }
            }
        }
    }

    private void incrementAndCheckDiamondBreakCounter(Player player) {
        int count = diamondBreakCounter.getOrDefault(player.getUniqueId().toString(), 0);
        diamondBreakCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 <= 8) { // Make sure not to exceed the total progress
            List<DailyTask> minerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.MINER);
            if (minerTasks != null) {
                for (DailyTask task : minerTasks) {
                    if (task.getName().equals("Gem Collector")) {
                        task.incrementProgress(1); // Increment progress by 1
                        break;
                    }
                }
            }
        }
    }

    private void markDiamondPickaxeCrafted(Player player) {
        diamondPickaxeCrafted.put(player.getUniqueId().toString(), true);
        markTaskCompleted(player, "Tool Crafter", 4);
    }

    private void markTaskCompleted(Player player, String taskName, int taskId) {
        List<DailyTask> minerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.MINER);
        if (minerTasks != null) {
            for (DailyTask task : minerTasks) {
                if (task.getTaskId() == taskId) {
                    // Mark the task as completed and update its completion status
                    task.setCompleted(true);
                    player.sendMessage(Main.prefix + ChatColor.LIGHT_PURPLE + "You've completed the " + ChatColor.GRAY + taskName + ChatColor.LIGHT_PURPLE + " task! Claim your reward in " + ChatColor.AQUA + "/jobs menu" + ChatColor.LIGHT_PURPLE + "!");
                    break;
                }
            }
        }
    }
}