package se.cygni.texasholdem.table;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.texasholdem.communication.message.event.*;
import se.cygni.texasholdem.communication.message.request.ActionRequest;
import se.cygni.texasholdem.communication.message.response.ActionResponse;
import se.cygni.texasholdem.dao.model.GameLog;
import se.cygni.texasholdem.dao.model.NoteworthyEvent;
import se.cygni.texasholdem.game.*;
import se.cygni.texasholdem.game.definitions.PlayState;
import se.cygni.texasholdem.game.definitions.PokerHand;
import se.cygni.texasholdem.game.pot.Pot;
import se.cygni.texasholdem.game.util.GameUtil;
import se.cygni.texasholdem.game.util.PokerHandRankUtil;
import se.cygni.texasholdem.server.eventbus.EventBusUtil;
import se.cygni.texasholdem.server.session.SessionManager;
import se.cygni.texasholdem.util.PlayerTypeConverter;

import java.util.*;
import java.util.Map.Entry;

public class GameRound {

    private static Logger log = LoggerFactory
            .getLogger(GameRound.class);

    private final List<BotPlayer> players = Collections
            .synchronizedList(new ArrayList<BotPlayer>());

    private final long tableId;
    private final long round;

    private final BotPlayer dealerPlayer;
    private final BotPlayer smallBlindPlayer;
    private final BotPlayer bigBlindPlayer;
    private BotPlayer lastPlayerToAct;

    private final long smallBlind;
    private final long bigBlind;

    private final int maxNoofTurnsPerState;
    private final int maxNoofActionRetries;

    private final EventBus eventBus;
    private final SessionManager sessionManager;
    private final List<Card> communityCards = new ArrayList<Card>();
    private final Pot pot;

    private final Map<BotPlayer, Long> payoutResult = new HashMap<BotPlayer, Long>();
    private PokerHandRankUtil rankUtil;

    private GameLog gameLog;


    public GameRound(final long tableId,
                     final long round,
                     final List<BotPlayer> players,
                     final BotPlayer dealerPlayer, final long smallBlind,
                     final long bigBlind,
                     final int maxNoofTurnsPerState,
                     final int maxNoofActionRetries,
                     final EventBus eventBus,
                     final SessionManager sessionManager) {

        pot = new Pot(GameUtil.getActivePlayersWithChipsLeft(players));

        this.tableId = tableId;
        this.round = round;
        this.players.addAll(players);
        this.dealerPlayer = dealerPlayer;

        this.smallBlindPlayer = GameUtil.getNextPlayerInPlay(this.players,
                dealerPlayer, pot);

        this.bigBlindPlayer = GameUtil.getNextPlayerInPlay(this.players,
                smallBlindPlayer, pot);


        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.maxNoofTurnsPerState = maxNoofTurnsPerState;
        this.maxNoofActionRetries = maxNoofActionRetries;
        this.eventBus = eventBus;
        this.sessionManager = sessionManager;
    }

