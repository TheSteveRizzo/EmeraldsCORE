package com.steve_rizzo.emeraldscore.jobs.miner;

import com.steve_rizzo.emeraldscore.jobs.DailyTask;

import java.util.ArrayList;
import java.util.List;

public class MinerTasks {

    public static List<DailyTask> generateTasksForMiner() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Coal Miner", 1, "Mine coal from the underground"));
        tasks.add(new DailyTask("Iron Extractor", 2, "Extract iron ore from the mountains"));
        tasks.add(new DailyTask("Gem Collector", 3, "Collect precious gems from the caves"));
        tasks.add(new DailyTask("Tool Crafter", 4, "Craft new tools in the workshop"));
        return tasks;
    }
}