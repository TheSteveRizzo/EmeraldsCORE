package com.steve_rizzo.emeraldscore.jobs;

public class DailyTask {
    private String name, description;
    private boolean completed, claimed; // Add a boolean flag to track completion status
    private int taskId;

    public DailyTask(String name, int taskId, String description) {
        this.name = name;
        this.description = description;
        this.completed = false; // Initialize completion status to false
        this.claimed = false; // Initialize claimed status to false
        this.taskId = taskId;
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
