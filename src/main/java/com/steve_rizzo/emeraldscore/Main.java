package com.steve_rizzo.emeraldscore;

import com.garbagemule.MobArena.MobArena;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.steve_rizzo.emeraldscore.commands.*;
import com.steve_rizzo.emeraldscore.emeraldsgames.commands.games.EGCommand;
import com.steve_rizzo.emeraldscore.emeraldsgames.commands.mobarena.KitCommand;
import com.steve_rizzo.emeraldscore.emeraldsgames.events.OpenGamesGUI;
import com.steve_rizzo.emeraldscore.emeraldsgames.games.mobarena.KitGUI;
import com.steve_rizzo.emeraldscore.events.RandomBlockReward;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import com.steve_rizzo.emeraldscore.events.SpecialTNT;
import com.steve_rizzo.emeraldscore.staffapps.StaffHandler;
import com.steve_rizzo.emeraldscore.staffapps.events.PlayerJoin;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Main extends JavaPlugin {

    public static String prefix;
    public static Permission perms = null;
    public static Economy economy = null;
    public static Chat chat = null;
    public static String hostStaff, portStaff, passwordStaff, usernameStaff, nameStaff,
            hostEmeralds, portEmeralds, passwordEmeralds, usernameEmeralds, nameEmeralds;
    public static Main core;
    public static MobArena mobarena;
    private static Main instance;

    File spawnYML = new File(getDataFolder() + "/spawn.yml");
    File emeraldsYML = new File(getDataFolder() + "/emeralds.yml");
    public FileConfiguration spawnConfig = YamlConfiguration.loadConfiguration(spawnYML);
    public FileConfiguration emeraldsConfig = YamlConfiguration.loadConfiguration(emeraldsYML);

    private HikariDataSource hikari;
    private FloatItem floatItem;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        core = this;
        instance = this;

        saveYML(spawnConfig, spawnYML);
        saveYML(emeraldsConfig, emeraldsYML);

        setupPermissions();
        setupChat();
        setupEconomy();
        setupMobArena();

        hostStaff = getConfig().getString("db_host");
        portStaff = getConfig().getString("db_port");
        passwordStaff = getConfig().getString("db_pass");
        usernameStaff = getConfig().getString("db_user");
        nameStaff = getConfig().getString("db_name");

        hostEmeralds = emeraldsConfig.getString("db_host");
        portEmeralds = emeraldsConfig.getString("db_port");
        passwordEmeralds = emeraldsConfig.getString("db_pass");
        usernameEmeralds = emeraldsConfig.getString("db_user");
        nameEmeralds = emeraldsConfig.getString("db_name");

        prefix = ChatColor.GRAY + "[" + ChatColor.GREEN + "EmeraldsMC" + ChatColor.GRAY + "]: ";

        Bukkit.getServer().getPluginManager().registerEvents(new ServerJoinPlayer(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RandomBlockReward(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SpecialTNT(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        this.getCommand("rank").setExecutor(new RankCommand(this));
        this.getCommand("fly").setExecutor(new FlyCommand());
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        this.getCommand("spawn").setExecutor(new SpawnCommand(this));
        this.getCommand("bc").setExecutor(new BroadcastCommand());
        this.getCommand("gm").setExecutor(new GamemodeCommand());
        this.getCommand("farmworld").setExecutor(new FarmworldCommand());
        this.getCommand("floorparty").setExecutor(new FloorParty());

        OpenGamesGUI openGamesGUI = new OpenGamesGUI();
        this.getCommand("eg").setExecutor(new EGCommand());
        Bukkit.getServer().getPluginManager().registerEvents(openGamesGUI, this);

        KitGUI kitGUI = new KitGUI();
        this.getCommand("kit").setExecutor(new KitCommand());
        Bukkit.getServer().getPluginManager().registerEvents(kitGUI, this);

        floatItem = new FloatItem();
        this.getCommand("floatitem").setExecutor(floatItem);

        saveDefaultConfig();
        StaffHandler.openConnection();
        StaffHandler.createTables();

        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", hostEmeralds);
        hikari.addDataSourceProperty("port", portEmeralds);
        hikari.addDataSourceProperty("databaseName", nameEmeralds);
        hikari.addDataSourceProperty("user", usernameEmeralds);
        hikari.addDataSourceProperty("password", passwordEmeralds);

        createTable();

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

        saveYML(spawnConfig, spawnYML);
        saveYML(emeraldsConfig, emeraldsYML);

        try {
            if (StaffHandler.connection != null && StaffHandler.connection.isClosed()) {
                StaffHandler.connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (hikari != null) hikari.close();

        Iterator it = floatItem.activeUserHolograms.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Hologram holoToDelete = (Hologram) pair.getValue();
            it.remove();
        }

        floatItem.activeUserHolograms.clear();

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
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Ranks(UUID varchar(36), name VARCHAR(16), rank varchar(16), date DATE)");
            System.out.println("[EmeraldsMC - Rank Handler]: Table created and/or connected successfully.");
        } catch (SQLException e) {
            System.out.println("[EmeraldsMC - Rank Handler]: Error. See below.");
            e.printStackTrace();
        }
    }
}