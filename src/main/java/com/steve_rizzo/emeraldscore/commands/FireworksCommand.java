package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FireworksCommand implements CommandExecutor {

    private boolean fireworksEnabled = false;
    private final Location centerLocation = new Location(Bukkit.getWorlds().get(0), -170, 69, 417);
    private final Random random = new Random();
    private final Color[] colors = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE, Color.ORANGE, Color.WHITE
    };

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

        String arg = args[0].toLowerCase();
        if (arg.equals("on")) {
            fireworksEnabled = true;
            sender.sendMessage(Main.prefix + ChatColor.AQUA + "Spawn Fireworks: " + ChatColor.GREEN + "ENABLED" + ChatColor.AQUA + ".");
            startFireworksTask();
        } else if (arg.equals("off")) {
            fireworksEnabled = false;
            sender.sendMessage(Main.prefix + ChatColor.AQUA + "Spawn Fireworks: " + ChatColor.RED + "DISABLED" + ChatColor.AQUA + ".");
        } else {
            sender.sendMessage(Main.prefix + ChatColor.GRAY + "Usage: " + ChatColor.AQUA + "/fireworks <on|off>");
        }
        return true;
    }

    private void startFireworksTask() {
        new BukkitRunnable() {
            double angle = 0;
            double radius = 25; // Reduced radius
            double increment = Math.toRadians(15); // Adjust the increment to change the spacing between fireworks

            @Override
            public void run() {
                if (!fireworksEnabled) {
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
        }.runTaskTimer(Main.core, 0, 5); // Adjust the delay to change the speed of the sweeping motion
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
