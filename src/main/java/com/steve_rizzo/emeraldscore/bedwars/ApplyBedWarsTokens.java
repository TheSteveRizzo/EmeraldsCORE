package com.steve_rizzo.emeraldscore.bedwars;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.tokens.TokensAPI;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ApplyBedWarsTokens implements CommandExecutor {

    private final Main core = Main.core;
    public static Permission perms = Main.perms;

    private static final Map<String, Integer> baseTokens = new HashMap<>();

    static {
        baseTokens.put("KILL", 1);
        baseTokens.put("BED", 3);
        baseTokens.put("WIN", 5);
    }

    private static final List<String> TIER_1 = Arrays.asList("guest", "member");
    private static final List<String> TIER_2 = Arrays.asList("donor1", "donor2");
    private static final List<String> TIER_3 = Arrays.asList("donor3", "donor4");
    private static final List<String> TIER_4 = Arrays.asList("owner", "admin", "mod", "helper", "youtube", "builder", "platinum", "elite");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player player) {
            String message = player.isOp()
                    ? "Only the system can issue this command."
                    : "No permission.";
            player.sendMessage(Main.prefix + ChatColor.RED + message);
            return true;
        }

        // Console/system-only
        // Usage: /applybedwarstokens <player> <KILL|BED|WIN>
        if (args.length != 2) {
            sender.sendMessage(Main.prefix + ChatColor.RED + "Usage: /applybedwarstokens <player> <KILL|BED|WIN>");
            return false;
        }

        String playerName = args[0];
        String awardType = args[1].toUpperCase();

        if (!baseTokens.containsKey(awardType)) {
            sender.sendMessage(Main.prefix + ChatColor.RED + "Invalid token type: " + awardType);
            return false;
        }

        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage(Main.prefix + ChatColor.RED + "Player not found.");
            return false;
        }

        String group = perms.getPrimaryGroup(target).toLowerCase();
        int multiplier = getMultiplierForGroup(group);
        int tokensAwarded = baseTokens.get(awardType) + multiplier;

        TokensAPI.addTokens(target, tokensAwarded);
        Bukkit.getLogger().log(Level.INFO, "Awarded " + tokensAwarded + " tokens to " + target.getName() +
                " (Group: " + group + ", Multiplier: " + multiplier + ", AwardType: " + awardType + ")");
        target.sendMessage(Main.prefix + ChatColor.GRAY + "The amount of " + ChatColor.GREEN + tokensAwarded + ChatColor.GRAY + " TOKENS were added to your balance.");
        return true;
    }

    private int getMultiplierForGroup(String group) {
        if (TIER_1.contains(group)) return 0;
        if (TIER_2.contains(group)) return 1;
        if (TIER_3.contains(group)) return 2;
        if (TIER_4.contains(group)) return 3;
        return 0;
    }
}
