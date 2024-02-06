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
                    p.sendMessage(prefix + ChatColor.GRAY + "Set your job type using "
                            + ChatColor.GREEN + "/jobs set <job>\n" + ChatColor.AQUA
                            + "Available job types: " + ChatColor.GRAY + Arrays.toString(JobAPI.JOB_TYPE.values()));
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
                        if (player != null && JobAPI.isPlayerInCooldown(player)) {
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
