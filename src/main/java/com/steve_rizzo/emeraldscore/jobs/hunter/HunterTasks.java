package com.steve_rizzo.emeraldscore.jobs.hunter;

import com.steve_rizzo.emeraldscore.jobs.DailyTask;

import java.util.ArrayList;
import java.util.List;

public class HunterTasks {
    public static List<DailyTask> generateTasksForHunter() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Wild Game Hunter", 1, "Track and hunt wild animals for meat (32 raw meat)"));
        tasks.add(new DailyTask("Trap Setter", 2, "Set traps to catch small game (16 rabbit meat)"));
        tasks.add(new DailyTask("Rare Prey Hunter", 3, "Explore hunting grounds for dangerous prey (kill 16 hostile mobs)"));
        tasks.add(new DailyTask("Archery Practice", 4, "Practice archery and marksmanship (kill 8 hostile mobs with bow)"));
        return tasks;
    }
}
