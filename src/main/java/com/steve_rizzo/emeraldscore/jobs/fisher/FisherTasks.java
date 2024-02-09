package com.steve_rizzo.emeraldscore.jobs.fisher;

import com.steve_rizzo.emeraldscore.jobs.DailyTask;

import java.util.ArrayList;
import java.util.List;

public class FisherTasks {
    public static List<DailyTask> generateTasksForFisher() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Cast a line and fish in different bodies of water", 1, "Explore the waters and catch 15 fish"));
        tasks.add(new DailyTask("Eat specific types of fish from a fish market", 2, "Eat 10 tropical fish"));
        tasks.add(new DailyTask("Explore coastal areas for unique marine life", 3, "Explore 2 different sea biomes"));
        tasks.add(new DailyTask("Repair fishing gear and maintain equipment", 4, "Enchant a fishing rod"));
        return tasks;
    }
}
