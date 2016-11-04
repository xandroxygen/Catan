package server.model;

import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.Game;

import java.util.HashMap;

/**
 * Created by Jonathan Skaggs on 11/4/2016.
 */
public class ServerGame extends Game {
    /**
     * Places a City in the Game from the given gameID for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The city location is where you currently have a settlement, You have the required resources (2 wheat, 3 ore; 1 city)
     * @post You lost the resources required to build a city (2 wheat, 3 ore, 1 city), The city is on the map at the specified location, You got a settlement back on the desired location
     * @param playerID the ID of the player who is requesting the move.
     * @param location The location of the city.
     * @return result
     */
    public void placeCity(int playerID, VertexLocation location){}

    /**
     * Places a Settlement in the Game from the given gameID for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The settlement location is open, The settlement location is not on water, The settlement location is connected to one of your roads except during setup, You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement cannot be placed adjacent to another settlement
     * @post You lost the resources required to build a settlement (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement is on the map at the specified location
     * @param playerID the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the settlement.
     * @return result
     */
    public void placeSettlement(int playerID, boolean free, VertexLocation location){}

    /**
     * Places a Road in the Game from the given gameID for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The road location is open, The road location is connected to another road owned by the player, The road location is not on water, You have the required resources (1 wood, 1 brick; 1 road), Setup round: Must be placed by settlement owned by the player with no adjacent road.
     * @post You lost the resources required to build a road (1 wood, 1 brick - 1 road), The road is on the map at the specified location, If applicable, longest road has been awarded to the player with the longest road
     * @param playerID the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the road.
     * @return result
     */
    public void placeRoad(int playerID, boolean free, EdgeLocation location){}

    /**
     * Buys a development card if the given player.
     * @pre It's your turn, You have the required resources (1 ore, 1 wheat, 1 sheep), There are dev cards left in the deck.
     * @post You have a new card; If it is a monument card, it has been added to your old devCard hand, If it is a non­monument card, it has been added to your new devCard hand (unplayable this turn)
     * @param playerID the ID of the player who is requesting the move
     * @return result
     */
    public void buyDevelopmentCard(int playerID){}

    /**
     * Places a soldier development card and moves the robber into the given location and robs the victim player if there is one
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		The robber is not being kept in the same location
     * 		If a player is being robbed (i.e., victimIndex != ­1)
     * 		The player being robbed has resource cards
     * 		</pre>
     * @post <pre>
     * 		The robber is in the new location
     * 		The player being robbed (if any) gave you one of his resource cards (randomly selected)
     * 		If applicable, largest army has been awarded to the player who has played the most soldier cards
     * 		You are not allowed to play other development cards during this turn (except for monument cards, which may still be played)
     * 	    Can not play another DevCard with the exception of the Monument Card
     * 		</pre>
     * @param playerID the ID of the player who is requesting the move
     * @param location the new robber location.
     * @param victimIndex The playerIndex of the player you wish to rob, or -1 to rob no one.
     * @return result
     */
    public void playSoldierCard(int playerID, HexLocation location, int victimIndex){}

    /**
     * Plays a year of plenty devCard.
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		Two specified resources are in the bank.
     * 		</pre>
     * @post <pre>
     * 		You gained the two specified resources
     * 	    Can not play another DevCard with the exception of the Monument Card
     * 		</pre>
     * @param playerID the ID of the player who is requesting the move
     * @param resource1 The type of the first resource you'd like to receive
     * @param resource2 The type of the second resource you'd like to receive
     * @return result
     */
    public void playYearOfPleanty(int playerID, ResourceType resource1, ResourceType resource2){}

    /**
     * Plays a road card.
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		The first road location (spot1) is connected to one of your roads.
     * 		The second road location (spot2) is connected to one of your roads or to the first road location (spot1)
     * 		Neither road location is on water
     * 		You have at least two unused roads
     * 		</pre>
     * @post <pre>
     * 		You have two fewer unused roads
     * 		Two new roads appear on the map at the specified locations
     * 		If applicable, longest road has been awarded to the player with the longest road
     * 	    Can not play another DevCard with the exception of the Monument Card
     * 		</pre>
     * @param playerID the ID of the player who is requesting the move
     * @param spot1 first edge location of road.
     * @param spot2 second edge location of road.
     * @return result
     */
    public void playRoadCard(int playerID, EdgeLocation spot1, EdgeLocation spot2){}

