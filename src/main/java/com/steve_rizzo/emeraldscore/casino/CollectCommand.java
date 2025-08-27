package com.steve_rizzo.emeraldscore.casino;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CollectCommand implements CommandExecutor {

    public static Permission perms = Main.perms;
    private final String prefix = Main.prefix;

    private final File incomeFile;
    private final FileConfiguration incomeConfig;

    public CollectCommand() {
        // Initialize the income.yml file
        incomeFile = new File(Main.getPlugin(Main.class).getDataFolder(), "income.yml");
        if (!incomeFile.exists()) {
            try {
                incomeFile.getParentFile().mkdirs();
                incomeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        incomeConfig = YamlConfiguration.loadConfiguration(incomeFile);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        String group = perms.getPrimaryGroup(player).toLowerCase();

        // Check if the group exists in Income enum
        Income income;
        try {
            income = Income.valueOf(group.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(prefix + ChatColor.RED + "ERROR: " + ChatColor.GRAY + "Your rank does not have an income!");
            return true;
        }

        // Cooldown check
        long lastCollected = incomeConfig.getLong(uuid.toString(), 0);
        long now = System.currentTimeMillis();
        long cooldown = 12 * 60 * 60 * 1000L; // 12 hours in milliseconds

        if (now - lastCollected < cooldown) {
            long remaining = cooldown - (now - lastCollected);

            long hr = remaining / 1000 / 60 / 60;
            long min = (remaining / 1000 / 60) % 60;
            long sec = (remaining / 1000) % 60;

            player.sendMessage(prefix + ChatColor.GRAY + "You have already collected your income! Collect again in: "
                    + ChatColor.AQUA + hr + ChatColor.GRAY + "H "
                    + ChatColor.AQUA + min + ChatColor.GRAY + "M "
                    + ChatColor.AQUA + sec + ChatColor.GRAY + "S");
            return true;
        }

        // Give income
        int amount = income.getAmount();
        EmeraldsCashAPI.addFunds(player, amount);
        player.sendMessage(prefix + ChatColor.GRAY + "You have collected "
                + ChatColor.GREEN + "$" + amount + ChatColor.GRAY + " for "
                + ChatColor.AQUA + group.toUpperCase() + ChatColor.GRAY + "'s income!");

        // Update YML
        incomeConfig.set(uuid.toString(), now);
        try {
            incomeConfig.save(incomeFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(prefix + ChatColor.RED + "An error occurred while saving your income claim.");
        }

        return true;
    }
}