    public void playGameRound() {

        // Shuffle the deck
        final Deck deck = Deck.getShuffledDeck();

        // Notify of start
        EventBusUtil.postPlayIsStarted(eventBus,
                smallBlind, bigBlind,
                dealerPlayer, smallBlindPlayer, bigBlindPlayer, tableId,
                GameUtil.getActivePlayersWithChipsLeft(players), players);

        EventBusUtil.postToEventBus(eventBus, new TableChangedStateEvent(pot.getCurrentPlayState()), players);

        // The OPEN
        dealACardToAllParticipatingPlayers(deck);
        dealACardToAllParticipatingPlayers(deck);

        long smallBlindAmount = pot.bet(smallBlindPlayer, smallBlind);
        EventBusUtil.postPlayerBetSmallBlind(eventBus, smallBlindPlayer, smallBlindAmount, players);
        log.debug("{} placed small blind of {}", smallBlindPlayer.getName(), smallBlindAmount);

        long bigBlindAmount = pot.bet(bigBlindPlayer, bigBlind);
        EventBusUtil.postPlayerBetBigBlind(eventBus, bigBlindPlayer, bigBlindAmount, players);
        log.debug("{} placed big blind of {}", bigBlindPlayer.getName(), bigBlindAmount);

        lastPlayerToAct = bigBlindPlayer;

        // Pre flop
        log.debug("PRE FLOP betting round");
        getBetsTillPotBalanced();

        // Flop
        pot.nextPlayState();
        EventBusUtil.postToEventBus(eventBus, new TableChangedStateEvent(pot.getCurrentPlayState()), players);
        burnAndDealCardsToCommunity(deck, 3);

        // Betting
        log.debug("FLOP betting round");
        getBetsTillPotBalanced();

        // Turn
        pot.nextPlayState();
        EventBusUtil.postToEventBus(eventBus, new TableChangedStateEvent(pot.getCurrentPlayState()), players);
        burnAndDealCardsToCommunity(deck, 1);

        // Betting
        log.debug("TURN betting round");
        getBetsTillPotBalanced();

        // River
        pot.nextPlayState();
        EventBusUtil.postToEventBus(eventBus, new TableChangedStateEvent(pot.getCurrentPlayState()), players);
        burnAndDealCardsToCommunity(deck, 1);

        // Betting
        log.debug("RIVER betting round");
        getBetsTillPotBalanced();

        // Showdown
        pot.nextPlayState();
        EventBusUtil.postToEventBus(eventBus, new TableChangedStateEvent(pot.getCurrentPlayState()), players);

        distributePayback();

        gameLog = GameUtil.createGameLog(
                smallBlind, bigBlind, dealerPlayer, bigBlindPlayer, smallBlindPlayer, players,
                communityCards, pot, payoutResult, rankUtil);

        // Clear cards, prepare for next round
        GameUtil.clearAllCards(players);

        // Log results
        if (log.isDebugEnabled()) {
            log.debug(GameUtil.printTransactions(
                    smallBlind, bigBlind, dealerPlayer, bigBlindPlayer, smallBlindPlayer, players,
                    pot, payoutResult, rankUtil));
        }
    }

    protected void burnAndDealCardsToCommunity(
            final Deck deck,
            final int noofCards) {

        deck.burn();
        for (int i = 0; i < noofCards; i++) {
            final Card card = deck.getNextCard();
            communityCards.add(card);

            EventBusUtil.postToEventBus(eventBus,
                    new CommunityHasBeenDealtACardEvent(
                            card), players);
        }
    }

    protected void dealACardToAllParticipatingPlayers(final Deck deck) {

        final Iterator<BotPlayer> iter = players.iterator();
        while (iter.hasNext()) {
            final BotPlayer player = iter.next();
            if (GameUtil.playerHasChips(player)) {
                final Card card = deck.getNextCard();
                player.receiveCard(card);

                EventBusUtil.postToEventBus(eventBus,
                        new YouHaveBeenDealtACardEvent(card), player);
            }
        }
    }

    protected void getBetsTillPotBalanced() {

        boolean isPreflop = pot.isInPlayState(PlayState.PRE_FLOP);
        if (isPreflop) {
            doInitialBettingRoundPreflop();
        }

        log.debug("Starting normal betting round pot is balanced: {}", pot.isCurrentPlayStateBalanced());

        List<BotPlayer> playersInOrder = isPreflop ? new ArrayList<>() : GameUtil.getOrderedListOfPlayersInPlay(players, dealerPlayer, pot);

        if (!isPreflop && GameUtil.getNoofPlayersWithChipsLeft(playersInOrder) < 2) {
            log.debug("*** Only one player left in gamebout, moving to next state");
            return;
        }

        /*
            Play should continue when:
            - pot is unbalanced
            - whenever not every player has had a chance to play
         */
        while (pot.isCurrentPlayStateUnbalanced() || notAllPlayersHasHadAChanceToPlay(playersInOrder)) {

            BotPlayer currentPlayer = GameUtil.getNextPlayerInPlay(players, lastPlayerToAct, pot);
            log.debug("Current player is {}", currentPlayer);

            final Action action = prepareAndGetActionFromPlayer(currentPlayer, true);

            act(action, currentPlayer);
            playersInOrder.remove(currentPlayer);
        }

        // Reset so that small blind is the first to act after a state change
        lastPlayerToAct = dealerPlayer;
    }

