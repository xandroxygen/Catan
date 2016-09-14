package client.server;

import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * Interface for the Server proxy and Mock proxy.
 * Contains all server API methods.
 * Created by Xander on 9/12/2016.
 */
public interface IServerProxy {

	/* START Non-move API */
	
	/**
	 * @param playerID ID to give to the player
	 */
	public void setPlayer(int playerID) {
		
	}
	
	/**
	 * Logs the caller in the server, and sets their catan.user HTTP cookie.
	 *  
	 * @param username Username of the player logging in.
	 * @param password Password that corresponds to the username of player logging in.
	 */
	public void userLogin(String username, String password) {
		
	}
	
	/**
	 * Returns information about all of the current games on the server.
	 */
	public void gamesList() {
		
	}
	
	/**
	 * Creates a new game on the server. 
	 * 
	 * @param name Name of the game
	 * @param randomTiles ??
	 * @param randomNumbers ??
	 * @param randomPorts ??
	 */
	public void gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
		
	}
	
	/**
	 * Adds the player to the specified game and sets their catan.game cookie.
	 * 
	 * @param gameID ID of the game to join
	 * @param color Player color
	 */
	public void gamesJoin(int gameID, CatanColor color) {
		
	}
	
	/**
	 * Returns the current state of the game in JSON format.
	 * 
	 * @param version The version number of the model. Used to compare and check if model has been updated.
	 */
	public void gameGetModel(int version) {
		
	}
	
	/**
	 * Returns a list of supported AI player types.
	 */
	public void gameListAI() {
		
	}
	
	
	/**
	 * Adds an AI player to the current game.
	 * 
	 * @param AI Type ???
	 */
	public void gameAddAI() {
		
	}
	
	/* END Non-move API */
	
	
	/* START Move API */
	
	/**
	 * Sends a chat message to the group.
	 * 
	 * @param content The message to send
	 */
	public void sendChat(String content) {
		
	}
	
	/**
	 *  A domestic trade is being offered.
	 * If the trade is accepted, the two players will swap the specified resources.
	 * If the trade is not accepted, no resources are exchanged and the 
	 * trade offer is removed.
	 * 
	 * @param willAccept Whether or not the player accepts the offered trade
	 */
	public void acceptTrade(boolean willAccept) {
		
	}
	
	/**
	 * 
	 */
	public void discardCards() {
		
	}
	
	public void rollNumber(int number) {
		
	}
	
	public void buildRoad(boolean isFree, EdgeLocation roadLocation ) {
		
	}
	
	public void buildSettlement(boolean isFree, VertexLocaiton vertexLocation) {
		
	}
	
	public void buildCity(VertexLocation vertexLocation) {
		
	}
	
	/**
	 * 
	 * @param offer 
	 * @param receiver The recepient of the trade offer 
	 */
	public void offerTrade(int offer, int playerIndex) {
		
	}
	
	/**
	 * 
	 * @param ratio 
	 * @param inputResource What is given
	 * @param outputResource What is received
	 */
	public void maritimeTrade(int ratio, ResourceType inputResource, ResourceType outputResource) {
		
	}
	
	/**
	 * 
	 * @param location the new robber location
	 * @param victimIndex the index of the player being robbed, or -1 if no one is being robbed
	 */
	public void robPlayer(HexLocation location, int victimIndex) {
		
	}
	
	public void finishTurn() {
		
	}
	
	public void buyDevCard() {
		
	}
	
	public void playSoldier() {
		
	}
	
	/**
	 * 
	 * @param resource1 The first resource you want to receive
	 * @param resource2 The second resource you want to receive
	 */
	public void playYearOfPlenty(ResourceType resource1, ResourceType resource2) {
		
	}
	
	/**
	 * 
	 * @param spot1 
	 * @param spot2
	 */
	public void playRoadBuilding(EdgeLocation spot1, EdgeLocation spot2) {
		
	}
	
	/**
	 * 
	 * @param resource The resource being taken from the other players
	 */
	public void playMonopoly(ResourceType resource) {
		
	}
	
	/**
	 * 
	 */
	public void playVictoryPoint() {
		
	}
	
	
	/* END Move API */
}
