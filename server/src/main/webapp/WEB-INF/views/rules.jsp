<div class="container">
    <div class="hero-unit">
        <h1>House Rules</h1>

        <p>and settings</p>
    </div>

    <div class="row">
        <div class="span6">
            <div class="well well-large">
                <h2>House rules</h2>

                <p>
                    A table is played until either a winner has been established, all real players have quit
                    or if a Player shuffle is decided from within a tournament.
                </p>

                <p>
                    A game bout is played till either all players but one has folded or the game has
                    entered the state SHOWDOWN. If the latter the pot is divided according to player hands,
                    bet amounts etc.
                </p>

                <p>
                    A maximum of ${gamePlan.maxNoofTurnsPerState} turns are allowed per player and state.
                    This is to hinder raise races between players. It is always possible to go ALL-IN.
                </p>

                <p>
                    Raises are always fixed to the current value of the big blind.
                </p>

                <p>
                    A bot player that <em>fails to respond in time (3 sec)</em> or responds with non valid actions (i.e.
                    trying to CHECK when a CALL or RAISE is needed) more than ${gamePlan.maxNoofActionRetries}
                    times in a row will automatically be folded in the current game bout.
                </p>

                <p>
                    Player names must be unique, if your name is taken you will not be able to connect and play.
                </p>

                <h2>Training</h2>

                <p>
                    A bot player may train against a few server-bot players that are always alive and eager to play!
                    This is done by joining the room TRAINING upon connection. The table stops playing as soon as
                    the bot player has either won or lost all of its chips.
                </p>

                <h2>Free play</h2>

                <p>
                    If you'd rather play against other bot players, or battle different versions of your own bot
                    join the room FREEPLAY. Whenever 3 or more players are connected to a room in FREEPLAY the
                    game starts (after an extra minute of wait to let latecomers join).
                </p>

                <h2>Tournament</h2>

                <p>
                    When it is time for a tournament all interested players connect and join the room TOURNAMENT.
                    An administrator starts the tournament manually and depending on how many players have joined
                    (max 11 players per table) a suitable amount of tables are started and players are placed.
                </p>

                <p>
                    Whenever a table has 3 or less players left all tables are stopped and the
                    remaining players are shuffled and placed on new tables. This is to even the odds and keep
                    the game balanced.
                </p>

                <p>
                    Upon that time that only 11 players or less are remaining in play all tables are stopped and
                    players are joined on one table. Showdown time!
                </p>

                <p>
                    The ultimate winner of a tournament is the last standing player. The tournament keeps track of
                    when a player becomes bankrupt and uses this timestamp for establishing the total rank between
                    players (i.e the earlier a player is bankrupt the lower the score).
                </p>
            </div>
        </div>
        <!--/span-->

        <div class="span6">
            <div class="well well-large">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th colspan="2"><h2>Game settings</h2></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Starting chip amount</td>
                        <td>
                            <div class="pull-right">$${gamePlan.startingChipsAmount}</div>
                        </td>
                    </tr>
                    <tr>
                        <td>Big blind</td>
                        <td>
                            <div class="pull-right">$${gamePlan.bigBlindStart}</div>
                        </td>
                    </tr>
                    <tr>
                        <td>Small blind</td>
                        <td>
                            <div class="pull-right">$${gamePlan.smallBlindStart}</div>
                        </td>
                    </tr>
                    <tr>
                        <td>Blind raise strategy</td>
                        <td>
                            <div class="pull-right">${gamePlan.blindRaiseStrategy}</div>
                        </td>
                    </tr>
                    <tr>
                        <td>Big blind raise</td>
                        <td>
                            <div class="pull-right">${gamePlan.bigBlindRaiseStrategyValue}</div>
                        </td>
                    </tr>
                    <tr>
                        <td>Small blind raise</td>
                        <td>
                            <div class="pull-right">${gamePlan.smallBlindRaiseStrategyValue}</div>
                        </td>
                    </tr>
                    <tr>
                        <td># rounds between blind raise</td>
                        <td>
                            <div class="pull-right">${gamePlan.playsBetweenBlindRaise}</div>
                        </td>
                    </tr>
                    <tr>
                        <td>Max # turns per state</td>
                        <td>
                            <div class="pull-right">${gamePlan.maxNoofTurnsPerState}</div>
                        </td>
                    </tr>
                    <tr>
                        <td>Max # action retries</td>
                        <td>
                            <div class="pull-right">${gamePlan.maxNoofActionRetries}</div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="well well-large">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th colspan="2"><h2>Poker hands</h2></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td style="white-space:nowrap;">Royal straight flush</td>
                            <td>
                                <div class="pull-right">
                                    <img class="pokercard" src="/resources/img/cards/ACE_SPADES_large.png"     alt="ACE of SPADES" rel="tooltip" data-placement="top" data-original-title="ACE of SPADES">
                                    <img class="pokercard" src="/resources/img/cards/KING_SPADES_large.png"   alt="KING of SPADES" rel="tooltip" data-placement="top" data-original-title="KING of SPADES">
                                    <img class="pokercard" src="/resources/img/cards/QUEEN_SPADES_large.png" alt="QUEEN of SPADES" rel="tooltip" data-placement="top" data-original-title="QUEEN of SPADES">
                                    <img class="pokercard" src="/resources/img/cards/JACK_SPADES_large.png"   alt="JACK of SPADES" rel="tooltip" data-placement="top" data-original-title="JACK of SPADES">
                                    <img class="pokercard" src="/resources/img/cards/TEN_SPADES_large.png"     alt="TEN of SPADES" rel="tooltip" data-placement="top" data-original-title="TEN of SPADES">
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td>Straight flush</td>
                            <td>
                                <div class="pull-right">
                                    <img class="pokercard" src="/resources/img/cards/TEN_DIAMONDS_large.png"     alt="TEN of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="TEN of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/NINE_DIAMONDS_large.png"   alt="NINE of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="NINE of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/EIGHT_DIAMONDS_large.png" alt="EIGHT of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="EIGHT of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/SEVEN_DIAMONDS_large.png"   alt="SEVEN of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="SEVEN of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/SIX_DIAMONDS_large.png"     alt="SIX of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="SIX of DIAMONDS">
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td>Four of a kind</td>
                            <td>
                                <div class="pull-right">
                                    <img class="pokercard" src="/resources/img/cards/TEN_CLUBS_large.png"     alt="TEN of CLUBS" rel="tooltip" data-placement="top" data-original-title="TEN of CLUBS">
                                    <img class="pokercard" src="/resources/img/cards/TEN_HEARTS_large.png"   alt="TEN of HEARTS" rel="tooltip" data-placement="top" data-original-title="TEN of HEARTS">
                                    <img class="pokercard" src="/resources/img/cards/TEN_DIAMONDS_large.png" alt="TEN of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="TEN of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/TEN_SPADES_large.png"   alt="TEN of SPADES" rel="tooltip" data-placement="top" data-original-title="TEN of SPADES">
                                    <img class="pokercard" src="/resources/img/cards/DEUCE_HEARTS_large.png"     alt="DEUCE of HEARTS" rel="tooltip" data-placement="top" data-original-title="DEUCE of HEARTS">
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td>Full House</td>
                            <td>
                                <div class="pull-right">
                                    <img class="pokercard" src="/resources/img/cards/QUEEN_DIAMONDS_large.png"     alt="QUEEN of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="QUEEN of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/QUEEN_CLUBS_large.png"   alt="QUEEN of CLUBS" rel="tooltip" data-placement="top" data-original-title="QUEEN of CLUBS">
                                    <img class="pokercard" src="/resources/img/cards/QUEEN_SPADES_large.png" alt="QUEEN of SPADES" rel="tooltip" data-placement="top" data-original-title="QUEEN of SPADES">
                                    <img class="pokercard" src="/resources/img/cards/SIX_DIAMONDS_large.png"   alt="SIX of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="SIX of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/SIX_SPADES_large.png"     alt="SIX of SPADES" rel="tooltip" data-placement="top" data-original-title="SIX of SPADES">
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td>Flush</td>
                            <td>
                                <div class="pull-right">
                                    <img class="pokercard" src="/resources/img/cards/KING_DIAMONDS_large.png"     alt="KING of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="KING of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/TEN_DIAMONDS_large.png"   alt="TEN of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="TEN of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/EIGHT_DIAMONDS_large.png" alt="EIGHT of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="EIGHT of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/FIVE_DIAMONDS_large.png"   alt="FIVE of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="FIVE of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/DEUCE_DIAMONDS_large.png"     alt="DEUCE of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="DEUCE of DIAMONDS">
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td>Straight</td>
                            <td>
                                <div class="pull-right">
                                    <img class="pokercard" src="/resources/img/cards/TEN_DIAMONDS_large.png"     alt="TEN of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="TEN of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/NINE_SPADES_large.png"   alt="NINE of SPADES" rel="tooltip" data-placement="top" data-original-title="NINE of SPADES">
                                    <img class="pokercard" src="/resources/img/cards/EIGHT_HEARTS_large.png" alt="EIGHT of HEARTS" rel="tooltip" data-placement="top" data-original-title="EIGHT of HEARTS">
                                    <img class="pokercard" src="/resources/img/cards/SEVEN_DIAMONDS_large.png"   alt="SEVEN of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="SEVEN of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/SIX_CLUBS_large.png"     alt="SIX of CLUBS" rel="tooltip" data-placement="top" data-original-title="SIX of CLUBS">
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td>Three of a kind</td>
                            <td>
                                <div class="pull-right">
                                    <img class="pokercard" src="/resources/img/cards/ACE_DIAMONDS_large.png"     alt="ACE of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="ACE of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/ACE_CLUBS_large.png"   alt="ACE of CLUBS" rel="tooltip" data-placement="top" data-original-title="ACE of CLUBS">
                                    <img class="pokercard" src="/resources/img/cards/ACE_SPADES_large.png" alt="ACE of SPADES" rel="tooltip" data-placement="top" data-original-title="ACE of SPADES">
                                    <img class="pokercard" src="/resources/img/cards/TEN_DIAMONDS_large.png"   alt="TEN of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="TEN of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/FIVE_SPADES_large.png"     alt="FIVE of SPADES" rel="tooltip" data-placement="top" data-original-title="FIVE of SPADES">
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td>Two pair</td>
                            <td>
                                <div class="pull-right">
                                    <img class="pokercard" src="/resources/img/cards/ACE_DIAMONDS_large.png"     alt="ACE of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="ACE of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/ACE_CLUBS_large.png"   alt="ACE of CLUBS" rel="tooltip" data-placement="top" data-original-title="ACE of CLUBS">
                                    <img class="pokercard" src="/resources/img/cards/JACK_DIAMONDS_large.png"   alt="JACK of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="JACK of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/JACK_SPADES_large.png"     alt="JACK of SPADES" rel="tooltip" data-placement="top" data-original-title="JACK of SPADES">
                                    <img class="pokercard" src="/resources/img/cards/THREE_SPADES_large.png" alt="THREE of SPADES" rel="tooltip" data-placement="top" data-original-title="THREE of SPADES">
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td>One pair</td>
                            <td>
                                <div class="pull-right">
                                    <img class="pokercard" src="/resources/img/cards/ACE_DIAMONDS_large.png"     alt="ACE of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="ACE of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/ACE_CLUBS_large.png"   alt="ACE of CLUBS" rel="tooltip" data-placement="top" data-original-title="ACE of CLUBS">
                                    <img class="pokercard" src="/resources/img/cards/NINE_DIAMONDS_large.png"   alt="NINE of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="NINE of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/SEVEN_SPADES_large.png"     alt="SEVEN of SPADES" rel="tooltip" data-placement="top" data-original-title="SEVEN of SPADES">
                                    <img class="pokercard" src="/resources/img/cards/THREE_SPADES_large.png" alt="THREE of SPADES" rel="tooltip" data-placement="top" data-original-title="THREE of SPADES">
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td>High card</td>
                            <td>
                                <div class="pull-right">
                                    <img class="pokercard" src="/resources/img/cards/KING_CLUBS_large.png"   alt="KING of CLUBS" rel="tooltip" data-placement="top" data-original-title="KING of CLUBS">
                                    <img class="pokercard" src="/resources/img/cards/JACK_DIAMONDS_large.png"   alt="JACK of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="JACK of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/NINE_HEARTS_large.png"     alt="NINE of HEARTS" rel="tooltip" data-placement="top" data-original-title="NINE of HEARTS">
                                    <img class="pokercard" src="/resources/img/cards/SIX_DIAMONDS_large.png"     alt="SIX of DIAMONDS" rel="tooltip" data-placement="top" data-original-title="SIX of DIAMONDS">
                                    <img class="pokercard" src="/resources/img/cards/THREE_SPADES_large.png" alt="THREE of SPADES" rel="tooltip" data-placement="top" data-original-title="THREE of SPADES">
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <!--/span-->
    </div>

    <!--/span-->
    <!--/row-->
</div>
<!--/.fluid-container-->
