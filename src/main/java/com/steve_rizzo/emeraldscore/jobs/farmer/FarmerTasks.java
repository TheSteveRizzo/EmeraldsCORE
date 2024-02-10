package com.steve_rizzo.emeraldscore.jobs.farmer;

import com.steve_rizzo.emeraldscore.jobs.DailyTask;

import java.util.ArrayList;
import java.util.List;

public class FarmerTasks {
    public static List<DailyTask> generateTasksForFarmer() {
        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(new DailyTask("Wheat Harvester", 1, "Harvest 128 wheat from the farm", 128));
        tasks.add(new DailyTask("Animal Feeder", 2, "Feed 2 animals in the barn", 2));
        tasks.add(new DailyTask("Crop Planter", 3, "Plant 32 new crops in the field", 32));
        tasks.add(new DailyTask("Produce Deliverer", 4, "Deliver 64 produce (carrots) to the market", 64));
        return tasks;
    }
}
