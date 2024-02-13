package com.steve_rizzo.emeraldscore.jobs.explorer;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExplorerTasks {

    private static final String JOB_TYPE = "EXPLORER";

    // Define constants for total progress values for each task
    public static final int CAVE_EXPLORER_TOTAL_PROG = 1;
    public static final int CARTOGRAPHER_TOTAL_PROG = 500;
    public static final int ARCHEOLOGIST_TOTAL_PROG = 3;
    public static final int MOUNTAINEER_TOTAL_PROG = 1;

    public static List<DailyTask> generateTasksForExplorer() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Cave Explorer", 1, "Discover hidden caves and caverns (go below Y-25)", CAVE_EXPLORER_TOTAL_PROG));
        tasks.add(new DailyTask("Cartographer", 2, "Map uncharted territories (walk 500 blocks)", CARTOGRAPHER_TOTAL_PROG));
        tasks.add(new DailyTask("Archeologist", 3, "Search for ancient ruins and artifacts (enter 3 different biomes)", ARCHEOLOGIST_TOTAL_PROG));
        tasks.add(new DailyTask("Mountaineer", 4, "Climb mountains and reach new heights (go above Y-150)", MOUNTAINEER_TOTAL_PROG));
        // Save default total progress values in the file
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

        // Check if the total progress value is already set in the file
        if (!config.contains(taskId + ".defaultTotalProgress")) {
            int defaultTotalProgress = 0;
            // Assign default total progress based on the task
            switch (task.getName()) {
                case "Cave Explorer":
                    defaultTotalProgress = CAVE_EXPLORER_TOTAL_PROG;
                    break;
                case "Cartographer":
                    defaultTotalProgress = CARTOGRAPHER_TOTAL_PROG;
                    break;
                case "Archeologist":
                    defaultTotalProgress = ARCHEOLOGIST_TOTAL_PROG;
                    break;
                case "Mountaineer":
                    defaultTotalProgress = MOUNTAINEER_TOTAL_PROG;
                    break;
                default:
                    // Handle default case if task name doesn't match any predefined tasks
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