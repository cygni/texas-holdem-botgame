package se.cygni.texasholdem.game;

import java.util.ArrayList;
import java.util.List;

public class BotPlayer {

    private final String name;
    private final String sessionId;
    private final List<Card> cards = new ArrayList<>();
    private long chipAmount;

    public BotPlayer(final String name, final String sessionId,
                     final long chipAmount) {

        this(name, sessionId);
        this.chipAmount = chipAmount;
    }

    public BotPlayer(final String name, final String sessionId) {

        this.name = name;
        this.sessionId = sessionId;
    }

    public void getChips(final long amount) {

        chipAmount -= amount;
    }

    public void addChipAmount(final long amount) {

        chipAmount += amount;
    }

    public long getChipAmount() {

        return chipAmount;
    }

    public void setChipAmount(final long chipAmount) {

        this.chipAmount = chipAmount;
    }

    public String getName() {

        return name;
    }

    public String getSessionId() {

        return sessionId;
    }

    public List<Card> getCards() {

        return new ArrayList<>(cards);
    }

    public void receiveCard(final Card c) {

        cards.add(c);
    }

    public void clearCards() {

        cards.clear();
    }

    @Override
    public String toString() {

        return name + " (" + getChipAmount() + ")";
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((sessionId == null) ? 0 : sessionId.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BotPlayer other = (BotPlayer) obj;
        if (sessionId == null) {
            return other.sessionId == null;
        } else return sessionId.equals(other.sessionId);
    }
}
