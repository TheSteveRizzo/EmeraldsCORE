package com.steve_rizzo.emeraldscore.features;

import com.steve_rizzo.emeraldscore.Main;
import com.steve_rizzo.emeraldscore.emeraldsgames.api.GamesAPI;
import com.steve_rizzo.emeraldscore.events.ServerJoinPlayer;
import net.minecraft.server.v1_16_R3.CommandSaveOn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Bukkit.getServer;

// Class for "Special Gift" promotions, which provides users with a gift for each day specified!
public class SpecialGift implements Listener {

    public class GiftOption {
        public String command;
        public int weight;
        public GiftType type;
        public String displayName;  // <-- Add this field

        public GiftOption(String command, int weight, GiftType type, String displayName) {
            this.command = command;
            this.weight = weight;
            this.type = type;
            this.displayName = displayName;
        }
    }

    public enum GiftType {
        COMMON("COMMON", ChatColor.GRAY),
        UNCOMMON("UNCOMMON", ChatColor.GREEN),
        RARE("RARE", ChatColor.BLUE),
        LEGENDARY("LEGENDARY", ChatColor.YELLOW),
        EXCLUSIVE("EXCLUSIVE", ChatColor.DARK_PURPLE);

        public final String name;
        public final ChatColor color;

        GiftType(String name, ChatColor color) {
            this.name = name;
            this.color = color;
        }
    }


    FileConfiguration config = Main.core.cooldownConfig;

    private List<GiftOption> regularGiftList = Arrays.asList(
            new GiftOption("givebal {user} 2500", 8, GiftType.COMMON, "$2,500 Emeralds Cash"),
            new GiftOption("givebal {user} 5000", 6, GiftType.UNCOMMON, "$5,000 Emeralds Cash"),
            new GiftOption("givebal {user} 7500", 4, GiftType.RARE, "$7,500 Emeralds Cash"),
            new GiftOption("givebal {user} 10000", 2, GiftType.LEGENDARY, "$10,000 Emeralds Cash"),
            new GiftOption("givebal {user} 12500", 1, GiftType.EXCLUSIVE, "$12,500 Emeralds Cash"),
            new GiftOption("give {user} minecraft:emerald 2", 6, GiftType.UNCOMMON, "2x Emeralds"),
            new GiftOption("give {user} minecraft:emerald 4", 4, GiftType.RARE, "4x Emeralds"),
            new GiftOption("give {user} minecraft:emerald 8", 2, GiftType.LEGENDARY, "8x Emeralds"),
            new GiftOption("give {user} minecraft:diamond 6", 8, GiftType.COMMON, "6x Diamonds"),
            new GiftOption("give {user} minecraft:diamond 8", 6, GiftType.UNCOMMON, "8x Diamonds"),
            new GiftOption("give {user} minecraft:diamond 12", 4, GiftType.RARE, "12x Diamonds"),
            new GiftOption("give {user} minecraft:cooked_beef 16", 10, GiftType.COMMON, "16x Cooked Beef"),
            new GiftOption("give {user} minecraft:cooked_beef 32", 8, GiftType.COMMON, "32x Cooked Beef"),
            new GiftOption("give {user} minecraft:cooked_beef 48", 6, GiftType.UNCOMMON, "48x Cooked Beef"),
            new GiftOption("give {user} minecraft:white_wool 64", 6, GiftType.UNCOMMON, "64x White Wool"),
            new GiftOption("give {user} minecraft:bookshelf 32", 6, GiftType.UNCOMMON, "32x Bookshelf"),
            new GiftOption("give {user} minecraft:pumpkin_pie 16", 6, GiftType.UNCOMMON, "16x Pumpkin Pie"),
            new GiftOption("give {user} minecraft:cake 8", 6, GiftType.UNCOMMON, "8x Cake"),
            new GiftOption("give {user} minecraft:raw_iron 32", 10, GiftType.COMMON, "32x Raw Iron"),
            new GiftOption("give {user} minecraft:raw_iron 64", 6, GiftType.UNCOMMON, "64x Raw Iron"),
            new GiftOption("give {user} minecraft:raw_iron 128", 4, GiftType.RARE, "128x Raw Iron"),
            new GiftOption("give {user} minecraft:stone 32", 10, GiftType.COMMON, "32x Stone"),
            new GiftOption("give {user} minecraft:stone 64", 8, GiftType.COMMON, "64x Stone"),
            new GiftOption("give {user} minecraft:stone 256", 6, GiftType.UNCOMMON, "256x Stone"),
            new GiftOption("give {user} minecraft:deepslate 32", 10, GiftType.COMMON, "32x Deepslate"),
            new GiftOption("give {user} minecraft:deepslate 64", 8, GiftType.COMMON, "64x Deepslate"),
            new GiftOption("give {user} minecraft:deepslate 128", 6, GiftType.UNCOMMON, "128x Deepslate"),
            new GiftOption("give {user} minecraft:white_concrete 32", 10, GiftType.COMMON, "32x White Concrete"),
            new GiftOption("give {user} minecraft:white_concrete 64", 8, GiftType.COMMON, "64x White Concrete"),
            new GiftOption("give {user} minecraft:black_concrete 32", 6, GiftType.UNCOMMON, "32x Black Concrete"),
            new GiftOption("give {user} minecraft:black_concrete 64", 4, GiftType.RARE, "64x Black Concrete"),
            new GiftOption("give {user} minecraft:raw_gold 32", 6, GiftType.UNCOMMON, "32x Raw Gold"),
            new GiftOption("give {user} minecraft:sea_lantern 8", 4, GiftType.RARE, "8x Sea Lantern"),
            new GiftOption("givetokens {user} 2", 2, GiftType.LEGENDARY, "2x Emeralds Tokens"),
            new GiftOption("givetokens {user} 4", 1, GiftType.EXCLUSIVE, "4x Emeralds Tokens"),
            new GiftOption("voucher give {user} LotteryVoucher 2", 2, GiftType.LEGENDARY, "2x Lottery Vouchers"),
            new GiftOption("voucher give {user} LotteryVoucher 4", 1, GiftType.EXCLUSIVE, "4x Lottery Vouchers")
    );

