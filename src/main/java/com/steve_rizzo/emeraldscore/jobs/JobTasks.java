package com.steve_rizzo.emeraldscore.jobs;

import com.steve_rizzo.emeraldscore.jobs.explorer.ExplorerTasks;
import com.steve_rizzo.emeraldscore.jobs.farmer.FarmerTasks;
import com.steve_rizzo.emeraldscore.jobs.fisher.FisherTasks;
import com.steve_rizzo.emeraldscore.jobs.gatherer.GathererTasks;
import com.steve_rizzo.emeraldscore.jobs.hunter.HunterTasks;
import com.steve_rizzo.emeraldscore.jobs.miner.MinerTasks;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class JobTasks {
    private Set<String> completedTasks = new HashSet<>();
    private Map<JobAPI.JOB_TYPE, List<DailyTask>> jobTasks = new HashMap<>();

    // Constructor to initialize job tasks and schedule daily reset
    public JobTasks() {
        initializeJobTasks();
        scheduleDailyReset();
    }

    // Method to check if a task is completed
    public boolean isTaskCompleted(String taskId) {
        return completedTasks.contains(taskId);
    }

    // Method to mark a task as completed
    public void markTaskCompleted(String taskId) {
        completedTasks.add(taskId);
    }

    // Method to reset completion status of all tasks
    public void resetCompletionStatus() {
        completedTasks.clear();
    }

    // Method to schedule daily task to reset completion status
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
    }

    // Method to refresh job tasks with new random tasks
    public void refreshJobTasks() {
        for (JobAPI.JOB_TYPE jobType : jobTasks.keySet()) {
            List<DailyTask> tasks = jobTasks.get(jobType);
            List<DailyTask> newTasks = generateRandomTasks(jobType);
            tasks.clear();
            tasks.addAll(newTasks);
        }
    }

    // Method to generate random tasks for a job type
    private List<DailyTask> generateRandomTasks(JobAPI.JOB_TYPE jobType) {
        switch (jobType) {
            case FARMER:
                return generateRandomTasks(FarmerTasks.generateTasksForFarmer());
            case MINER:
                return generateRandomTasks(MinerTasks.generateTasksForMiner());
            case GATHERER:
                return generateRandomTasks(GathererTasks.generateTasksForGatherer());
            case HUNTER:
                return generateRandomTasks(HunterTasks.generateTasksForHunter());
            case EXPLORER:
                return generateRandomTasks(ExplorerTasks.generateTasksForExplorer());
            case FISHER:
                return generateRandomTasks(FisherTasks.generateTasksForFisher());
            default:
                return new ArrayList<>();
        }
    }

    // Method to generate a random subset of tasks
    private List<DailyTask> generateRandomTasks(List<DailyTask> sourceTasks) {
        List<DailyTask> randomTasks = new ArrayList<>();
        Collections.shuffle(sourceTasks); // Shuffle the tasks to get a random order
        int numTasks = Math.min(sourceTasks.size(), 4); // Select up to 4 tasks (or less if fewer available)
        for (int i = 0; i < numTasks; i++) {
            randomTasks.add(sourceTasks.get(i));
        }
        return randomTasks;
    }
    public Map<JobAPI.JOB_TYPE, List<DailyTask>> getJobTasks() {
        return this.jobTasks;
    }
}
