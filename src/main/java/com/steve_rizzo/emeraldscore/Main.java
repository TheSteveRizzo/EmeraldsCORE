package com.steve_rizzo.emeraldscore;

import com.garbagemule.MobArena.MobArena;
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
import java.util.Objects;

public class Main extends JavaPlugin {

    public static String prefix;
    public static Permission perms = null;
    public static Economy economy = null;
    public static Chat chat = null;
    public static String host, port, password, username, name;
    public static Main core;
    public static MobArena mobarena;
    File spawnYML = new File(getDataFolder() + "/spawn.yml");
    public FileConfiguration spawnConfig = YamlConfiguration.loadConfiguration(spawnYML);

    @Override
    public void onEnable() {

        core = this;

        saveSpawnYML(spawnConfig, spawnYML);

        setupPermissions();
        setupChat();
        setupEconomy();
        setupMobArena();

        host = getConfig().getString("db_host");
        port = getConfig().getString("db_port");
        password = getConfig().getString("db_pass");
        username = getConfig().getString("db_user");
        name = getConfig().getString("db_name");

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

        saveDefaultConfig();
        StaffHandler.openConnection();
        StaffHandler.createTables();

        System.out.println(Color.GREEN + ChatColor.stripColor(prefix) + " has SUCCESSFULLY LOADED!");
    }

    @Override
    public void onDisable() {

        saveSpawnYML(spawnConfig, spawnYML);

        try {
            if (StaffHandler.connection != null && StaffHandler.connection.isClosed()) {
                StaffHandler.connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bukkit.getServer().getPluginManager().disablePlugin(this);
        System.out.println(Color.RED + ChatColor.stripColor(prefix) + " has SUCCESSFULLY UNLOADED!");

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

    public void saveSpawnYML(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
