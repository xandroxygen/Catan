package client.server;

import client.model.InvalidActionException;
import org.json.simple.JSONObject;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import java.util.Map;

/**
 * Mock server used only for testing.
 * Instead of calling API, returns hard-coded results for local testing.
 * No special parameters needed, just call the methods.
 * TODO: will this mock server need to respond to calls with an updated model?
 * Created by Xander on 9/13/2016.
 */
public class MockServerProxy implements IServerProxy {
	
	/**
     * Instructs the server communicator which player's turn it is.
     * The communicator uses the player's specific cookie.
     *
     * @param playerID ID to give to the player
     */
    @Override
    public void setPlayer(int playerID) {

    }

    /**
     * Logs the caller into the server and sets their catan.user HTTP cookie.
     *
     * @param username Username of the player logging in.
     * @param password Password that corresponds to the username of player logging in.
     * @pre <pre>
     * 	username is not null
     * 	password is not null
     * 	</pre>
     * @post <pre>
     * If username/ password is valid:
     * 		1. Server returns an HTTP 200 response message
     * 		2. HTTP response headers set catan.cookie to contain identity of the logged in player.
     *
     * If username/ password is not valid:
     *  	1. Server returns 400 error response and body contains an error message.
     *  </pre>
     */
    @Override
    public void userLogin(String username, String password) {

    }
    
    /**
	 * Creates a new user account & logs the caller into the server as the new user and sets
	 * their catan.user HTTP cookie.
	 *
	 * @pre <pre>
	 * 	username is not null
	 * 	password is not null
	 *  username has not already been taken
	 * 	</pre>
	 * 
	 * @post <pre>
	 * If username/ password is valid:
	 * 		1. Server returns an HTTP 200 response message.
	 * 		2. A new user account is created with the specified username and password.
	 * 		3. HTTP response headers set catan.cookie to contain identity of the logged in player.
	 *
	 * If username/ password is not valid:
	 *  	1. Server returns 400 error response and body contains an error message.
	 *  </pre>
	 * 
	 * @param username Username of the new player being registered.
	 * @param password Password that corresponds to the username of new player being registered.
	 */
    @Override
    public void userRegister(String username, String password) {
    	
    }

    /**
     * Returns information about all of the current games on the server.
     *
     * @post <pre>
     * 	If the operation succeeds:
     * 		1. Server returns an HTTP 200 reponse message
     * 		2. The body contains a JSON array with a list of objects that contain info about the server's games.
     *
     * 	If the operation fails:
     * 		1. Server returns 400 error response and body contains an error message.
     * 	</pre>
     */
    @Override
    public String gamesList() {
    	return null;
    }

