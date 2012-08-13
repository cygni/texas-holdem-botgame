package se.cygni.texasholdem.server.session;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.codemonkey.swiftsocketserver.ClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.cygni.texasholdem.communication.message.exception.NoRoomSpecifiedException;
import se.cygni.texasholdem.communication.message.exception.UsernameAlreadyTakenException;
import se.cygni.texasholdem.communication.message.request.ActionRequest;
import se.cygni.texasholdem.communication.message.request.RegisterForPlayRequest;
import se.cygni.texasholdem.communication.message.request.TexasRequest;
import se.cygni.texasholdem.communication.message.response.ActionResponse;
import se.cygni.texasholdem.communication.message.response.RegisterForPlayResponse;
import se.cygni.texasholdem.communication.message.response.TexasResponse;
import se.cygni.texasholdem.game.Action;
import se.cygni.texasholdem.game.BotPlayer;
import se.cygni.texasholdem.game.Room;
import se.cygni.texasholdem.game.trainingplayers.TrainingPlayer;
import se.cygni.texasholdem.server.communication.MessageSender;
import se.cygni.texasholdem.server.eventbus.EventWrapper;
import se.cygni.texasholdem.server.eventbus.NewPlayerEvent;
import se.cygni.texasholdem.server.eventbus.PlayerQuitEvent;
import se.cygni.texasholdem.server.eventbus.RegisterForPlayWrapper;
import se.cygni.texasholdem.server.room.Training;
import se.cygni.texasholdem.table.GamePlan;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@Service
public class SessionManagerRemote implements SessionManager {

    private static Logger log = LoggerFactory
            .getLogger(SessionManagerRemote.class);

    private final EventBus eventBus;
    private final GamePlan gamePlan;
    private final MessageSender messageSender;

    private final Map<String, BotPlayer> sessionPlayerMap = new ConcurrentHashMap<String, BotPlayer>();
    private final Map<String, ClientContext> sessionClientContextMap = new ConcurrentHashMap<String, ClientContext>();

    @Autowired
    public SessionManagerRemote(final EventBus eventBus,
            final MessageSender messageSender,
            final GamePlan gamePlan) {

        this.eventBus = eventBus;
        this.messageSender = messageSender;
        this.gamePlan = gamePlan;

        eventBus.register(this);

        initConnectionStatusTimer();
    }

    @Override
    public TexasResponse sendAndWaitForResponse(
            final BotPlayer player,
            final TexasRequest request) {

        if (player instanceof TrainingPlayer && request instanceof ActionRequest) {
            Action action = ((TrainingPlayer) player).actionRequired((ActionRequest)request);
            ActionResponse response = new ActionResponse();
            response.setAction(action);
            return response;
        }

        final ClientContext context = sessionClientContextMap.get(player
                .getSessionId());

        return messageSender.sendAndWaitForResponse(
                context, request);
    }

    @Subscribe
    @Override
    public void notifyPlayerOfEvent(final EventWrapper eventWrapper) {

        // log.debug("Notifying players {} of event: {}",
        // eventWrapper.getReceivers(), eventWrapper.getEvent());

        for (final BotPlayer player : eventWrapper.getReceivers()) {
            if (player instanceof TrainingPlayer) {
                continue;
            }

            final ClientContext context = sessionClientContextMap.get(player
                    .getSessionId());

            if (context == null || !context.isActive()) {
                log.info("Player {} has unexpectedly left the game",
                        player.getName());
                eventBus.post(new PlayerQuitEvent(player));
                continue;
            }

            messageSender.sendMessage(
                    sessionClientContextMap.get(player.getSessionId()),
                    eventWrapper.getEvent());
        }
    }

    @Override
    public void terminateSession(BotPlayer player) {
        final String sessionId = player.getSessionId();

        sessionPlayerMap.remove(sessionId);
        sessionClientContextMap.remove(sessionId);
    }

    @Subscribe
    @Override
    public void onPlayerQuit(final PlayerQuitEvent playerQuitEvent) {

        log.info("Player {} has left the game",
                playerQuitEvent.getPlayer().getName());

        terminateSession(playerQuitEvent.getPlayer());
    }

    @Subscribe
    @Override
    public void onRegisterForPlay(final RegisterForPlayWrapper requestWrapper) {

        final ClientContext clientContext = requestWrapper.getClientContext();
        final RegisterForPlayRequest request = (RegisterForPlayRequest) requestWrapper
                .getRequest();

        // Check that user name is not already in use
        if (!isNameUnique(request.name)) {
            final UsernameAlreadyTakenException e = new UsernameAlreadyTakenException();
            e.message = request.name + " is already used by another player.";
            e.setRequestId(request.getRequestId());
            messageSender.sendMessage(clientContext, e);
            return;
        }

        // Check that a valid Room has been choosen
        if (request.room == null) {
            final NoRoomSpecifiedException e = new NoRoomSpecifiedException();
            e.message = "No valid room was specified in RegisterForPlayRequest.";
            e.setRequestId(request.getRequestId());
            messageSender.sendMessage(clientContext, e);
            return;
        }

        final String sessionId = createSessionId();

        // Create response
        final RegisterForPlayResponse response = new RegisterForPlayResponse();
        response.setRequestId(request.getRequestId());
        response.sessionId = sessionId;

        // Create the bot player
        final BotPlayer player = new BotPlayer(request.name, sessionId,
                gamePlan.getStartingChipsAmount());

        sessionPlayerMap.put(sessionId, player);
        sessionClientContextMap.put(sessionId, clientContext);

        log.debug("New client connection registered. sessionId: {} name: {}",
                sessionId, player.getName());

        // Store the sessionId in the context
        clientContext.getSessionData().put(SESSION_ID, sessionId);

        // Send login response to client
        messageSender.sendMessage(clientContext, response);

        switch (request.room) {
            case TRAINING:
                se.cygni.texasholdem.server.room.Room room = new Training(eventBus, gamePlan, this);
                room.addPlayer(player);
                break;

            default:
                log.info("Player connected with invalid room definition, terminating");
                terminateSession(player);
        }

    }

    @Override
    public int getNoofPlayers() {
        return sessionPlayerMap.values().size();
    }

    @Override
    public List<BotPlayer> listPlayers() {
        List<BotPlayer> players = new ArrayList<BotPlayer>();
        players.addAll(
                sessionPlayerMap.values());

        return players;
    }

    private String createSessionId() {

        return UUID.randomUUID().toString();
    }

    private boolean isNameUnique(final String name) {

        for (final BotPlayer player : sessionPlayerMap.values()) {
            if (StringUtils.equalsIgnoreCase(name, player.getName())) {
                if (!sessionClientContextMap.get(player.getSessionId())
                        .isActive()) {
                    eventBus.post(new PlayerQuitEvent(player));
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    private void initConnectionStatusTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<BotPlayer> disconnectedPlayers = new ArrayList<BotPlayer>();
                for (BotPlayer player : listPlayers()) {
                    if (!sessionClientContextMap.get(player.getSessionId()).isActive()) {
                        log.debug("Found disconnected player: {}", player);
                        disconnectedPlayers.add(player);
                    }
                }

                for (BotPlayer player : disconnectedPlayers)
                    eventBus.post(new PlayerQuitEvent(player));
            }
        }, 5000, 500);
        // delay, repeat every ms
    }
}
