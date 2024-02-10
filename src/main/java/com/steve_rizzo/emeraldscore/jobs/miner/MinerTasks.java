package com.steve_rizzo.emeraldscore.jobs.miner;

import com.steve_rizzo.emeraldscore.jobs.DailyTask;

import java.util.ArrayList;
import java.util.List;

public class MinerTasks {

    public static List<DailyTask> generateTasksForMiner() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Coal Miner", 1, "Mine 256 coal ores from the underground", 256));
        tasks.add(new DailyTask("Iron Extractor", 2, "Extract 128 iron ores from the mountains", 128));
        tasks.add(new DailyTask("Gem Collector", 3, "Collect 8 precious diamonds from the caves", 8));
        tasks.add(new DailyTask("Tool Crafter", 4, "Craft a new diamond pickaxe in the workshop", 1));
        return tasks;
    }
}