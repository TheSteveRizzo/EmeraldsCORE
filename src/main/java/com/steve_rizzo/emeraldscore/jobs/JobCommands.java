package com.steve_rizzo.emeraldscore.jobs;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class JobCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String prefix = Main.prefix;

            if (args.length == 0) {
                player.sendMessage(prefix + ChatColor.GRAY + "Learn more about jobs using "
                        + ChatColor.AQUA + "/jobs help" + ChatColor.GRAY + " or " + ChatColor.AQUA + "/jobs menu");
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(prefix + ChatColor.GRAY + "Learn more about jobs using: "
                            + ChatColor.AQUA + "/jobs set <job>" + ChatColor.GRAY + " || " + ChatColor.AQUA + "/jobs list" +
                            ChatColor.GRAY + " || " + ChatColor.AQUA + "/jobs tasks");
                    return true;
                } else if (args[0].equalsIgnoreCase("list")) {
                    player.sendMessage(prefix + ChatColor.GRAY + "Available job types:");
                    StringBuilder jobList = new StringBuilder();
                    for (JobAPI.JOB_TYPE jobType : JobAPI.JOB_TYPE.values()) {
                        jobList.append(ChatColor.LIGHT_PURPLE).append(jobType.name()).append(ChatColor.GRAY).append(", ");
                    }
                    // Remove the last comma and space
                    if (jobList.length() > 2) {
                        jobList.delete(jobList.length() - 2, jobList.length());
                    }
                    player.sendMessage(jobList.toString());
                    return true;
                } else if (args[0].equalsIgnoreCase("menu")) {
                    JobMenu.openJobSelectionMenu(player);
                    return true;

                } else if (args[0].equalsIgnoreCase("tasks")) {

                    if (JobAPI.getPlayer(player.getName()).getJob().equals(JobAPI.JOB_TYPE.NONE)) {
                        player.sendMessage(prefix + ChatColor.DARK_AQUA + "You must first set a job using " + ChatColor.AQUA + "/jobs set <jobType>");
                        return true;
                    } else {
                        JobMenu.openTaskSelectorMenu(player);
                        player.sendMessage(prefix + ChatColor.GRAY + "Task menu opened.");
                        return true;
                    }

                } else {
                    player.sendMessage(prefix + ChatColor.RED + "Unrecognized command. Use /jobs help for assistance.");
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    String jobTypeString = args[1].toUpperCase();
                    try {
                        JobAPI.JOB_TYPE jobType = JobAPI.JOB_TYPE.valueOf(jobTypeString);

                        // Retrieve the JobPlayer instance for the player
                        JobAPI.JobPlayer playerJob = JobAPI.getPlayer(player.getName());

                        if (playerJob == null || playerJob.getJob() == JobAPI.JOB_TYPE.NONE) {
                            player.sendMessage(prefix + ChatColor.RED + "You must select a job before viewing tasks.");
                            return true;
                        }

                        // Send a message to the player indicating how to set their job
                        player.sendMessage(ChatColor.GRAY + "If you wish to set your job to " + ChatColor.AQUA + jobType +
                                ChatColor.GRAY + ", perform the following command:");
                        player.sendMessage(ChatColor.AQUA + "/job set " + jobType);

                        JobMenu.openTaskSelectorMenu(player);

                        return true;
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(prefix + ChatColor.RED + "Invalid job type. Available job types: "
                                + ChatColor.GRAY + Arrays.toString(JobAPI.JOB_TYPE.values()));
                        return true;
                    }
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "Unrecognized command. Use /jobs help for assistance.");
                    return true;
                }
            }
        }
        return false;
    }

    public static void openJobPlayerMenu(Player player, JobAPI.JOB_TYPE jobType) {
        Inventory jobPlayerMenu = Bukkit.createInventory(null, 54, ChatColor.GRAY + "[" + jobType.toString() + ChatColor.GRAY + "]" + ChatColor.GOLD + " User Directory");

        // Iterate through online players to find players belonging to the specified job
        for (OfflinePlayer allOffline : Bukkit.getOfflinePlayers()) {
            JobAPI.JobPlayer jobPlayer = JobAPI.getPlayer(allOffline.getName());
            if (jobPlayer != null && jobPlayer.getJob() == jobType) {
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + allOffline.getName());
                meta.setLore(Arrays.asList(ChatColor.YELLOW + jobType.toString()));
                meta.setOwningPlayer(allOffline);
                playerHead.setItemMeta(meta);
                jobPlayerMenu.addItem(playerHead);
            }
        }

        // Add back button to return to job selector menu
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "Back to Job Selection");
        backButton.setItemMeta(backMeta);
        jobPlayerMenu.setItem(53, backButton);

        // Check if the player has tasks for this job
        JobAPI.JobPlayer playerJob = JobAPI.getPlayer(player.getName());
        if (playerJob != null && playerJob.getJob() == jobType) {
            ItemStack taskButton = new ItemStack(Material.DIAMOND);
            ItemMeta taskMeta = taskButton.getItemMeta();
            taskMeta.setDisplayName(ChatColor.GREEN + "Job Tasks");
            taskButton.setItemMeta(taskMeta);
            jobPlayerMenu.setItem(52, taskButton);
        } else {
            // Deny task access if the player doesn't have this job
            ItemStack denyButton = new ItemStack(Material.BARRIER);
            ItemMeta denyMeta = denyButton.getItemMeta();
            denyMeta.setDisplayName(ChatColor.RED + "No Tasks Available");
            denyButton.setItemMeta(denyMeta);
            jobPlayerMenu.setItem(52, denyButton);
        }

        player.openInventory(jobPlayerMenu);
    }
}