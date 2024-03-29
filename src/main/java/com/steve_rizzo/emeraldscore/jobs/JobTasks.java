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
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class JobTasks {
    private Map<String, Boolean> completedTasks = new HashMap<>(); // Use Map to track completion status of each task
    private Map<JobAPI.JOB_TYPE, List<DailyTask>> jobTasks = new HashMap<>();

    // Constructor to initialize job tasks and schedule daily reset
    public JobTasks() {
        initializeJobTasks();
        scheduleDailyReset();
    }

    // Method to check if a task is completed
    public boolean isTaskCompleted(String taskId) {
        return completedTasks.getOrDefault(taskId, false);
    }

    // Method to mark a task as completed
    public void markTaskCompleted(String taskId) {
        completedTasks.put(taskId, true);
    }

    // Method to reset completion status of all tasks
    public void resetCompletionStatus() {
        completedTasks.clear();
    }

    // Modified scheduleDailyReset method to refresh job tasks and reset completion status
    public void scheduleDailyReset() {
        // Get the current time in New York timezone
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));

        // Calculate the time until the next day at 12:00 AM
        ZonedDateTime nextResetTime = now.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1);

        // Calculate the initial delay until the next reset time
        Duration initialDelay = Duration.between(now, nextResetTime);

        // Schedule a task to run every day at 12:00 AM
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshJobTasks();
                resetCompletionStatus();
            }
        }, initialDelay.toMillis(), Duration.ofDays(1).toMillis());
    }


    // Method to initialize job tasks
    private void initializeJobTasks() {
        // Initialize tasks for each job type
        jobTasks.put(JobAPI.JOB_TYPE.FARMER, FarmerTasks.generateTasksForFarmer());
        jobTasks.put(JobAPI.JOB_TYPE.MINER, MinerTasks.generateTasksForMiner());
        jobTasks.put(JobAPI.JOB_TYPE.GATHERER, GathererTasks.generateTasksForGatherer());
        jobTasks.put(JobAPI.JOB_TYPE.HUNTER, HunterTasks.generateTasksForHunter());
        jobTasks.put(JobAPI.JOB_TYPE.EXPLORER, ExplorerTasks.generateTasksForExplorer());
        jobTasks.put(JobAPI.JOB_TYPE.FISHER, FisherTasks.generateTasksForFisher());

        // Initialize completion status of tasks
        for (List<DailyTask> tasks : jobTasks.values()) {
            for (DailyTask task : tasks) {
                completedTasks.put(task.getName(), false);
            }
        }
    }

    // Method to refresh job tasks with new random tasks for each job type and delete existing player task data
    public void refreshJobTasks() {
        for (JobAPI.JOB_TYPE jobType : jobTasks.keySet()) {
            // Delete existing player task data for all tasks in the job tasks data file
            deletePlayerTaskData(jobType);

            // Generate new random tasks for the job type
            List<DailyTask> tasks = jobTasks.get(jobType);
            List<DailyTask> newTasks = generateRandomTasks(jobType);

            // Clear existing tasks and add new tasks
            tasks.clear();
            tasks.addAll(newTasks);
        }
    }

    // Method to delete existing player task data for all tasks in the job tasks data file
    private void deletePlayerTaskData(JobAPI.JOB_TYPE jobType) {
        for (DailyTask task : jobTasks.get(jobType)) {
            File taskFile = new File(Main.core.getDataFolder(), "tasks_" + jobType.toString() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(taskFile);
            String taskIdStr = String.valueOf(task.getTaskId());
            if (config.contains(taskIdStr + ".players")) {
                config.set(taskIdStr + ".players", null); // Delete player task data for the task
                try {
                    config.save(taskFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // Modified method to generate random tasks for a job type
    private List<DailyTask> generateRandomTasks(JobAPI.JOB_TYPE jobType) {
        switch (jobType) {
            case FARMER:
                return FarmerTasks.generateTasksForFarmer();
            case MINER:
                return MinerTasks.generateTasksForMiner();
            case GATHERER:
                return GathererTasks.generateTasksForGatherer();
            case HUNTER:
                return HunterTasks.generateTasksForHunter();
            case EXPLORER:
                return ExplorerTasks.generateTasksForExplorer();
            case FISHER:
                return FisherTasks.generateTasksForFisher();
            default:
                return new ArrayList<>();
        }
    }


    public List<DailyTask> getPlayerJobTasks(String playerUUID, JobAPI.JOB_TYPE jobType) {
        List<DailyTask> allTasks = jobTasks.get(jobType);
        List<DailyTask> playerTasks = new ArrayList<>();
        if (allTasks != null) {
            for (DailyTask task : allTasks) {
                // Add tasks to playerTasks only if they are not completed and not claimed
                if (!completedTasks.getOrDefault(task.getName(), false) && !task.isClaimed(playerUUID, jobType.toString())) {
                    playerTasks.add(task);
                }
            }
        }
        return playerTasks;
    }


    // Getters
    public Map<JobAPI.JOB_TYPE, List<DailyTask>> getJobTasks() {
        return jobTasks;
    }
}
