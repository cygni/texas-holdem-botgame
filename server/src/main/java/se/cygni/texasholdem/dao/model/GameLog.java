package se.cygni.texasholdem.dao.model;

import se.cygni.texasholdem.game.Card;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameLog {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<PlayerInGame> players = new ArrayList<PlayerInGame>();
    public long bigBlindValue;
    public long smallBlindValue;
    public List<Card> communityCards = new ArrayList<Card>();
    public int logPosition;
    public long tableCounter;
    public int roundNumber;
    public boolean lastGame;
    public int knownNoofRounds;
    public Date executionDate;

    public List<PlayerInGame> getPlayers() {
        return players;
    }

    public long getBigBlindValue() {
        return bigBlindValue;
    }

    public long getSmallBlindValue() {
        return smallBlindValue;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public List<Card> getFlopCards() {
        return communityCards.subList(0, 3);
    }

    public List<Card> getTurnCards() {
        return communityCards.subList(3, 4);
    }

    public List<Card> getRiverCards() {
        return communityCards.subList(4, 5);
    }

    public int getLogPosition() {
        return logPosition;
    }

    public long getTableCounter() {
        return tableCounter;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public boolean isLastGame() {
        return lastGame;
    }

    public int getKnownNoofRounds() {
        return knownNoofRounds;
    }

    public String getExecutionDate() {
        return sdf.format(executionDate);
    }

    public void setPlayers(List<PlayerInGame> players) {
        this.players = players;
    }

    public void setBigBlindValue(long bigBlindValue) {
        this.bigBlindValue = bigBlindValue;
    }

    public void setSmallBlindValue(long smallBlindValue) {
        this.smallBlindValue = smallBlindValue;
    }

    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards = communityCards;
    }

    public void setLogPosition(int logPosition) {
        this.logPosition = logPosition;
    }

    public void setTableCounter(long tableCounter) {
        this.tableCounter = tableCounter;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setLastGame(boolean lastGame) {
        this.lastGame = lastGame;
    }

    public void setKnownNoofRounds(int knownNoofRounds) {
        this.knownNoofRounds = knownNoofRounds;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }
}
