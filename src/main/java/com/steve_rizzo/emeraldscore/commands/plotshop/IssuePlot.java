package com.steve_rizzo.emeraldscore.commands.plotshop;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.commands.economy.api.EmeraldsCashAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getServer;

public class IssuePlot implements CommandExecutor {

    Logger log = Main.core.getLogger();
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (args.length != 2) {
            log.log(Level.INFO, "Usage: /issueplot <player> <plotID>");
            return true;
        }

        String uuid = Objects.requireNonNull(Bukkit.getPlayer(args[0])).getUniqueId().toString();
        String plotRegionID = "shopplot_"+args[1];

        if (hasEnoughFunds(uuid, 10000)) {

            EmeraldsCashAPI.deductFundsUUID(uuid, 10000);

            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), "rg addowner -w \"world\"" + " " + plotRegionID + " " + args[0]);
            log.log(Level.INFO, "Plot `" + plotRegionID + "` successfully issued to player `" + args[0] + "`");

            return true;

        }

        log.log(Level.INFO, "Plot `" + plotRegionID + "` NOT issued to player `" + args[0] + "` || REASON: Insufficient Funds");

        return false;
    }

    private boolean hasEnoughFunds(String uuid, int cost) {
        return EmeraldsCashAPI.returnUUIDBalance(uuid) >= cost;
    }

}