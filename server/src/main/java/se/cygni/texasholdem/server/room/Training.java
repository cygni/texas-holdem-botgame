package se.cygni.texasholdem.server.room;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.texasholdem.game.BotPlayer;
import se.cygni.texasholdem.server.session.SessionManager;
import se.cygni.texasholdem.table.GamePlan;
import se.cygni.texasholdem.table.Table;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Rooms manage players in tables and starts games when appropriate.
 * A player may be moved to another table when the Room deems necessary.
 */
public class Training extends Room {

    private static Logger log = LoggerFactory
            .getLogger(Training.class);

    BotPlayer player;
    Table table;

    Thread thread;

    public Training(EventBus eventBus, GamePlan gamePlan, SessionManager sessionManager) {
        super(eventBus, gamePlan, sessionManager);
    }

    @Override
    public boolean addPlayer(BotPlayer player) {

        this.player = player;

        table = new Table(gamePlan, this, eventBus, sessionManager);
        table.addPlayer(player);
        table.addPlayer(getWeightedPlayer());
        table.addPlayer(getRaiserPlayer());
        table.addPlayer(getSensiblePlayer());
        table.addPlayer(getCautiousPlayer());
        table.addPlayer(getPhilHellmuthPlayer());

        tables.add(table);

        thread = new Thread(table);
        thread.setName("Training - " + player.getName() + " tableId: " + table.getTableCounter());
        thread.start();

        return true;
    }

    @Override
    public void onTableGameDone(Table table) {


        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                log.info("Training for player: " + player.getName() + " is done");
                eventBus.unregister(this);
                sessionManager.terminateSession(player);
            }
        }, 3, TimeUnit.SECONDS);
    }

    @Override
    public void onPlayerBusted(BotPlayer player) {
    }
}