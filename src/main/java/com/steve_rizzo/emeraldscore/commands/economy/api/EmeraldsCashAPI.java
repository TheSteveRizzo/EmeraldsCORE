package com.steve_rizzo.emeraldscore.commands.economy.api;

import com.steve_rizzo.emeraldscore.Main;
import net.milkbowl.vault.economy.EconomyResponse;
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

    private static int returnedBal = 0;

    public static void displayBalance(Player player) {

        Player p = player;

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
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
        });
    }

    public static void displayBalanceUUID(String balanceUserName, String balanceUsersUUID, Player playerRequestingBalance) {

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
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
        });
    }


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

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
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

    public static void deductFundsUUID(String uuid, int amount) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
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

    public static void addFunds(Player player, int amount) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
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

    public static void addFundsToUUID(String uuid, int amount) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
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

    private static int returnPlayerCurrencyAmount(Player p) {

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA)) {
                selectionStatement.setString(1, p.getUniqueId().toString());
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

        //             "INSERT INTO EmeraldsCash(uuid, name, balance, date) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance=?";
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
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

    private static void updatePlayerUUIDCurrencyAmount(String uuid, int amount) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = format.format(date);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
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

    public static void returnTopBalances(Player p, int topCount) {
        final HashMap<String, Integer> balTopList = new HashMap<>();

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement statement = connection.prepareStatement(SELECT_ALL_BALANCES)) {
                ResultSet resultBalance = statement.executeQuery();
                while (resultBalance.next()) {
                    String name = resultBalance.getString(1);
                    int userBal = resultBalance.getInt(2);

                    balTopList.put(name, userBal);
                }

                // Sort the balances in descending order
                LinkedHashMap<String, Integer> sortedBalances = getTopBalances(balTopList, false, topCount);

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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static LinkedHashMap<String, Integer> getTopBalances(HashMap<String, Integer> unsortedMap, final boolean order, int topCount) {
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
    }

    public static boolean doesAccountExist(String uuid) {
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
