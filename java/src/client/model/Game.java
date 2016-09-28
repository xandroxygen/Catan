package client.model;

import java.util.ArrayList;

import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

/**
 * Game class.
 */
public class Game {

    public ArrayList<Player> playerList;
    public Map theMap;
    public Bank bank;
    public int currentTurnIndex;
    public TurnTracker turnTracker;
    public MessageList log;
    public MessageList chat;
    public String winner;

    /**
     * checks to see if the game can create a new user
     *
     * @pre <pre>
     *      There are less than 4 players
     *      The game has not started
     * 	</pre>
     *
     * @post <pre>
     *      returns true to add a new player
     * </pre>
     *
     * @return
     */
    public boolean canCreateUser(){
        return false;
    }

    /**
     * Authenticates the user
     *
     * @pre <pre>
     *      The player must be an atherized user
     * 	</pre>
     *
     * @post <pre>
     *      returns true to authenticate the user.
     * </pre>
     *
     * @return
     */
    public boolean canAuthenticateUser(){
        return false;
    }
    
    /**
     * Checks whether the player can place a city.
     * @pre It's your turn, The city location is where you currently have a settlement, You have the required resources (2 wheat, 3 ore; 1 city)
     * @post You lost the resources required to build a city (2 wheat, 3 ore; 1 city), The city is on the map at the specified location, You got a settlement back
     * @param playerId the ID of the player who is requesting the move
     * @param location The location of the city.
     * @return result
     */
    boolean canPlaceCity(int playerId, VertexLocation location){
    	Player player = this.getPlayerById(playerId);
    	return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) && 
				theMap.hasSettlementAtLocation(location) && (player.getResourceHand().get("WHEAT") >= 2) && 
				(player.getResourceHand().get("ORE") >= 3) && (player.getResourceHand().get("CITY") >= 1));
    }

    /**
     * Checks whether the player can place a settlement.
     * @pre <pre>
     * 		It's your turn
     * 		The settlement location is open
     * 		The settlement location is not on water
     * 		The settlement location is connected to one of your roads except during setup
     * 		You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement)
     * 		The settlement cannot be placed adjacent to another settlement
     * 		</pre>
     * @post You lost the resources required to build a settlement (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement is on the map at the specified location
     * @param playerId the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the settlement.
     * @return result
     */
    boolean canPlaceSettlement(int playerId, boolean free, VertexLocation location){
    	Player player = this.getPlayerById(playerId);
    	if (free) {
    		return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) && 
    				!theMap.hasSettlementAtLocation(location) && !theMap.hasAdjacentSettlement(location) && 
    				theMap.vertexIsOnPlayerRoad(location, player));
    	}
    	return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) && 
				!theMap.hasSettlementAtLocation(location) && theMap.vertexIsOnPlayerRoad(location, player) && 
				(player.getResourceHand().get("WOOD") >= 1) && (player.getResourceHand().get("BRICK") >= 1) && 
				(player.getResourceHand().get("WHEAT") >= 1) && (player.getResourceHand().get("SHEEP") >= 1) && 
				(player.getResourceHand().get("SETTLEMENT") >= 1));
    }

    /**
     * Checks whether the player can place a road.
     * @pre <pre>
     * 		It's your turn
     * 		The road location is open
     * 		The road location is connected to another road owned by the player
     * 		The road location is not on water
     * 		You have the required resources (1 wood, 1 brick; 1 road)
     * 		Setup round: Must be placed by settlement owned by the player with no adjacent road.
     * 		</pre>
     * @post You lost the resources required to build a road (1 wood, 1 brick - 1 road), The road is on the map at the specified location, If applicable, longest road has been awarded to the player with the longest road
     * @param playerId the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the road.
     * @return result
     */
    boolean canPlaceRoad(int playerId, boolean free, EdgeLocation location) {
    	Player player = this.getPlayerById(playerId);
    	if (free) {
    		return (turnTracker.getCurrentTurn() == player.getPlayerIndex() && 
    				theMap.hasRoadAtLocation(location) && 
    				(theMap.edgeHasPlayerMunicipality(location, player) || theMap.edgeHasAdjacentPlayerRoad(location, player)));
    	}
    	return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) && 
				theMap.hasRoadAtLocation(location) && 
				(theMap.edgeHasPlayerMunicipality(location, player) || theMap.edgeHasAdjacentPlayerRoad(location, player)) &&
				(player.getResourceHand().get("WOOD") >= 1) && 
				(player.getResourceHand().get("BRICK") >= 1) && (player.getPiecesAvailable().get("ROAD") >= 1));
    }
    
    /**
     * Checks whether the player can trade with another player
     * @pre It's your turn, You have the resources you are offering.
     * @post The trade is offered to the other player (stored in the server model)
     * @param offer list of Resources, Negative numbers mean you get those cards
     * @param recieverPlayerId the playerIndex of the offer recipient.
     * @return result
     */
    boolean canTradeWithPlayer(int senderPlayerId, int recieverPlayerId, Map<ResourceType, Integer> offer){
    	Player player = this.getPlayerById(senderPlayerId);
    	return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) && 
				player.hasOfferResources(offer));
    }
    
    boolean canRollDice(int playerId){
    	Player player = this.getPlayerById(playerId);
    	return turnTracker.getCurrentTurn() == player.getPlayerIndex();
    }

    /**
     * Checks whether the player can send a message.
     * @post  The chat contains your message at the end.
     * @param message the message the player wishes to send.
     * @return
     */
    boolean sendMessage(int playerId, String message){
        return false; //ME
    }

    /**
     * Checks whether the player can end the turn.
     * @post  The cards in your new dev card hand have been transferred to your old dev card hand, It is the next player’s turn
     * @return result
     */
    boolean endTurn(int playerId){
        return false; //ME
    }
    
    /**
     * Checks whether the player can get rolled resources.
     * @param playerId ID of player who needs resources
     * @param diceRoll the number that was rolled
     * @return true if there are resources to recieve
     */
    boolean canGetRolledResourses(int playerId, int diceRoll){
        // TODO: If piece is on hex with that number, return true.
    }
    
    // MARK: HELPER METHODS
    private Player getPlayerById(int playerId) {
    	for (Player player : playerList) {
    		if (player.getPlayerId() == playerId) {
    			return player;
    		}
    	}
    	return null;
    }
    
}
