package com.steve_rizzo.emeraldscore.jobs.gatherer;

import com.steve_rizzo.emeraldscore.jobs.DailyTask;

import java.util.ArrayList;
import java.util.List;

public class GathererTasks {
    public static List<DailyTask> generateTasksForGatherer() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Herb Collector", 1, "Collect 64 herbs and plants from the forest"));
        tasks.add(new DailyTask("Mushroom Gatherer", 2, "Gather 32 mushrooms and berries"));
        tasks.add(new DailyTask("Flower Collector", 3, "Search for 32 rare flowers in the wilderness"));
        tasks.add(new DailyTask("Honey Gatherer", 4, "Harvest 8 honey from beehives"));
        return tasks;
    }
}