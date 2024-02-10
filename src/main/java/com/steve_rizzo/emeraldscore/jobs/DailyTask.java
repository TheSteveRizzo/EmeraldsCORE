package com.steve_rizzo.emeraldscore.jobs;

public class DailyTask {
    private String name, description;
    private boolean completed, claimed; // Add a boolean flag to track completion status
    private int taskId;
    private int currentProgress; // Add a field to track current progress
    private int totalProgress; // Add a field to track total progress

    public DailyTask(String name, int taskId, String description, int totalProgress) {
        this.name = name;
        this.description = description;
        this.completed = false; // Initialize completion status to false
        this.claimed = false; // Initialize claimed status to false
        this.taskId = taskId;
        this.currentProgress = 0; // Initialize current progress to 0
        this.totalProgress = totalProgress; // Set total progress (requirement)
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getTotalProgress() {
        return totalProgress;
    }

    public void incrementProgress(int amount) {
        currentProgress += amount;
        if (currentProgress >= totalProgress) {
            completed = true;
            currentProgress = totalProgress; // Ensure progress doesn't exceed the total
        }
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public String getDescription() {
        return description;
    }

    public int getTaskId() {
        return taskId;
    }
}
