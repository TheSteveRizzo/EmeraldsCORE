package com.steve_rizzo.emeraldscore.jobs.fisher;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
import com.steve_rizzo.emeraldscore.jobs.JobTasks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

        // Check if the player is a fisher
        if (JobAPI.getPlayer(player.getName()).getJob() == JobAPI.JOB_TYPE.FISHER) {
            // Check if the event is a successful catch
            if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                incrementAndCheckFishCatchCounter(player);
            }
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        // Check if the player is a fisher and consumes tropical fish
        if (JobAPI.getPlayer(player.getName()).getJob() == JobAPI.JOB_TYPE.FISHER &&
                event.getItem().getType() == Material.TROPICAL_FISH) {
            incrementAndCheckTropicalFishCounter(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Check if the player is a fisher and has an enchanted fishing rod
        Player player = event.getPlayer();
        if (JobAPI.getPlayer(player.getName()).getJob() == JobAPI.JOB_TYPE.FISHER &&
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
