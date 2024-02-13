package com.steve_rizzo.emeraldscore.jobs.miner;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.DailyTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MinerTasks {

    private static final String JOB_TYPE = "MINER";

    // Define constants for total progress values for each task
    public static final int COAL_MINER_TOTAL_PROG = 256;
    public static final int IRON_EXTRACTOR_TOTAL_PROG = 128;
    public static final int GEM_COLLECTOR_TOTAL_PROG = 8;
    public static final int TOOL_CRAFTER_TOTAL_PROG = 1;

    public static List<DailyTask> generateTasksForMiner() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Coal Miner", 1, "Mine 256 coal ores from the underground", COAL_MINER_TOTAL_PROG));
        tasks.add(new DailyTask("Iron Extractor", 2, "Extract 128 iron ores from the mountains", IRON_EXTRACTOR_TOTAL_PROG));
        tasks.add(new DailyTask("Gem Collector", 3, "Collect 8 precious diamonds from the caves", GEM_COLLECTOR_TOTAL_PROG));
        tasks.add(new DailyTask("Tool Crafter", 4, "Craft a new diamond pickaxe in the workshop", TOOL_CRAFTER_TOTAL_PROG));
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
                case "Coal Miner":
                    defaultTotalProgress = COAL_MINER_TOTAL_PROG;
                    break;
                case "Iron Extractor":
                    defaultTotalProgress = IRON_EXTRACTOR_TOTAL_PROG;
                    break;
                case "Gem Collector":
                    defaultTotalProgress = GEM_COLLECTOR_TOTAL_PROG;
                    break;
                case "Tool Crafter":
                    defaultTotalProgress = TOOL_CRAFTER_TOTAL_PROG;
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