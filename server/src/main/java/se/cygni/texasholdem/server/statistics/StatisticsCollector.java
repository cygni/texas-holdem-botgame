package se.cygni.texasholdem.server.statistics;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.cygni.texasholdem.dao.model.GameLog;
import se.cygni.texasholdem.dao.model.stats.StatsActions;
import se.cygni.texasholdem.dao.model.stats.StatsChips;
import se.cygni.texasholdem.server.eventbus.RegisterForPlayWrapper;
import se.cygni.texasholdem.server.eventbus.TableDoneEvent;
import se.cygni.texasholdem.util.CircularBuffer;
import se.cygni.texasholdem.util.ObjectMatcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public final class StatisticsCollector {

    private static final int MAX_HISTORY_TABLES = 500;

    protected long startUp = System.currentTimeMillis();

    private AtomicLong noofConnections = new AtomicLong();

    private CircularBuffer<TableHistory> tableHistories;


    public StatisticsCollector() {

        tableHistories = new CircularBuffer<TableHistory>(MAX_HISTORY_TABLES, new Comparator<TableHistory>() {
            @Override
            public int compare(TableHistory first, TableHistory second) {
                return Long.valueOf(first.getTableId()).
                        compareTo(Long.valueOf(second.getTableId()));
            }
        });
    }

    @Autowired
    public void setEventBus(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    public void onRegisterForPlay(final RegisterForPlayWrapper requestWrapper) {
        noofConnections.incrementAndGet();
    }

    @Subscribe
    public void addGameLog(final GameLog gameLog) {

        TableHistory tableHistory = getTableHistory(gameLog.tableCounter);

        if (tableHistory == null) {
            tableHistory = new TableHistory(gameLog.tableCounter);
            tableHistories.add(tableHistory);
        }

        tableHistory.addGameLog(gameLog);
    }

    @Subscribe
    public void tableDone(final TableDoneEvent tableDoneEvent) {
        TableHistory tableHistory = getTableHistory(tableDoneEvent.getTable().getTableCounter());
        if (tableHistory != null) {
            tableHistory.setGameEnded(true);
        }
    }

    public long getTotalNoofConnectionsMade() {
        return noofConnections.longValue();
    }

    public long getUpTime() {
        return System.currentTimeMillis() - startUp;
    }

    public String getUpTimeAsText() {

        PeriodFormatter daysHoursMinutes = new PeriodFormatterBuilder()
                .appendWeeks()
                .appendSuffix(" week", " weeks")
                .appendSeparator(", ")
                .appendDays()
                .appendSuffix(" day", " days")
                .appendSeparator(", ")
                .appendHours()
                .appendSuffix(" hour", " hours")
                .appendSeparator(", ")
                .appendMinutes()
                .appendSuffix(" minute", " minutes")
                .appendSeparator(" and ")
                .appendSeconds()
                .appendSuffix(" second", " seconds")
                .toFormatter();

        Period period = new Period(getUpTime());
        return daysHoursMinutes.print(period.normalizedStandard());
    }

    public List<Long> listTableIds() {
        List<Long> tableIds = new ArrayList<Long>(MAX_HISTORY_TABLES);
        for (TableHistory history : tableHistories.getAll()) {
            tableIds.add(history.tableId);
        }

        return tableIds;
    }

    //    @Cacheable("statistics-actions")
    public StatsActions getStatsActions(final long tableId, final int position) {
        TableHistory tableHistory = getTableHistory(tableId);

        StatsActions sa = new StatsActions();

        if (position < 0 || position > tableHistory.gameLogs.size()) {
            sa.recordsGameLogs(tableHistory.gameLogs);
        } else {
            sa.recordsGameLogs(tableHistory.gameLogs.subList(0, position + 1));
        }

        return sa;
    }

    //    @Cacheable("statistics-chips")
    public StatsChips getStatsChips(final long tableId, final int position) {
        TableHistory tableHistory = getTableHistory(tableId);

        StatsChips sc = new StatsChips();

        if (position < 0 || position > tableHistory.gameLogs.size()) {
            sc.recordGameLogs(tableHistory.gameLogs);
        } else {
            sc.recordGameLogs(tableHistory.gameLogs.subList(0, position + 1));
        }

        return sc;
    }

    public GameLog getLastGameLog() {
        try {
            return getLastGameLog(tableHistories.getLast().tableId);
        } catch (Exception e) {
        }
        return null;
    }

    public GameLog getLastGameLog(final long tableId) {
        try {
            TableHistory th = getTableHistory(tableId);
            GameLog gl = th.getLastGameLog();

            gl.knownNoofRounds = gl.roundNumber;
            gl.lastGame = th.gameEnded;

            return gl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    @Cacheable("gamelog")
    public GameLog getGameLogAtPos(final long tableId, int position) {
        try {
            TableHistory th = getTableHistory(tableId);
            GameLog gl = th.get(position);

            GameLog glLast = th.getLastGameLog();

            gl.knownNoofRounds = glLast.roundNumber;
            gl.lastGame = gl.roundNumber == glLast.roundNumber && th.gameEnded;

            return gl;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getLastGameLog(tableId);
    }

    public int getNoofGameLogs(final long tableId) {
        TableHistory tableHistory = getTableHistory(tableId);
        if (tableHistory == null) {
            return 0;
        }

        return tableHistory.size();
    }

    private TableHistory getTableHistory(final long tableId) {
        return tableHistories.get(new ObjectMatcher<TableHistory>() {
            @Override
            public boolean matches(TableHistory first) {
                return first.getTableId() == tableId;
            }
        });
    }

    private class TableHistory {
        private long tableId;
        private boolean gameEnded;
        private List<GameLog> gameLogs = new ArrayList<GameLog>(150);

        private TableHistory(long tableId) {
            this.tableId = tableId;
        }

        public long getTableId() {
            return tableId;
        }

        public boolean isGameEnded() {
            return gameEnded;
        }

        public void setGameEnded(boolean gameEnded) {
            this.gameEnded = gameEnded;
        }

        public GameLog getFirstGameLog() {
            if (gameLogs.isEmpty()) {
                return null;
            }

            return gameLogs.get(0);
        }

        public GameLog getLastGameLog() {
            if (gameLogs.isEmpty()) {
                return null;
            }

            return gameLogs.get(gameLogs.size() - 1);
        }

        public GameLog get(int position) {
            if (position >= gameLogs.size()) {
                return getLastGameLog();
            }

            return gameLogs.get(position);
        }

        public int size() {
            return gameLogs.size();
        }

        public void addGameLog(GameLog gameLog) {
            gameLogs.add(gameLog);
            gameLog.logPosition = gameLogs.size() - 1;
        }
    }
}
