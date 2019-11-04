package com.steve_rizzo.emeraldscore.features;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import com.steve_rizzo.emeraldscore.utils.LimitedRepeatingTask;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LaunchDonorDrop implements CommandExecutor {

    // LIST OF GIFTS:

    // [1% Chance to occur]
    // JACKPOT (32 Emeralds, 32 Diamonds,
    //      1 End Crystal, 1 Beacon)

    // [2% Chance to be dropped]
    // 1x END CRYSTAL

    // [5% Chance to be dropped (one of the following)]
    // 32x DIAMOND
    // 32x EMERALD
    // 1x BEACON
    // 1x DIAMOND SWORD, SHARPNESS III
    // 1x DIAMOND PICK AXE, EFFICIENCY III, UNBREAKABLE II

    // [10% Chance to be dropped (one of the following)]
    // 1x DIAMOND HELMET, PROTECTION II
    // 1x IRON CHESTPLATE, PROTECTION III

    // [15% Chance to be dropped]
    // 32x GOLD INGOT

    // [20% Chance to be dropped (one of the following)]
    // 16x EMERALD
    // 16x DIAMOND

    // [25% Chance to be dropped]
    // 64x IRON INGOT

    // [~22% Chance to be dropped (one of the following)]
    // 8x DIAMOND
    // 8x EMERALD
    // 128x OAK PLANKS
    // 32x OAK WOOD LOGS

    // END LIST OF GIFTS

    // Drop timers
    public static int timesToRun = 30;
    public static BukkitTask task = null;
    private List<Location> listOfDropLocations = new ArrayList<>();
    // Item by percent lists
    private List<ItemStack> itemsAt5Percent = new ArrayList<>();
    private List<ItemStack> itemsAt10Percent = new ArrayList<>();
    private List<ItemStack> itemsAt20Percent = new ArrayList<>();
    private List<ItemStack> itemsAt22Percent = new ArrayList<>();

    public LaunchDonorDrop() {
        initializeLists();
    }

    public static boolean percentChance(double percent) {
        if (percent > 100 || percent < 0) {
            throw new IllegalArgumentException("Percentage cannot be greater than 100 or less than 0!");
        }
        double result = new Random().nextDouble() * 100;
        return result <= percent;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) return false;

        // Only correct usage /launchdonordrop {user} (where user = user who launched/donated for this)
        if (args.length == 1) {

            // Collect info.
            String playerName = args[0];

            FileConfiguration spawnyml = Main.core.spawnConfig;
            String locSerialized = spawnyml.getString(Bukkit.getWorld("world").getName() + ".Location");
            String[] locString = locSerialized.split(",");

            Location spawnLoc = new Location(Bukkit.getWorld(locString[0]), Double.parseDouble(locString[1]), Double.parseDouble(locString[2]) + 5, Double.parseDouble(locString[3]) - 3);

            // Set regions to drop from in list.
            if (listOfDropLocations.isEmpty()) setRadiusListLocations(spawnLoc);

            triggerDropPartyEvent(playerName);

        }

        return false;

    }

    private void setRadiusListLocations(Location spawnLocation) {

        int radius = 5;
        HashMap<BlockState, Location> blockList = new HashMap<>();

        for (int xMod = -radius; xMod <= radius; xMod++) {
            for (int zMod = -radius; zMod <= radius; zMod++) {
                Block theBlock = spawnLocation.getBlock().getRelative(xMod, 0, zMod);
                listOfDropLocations.add(theBlock.getLocation());
            }
        }
    }

    private Location locToSpawnItem() {
        Random rand = new Random();
        Location loc = (Location) listOfDropLocations.get(rand.nextInt(listOfDropLocations.size()));

        return loc;
    }

    private void randomChooseItemAndSpawn() {

        // Get main world as object.
        World world = Bukkit.getWorld("world");

        // Notify players
        Bukkit.broadcastMessage(Main.prefix + ChatColor.GRAY + "A jackpot item was dropped at spawn!");

        // Play drop sound
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            all.playSound(all.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 2F, 1F);
        }

        // [1% Chance to occur]
        if (percentChance(1)) {

            ItemStack jackpotItem1 = new ItemStack(Material.EMERALD, 32);
            ItemStack jackpotItem2 = new ItemStack(Material.DIAMOND, 32);
            ItemStack jackpotItem3 = new ItemStack(Material.END_CRYSTAL, 1);
            ItemStack jackpotItem4 = new ItemStack(Material.BEACON, 1);

            // Play sound to alert players.
            for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 2F, 1F);
            }

            // Announce drop party JACKPOT was hit!
            Bukkit.broadcastMessage(ChatColor.GREEN + "---------=[x+x]=---------");
            Bukkit.broadcastMessage(Main.prefix + ChatColor.LIGHT_PURPLE +
                    ChatColor.BOLD + "ATTENTION: " + ChatColor.RESET
                    + ChatColor.AQUA + "THE JACKPOT WAS HIT!\n" +
                    ChatColor.GREEN + "32x Emeralds" + ChatColor.AQUA + ", " +
                    ChatColor.GREEN + "32x Diamonds" + ChatColor.AQUA + ", " +
                    ChatColor.GREEN + "1x End Crystal" + ChatColor.AQUA + ", " +
                    ChatColor.GREEN + "and 1x Beacon " + ChatColor.AQUA + "were dropped!");
            Bukkit.broadcastMessage(ChatColor.GREEN + "---------=[x+x]=---------");

            world.dropItemNaturally(locToSpawnItem(), jackpotItem1);
            world.dropItemNaturally(locToSpawnItem(), jackpotItem2);
            world.dropItemNaturally(locToSpawnItem(), jackpotItem3);
            world.dropItemNaturally(locToSpawnItem(), jackpotItem4);


            // [2% Chance to be dropped]
        } else if (percentChance(2)) {

            ItemStack endCrystal = new ItemStack(Material.END_CRYSTAL, 1);
            world.dropItemNaturally(locToSpawnItem(), endCrystal);

            // [5% Chance to be dropped (one of the following)]
        } else if (percentChance(5)) {

            Random rand = new Random();
            ItemStack itemToDrop = (ItemStack)
                    itemsAt5Percent.get(rand.nextInt(itemsAt5Percent.size()));

            world.dropItemNaturally(locToSpawnItem(), itemToDrop);

            // [10% Chance to be dropped (one of the following)]
        } else if (percentChance(10)) {

            Random rand = new Random();
            ItemStack itemToDrop = (ItemStack)
                    itemsAt10Percent.get(rand.nextInt(itemsAt10Percent.size()));

            world.dropItemNaturally(locToSpawnItem(), itemToDrop);

            // [15% Chance to be dropped]
        } else if (percentChance(15)) {

            ItemStack gold = new ItemStack(Material.GOLD_INGOT, 32);
            world.dropItemNaturally(locToSpawnItem(), gold);

            // [20% Chance to be dropped (one of the following)]
        } else if (percentChance(20)) {

            Random rand = new Random();
            ItemStack itemToDrop = (ItemStack)
                    itemsAt20Percent.get(rand.nextInt(itemsAt20Percent.size()));

            world.dropItemNaturally(locToSpawnItem(), itemToDrop);

            // [25% Chance to be dropped]
        } else if (percentChance(25)) {

            ItemStack iron = new ItemStack(Material.IRON_INGOT, 64);
            world.dropItemNaturally(locToSpawnItem(), iron);

            // [~22% Chance to be dropped (one of the following)]
        } else {

            Random rand = new Random();
            ItemStack itemToDrop = (ItemStack)
                    itemsAt22Percent.get(rand.nextInt(itemsAt22Percent.size()));

            world.dropItemNaturally(locToSpawnItem(), itemToDrop);

        }
    }

    private void initializeLists() {

        // 5% Items
        itemsAt5Percent.add(new ItemStack(Material.DIAMOND, 32));
        itemsAt5Percent.add(new ItemStack(Material.EMERALD, 32));
        itemsAt5Percent.add(new ItemStack(Material.BEACON, 1));
        ItemStack diaSword = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta diaSwordMeta = diaSword.getItemMeta();
        diaSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
        diaSword.setItemMeta(diaSwordMeta);
        itemsAt5Percent.add(diaSword);
        ItemStack diaPick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta diaPickMeta = diaPick.getItemMeta();
        diaPickMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
        diaPickMeta.addEnchant(Enchantment.DURABILITY, 2, true);
        diaPick.setItemMeta(diaPickMeta);
        itemsAt5Percent.add(diaPick);

        // 10% Items
        ItemStack diaHelm = new ItemStack(Material.DIAMOND_HELMET, 1);
        ItemMeta diaHelmMeta = diaHelm.getItemMeta();
        diaHelmMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
        diaHelm.setItemMeta(diaHelmMeta);
        itemsAt10Percent.add(diaHelm);
        ItemStack ironChest = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemMeta ironChestMeta = ironChest.getItemMeta();
        ironChestMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
        ironChest.setItemMeta(ironChestMeta);
        itemsAt10Percent.add(ironChest);

        // 20% Items
        ItemStack emeralds = new ItemStack(Material.EMERALD, 16);
        itemsAt20Percent.add(emeralds);
        ItemStack diamonds = new ItemStack(Material.DIAMOND, 16);
        itemsAt20Percent.add(diamonds);

        // 30% Items
        ItemStack diamond = new ItemStack(Material.DIAMOND, 8);
        itemsAt22Percent.add(diamond);
        ItemStack emerald = new ItemStack(Material.EMERALD, 8);
        itemsAt22Percent.add(emerald);
        ItemStack oakWood = new ItemStack(Material.OAK_PLANKS, 128);
        itemsAt22Percent.add(oakWood);
        ItemStack oakWoodLogs = new ItemStack(Material.OAK_LOG, 32);
        itemsAt22Percent.add(oakWoodLogs);

    }

    private void triggerDropPartyEvent(String playerName) {

        // Play sound to alert players.
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 2F, 1F);
        }

        // Announce drop party donation!
        Bukkit.broadcastMessage(ChatColor.GREEN + "---------=[x+x]=---------");
        Bukkit.broadcastMessage(Main.prefix + ChatColor.LIGHT_PURPLE +
                ChatColor.BOLD + "ATTENTION: " + ChatColor.RESET
                + ChatColor.LIGHT_PURPLE + "A SERVER-WIDE \"JACKPOT\" DROP PARTY IS " + ChatColor.GREEN + "ABOUT TO OCCUR" + ChatColor.LIGHT_PURPLE + " AT SPAWN!");
        if (Bukkit.getPlayer(playerName) != null) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "This drop party was launched by: "
                    + ChatColor.AQUA + ServerJoinPlayer.getPlayerPrefixAndName(Bukkit.getPlayer(playerName))
                    + ChatColor.YELLOW + ". Make sure to say thanks!");
        } else {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "This drop party was launched by: "
                    + ChatColor.AQUA + playerName
                    + ChatColor.YELLOW + ". Make sure to say thanks!");
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "---------=[x+x]=---------");


        // Begins after 5 seconds (200 Ticks)
        // Drops a total of 30 items. Each item has a chance of being dropped at the spawn droppers.
        // Spawn a random item at a random drop location every second (20 ticks) for 30 seconds (iterations)
        LimitedRepeatingTask countdown = new LimitedRepeatingTask(Main.core,
                0, 20, 30) {
            @Override
            public void onRepeat() {
                randomChooseItemAndSpawn();
            }
        };

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.core, new Runnable() {
            public void run() {
                sendClosingMessage(playerName);
            }
        }, 650L);
    }

    private void sendClosingMessage(String playerName) {
        // Drop items is over.
        Bukkit.broadcastMessage(ChatColor.GREEN + "---------=[x+x]=---------");
        Bukkit.broadcastMessage(Main.prefix + ChatColor.LIGHT_PURPLE +
                ChatColor.BOLD + "ATTENTION: " + ChatColor.RESET
                + ChatColor.LIGHT_PURPLE + "THE SERVER-WIDE \"JACKPOT\" DROP PARTY IS " + ChatColor.RED + "NOW OVER" + ChatColor.LIGHT_PURPLE + "!");
        if (Bukkit.getPlayer(playerName) != null) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "This drop party was launched by: "
                    + ChatColor.AQUA + ServerJoinPlayer.getPlayerPrefixAndName(Bukkit.getPlayer(playerName))
                    + ChatColor.YELLOW + ".\nThank you!");
        } else {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "This drop party was launched by: "
                    + ChatColor.AQUA + playerName
                    + ChatColor.YELLOW + ".\nThank you!");
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "---------=[x+x]=---------");
    }
}
