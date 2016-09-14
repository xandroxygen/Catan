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
	public void setPlayer(int playerID);
	
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
	public void userLogin(String username, String password);
	
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
	public void gamesList();
	
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
	public void gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts);
	
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
	public void gamesJoin(int gameID, CatanColor color);
	
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
	public void gameGetModel();

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
	public void gameGetModel(int version);
	
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
	public void gameListAI();
	
	
	/**
	 * Adds an AI player to the current game.
	 * 
	 * @pre
	 * PRE-CONDITIONS:
	 * 	1. The caller has previously logged in to the server and joined a game (they have valid catan.user and catan.game HTTP cookies).
	 * 	2. There is space in the game for another player (the game is not �full�).
	 * 	3. The specified �AIType� is valid (one of the values returned by the /game/listAI method).
	 *
	 * @post
	 * POST-CONDITIONS:
	 * If the operation succeeds:
	 * 	1. The server returns an HTTP 200 success response with �Success� in the body.
	 * 	2. A new AI player of the specified type has been added to the current game.
	 * 	   The server selected a name and color for the player.
	 *
	 * If the operation fails:
	 * 	1. The server returns an HTTP 400 error response, and the body contains an error message.
	 *
	 * @param aiType The AI player to add to the game
	 */
	public void gameAddAI(String aiType);

	/* END Non-move API */
	
	
	/* START Move API */
	/*
	    Universal pre-conditions:
	    - player is logged in
	    - player has joined a game
	 */

	/**
	 * Sends a chat message to the group.
	 * 
	 * @param content The message to send
     * @post the chat box contains the sent message
	 */
	public void sendChat(String content);
	
	/**
	 *  A domestic trade is being offered.
	 * If the trade is accepted, the two players will swap the specified resources.
	 * If the trade is not accepted, no resources are exchanged and the 
	 * trade offer is removed.
	 * 
	 * @param willAccept Whether or not the player accepts the offered trade
     * @pre You have been offered a tag
     * @pre You have the required resources to accept the tag
     * @post If you accepted, you swap resources with the offering player
     * @post If you declined, no resources are swapped
     * @post The trade offer is removed
	 */
	public void acceptTrade(boolean willAccept);

	/**
     * @param hand The cards being discarded
	 * @pre The status of the client model is 'Discarding'
     * @pre You have > 7 cards
     * @pre You have the resources you are discarding
     * TODO: Replace Object with correct class
	 */
	public void discardCards(Object hand);

    /**
     * Roll the dice.
     * @param number the number that was rolled, in the range 2-12
     * @pre It is your turn
     * @pre The status of the client model is 'Rolling'
     * TODO: will the actual rolling be done in the controller or here?
     */
	public void rollNumber(int number);

    /**
     * @param isFree during the setup phase, roads are free
     * @param roadLocation the new road's location.
     * @pre It is your turn
     * @pre The status of the client model is 'Playing'
     * @pre The road location is open
     * @pre The road location is connected to your road
     * @pre The road location is not on water
     * @pre You have the required resources
     * @pre During setup, must be placed by settlement without adjacent road.
     * @post You have spent the required resources
     * @post The road is at the location on the map
     * @post Longest road has been awarded, if applicable
     */
	public void buildRoad(boolean isFree, EdgeLocation roadLocation );

    /**
     * @param isFree during the setup phase, settlements are free
     * @param vertexLocation the new settlement location
     * @pre It is your turn
     * @pre The status of the client model is 'Playing'
     * @pre The settlement location is open
     * @pre The settlement location is connected to your road, except during setup
     * @pre The settlement location is not on water
     * @pre You have the required resources
     * @post You have spent the required resources
     * @post The settlement is at the location on the map
     */
	public void buildSettlement(boolean isFree, VertexLocation vertexLocation);

    /**
     * @param vertexLocation the new city's location
     * @pre It is your turn
     * @pre The status of the client model is 'Playing'
     * @pre There is a settlement at the city location
     * @pre You have the required resources
     * @post You have spent the required resources
     * @post The settlement is at the location on the map
     * @post You regain 1 settlement
     */
	public void buildCity(VertexLocation vertexLocation);

	/**
	 * Contact another player and offer to trade cards back and forth.
	 * @param offer Cards you are offering - negative numbers means you receive those cards, positive means you give
	 * @param receiverIndex The index of the recipient of the trade offer
     * @pre You have the resources you are offering
     * @post The trade is offered to the other player
	 */
	public void offerTrade(Object offer, int receiverIndex);
	
	/**
	 * Used when built on a port, or when trading to the bank.
	 * @param ratio 4 (to 1, when not on a port), 3 (to 1, when on a general port), 2 (to 1, when on a resource port)
	 * @param inputResource What is given
	 * @param outputResource What is received
     * @pre You have the required resources
     * @pre If ratio is < 4, you are built on the correct port
     * @post The offered resources are in the bank
     * @post You have the requested resource.
	 */
	public void maritimeTrade(int ratio, ResourceType inputResource, ResourceType outputResource);
	
	/**
	 * Called when a 7 is rolled and the robber is being moved.
	 * @param location the new robber location
	 * @param victimIndex the index of the player being robbed, or -1 if no one is being robbed
     * @pre The robber is not being kept in the same spot.
     * @pre The player being robbed has resource cards.
     * @post The robber is in the new location
     * @post The player being robbed gave you a random resource card.
	 */
	public void robPlayer(HexLocation location, int victimIndex);

    /**
     * Called at the end of a player's turn.
     * @post The cards in your new dev card hand have been moved to your old dev card hand.
     * @post It is the next player's turn.
     */
	public void finishTurn();

    /**
     * Purchase a development card for 1 wheat, 1 sheep, and 1 ore.
     * @pre You have the required resources
     * @pre There are dev cards left to buy
     * @post You have a new dev card
     * @post Monument cards are available immediately (in your old dev card hand)
     * @post Other dev cards are unplayable this turn (in your new dev card hand)
     * @post You have spent the required resources
     */
	public void buyDevCard();

    /**
     * Play a soldier/knight dev card. Analogous to moving the robber.
     * @param location The location to move the robber to.
     * @param victimIndex The player to rob, or -1 if there are none.
     * @pre It is your turn.
     * @pre The status of the client model is 'Playing'
     * @pre You have a soldier card in your old dev card hand
     * @pre You have not yet played a non-monument dev card this turn
     * @pre The robber is not being kept in the same location
     * @pre The player being robbed has resource cards
     * @post The robber has been moved to the new location
     * @post The player being robbed gave you a random resource card
     * @post Largest army has been awarded, if applicable
     * @post You are not allowed to play non-monument dev cards
     */
	public void playSoldier(HexLocation location, int victimIndex);

	/**
	 * Play a Year of Plenty card, and receive 2 free resources.
	 * @param resource1 The first resource you want to receive
	 * @param resource2 The second resource you want to receive
     * @pre It is your turn.
     * @pre The status of the client model is 'Playing'
     * @pre You have a Year of Plenty card in your old dev card hand
     * @pre You have not yet played a non-monument dev card this turn
     * @pre The requested resources are in the bank.
     * @post You have the requested resources and the bank does not.
	 */
	public void playYearOfPlenty(ResourceType resource1, ResourceType resource2);
	
	/**
	 * Play a Road Building card, and build 2 roads.
	 * @param spot1 
	 * @param spot2
     * @pre It is your turn.
     * @pre The status of the client model is 'Playing'
     * @pre You have a Road Building card in your old dev card hand
     * @pre You have not yet played a non-monument dev card this turn
     * @pre The first road location is connected to one of your roads
     * @pre The second road location is connected to one of your roads, or to the first road
     * @pre Neither road location is on water
     * @pre You have 2+ unused roads.
     * @post You have 2 fewer unused roads.
     * @post 2 new roads are placed on the map.
     * @post Longest road is awarded, if applicable.
	 */
	public void playRoadBuilding(EdgeLocation spot1, EdgeLocation spot2);
	
	/**
	 * Play a Monopoly card, and collect a specific resource from all other players.
	 * @param resource The resource being taken from the other players
     * @pre It is your turn.
     * @pre The status of the client model is 'Playing'
     * @pre You have a Monopoly card in your old dev card hand
     * @pre You have not yet played a non-monument dev card this turn
     * @post All the players have given you all of their resources of the specified type
	 */
	public void playMonopoly(ResourceType resource);
	
	/**
	 * Play a Monument card, and be awarded a victory point.
     * @pre You have enough monument cards to reach 10 pts and win the game.
     * @post You gained a victory point.
	 */
	public void playVictoryPoint();

	/* END Move API */
}
