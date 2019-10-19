package com.steve_rizzo.emeraldscore.commands;

import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FloatItem implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {

            if (sender instanceof Player) {

                Player p = (Player) sender;

                if (GamesAPI.isDonorOrHigher(p)) {


                } else {


                }
            }
        }

        return false;

    }
}
