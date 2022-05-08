package com.steve_rizzo.emeraldscore.commands.economy.api;

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

public class EmeraldsCashAPI {

    // Prepared Statements for accessing, updating, and storing EmeraldsCashAPI information
    private static final String QUERY_CURRENCY_DATA = "SELECT balance FROM EmeraldsCash WHERE uuid=?";

    private static final String INSERT_CURRENCY_DATA = "INSERT INTO EmeraldsCash VALUES(?,?,?,?)";

    private static final String SELECT_CURRENCY_DATA = "SELECT balance FROM EmeraldsCash WHERE uuid=?";
    private static final String SELECT_CURRENCY_DATA_UUID = "SELECT balance FROM EmeraldsCash WHERE uuid=?";

    private static final String UPDATE_CURRENCY_DATA = "UPDATE EmeraldsCash SET balance=?, date=? WHERE uuid=?";
    private static final String UPDATE_CURRENCY_DATA_UUID = "UPDATE EmeraldsCash SET balance=?, date=? WHERE uuid=?";

    private static final String SELECT_ALL_BALANCES = "SELECT name, balance FROM EmeraldsCash";

    private static int returnedBal = 0;
    private static boolean accountExists = false;

    public static int getBalance(Player player) {
        return returnPlayerCurrencyAmount(player);
    }

    public static int getUUIDBalance(String uuid) {
        return returnPlayerUUIDCurrencyAmount(uuid);
    }

    public static void setBalance(Player player, int amount) {
        updatePlayerCurrencyAmount(player, amount);
    }

    public static void setBalanceUUID(String uuid, int amount) {
        updatePlayerUUIDCurrencyAmount(uuid, amount);
    }

    public static void deductFunds(Player player, int amount) {
        int existingBal = returnPlayerCurrencyAmount(player);
        int newBal = (existingBal - amount);
        updatePlayerCurrencyAmount(player, newBal);
    }

    public static void deductFundsUUID(String uuid, int amount) {
        int existingBal = returnPlayerUUIDCurrencyAmount(uuid);
        int newBal = (existingBal - amount);
        updatePlayerUUIDCurrencyAmount(uuid, newBal);
    }

    public static void addFunds(Player player, int amount) {
        int existingBal = returnPlayerCurrencyAmount(player);
        int newBal = (existingBal + amount);
        updatePlayerCurrencyAmount(player, newBal);
    }

    public static void addFundsToUUID(String uuid, int amount) {
        int existingBal = returnPlayerUUIDCurrencyAmount(uuid);
        int newBal = (existingBal + amount);
        updatePlayerUUIDCurrencyAmount(uuid, newBal);
    }

    public static void createAccount(Player player) {

        if (!EmeraldsCashAPI.doesPlayerAccountExist(player)) {

            int amountOnFirstJoin = 500;

            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateTime = format.format(date);

            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                try (Connection connection = Main.getInstance().getHikari();
                     PreparedStatement insert = connection.prepareStatement(INSERT_CURRENCY_DATA)) {
                    insert.setString(1, player.getUniqueId().toString());
                    insert.setString(2, player.getName());
                    insert.setInt(3, amountOnFirstJoin);
                    insert.setString(4, currentDateTime);
                    insert.execute();

                    System.out.println("[EmeraldsMC - Currency Handler]: Data CREATED for " + player.getName() + ".");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        }
    }

    public static void createAccountUUID(String uuid) {

        if (!EmeraldsCashAPI.doesPlayerUUIDAccountExist(uuid)) {

            int amountOnFirstJoin = 500;

            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateTime = format.format(date);

            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                try (Connection connection = Main.getInstance().getHikari();
                     PreparedStatement insert = connection.prepareStatement(INSERT_CURRENCY_DATA)) {
                    insert.setString(1, uuid);
                    insert.setString(2, null);
                    insert.setInt(3, amountOnFirstJoin);
                    insert.setString(4, currentDateTime);
                    insert.execute();

                    System.out.println("[EmeraldsMC - Currency Handler]: Data CREATED for " + uuid + ".");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        }
    }

    public static boolean doesPlayerAccountExist(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(QUERY_CURRENCY_DATA)) {
                selectionStatement.setString(1, player.getUniqueId().toString());
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (!resultBalance.isBeforeFirst()) {
                    accountExists = false;
                    System.out.println("[EmeraldsMC - Currency Handler]: Data QUERIED, account does NOT exist for " + player.getName() + ".");
                } else {
                    System.out.println("[EmeraldsMC - Currency Handler]: Data QUERIED, account DOES exist for " + player.getName() + ".");
                    accountExists = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return accountExists;
    }

    public static boolean doesPlayerUUIDAccountExist(String uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(QUERY_CURRENCY_DATA)) {
                selectionStatement.setString(1, uuid);
                selectionStatement.execute();
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (!resultBalance.next()) {
                    accountExists = false;
                    System.out.println("[EmeraldsMC - Currency Handler]: Data QUERIED, account does NOT exist for " + uuid + ".");
                } else {
                    System.out.println("[EmeraldsMC - Currency Handler]: Data QUERIED, account DOES exist for " + uuid + ".");
                    accountExists = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return accountExists;
    }

    private static int returnPlayerCurrencyAmount(Player p) {

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA)) {
                selectionStatement.setString(1, p.getUniqueId().toString());
                selectionStatement.execute();
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (resultBalance.next()) returnedBal = resultBalance.getInt(1);
                System.out.println("[EmeraldsMC - Currency Handler]: Data RETURNED for " + p.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return returnedBal;
    }

    private static int returnPlayerUUIDCurrencyAmount(String uuid) {

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA_UUID)) {
                selectionStatement.setString(1, uuid);
                selectionStatement.execute();
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (resultBalance.next()) returnedBal = resultBalance.getInt(1);
                System.out.println("[EmeraldsMC - Currency Handler]: Data RETURNED for " + uuid + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return returnedBal;

    }

    private static void updatePlayerCurrencyAmount(Player p, int amount) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement statement = connection.prepareStatement(UPDATE_CURRENCY_DATA)) {
                statement.setInt(1, amount);
                statement.setString(2, currentDateTime);
                statement.setString(3, p.getUniqueId().toString());
                statement.execute();
                System.out.println("[EmeraldsMC - Currency Handler]: Data UPDATED for " + p.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static void updatePlayerUUIDCurrencyAmount(String uuid, int amount) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement statement = connection.prepareStatement(UPDATE_CURRENCY_DATA_UUID)) {
                statement.setInt(1, amount);
                statement.setString(2, currentDateTime);
                statement.setString(3, uuid);
                statement.execute();
                System.out.println("[EmeraldsMC - Currency Handler]: Data UPDATED for " + uuid + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static HashMap<String, Integer> returnAllBalances() {

        HashMap<String, Integer> balTopList = new HashMap<>();

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BALANCES)) {
                ResultSet resultBalance = statement.executeQuery();
                while (resultBalance.next()) {
                    // has another value to add to the baltop list
                    // NAME, BAL
                    String name = resultBalance.getString(1);
                    int userBal = resultBalance.getInt(2);

                    balTopList.put(name, userBal);

                }
                // no more values to add to the list
                System.out.println("[EmeraldsMC - Currency Handler]: BalTOP Query executed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return balTopList;
    }
}