    private List<GiftOption> donorGiftList = Arrays.asList(
            new GiftOption("givebal {user} 7500", 8, GiftType.COMMON, "$7,500 Emeralds Cash"),
            new GiftOption("givebal {user} 10000", 6, GiftType.UNCOMMON, "$10,000 Emeralds Cash"),
            new GiftOption("givebal {user} 12500", 4, GiftType.RARE, "$12,500 Emeralds Cash"),
            new GiftOption("give {user} minecraft:emerald 8", 6, GiftType.UNCOMMON, "8x Emeralds"),
            new GiftOption("give {user} minecraft:emerald 12", 4, GiftType.RARE, "12x Emeralds"),
            new GiftOption("give {user} minecraft:emerald 16", 2, GiftType.LEGENDARY, "16x Emeralds"),
            new GiftOption("give {user} minecraft:diamond 12", 6, GiftType.UNCOMMON, "12x Diamonds"),
            new GiftOption("give {user} minecraft:diamond 16", 4, GiftType.RARE, "16x Diamonds"),
            new GiftOption("give {user} minecraft:diamond 32", 4, GiftType.RARE, "32x Diamonds"),
            new GiftOption("give {user} minecraft:cooked_beef 48", 8, GiftType.COMMON, "48x Cooked Beef"),
            new GiftOption("give {user} minecraft:cooked_beef 64", 8, GiftType.COMMON, "64x Cooked Beef"),
            new GiftOption("give {user} minecraft:bookshelf 64", 6, GiftType.UNCOMMON, "64x Bookshelf"),
            new GiftOption("give {user} minecraft:white_wool 64", 6, GiftType.UNCOMMON, "64x White Wool"),
            new GiftOption("give {user} minecraft:netherite_ingot 2", 4, GiftType.RARE, "2x Netherite Ingots"),
            new GiftOption("give {user} minecraft:netherite_ingot 4", 2, GiftType.LEGENDARY, "4x Netherite Ingots"),
            new GiftOption("give {user} minecraft:netherite_ingot 6", 1, GiftType.EXCLUSIVE, "6x Netherite Ingots"),
            new GiftOption("give {user} minecraft:raw_iron 128", 6, GiftType.UNCOMMON, "128x Raw Iron"),
            new GiftOption("give {user} minecraft:raw_iron 256", 4, GiftType.RARE, "256x Raw Iron"),
            new GiftOption("give {user} minecraft:obsidian 64", 4, GiftType.RARE, "64x Obsidian"),
            new GiftOption("give {user} minecraft:glass 128", 6, GiftType.UNCOMMON, "128x Glass"),
            new GiftOption("give {user} minecraft:sea_lantern 16", 4, GiftType.RARE, "16x Sea Lantern"),
            new GiftOption("give {user} minecraft:black_concrete 16", 6, GiftType.UNCOMMON, "16x Black Concrete"),
            new GiftOption("give {user} minecraft:black_concrete 64", 4, GiftType.RARE, "64x Black Concrete"),
            new GiftOption("give {user} minecraft:beacon 1", 1, GiftType.EXCLUSIVE, "1x Beacon"),
            new GiftOption("give {user} minecraft:wither_skeleton_skull 1", 1, GiftType.EXCLUSIVE, "1x Wither Skeleton Skull"),
            new GiftOption("give {user} minecraft:heart_of_the_sea 1", 2, GiftType.LEGENDARY, "1x Heart of the Sea"),
            new GiftOption("give {user} minecraft:golden_apple 2", 2, GiftType.LEGENDARY, "2x Golden Apple"),
            new GiftOption("give {user} minecraft:enchanted_golden_apple 1", 2, GiftType.LEGENDARY, "1x Enchanted Golden Apple"),
            new GiftOption("givetokens {user} 3", 4, GiftType.RARE, "3x Emeralds Tokens"),
            new GiftOption("givetokens {user} 4", 2, GiftType.LEGENDARY, "4x Emeralds Tokens"),
            new GiftOption("givetokens {user} 5", 1, GiftType.EXCLUSIVE, "5x Emeralds Tokens"),
            new GiftOption("voucher give {user} LotteryVoucher 4", 2, GiftType.LEGENDARY, "4x Lottery Vouchers"),
            new GiftOption("voucher give {user} LotteryVoucher 6", 1, GiftType.EXCLUSIVE, "6x Lottery Vouchers")
    );

