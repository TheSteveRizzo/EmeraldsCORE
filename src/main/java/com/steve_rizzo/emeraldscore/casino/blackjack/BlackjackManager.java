package com.steve_rizzo.emeraldscore.casino.blackjack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlackjackManager {
    private static final Map<UUID, BlackjackGame> activeGames = new HashMap<>();

    public static BlackjackGame startGame(UUID uuid, int bet){
        BlackjackGame game = new BlackjackGame(bet);
        activeGames.put(uuid, game);
        return game;
    }

    public static BlackjackGame getGame(UUID uuid){ return activeGames.get(uuid); }

    public static void endGame(UUID uuid){ activeGames.remove(uuid); }
}
