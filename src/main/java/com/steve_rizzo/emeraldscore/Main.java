package com.steve_rizzo.emeraldscore;

import com.garbagemule.MobArena.MobArena;
import com.steve_rizzo.emeraldscore.commands.*;
import com.steve_rizzo.emeraldscore.commands.economy.*;
import com.steve_rizzo.emeraldscore.commands.economy.vault.EconomyImplement;
import com.steve_rizzo.emeraldscore.emeraldsgames.commands.games.EGCommand;
import com.steve_rizzo.emeraldscore.emeraldsgames.commands.mobarena.KitCommand;
import com.steve_rizzo.emeraldscore.emeraldsgames.events.OpenGamesGUI;
import com.steve_rizzo.emeraldscore.emeraldsgames.games.mobarena.KitGUI;
import com.steve_rizzo.emeraldscore.events.*;
import com.steve_rizzo.emeraldscore.features.LaunchDonorDrop;
import com.steve_rizzo.emeraldscore.features.SpecialGift;
import com.zaxxer.hikari.HikariDataSource;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Main extends JavaPlugin {

    public static String prefix;
    public static Permission perms = null;
    public static Economy economy = null;
    public EconomyImplement economyImplementer;

    private void instanceClasses() {
        economyImplementer = new EconomyImplement();
    }

    private Economy provider;

    public void hook() {
        provider = economyImplementer;
        Bukkit.getServicesManager().register(Economy.class, this.provider, this, ServicePriority.Highest);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "VaultAPI hooked into " + ChatColor.AQUA + this.getName());
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, this.provider);
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "VaultAPI unhooked from " + ChatColor.AQUA + this.getName());

    }


    public static Chat chat = null;
    public static String
            hostEmeralds, portEmeralds, passwordEmeralds, usernameEmeralds, nameEmeralds;
    public static Main core;
    public static MobArena mobarena;
    private static Main instance;
    File spawnYML = new File(getDataFolder() + "/spawn.yml");
    File emeraldsYML = new File(getDataFolder() + "/emeralds.yml");
    File cooldownNPCYML = new File(getDataFolder() + "/cooldownNPC.yml");
    public FileConfiguration spawnConfig = YamlConfiguration.loadConfiguration(spawnYML);
    public FileConfiguration emeraldsConfig = YamlConfiguration.loadConfiguration(emeraldsYML);
    public FileConfiguration cooldownConfig = YamlConfiguration.loadConfiguration(cooldownNPCYML);

    private HikariDataSource hikari;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        core = this;

        instance = this;

        instanceClasses();
        hook();

        saveYML(spawnConfig, spawnYML);
        saveYML(emeraldsConfig, emeraldsYML);
        saveYML(cooldownConfig, cooldownNPCYML);

        setupPermissions();
        setupChat();
        setupEconomy();
        setupMobArena();

        hostEmeralds = emeraldsConfig.getString("db_host");
        portEmeralds = emeraldsConfig.getString("db_port");
        passwordEmeralds = emeraldsConfig.getString("db_pass");
        usernameEmeralds = emeraldsConfig.getString("db_user");
        nameEmeralds = emeraldsConfig.getString("db_name");

        prefix = ChatColor.GRAY + "[" + ChatColor.GREEN + "EmeraldsMC" + ChatColor.GRAY + "]: ";

        Bukkit.getServer().getPluginManager().registerEvents(new ServerJoinPlayer(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RandomBlockReward(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SpecialTNT(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatPing(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerVanish(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PingServer(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SpecialGift(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RankShopCommand(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BountyKillPlayer(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new NoLongerAFK(), this);

        this.getCommand("rank").setExecutor(new RankCommand(this));
        this.getCommand("fly").setExecutor(new FlyCommand());
        this.getCommand("flyspeed").setExecutor(new FlyspeedCommand());
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("bc").setExecutor(new BroadcastCommand());
        this.getCommand("broadcast").setExecutor(new BroadcastCommand());
        this.getCommand("gm").setExecutor(new GamemodeCommand());
        this.getCommand("farmworld").setExecutor(new FarmworldCommand());
        this.getCommand("floorparty").setExecutor(new FloorParty());
        this.getCommand("launchdonordrop").setExecutor(new LaunchDonorDrop());
        this.getCommand("store").setExecutor(new StoreCommand());
        this.getCommand("rules").setExecutor(new RulesCommand());
        this.getCommand("staffchat").setExecutor(new StaffChatCommand());
        this.getCommand("afk").setExecutor(new AFKCommand());
        this.getCommand("casino").setExecutor(new CasinoCommand());
        this.getCommand("maintenance").setExecutor(new MaintenanceMode());

        this.getCommand("balance").setExecutor(new BalanceCommand());
        this.getCommand("pay").setExecutor(new PayCommand());
        this.getCommand("setbalance").setExecutor(new SetCommand());
        this.getCommand("takebalance").setExecutor(new TakeCommand());
        this.getCommand("givebalance").setExecutor(new GiveCommand());
        this.getCommand("baltop").setExecutor(new BaltopCommand());
        this.getCommand("apply").setExecutor(new ApplyCommand());
        this.getCommand("rankshop").setExecutor(new RankShopCommand());
        this.getCommand("message").setExecutor(new MessageCommand());
        this.getCommand("reply").setExecutor(new MessageCommand());
        this.getCommand("payheal").setExecutor(new PayHeal());
        this.getCommand("pvp").setExecutor(new PVPCommand());
        this.getCommand("survival").setExecutor(new SurvivalCommand());
        this.getCommand("list").setExecutor(new ListCommand());
        this.getCommand("help").setExecutor(new HelpCommand());
        this.getCommand("test").setExecutor(new TestCommand());

        OpenGamesGUI openGamesGUI = new OpenGamesGUI();
        this.getCommand("eg").setExecutor(new EGCommand());
        Bukkit.getServer().getPluginManager().registerEvents(openGamesGUI, this);

        KitGUI kitGUI = new KitGUI();
        this.getCommand("kit").setExecutor(new KitCommand());
        Bukkit.getServer().getPluginManager().registerEvents(kitGUI, this);

        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", hostEmeralds);
        hikari.addDataSourceProperty("port", portEmeralds);
        hikari.addDataSourceProperty("databaseName", nameEmeralds);
        hikari.addDataSourceProperty("user", usernameEmeralds);
        hikari.addDataSourceProperty("password", passwordEmeralds);

        createTable();
        createEconomyTable();
        setPVPRegions();

        // Plugin startup success
        System.out.println(Color.GREEN + ChatColor.stripColor(prefix) + " has SUCCESSFULLY LOADED!");
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer()
                .getServicesManager().getRegistration(
                        Permission.class);
        if (permissionProvider != null) {
            perms = (Permission) permissionProvider.getProvider();
        }
        return perms != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    private void setPVPRegions() {
        if (getServer().getWorld("world") != null) Objects.requireNonNull(getServer().getWorld("world")).setPVP(false);
        if (getServer().getWorld("farmworld") != null)
            Objects.requireNonNull(getServer().getWorld("farmworld")).setPVP(false);
        if (getServer().getWorld("lootworld") != null)
            Objects.requireNonNull(getServer().getWorld("lootworld")).setPVP(true);
    }

    private void setupMobArena() {
        Plugin plugin = getServer().getPluginManager().getPlugin("MobArena");
        if (plugin == null) {
            return;
        }
        this.mobarena = (MobArena) plugin;
    }

    public void saveYML(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

        unhook();

        saveYML(spawnConfig, spawnYML);
        saveYML(emeraldsConfig, emeraldsYML);
        saveYML(cooldownConfig, cooldownNPCYML);

        if (hikari != null) hikari.close();

        Bukkit.getServer().getPluginManager().disablePlugin(this);
        System.out.println(Color.RED + ChatColor.stripColor(prefix) + " has SUCCESSFULLY UNLOADED!");

    }

    public Connection getHikari() {
        try {
            return hikari.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void createTable() {
        try (Connection connection = hikari.getConnection();
             Statement statement = connection.createStatement();) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Ranks(UUID varchar(36) UNIQUE, name VARCHAR(16), rank varchar(16), date DATE);");
            System.out.println("[EmeraldsMC - Rank Handler]: Table created and/or connected successfully.");
        } catch (SQLException e) {
            System.out.println("[EmeraldsMC - Rank Handler]: Error. See below.");
            e.printStackTrace();
        }
    }

    public void createEconomyTable() {
        try (Connection connection = hikari.getConnection();
             Statement statement = connection.createStatement();) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS EmeraldsCash(UUID varchar(36) UNIQUE, name VARCHAR(16), balance INT, date DATE, PRIMARY KEY (UUID));");
            System.out.println("[EmeraldsMC - Currency Handler]: Table created and/or connected successfully.");
        } catch (SQLException e) {
            System.out.println("[EmeraldsMC - Currency Handler]: Error. See below.");
            e.printStackTrace();
        }
    }
}
