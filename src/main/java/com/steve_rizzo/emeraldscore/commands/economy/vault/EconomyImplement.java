package com.steve_rizzo.emeraldscore.commands.economy.vault;

import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class EconomyImplement implements Economy {

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String getName() {
        return "EmeraldsCash";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double v) {
        String s = String.valueOf((int) v);
        if (v % 1000 == 0) {
            return s.substring(0, s.length() - 3);
        }
        return s;
    }

    @Override
    public String currencyNamePlural() {
        return "Emeralds Dollars";
    }

    @Override
    public String currencyNameSingular() {
        return "Emeralds Dollar";
    }

    @Override
    public boolean hasAccount(String s) {
        CompletableFuture<Boolean> accountExistFuture = EmeraldsCashAPI.doesAccountExist(s);
        return accountExistFuture.join();
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        CompletableFuture<Boolean> accountExistFuture = EmeraldsCashAPI.doesAccountExist(offlinePlayer.getUniqueId().toString());
        return accountExistFuture.join();
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        CompletableFuture<Boolean> accountExistFuture = EmeraldsCashAPI.doesAccountExist(s);
        return accountExistFuture.join();
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        CompletableFuture<Boolean> accountExistFuture = EmeraldsCashAPI.doesAccountExist(offlinePlayer.getUniqueId().toString());
        return accountExistFuture.join();
    }

    @Override
    public double getBalance(String s) {
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getBalance(Bukkit.getPlayer(s));
        return balanceFuture.join();
    }
    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getUUIDBalance(offlinePlayer.getUniqueId().toString());
        return balanceFuture.join();
    }

    @Override
    public double getBalance(String s, String s1) {
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getBalance(Bukkit.getPlayer(s));
        return balanceFuture.join();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getUUIDBalance(offlinePlayer.getUniqueId().toString());
        return balanceFuture.join();
    }

    @Override
    public boolean has(String s, double v) {
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getBalance(Bukkit.getPlayer(s));
        int balance = balanceFuture.join();
        return balance >= v;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getUUIDBalance(offlinePlayer.getUniqueId().toString());
        int balance = balanceFuture.join();
        return balance >= v;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getBalance(Bukkit.getPlayer(s));
        int balance = balanceFuture.join();
        return balance >= v;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getUUIDBalance(offlinePlayer.getUniqueId().toString());
        int balance = balanceFuture.join();
        return balance >= v;
    }
    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        Player player = Bukkit.getPlayer(s);
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getBalance(player);

        // Handle completion of the CompletableFuture and convert to double
        int balance = balanceFuture.join();
        double currentBalance = (double) balance;

        if (currentBalance >= v) {
            EmeraldsCashAPI.deductFunds(player, (int) v);
            return new EconomyResponse(v, currentBalance,
                    EconomyResponse.ResponseType.SUCCESS, "Withdraw Success");
        } else {
            return new EconomyResponse(v, currentBalance,
                    EconomyResponse.ResponseType.FAILURE, "Failed");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getUUIDBalance(uuid.toString());

        // Handle completion of the CompletableFuture and convert to double
        int balance = balanceFuture.join();
        double currentBalance = (double) balance;

        if (currentBalance >= v) {
            EmeraldsCashAPI.deductFundsUUID(uuid.toString(), (int) v);
            return new EconomyResponse(v, currentBalance,
                    EconomyResponse.ResponseType.SUCCESS, "Withdraw Success");
        } else {
            return new EconomyResponse(v, currentBalance,
                    EconomyResponse.ResponseType.FAILURE, "Failed");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        Player player = Bukkit.getPlayer(s);
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getBalance(player);

        // Handle completion of the CompletableFuture and convert to double
        int balance = balanceFuture.join();
        double currentBalance = (double) balance;

        if (currentBalance >= v) {
            EmeraldsCashAPI.deductFunds(player, (int) v);
            return new EconomyResponse(v, currentBalance,
                    EconomyResponse.ResponseType.SUCCESS, "Withdraw Success");
        } else {
            return new EconomyResponse(v, currentBalance,
                    EconomyResponse.ResponseType.FAILURE, "Failed");
        }
    }
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getUUIDBalance(uuid.toString());

        // Handle completion of the CompletableFuture and convert to double
        int balance = balanceFuture.join();
        double currentBalance = (double) balance;

        if (currentBalance >= v) {
            EmeraldsCashAPI.deductFundsUUID(uuid.toString(), (int) v);
            return new EconomyResponse(v, currentBalance,
                    EconomyResponse.ResponseType.SUCCESS, "Withdraw Success");
        } else {
            return new EconomyResponse(v, currentBalance,
                    EconomyResponse.ResponseType.FAILURE, "Failed");
        }
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        Player player = Bukkit.getPlayer(s);
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getBalance(player);
        int balance = balanceFuture.join();

        EmeraldsCashAPI.addFunds(player, (int) v);
        double newBalance = balance + v;

        return new EconomyResponse(v, newBalance,
                EconomyResponse.ResponseType.SUCCESS, "Deposit Success");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getUUIDBalance(uuid.toString());
        int balance = balanceFuture.join();

        EmeraldsCashAPI.addFundsToUUID(uuid.toString(), (int) v);
        double newBalance = balance + v;

        return new EconomyResponse(v, newBalance,
                EconomyResponse.ResponseType.SUCCESS, "Deposit Success");
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        Player player = Bukkit.getPlayer(s);
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getBalance(player);
        int balance = balanceFuture.join();

        EmeraldsCashAPI.addFunds(player, (int) v);
        double newBalance = balance + v;

        return new EconomyResponse(v, newBalance,
                EconomyResponse.ResponseType.SUCCESS, "Deposit Success");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        CompletableFuture<Integer> balanceFuture = EmeraldsCashAPI.getUUIDBalance(uuid.toString());
        int balance = balanceFuture.join();

        EmeraldsCashAPI.addFundsToUUID(uuid.toString(), (int) v);
        double newBalance = balance + v;

        return new EconomyResponse(v, newBalance,
                EconomyResponse.ResponseType.SUCCESS, "Deposit Success");
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return true;
    }
}