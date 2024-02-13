package com.steve_rizzo.emeraldscore.jobs.farmer;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FarmerTasks {

    private static final String JOB_TYPE = "FARMER";

    // Define constants for total progress values for each task
    public static final int WHEAT_HARVESTER_TOTAL_PROG = 128;
    public static final int ANIMAL_FEEDER_TOTAL_PROG = 2;
    public static final int CROP_PLANTER_TOTAL_PROG = 32;
    public static final int PRODUCE_DELIVERER_TOTAL_PROG = 64;

    public static List<DailyTask> generateTasksForFarmer() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Wheat Harvester", 1, "Harvest 128 wheat from the farm", WHEAT_HARVESTER_TOTAL_PROG));
        tasks.add(new DailyTask("Animal Feeder", 2, "Feed 2 animals in the barn", ANIMAL_FEEDER_TOTAL_PROG));
        tasks.add(new DailyTask("Crop Planter", 3, "Plant 32 new crops in the field", CROP_PLANTER_TOTAL_PROG));
        tasks.add(new DailyTask("Produce Deliverer", 4, "Deliver 64 produce (carrots) to the market", PRODUCE_DELIVERER_TOTAL_PROG));
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
                case "Wheat Harvester":
                    defaultTotalProgress = WHEAT_HARVESTER_TOTAL_PROG;
                    break;
                case "Animal Feeder":
                    defaultTotalProgress = ANIMAL_FEEDER_TOTAL_PROG;
                    break;
                case "Crop Planter":
                    defaultTotalProgress = CROP_PLANTER_TOTAL_PROG;
                    break;
                case "Produce Deliverer":
                    defaultTotalProgress = PRODUCE_DELIVERER_TOTAL_PROG;
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