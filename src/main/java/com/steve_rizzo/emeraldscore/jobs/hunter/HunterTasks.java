package com.steve_rizzo.emeraldscore.jobs.hunter;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HunterTasks {

    private static final String JOB_TYPE = "HUNTER";

    public static final int WILD_GAME_HUNTER_TOTAL_PROG = 32;
    public static final int TRAP_SETTER_TOTAL_PROG = 16;
    public static final int RARE_PREY_HUNTER_TOTAL_PROG = 16;
    public static final int ARCHERY_PRACTICE_TOTAL_PROG = 8;

    public static List<DailyTask> generateTasksForHunter() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Wild Game Hunter", 1, "Track and hunt wild animals for meat (32 raw meat)", WILD_GAME_HUNTER_TOTAL_PROG));
        tasks.add(new DailyTask("Trap Setter", 2, "Set traps to catch small game (16 rabbit meat)", TRAP_SETTER_TOTAL_PROG));
        tasks.add(new DailyTask("Rare Prey Hunter", 3, "Explore hunting grounds for dangerous prey (kill 16 hostile mobs)", RARE_PREY_HUNTER_TOTAL_PROG));
        tasks.add(new DailyTask("Archery Practice", 4, "Practice archery and marksmanship (kill 8 hostile mobs with bow)", ARCHERY_PRACTICE_TOTAL_PROG));

        for (DailyTask task : tasks) {
            saveDefaultTotalProgress(task);
        }

        return tasks;
    }

    public static void savePlayerTaskProgress(String playerName, DailyTask task) {
        File taskFile = new File(Main.core.getDataFolder(), "tasks_" + JOB_TYPE + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(taskFile);

        String taskId = String.valueOf(task.getTaskId());
        String playerUUID = Bukkit.getPlayer(playerName).getUniqueId().toString();

        config.set(taskId + ".players." + playerUUID + ".currentProgress", task.getProgress(playerUUID, JOB_TYPE));
        config.set(taskId + ".players." + playerUUID + ".totalProgress", task.getTotalProgress(playerUUID, JOB_TYPE));
        config.set(taskId + ".players." + playerUUID + ".completed", task.isCompleted(playerUUID, JOB_TYPE));
        config.set(taskId + ".players." + playerUUID + ".claimed", task.isClaimed(playerUUID, JOB_TYPE));

        try {
            config.save(taskFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveDefaultTotalProgress(DailyTask task) {
        File taskFile = new File(Main.core.getDataFolder(), "tasks_" + JOB_TYPE + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(taskFile);

        String taskId = String.valueOf(task.getTaskId());

        if (!config.contains(taskId + ".defaultTotalProgress")) {
            int defaultTotalProgress = 0;
            switch (task.getName()) {
                case "Wild Game Hunter":
                    defaultTotalProgress = WILD_GAME_HUNTER_TOTAL_PROG;
                    break;
                case "Trap Setter":
                    defaultTotalProgress = TRAP_SETTER_TOTAL_PROG;
                    break;
                case "Rare Prey Hunter":
                    defaultTotalProgress = RARE_PREY_HUNTER_TOTAL_PROG;
                    break;
                case "Archery Practice":
                    defaultTotalProgress = ARCHERY_PRACTICE_TOTAL_PROG;
                    break;
                default:
                    defaultTotalProgress = 0;
                    break;
            }
            config.set(taskId + ".defaultTotalProgress", defaultTotalProgress);
            try {
                config.save(taskFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}