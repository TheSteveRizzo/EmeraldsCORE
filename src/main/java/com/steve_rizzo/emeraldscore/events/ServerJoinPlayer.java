package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import com.steve_rizzo.emeraldscore.utils.Ranks;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ServerJoinPlayer implements Listener {

    public static Permission perms = Main.perms;
    public static Chat chat = Main.chat;
    public static Ranks ranks = new Ranks();

    public static String getPlayerPrefixAndName(Player player) {
        String playerGroup = perms.getPrimaryGroup(player);
        String playerName = player.getName();
        String prefix = chat.getGroupPrefix("EmeraldsKingdom", playerGroup);
        return ChatColor.translateAlternateColorCodes('&', prefix + playerName);
    }

    public static String getPlayerPrefixAndName(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        String coloredPrefix;

        if (player != null) {
            String playerGroup = perms.getPrimaryGroup(player);
            String prefix = chat.getGroupPrefix("EmeraldsKingdom", playerGroup);
            coloredPrefix = ChatColor.translateAlternateColorCodes('&', prefix);
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            String playerGroup = perms.getPrimaryGroup(null, offlinePlayer);
            String prefix = chat.getGroupPrefix("EmeraldsKingdom", playerGroup);
            coloredPrefix = ChatColor.translateAlternateColorCodes('&', prefix);
        }

        return coloredPrefix + playerName;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        String worldName = "world";
        if (Main.serverIDName.equalsIgnoreCase("hub")) worldName = "world";
        if (Main.serverIDName.equalsIgnoreCase("smp")) worldName = "EmeraldsKingdom";
        if (Main.serverIDName.equalsIgnoreCase("bed")) worldName = "world";

        // Spawn 5 fireworks
        for (int i = 0; i < 5; i++) {
            Firework fw = (Firework) Bukkit.getServer().getWorld(worldName).spawnEntity(e.getPlayer().getLocation(), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            fw.setMetadata("spawnfirework", new FixedMetadataValue(Main.core, true));
            addFireworkEffects(fwm);
            fw.setFireworkMeta(fwm);
        }

        if (!e.getPlayer().hasPlayedBefore()) {

            e.setJoinMessage(ChatColor.LIGHT_PURPLE + e.getPlayer().getName() + " has joined The Emeralds for the first time!");

            String finalWorldName = worldName;
            EmeraldsCashAPI.doesAccountExist(e.getPlayer().getUniqueId().toString()).thenAccept(doesUserAccountExist -> {
                if (!doesUserAccountExist) {
                    // Set money for first-time user
                    EmeraldsCashAPI.setBalance(e.getPlayer(), 500);

                    ranks.loadFirstTimePlayer(e.getPlayer());

                    World userWorld = Bukkit.getWorld(finalWorldName);
                    if (userWorld != null) {
                        Location userSpawnLoc = userWorld.getSpawnLocation();
                        float yaw = userSpawnLoc.getYaw();
                        float pitch = userSpawnLoc.getPitch();

                        Location locWithRotation = new Location(
                                userWorld,
                                userSpawnLoc.getX(),
                                userSpawnLoc.getY(),
                                userSpawnLoc.getZ(),
                                yaw,
                                pitch
                        );
                        p.teleport(locWithRotation);

                    }

                    // Play a sound to inform the users about a new user joining!
                    for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                        Bukkit.getServer().getWorld(finalWorldName).playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10F, 1F);
                    }
                }

                e.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "=====" + ChatColor.GRAY + "[" + ChatColor.GREEN + ChatColor.BOLD + "EmeraldsMC" + ChatColor.GRAY + "]" + ChatColor.YELLOW + "" + ChatColor.BOLD + "=====\n" +
                        ChatColor.GRAY + "Welcome to " + ChatColor.GREEN + "play.emeraldsmc.com" + ChatColor.GRAY + "!\nMake sure to:\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Read the " + ChatColor.RED + ChatColor.BOLD + "/rules\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "View commands " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "/commands\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Apply for Member " + ChatColor.GREEN + ChatColor.BOLD + "/apply\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Join our Discord " + ChatColor.GOLD + ChatColor.BOLD + "/discord\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Go back to Survival " + ChatColor.DARK_AQUA + ChatColor.BOLD + "/survival\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Visit the Hub " + ChatColor.DARK_AQUA + ChatColor.BOLD + "/hub\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Play Bed Wars " + ChatColor.DARK_AQUA + ChatColor.BOLD + "/bed\n" +
                        ChatColor.YELLOW + "> " + ChatColor.AQUA + "Vote & Earn Cash " + ChatColor.YELLOW + ChatColor.BOLD + "/vote\n" +
                        ChatColor.YELLOW + ChatColor.BOLD + "===== ===== =====");

            }).exceptionally(ex -> {
                // Handle any exceptions
                ex.printStackTrace();
                return null;
            });
        } else {

            // Display player join message
            e.setJoinMessage("");
            Bukkit.broadcastMessage(getPlayerPrefixAndName(e.getPlayer()) + ChatColor.YELLOW + " has joined The Emeralds.");
            // Update and save player rank data
            ranks.updateAndSaveData(e.getPlayer());

            // Spawn a single firework
            Firework fw = (Firework) Bukkit.getServer().getWorld(worldName).spawnEntity(e.getPlayer().getLocation(), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            fw.setMetadata("spawnfirework", new FixedMetadataValue(Main.core, true));
            addFireworkEffects(fwm);
            fw.setFireworkMeta(fwm);

        }

        // Set flight to disabled
        if (e.getPlayer().getAllowFlight()) e.getPlayer().setAllowFlight(false);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // Save rank data
        ranks.updateAndSaveData(e.getPlayer());
        // Display quit message
        e.setQuitMessage(getPlayerPrefixAndName(e.getPlayer()) + ChatColor.YELLOW + " has left The Emeralds.");
    }

    // Disable damage caused by the joining Firework
    @EventHandler
    public void fireworkDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework) {
            Firework fw = (Firework) event.getDamager();
            if (fw.hasMetadata("spawnfirework")) {
                event.setCancelled(true);
            }
        }
    }

    private Color getColor(int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }

        return c;
    }

    private FireworkMeta addFireworkEffects(FireworkMeta fwm) {

        //Our random generator
        Random r = new Random();

        //Get the type
        int rt = r.nextInt(4) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;

        //Get our random colours
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);

        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

        //Then apply the effect to the meta
        fwm.addEffect(effect);

        //Generate some random power and set it
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);

        return fwm;
    }

}
