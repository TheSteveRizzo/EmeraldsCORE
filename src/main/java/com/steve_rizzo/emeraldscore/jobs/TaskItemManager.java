package com.steve_rizzo.emeraldscore.jobs;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TaskItemManager {
    private static final Map<ItemStack, DailyTask> taskItemMap = new HashMap<>();

    public static void addTaskItem(ItemStack taskItem, DailyTask task) {
        taskItemMap.put(taskItem, task);
    }

    public static void removeTaskItem(ItemStack taskItem) {
        taskItemMap.remove(taskItem);
    }

    public static DailyTask getTaskByItem(ItemStack taskItem) {
        return taskItemMap.get(taskItem);
    }

    public static void clear() {
        taskItemMap.clear();
    }

    public static Map<ItemStack, DailyTask> getTaskItemMap() {
        return taskItemMap;
    }
}
