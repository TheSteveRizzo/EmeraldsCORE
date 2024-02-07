package com.steve_rizzo.emeraldscore.jobs.explorer;

import com.steve_rizzo.emeraldscore.jobs.DailyTask;

import java.util.ArrayList;
import java.util.List;

public class ExplorerTasks {
    public static List<DailyTask> generateTasksForExplorer() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Cave Explorer", 1, "Discover hidden caves and caverns"));
        tasks.add(new DailyTask("Cartographer", 2, "Map uncharted territories"));
        tasks.add(new DailyTask("Archeologist", 3, "Search for ancient ruins and artifacts"));
        tasks.add(new DailyTask("Mountaineer", 4, "Climb mountains and reach new heights"));
        return tasks;
    }
}
