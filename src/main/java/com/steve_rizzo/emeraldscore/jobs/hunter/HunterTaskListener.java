package com.steve_rizzo.emeraldscore.jobs.hunter;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
import com.steve_rizzo.emeraldscore.jobs.JobTasks;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class HunterTaskListener implements Listener {

    private final JobTasks jobTasks;
    private final Map<String, Integer> rawMeatCounter = new HashMap<>();
    private final Map<String, Integer> rabbitMeatCounter = new HashMap<>();
    private final Map<String, Integer> hostileMobCounter = new HashMap<>();
    private final Map<String, Integer> bowKillCounter = new HashMap<>();

    // Constructor to initialize HunterTaskListener with JobTasks instance
    public HunterTaskListener(JobTasks jobTasks) {
        this.jobTasks = jobTasks;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player != null) {
            JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
            if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.HUNTER) {
                // Check if the killed entity is a valid target
                switch (event.getEntityType()) {
                    case COW:
                    case PIG:
                    case CHICKEN:
                    case SHEEP:
                        incrementAndCheckRawMeatCounter(player);
                        break;
                    case RABBIT:
                        incrementAndCheckRabbitMeatCounter(player);
                        break;
                    case ZOMBIE:
                    case SKELETON:
                    case SPIDER:
                    case CREEPER:
                    case ENDERMAN:
                    case WITCH:
                        incrementAndCheckHostileMobCounter(player);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(attacker.getName());
            if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.HUNTER) {
                if (event.getCause() == EntityDamageByEntityEvent.DamageCause.PROJECTILE &&
                        attacker.getInventory().getItemInMainHand().getType().toString().contains("BOW")) {
                    incrementAndCheckBowKillCounter(attacker);
                }
            }
        }
    }

    private void incrementAndCheckRawMeatCounter(Player player) {
        int count = rawMeatCounter.getOrDefault(player.getUniqueId().toString(), 0);
        rawMeatCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 32) {
            markTaskCompleted(player, "Wild Game Hunter");
        }
    }

    private void incrementAndCheckRabbitMeatCounter(Player player) {
        int count = rabbitMeatCounter.getOrDefault(player.getUniqueId().toString(), 0);
        rabbitMeatCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 16) {
            markTaskCompleted(player, "Trap Setter");
        }
    }

    private void incrementAndCheckHostileMobCounter(Player player) {
        int count = hostileMobCounter.getOrDefault(player.getUniqueId().toString(), 0);
        hostileMobCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 16) {
            markTaskCompleted(player, "Rare Prey Hunter");
        }
    }

    private void incrementAndCheckBowKillCounter(Player player) {
        int count = bowKillCounter.getOrDefault(player.getUniqueId().toString(), 0);
        bowKillCounter.put(player.getUniqueId().toString(), count + 1);

        if (count + 1 >= 8) {
            markTaskCompleted(player, "Archery Practice");
        }
    }

    private void markTaskCompleted(Player player, String taskName) {
        // Mark the task as completed and notify the player
        jobTasks.markTaskCompleted(taskName);
        player.sendMessage(Main.prefix + ChatColor.LIGHT_PURPLE + "You've completed the " + taskName + " task! Claim your reward in " + ChatColor.AQUA + "/jobs menu" + ChatColor.LIGHT_PURPLE + "!");
    }
}