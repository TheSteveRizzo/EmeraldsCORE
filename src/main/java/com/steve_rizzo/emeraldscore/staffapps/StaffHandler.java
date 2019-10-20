package com.steve_rizzo.emeraldscore.staffapps;

import com.steve_rizzo.emeraldscore.Main;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class StaffHandler extends JavaPlugin {

    public static Plugin plugin;
    public static Connection connection;
    public static String prefix = ChatColor.GRAY + "[" + ChatColor.GREEN + "EmeraldApps" + ChatColor.GRAY + "]: ";
    private static String host = Main.hostStaff;
    private static String port = Main.portStaff;
    private static String password = Main.passwordStaff;
    private static String username = Main.usernameStaff;
    private static String name = Main.nameStaff;
    private static Permission perms = Main.perms;

    public static void setRank(Player p, String rank) {
        for (String group : perms.getPlayerGroups(p)) {
            perms.playerRemoveGroup(p, group);
        }


        perms.playerAddGroup(p, rank);
    }


    public static boolean hasAcceptedApp(Player p) {
        if (connection != null)
            try {
                PreparedStatement sql = connection.prepareStatement("SELECT * FROM `apps` WHERE ign=? AND status=?;");
                sql.setString(1, p.getName());
                sql.setString(2, "ACCEPTED");
                ResultSet resultSet = sql.executeQuery();
                boolean hasApp = resultSet.next();

                sql.close();
                resultSet.close();
                if (hasApp) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                System.out.println("Error checking if player has accepted app!");
                e.printStackTrace();

                return false;
            }
        return false;
    }

    public static void setPromoted(Player p) {
        if (connection != null) {
            try {
                PreparedStatement sql = connection.prepareStatement("UPDATE `apps` SET status=? WHERE ign=?;");
                sql.setString(1, "PROMOTED");
                sql.setString(2, p.getName());
                sql.executeUpdate();
                sql.close();
            } catch (Exception e) {
                System.out.println("-----------------------[EmeraldApps PROMOTE]-------------------------");
                e.printStackTrace();
                System.out.println("[EmeraldApps] ERROR: Couldn't update MySQL table.");
            }
        }
    }

    public static void setViewed(Player p) {
        if (connection != null) {
            try {
                PreparedStatement sql = connection.prepareStatement("UPDATE `apps` SET status=? WHERE ign=?;");
                sql.setString(1, "VIEWED");
                sql.setString(2, p.getName());
                sql.executeUpdate();
                sql.close();
            } catch (Exception e) {
                System.out.println("[EmeraldApps] ERROR: Couldn't update MySQL table.");
            }
        }
    }

    public static boolean hasDeniedApp(Player p) {
        if (connection != null)
            try {
                PreparedStatement sql = connection.prepareStatement("SELECT * FROM `apps` WHERE ign=? AND status=?;");
                sql.setString(1, p.getName());
                sql.setString(2, "DENIED");
                ResultSet resultSet = sql.executeQuery();
                boolean hasApp = resultSet.next();
                sql.close();
                resultSet.close();
                if (hasApp == true) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                System.out.println("[EmeraldApps] Error checking if player has denied app!");
                e.printStackTrace();

                return false;
            }
        return false;
    }


    public static Plugin getMain() {
        return new com.steve_rizzo.emeraldscore.Main();
    }


    public static void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name, username, password);
            System.out.println("[EmeraldApps] Connection to database established.");
        } catch (Exception e) {
            System.out.println("[EmeraldApps] Error connecting to database! Please check your config details.");
        }
    }

    public static void createTables() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS apps (  fullname varchar(255) NOT NULL,  ign varchar(17) NOT NULL,  rank varchar(255) NOT NULL,  skype varchar(255) NOT NULL,  age int(11) NOT NULL,  country varchar(255) NOT NULL,  do_to_help longtext NOT NULL,  experience longtext NOT NULL,  additional longtext NOT NULL,  status longtext NOT NULL)";


            String sql2 = "CREATE TABLE IF NOT EXISTS users (  ign varchar(17) NOT NULL,  password varchar(255) NOT NULL,  rank varchar(255) NOT NULL)";


            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
        } catch (Exception e) {
            System.out.println("[EmeraldApps] Tables already created! Good news.");
        }
    }
}