    protected boolean notAllPlayersHasHadAChanceToPlay(List<BotPlayer> playersInOrder) {
        boolean stillPlayersLeft = playersInOrder.size() > 0;
        log.debug("Still {} players left to make an action", playersInOrder.size());
        return stillPlayersLeft;
    }

    protected void doInitialBettingRoundPreflop() {

        log.debug("Initial Betting Round starting");

        // Start with next active player after bigBlindPlayer
        final List<BotPlayer> playerOrder = GameUtil
                .getOrderedListOfPlayersInPlayPreFlop(players, bigBlindPlayer, smallBlindPlayer, bigBlindPlayer, pot);

        for (final BotPlayer player : playerOrder) {
            log.debug("Letting {} act", player);

            final Action action = prepareAndGetActionFromPlayer(player, true);
            act(action, player);
        }
    }

    protected Action prepareAndGetActionFromPlayer(final BotPlayer player, boolean raiseAllowed) {

        final List<Action> possibleActions = GameUtil.getPossibleActions(player, pot, smallBlind, bigBlind, raiseAllowed);

        int counter = 0;

        Action userAction = null;
        while (!GameUtil.isActionValid(player, pot, possibleActions, userAction) && counter < maxNoofActionRetries) {
            userAction = getActionFromPlayer(possibleActions, player);
            counter++;
        }

        if (!GameUtil.isActionValid(player, pot, possibleActions, userAction)) {
            log.info("{} did not reply with a valid action, auto-folding. TableId: {}, round: {}, invalid action: {}", player, tableId, round, userAction);
            userAction = new Action(ActionType.FOLD, 0);
            EventBusUtil.postPlayerForcedFolded(eventBus, player, pot.getTotalBetAmountForPlayer(player));
        }

        lastPlayerToAct = player;
        return userAction;
    }

    protected Action getActionFromPlayer(final List<Action> possibleActions, final BotPlayer player) {
        try {
            final ActionRequest request = new ActionRequest(possibleActions);
            final ActionResponse response = (ActionResponse) sessionManager
                    .sendAndWaitForResponse(player, request);

            return response.getAction();

        } catch (final Exception e) {
            log.info("Player {} failed to respond on table {}, folding for round: {}. {}", player.getName(), tableId, round, e.getMessage());
            pot.fold(player);
            EventBusUtil.postPlayerForcedFolded(eventBus, player, pot.getTotalBetAmountForPlayer(player));
            return new Action(ActionType.FOLD, 0);
        }
    }

    protected void act(final Action action, final BotPlayer player) {

        switch (action.getActionType()) {
            case ALL_IN:
                log.debug("{} went all in with amount {}", player.getName(), player.getChipAmount());
                long chipAmount = player.getChipAmount();
                pot.bet(player, player.getChipAmount());
                EventBusUtil.postPlayerWentAllIn(eventBus, player, chipAmount, players);
                break;

            case CALL:
                log.debug("{} called with amount {}", player.getName(), action.getAmount());
                pot.bet(player, action.getAmount());
                EventBusUtil.postPlayerCalled(eventBus, player, action.getAmount(), players);
                break;

            case CHECK:
                log.debug("{} checked", player.getName());
                EventBusUtil.postPlayerChecked(eventBus, player, players);
                break;

            case FOLD:
                log.debug("{} folded", player.getName());
                pot.fold(player);
                EventBusUtil.postPlayerFolded(eventBus, player, pot.getTotalBetAmountForPlayer(player), players);
                break;

            case RAISE:
                pot.bet(player, action.getAmount());
                if (player.getChipAmount() == 0) {
                    log.debug("{} raised but it became an all in with amount {}", player.getName(), action.getAmount());
                    EventBusUtil.postPlayerWentAllIn(eventBus, player, action.getAmount(), players);
                } else {
                    log.debug("{} raised with amount {}", player.getName(), action.getAmount());
                    EventBusUtil.postPlayerRaised(eventBus, player, action.getAmount(), players);
                }

                break;

            default:
                break;
        }
    }

