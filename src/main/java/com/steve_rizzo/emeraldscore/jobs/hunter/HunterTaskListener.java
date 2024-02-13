package com.steve_rizzo.emeraldscore.jobs.hunter;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import com.steve_rizzo.emeraldscore.jobs.JobAPI;
import com.steve_rizzo.emeraldscore.jobs.JobMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

import static com.steve_rizzo.emeraldscore.jobs.hunter.HunterTasks.savePlayerTaskProgress;

public class HunterTaskListener implements Listener {

    private static final String JOB_TYPE = "HUNTER";


    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player != null) {
            JobAPI.JOB_TYPE jobType = JobAPI.JOB_TYPE.NONE;
            JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
            if (jobPlayer != null) jobType = jobPlayer.getJob();
            if (jobType == JobAPI.JOB_TYPE.HUNTER) {
                DailyTask task = getTaskByEntityType(event.getEntity().getType());
                if (task != null) {
                    incrementAndCheckProgress(player, task);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            // Check if the player has the Hunter job
            JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(player.getName());
            if (jobPlayer != null && jobPlayer.getJob() == JobAPI.JOB_TYPE.HUNTER) {
                // Get the entity being attacked
                org.bukkit.entity.Entity entity = event.getEntity();
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    String taskName = null;
                    switch (livingEntity.getType()) {
                        case COW:
                        case PIG:
                        case CHICKEN:
                        case SHEEP:
                            taskName = "Wild Game Hunter";
                            break;
                        case RABBIT:
                            taskName = "Trap Setter";
                            break;
                        case ZOMBIE:
                        case SKELETON:
                        case SPIDER:
                        case CREEPER:
                        case ENDERMAN:
                        case WITCH:
                            if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE
                                    && player.getInventory().getItemInMainHand().getType() == Material.BOW) {
                                taskName = "Archery Practice";
                            } else {
                                taskName = "Rare Prey Hunter";
                            }
                            break;
                    }
                    if (taskName != null) {
                        DailyTask task = getTaskByName(taskName);
                        if (task != null) {
                            incrementAndCheckProgress(player, task);
                        }
                    }
                }
            }
        }
    }

    private void incrementAndCheckProgress(Player player, DailyTask task) {
        String playerUUID = player.getUniqueId().toString();
        task.incrementProgress(1, playerUUID, JOB_TYPE); // Increment progress by 1
        if (task.getProgress(playerUUID, JOB_TYPE) == task.getTotalProgress(playerUUID, JOB_TYPE)) {
            markTaskCompleted(player, task.getName(), task.getTaskId());
        }
        // Update the task item in the menu with the new progress
        JobMenu.updateTaskMenuItem(player, task, JOB_TYPE);
        // Save the player's task progress
        savePlayerTaskProgress(player.getName(), task); // This line saves the player's task progress
    }

    private void markTaskCompleted(Player player, String taskName, int taskId) {
        String playerUUID = player.getUniqueId().toString();
        List<DailyTask> hunterTasks = HunterTasks.generateTasksForHunter(); // Get hunter tasks from HunterTasks class
        if (hunterTasks != null) {
            for (DailyTask task : hunterTasks) {
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

    private DailyTask getTaskByName(String taskName) {
        List<DailyTask> hunterTasks = HunterTasks.generateTasksForHunter(); // Get hunter tasks from HunterTasks class
        if (hunterTasks != null) {
            for (DailyTask task : hunterTasks) {
                if (task.getName().equals(taskName)) {
                    return task;
                }
            }
        }
        return null;
    }

    private DailyTask getTaskByEntityType(org.bukkit.entity.EntityType entityType) {
        List<DailyTask> hunterTasks = HunterTasks.generateTasksForHunter(); // Get hunter tasks from HunterTasks class
        if (hunterTasks != null) {
            for (DailyTask task : hunterTasks) {
                switch (entityType) {
                    case COW:
                    case PIG:
                    case CHICKEN:
                    case SHEEP:
                        if (task.getName().equals("Wild Game Hunter")) {
                            return task;
                        }
                        break;
                    case RABBIT:
                        if (task.getName().equals("Trap Setter")) {
                            return task;
                        }
                        break;
                    case ZOMBIE:
                    case SKELETON:
                    case SPIDER:
                    case CREEPER:
                    case ENDERMAN:
                    case WITCH:
                        if (task.getName().equals("Rare Prey Hunter")) {
                            return task;
                        }
                        break;
                }
            }
        }
        return null;
    }
}