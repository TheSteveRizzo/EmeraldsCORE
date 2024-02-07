package com.steve_rizzo.emeraldscore.jobs.fisher;

import com.steve_rizzo.emeraldscore.jobs.DailyTask;

import java.util.ArrayList;
import java.util.List;

public class FisherTasks {
    public static List<DailyTask> generateTasksForFisher() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Cast a line and fish in different bodies of water", 1, "Explore the waters and catch fish"));
        tasks.add(new DailyTask("Catch specific types of fish for a fish market", 2, "Find and catch specific fish for selling"));
        tasks.add(new DailyTask("Explore coastal areas for unique marine life", 3, "Search for unique marine creatures along the coast"));
        tasks.add(new DailyTask("Repair fishing gear and maintain equipment", 4, "Perform maintenance on fishing equipment"));
        return tasks;
    }
}
