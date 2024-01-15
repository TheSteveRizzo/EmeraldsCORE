package com.steve_rizzo.emeraldscore.commands.tokens;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TokensAPI {

    // Prepared Statements for accessing, updating, and storing EmeraldsTokensAPI information

    private static final String SELECT_CURRENCY_DATA = "SELECT balance FROM EmeraldsTokens WHERE uuid=?";
    private static final String SELECT_CURRENCY_DATA_UUID = "SELECT balance FROM EmeraldsTokens WHERE uuid=?";
    private static final String DELETE_BALANCE_BY_NAME =
            "DELETE FROM EmeraldsTokens WHERE name=?";
    private static final String UPDATE_CURRENCY_DATA =
            "INSERT INTO EmeraldsTokens(uuid, name, balance, date) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance=?";
    private static final String ADD_CURRENCY_DATA =
            "INSERT INTO EmeraldsTokens(uuid, name, balance, date) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = balance + ?";
    private static final String DEDUCT_CURRENCY_DATA =
            "INSERT INTO EmeraldsTokens(uuid, name, balance, date) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = balance - ?";
    private static final String SELECT_ALL_BALANCES = "SELECT name, balance FROM EmeraldsTokens";


    public static CompletableFuture<Integer> displayTokenBalance(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            int returnedBal = 0;
            Player p = player;
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA)) {
                selectionStatement.setString(1, p.getUniqueId().toString());
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (resultBalance.next()) {
                    returnedBal = resultBalance.getInt(1);
                    p.sendMessage(Main.prefix + ChatColor.GRAY + "Your " + ChatColor.GREEN + "EmeraldsTokens" + ChatColor.GRAY + " balance is: "
                            + ChatColor.AQUA + returnedBal + ChatColor.GRAY + ".");
                }
                System.out.println("[EmeraldsMC - Token Handler]: Data RETURNED for " + p.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return returnedBal;
        });
    }



    public static CompletableFuture<Integer> displayTokenBalanceUUID(String balanceUserName, String balanceUsersUUID, Player playerRequestingBalance) {
        return CompletableFuture.supplyAsync(() -> {
            int returnedBal = 0;
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA)) {
                selectionStatement.setString(1, balanceUsersUUID);
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (resultBalance.next()) {
                    returnedBal = resultBalance.getInt(1);
                    playerRequestingBalance.sendMessage(Main.prefix + ChatColor.GRAY + "The " + ChatColor.GREEN + "EmeraldsTokens" + ChatColor.GRAY + " balance of " +
                            ChatColor.AQUA + balanceUserName + ChatColor.GRAY + " is: "
                            + ChatColor.AQUA + returnedBal + ChatColor.GRAY + ".");
                }
                System.out.println("[EmeraldsMC - Token Handler]: Data RETURNED for " + balanceUserName + " REQUESTED BY " + playerRequestingBalance.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return returnedBal;
        });
    }

    public static CompletableFuture<Integer> getTokensBalance(Player player) {
        return returnPlayerTokensAmount(player);
    }

    public static CompletableFuture<Integer> getUUIDTokensBalance(String uuid) {
        return returnPlayerUUIDTokensAmount(uuid);
    }

    public static int returnTokensBalance(Player player) {
        CompletableFuture<Integer> balanceFuture = returnPlayerTokensAmount(player);
        return balanceFuture.join();
    }

    public static int returnUUIDBTokensalance(String uuid) {
        CompletableFuture<Integer> balanceFuture = returnPlayerUUIDTokensAmount(uuid);
        return balanceFuture.join();
    }

    public static CompletableFuture<Void> setTokensBalance(Player player, int amount) {
        return updatePlayerTokensAmount(player, amount);
    }

    public static CompletableFuture<Void> setTokensBalanceUUID(String uuid, int amount) {
        return updatePlayerUUIDTokensAmount(uuid, amount);
    }

    public static CompletableFuture<Void> deductTokens(Player player, int amount) {
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

                System.out.println("[EmeraldsMC - Token Handler]: Data SUBTRACTED for " + player.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Void> deductTokensUUID(String uuid, int amount) {
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

                System.out.println("[EmeraldsMC - Token Handler]: Data SUBTRACTED for " + uuid + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Void> addTokens(Player player, int amount) {
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

                System.out.println("[EmeraldsMC - Token Handler]: Data ADDED for " + player.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Void> addTokensToUUID(String uuid, int amount) {
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

                System.out.println("[EmeraldsMC - Token Handler]: Data ADDED for " + uuid + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Integer> returnPlayerTokensAmount(Player p) {
        return CompletableFuture.supplyAsync(() -> {
            int returnedBal = 0;
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA)) {
                selectionStatement.setString(1, p.getUniqueId().toString());
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (resultBalance.next()) {
                    returnedBal = resultBalance.getInt(1);
                }
                System.out.println("[EmeraldsMC - Token Handler]: Data RETURNED for " + p.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return returnedBal;
        });
    }

    public static CompletableFuture<Integer> returnPlayerUUIDTokensAmount(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            int returnedBal = 0;
            try (Connection connection = Main.getInstance().getHikari();
                 PreparedStatement selectionStatement = connection.prepareStatement(SELECT_CURRENCY_DATA_UUID)) {
                selectionStatement.setString(1, uuid);
                ResultSet resultBalance = selectionStatement.executeQuery();
                if (resultBalance.next()) {
                    returnedBal = resultBalance.getInt(1);
                }
                System.out.println("[EmeraldsMC - Token Handler]: Data RETURNED for " + uuid + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return returnedBal;
        });
    }
    public static CompletableFuture<Void> updatePlayerTokensAmount(Player p, int amount) {
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
                System.out.println("[EmeraldsMC - Token Handler]: Data UPDATED for " + p.getName() + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static CompletableFuture<Void> updatePlayerUUIDTokensAmount(String uuid, int amount) {
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
                System.out.println("[EmeraldsMC - Token Handler]: Data UPDATED for " + uuid + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void sendTopTokensMessage(Player player) {
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
                        // If null balance found, remove it from the database
                        PreparedStatement deleteStatement = connection.prepareStatement(DELETE_BALANCE_BY_NAME);
                        deleteStatement.setString(1, name);
                        deleteStatement.executeUpdate();
                        deleteStatement.close();
                    }
                }

                // Get top balances
                List<Map.Entry<String, Integer>> sortedBalances = balTopList.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(10)
                        .collect(Collectors.toList());

                // Send top balances to the player
                player.sendMessage(ChatColor.GREEN + "---" + ChatColor.AQUA + "---["
                        + ChatColor.GREEN + "EmeraldsTokens" + ChatColor.AQUA + "]---" + ChatColor.GREEN + "---");
                player.sendMessage(ChatColor.GRAY + "Top Balances:");

                int rank = 1;
                for (Map.Entry<String, Integer> entry : sortedBalances) {
                    String name = entry.getKey();
                    int balance = entry.getValue();

                    player.sendMessage(ChatColor.RED + "#" + rank + " " + ChatColor.GREEN + ServerJoinPlayer.getPlayerPrefixAndName(name) + ChatColor.GRAY + " : " + ChatColor.AQUA + balance);
                    rank++;
                }

                player.sendMessage(ChatColor.GREEN + "---" + ChatColor.AQUA + "---["
                        + ChatColor.GREEN + "EmeraldsTokens" + ChatColor.AQUA + "]---" + ChatColor.GREEN + "---");

                System.out.println("[EmeraldsMC - Token Handler]: Top 10 balances queried by " + player.getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Boolean> doesTokenAccountExist(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            boolean accountExists = false;
            String SELECT_ACCOUNT = "SELECT uuid FROM EmeraldsTokens WHERE uuid=?";

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
        for (OfflinePlayer allOffP : Main.getInstance().getServer().getOfflinePlayers()) {
            if (uuid.equalsIgnoreCase(allOffP.getUniqueId().toString()))
                return allOffP.getName();
        }
        return null;
    }
    
}