    /**
     * Creates a new game on the server.
     *
     * @param name          Name of the game
     * @param randomTiles   true if the tiles should be randomized, false if they should be preset
     * @param randomNumbers true if the numbers should be randomized, false if they should be preset
     * @param randomPorts   true if the ports should be randomized, false if they should be preset
     * @pre <pre>
     * 		1. name is not null
     * 		2. randomTiles, randomNumbers, randomPorts contain valid boolean values
     * </pre>
     * @post <pre>
     * If the operation succeeds:
     * 		1. The server returns an HTTP 200 response message
     * 		2. The body contains a JSON object describing the newly created game
     *
     * If the operation fails:
     * 		1. Server returns 400 error response and body contains an error message.
     * 	</pre>
     */
    @Override
    public String gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
    	return null;
    }

    /**
     * Adds the player to the specified game and sets their catan.game cookie.
     *
     * @param gameID ID of the game to join
     * @param color  Player color
     * @pre <pre>
     * 	1. The user has previously logged into the server (they have a valid catan.user HTTP cookie)
     * 	2. The player may join the game because
     * 		a. They are already in the game -OR-
     * 		b. There is space in the game to add a new player
     * 	3. The specified game is valid
     * 	4. The specified color is valid
     * 	</pre>
     * @post <pre>
     * 	If the operation succeeds:
     * 		1. The server returns an HTTP 200 success response
     * 		2. The player is in the game with the specified color
     * 		3. The server response includes the \"set-cookie\" response header setting the catan.game HTTP cookie
     *
     * 	If the operation fails:
     * 		1. Server returns 400 error response and body contains an error message.
     * </pre>
     */
    @Override
    public void gamesJoin(int gameID, CatanColor color) {

    }

    /**
     * Returns the current state of the game in JSON format.
     *
     * @pre <pre>
     * 	1. The caller has previously logged into the server and joined a game (they have valid catan.game and catan.user HTTP cookies)
     * </pre>
     * @post <pre>
     * 	If the operation succeeds:
     * 		1. Server returns HTTP 200 response message
     * 		2. The response body contains the full client model JSON
     *
     *
     *  If the operation fails:
     * 		1. The server returns an HTTP 400 error message and the response body contains an error message
     * </pre>
     */
    @Override
    public String gameGetModel() {

        return null;
    }

    /**
     * Returns the current state of the game in JSON format.
     *
     * @param version The version number of the model. Used to compare and check if model has been updated.
     * @pre <pre>
     * 	1. The caller has previously logged into the server and joined a game (they have valid catan.game and catan.user HTTP cookies)
     * 	2. If specified, the version number is included as the \"version\" query parameter in the request URL,
     * 		and its value is a valid integer.
     *  </pre>
     * @post <pre>
     * 	If the operation succeeds:
     * 		 1. Server returns HTTP 200 response message
     * 		 2. The response body contains JSON data
     * 			a. The full client model JSON is returned if the client does not provide a client number,
     * 				or the provided version number does not match the version on the server
     * 			b. "true" is returned if the caller provided a version number and it matched the version number on the server
     *
     *  If the operation fails:
     *  	 1. The server returns an HTTP 400 error message and the response body contains an error message
     *  </pre>
     */
    @Override
    public String gameGetModel(int version) {
    	switch(version){
    		case(0): return testModel1;
    		case(1): return testModel2;
    		case(2): return testModel3;
    		case(4): return testModel3;	// the version numbers match, the string "true" is returned
    		default: return testModel1; // this should never be reached
    	}
    }

    /**
     * Returns a list of supported AI player types.
     *
     * @pre <pre>
     * If the operation succeeds
     * 		 1. The server returns an HTTP 200 response message
     * 		 2. The body contains a JSON string array enumerating the different types of AI players.
     * 		   These are values that may be passed to the gameAddAI method
     *
     *  If the operation fails:
     * 		 1. The server returns an HTTP 400 error message and the response body contains an error message
     * </pre>
     */
    @Override
    public String gameListAI() {
    	return null;
    }

    /**
     * Adds an AI player to the current game.
     *
     * @param aiType The AI player to add to the game
     * @pre <pre>
     * The caller has previously logged in to the server and joined a game (they have valid catan.user and catan.game HTTP cookies).
     * There is space in the game for another player (the game is not �full�).
     * The specified �AIType� is valid (one of the values returned by the /game/listAI method).
     * </pre>
     * @post <pre>
     * If the operation succeeds:
     * 		1. The server returns an HTTP 200 success response with �Success� in the body.
     * 		2. A new AI player of the specified type has been added to the current game.
     * 	   		The server selected a name and color for the player.
     *
     * If the operation fails:
     * 		1. The server returns an HTTP 400 error response, and the body contains an error message.
     * </pre>
     */
    @Override
    public void gameAddAI(String aiType) {

    }

    /**
     * Sends a chat message to the group.
     *
     * @param content The message to send
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * </pre>
     * @post the chat box contains the sent message
     */
    @Override
    public void sendChat(String content) {

    }

    /**
     * A domestic trade is being offered.
     * If the trade is accepted, the two players will swap the specified resources.
     * If the trade is not accepted, no resources are exchanged and the
     * trade offer is removed.
     *
     * @param willAccept Whether or not the player accepts the offered trade
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		You have been offered a tag
     * 		You have the required resources to accept the tag
     * </pre>
     * @post <pre>
     * 		If you accepted, you swap resources with the offering player
     * 		If you declined, no resources are swapped
     * 		The trade offer is removed
     * </pre>
     */
    @Override
    public void acceptTrade(boolean willAccept) {

    }

    /**
     * Tell the server that the dice were rolled.
     *
     * @param number the number that was rolled, in the range 2-12
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		It is your turn
     * 		The status of the client model is 'Rolling'
     * </pre>
     */
    @Override
    public void rollNumber(int number) {

    }

    /**
     * @param isFree       during the setup phase, roads are free
     * @param roadLocation the new road's location.
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		It is your turn
     * 		The status of the client model is 'Playing'
     * 		The road location is open
     * 		The road location is connected to your road
     * 		The road location is not on water
     * 		You have the required resources
     * 		During setup, must be placed by settlement without adjacent road.
     * </pre>
     * @post <pre>
     * 		You have spent the required resources
     * 		The road is at the location on the map
     * 		Longest road has been awarded, if applicable
     * </pre>
     */
    @Override
    public void buildRoad(boolean isFree, EdgeLocation roadLocation) {

    }

    /**
     * @param isFree         during the setup phase, settlements are free
     * @param vertexLocation the new settlement location
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		It is your turn
     * 		The status of the client model is 'Playing'
     * 		The settlement location is open
     * 		The settlement location is connected to your road, except during setup
     * 		The settlement location is not on water
     * 		You have the required resources
     * </pre>
     * @post <pre>
     * 		You have spent the required resources
     * 		The settlement is at the location on the map
     * </pre>
     */
    @Override
    public void buildSettlement(boolean isFree, VertexLocation vertexLocation) {

    }

    /**
     * @param vertexLocation the new city's location
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		It is your turn
     * 		The status of the client model is 'Playing'
     * 		There is a settlement at the city location
     * 		You have the required resources
     * </pre>
     * @post <pre>
     * 		You have spent the required resources
     * 		The settlement is at the location on the map
     * 		You regain 1 settlement
     * </pre>
     */
    @Override
    public void buildCity(VertexLocation vertexLocation) {

    }

    /**
     * Used when built on a port, or when trading to the bank.
     *
     * @param ratio 4 (to 1, when not on a port), 3 (to 1, when on a general port), 2 (to 1, when on a resource port)
     * @param inputResource  What is given
     * @param outputResource What is received
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		You have the required resources
     * 		If ratio is less than 4, you are built on the correct port
     * </pre>
     * @post <pre>
     * 		The offered resources are in the bank
     * 		You have the requested resource.
     * </pre>
     */
    @Override
    public void maritimeTrade(int ratio, ResourceType inputResource, ResourceType outputResource) {

    }

    /**
     * Called when a 7 is rolled and the robber is being moved.
     *
     * @param location    the new robber location
     * @param victimIndex the index of the player being robbed, or -1 if no one is being robbed
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		The robber is not being kept in the same spot.
     * 		The player being robbed has resource cards.
     * </pre>
     * @post <pre>
     * 		The robber is in the new location
     * 		The player being robbed gave you a random resource card.
     * </pre>
     */
    @Override
    public void robPlayer(HexLocation location, int victimIndex) {

    }

    /**
     * Called at the end of a player's turn.
     *
     * @post <pre>
     * 		The cards in your new dev card hand have been moved to your old dev card hand.
     * 		It is the next player's turn.
     * </pre>
     */
    @Override
    public void finishTurn() {

    }

    /**
     * Purchase a development card for 1 wheat, 1 sheep, and 1 ore.
     *
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		You have the required resources
     * 		There are dev cards left to buy
     * </pre>
     * @post <pre>
     *      You have a new dev card
     * 		Monument cards are available immediately (in your old dev card hand)
     * 		Other dev cards are unplayable this turn (in your new dev card hand)
     * 		You have spent the required resources
     * </pre>
     */
    @Override
    public void buyDevCard() {

    }

    /**
     * Play a soldier/knight dev card. Analogous to moving the robber.
     *
     * @param location    The location to move the robber to.
     * @param victimIndex The player to rob, or -1 if there are none.
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		It is your turn.
     * 		The status of the client model is 'Playing'
     * 		You have a soldier card in your old dev card hand
     * 		You have not yet played a non-monument dev card this turn
     * 		The robber is not being kept in the same location
     * 		The player being robbed has resource cards
     * </pre>
     * @post <pre>
     * 		The robber has been moved to the new location
     * 		The player being robbed gave you a random resource card
     * 		Largest army has been awarded, if applicable
     * 		You are not allowed to play non-monument dev cards
     * </pre>
     */
    @Override
    public void playSoldier(HexLocation location, int victimIndex) {

    }

    /**
     * Play a Year of Plenty card, and receive 2 free resources.
     *
     * @param resource1 The first resource you want to receive
     * @param resource2 The second resource you want to receive
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		It is your turn.
     * 		The status of the client model is 'Playing'
     * 		You have a Year of Plenty card in your old dev card hand
     * 		You have not yet played a non-monument dev card this turn
     * 		The requested resources are in the bank.
     * </pre>
     * @post You have the requested resources and the bank does not.
     */
    @Override
    public void playYearOfPlenty(ResourceType resource1, ResourceType resource2) {

    }

    /**
     * Play a Road Building card, and build 2 roads.
     *
     * @param location1 The first location to build a road
     * @param location2 The second location to build a road
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		It is your turn.
     * 		The status of the client model is 'Playing'
     * 		You have a Road Building card in your old dev card hand
     * 		You have not yet played a non-monument dev card this turn
     * 		The first road location is connected to one of your roads
     * 		The second road location is connected to one of your roads, or to the first road
     * 		Neither road location is on water
     * 		You have 2+ unused roads.
     * </pre>
     * @post <pre>
     * 		You have 2 fewer unused roads.
     * 		2 new roads are placed on the map.
     * 		Longest road is awarded, if applicable.
     * </pre>
     */
    @Override
    public void playRoadBuilding(EdgeLocation location1, EdgeLocation location2) {

    }

    /**
     * Play a Monopoly card, and collect a specific resource from all other players.
     *
     * @param resource The resource being taken from the other players
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		It is your turn.
     * 		The status of the client model is 'Playing'
     * 		You have a Monopoly card in your old dev card hand
     * 		You have not yet played a non-monument dev card this turn
     * </pre>
     * @post All the players have given you all of their resources of the specified type
     */
    @Override
    public void playMonopoly(ResourceType resource) {

    }

    /**
     * Play a Monument card, and be awarded a victory point.
     *
     * @pre You have enough monument cards to reach 10 pts and win the game.
     * @post You gained a victory point.
     */
    @Override
    public void playVictoryPoint() {

    }

	@Override
	public void discardCards(Map<ResourceType, Integer> hand) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerTrade(Map<ResourceType, Integer> offer, int receiverIndex) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}
	
	
	/// FOR TESTING ///
	public String testModel1 = "{\"deck\":{\"yearOfPlenty\":2,\"monopoly\":2,\"soldier\":14,\"roadBuilding\":2,\"monument\":5},\"map\":{\"hexes\":[{\"resource\":\"wood\",\"location\":{\"x\":0,\"y\":-2},\"number\":7},{\"resource\":\"brick\",\"location\":{\"x\":1,\"y\":-2},\"number\":4},{\"resource\":\"wood\",\"location\":{\"x\":2,\"y\":-2},\"number\":11},{\"resource\":\"brick\",\"location\":{\"x\":-1,\"y\":-1},\"number\":8},{\"resource\":\"wood\",\"location\":{\"x\":0,\"y\":-1},\"number\":3},{\"resource\":\"ore\",\"location\":{\"x\":1,\"y\":-1},\"number\":9},{\"resource\":\"sheep\",\"location\":{\"x\":2,\"y\":-1},\"number\":12},{\"resource\":\"ore\",\"location\":{\"x\":-2,\"y\":0},\"number\":5},{\"resource\":\"sheep\",\"location\":{\"x\":-1,\"y\":0},\"number\":10},{\"resource\":\"wheat\",\"location\":{\"x\":0,\"y\":0},\"number\":11},{\"resource\":\"brick\",\"location\":{\"x\":1,\"y\":0},\"number\":5},{\"resource\":\"wheat\",\"location\":{\"x\":2,\"y\":0},\"number\":6},{\"resource\":\"wheat\",\"location\":{\"x\":-2,\"y\":1},\"number\":2},{\"resource\":\"sheep\",\"location\":{\"x\":-1,\"y\":1},\"number\":9},{\"resource\":\"wood\",\"location\":{\"x\":0,\"y\":1},\"number\":4},{\"resource\":\"sheep\",\"location\":{\"x\":1,\"y\":1},\"number\":10},{\"resource\":\"wood\",\"location\":{\"x\":-2,\"y\":2},\"number\":6},{\"resource\":\"ore\",\"location\":{\"x\":-1,\"y\":2},\"number\":3},{\"resource\":\"wheat\",\"location\":{\"x\":0,\"y\":2},\"number\":8}],\"roads\":[],\"cities\":[],\"settlements\":[],\"radius\":3,\"ports\":[{\"ratio\":3,\"direction\":\"NW\",\"location\":{\"x\":2,\"y\":1}},{\"ratio\":2,\"resource\":\"brick\",\"direction\":\"NE\",\"location\":{\"x\":-2,\"y\":3}},{\"ratio\":3,\"direction\":\"SW\",\"location\":{\"x\":3,\"y\":-3}},{\"ratio\":3,\"direction\":\"N\",\"location\":{\"x\":0,\"y\":3}},{\"ratio\":2,\"resource\":\"wood\",\"direction\":\"NE\",\"location\":{\"x\":-3,\"y\":2}},{\"ratio\":3,\"direction\":\"SE\",\"location\":{\"x\":-3,\"y\":0}},{\"ratio\":2,\"resource\":\"wheat\",\"direction\":\"S\",\"location\":{\"x\":-1,\"y\":-2}},{\"ratio\":2,\"resource\":\"ore\",\"direction\":\"S\",\"location\":{\"x\":1,\"y\":-3}},{\"ratio\":2,\"resource\":\"sheep\",\"direction\":\"NW\",\"location\":{\"x\":3,\"y\":-1}}],\"robber\":{\"x\":0,\"y\":-2}},\"players\":[{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":0,\"playerIndex\":0,\"name\":\"Sam\",\"color\":\"white\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":1,\"playerIndex\":1,\"name\":\"Brooke\",\"color\":\"blue\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":10,\"playerIndex\":2,\"name\":\"Pete\",\"color\":\"red\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":11,\"playerIndex\":3,\"name\":\"Mark\",\"color\":\"green\"}],\"log\":{\"lines\":[]},\"chat\":{\"lines\":[]},\"bank\":{\"brick\":24,\"wood\":24,\"sheep\":24,\"wheat\":24,\"ore\":24},\"turnTracker\":{\"status\":\"FirstRound\",\"currentTurn\":0,\"longestRoad\":-1,\"largestArmy\":-1},\"winner\":-1,\"version\":0}";
	public String testModel2 = "{\"deck\":{\"yearOfPlenty\":2,\"monopoly\":2,\"soldier\":14,\"roadBuilding\":2,\"monument\":5},\"map\":{\"hexes\":[{\"resource\":\"wood\",\"location\":{\"x\":0,\"y\":-2},\"number\":7},{\"resource\":\"brick\",\"location\":{\"x\":1,\"y\":-2},\"number\":4},{\"resource\":\"wood\",\"location\":{\"x\":2,\"y\":-2},\"number\":11},{\"resource\":\"brick\",\"location\":{\"x\":-1,\"y\":-1},\"number\":8},{\"resource\":\"wood\",\"location\":{\"x\":0,\"y\":-1},\"number\":3},{\"resource\":\"ore\",\"location\":{\"x\":1,\"y\":-1},\"number\":9},{\"resource\":\"sheep\",\"location\":{\"x\":2,\"y\":-1},\"number\":12},{\"resource\":\"ore\",\"location\":{\"x\":-2,\"y\":0},\"number\":5},{\"resource\":\"sheep\",\"location\":{\"x\":-1,\"y\":0},\"number\":10},{\"resource\":\"wheat\",\"location\":{\"x\":0,\"y\":0},\"number\":11},{\"resource\":\"brick\",\"location\":{\"x\":1,\"y\":0},\"number\":5},{\"resource\":\"wheat\",\"location\":{\"x\":2,\"y\":0},\"number\":6},{\"resource\":\"wheat\",\"location\":{\"x\":-2,\"y\":1},\"number\":2},{\"resource\":\"sheep\",\"location\":{\"x\":-1,\"y\":1},\"number\":9},{\"resource\":\"wood\",\"location\":{\"x\":0,\"y\":1},\"number\":4},{\"resource\":\"sheep\",\"location\":{\"x\":1,\"y\":1},\"number\":10},{\"resource\":\"wood\",\"location\":{\"x\":-2,\"y\":2},\"number\":6},{\"resource\":\"ore\",\"location\":{\"x\":-1,\"y\":2},\"number\":3},{\"resource\":\"wheat\",\"location\":{\"x\":0,\"y\":2},\"number\":8}],\"roads\":[],\"cities\":[],\"settlements\":[],\"radius\":3,\"ports\":[{\"ratio\":3,\"direction\":\"NW\",\"location\":{\"x\":2,\"y\":1}},{\"ratio\":2,\"resource\":\"brick\",\"direction\":\"NE\",\"location\":{\"x\":-2,\"y\":3}},{\"ratio\":3,\"direction\":\"SW\",\"location\":{\"x\":3,\"y\":-3}},{\"ratio\":3,\"direction\":\"N\",\"location\":{\"x\":0,\"y\":3}},{\"ratio\":2,\"resource\":\"wood\",\"direction\":\"NE\",\"location\":{\"x\":-3,\"y\":2}},{\"ratio\":3,\"direction\":\"SE\",\"location\":{\"x\":-3,\"y\":0}},{\"ratio\":2,\"resource\":\"wheat\",\"direction\":\"S\",\"location\":{\"x\":-1,\"y\":-2}},{\"ratio\":2,\"resource\":\"ore\",\"direction\":\"S\",\"location\":{\"x\":1,\"y\":-3}},{\"ratio\":2,\"resource\":\"sheep\",\"direction\":\"NW\",\"location\":{\"x\":3,\"y\":-1}}],\"robber\":{\"x\":0,\"y\":-2}},\"players\":[{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":0,\"playerIndex\":0,\"name\":\"Thomas\",\"color\":\"white\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":1,\"playerIndex\":1,\"name\":\"Brooke\",\"color\":\"blue\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":10,\"playerIndex\":2,\"name\":\"Pete\",\"color\":\"red\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":11,\"playerIndex\":3,\"name\":\"Mark\",\"color\":\"green\"}],\"log\":{\"lines\":[]},\"chat\":{\"lines\":[]},\"bank\":{\"brick\":24,\"wood\":24,\"sheep\":24,\"wheat\":24,\"ore\":24},\"turnTracker\":{\"status\":\"FirstRound\",\"currentTurn\":0,\"longestRoad\":-1,\"largestArmy\":-1},\"winner\":-1,\"version\":0}";
	public String testModel3 = "{\"deck\":{\"yearOfPlenty\":2,\"monopoly\":2,\"soldier\":14,\"roadBuilding\":2,\"monument\":5},\"map\":{\"hexes\":[{\"resource\":\"wood\",\"location\":{\"x\":0,\"y\":-2},\"number\":7},{\"resource\":\"brick\",\"location\":{\"x\":1,\"y\":-2},\"number\":4},{\"resource\":\"wood\",\"location\":{\"x\":2,\"y\":-2},\"number\":11},{\"resource\":\"brick\",\"location\":{\"x\":-1,\"y\":-1},\"number\":8},{\"resource\":\"wood\",\"location\":{\"x\":0,\"y\":-1},\"number\":3},{\"resource\":\"ore\",\"location\":{\"x\":1,\"y\":-1},\"number\":9},{\"resource\":\"sheep\",\"location\":{\"x\":2,\"y\":-1},\"number\":12},{\"resource\":\"ore\",\"location\":{\"x\":-2,\"y\":0},\"number\":5},{\"resource\":\"sheep\",\"location\":{\"x\":-1,\"y\":0},\"number\":10},{\"resource\":\"wheat\",\"location\":{\"x\":0,\"y\":0},\"number\":11},{\"resource\":\"brick\",\"location\":{\"x\":1,\"y\":0},\"number\":5},{\"resource\":\"wheat\",\"location\":{\"x\":2,\"y\":0},\"number\":6},{\"resource\":\"wheat\",\"location\":{\"x\":-2,\"y\":1},\"number\":2},{\"resource\":\"sheep\",\"location\":{\"x\":-1,\"y\":1},\"number\":9},{\"resource\":\"wood\",\"location\":{\"x\":0,\"y\":1},\"number\":4},{\"resource\":\"sheep\",\"location\":{\"x\":1,\"y\":1},\"number\":10},{\"resource\":\"wood\",\"location\":{\"x\":-2,\"y\":2},\"number\":6},{\"resource\":\"ore\",\"location\":{\"x\":-1,\"y\":2},\"number\":3},{\"resource\":\"wheat\",\"location\":{\"x\":0,\"y\":2},\"number\":8}],\"roads\":[],\"cities\":[],\"settlements\":[],\"radius\":3,\"ports\":[{\"ratio\":3,\"direction\":\"NW\",\"location\":{\"x\":2,\"y\":1}},{\"ratio\":2,\"resource\":\"brick\",\"direction\":\"NE\",\"location\":{\"x\":-2,\"y\":3}},{\"ratio\":3,\"direction\":\"SW\",\"location\":{\"x\":3,\"y\":-3}},{\"ratio\":3,\"direction\":\"N\",\"location\":{\"x\":0,\"y\":3}},{\"ratio\":2,\"resource\":\"wood\",\"direction\":\"NE\",\"location\":{\"x\":-3,\"y\":2}},{\"ratio\":3,\"direction\":\"SE\",\"location\":{\"x\":-3,\"y\":0}},{\"ratio\":2,\"resource\":\"wheat\",\"direction\":\"S\",\"location\":{\"x\":-1,\"y\":-2}},{\"ratio\":2,\"resource\":\"ore\",\"direction\":\"S\",\"location\":{\"x\":1,\"y\":-3}},{\"ratio\":2,\"resource\":\"sheep\",\"direction\":\"NW\",\"location\":{\"x\":3,\"y\":-1}}],\"robber\":{\"x\":0,\"y\":-2}},\"players\":[{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":0,\"playerIndex\":0,\"name\":\"James\",\"color\":\"white\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":1,\"playerIndex\":1,\"name\":\"Brooke\",\"color\":\"blue\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":10,\"playerIndex\":2,\"name\":\"Pete\",\"color\":\"red\"},{\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"playedDevCard\":false,\"discarded\":false,\"playerID\":11,\"playerIndex\":3,\"name\":\"Mark\",\"color\":\"green\"}],\"log\":{\"lines\":[]},\"chat\":{\"lines\":[]},\"bank\":{\"brick\":24,\"wood\":24,\"sheep\":24,\"wheat\":24,\"ore\":24},\"turnTracker\":{\"status\":\"FirstRound\",\"currentTurn\":0,\"longestRoad\":-1,\"largestArmy\":-1},\"winner\":-1,\"version\":0}";
	public String testModel4 = "true";
}