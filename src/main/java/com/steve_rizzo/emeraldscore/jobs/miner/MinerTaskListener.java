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

        if (JobAPI.getPlayer(player.getName()).getJob() == JobAPI.JOB_TYPE.MINER) {
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

        if (JobAPI.getPlayer(player.getName()).getJob() == JobAPI.JOB_TYPE.MINER &&
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

        if (count + 1 >= 128) {
            markTaskCompleted(player, "Coal Miner");
        }
    }

    private void incrementAndCheckIronBreakCounter(Player player) {
        int count = ironBreakCounter.getOrDefault(player.getUniqueId().toString(), 0);
        ironBreakCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 64) {
            markTaskCompleted(player, "Iron Extractor");
        }
    }

    private void incrementAndCheckDiamondBreakCounter(Player player) {
        int count = diamondBreakCounter.getOrDefault(player.getUniqueId().toString(), 0);
        diamondBreakCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 32) {
            markTaskCompleted(player, "Gem Collector");
        }
    }

    private void markDiamondPickaxeCrafted(Player player) {
        diamondPickaxeCrafted.put(player.getUniqueId().toString(), true);
        markTaskCompleted(player, "Tool Crafter");
    }

    private void markTaskCompleted(Player player, String taskName) {
        List<DailyTask> minerTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.MINER);
        if (minerTasks != null) {
            for (DailyTask task : minerTasks) {
                if (task.getName().equalsIgnoreCase(taskName)) {
                    jobTasks.markTaskCompleted(task.getName());
                    player.sendMessage(Main.prefix + ChatColor.LIGHT_PURPLE + "You've completed the " + ChatColor.GRAY + taskName + ChatColor.LIGHT_PURPLE + " task! Claim your reward in " + ChatColor.AQUA + "/jobs menu" + ChatColor.LIGHT_PURPLE + "!");
                    break;
                }
            }
        }
    }
}
