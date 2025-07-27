package com.steve_rizzo.emeraldscore;

import com.andrei1058.bedwars.api.BedWars;
import com.steve_rizzo.emeraldscore.bedwars.ApplyBedWarsTokens;
import com.steve_rizzo.emeraldscore.chat.PrefixSender;
import com.steve_rizzo.emeraldscore.commands.*;
import com.steve_rizzo.emeraldscore.commands.economy.*;
import com.steve_rizzo.emeraldscore.commands.economy.vault.EconomyImplement;
import com.steve_rizzo.emeraldscore.commands.plotshop.IssuePlot;
import com.steve_rizzo.emeraldscore.commands.tokens.*;
import com.steve_rizzo.emeraldscore.events.*;
import com.steve_rizzo.emeraldscore.features.LaunchDonorDrop;
import com.steve_rizzo.emeraldscore.features.SpecialGift;
import com.steve_rizzo.emeraldscore.features.jobs.JobHandler;
import com.steve_rizzo.emeraldscore.features.miningpouch.*;
import com.steve_rizzo.emeraldscore.features.villagersave.VillagerSaverCommands;
import com.steve_rizzo.emeraldscore.features.villagersave.VillagerSaverListener;
import com.steve_rizzo.emeraldscore.pets.CatCommand;
import com.steve_rizzo.emeraldscore.pets.DogCommand;
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
    public static String serverIDName;
    public static Permission perms = null;
    public static Economy economy = null;
    public EconomyImplement economyImplementer;
    public static ShapedRecipe pouchRecipe;
    private void instanceClasses() {
        economyImplementer = new EconomyImplement();
    }

    private Economy provider;

    public static BedWars bwAPI;

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
    private static Main instance;
    File emeraldsYML = new File(getDataFolder() + "/emeralds.yml");
    File cooldownNPCYML = new File(getDataFolder() + "/cooldownNPC.yml");
    public File jobDataYML = new File(getDataFolder() + "/jobData.yml");
    public File serverIDYML = new File(getDataFolder() + "/serverID.yml");
    public FileConfiguration emeraldsConfig = YamlConfiguration.loadConfiguration(emeraldsYML);
    public FileConfiguration cooldownConfig = YamlConfiguration.loadConfiguration(cooldownNPCYML);
    public FileConfiguration jobDataConfig = YamlConfiguration.loadConfiguration(jobDataYML);
    public FileConfiguration serverIDConfig = YamlConfiguration.loadConfiguration(serverIDYML);
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

        // Save config files
        saveYML(emeraldsConfig, emeraldsYML);
        saveYML(cooldownConfig, cooldownNPCYML);
        saveYML(jobDataConfig, jobDataYML);
        saveYML(serverIDConfig, serverIDYML);

        // Load Server Identifier File
        handleServerIDConfig();

        // Set up hooks
        setupPermissions();
        setupChat();
        setupEconomy();

        // Register cross-server chat events
        Bukkit.getServer().getPluginManager().registerEvents(new PrefixSender(), this);
        // Register for plugin messaging
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "emeraldscore:chat");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "emeraldscore:chat", new PrefixSender());

        // Database info
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
        Bukkit.getServer().getPluginManager().registerEvents(new TokenShopCommand(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BountyKillPlayer(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new NoLongerAFK(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new WardenDeathEvent(), this);

        // Load Token Listener
        Bukkit.getServer().getPluginManager().registerEvents(new TokenHandler(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BuyTokensCommand(), this);

        // Load Job Listener
        Bukkit.getServer().getPluginManager().registerEvents(new JobHandler(), this);

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
        this.getCommand("maintenance").setExecutor(new MaintenanceMode());
        this.getCommand("apply").setExecutor(new ApplyCommand());
        this.getCommand("rankshop").setExecutor(new RankShopCommand());
        this.getCommand("tokenshop").setExecutor(new TokenShopCommand());
        this.getCommand("message").setExecutor(new MessageCommand());
        this.getCommand("reply").setExecutor(new MessageCommand());
        this.getCommand("payheal").setExecutor(new PayHeal());
        this.getCommand("survival").setExecutor(new SurvivalCommand());
        this.getCommand("list").setExecutor(new ListCommand());
        this.getCommand("help").setExecutor(new HelpCommand());
        this.getCommand("test").setExecutor(new TestCommand());
        this.getCommand("clearchat").setExecutor(new ClearChatCommand());
        this.getCommand("commands").setExecutor(new CommandsCommand());
        this.getCommand("plugins").setExecutor(new PluginsCommand());
        this.getCommand("hub").setExecutor(new HubCommand());
        this.getCommand("boost").setExecutor(new BoostCommand());
        this.getCommand("claimfurnaces").setExecutor(new FurnaceClaimCommand());
        this.getCommand("buytokens").setExecutor(new BuyTokensCommand());
        this.getCommand("fireworks").setExecutor(new FireworksCommand());
        this.getCommand("back").setExecutor(new BackCommand());

        // Currency Commands
        this.getCommand("balance").setExecutor(new BalanceCommand());
        this.getCommand("pay").setExecutor(new PayCommand());
        this.getCommand("setbalance").setExecutor(new SetCommand());
        this.getCommand("takebalance").setExecutor(new TakeCommand());
        this.getCommand("givebalance").setExecutor(new GiveCommand());
        this.getCommand("baltop").setExecutor(new BaltopCommand());

        // Tokens Commands
        this.getCommand("tokens").setExecutor(new TokenBalanceCommand());
        this.getCommand("settokens").setExecutor(new SetTokensCommand());
        this.getCommand("givetokens").setExecutor(new GiveTokensCommand());
        this.getCommand("tokenstop").setExecutor(new TokenstopCommand());

        // Job Commands
        this.getCommand("job").setExecutor(new JobHandler());
        this.getCommand("jobs").setExecutor(new JobHandler());

        // Plot Handler Command
        this.getCommand("issueplot").setExecutor(new IssuePlot());

        // Emeralds Simple Pet Handler
        this.getCommand("cat").setExecutor(new CatCommand());
        this.getCommand("dog").setExecutor(new DogCommand());

        // Emeralds Bed Wars Commands
        this.getCommand("applybedwarstokens").setExecutor(new ApplyBedWarsTokens());


        // Database Connection
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", hostEmeralds);
        hikari.addDataSourceProperty("port", portEmeralds);
        hikari.addDataSourceProperty("databaseName", nameEmeralds);
        hikari.addDataSourceProperty("user", usernameEmeralds);
        hikari.addDataSourceProperty("password", passwordEmeralds);

        // Create Databases
        createTable();
        createEconomyTable();
        createTokensTable();
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

        // Load Job Task Data
        JobHandler.loadJobsFromConfig();

        // Load Bed Wars API (if Bed Wars server ONLY)
        if (serverIDName.equalsIgnoreCase("bed")) {
            bwAPI = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        }
    }

    private void handleServerIDConfig() {
        // Check if "serverID" exists in the config
        if (serverIDConfig.contains("serverID")) {
            // Retrieve and assign the existing serverID value to serverIDName
            serverIDName = serverIDConfig.getString("serverID");
        } else {
            // Default value if "serverID" does not exist
            serverIDName = "defaultServerName"; // Replace with your desired default value

            // Set the default value in the config
            serverIDConfig.set("serverID", serverIDName);

            // Save the changes to the config file
            try {
                serverIDConfig.save(serverIDYML);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }

        // Optionally, log the serverIDName to ensure it's set correctly
        getLogger().info("Server ID Name: " + serverIDName);
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
        if (Main.serverIDName.equalsIgnoreCase("smp")) {
            if (getServer().getWorld("EmeraldsKingdom") != null)
                Objects.requireNonNull(getServer().getWorld("EmeraldsKingdom")).setPVP(true);
            if (getServer().getWorld("farmworld") != null)
                Objects.requireNonNull(getServer().getWorld("farmworld")).setPVP(false);
        }
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

        saveYML(emeraldsConfig, emeraldsYML);
        saveYML(cooldownConfig, cooldownNPCYML);
        saveYML(jobDataConfig, jobDataYML);

        if (hikari != null) hikari.close();

        // Stop TimedXP Timer Function
        System.out.println(Color.GREEN + ChatColor.stripColor(prefix) + " stopped TimedXP Timer function!");
        TimedXP.endTask();

        // Save Villager worlds
        SaveWorldBlackList();
        LogInfo("VillagerSaver disabled!");

        // CLear death locations
        BackCommand.deathLocations.clear();

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

    public void createTokensTable() {
        try (Connection connection = hikari.getConnection();
             Statement statement = connection.createStatement();) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS EmeraldsTokens(UUID varchar(36) UNIQUE, name VARCHAR(16), balance INT, date DATE, PRIMARY KEY (UUID));");
            System.out.println("[EmeraldsMC - Token Handler]: Tokens table created and/or connected successfully.");
        } catch (SQLException e) {
            System.out.println("[EmeraldsMC - Token Handler]: Error. See below.");
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
