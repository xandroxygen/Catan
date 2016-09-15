package client.model;
import java.util.HashMap;
import java.util.List;

import shared.definitions.*;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * Player class
 */
public class Player {
	
	private String name;
	private CatanColor color;
	private int playerIndex;
	private int playerId;
	private boolean developmentCardPlayed;
	private int victoryPoints;
	private HashMap<ResourceType, Integer> resourceHand;
	private HashMap<DevCardType, Integer> playableDevCardHand;
	private HashMap<DevCardType, Integer> unplayableDevCardHand;
	private HashMap<PieceType, Integer> piecesAvailable;
	
	/**
	 * Checks whether the player can place a city.
	 * @pre It's your turn, The city location is where you currently have a settlement, You have the required resources (2 wheat, 3 ore; 1 city)
	 * @post You lost the resources required to build a city (2 wheat, 3 ore; 1 city), The city is on the map at the specified location, You got a settlement back
	 * @param location The location of the city.
	 * @return result
	 */
	public boolean canPlaceCity(VertexLocation location) {
		return false;
	}
	
	/**
	 * Checks whether the player can place a settlement.
	 * @pre It's your turn, The settlement location is open, The settlement location is not on water, The settlement location is connected to one of your roads except during setup, You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement cannot be placed adjacent to another settlement
	 * @post You lost the resources required to build a settlement (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement is on the map at the specified location
	 * @param free Whether or not piece can be built for free.
	 * @param location The location of the settlement.
	 * @return result
	 */
	public boolean canPlaceSettlement(boolean free, VertexLocation location) {
		return false;
	}
	
	/**
	 * Checks whether the player can place a road.
	 * @pre It's your turn, The road location is open, The road location is connected to another road owned by the player, The road location is not on water, You have the required resources (1 wood, 1 brick; 1 road), Setup round: Must be placed by settlement owned by the player with no adjacent road.
	 * @post You lost the resources required to build a road (1 wood, 1 brick - 1 road), The road is on the map at the specified location, If applicable, longest road has been awarded to the player with the longest road
	 * @param free Whether or not piece can be built for free.
	 * @param location The location of the road.
	 * @return result 
	 */
	public boolean canPlaceRoad(boolean free, VertexLocation location) {
		return false;
	}
	
	/**
	 * Checks whether the player can buy a development card.
	 * @pre It's your turn, You have the required resources (1 ore, 1 wheat, 1 sheep), There are dev cards left in the deck.
	 * @post You have a new card; If it is a monument card, it has been added to your old devcard hand, If it is a non­monument card, it has been added to your new devcard hand (unplayable this turn)
	 * @return result
	 */
	public boolean canBuyDevelopmentCard() {
		return false;
	}
	
	/**
	 * Checks whether the player can trade with another player
	 * @pre It's your turn, You have the resources you are offering.
	 * @post The trade is offered to the other player (stored in the server model)
	 * @param offer list of Resources, Negative numbers mean you get those cards
	 * @param recipient the playerIndex of the offer recipient.
	 * @return result
	 */
	public boolean canTradeWithPlayer(HashMap<ResourceType, Integer> offer, int recipient) {
		return false;
	}
	
	/**
	 * Checks whether the player can trade with the maritime.
	 * @pre It's your turn, You have the resources you are giving, For ratios less than 4, you have the correct port for the trade
	 * @post The trade has been executed (the offered resources are in the bank, and the requested resource has been received)
	 * @param ratio It must be a 2, 3, or 4.
	 * @param inputResource Type of resource you are giving.
	 * @param outputResource Type of resource you are receiving.
	 * @return result
	 */
	public boolean canTradeWithMaritime(int ratio, ResourceType inputResource, ResourceType outputResource) {
		return false;
	}
	
	/**
	 * Checks whether the robber can be moved and a player robbed.
	 * @pre The robber is not being kept in the same location, If a player is being robbed (i.e., victimIndex != ­1), the player being robbed has resource cards
	 * @post The robber is in the new location, The player being robbed (if any) gave you one of his resource cards (randomly selected)
	 * @param location New location for robber.
	 * @param playerIndex Index of player to be robbed or -1 if no is to be robbed.
	 * @return result
	 */
	public boolean canRobPlayer(HexLocation location, int playerIndex) {
		return false;
	}
	
	/**
	 * Checks whether the player can roll the dice.
	 * @pre It is your turn, The client model’s status is ‘Rolling’
	 * @post The client model’s status is now in ‘Discarding’ or ‘Robbing’ or ‘Playing’
	 * @return 
	 */
	public boolean canRollDice() {
		return false;
	}
	
	
	/**
	 * Checks whether the player can send a message.
	 * @post  The chat contains your message at the end.
	 * @param message the message the player wishes to send.
	 * @return 
	 */
	public boolean canSendMessage(String message) {
		return false;
	}
	
	/**
	 * Checks whether the player can end the turn.
	 * @post  The cards in your new dev card hand have been transferred to your old dev card hand, It is the next player’s turn
	 * @return result
	 */
	public boolean canEndTurn() {
		return false;
	}
	
}
