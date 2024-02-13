package com.steve_rizzo.emeraldscore.jobs;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobAPI {

    private static final Map<String, Long> lastTeamChange = new HashMap<>();
    private static final String JOBS_FILE_NAME = "jobs.yml";
    private static FileConfiguration jobsConfig;

    public static void initialize() {
        File jobsFile = new File(Main.core.getDataFolder(), JOBS_FILE_NAME);
        if (!jobsFile.exists()) {
            Main.core.saveResource(JOBS_FILE_NAME, false);
        }
        jobsConfig = YamlConfiguration.loadConfiguration(jobsFile);

        // Load cooldown data from the file
        loadCooldownData();
    }

    public static void loadCooldownData() {
        if (jobsConfig.contains("cooldowns")) {
            ConfigurationSection cooldownsSection = jobsConfig.getConfigurationSection("cooldowns");
            if (cooldownsSection != null) {
                for (String playerName : cooldownsSection.getKeys(false)) {
                    long lastChangeTimestamp = cooldownsSection.getLong(playerName);
                    lastTeamChange.put(playerName, lastChangeTimestamp);
                }
            }
        }
    }

    public static void saveCooldownData() {
        ConfigurationSection cooldownsSection = jobsConfig.createSection("cooldowns");
        for (Map.Entry<String, Long> entry : lastTeamChange.entrySet()) {
            cooldownsSection.set(entry.getKey(), entry.getValue());
        }

        saveJobsFile();
    }

    public static long getLastTeamChange(String playerName) {
        return lastTeamChange.getOrDefault(playerName, 0L);
    }

    private static void setLastTeamChange(String playerName, long timestamp) {
        lastTeamChange.put(playerName, timestamp);
    }

    public static boolean isPlayerInCooldown(String playerName) {
        return System.currentTimeMillis() - getLastTeamChange(playerName) < 3 * 24 * 60 * 60 * 1000;
    }

    public static JobPlayer getPlayer(String playerName) {
        for (JobPlayer player : getPlayerList()) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
        return null; // Player not found
    }

    public static List<JobPlayer> getPlayerList() {
        List<JobPlayer> players = new ArrayList<>();
        if (jobsConfig.contains("players")) {
            for (String playerName : jobsConfig.getConfigurationSection("players").getKeys(false)) {
                String jobName = jobsConfig.getString("players." + playerName + ".job");
                JobPlayer jobPlayer = new JobPlayer(playerName, JOB_TYPE.valueOf(jobName));
                players.add(jobPlayer);
            }
        }
        return players;
    }

    public static void savePlayerJobToFile(JobPlayer player) {
        jobsConfig.set("players." + player.getPlayerName() + ".job", player.getJob().name());
        saveJobsFile();
    }

    private static void saveJobsFile() {
        try {
            jobsConfig.save(new File(Main.core.getDataFolder(), JOBS_FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class JobPlayer {
        private final String playerName;
        private JOB_TYPE job;

        public JobPlayer(String playerName, JOB_TYPE job) {
            this.playerName = playerName;
            this.job = job;
        }

        public JOB_TYPE getJob() {
            if (job != null) {
                return job;
            } else {
                // If the job is null, you can return a default job type or handle the situation as needed
                return JOB_TYPE.NONE;
            }
        }

        public void setJob(JOB_TYPE job) {
            if (this.job == null || System.currentTimeMillis() - getLastTeamChange(playerName) >= 3 * 24 * 60 * 60 * 1000) {
                // Remove old task data if the player had a previous job
                if (this.job != null && this.job != JOB_TYPE.NONE) {
                    removeOldTaskData(playerName, this.job);
                }

                this.job = job;
                setLastTeamChange(playerName, System.currentTimeMillis());
                savePlayerJobToFile(this); // Save player's job to file
                addPlayerToCooldown(playerName); // Add player to cooldown list
            } else {
                Player player = Bukkit.getPlayer(playerName);
                if (player != null) {
                    player.sendMessage(getCooldownMessage());
                }
            }
        }

        private void removeOldTaskData(String playerName, JOB_TYPE oldJob) {
            // Get the file corresponding to the old job
            File taskFile = new File(Main.core.getDataFolder(), "tasks_" + oldJob.toString() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(taskFile);

            // Remove the player's task data from the file
            if (config.contains("players." + playerName)) {
                config.set("players." + playerName, null);
                try {
                    config.save(taskFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getCooldownMessage() {
            long lastChangeTimestamp = getLastTeamChange(playerName);
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - lastChangeTimestamp;
            long cooldownRemaining = 3 * 24 * 60 * 60 * 1000 - timeElapsed;

            long days = cooldownRemaining / (24 * 60 * 60 * 1000);
            long hours = (cooldownRemaining % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
            long minutes = (cooldownRemaining % (60 * 60 * 1000)) / (60 * 1000);

            return ChatColor.RED + "You cannot change your job for another " + ChatColor.GRAY +
                    ChatColor.AQUA + days + ChatColor.GRAY + " day(s) " +
                    ChatColor.AQUA + hours + ChatColor.GRAY + " hour(s), and " +
                    ChatColor.AQUA + minutes + ChatColor.GRAY + " minute(s).";
        }
    }

    // Method to add a player to the cooldown list
    public static void addPlayerToCooldown(String playerName) {
        // Retrieve cooldown data
        Map<String, Long> cooldownData = getCooldownData();

        // Add the player to the cooldown list with current timestamp
        cooldownData.put(playerName, System.currentTimeMillis());

        // Save updated cooldown data to file
        saveCooldownData();
    }

    // Method to retrieve cooldown data from file
    public static Map<String, Long> getCooldownData() {
        if (jobsConfig.contains("cooldowns")) {
            ConfigurationSection cooldownsSection = jobsConfig.getConfigurationSection("cooldowns");
            if (cooldownsSection != null) {
                for (String playerName : cooldownsSection.getKeys(false)) {
                    long lastChangeTimestamp = cooldownsSection.getLong(playerName);
                    lastTeamChange.put(playerName, lastChangeTimestamp);
                }
            }
        }
        return lastTeamChange;
    }

    public enum JOB_TYPE {
        FARMER,
        MINER,
        GATHERER,
        HUNTER,
        EXPLORER,
        FISHER,
        NONE;

    }
}