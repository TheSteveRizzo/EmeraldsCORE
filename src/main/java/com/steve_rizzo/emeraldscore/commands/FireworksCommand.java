package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FireworksCommand implements CommandExecutor {

    private boolean fireworksEnabled = false;
    private Location centerLocation;
    private World activeFireworkWorld;
    private final Random random = new Random();
    private final Color[] colors = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE, Color.ORANGE, Color.WHITE
    };
    private BukkitRunnable fireworkTask;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Main.prefix + ChatColor.GRAY + "Usage: " + ChatColor.AQUA + "/fireworks <on|off>");
            return true;
        }

        if (!sender.hasPermission("emeraldsmc.fireworkadmin")) {
            sender.sendMessage(Main.prefix + ChatColor.RED + "No permission!");
            return true;
        }

        if (sender instanceof Player) {
            Player p = (Player) sender;

            String arg = args[0].toLowerCase();
            if (arg.equals("on")) {
                if (fireworksEnabled && activeFireworkWorld != null && activeFireworkWorld.equals(p.getWorld())) {
                    sender.sendMessage(Main.prefix + ChatColor.RED + "Fireworks are already enabled in this world.");
                    return true;
                }

                centerLocation = p.getWorld().getSpawnLocation();
                toggleFireworksOn(p.getWorld());
                sender.sendMessage(Main.prefix + ChatColor.AQUA + "Spawn Fireworks: " + ChatColor.GREEN + "ENABLED" + ChatColor.AQUA + " in world " + ChatColor.GOLD + p.getWorld().getName());
            } else if (arg.equals("off")) {
                if (fireworksEnabled && activeFireworkWorld.equals(p.getWorld())) {
                    toggleFireworksOff();
                    sender.sendMessage(Main.prefix + ChatColor.AQUA + "Spawn Fireworks: " + ChatColor.RED + "DISABLED" + ChatColor.AQUA + " in world " + ChatColor.GOLD + p.getWorld().getName());
                } else {
                    sender.sendMessage(Main.prefix + ChatColor.RED + "Fireworks are not enabled in this world.");
                }
            } else {
                sender.sendMessage(Main.prefix + ChatColor.GRAY + "Usage: " + ChatColor.AQUA + "/fireworks <on|off>");
            }
            return true;
        }

        return false;
    }

    private void toggleFireworksOn(World world) {
        // Disable fireworks in the previous world if it exists
        if (fireworksEnabled && activeFireworkWorld != null && fireworkTask != null) {
            toggleFireworksOff();
        }

        // Enable fireworks in the new world
        fireworksEnabled = true;
        activeFireworkWorld = world;
        startFireworksTask();
    }

    private void toggleFireworksOff() {
        fireworksEnabled = false;
        if (fireworkTask != null) {
            fireworkTask.cancel();
            fireworkTask = null;
        }
        activeFireworkWorld = null;
    }

    private void startFireworksTask() {
        fireworkTask = new BukkitRunnable() {
            double angle = 0;
            double radius = 25; // Reduced radius
            double increment = Math.toRadians(15); // Adjust the increment to change the spacing between fireworks

            @Override
            public void run() {
                if (!fireworksEnabled || activeFireworkWorld == null) {
                    cancel();
                    return;
                }

                // Calculate the position of the next firework
                double x = centerLocation.getX() + radius * Math.cos(angle);
                double z = centerLocation.getZ() + radius * Math.sin(angle);
                Location fireworkLocation = new Location(centerLocation.getWorld(), x, centerLocation.getY(), z);

                spawnFirework(fireworkLocation);

                // Update the angle for the next firework
                angle += increment;
                if (angle >= Math.PI * 2) {
                    angle = 0; // Reset angle to sweep in a circle
                }
            }
        };

        fireworkTask.runTaskTimer(Main.core, 0, 5); // Adjust the delay to change the speed of the sweeping motion
    }

    private void spawnFirework(Location location) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(1); // Set the power of the firework
        meta.addEffect(FireworkEffect.builder()
                .withColor(getRandomColor())
                .flicker(true)
                .build()); // Customize firework effect
        firework.setFireworkMeta(meta);
    }

    private Color getRandomColor() {
        return colors[random.nextInt(colors.length)];
    }
}