    private List<GiftOption> bedWarsGiftList = Arrays.asList(
            new GiftOption("bw level giveXp {user} 500", 16, GiftType.COMMON, "500 BedWars XP"),
            new GiftOption("givebal {user} 1000", 16, GiftType.COMMON, "$1,000 Emeralds Cash"),
            new GiftOption("bw level giveXp {user} 750", 16, GiftType.COMMON, "750 BedWars XP"),
            new GiftOption("givebal {user} 5000", 16, GiftType.COMMON, "$5,000 Emeralds Cash"),
            new GiftOption("bw level giveXp {user} 1000", 16, GiftType.COMMON, "1,000 BedWars XP"),
            new GiftOption("givetokens {user} 5", 3, GiftType.UNCOMMON, "5x Emeralds Tokens"),
            new GiftOption("givebal {user} 10000", 3, GiftType.UNCOMMON, "$10,000 Emeralds Cash"),
            new GiftOption("givetokens {user} 10", 3, GiftType.UNCOMMON, "10x Emeralds Tokens"),
            new GiftOption("givebal {user} 20000", 3, GiftType.UNCOMMON, "$20,000 Emeralds Cash"),
            new GiftOption("bw level giveXp {user} 2000", 3, GiftType.UNCOMMON, "2,000 BedWars XP"),
            new GiftOption("ecrates key give {user} warcrate 1", 1, GiftType.RARE, "1x Warcrate Key"),
            new GiftOption("givebal {user} 30000", 1, GiftType.RARE, "$30,000 Emeralds Cash"),
            new GiftOption("bw level giveXp {user} 5000", 1, GiftType.RARE, "5,000 BedWars XP"),
            new GiftOption("givetokens {user} 15", 1, GiftType.RARE, "15x Emeralds Tokens"),
            new GiftOption("ecrates key give {user} warcrate 2", 1, GiftType.EXCLUSIVE, "2x Warcrate Keys")
    );

