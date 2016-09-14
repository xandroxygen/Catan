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
	 * Assigns a player ID to a player.
	 * 
	 * @param playerID ID to give to the player
	 */
	public void setPlayer(int playerID) {
		
	}
	
	/**
	 * Logs the caller into the server and sets their catan.user HTTP cookie.
	 * 
	 * @pre
	 * PRE-CONDITIONS:
	 * 	username is not null
	 * 	password is not null
	 * 
	 * @post
	 * POST-CONDITIONS:
	 * If username/ password is valid:
	 * 		1. Server returns an HTTP 200 response message
	 * 		2. HTTP response headers set catan.cookie to contain identity of the logged in player.
	 * 
	 * If username/ password is not valid:
	 * 		1. Server returns 400 error response and body contains an error message.
	 *  
	 * @param username Username of the player logging in.
	 * @param password Password that corresponds to the username of player logging in.
	 */
	public void userLogin(String username, String password) {
		
	}
	
	/**
	 * Returns information about all of the current games on the server.
	 * 
	 * @pre
	 * PRE-CONDITIONS: None
	 * 
	 * @post
	 * POST-CONDITIONS:	
	 * 	If the operation succeeds:
	 * 		1. Server returns an HTTP 200 reponse message
	 * 		2. The body contains a JSON array with a list of objects that contain info about the server's games.
	 * 
	 * 	If the operation fails:
	 * 		1. Server returns 400 error response and body contains an error message.
	 */
	public void gamesList() {
		
	}
	
	/**
	 * Creates a new game on the server. 
	 * 
	 * @pre
	 * name is not null
	 * randomTiles, randomNumbers, randomPorts contain valid boolean values
	 * 
	 * @post
	 * POST-CONDITIONS:
	 * If the operation succeeds:
	 * 		1. The server returns an HTTP 200 response message
	 * 		2. The body contains a JSON object describing the newly created game
	 * 
	 * If the operation fails:
	 * 		1. Server returns 400 error response and body contains an error message. 
	 * 
	 * @param name Name of the game
	 * @param randomTiles true if the tiles should be randomized, false if they should be preset
	 * @param randomNumbers true if the numbers should be randomized, false if they should be preset
	 * @param randomPorts true if the ports should be randomized, false if they should be preset
	 */
	public void gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
		
	}
	
	/**
	 * Adds the player to the specified game and sets their catan.game cookie.
	 * 
	 * @pre
	 * PRE-CONDITIONS:
	 * 	1. The user has previously logged into the server (they have a valid catan.user HTTP cookie)
	 * 	2. The player may join the game because
	 * 		a. They are already in the game -OR-
	 * 		b. There is space in the game to add a new player
	 * 	3. The specified game is valid
	 * 	4. The specified color is valid
	 * 
	 * @post
	 * POST-CONDITIONS:
	 * 	If the operation succeeds:
	 * 		1. The server returns an HTTP 200 success response
	 * 		2. The player is in the game with the specified color
	 * 		3. The server response includes the "set-cookie" response header setting the catan.game HTTP cookie
	 * 
	 * 	If the operation fails:
	 * 		1. Server returns 400 error response and body contains an error message.
	 * 
	 * @param gameID ID of the game to join
	 * @param color Player color
	 */
	public void gamesJoin(int gameID, CatanColor color) {
		
	}
	
	/**
	 * Returns the current state of the game in JSON format.
	 * 
	 * @pre
	 * PRE-CONDITIONS:
	 * 	1. The caller has previously logged into the server and joined a game (they have valid catan.game and catan.user HTTP cookies)
	 * 
	 * @post
	 * POST-CONDITIONS:
	 * 	If the operation succeeds:
	 * 		1. Server returns HTTP 200 response message
	 * 		2. The response body contains the full client model JSON 
	 * 
	 *  If the operation fails:
	 *  	1. The server returns an HTTP 400 error message and the response body contains an error message
	 */
	public void gameGetModel() {
		
	}
	
	/**
	 * Returns the current state of the game in JSON format.
	 * 
	 * @pre
	 * PRE-CONDITIONS:
	 * 	1. The caller has previously logged into the server and joined a game (they have valid catan.game and catan.user HTTP cookies)
	 * 	2. If specified, the version number is included as the "version" query parameter in the request URL, 
	 * 		and its value is a valid integer.
	 * 
	 * @post
	 * POST-CONDITIONS:
	 * 	If the operation succeeds:
	 * 		1. Server returns HTTP 200 response message
	 * 		2. The response body contains JSON data
	 * 			a. The full client model JSON is returned if the client does not provide a client number, 
	 * 				or the provided version number does not match the version on the server
	 * 			b. "true" is returned if the caller provided a version number and it matched the version number on the server
	 * 
	 *  If the operation fails:
	 *  	1. The server returns an HTTP 400 error message and the response body contains an error message
	 * 
	 * @param version The version number of the model. Used to compare and check if model has been updated.
	 */
	public void gameGetModel(int version) {
		
	}
	
	/**
	 * Returns a list of supported AI player types.
	 * 
	 * @pre
	 * PRE-CONDITIONS: None
	 * 
	 * @post
	 * POST-CONDITIONS: 
	 * 	If the operation succeeds
	 * 		1. The server returns an HTTP 200 response message
	 * 		2. The body contains a JSON string array enumerating the different types of AI players.
	 * 		   These are values that may be passed to the gameAddAI method	
	 * 
	 *  If the operation fails:
	 *  	1. The server returns an HTTP 400 error message and the response body contains an error message
	 */
	public void gameListAI() {
		
	}
	
	/**
	 * Adds an AI player to the current game.
	 * 
	 * @pre
	 * PRE-CONDITIONS:
	 * 	1. The caller has previously logged in to the server and joined a game (they have valid catan.user and catan.game HTTP cookies).
	 * 	2. There is space in the game for another player (the game is not “full”).
	 * 	3. The specified “AIType” is valid (one of the values returned by the /game/listAI method).
	 * 
	 * @post
	 * POST-CONDITIONS:
	 * If the operation succeeds:
	 * 	1. The server returns an HTTP 200 success response with “Success” in the body.
	 * 	2. A new AI player of the specified type has been added to the current game. 
	 * 	   The server selected a name and color for the player.
	 * 
	 * If the operation fails:
	 * 	1. The server returns an HTTP 400 error response, and the body contains an error message.
	 * 
	 * @param aiTypes The AI player to add to the game
	 */
	public void gameAddAI(String aiType) {
		
	}
	
	/* END Non-move API */
	
	
	/* START Move API */

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
