package com.steve_rizzo.emeraldscore.jobs.gatherer;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GathererTasks {

    private static final String JOB_TYPE = "GATHERER";

    // Define constants for total progress values for each task
    public static final int HERB_COLLECTOR_TOTAL_PROG = 64;
    public static final int MUSHROOM_GATHERER_TOTAL_PROG = 32;
    public static final int FLOWER_COLLECTOR_TOTAL_PROG = 32;
    public static final int HONEY_GATHERER_TOTAL_PROG = 8;

    public static List<DailyTask> generateTasksForGatherer() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Herb Collector", 1, "Collect 64 herbs and plants from the forest", HERB_COLLECTOR_TOTAL_PROG));
        tasks.add(new DailyTask("Mushroom Gatherer", 2, "Gather 32 mushrooms and berries", MUSHROOM_GATHERER_TOTAL_PROG));
        tasks.add(new DailyTask("Flower Collector", 3, "Search for 32 rare flowers in the wilderness", FLOWER_COLLECTOR_TOTAL_PROG));
        tasks.add(new DailyTask("Honey Gatherer", 4, "Harvest 8 honey from beehives", HONEY_GATHERER_TOTAL_PROG));
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
                case "Herb Collector":
                    defaultTotalProgress = HERB_COLLECTOR_TOTAL_PROG;
                    break;
                case "Mushroom Gatherer":
                    defaultTotalProgress = MUSHROOM_GATHERER_TOTAL_PROG;
                    break;
                case "Flower Collector":
                    defaultTotalProgress = FLOWER_COLLECTOR_TOTAL_PROG;
                    break;
                case "Honey Gatherer":
                    defaultTotalProgress = HONEY_GATHERER_TOTAL_PROG;
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
