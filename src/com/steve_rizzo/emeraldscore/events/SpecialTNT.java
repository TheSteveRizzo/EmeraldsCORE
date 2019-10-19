package com.steve_rizzo.emeraldscore.events;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class SpecialTNT implements Listener {

    int delay = 60;
    private HashMap<FallingBlock, Location> blocks = new HashMap<FallingBlock, Location>();

    @EventHandler
    void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntityType().equals(EntityType.PRIMED_TNT)) {

            TNTPrimed tnt = (TNTPrimed) e.getEntity();

            if (tnt.getSource() instanceof Player) {

                Player p = (Player) tnt.getSource();

                if (p.hasPermission("emeraldsmc.tntuse")) {

                    for (Block b : e.blockList()) {
                        final BlockState state = b.getState();

                        bounceBlock(b);

                        if ((b.getType() == Material.SAND) || (b.getType() == Material.GRAVEL)) {
                            delay += 1;
                        }

                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.core, new Runnable() {
                            public void run() {
                                state.update(true, false);
                            }
                        }, delay);
                    }
                }
            }
        }
    }

    public void bounceBlock(Block b) {
        if (b == null) return;

        FallingBlock fb = b.getWorld()
                .spawnFallingBlock(b.getLocation(), b.getBlockData());

        blocks.put(fb, b.getLocation());

        b.setType(Material.AIR);

        float x = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
        float y = 2;//(float) -5 + (float)(Math.random() * ((5 - -5) + 1));
        float z = (float) -0.3 + (float) (Math.random() * ((0.3 - -0.3) + 1));

        fb.setVelocity(new Vector(x, y, z));
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock) {
            FallingBlock fb = (FallingBlock) e.getEntity();
            if (blocks.containsKey(fb)) {
                e.setCancelled(true);
                Location loc = blocks.get(fb);
                loc.getBlock().setType(fb.getMaterial());
                loc.getBlock().setBlockData(fb.getBlockData());
                blocks.remove(fb);
                fb.remove();
            }
        }
    }
}