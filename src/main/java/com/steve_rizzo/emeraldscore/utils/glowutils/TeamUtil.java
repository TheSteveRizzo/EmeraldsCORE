package com.steve_rizzo.emeraldscore.utils.glowutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;


public class TeamUtil {
    static Map<String, String> teamEntries = new HashMap();
    static Map<ChatColor, String> colorNames = new HashMap();

    static ScoreboardManager manager = Bukkit.getScoreboardManager();
    public static Scoreboard board = manager.getNewScoreboard();


    public static void onEnable() {
        addColorNames();
    }


    public static void onLeave(Player p) {
        if (teamEntries.containsKey(p.getName())) {
            Team team = board.getTeam((String) teamEntries.get(p.getName()));
            team.removeEntry(p.getName());
            team.unregister();
            teamEntries.remove(p.getName());
        }

        if (getGlowing(p)) {
            setGlowing(p, false);
        }
    }


    public static void setTeam(Player p, ChatColor color) {
        String priority = "";
        String playerName = p.getName();
        String shortPlayerName = p.getName();
        String colorName = (String) colorNames.get(color);

        if (shortPlayerName.length() > 8) {
            shortPlayerName = shortPlayerName.substring(0, 8);
        }

        if (teamEntries.containsKey(playerName)) {
            String oldTeamName = (String) teamEntries.get(playerName);
            String newTeamName = String.valueOf(priority) + shortPlayerName + colorName;

            if (!newTeamName.equals(oldTeamName)) {
                Team oldTeam = getPlayerTeam(p);
                Team newTeam = board.registerNewTeam(String.valueOf(priority) + shortPlayerName + colorName);

                if (color != null) {
                    newTeam.setColor(color);
                }

                newTeam.addEntry(p.getName());
                oldTeam.removeEntry(playerName);
                oldTeam.unregister();

                teamEntries.remove(playerName);
                teamEntries.put(playerName, newTeam.getName());
            }
        } else {
            Team newTeam = board.registerNewTeam(String.valueOf(priority) + shortPlayerName + colorName);

            if (color != null) {
                newTeam.setColor(color);
            }

            newTeam.addEntry(p.getName());
            teamEntries.put(p.getName(), newTeam.getName());
        }
    }


    public static Team getPlayerTeam(Player p) {
        return board.getTeam((String) teamEntries.get(p.getName()));
    }


    public static boolean getGlowing(Player p) {
        return p.isGlowing();
    }


    public static void setGlowing(Player p, boolean status) {
        if (status) {
            p.setGlowing(true);
        } else {
            p.setGlowing(false);
        }
    }


    private static void addColorNames() {
        if (colorNames.isEmpty()) {
            colorNames.put(ChatColor.RED, "LRED");
            colorNames.put(ChatColor.DARK_RED, "DRED");
            colorNames.put(ChatColor.GOLD, "GOLD");
            colorNames.put(ChatColor.YELLOW, "YELL");
            colorNames.put(ChatColor.GREEN, "GREE");
            colorNames.put(ChatColor.DARK_GREEN, "DGRE");
            colorNames.put(ChatColor.AQUA, "AQUA");
            colorNames.put(ChatColor.DARK_AQUA, "DAQU");
            colorNames.put(ChatColor.BLUE, "BLUE");
            colorNames.put(ChatColor.DARK_BLUE, "DBLU");
            colorNames.put(ChatColor.LIGHT_PURPLE, "PINK");
            colorNames.put(ChatColor.DARK_PURPLE, "PURP");
            colorNames.put(ChatColor.WHITE, "WHIT");
            colorNames.put(ChatColor.GRAY, "GRAY");
            colorNames.put(ChatColor.DARK_GRAY, "DGRA");
            colorNames.put(ChatColor.BLACK, "BLAC");
        }
    }


    public static String getColor(Player p) {
        String priority = "";
        String shortPlayerName = p.getName();

        if (shortPlayerName.length() > 8) {
            shortPlayerName = shortPlayerName.substring(0, 8);
        }

        if (teamEntries.containsKey(p.getName())) {
            String teamName = (String) teamEntries.get(p.getName());

            teamName = teamName.replace(priority, "").replace(shortPlayerName, "");
            return getStringColor(teamName);
        }

        return "§f";
    }


    private static String getStringColor(String shortColor) {
        if (shortColor.equals("LRED"))
            return "§c";
        if (shortColor.equals("DRED"))
            return "§4";
        if (shortColor.equals("GOLD"))
            return "§6";
        if (shortColor.equals("YELL"))
            return "§e";
        if (shortColor.equals("GREE"))
            return "§a";
        if (shortColor.equals("DGRE"))
            return "§2";
        if (shortColor.equals("AQUA"))
            return "§b";
        if (shortColor.equals("DAQU"))
            return "§3";
        if (shortColor.equals("BLUE"))
            return "§9";
        if (shortColor.equals("DBLU"))
            return "§1";
        if (shortColor.equals("PINK"))
            return "§d";
        if (shortColor.equals("PURP"))
            return "§5";
        if (shortColor.equals("WHIT"))
            return "§f";
        if (shortColor.equals("GRAY"))
            return "§7";
        if (shortColor.equals("DGRA"))
            return "§8";
        if (shortColor.equals("BLAC")) {
            return "§0";
        }
        return "§f";
    }
}
