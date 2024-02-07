package com.steve_rizzo.emeraldscore.jobs;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class JobCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String prefix = Main.prefix;

            if (args.length == 0) {
                p.sendMessage(prefix + ChatColor.GRAY + "Learn more about jobs using "
                        + ChatColor.AQUA + "/jobs help" + ChatColor.GRAY + " or " + ChatColor.AQUA + "/jobs menu");
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    p.sendMessage(prefix + ChatColor.GRAY + "Learn more about jobs using: "
                            + ChatColor.AQUA + "/jobs set <job>" + ChatColor.GRAY + " or " + ChatColor.AQUA + "/jobs list");
                    return true;
                } else if (args[0].equalsIgnoreCase("list")) {
                    p.sendMessage(prefix + ChatColor.GRAY + "Available job types:");
                    StringBuilder jobList = new StringBuilder();
                    for (JobAPI.JOB_TYPE jobType : JobAPI.JOB_TYPE.values()) {
                        jobList.append(ChatColor.LIGHT_PURPLE).append(jobType.name()).append(ChatColor.GRAY).append(", ");
                    }
                    // Remove the last comma and space
                    if (jobList.length() > 2) {
                        jobList.delete(jobList.length() - 2, jobList.length());
                    }
                    p.sendMessage(jobList.toString());
                    return true;
                } else if (args[0].equalsIgnoreCase("menu")) {
                    JobMenu.openJobSelectionMenu(p);
                    return true;
                } else {
                    p.sendMessage(prefix + ChatColor.RED + "Unrecognized command. Use /jobs help for assistance.");
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    String jobTypeString = args[1].toUpperCase();
                    try {
                        JobAPI.JOB_TYPE jobType = JobAPI.JOB_TYPE.valueOf(jobTypeString);

                        // Retrieve the JobPlayer instance for the player
                        JobAPI.JobPlayer player = JobAPI.getPlayer(p.getName());

                        // Check if the player is in cooldown
                        if (player != null && JobAPI.isPlayerInCooldown(player.getPlayerName())) {
                            p.sendMessage(prefix + player.getCooldownMessage());
                            return true;
                        }

                        // Set the job for the player and update last job change timestamp
                        if (player == null) {
                            // If the player is selecting their first job
                            player = new JobAPI.JobPlayer(p.getName(), jobType);
                        } else {
                            player.setJob(jobType);
                        }

                        // Save player's job to file after setting it only if not in cooldown
                        if (!JobAPI.isPlayerInCooldown(player.getPlayerName())) {
                            JobAPI.savePlayerJobToFile(player);
                        }

                        // Save cooldown data to file
                        JobAPI.saveCooldownData();

                        p.sendMessage(prefix + ChatColor.GRAY + "You've set your job to "
                                + ChatColor.AQUA + jobType);
                        return true;
                    } catch (IllegalArgumentException e) {
                        p.sendMessage(prefix + ChatColor.RED + "Invalid job type. Available job types: "
                                + ChatColor.GRAY + Arrays.toString(JobAPI.JOB_TYPE.values()));
                        return true;
                    }
                } else {
                    p.sendMessage(prefix + ChatColor.RED + "Unrecognized command. Use /jobs help for assistance.");
                    return true;
                }
            }
        }
        return false;
    }
}