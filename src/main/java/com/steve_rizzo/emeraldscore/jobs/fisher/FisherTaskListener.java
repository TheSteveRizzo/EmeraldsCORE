package com.steve_rizzo.emeraldscore.jobs.fisher;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
import com.steve_rizzo.emeraldscore.jobs.JobTasks;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FisherTaskListener implements Listener {

    private final JobTasks jobTasks;
    private final Map<String, Integer> fishCatchCounter = new HashMap<>();
    private final Map<String, Integer> tropicalFishCounter = new HashMap<>();
    private final Map<String, Integer> seaBiomeCounter = new HashMap<>();
    private final Map<String, Boolean> rodEnchantment = new HashMap<>();

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
                incrementAndCheckFishCatchCounter(player);
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
            incrementAndCheckTropicalFishCounter(player);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();

        // Check if the player is a fisher and has a job assigned
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
        if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.FISHER) {
            Biome biome = to.getBlock().getBiome();
            // Check if the player enters a sea biome
            if (biome == Biome.OCEAN || biome == Biome.DEEP_OCEAN || biome == Biome.WARM_OCEAN || biome == Biome.LUKEWARM_OCEAN || biome == Biome.COLD_OCEAN || biome == Biome.FROZEN_OCEAN) {
                incrementAndCheckSeaBiomeCounter(player);
            }
        }
    }

    private void incrementAndCheckSeaBiomeCounter(Player player) {
        int count = seaBiomeCounter.getOrDefault(player.getUniqueId().toString(), 0);
        seaBiomeCounter.put(player.getUniqueId().toString(), count + 1);

        // Check if the player has entered a sea biome a certain number of times
        if (count + 1 >= 2) {
            markTaskCompleted(player, "Explore the seas");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());

        // Check if the player is a fisher, has a job assigned, and has an enchanted fishing rod
        if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.FISHER &&
                player.getInventory().contains(Material.ENCHANTED_BOOK) &&
                player.getInventory().contains(Material.FISHING_ROD)) {
            rodEnchantment.put(player.getUniqueId().toString(), true);
            markTaskCompleted(player, "Repair fishing gear and maintain equipment");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove player data when they quit to prevent memory leaks
        Player player = event.getPlayer();
        String playerId = player.getUniqueId().toString();
        fishCatchCounter.remove(playerId);
        tropicalFishCounter.remove(playerId);
        seaBiomeCounter.remove(playerId);
        rodEnchantment.remove(playerId);
    }

    private void incrementAndCheckFishCatchCounter(Player player) {
        int count = fishCatchCounter.getOrDefault(player.getUniqueId().toString(), 0);
        fishCatchCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 15) {
            markTaskCompleted(player, "Cast a line and fish in different bodies of water");
        }
    }

    private void incrementAndCheckTropicalFishCounter(Player player) {
        int count = tropicalFishCounter.getOrDefault(player.getUniqueId().toString(), 0);
        tropicalFishCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 10) {
            markTaskCompleted(player, "Eat specific types of fish from a fish market");
        }
    }

    private void markTaskCompleted(Player player, String taskName) {
        // Mark the task as completed and notify the player
        List<DailyTask> fisherTasks = jobTasks.getJobTasks().get(JobAPI.JOB_TYPE.FISHER);
        if (fisherTasks != null) {
            for (DailyTask task : fisherTasks) {
                if (task.getName().equalsIgnoreCase(taskName)) {
                    jobTasks.markTaskCompleted(task.getName());
                    player.sendMessage(Main.prefix + ChatColor.LIGHT_PURPLE + "You've completed the " + ChatColor.GRAY + taskName + ChatColor.LIGHT_PURPLE + " task! Claim your reward in " + ChatColor.AQUA + "/jobs menu" + ChatColor.LIGHT_PURPLE + "!");
                    break;
                }
            }
        }
    }
}
