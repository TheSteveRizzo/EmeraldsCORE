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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

import static com.steve_rizzo.emeraldscore.Main.prefix;

public class ServerJoinPlayer implements Listener {

    public static Permission perms = Main.perms;
    public static Chat chat = Main.chat;
    public static Ranks ranks = new Ranks();

    public static void setPlayerTabName(Player p) {
        String playerGroup = perms.getPrimaryGroup(p);
        String playerName = p.getName();
        String prefix = chat.getGroupPrefix(p.getWorld(), playerGroup);
        p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix) + playerName);
    }

    public static void setAFKPlayerTabName(Player p) {
        String playerName = p.getName();
        p.setPlayerListName(ChatColor.GRAY + playerName);
    }

    public static String getPlayerPrefixAndName(Player player) {
        String playerGroup = perms.getPrimaryGroup(player);
        String playerName = player.getName();
        String prefix = chat.getGroupPrefix(player.getWorld(), playerGroup);
        return ChatColor.translateAlternateColorCodes('&', prefix + playerName);
    }

    public static String getPlayerPrefixAndName(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player != null) {
            String playerGroup = perms.getPrimaryGroup(player);
            String prefix = chat.getGroupPrefix(player.getWorld(), playerGroup);
            return ChatColor.translateAlternateColorCodes('&', prefix + playerName);
        } else {
            // If the player is offline, use OfflinePlayer to get the data
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            String playerGroup = perms.getPrimaryGroup(null, offlinePlayer);
            String prefix = chat.getGroupPrefix("world", playerGroup);
            return ChatColor.translateAlternateColorCodes('&', prefix + playerName);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        if (!e.getPlayer().hasPlayedBefore()) {

            // Set money first time user
            EmeraldsCashAPI.setBalance(e.getPlayer(), 500);


            e.setJoinMessage(ChatColor.LIGHT_PURPLE + e.getPlayer().getName() + " has joined The Emeralds for the first time!");
            ranks.loadFirstTimePlayer(e.getPlayer());

            // Spawn 5 fireworks
            for (int i = 0; i < 5; i++) {
                Firework fw = (Firework) e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();
                addFireworkEffects(fwm);
                fw.setFireworkMeta(fwm);
            }

            // Play a sound to inform the users about a new user joining!
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10F, 1F);
            }

            e.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "=====" + ChatColor.GRAY + "[" + ChatColor.GREEN + ChatColor.BOLD + "EmeraldsMC" + ChatColor.GRAY + "]" + ChatColor.AQUA + "" + ChatColor.BOLD +  "=====\n" +
                    ChatColor.GRAY + "Welcome to " + ChatColor.GREEN + "play.emeraldsmc.com" + ChatColor.GRAY + "! Make sure to:\n" +
                    ChatColor.AQUA + "> Read the " + ChatColor.RED + ChatColor.BOLD + "/rules\n" +
                    ChatColor.AQUA + "> Apply for Member using " + ChatColor.GREEN + ChatColor.BOLD + "/apply\n" +
                    ChatColor.AQUA + "> Join our Discord using " + ChatColor.GOLD + ChatColor.BOLD + "/discord\n" +
                    ChatColor.AQUA + "> Visit our PVP World using " + ChatColor.RED + ChatColor.BOLD + "/pvp\n" +
                    ChatColor.AQUA + "> Go back to Survival using " + ChatColor.DARK_AQUA + ChatColor.BOLD + "/survival\n" +
                    ChatColor.AQUA + ChatColor.BOLD + "===== ===== ====="
            );

        } else {

            // Display player join message
            e.setJoinMessage(getPlayerPrefixAndName(e.getPlayer()) + ChatColor.YELLOW + " has joined The Emeralds.");
            // Update and save player rank data
            ranks.updateAndSaveData(e.getPlayer());

            // Spawn a single firework
            Firework fw = (Firework) e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            addFireworkEffects(fwm);
            fw.setFireworkMeta(fwm);

        }

        // Set flight to disabled
        if (e.getPlayer().getAllowFlight()) e.getPlayer().setAllowFlight(false);
        // Set player tab name
        setPlayerTabName(e.getPlayer());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // Save rank data
        ranks.updateAndSaveData(e.getPlayer());
        // Display quit message
        e.setQuitMessage(getPlayerPrefixAndName(e.getPlayer()) + ChatColor.YELLOW + " has left The Emeralds.");
        e.setQuitMessage(ChatColor.YELLOW + e.getPlayer().getName() + " has left The Emeralds.");
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

    // Check if can glow
    private boolean isPermittedToUseGlow(String rank) {
        if ((rank.equalsIgnoreCase("elite") || (rank.equalsIgnoreCase("youtuber") ||
                (rank.equalsIgnoreCase("mod") || (rank.equalsIgnoreCase("helper") ||
                        (rank.equalsIgnoreCase("admin") || (rank.equalsIgnoreCase("owner"))))))))
            return true;

        return false;
    }

}
