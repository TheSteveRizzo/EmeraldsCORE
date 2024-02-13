package com.steve_rizzo.emeraldscore.jobs;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.jobs.explorer.ExplorerTasks;
import com.steve_rizzo.emeraldscore.jobs.farmer.FarmerTasks;
import com.steve_rizzo.emeraldscore.jobs.fisher.FisherTasks;
import com.steve_rizzo.emeraldscore.jobs.gatherer.GathererTasks;
import com.steve_rizzo.emeraldscore.jobs.hunter.HunterTasks;
import com.steve_rizzo.emeraldscore.jobs.miner.MinerTasks;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DailyTask {
    private String name;
    private String description;
    private boolean completed;
    private boolean claimed;
    private int taskId;
    private int currentProgress;
    private int totalProgress;

    public DailyTask(String name, int taskId, String description, int totalProgress) {
        this.name = name;
        this.taskId = taskId;
        this.description = description;
        this.completed = false;
        this.claimed = false;
        this.totalProgress = totalProgress;
    }

    public int getProgress(String playerUUID, String jobType) {
        File taskFile = new File(Main.core.getDataFolder(), "tasks_" + jobType + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(taskFile);
        String taskIdStr = String.valueOf(taskId);
        if (config.contains(taskIdStr + ".players." + playerUUID)) {
            return config.getInt(taskIdStr + ".players." + playerUUID + ".currentProgress");
        } else {
            return 0;
        }
    }

    public void saveProgress(String playerUUID, String jobType) {
        File taskFile = new File(Main.core.getDataFolder(), "tasks_" + jobType + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(taskFile);
        String taskIdStr = String.valueOf(taskId);
        config.set(taskIdStr + ".players." + playerUUID + ".currentProgress", currentProgress);
        config.set(taskIdStr + ".players." + playerUUID + ".totalProgress", totalProgress); // Store the total progress
        config.set(taskIdStr + ".players." + playerUUID + ".claimed", claimed);
        config.set(taskIdStr + ".players." + playerUUID + ".completed", completed); // Save the completed status
        try {
            config.save(taskFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentProgress(int currentProgress, String playerUUID, String jobType) {
        this.currentProgress = currentProgress;
        saveProgress(playerUUID, jobType); // Save progress whenever it's updated
    }

    public void incrementProgress(int amount, String playerUUID, String jobType) {
        this.currentProgress += amount;
        saveProgress(playerUUID, jobType); // Save progress whenever it's updated
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isClaimed(String playerUUID, String jobType) {
        File taskFile = new File(Main.core.getDataFolder(), "tasks_" + jobType + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(taskFile);
        String taskIdStr = String.valueOf(taskId);
        if (config.contains(taskIdStr + ".players." + playerUUID)) {
            return config.getBoolean(taskIdStr + ".players." + playerUUID + ".claimed");
        } else {
            return false;
        }
    }

    public boolean isCompleted(String playerUUID, String jobType) {
        File taskFile = new File(Main.core.getDataFolder(), "tasks_" + jobType + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(taskFile);
        String taskIdStr = String.valueOf(taskId);
        if (config.contains(taskIdStr + ".players." + playerUUID)) {
            return config.getBoolean(taskIdStr + ".players." + playerUUID + ".completed");
        } else {
            return false;
        }
    }

    public void setCompleted(boolean completed, String playerUUID, String jobType) {
        this.completed = completed;
        saveProgress(playerUUID, jobType); // Save completion status whenever it's updated
    }

    public void setClaimed(boolean claimed, String playerUUID, String jobType) {
        this.claimed = claimed;
        saveProgress(playerUUID, jobType); // Save claimed status whenever it's updated
    }


    public int getTaskId() {
        return taskId;
    }

    public int getTotalProgress(String playerUUID, String jobType) {
        File taskFile = new File(Main.core.getDataFolder(), "tasks_" + jobType + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(taskFile);
        String taskIdStr = String.valueOf(taskId);
        if (config.contains(taskIdStr + ".players." + playerUUID)) {
            return config.getInt(taskIdStr + ".players." + playerUUID + ".totalProgress");
        } else if (config.contains(taskIdStr + ".defaultTotalProgress")) {
            return config.getInt(taskIdStr + ".defaultTotalProgress");
        } else {
            // Return the default total progress value based on job type
            if (jobType.equalsIgnoreCase("MINER")) {
                return switch (name) {
                    case "Coal Miner" -> MinerTasks.COAL_MINER_TOTAL_PROG;
                    case "Iron Extractor" -> MinerTasks.IRON_EXTRACTOR_TOTAL_PROG;
                    case "Gem Collector" -> MinerTasks.GEM_COLLECTOR_TOTAL_PROG;
                    case "Tool Crafter" -> MinerTasks.TOOL_CRAFTER_TOTAL_PROG;
                    default -> 0;
                };
            } else if (jobType.equalsIgnoreCase("HUNTER")) {
                return switch (name) {
                    case "Wild Game Hunter" -> HunterTasks.WILD_GAME_HUNTER_TOTAL_PROG;
                    case "Trap Setter" -> HunterTasks.TRAP_SETTER_TOTAL_PROG;
                    case "Rare Prey Hunter" -> HunterTasks.RARE_PREY_HUNTER_TOTAL_PROG;
                    case "Archery Practice" -> HunterTasks.ARCHERY_PRACTICE_TOTAL_PROG;
                    default -> 0;
                };
            } else if (jobType.equalsIgnoreCase("GATHERER")) {
                return switch (name) {
                    case "Herb Collector" -> GathererTasks.HERB_COLLECTOR_TOTAL_PROG;
                    case "Mushroom Gatherer" -> GathererTasks.MUSHROOM_GATHERER_TOTAL_PROG;
                    case "Flower Collector" -> GathererTasks.FLOWER_COLLECTOR_TOTAL_PROG;
                    case "Honey Gatherer" -> GathererTasks.HONEY_GATHERER_TOTAL_PROG;
                    default -> 0;
                };
            } else if (jobType.equalsIgnoreCase("FISHER")) {
                return switch (name) {
                    case "Cast a line and fish in different bodies of water" -> FisherTasks.CATCH_FISH_TOTAL_PROG;
                    case "Eat specific types of fish from a fish market" -> FisherTasks.EAT_FISH_TOTAL_PROG;
                    case "Explore coastal areas for unique marine life" -> FisherTasks.EXPLORE_COAST_TOTAL_PROG;
                    case "Repair fishing gear and maintain equipment" -> FisherTasks.ENCHANT_ROD_TOTAL_PROG;
                    default -> 0;
                };

            } else if (jobType.equalsIgnoreCase("FARMER")) {
                return switch (name) {
                    case "Wheat Harvester" -> FarmerTasks.WHEAT_HARVESTER_TOTAL_PROG;
                    case "Animal Feeder" -> FarmerTasks.ANIMAL_FEEDER_TOTAL_PROG;
                    case "Crop Planter" -> FarmerTasks.CROP_PLANTER_TOTAL_PROG;
                    case "Produce Deliverer" -> FarmerTasks.PRODUCE_DELIVERER_TOTAL_PROG;
                    default -> 0;
                };
            } else if (jobType.equalsIgnoreCase("EXPLORER")) {
                return switch (name) {
                    case "Cave Explorer" -> ExplorerTasks.CAVE_EXPLORER_TOTAL_PROG;
                    case "Cartographer" -> ExplorerTasks.CARTOGRAPHER_TOTAL_PROG;
                    case "Archeologist" -> ExplorerTasks.ARCHEOLOGIST_TOTAL_PROG;
                    case "Mountaineer" -> ExplorerTasks.MOUNTAINEER_TOTAL_PROG;
                    default -> 0;
                };
            }
        }
        return 0;
    }
}

