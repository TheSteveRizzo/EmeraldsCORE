package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BoostCommand implements CommandExecutor {

    private final Map<Player, Long> cooldowns = new HashMap<>();
    private final long COOLDOWN_TIME = 15 * 60 * 1000; // 15 minutes in milliseconds

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("emeraldsmc.boostcommand")) {
            player.sendMessage(Main.prefix + ChatColor.RED + "You don't have permission to use this command!");
            return false;
        }

        if (isOnCooldown(player)) {
            long remainingTime = getRemainingCooldown(player);
            player.sendMessage(Main.prefix + ChatColor.RED + "This command is on cooldown. Please wait " + remainingTime / 1000 + " seconds.");
            return false;
        }

        // Apply cooldown
        applyCooldown(player);

        // Spawn fireworks
        for (int i = 0; i < 5; i++) { // Spawn 5 different fireworks
            spawnFireworks(player.getLocation());
        }

        // Apply particle effects
        spawnParticleEffects(player);

        // Launch player into the air
        Vector boostVector = player.getLocation().getDirection().multiply(2).setY(1.5); // Adjust the Y value as needed
        player.setVelocity(boostVector);

        player.sendMessage(Main.prefix + ChatColor.GREEN + "You've been " + ChatColor.AQUA + "BOOSTED" + ChatColor.GREEN + "!");

        return true;
    }

    private void spawnFireworks(Location location) {
        // Random firework effects
        Random random = new Random();
        for (int i = 0; i < 20; i++) { // Spawn 20 fireworks
            Firework firework = location.getWorld().spawn(location, Firework.class);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            FireworkEffect.Type[] types = FireworkEffect.Type.values();
            FireworkEffect.Type randomType = types[random.nextInt(types.length)];

            Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE};
            Color randomColor = colors[random.nextInt(colors.length)];

            fireworkMeta.addEffect(FireworkEffect.builder()
                    .flicker(true)
                    .trail(true)
                    .with(randomType)
                    .withColor(randomColor)
                    .build());
            fireworkMeta.setPower(1);
            firework.setFireworkMeta(fireworkMeta);
        }
    }


    private void spawnParticleEffects(Player player) {
        // Random particle colors
        Random random = new Random();
        for (int i = 0; i < 100; i++) { // Spawn 100 particles
            double radius = 2; // Radius around the player
            double angle = random.nextDouble() * Math.PI * 2;
            double x = player.getLocation().getX() + radius * Math.cos(angle);
            double y = player.getLocation().getY() + random.nextDouble() * 2; // Height variation
            double z = player.getLocation().getZ() + radius * Math.sin(angle);

            Location particleLoc = new Location(player.getWorld(), x, y, z);
            player.spawnParticle(Particle.FIREWORKS_SPARK, particleLoc, 1);
        }

        // Schedule a task to stop particles after 5 seconds (100 ticks per second)
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            // Stop particle effects
            for (int i = 0; i < 100; i++) {
                player.spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 0);
            }
        }, 5 * 20); // 5 seconds (20 ticks per second)
    }

    private boolean isOnCooldown(Player player) {
        return cooldowns.containsKey(player);
    }

    private void applyCooldown(Player player) {
        cooldowns.put(player, System.currentTimeMillis());
    }

    private long getRemainingCooldown(Player player) {
        long cooldownStart = cooldowns.get(player);
        long currentTime = System.currentTimeMillis();
        return COOLDOWN_TIME - (currentTime - cooldownStart);
    }
}