    protected void distributePayback() {

        rankUtil = new PokerHandRankUtil(
                communityCards, pot.getAllPlayers());

        // Calculate player ranking
        final List<List<BotPlayer>> playerRanking = rankUtil
                .getPlayerRankings();

        final Map<BotPlayer, Long> payout = pot
                .calculatePayout(playerRanking);

        final List<PlayerShowDown> showDowns = new ArrayList<PlayerShowDown>();

        analyseForNoteworthyEvents(rankUtil, payout);

        // Distribute payout
        for (final Entry<BotPlayer, Long> entry : payout.entrySet()) {

            final BotPlayer player = entry.getKey();
            final Long amount = entry.getValue();
            final BestHand bestHand = rankUtil.getBestHand(player);

            // Transfer funds
            log.debug("{} won {}", player, amount);
            player.addChipAmount(amount);

            EventBusUtil.postToEventBus(
                    eventBus,
                    new YouWonAmountEvent(amount, player.getChipAmount()),
                    player);

            final PlayerShowDown psd = new PlayerShowDown(
                    PlayerTypeConverter.fromBotPlayer(player),
                    new Hand(bestHand.getCards(), bestHand.getPokerHand(), pot.hasFolded(player)),
                    amount);
            showDowns.add(psd);
        }

        payoutResult.putAll(payout);

        final ShowDownEvent event = new ShowDownEvent(showDowns);
        EventBusUtil.postToEventBus(eventBus, event, players);


    }

    private final void analyseForNoteworthyEvents(PokerHandRankUtil rankUtil, Map<BotPlayer, Long> payout) {

        long chipsOnPlayers = 0;
        int noofPlayers = 0;
        long potAmount = 0;
        long highestPayout = 0;
        String highestEarner = "";

        for (final Entry<BotPlayer, Long> entry : payout.entrySet()) {
            final BotPlayer player = entry.getKey();
            chipsOnPlayers += player.getChipAmount();
            potAmount += entry.getValue();
            if (entry.getValue() > highestPayout) {
                highestEarner = player.getName();
                highestPayout = entry.getValue();
            }
            noofPlayers++;
        }

        for (final Entry<BotPlayer, Long> entry : payout.entrySet()) {
            final BotPlayer player = entry.getKey();
            final Long amount = entry.getValue();
            final BestHand bestHand = rankUtil.getBestHand(player);

            if (bestHand.getPokerHand().getOrderValue() > 8) {
                String msg = pot.hasFolded(player) ? " could have had a " : " got a ";
                eventBus.post(new NoteworthyEvent(tableId, round, player.getName() + msg + bestHand.getPokerHand().getName() + "!"));
            }

            if (player.getChipAmount() + amount == 0) {
                eventBus.post(new NoteworthyEvent(tableId, round, player.getName() + " is busted by " + highestEarner));
            }

            try {
                long oldChipCount = player.getChipAmount() + pot.getTotalBetAmountForPlayer(player);
                long newChipCount = player.getChipAmount() + amount;
                long actualWinnings = newChipCount - oldChipCount;

                double changeInPercent = (double) newChipCount / (double) oldChipCount;
                if (changeInPercent >= 1.35 && actualWinnings > 5000)
                    eventBus.post(new NoteworthyEvent(tableId, round, player.getName() + " won $" + (newChipCount - oldChipCount)));
            } catch (Exception e) {}
        }
    }

    public void removePlayerFromGame(final BotPlayer player) {

        pot.fold(player);
    }

    protected BotPlayer getDealerPlayer() {

        return dealerPlayer;
    }

    protected BotPlayer getSmallBlindPlayer() {

        return smallBlindPlayer;
    }

    protected BotPlayer getBigBlindPlayer() {

        return bigBlindPlayer;
    }

    public GameLog getGameLog() {
        return gameLog;
    }
}
