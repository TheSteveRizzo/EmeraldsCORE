package com.steve_rizzo.emeraldscore.casino.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlackjackGame {
    private final List<String> playerCards = new ArrayList<>();
    private final List<String> dealerCards = new ArrayList<>();
    private final int bet;
    private boolean finished = false;

    private static final Random random = new Random();
    private final List<String> cardDeck = new ArrayList<>();

    private static final String[] CARD_VALUES = {
            "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"
    };

    public BlackjackGame(int bet) {
        this.bet = bet;
        initializeDeck();
        shuffleDeck();

        // initial 2 cards each
        playerCards.add(drawCard());
        playerCards.add(drawCard());
        dealerCards.add(drawCard());
        dealerCards.add(drawCard());
    }

    private void initializeDeck() {
        cardDeck.clear();
        // 4 decks, 4 of each suit per deck
        for (int deck = 0; deck < 4; deck++) {
            for (String value : CARD_VALUES) {
                for (int i = 0; i < 4; i++) { // 4 suits
                    cardDeck.add(value);
                }
            }
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(cardDeck);
    }

    private String drawCard() {
        if (cardDeck.isEmpty()) {
            initializeDeck();
            shuffleDeck();
        }
        return cardDeck.remove(0); // draw top card
    }

    public void hitPlayer() { playerCards.add(drawCard()); }

    public void hitDealer() { dealerCards.add(drawCard()); }

    public List<String> getPlayerCards() { return playerCards; }

    public List<String> getDealerCards() { return dealerCards; }

    public int getBet() { return bet; }

    public int getPlayerTotal() { return calculateBestTotal(playerCards); }

    public int getDealerTotal() { return calculateBestTotal(dealerCards); }

    public boolean isFinished() { return finished; }

    public void setFinished(boolean finished) { this.finished = finished; }

    private int calculateBestTotal(List<String> cards) {
        int total = 0;
        int aces = 0;

        for (String card : cards) {
            switch (card) {
                case "J", "Q", "K" -> total += 10;
                case "A" -> {
                    aces++;
                    total += 11;
                }
                default -> total += Integer.parseInt(card);
            }
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }
}
