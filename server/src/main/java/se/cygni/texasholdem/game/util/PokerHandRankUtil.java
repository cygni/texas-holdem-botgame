package se.cygni.texasholdem.game.util;

import se.cygni.texasholdem.game.BestHand;
import se.cygni.texasholdem.game.BotPlayer;
import se.cygni.texasholdem.game.Card;
import se.cygni.texasholdem.game.Hand;

import java.util.*;

public class PokerHandRankUtil {

    private final List<Card> communityCards;
    private final List<BotPlayer> players;

    private List<List<BotPlayer>> playerRankings;
    private Map<BotPlayer, BestHand> playerBestHandMap;

    public PokerHandRankUtil(final List<Card> communityCards,
                             final List<BotPlayer> players) {

        this.communityCards = communityCards;
        this.players = players;
        init();
    }

    private void init() {

        playerRankings = new ArrayList<List<BotPlayer>>();
        playerBestHandMap = new HashMap<BotPlayer, BestHand>();

        final List<BestHand> bestHands = new ArrayList<BestHand>();

        for (final BotPlayer player : players) {

            final PokerHandUtil phu = new PokerHandUtil(communityCards,
                    player.getCards());

            final Hand hand = phu.getBestHand();
            final BestHand bestHand = new BestHand(hand.getCards(), hand.getPokerHand(), player);

            bestHands.add(bestHand);
            playerBestHandMap.put(player, bestHand);
        }

        Collections.sort(bestHands);

        BestHand previousBestHand = null;
        for (final BestHand bestHand : bestHands) {

            if (previousBestHand == null ||
                    previousBestHand.compareTo(bestHand) != 0) {

                final List<BotPlayer> currPos = new ArrayList<BotPlayer>();
                currPos.add(bestHand.getPlayer());
                playerRankings.add(currPos);
            } else if (previousBestHand.compareTo(bestHand) == 0) {
                playerRankings.get(playerRankings.size() - 1).add(
                        bestHand.getPlayer());
            }

            previousBestHand = bestHand;
        }

    }

    public List<List<BotPlayer>> getPlayerRankings() {

        return playerRankings;
    }

    public BestHand getBestHand(final BotPlayer player) {

        return playerBestHandMap.get(player);
    }
}
