package se.cygni.texasholdem.dao.model;

public class NoteworthyEvent {
    private final long tableId;
    private final long gameRound;
    private final String description;
    private final long tstamp;

    public NoteworthyEvent(long tableId, long gameRound, String description) {
        this.tableId = tableId;
        this.gameRound = gameRound;
        this.description = description;
        this.tstamp = System.currentTimeMillis();
    }

    public long getTableId() {
        return tableId;
    }

    public long getGameRound() {
        return gameRound;
    }

    public String getDescription() {
        return description;
    }

    public long getTstamp() {
        return tstamp;
    }
}
