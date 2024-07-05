package com.steve_rizzo.emeraldscore.features.jobs;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JobHandler implements Listener, CommandExecutor, TabCompleter {

    private final Plugin core = Main.core;
    private static final HashMap<Integer, Job> jobs = new HashMap<>();
    private static int jobIdCounter = 1;
    static FileConfiguration jobDataConfig = Main.core.jobDataConfig;
    static File jobDataYml = Main.core.jobDataYML;

    String prefix = Main.prefix;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            if (command.getName().equalsIgnoreCase("job")) {
                if (args.length == 0) {
                    player.sendMessage(prefix + ChatColor.YELLOW + "Use: " + ChatColor.AQUA + "/job <list/delist/show/assign/complete> [price/id] [task info]");
                    return true;
                } else {
                    switch (args[0].toLowerCase()) {
                        case "show":
                            listJobs(player);
                            return true;
                        case "list":
                            if (args.length > 2) {
                                try {
                                    int price = Integer.parseInt(args[1]);
                                    String taskInfo = Stream.of(args).skip(2).collect(Collectors.joining(" "));
                                    if (EmeraldsCashAPI.returnBalance(player) >= price) {
                                        EmeraldsCashAPI.deductFunds(player, price);
                                        listJob(player, price, taskInfo);
                                    } else {
                                        player.sendMessage(prefix + ChatColor.RED + "Insufficient funds to list the job.");
                                    }
                                } catch (NumberFormatException e) {
                                    player.sendMessage(prefix + ChatColor.RED + "Invalid price.");
                                }
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "Usage: /job list <price> <task info>");
                            }
                            return true;

                        case "delist":
                            if (args.length == 2) {
                                try {
                                    int jobId = Integer.parseInt(args[1]);
                                    Job job = jobs.get(jobId);
                                    if (job != null && job.getLister().equals(player.getUniqueId())) {
                                        EmeraldsCashAPI.addFunds(player, job.getPrice());
                                        delistJob(player, jobId);
                                    } else {
                                        player.sendMessage(prefix + ChatColor.RED + "Job not found or you are not the lister.");
                                    }
                                } catch (NumberFormatException e) {
                                    player.sendMessage(prefix + ChatColor.RED + "Invalid job ID.");
                                }
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "Usage: /job delist <id>");
                            }
                            return true;

                        case "assign":
                            if (args.length == 2) {
                                try {
                                    int jobId = Integer.parseInt(args[1]);
                                    Job job = jobs.get(jobId);
                                    if (job != null) {
                                        if (job.getAssignee() == null) {
                                            assignJob(player, jobId);
                                            player.sendMessage(prefix + ChatColor.GREEN + "Successfully assigned job.");
                                        } else {
                                            player.sendMessage(prefix + ChatColor.RED + "Job already assigned to a user.");
                                        }
                                        return true;
                                    }
                                } catch (NumberFormatException e) {
                                    player.sendMessage(prefix + ChatColor.RED + "Invalid job ID.");
                                }
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "Usage: /job assign <id>");
                            }
                            return true;

                        case "cancel":
                            if (args.length == 2) {
                                try {
                                    int jobId = Integer.parseInt(args[1]);
                                    cancelJob(player, jobId);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(prefix + ChatColor.RED + "Invalid job ID.");
                                }
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "Usage: /job cancel <id>");
                            }
                            return true;

                        case "complete":
                            if (args.length == 2) {
                                try {
                                    int jobId = Integer.parseInt(args[1]);
                                    completeJob(player, jobId);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(prefix + ChatColor.RED + "Invalid job ID.");
                                }
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "Usage: /job complete <id>");
                            }
                            return true;

                        case "unassign":
                            if (args.length == 2) {
                                try {
                                    int jobId = Integer.parseInt(args[1]);
                                    unassignJob(player, jobId);
                                    player.sendMessage(prefix + ChatColor.RED + "Successfully un-assigned job: " + ChatColor.YELLOW + jobId + ChatColor.RED + "!");
                                } catch (NumberFormatException e) {
                                    player.sendMessage(prefix + ChatColor.RED + "Invalid job ID.");
                                }
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "Usage: /job unassign <id>");
                            }
                            return true;

                        default:
                            player.sendMessage(prefix + ChatColor.RED + "Unknown command.");
                            return true;
                    }
                }
            } else if (command.getName().equalsIgnoreCase("jobs")) {
                openJobsMenu(player);
                return true;
            }
        } else {
            sender.sendMessage(prefix + ChatColor.RED + "Only players can use this command.");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("job")) {
            List<String> completions = new ArrayList<>();
            if (args.length == 1) {
                completions.add("list");
                completions.add("delist");
                completions.add("show");
                completions.add("assign");
                completions.add("cancel");
                completions.add("complete");
            }
            return completions;
        }
        return null;
    }

    private void listJob(Player player, int price, String taskInfo) {
        Job job = new Job(jobIdCounter++, player.getUniqueId(), price, taskInfo);
        jobs.put(job.getId(), job);
        saveJobToConfig(job);
        player.sendMessage(prefix + ChatColor.GREEN + "Job listed with ID: " + job.getId());

        String broadcastMessage =
                ChatColor.GRAY + "======[" + ChatColor.GREEN + "EmeraldsMC" + ChatColor.GRAY + "]======\n" +
                        ChatColor.YELLOW + "A new job has been listed by " + ServerJoinPlayer.getPlayerPrefixAndName(player) +
                        ChatColor.AQUA + "\nTask: " + ChatColor.GRAY + taskInfo +
                        ChatColor.AQUA + "\nPrice: " + ChatColor.GREEN + "$" + price +
                        ChatColor.AQUA + "\nJob ID: " + ChatColor.YELLOW + job.getId() +
                        "\n" +
                        ChatColor.GRAY + "\nUse " + ChatColor.AQUA + "/jobs" + ChatColor.GRAY + " to assign this job!" +
                        ChatColor.GRAY + "\n======[" + ChatColor.GREEN + "EmeraldsMC" + ChatColor.GRAY + "]======";
        Bukkit.getServer().broadcastMessage(broadcastMessage);

        // Truncate taskInfo if it's too long
        String truncatedTaskInfo = taskInfo.length() > 50 ? taskInfo.substring(0, 50) + "..." : taskInfo;

        // Create and send an embedded message to Discord
        TextChannel channel = DiscordSRV.getPlugin().getMainGuild().getTextChannelById("1204459087778160711");
        if (channel != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Emeralds Jobs - New Listing")
                    .setColor(0x00FF00) // Green color
                    .addField("Listed By:", player.getName(), true)
                    .addField("Task Info:", truncatedTaskInfo, true)
                    .addField("Job Pay:", "$" + price, true)
                    .addField("Job ID:", String.valueOf(job.getId()), true)
                    .setFooter("Use /jobs in-game to assign this job!");

            channel.sendMessageEmbeds(embedBuilder.build()).queue(
                    success -> {
                        System.out.println("[EmeraldsJobs]: Successfully posted job listing to Discord.");
                    },
                    failure -> {
                        player.sendMessage(prefix + ChatColor.RED + "Failed to send job listing to Discord. Please report to admin. [ERR_CHAN_NF]");
                        System.out.println("[EmeraldsJobs]: Failed to post job listing - " + failure.getMessage());
                    }
            );
        } else {
            player.sendMessage(prefix + ChatColor.RED + "Failed to send job listing to Discord. Please report to admin. [ERR_CHAN_NF]");
            System.out.println("[EmeraldsJobs]: Failed to post job listing - Discord channel not found for ID: 1204459087778160711");
        }
    }

    private void delistJob(Player player, int jobId) {
        Job job = jobs.get(jobId);
        if (job != null && job.getLister().equals(player.getUniqueId())) {
            jobs.remove(jobId);
            removeJobFromConfig(jobId);
            player.sendMessage(prefix + ChatColor.GREEN + "Job delisted with ID: " + jobId);
        } else {
            player.sendMessage(prefix + ChatColor.RED + "Job not found or you are not the lister.");
        }
    }

    private void listJobs(Player player) {
        if (jobs.isEmpty()) {
            player.sendMessage(prefix + ChatColor.YELLOW + "No jobs available.");
        } else {
            player.sendMessage(prefix + ChatColor.GREEN + "Listed Jobs:");
            for (Job job : jobs.values()) {
                String assigned = ChatColor.GREEN + "AVAILABLE";
                if (job.getAssignee() != null) assigned = ChatColor.RED + "ASSIGNED";
                player.sendMessage(ChatColor.GRAY + "ID: " + ChatColor.AQUA + job.getId() + ChatColor.GRAY + ", Price: " + ChatColor.GREEN + job.getPrice()
                        + ChatColor.GRAY + ", Task: " + ChatColor.YELLOW + job.getTaskInfo() + ChatColor.GRAY + ", Assigned: " + assigned);
            }
        }
    }

    private void assignJob(Player player, int jobId) {
        Job job = jobs.get(jobId);
        if (job != null && job.getAssignee() == null) {
            job.setAssignee(player.getUniqueId());
            saveJobToConfig(job);
            player.sendMessage(prefix + ChatColor.GREEN + "Job assigned with ID: " + jobId);
        } else {
            player.sendMessage(prefix + ChatColor.RED + "Job not found or already assigned.");
        }
    }

    private void unassignJob(Player player, int jobId) {
        Job job = jobs.get(jobId);
        if (job != null) {
            UUID listerUUID = job.getLister();
            UUID assigneeUUID = job.getAssignee();

            // Check if the player is either the lister or the assignee
            if (listerUUID.equals(player.getUniqueId()) || (assigneeUUID != null && assigneeUUID.equals(player.getUniqueId()))) {
                if (job.getAssignee() != null) {
                    // Unassign the job
                    job.setAssignee(null);
                    saveJobToConfig(job);
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "Job is not assigned to anyone.");
                }
            } else {
                player.sendMessage(prefix + ChatColor.RED + "You are not authorized to unassign this job.");
            }
        } else {
            player.sendMessage(prefix + ChatColor.RED + "Job not found.");
        }
    }

    private void cancelJob(Player player, int jobId) {
        Job job = jobs.get(jobId);
        if (job != null && (job.getLister().equals(player.getUniqueId()) || job.getAssignee().equals(player.getUniqueId()))) {
            job.setAssignee(null);
            saveJobToConfig(job);
            player.sendMessage(prefix + ChatColor.GREEN + "Job assignment canceled with ID: " + jobId);
        } else {
            player.sendMessage(prefix + ChatColor.RED + "Job not found or you are not authorized.");
        }
    }

    private void completeJob(Player player, int jobId) {
        Job job = jobs.get(jobId);
        if (job != null && (job.getLister().equals(player.getUniqueId()) || job.getAssignee().equals(player.getUniqueId()))) {
            if (job.isCompletedByLister() && job.isCompletedByAssignee()) {
                player.sendMessage(prefix + ChatColor.GREEN + "Job already marked as complete by both parties.");
                return;
            }

            // Mark job as complete by the appropriate party
            if (job.getLister().equals(player.getUniqueId())) {
                job.setCompletedByLister(true);
            } else if (job.getAssignee().equals(player.getUniqueId())) {
                job.setCompletedByAssignee(true);
            }

            player.sendMessage(prefix + ChatColor.GREEN + "Job marked as complete with ID: " + jobId);

            // Check if both parties have marked the job as complete
            if (job.isCompletedByLister() && job.isCompletedByAssignee()) {
                UUID listerUUID = job.getLister();
                UUID assigneeUUID = job.getAssignee();
                int amount = job.getPrice();

                // Transfer payment from lister to assignee
                if (EmeraldsCashAPI.returnBalance(Bukkit.getPlayer(listerUUID)) >= amount) {

                    EmeraldsCashAPI.addFunds(Bukkit.getPlayer(assigneeUUID), amount);

                    Player lister = Bukkit.getPlayer(listerUUID);
                    Player assignee = Bukkit.getPlayer(assigneeUUID);

                    // Notify both parties about the payment
                    if (lister != null && lister.isOnline()) {
                        assert assignee != null;
                        lister.sendMessage(prefix + ChatColor.YELLOW + "Job payment of " + ChatColor.GREEN + "$" + amount + ChatColor.YELLOW + " Emeralds Cash processed to " + ServerJoinPlayer.getPlayerPrefixAndName(assignee.getName()));
                    }

                    if (assignee != null && assignee.isOnline()) {
                        assert lister != null;
                        assignee.sendMessage(prefix + ChatColor.YELLOW + "You have received a payment of " + ChatColor.GREEN + "$" + amount + ChatColor.YELLOW + " Emeralds Cash from " + ServerJoinPlayer.getPlayerPrefixAndName(lister.getName()));
                    }

                    player.sendMessage(prefix + ChatColor.GREEN + "Job payment processed.");
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "Lister does not have enough funds to complete the payment.");
                    if (job.getLister().equals(player.getUniqueId())) {
                        player.sendMessage(prefix + ChatColor.RED + "You do not have enough funds to complete the payment.");
                    } else if (job.getAssignee().equals(player.getUniqueId())) {
                        player.sendMessage(prefix + ChatColor.RED + "Lister does not have enough funds to complete the payment.");
                    }
                }
                // Remove the job from config after completion
                jobs.remove(jobId);
                removeJobFromConfig(jobId);
            }
        } else {
            player.sendMessage(prefix + ChatColor.RED + "Job not found or you are not authorized.");
        }
    }

    private void openJobsMenu(Player player) {
        Inventory jobsMenu = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "" + ChatColor.BOLD + ":mc_diamond_axe: " + "Available Jobs" + " :mc_diamond_axe:");

        if (jobs.isEmpty()) {
            // If no jobs are listed, show a red wool block with message
            ItemStack noJobsItem = new ItemStack(Material.RED_WOOL);
            ItemMeta noJobsMeta = noJobsItem.getItemMeta();
            noJobsMeta.setDisplayName(ChatColor.RED + "NO JOBS POSTED");
            noJobsItem.setItemMeta(noJobsMeta);

            jobsMenu.setItem(22, noJobsItem); // Place the red wool in the center
        } else {
            int slot = 0;
            for (Job job : jobs.values()) {
                ItemStack jobItem = new ItemStack(Material.PAPER);
                ItemMeta meta = jobItem.getItemMeta();
                assert meta != null;
                meta.setDisplayName(ChatColor.GOLD + "JOB #" + ChatColor.YELLOW + job.getId());
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.YELLOW + "• Posted By: " + ChatColor.GRAY + Bukkit.getOfflinePlayer(job.getLister()).getName());
                lore.add(ChatColor.YELLOW + "• Job Pay: " + ChatColor.GREEN + "$" + job.getPrice());
                lore.add(ChatColor.YELLOW + "• Job Description: " + ChatColor.GRAY + job.getTaskInfo());
                lore.add("");
                if (job.getAssignee() != null) {
                    lore.add(ChatColor.YELLOW + "• Assigned to: " + ChatColor.RED + Bukkit.getOfflinePlayer(job.getAssignee()).getName());
                } else {
                    lore.add(ChatColor.YELLOW + "• Assigned to: " + ChatColor.GREEN + "AVAILABLE");
                }
                lore.add("");
                lore.add(ChatColor.LIGHT_PURPLE + "Click to Assign/Un-assign Job");
                meta.setLore(lore);
                jobItem.setItemMeta(meta);

                jobsMenu.setItem(slot++, jobItem);

                if (slot >= 54) break; // Maximum inventory size reached
            }
        }

        player.openInventory(jobsMenu);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory != null && ChatColor.stripColor(event.getView().getTitle()).equals(ChatColor.stripColor(ChatColor.YELLOW + "" + ChatColor.BOLD + ":mc_diamond_axe: Available Jobs :mc_diamond_axe:"))) {
            event.setCancelled(true); // Cancel dragging items out of the inventory

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.PAPER && clickedItem.hasItemMeta()) {
                ItemMeta meta = clickedItem.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    String displayName = ChatColor.stripColor(meta.getDisplayName());
                    if (displayName.startsWith("JOB #")) {
                        try {
                            // Extract job ID from display name ("JOB #<ID>")
                            String idString = displayName.replace("JOB #", "").trim();
                            int jobId = Integer.parseInt(idString);
                            Job job = jobs.get(jobId);
                            if (job != null) {
                                if (job.getAssignee() == null) {
                                    assignJob(player, jobId);
                                    player.sendMessage(prefix + ChatColor.GREEN + "Successfully assigned job: " + ChatColor.YELLOW + jobId + ChatColor.GREEN + "!");
                                    player.closeInventory();
                                } else if (job.getAssignee().equals(player.getUniqueId())) {
                                    unassignJob(player, jobId);
                                    player.sendMessage(prefix + ChatColor.RED + "Successfully un-assigned job: " + ChatColor.YELLOW + jobId + ChatColor.RED + "!");
                                    player.closeInventory();
                                } else {
                                    player.sendMessage(prefix + ChatColor.RED + "Job is already assigned.");
                                }
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "Job not found.");
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(prefix + ChatColor.RED + "Invalid job ID format.");
                        }
                    }
                }
            }
        }
    }

    private void saveJobToConfig(Job job) {
        String path = "jobs." + job.getId();
        jobDataConfig.set(path + ".lister", job.getLister().toString());
        jobDataConfig.set(path + ".price", job.getPrice());
        jobDataConfig.set(path + ".taskInfo", job.getTaskInfo());
        jobDataConfig.set(path + ".assignee", job.getAssignee() != null ? job.getAssignee().toString() : null);
        jobDataConfig.set(path + ".completedByLister", job.isCompletedByLister());
        jobDataConfig.set(path + ".completedByAssignee", job.isCompletedByAssignee());
        Main.core.saveYML(jobDataConfig, jobDataYml);
    }

    private void removeJobFromConfig(int jobId) {
        String path = "jobs." + jobId;
        jobDataConfig.set(path, null);
        Main.core.saveYML(jobDataConfig, jobDataYml);
    }

    public static void loadJobsFromConfig() {
        if (jobDataConfig.contains("jobs")) {
            for (String key : Objects.requireNonNull(jobDataConfig.getConfigurationSection("jobs")).getKeys(false)) {
                int id = Integer.parseInt(key);
                UUID lister = UUID.fromString(Objects.requireNonNull(jobDataConfig.getString("jobs." + id + ".lister")));
                int price = jobDataConfig.getInt("jobs." + id + ".price");
                String taskInfo = jobDataConfig.getString("jobs." + id + ".taskInfo");
                UUID assignee = jobDataConfig.contains("jobs." + id + ".assignee") ? UUID.fromString(Objects.requireNonNull(jobDataConfig.getString("jobs." + id + ".assignee"))) : null;
                boolean completedByLister = jobDataConfig.getBoolean("jobs." + id + ".completedByLister");
                boolean completedByAssignee = jobDataConfig.getBoolean("jobs." + id + ".completedByAssignee");

                Job job = new Job(id, lister, price, taskInfo);
                job.setAssignee(assignee);
                job.setCompletedByLister(completedByLister);
                job.setCompletedByAssignee(completedByAssignee);
                jobs.put(id, job);

                if (id >= jobIdCounter) {
                    jobIdCounter = id + 1;
                }
            }
        }
    }

    private static class Job {
        private final int id;
        private final UUID lister;
        private final int price;
        private final String taskInfo;
        private UUID assignee;
        private boolean completedByLister;
        private boolean completedByAssignee;

        public Job(int id, UUID lister, int price, String taskInfo) {
            this.id = id;
            this.lister = lister;
            this.price = price;
            this.taskInfo = taskInfo;
            this.assignee = null;
            this.completedByLister = false;
            this.completedByAssignee = false;
        }

        public int getId() {
            return id;
        }

        public UUID getLister() {
            return lister;
        }

        public int getPrice() {
            return price;
        }

        public String getTaskInfo() {
            return taskInfo;
        }

        public UUID getAssignee() {
            return assignee;
        }

        public void setAssignee(UUID assignee) {
            this.assignee = assignee;
        }

        public boolean isCompletedByLister() {
            return completedByLister;
        }

        public void setCompletedByLister(boolean completedByLister) {
            this.completedByLister = completedByLister;
        }

        public boolean isCompletedByAssignee() {
            return completedByAssignee;
        }

        public void setCompletedByAssignee(boolean completedByAssignee) {
            this.completedByAssignee = completedByAssignee;
        }
    }
}