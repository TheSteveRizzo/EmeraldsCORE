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
import com.steve_rizzo.emeraldscore.features.miningpouch.*;
import com.steve_rizzo.emeraldscore.features.villagersave.VillagerSaverCommands;
import com.steve_rizzo.emeraldscore.features.villagersave.VillagerSaverListener;
import com.zaxxer.hikari.HikariDataSource;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
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
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static String prefix;
    public static Permission perms = null;
    public static Economy economy = null;
    public EconomyImplement economyImplementer;

    public static ShapedRecipe pouchRecipe;

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
    public static ArrayList<String> WorldBlackList = new ArrayList<>();

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

        // Load Core Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new ServerJoinPlayer(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RandomBlockReward(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RandomFishingReward(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SpecialTNT(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatPing(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerVanish(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PingServer(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SpecialGift(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RankShopCommand(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BountyKillPlayer(), this);

        // Load MiningPouch Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new PouchPickupItem(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PouchClick(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PouchInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PouchPlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PouchPreCraft(), this);
        loadMiningPouchRecipe();;

        // Load Core Commands
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

        // Load Villager Features
        Bukkit.getServer().getPluginManager().registerEvents(new VillagerSaverListener(), this);
        LogInfo("Villager Plugin listener registered.");
        LoadWorldBlackList();
        this.getCommand("villagersaver").setExecutor(new VillagerSaverCommands());
        LogInfo("Villager Plugin commands registered.");
        LogInfo("VillagerSaver extension loaded!");

        // Start TimedXP Timer Function
        System.out.println(Color.GREEN + ChatColor.stripColor(prefix) + " started TimedXP Timer function!");
        TimedXP.startTask();

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

        // Stop TimedXP Timer Function
        System.out.println(Color.GREEN + ChatColor.stripColor(prefix) + " stopped TimedXP Timer function!");
        TimedXP.endTask();

        // Save Villager worlds
        SaveWorldBlackList();
        LogInfo("VillagerSaver disabled!");

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

    private void LogInfo(String line) {
        this.getLogger().log(Level.INFO, line);
    }

    private void LogWarn(String line) {
        this.getLogger().log(Level.WARNING, line);
    }

    private void LogError(String line) {
        this.getLogger().log(Level.SEVERE, line);
    }

    private void SaveWorldBlackList() {
        LogInfo("Saving blacklist.");
        File WorldBlackListFile = new File(getDataFolder().getAbsolutePath(), "VillagerBlacklistWorld.yml");
        try {
            WorldBlackListFile.delete();
            WorldBlackListFile.createNewFile();
            if (WorldBlackList == null)
                WorldBlackList = new ArrayList<>();
        } catch (Exception ex) {
            LogError("Error saving blacklist: " + ex.getMessage());
            return;
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(WorldBlackListFile);
        yamlConfiguration.set("BlackList", WorldBlackList);
        try {
            yamlConfiguration.save(WorldBlackListFile);
        } catch (Exception ex) {
            LogError("Error saving blacklist: " + ex.getMessage());
            return;
        }
        LogInfo("Blacklist saved.");
    }

    private void LoadWorldBlackList() {
        File WorldBlackListFile = new File(getDataFolder().getAbsolutePath(), "VillagerBlacklistWorld.yml");
        try {
            WorldBlackListFile.createNewFile();
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(WorldBlackListFile);
            WorldBlackList = (ArrayList<String>) yamlConfiguration.get("BlackList");
        } catch (Exception ex) {
            LogWarn("World blacklist file has not been loaded: " + ex.getMessage());
            LogWarn("If this is the first time running the plugin the file will be created on server stop.");
            return;
        }
        if (WorldBlackList == null)
            WorldBlackList = new ArrayList<>();
        LogInfo("World Blacklist loaded.");
    }

    private void loadMiningPouchRecipe() {
        LogInfo("MINING POUCH RECIPE ENABLED");
        pouchRecipe = new ShapedRecipe((new Pouch(null)).getPouch());
        pouchRecipe.shape(new String[]{"XYX", "XZX", "XYX"});
        pouchRecipe.setIngredient('X', Material.LEATHER);
        pouchRecipe.setIngredient('Y', Material.CHEST);
        pouchRecipe.setIngredient('Z', Material.NETHERITE_PICKAXE);
        getServer().addRecipe((Recipe) pouchRecipe);
    }
}
