package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.utils.Ranks;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class ServerJoinPlayer implements Listener {

    public static Permission perms = Main.perms;
    public static Chat chat = Main.chat;
    Main serverEssentials;

    private Ranks ranks = new Ranks();

    public static void setPlayerTabName(Player p) {
        String playerGroup = perms.getPrimaryGroup(p);
        String playerName = p.getName();
        String prefix = chat.getGroupPrefix(p.getWorld(), playerGroup);
        p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix) + playerName);
    }

    public static String getPlayerPrefixAndName(Player player) {
        String playerGroup = perms.getPrimaryGroup(player);
        String playerName = player.getName();
        String prefix = chat.getGroupPrefix(player.getWorld(), playerGroup);
        return ChatColor.translateAlternateColorCodes('&', prefix + playerName);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        if (!e.getPlayer().hasPlayedBefore()) {


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
                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), "welcome.mp3", 10F, 1F);
            }

        } else {

            e.setJoinMessage(getPlayerPrefixAndName(e.getPlayer()) + ChatColor.YELLOW + " has joined The Emeralds.");
            ranks.updateAndSaveData(e.getPlayer());

            // Spawn a single firework
            Firework fw = (Firework) e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            addFireworkEffects(fwm);
            fw.setFireworkMeta(fwm);

        }

        if (e.getPlayer().getAllowFlight()) e.getPlayer().setAllowFlight(false);
        setPlayerTabName(e.getPlayer());

        // NOT YET FULLY TESTED & SUPPORTED.
        // setUserGlowStatus(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ranks.updateAndSaveData(e.getPlayer());
        e.setQuitMessage(getPlayerPrefixAndName(e.getPlayer()) + ChatColor.YELLOW + " has left The Emeralds.");
        // NOT YET FULLY TESTED & SUPPORTED.
        // GlowUtil.disableGlow(e.getPlayer());
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

    // Sets a user's glow.
    private void setUserGlowStatus(Player player) {

        String playerRank = perms.getPrimaryGroup(player);

        /** NOT YET FULLY TESTED & SUPPORTED.

         if (isPermittedToUseGlow(playerRank)) {
         // Delay until a user actually joins to set glow color.
         Bukkit.getScheduler().runTaskLater(Main.core, new Runnable() {
        @Override public void run() {
        GlowUtil.activateGlow(player, returnGlowColor(playerRank));
        }
        }, 20);
         }
         */
    }

    private String returnGlowColor(String playerRank) {
        switch (playerRank.toLowerCase()) {
            case "owner":
                return "red";
            case "admin":
                return "darkred";
            case "mod":
                return "aqua";
            case "helper":
                return "darkaqua";
            case "youtuber":
                return "gold";
            case "elite":
                return "green";
        }
        return "black";
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