    /**
     * Plays a monopoly devCard.
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		</pre>
     * @post <pre>
     * 		All of the other players have given you all of their resource cards of the specified type
     * 	    Can not play another DevCard with the exception of the Monument Card
     * 		</pre>
     * @param playerID the ID of the player who is requesting the move
     * @param resource The type of resource desired from other players.
     * @return result
     */
    public void playMonopolyCard(int playerID, ResourceType resource){}

    /**
     * Plays a monument devCard.
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		You have enough monument cards to win the game (i.e., reach 10 victory points)
     * 		</pre>
     * @post <pre>
     * 		You gained a victory point
     * 		</pre>
     * @param playerID the ID of the player who is requesting the move
     * @return result
     */
    public void playMonumentCard(int playerID){}

    /**
     * Checks whether the player can roll the dice
     * @pre It's their turn, and the model status is ROLLING
     * @post distributes the correct amount of resources corresponding to the roll for every player
     * @param playerID the ID of the player who is requesting the move
     * @param rollValue the value that was rolled
     * @return
     */
    public void rollDice(int playerID,  int rollValue){}

    /**
     * Sends a chat message.
     * @post  The chat contains your message at the end.
     * @param playerID the ID of the player who is requesting the move
     * @param message the message the player wishes to send.
     */
    public void sendMessage(int playerID, String message){}

    /**
     * Make a trade offer to another player.
     * @param senderPlayerID Player offering the trade
     * @param receiverPlayerID Player being offered the trade
     * @param offer hand of cards to trade
     */
    public void makeTradeOffer(int senderPlayerID, int receiverPlayerID, HashMap<ResourceType, Integer> offer){}

    /**
     * Accept the TradeOffer currently on the table.
     */
    public void acceptTradeOffer(){}

    /**
     * Accept the TradeOffer currently on the table.
     * @param playerID the ID of the player who is requesting the move
     */
    public void makeMaritimeTrade(int playerID){}

    /**
     * Adds a player to the game.
     * @param playerID the ID of the player who is requesting the move
     */
    public void addPlayer(int playerID){}

    /**
     * Adds an computer player to the game.
     * * * @pre <pre>
     * 		There are less then four players in the current game
     * 		</pre>
     * * @post <pre>
     *      there is a new computer player added to the game given by the gameID
     * 		</pre>
     */
    public void addComputerPlayer(){
        //This method needs to check to see if there are four players before execution
    }

    /**
     * Ends the current players turn
     * * * @pre <pre>
     *     is called by the player whose turn it is
     *     the player is in the playing state
     * 		</pre>
     * * @post <pre>
     *     the current players turn is over and the next players is starting the game
     * 		</pre>
     */
    public void finishTurn(){}

    /**
     * Robs a player
     * * * @pre <pre>
     *
     * 		</pre>
     * * @post <pre>
     *      Robs 1 resource from the victim player
     * 		</pre>
     * @param playerID the ID of the player who is requesting the move
     * @param victimIndex .
     */
    public void robPlayer(int playerID, int victimIndex){}

    /**
     * Discards the given resources from the given player
     * * * @pre <pre>
     *      The player has sufficient resources to be able to discard
     * 		</pre>
     * * @post <pre>
     *      the given resources are discarded
     * 		</pre>
     * @param playerID the ID of the player who is requesting the move
     */
    public void discardCards(int playerID, HashMap<ResourceType, Integer> discardCards){

    }

    /**
     * Lists out all types of AI Players for a particular game
     * * @post <pre>
     *      If there are AI players it will return an array of their corresponding types
     * 		</pre>
     */
    public String[] listAIPlayers(int gameID){
        return null;
    }

    /**
     * Gives the longestRoad Card to the appropriate player.
     * * * @pre <pre>
     * 		There are less then four players in the current game
     * 		</pre>
     * * @post <pre>
     *      there is a new computer player added to the game given by the gameID
     * 		</pre>
     */
    public void longestRoad(){}

    /**
     * Gives the longestRoad Card to the appropriate player.
     * * * @pre <pre>
     * 		There are less then four players in the current game
     * 		</pre>
     * * @post <pre>
     *      there is a new computer player added to the game given by the gameID
     * 		</pre>
     */
    public void largestArmy(){}
}