    private List<GiftOption> bedWarsDonorGiftList = Arrays.asList(
            new GiftOption("givebal {user} 2000", 20, GiftType.COMMON, "$2,000 Emeralds Cash"),
            new GiftOption("bw level giveXp {user} 1250", 20, GiftType.COMMON, "1,250 BedWars XP"),
            new GiftOption("givebal {user} 3000", 17, GiftType.UNCOMMON, "$3,000 Emeralds Cash"),
            new GiftOption("bw level giveXp {user} 2000", 17, GiftType.UNCOMMON, "2,000 BedWars XP"),
            new GiftOption("givebal {user} 5000", 4, GiftType.RARE, "$5,000 Emeralds Cash"),
            new GiftOption("givebal {user} 10000", 4, GiftType.RARE, "$10,000 Emeralds Cash"),
            new GiftOption("bw level giveXp {user} 5000", 4, GiftType.RARE, "5,000 BedWars XP"),
            new GiftOption("givebal {user} 20000", 4, GiftType.RARE, "$20,000 Emeralds Cash"),
            new GiftOption("givetokens {user} 10", 4, GiftType.RARE, "10x Emeralds Tokens"),
            new GiftOption("bw level giveXp {user} 8000", 4, GiftType.RARE, "8,000 BedWars XP"),
            new GiftOption("givebal {user} 40000", 4, GiftType.RARE, "$40,000 Emeralds Cash"),
            new GiftOption("givetokens {user} 20", 2, GiftType.LEGENDARY, "20x Emeralds Tokens"),
            new GiftOption("givebal {user} 50000", 2, GiftType.LEGENDARY, "$50,000 Emeralds Cash"),
            new GiftOption("bw level giveXp {user} 9000", 2, GiftType.LEGENDARY, "9,000 BedWars XP"),
            new GiftOption("ecrates key give {user} warcrate 5", 1, GiftType.EXCLUSIVE, "5x Warcrate Keys")
    );


    @EventHandler
    public void onClickGiftNPC(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        String clickedName = e.getRightClicked().getName();

        if (clickedName.equalsIgnoreCase("vote")) p.performCommand("vote");
        if (clickedName.equalsIgnoreCase("kits")) p.performCommand("kits");
        if (clickedName.equalsIgnoreCase("bounty") || clickedName.equalsIgnoreCase("bounty2")) p.performCommand("bounty");
        if (clickedName.equalsIgnoreCase("rtp") || clickedName.equalsIgnoreCase("rtp2")) p.performCommand("rtp");

        Date curDate = new Date();
        LocalDate localDate = curDate.toInstant().atZone(ZoneId.of("America/New_York")).toLocalDate();
        int year = localDate.getYear(), month = localDate.getMonthValue(), day = localDate.getDayOfMonth();

        if (Main.serverIDName.equalsIgnoreCase("bed") && clickedName.equalsIgnoreCase("EmeraldsGhost")) {
            if (year == 2025 && month == 10) {
                if (isInCooldown(p)) {
                    if (isDonor(p)) {
                        giveBedWarsGiftDonor(p);
                    } else {
                        giveBedWarsGiftReg(p);
                    }
                    addToCooldown(p);
                } else {
                    p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a Bed Wars gift today. You may receive your next gift in: " +
                            ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");
                }
            }
        }

        if (clickedName.equalsIgnoreCase("EmeraldsGhost")) {
            if (year == 2025 && month == 10) {
                if (isInCooldown(p)) {
                    if (isDonor(p)) {
                        giveDonorGift(p);
                    } else {
                        giveRegularGift(p);
                    }
                    addToCooldown(p);
                } else {
                    p.sendMessage(Main.prefix + ChatColor.RED + "You have already received a gift today. You may receive your next gift in: " +
                            ChatColor.AQUA + getRemainingTime(p) + ChatColor.RED + "!");
                }
            }
        }
    }

    private void giveRegularGift(Player player) {
        GiftOption chosen = chooseGift(regularGiftList);

        String message = ChatColor.BOLD + "" + ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "EmeraldsMC" + ChatColor.DARK_GRAY + "]: "
                + ChatColor.YELLOW + ServerJoinPlayer.getPlayerPrefixAndName(player)
                + ChatColor.YELLOW + " just claimed a "
                + chosen.type.color + chosen.type.name
                + ChatColor.GRAY + " (" + chosen.type.color + chosen.displayName + ChatColor.GRAY + ")"
                + ChatColor.YELLOW + " gift "
                + ChatColor.YELLOW + "from the "
                + ChatColor.GREEN + ChatColor.BOLD + "Emeralds Queen"
                + ChatColor.YELLOW + "! Go claim your daily prize at /spawn!";

        Bukkit.broadcastMessage(message);

        Bukkit.dispatchCommand(getServer().getConsoleSender(),
                chosen.command.replace("{user}", player.getName()));
    }

