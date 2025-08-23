package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FloorParty implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefix = Main.prefix;

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (player.hasPermission("emeraldsmc.floorparty")) {

                Location loc = player.getLocation().clone().add(0.0, -1.0, 0.0);
                int radius = 1;
                setFloor(loc, radius);

            }
        }

        return true;

    }

    public void setFloor(Location center, int radius) {

        HashMap<BlockState, Location> blockList = new HashMap<>();

        for (int xMod = -radius; xMod <= radius; xMod++) {
            for (int zMod = -radius; zMod <= radius; zMod++) {
                blockList.put(center.getBlock().getRelative(xMod, 0, zMod).getState(), center.getBlock().getRelative(xMod, 0, zMod).getLocation());
                Block theBlock = center.getBlock().getRelative(xMod, 0, zMod);
                // theBlockDisco add
            }
        }


    }
}
