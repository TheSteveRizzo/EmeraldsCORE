package com.steve_rizzo.emeraldscore.utils;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Ranks {

    //Ranks(UUID varchar(36), name VARCHAR(16), rank varchar(16), date DATE)

    private static final String FIRST_INSERT = "INSERT INTO Ranks VALUES(?,?,?,?)";
    private static final String SELECT_DATA = "SELECT rank FROM Ranks WHERE uuid=?";
    private static final String UPDATE_DATA = "UPDATE Ranks SET rank=?, date=? WHERE uuid=?";
    private static final String DELETE_DATA = "DELETE FROM Ranks WHERE uuid=?";

    // Cache if ever needed.
    private Map<UUID, String> rankMap;

    public Ranks() {
        this.rankMap = new HashMap<>();
    }

    public void removePlayer(Player p) {
        rankMap.remove(p.getUniqueId());
    }

    public String getRank(Player p) {
        return rankMap.get(p.getUniqueId());
    }

    public void loadFirstTimePlayer(Player p) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        String rank = Main.perms.getPrimaryGroup(p);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                try (Connection connection = Main.getInstance().getHikari();
                     PreparedStatement insert = connection.prepareStatement(FIRST_INSERT);
                     PreparedStatement select = connection.prepareStatement(SELECT_DATA)) {
                    insert.setString(1, p.getUniqueId().toString());
                    insert.setString(2, p.getName());
                    insert.setString(3, rank);
                    insert.setString(4, currentDateTime);
                    insert.execute();

                    select.setString(1, p.getUniqueId().toString());
                    ResultSet result = select.executeQuery();
                    if (result.next())
                        rankMap.put(p.getUniqueId(), result.getString("rank"));
                    result.close();
                    System.out.println("[EmeraldsMC - Rank Handler]: Data CREATED for " + p.getName() + ".");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateAndSaveData(Player p) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                try (Connection connection = Main.getInstance().getHikari();
                     PreparedStatement statement = connection.prepareStatement(UPDATE_DATA)) {
                    statement.setString(1, Main.perms.getPrimaryGroup(p));
                    statement.setString(2, currentDateTime);
                    statement.setString(3, p.getUniqueId().toString());
                    statement.execute();
                    System.out.println("[EmeraldsMC - Rank Handler]: Data SAVED/UPDATED for " + p.getName() + ".");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}