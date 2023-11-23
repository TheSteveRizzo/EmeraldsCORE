package com.steve_rizzo.emeraldscore.commands.economy.api;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EmeraldsCashAPI {

    // Prepared Statements for accessing, updating, and storing EmeraldsCashAPI information

    private static final String SELECT_CURRENCY_DATA = "SELECT balance FROM EmeraldsCash WHERE uuid=?";
    private static final String SELECT_CURRENCY_DATA_UUID = "SELECT balance FROM EmeraldsCash WHERE uuid=?";

    private static final String UPDATE_CURRENCY_DATA =
            "INSERT INTO EmeraldsCash(uuid, name, balance, date) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance=?";

    private static final String ADD_CURRENCY_DATA =
            "INSERT INTO EmeraldsCash(uuid, name, balance, date) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = balance + ?";

    private static final String DEDUCT_CURRENCY_DATA =
            "INSERT INTO EmeraldsCash(uuid, name, balance, date) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = balance - ?";

    private static final String SELECT_ALL_BALANCES = "SELECT name, balance FROM EmeraldsCash";

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
                            + ChatColor.GREEN + "$" + returnedBal + ChatColor.GRAY + ".");
                }
                System.out.println("[EmeraldsMC - Currency Handler]: Data RETURNED for " + p.getName() + ".");
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
                            + ChatColor.GREEN + "$" + returnedBal + ChatColor.GRAY + ".");
                }
                System.out.println("[EmeraldsMC - Currency Handler]: Data RETURNED for " + balanceUserName + " REQUESTED BY " + playerRequestingBalance.getName() + ".");
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
                selectionStatement.setString(2, getPlayerName(uuid));
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
                selectionStatement.setString(2, getPlayerName(uuid));
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
                System.out.println("[EmeraldsMC - Currency Handler]: Data RETURNED for " + p.getName() + ".");
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
                System.out.println("[EmeraldsMC - Currency Handler]: Data RETURNED for " + uuid + ".");
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

    private static CompletableFuture<Void> updatePlayerUUIDCurrencyAmount(String uuid, int amount) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        return CompletableFuture.runAsync(() -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement statement = connection.prepareStatement(UPDATE_CURRENCY_DATA)) {
                statement.setString(1, uuid);
                statement.setString(2, getPlayerName(uuid));
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
    public static CompletableFuture<Void> returnTopBalances(Player p, int topCount) {
        return CompletableFuture.runAsync(() -> {
            final HashMap<String, Integer> balTopList = new HashMap<>();

            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BALANCES)) {
                ResultSet resultBalance = statement.executeQuery();
                while (resultBalance.next()) {
                    String name = resultBalance.getString(1);
                    int userBal = resultBalance.getInt(2);

                    balTopList.put(name, userBal);
                }

                // Sort the balances in descending order asynchronously
                getTopBalances(balTopList, false, topCount)
                        .thenAcceptAsync(sortedBalances -> {
                            // Iterate over the top balances and send them to the player
                            int rank = 1;
                            for (Map.Entry<String, Integer> entry : sortedBalances.entrySet()) {
                                if (rank > topCount) {
                                    break; // Only send the top 10 balances
                                }

                                String name = entry.getKey();
                                int balance = entry.getValue();

                                p.sendMessage(ChatColor.AQUA + "#" + rank + " " + name + ChatColor.GRAY + " : " + ChatColor.GREEN + "$" + balance);
                                rank++;
                            }

                            p.sendMessage(ChatColor.GREEN + "---" + ChatColor.AQUA + "---["
                                    + ChatColor.GREEN + "EmeraldsCash" + ChatColor.AQUA + "]---" + ChatColor.GREEN + "---");
                            System.out.println("[EmeraldsMC - Currency Handler]: Top " + topCount + " balances queried.");
                        });

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static CompletableFuture<LinkedHashMap<String, Integer>> getTopBalances(HashMap<String, Integer> unsortedMap, final boolean order, int topCount) {
        return CompletableFuture.supplyAsync(() -> {
            List<HashMap.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entrySet());

            // Sorting the list based on values
            list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                    ? o1.getKey().compareTo(o2.getKey())
                    : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                    ? o2.getKey().compareTo(o1.getKey())
                    : o2.getValue().compareTo(o1.getValue()));

            // Limit the result to the top 'topCount' entries
            List<HashMap.Entry<String, Integer>> topEntries = list.stream().limit(topCount).collect(Collectors.toList());

            // Collect the top entries into a LinkedHashMap
            LinkedHashMap<String, Integer> topBalances = new LinkedHashMap<>();
            for (HashMap.Entry<String, Integer> entry : topEntries) {
                topBalances.put(entry.getKey(), entry.getValue());
            }

            return topBalances;
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

    private static void printMap(HashMap<String, Integer> map) {
        map.forEach((key, value) -> System.out.println("Key : " + key + " Value : " + value));
    }

    private static String getPlayerName(String uuid) {
        for (OfflinePlayer allOffP : Bukkit.getServer().getOfflinePlayers()) {
            if (uuid.equalsIgnoreCase(allOffP.getUniqueId().toString()))
                return allOffP.getName();
        }
        return null;
    }
}
