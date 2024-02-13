package com.steve_rizzo.emeraldscore.jobs.fisher;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FisherTasks {

    private static final String JOB_TYPE = "FISHER";

    public static List<DailyTask> generateTasksForFisher() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Cast a line and fish in different bodies of water", 1, "Explore the waters and catch 15 fish", 15));
        tasks.add(new DailyTask("Eat specific types of fish from a fish market", 2, "Eat 10 tropical fish", 10));
        tasks.add(new DailyTask("Explore coastal areas for unique marine life", 3, "Explore 2 different sea biomes", 2));
        tasks.add(new DailyTask("Repair fishing gear and maintain equipment", 4, "Enchant a fishing rod", 1));
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
        String playerUUID = playerName; // Assuming playerName is already the UUID

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
                case "Cast a line and fish in different bodies of water":
                    defaultTotalProgress = 15;
                    break;
                case "Eat specific types of fish from a fish market":
                    defaultTotalProgress = 10;
                    break;
                case "Explore coastal areas for unique marine life":
                    defaultTotalProgress = 2;
                    break;
                case "Repair fishing gear and maintain equipment":
                    defaultTotalProgress = 1;
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