    private void giveDonorGift(Player player) {
        GiftOption chosen = chooseGift(donorGiftList);

        String message = ChatColor.BOLD + "" + ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "EmeraldsMC" + ChatColor.DARK_GRAY + "]: "
                + ChatColor.YELLOW + ServerJoinPlayer.getPlayerPrefixAndName(player)
                + ChatColor.YELLOW + " just claimed a "
                + chosen.type.color + chosen.type.name
                + ChatColor.GRAY + " (" + chosen.type.color + chosen.displayName + ChatColor.GRAY + ")"
                + ChatColor.YELLOW + " "
                + ChatColor.DARK_PURPLE + ChatColor.BOLD + "DONOR Gift "
                + ChatColor.YELLOW + "from the "
                + ChatColor.GREEN + ChatColor.BOLD + "Emeralds Queen"
                + ChatColor.YELLOW + "! Donors receive extra rewards!";

        Bukkit.broadcastMessage(message);

        Bukkit.dispatchCommand(getServer().getConsoleSender(),
                chosen.command.replace("{user}", player.getName()));
    }

    private void giveBedWarsGiftReg(Player player) {
        GiftOption chosen = chooseGift(bedWarsGiftList);

        String message = ChatColor.BOLD + "" + ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "EmeraldsMC" + ChatColor.DARK_GRAY + "]: "
                + ChatColor.YELLOW + ServerJoinPlayer.getPlayerPrefixAndName(player)
                + ChatColor.YELLOW + " just claimed a "
                + chosen.type.color + chosen.type.name
                + ChatColor.GRAY + " (" + chosen.type.color + chosen.displayName + ChatColor.GRAY + ")"
                + ChatColor.YELLOW + " gift "
                + ChatColor.YELLOW + "from the "
                + ChatColor.AQUA + ChatColor.BOLD + "War Leader"
                + ChatColor.YELLOW + "! Go claim your daily prize at /spawn!";

        Bukkit.broadcastMessage(message);

        Bukkit.dispatchCommand(getServer().getConsoleSender(),
                chosen.command.replace("{user}", player.getName()));
    }

    private void giveBedWarsGiftDonor(Player player) {
        GiftOption chosen = chooseGift(bedWarsDonorGiftList);

        String message = ChatColor.BOLD + "" + ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "EmeraldsMC" + ChatColor.DARK_GRAY + "]: "
                + ChatColor.YELLOW + ServerJoinPlayer.getPlayerPrefixAndName(player)
                + ChatColor.YELLOW + " just claimed a "
                + chosen.type.color + chosen.type.name
                + ChatColor.GRAY + " (" + chosen.type.color + chosen.displayName + ChatColor.GRAY + ")"
                + ChatColor.YELLOW + " "
                + ChatColor.DARK_PURPLE + ChatColor.BOLD + "DONOR Gift "
                + ChatColor.YELLOW + "from the "
                + ChatColor.AQUA + ChatColor.BOLD + "War Leader"
                + ChatColor.YELLOW + "! Donors receive extra rewards!";

        Bukkit.broadcastMessage(message);

        Bukkit.dispatchCommand(getServer().getConsoleSender(),
                chosen.command.replace("{user}", player.getName()));
    }

    private void addToCooldown(Player player) {

        config.set(player.getUniqueId().toString(), System.currentTimeMillis());
    }

    private Long getCooldownTime(Player player) {

        return (Long) config.get(player.getUniqueId().toString());
    }

    // TODO
    private boolean isInCooldown(Player player) {

        Long time = getCooldownTime(player);

        if (time != null) {
            // Checks if one day has passed.
            if (System.currentTimeMillis() - time >= TimeUnit.DAYS.toMillis(1)) {
                return true;
            }
            return false;
        }
        return true;
    }

    private GiftOption chooseGift(List<GiftOption> giftList) {
        double totalWeight = giftList.stream().mapToDouble(g -> g.weight).sum();
        double random = Math.random() * totalWeight;
        double cumulativeWeight = 0.0;

        for (GiftOption gift : giftList) {
            cumulativeWeight += gift.weight;
            if (random < cumulativeWeight) {
                return gift;
            }
        }

        return giftList.get(giftList.size() - 1);
    }

    private String getRemainingTime(Player player) {

        Long time = getCooldownTime(player);

        long timeLeft = (time + TimeUnit.DAYS.toMillis(1) - System.currentTimeMillis());

        long seconds = timeLeft / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        return hours % 24 + " hours " + minutes % 60 + " minutes " + seconds % 60 + " seconds";
    }

    private boolean isDonor(Player player) {
        return GamesAPI.isDonorOrHigher(player);
    }
}