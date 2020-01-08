package se.cygni.texasholdem.player;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.texasholdem.client.PlayerClient;
import se.cygni.texasholdem.communication.message.event.PlayIsStartedEvent;
import se.cygni.texasholdem.communication.message.event.TableIsDoneEvent;
import se.cygni.texasholdem.communication.message.request.ActionRequest;
import se.cygni.texasholdem.game.Action;
import se.cygni.texasholdem.game.Room;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PerformanceTestPlayer extends BasicPlayer {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 4711;
    private static final String DEFAULT_NAME = "perftest";
    private static final int DEFAULT_NOOF_PLAYERS = 1;
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final String HOST_PROPERTY = "host";
    private static final String PORT_PROPERTY = "port";
    private static final String NAME_PROPERTY = "name";
    private static final String NOOF_PLAYERS_PROPERTY = "noof";
    private static Logger log = LoggerFactory
            .getLogger(PerformanceTestPlayer.class);
    private static AtomicLong noofNewPlays = new AtomicLong(0);
    private static AtomicLong totalLengthOfPlays = new AtomicLong(0);
    private final String name;
    private PlayerClient playerClient;
    private long lastNewGame;
    private int lastNoofPlayers = 0;

    public PerformanceTestPlayer() {
        name = getSystemProperty(NAME_PROPERTY, DEFAULT_NAME) + "_" + counter.getAndIncrement();
        playerClient = new PlayerClient(this, getSystemProperty(HOST_PROPERTY, DEFAULT_HOST), getSystemProperty(PORT_PROPERTY, DEFAULT_PORT));
    }

    private static String getSystemProperty(String name, String defaultValue) {
        String hostFromSystemProp = System.getProperty(name);
        if (StringUtils.isEmpty(hostFromSystemProp)) {
            return defaultValue;
        }

        return hostFromSystemProp;
    }

    private static int getSystemProperty(String name, int defaultValue) {
        String portFromSystemProp = System.getProperty(name);
        if (StringUtils.isEmpty(portFromSystemProp)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(portFromSystemProp);
        } catch (Exception e) {
            log.warn("Failed to parse integer value from system properties");
        }

        return defaultValue;
    }

    public static void main(String[] args) {

        int noofPlayers = getSystemProperty(NOOF_PLAYERS_PROPERTY, DEFAULT_NOOF_PLAYERS);

        for (int i = 0; i < noofPlayers; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    PerformanceTestPlayer player = new PerformanceTestPlayer();
                    player.playAGame();
                }
            });

            t.start();

            try {
                Thread.sleep(75);
            } catch (Exception w) {
            }
        }

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (noofNewPlays.longValue() < 1) {
                    return;
                }

                log.info("Noof plays: {}, average game bout time (ms) {}", noofNewPlays.longValue(), totalLengthOfPlays.longValue() / noofNewPlays.longValue());

            }
        }, 5000, 1000);
    }

    public void playAGame() {
        try {
            playerClient.connect();
            playerClient.registerForPlay(Room.TRAINING);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public void onPlayIsStarted(PlayIsStartedEvent event) {
        if (lastNewGame > 0) {

            totalLengthOfPlays.addAndGet(System.currentTimeMillis() - lastNewGame);
            lastNewGame = System.currentTimeMillis();
            noofNewPlays.incrementAndGet();
        } else {
            lastNewGame = System.currentTimeMillis();
        }

//        if (playerClient.getCurrentPlayState().getNumberOfPlayers() != lastNoofPlayers) {
//            log.info("{} reports, noof players: {}", this.getName(), playerClient.getCurrentPlayState().getNumberOfPlayers());
//            log.info(StringUtils.join(playerClient.getCurrentPlayState().getPlayers(), ","));
//            lastNoofPlayers = playerClient.getCurrentPlayState().getNumberOfPlayers();
//        }

    }

    @Override
    public Action actionRequired(final ActionRequest request) {

        Action callAction = null;
        Action checkAction = null;
        Action foldAction = null;


        for (final Action action : request.getPossibleActions()) {
            switch (action.getActionType()) {
                case CALL:
                    callAction = action;
                    break;
                case CHECK:
                    checkAction = action;
                    break;
                case FOLD:
                    foldAction = action;
                    break;
                default:
                    break;
            }
        }

        Action action = null;
        if (callAction != null) {
            action = callAction;
        } else if (checkAction != null) {
            action = checkAction;
        } else {
            action = foldAction;
        }

        return action;
    }

    @Override
    public void connectionToGameServerLost() {

        playAGame();
    }

    @Override
    public void onTableIsDone(TableIsDoneEvent event) {

    }
}
