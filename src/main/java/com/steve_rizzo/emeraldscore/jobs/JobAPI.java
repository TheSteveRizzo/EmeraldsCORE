package com.steve_rizzo.emeraldscore.jobs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobAPI {

    public enum JOB_TYPE {
        FARMER,
        MINER,
        GATHERER,
        HUNTER,
        EXPLORER,
        FISHER
    }

    public static class JobPlayer {
        private final String playerName; // Unique identifier for each player
        private JOB_TYPE job;

        public JobPlayer(String playerName, JOB_TYPE job) {
            this.playerName = playerName;
            this.job = job;
            // Add the player to the player list when creating a JobPlayer instance
            addPlayerToList(this);
        }
        public JOB_TYPE getJob() {
            return job;
        }

        public void setJob(JOB_TYPE job) {
            if (this.job == null || System.currentTimeMillis() - getLastTeamChange(playerName) >= 3 * 24 * 60 * 60 * 1000) {
                this.job = job;
                setLastTeamChange(playerName, System.currentTimeMillis());
            } else {
                Player player = Bukkit.getPlayer(playerName);
                if (player != null) {
                    player.sendMessage(getCooldownMessage());
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

    public static class JobTeam {
        private final JOB_TYPE jobType;
        private final List<String> players;

        public JobTeam(JOB_TYPE jobType) {
            this.jobType = jobType;
            this.players = new ArrayList<>();
        }

        public JOB_TYPE getJobType() {
            return jobType;
        }

        public List<String> getPlayers() {
            return players;
        }

        public void addPlayer(String playerName) {
            for (JobTeam team : loadTeams()) {
                if (team.getPlayers().contains(playerName)) {
                    team.removePlayer(playerName);
                }
            }
            players.add(playerName);
        }

        public void removePlayer(String playerName) {
            players.remove(playerName);
        }
    }

    private static final Map<String, Long> lastTeamChange = new HashMap<>();

    public static long getLastTeamChange(String playerName) {
        return lastTeamChange.getOrDefault(playerName, 0L);
    }

    public static void setLastTeamChange(String playerName, long timestamp) {
        lastTeamChange.put(playerName, timestamp);
    }

    public static boolean isPlayerInCooldown(JobPlayer player) {
        return player != null && System.currentTimeMillis() - getLastTeamChange(player.getPlayerName()) < 3 * 24 * 60 * 60 * 1000;
    }

    public static JobPlayer getPlayer(String playerName) {
        // Assuming you have a list of JobPlayer instances stored somewhere accessible
        for (JobPlayer player : getPlayerList()) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
        return null; // Player not found
    }

    private static final List<JobPlayer> playerList = new ArrayList<>();

    // Method to add a JobPlayer to the list
    public static void addPlayerToList(JobPlayer player) {
        playerList.add(player);
    }

    // Method to get all JobPlayers
    public static List<JobPlayer> getPlayerList() {
        return playerList;
    }


    public static void saveTeams(List<JobTeam> teams) {
        try (FileWriter writer = new FileWriter("teams.yml")) {
            Yaml yaml = new Yaml();
            yaml.dump(teams, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<JobTeam> loadTeams() {
        List<JobTeam> teams = new ArrayList<>();
        File file = new File("teams.yml");
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Yaml yaml = new Yaml();
                teams = yaml.loadAs(reader, List.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return teams;
    }
}
