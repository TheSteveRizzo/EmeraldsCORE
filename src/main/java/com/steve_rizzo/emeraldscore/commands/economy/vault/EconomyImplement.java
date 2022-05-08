package com.steve_rizzo.emeraldscore.commands.economy.vault;

import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

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
        return EmeraldsCashAPI.doesPlayerAccountExist(Bukkit.getPlayer(s));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.hasPlayedBefore()) {
            String uuid = offlinePlayer.getUniqueId().toString();
            return EmeraldsCashAPI.doesPlayerUUIDAccountExist(uuid);
        } else {
            return false;
        }
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public double getBalance(String s) {
        Player player = Bukkit.getPlayer(s);
        return EmeraldsCashAPI.getBalance(player);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        UUID uuid = offlinePlayer.getUniqueId();
        return EmeraldsCashAPI.getUUIDBalance(uuid.toString());

    }

    @Override
    public double getBalance(String s, String s1) {
        Player player = Bukkit.getPlayer(s);
        return EmeraldsCashAPI.getBalance(player);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        UUID uuid = offlinePlayer.getUniqueId();
        return EmeraldsCashAPI.getUUIDBalance(uuid.toString());
    }

    @Override
    public boolean has(String s, double v) {
        if (EmeraldsCashAPI.getBalance(Bukkit.getPlayer(s)) >= v) return true;
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        if (EmeraldsCashAPI.getUUIDBalance(offlinePlayer.getUniqueId().toString()) >= v) return true;
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        if (EmeraldsCashAPI.getBalance(Bukkit.getPlayer(s)) >= v) return true;
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        if (EmeraldsCashAPI.getUUIDBalance(offlinePlayer.getUniqueId().toString()) >= v) return true;
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        Player player = Bukkit.getPlayer(s);
        EmeraldsCashAPI.deductFunds(player, (int) v);
        return new EconomyResponse(v, EmeraldsCashAPI.getBalance(player),
                EconomyResponse.ResponseType.SUCCESS, "Withdraw Success");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        EmeraldsCashAPI.deductFundsUUID(uuid.toString(), (int) v);
        return new EconomyResponse(v, EmeraldsCashAPI.getUUIDBalance(uuid.toString()),
                EconomyResponse.ResponseType.SUCCESS, "Withdraw Success");
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        Player player = Bukkit.getPlayer(s);
        EmeraldsCashAPI.deductFunds(player, (int) v);
        return new EconomyResponse(v, EmeraldsCashAPI.getBalance(player),
                EconomyResponse.ResponseType.SUCCESS, "Withdraw Success");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        EmeraldsCashAPI.deductFundsUUID(uuid.toString(), (int) v);
        return new EconomyResponse(v, EmeraldsCashAPI.getUUIDBalance(uuid.toString()),
                EconomyResponse.ResponseType.SUCCESS, "Withdraw Success");
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        Player player = Bukkit.getPlayer(s);
        EmeraldsCashAPI.addFunds(player, (int) v);
        return new EconomyResponse(v, EmeraldsCashAPI.getBalance(player),
                EconomyResponse.ResponseType.SUCCESS, "Deposit Success");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        EmeraldsCashAPI.addFundsToUUID(uuid.toString(), (int) v);
        return new EconomyResponse(v, EmeraldsCashAPI.getUUIDBalance(uuid.toString()),
                EconomyResponse.ResponseType.SUCCESS, "Deposit Success");
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        Player player = Bukkit.getPlayer(s);
        EmeraldsCashAPI.addFunds(player, (int) v);
        return new EconomyResponse(v, EmeraldsCashAPI.getBalance(player),
                EconomyResponse.ResponseType.SUCCESS, "Deposit Success");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        EmeraldsCashAPI.addFundsToUUID(uuid.toString(), (int) v);
        return new EconomyResponse(v, EmeraldsCashAPI.getUUIDBalance(offlinePlayer.getUniqueId().toString()),
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