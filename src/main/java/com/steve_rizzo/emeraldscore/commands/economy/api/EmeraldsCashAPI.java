package com.steve_rizzo.emeraldscore.commands.economy.api;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EmeraldsCashAPI {

    private static final String SELECT_CURRENCY_DATA = "SELECT balance FROM EmeraldsCash WHERE uuid=?";
    private static final String SELECT_CURRENCY_DATA_UUID = "SELECT balance FROM EmeraldsCash WHERE uuid=?";
    private static final String DELETE_BALANCE_BY_NAME = "DELETE FROM EmeraldsCash WHERE name=?";
    private static final String UPDATE_CURRENCY_DATA =
            "INSERT INTO EmeraldsCash(uuid, name, balance, date) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance=?";
    private static final String ADD_CURRENCY_DATA =
            "INSERT INTO EmeraldsCash(uuid, name, balance, date) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = balance + ?";
    private static final String DEDUCT_CURRENCY_DATA =
            "INSERT INTO EmeraldsCash(uuid, name, balance, date) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = balance - ?";
    private static final String SELECT_ALL_BALANCES = "SELECT name, balance FROM EmeraldsCash";
    private static final String UPDATE_PLAYER_NAME = "UPDATE EmeraldsCash SET name=? WHERE uuid=?";

    public static CompletableFuture<Integer> displayBalance(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            int returnedBal = 0;
            Player p = player;
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA)) {
                selectionStatement.setString(1, p.getUniqueId().toString());
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (resultBalance.next()) {
                    returnedBal = resultBalance.getInt(1);
                    p.sendMessage(Main.prefix + ChatColor.GRAY + "Your " + ChatColor.GREEN + "EmeraldsCash" + ChatColor.GRAY + " balance is: "
                            + ChatColor.GREEN + "$" + formatBalance(returnedBal) + ChatColor.GRAY + ".");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return returnedBal;
        });
    }

    public static CompletableFuture<Integer> displayBalanceUUID(String balanceUserName, String balanceUsersUUID, Player playerRequestingBalance) {
        return CompletableFuture.supplyAsync(() -> {
            int returnedBal = 0;
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA)) {
                selectionStatement.setString(1, balanceUsersUUID);
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (resultBalance.next()) {
                    returnedBal = resultBalance.getInt(1);
                    playerRequestingBalance.sendMessage(Main.prefix + ChatColor.GRAY + "The " + ChatColor.GREEN + "EmeraldsCash" + ChatColor.GRAY + " balance of " +
                            ChatColor.AQUA + balanceUserName + ChatColor.GRAY + " is: "
                            + ChatColor.GREEN + "$" + formatBalance(returnedBal) + ChatColor.GRAY + ".");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return returnedBal;
        });
    }

    public static CompletableFuture<Integer> getBalance(Player player) {
        return returnPlayerCurrencyAmount(player);
    }

    public static CompletableFuture<Integer> getUUIDBalance(String uuid) {
        return returnPlayerUUIDCurrencyAmount(uuid);
    }

    public static int returnBalance(Player player) {
        CompletableFuture<Integer> balanceFuture = returnPlayerCurrencyAmount(player);
        return balanceFuture.join();
    }

    public static int returnUUIDBalance(String uuid) {
        CompletableFuture<Integer> balanceFuture = returnPlayerUUIDCurrencyAmount(uuid);
        return balanceFuture.join();
    }

    public static CompletableFuture<Void> setBalance(Player player, int amount) {
        return updatePlayerCurrencyAmount(player, amount);
    }

    public static CompletableFuture<Void> setBalanceUUID(String uuid, int amount) {
        return updatePlayerUUIDCurrencyAmount(uuid, amount);
    }

    public static CompletableFuture<Void> deductFunds(Player player, int amount) {
        return CompletableFuture.runAsync(() -> {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateTime = format.format(date);
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(DEDUCT_CURRENCY_DATA)) {
                selectionStatement.setString(1, player.getUniqueId().toString());
                selectionStatement.setString(2, player.getName());
                selectionStatement.setInt(3, amount);
                selectionStatement.setString(4, currentDateTime);
                selectionStatement.setInt(5, amount);
                selectionStatement.execute();

                System.out.println("[EmeraldsMC - Currency Handler]: Data SUBTRACTED for " + player.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Void> deductFundsUUID(String uuid, int amount) {
        return CompletableFuture.runAsync(() -> {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateTime = format.format(date);
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(DEDUCT_CURRENCY_DATA)) {
                selectionStatement.setString(1, uuid);
                selectionStatement.setString(2, getPlayerNameFromDB(connection, uuid));
                selectionStatement.setInt(3, amount);
                selectionStatement.setString(4, currentDateTime);
                selectionStatement.setInt(5, amount);
                selectionStatement.execute();

                System.out.println("[EmeraldsMC - Currency Handler]: Data SUBTRACTED for " + uuid + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Void> addFunds(Player player, int amount) {
        return CompletableFuture.runAsync(() -> {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateTime = format.format(date);
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(ADD_CURRENCY_DATA)) {
                selectionStatement.setString(1, player.getUniqueId().toString());
                selectionStatement.setString(2, player.getName());
                selectionStatement.setInt(3, amount);
                selectionStatement.setString(4, currentDateTime);
                selectionStatement.setInt(5, amount);
                selectionStatement.execute();

                System.out.println("[EmeraldsMC - Currency Handler]: Data ADDED for " + player.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Void> addFundsToUUID(String uuid, int amount) {
        return CompletableFuture.runAsync(() -> {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateTime = format.format(date);

            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(ADD_CURRENCY_DATA)) {
                selectionStatement.setString(1, uuid);
                selectionStatement.setString(2, getPlayerNameFromDB(connection, uuid));
                selectionStatement.setInt(3, amount);
                selectionStatement.setString(4, currentDateTime);
                selectionStatement.setInt(5, amount);
                selectionStatement.execute();

                System.out.println("[EmeraldsMC - Currency Handler]: Data ADDED for " + uuid + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Integer> returnPlayerCurrencyAmount(Player p) {
        return CompletableFuture.supplyAsync(() -> {
            int returnedBal = 0;
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA)) {
                selectionStatement.setString(1, p.getUniqueId().toString());
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (resultBalance.next()) {
                    returnedBal = resultBalance.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return returnedBal;
        });
    }

    public static CompletableFuture<Integer> returnPlayerUUIDCurrencyAmount(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            int returnedBal = 0;
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA_UUID)) {
                selectionStatement.setString(1, uuid);
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (resultBalance.next()) {
                    returnedBal = resultBalance.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return returnedBal;
        });
    }

    public static CompletableFuture<Void> updatePlayerCurrencyAmount(Player p, int amount) {
        return CompletableFuture.runAsync(() -> {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateTime = format.format(date);

            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement statement = connection.prepareStatement(UPDATE_CURRENCY_DATA)) {

                String currentNameInDB = getPlayerNameFromDB(connection, p.getUniqueId().toString());
                if (currentNameInDB == null || !currentNameInDB.equals(p.getName())) {
                    updatePlayerNameInDB(p.getUniqueId().toString(), p.getName());
                }

                statement.setString(1, p.getUniqueId().toString());
                statement.setString(2, p.getName());
                statement.setInt(3, amount);
                statement.setString(4, currentDateTime);
                statement.setInt(5, amount);
                statement.execute();
                System.out.println("[EmeraldsMC - Currency Handler]: Data UPDATED for " + p.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static void updatePlayerNameInDB(String uuid, String newName) {
        try (Connection connection = Main.getInstance().getHikari();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PLAYER_NAME)) {
            statement.setString(1, newName);
            statement.setString(2, uuid);
            statement.executeUpdate();
            System.out.println("[EmeraldsMC - Currency Handler]: Player name UPDATED in the database for UUID: " + uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CompletableFuture<Void> updatePlayerUUIDCurrencyAmount(String uuid, int amount) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        return CompletableFuture.runAsync(() -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement statement = connection.prepareStatement(UPDATE_CURRENCY_DATA)) {
                String currentNameInDB = getPlayerNameFromDB(connection, uuid);
                if (currentNameInDB == null || !currentNameInDB.equals(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName())) {
                    updatePlayerNameInDB(uuid, Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
                }

                statement.setString(1, uuid);
                statement.setString(2, getPlayerNameFromDB(connection, uuid));
                statement.setInt(3, amount);
                statement.setString(4, currentDateTime);
                statement.setInt(5, amount);
                statement.execute();
                System.out.println("[EmeraldsMC - Currency Handler]: Data UPDATED for " + uuid + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void sendTopBalancesMessage(Player player) {
        CompletableFuture.runAsync(() -> {
            Map<String, Integer> balTopList = new HashMap<>();

            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BALANCES)) {
                ResultSet resultBalance = statement.executeQuery();
                while (resultBalance.next()) {
                    String name = resultBalance.getString(1);
                    Integer userBal = resultBalance.getInt(2);

                    if (userBal != null) {
                        balTopList.put(name, userBal);
                    } else {
                        PreparedStatement deleteStatement = connection.prepareStatement(DELETE_BALANCE_BY_NAME);
                        deleteStatement.setString(1, name);
                        deleteStatement.executeUpdate();
                        deleteStatement.close();
                    }
                }

                List<Map.Entry<String, Integer>> sortedBalances = balTopList.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(10)
                        .collect(Collectors.toList());

                player.sendMessage(ChatColor.GREEN + "---" + ChatColor.AQUA + "---["
                        + ChatColor.GREEN + "EmeraldsCash" + ChatColor.AQUA + "]---" + ChatColor.GREEN + "---");
                player.sendMessage(ChatColor.GRAY + "Top Balances:");

                int rank = 1;
                for (Map.Entry<String, Integer> entry : sortedBalances) {
                    String name = entry.getKey();
                    int balance = entry.getValue();

                    player.sendMessage(ChatColor.RED + "#" + rank + " " + ChatColor.GREEN + ServerJoinPlayer.getPlayerPrefixAndName(name) + ChatColor.GRAY + " : " + ChatColor.GREEN + "$" + balance);
                    rank++;
                }

                player.sendMessage(ChatColor.GREEN + "---" + ChatColor.AQUA + "---["
                        + ChatColor.GREEN + "EmeraldsCash" + ChatColor.AQUA + "]---" + ChatColor.GREEN + "---");

                System.out.println("[EmeraldsMC - Currency Handler]: Top 10 balances queried by " + player.getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Boolean> doesAccountExist(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            boolean accountExists = false;
            String SELECT_ACCOUNT = "SELECT uuid FROM EmeraldsCash WHERE uuid=?";

            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT)) {
                statement.setString(1, uuid);
                ResultSet resultSet = statement.executeQuery();
                accountExists = resultSet.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return accountExists;
        });
    }

    private static String getPlayerNameFromDB(Connection connection, String uuid) throws SQLException {
        String SELECT_NAME = "SELECT name FROM EmeraldsCash WHERE uuid=?";
        try (PreparedStatement statement = connection.prepareStatement(SELECT_NAME)) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        }
        return null;
    }

    private static String formatBalance(int balance) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(balance);
    }
}