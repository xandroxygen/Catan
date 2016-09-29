package client.server;

import client.model.InvalidActionException;
import com.google.gson.JsonObject;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Communicates with the server API, and
 * Keeps track of player and game cookies.
 * Uses the HTTPOperations class to make post and get calls.
 */
public class ServerProxy implements IServerProxy {

    private HTTPOperations http;
    private Map<String, String> headers;
    private String currentGameCookie;
    private String currentPlayerCookie;
    private int currentPlayerIndex;
    private String urlExt;

    private static final String EXCEPTION_MESSAGE_CALL = "API call failed.";
    private static final String EXCEPTION_MESSAGE_RESPONSE = "API response failed.";


    /**
     * Default constructor, sets all values to new objects.
     * Also sets host, port, and base URL for the communicator class.
     */
    public ServerProxy() {
        http = new HTTPOperations("localhost", "8081"); // hard-coded for ease of use
        headers = new HashMap<>();
        urlExt = "";
    }

    /**
     * Instructs the server communicator which player's turn it is.
     * The communicator uses the player's specific cookie.
     *
     * @param playerIndex index (NOT id) of the player whose turn it is
     */
    @Override
    public void setPlayer(int playerIndex) {
        currentPlayerIndex = playerIndex;
    }

    // --- HELPER FUNCTIONS ---
    private void setHeaders() {
        headers.clear();

        String cookies = currentPlayerCookie + "; " + currentGameCookie;
        headers.put("Cookie", cookies);
    }

    private RequestResponse post(String urlExt, Map<String, String> headers, String body) throws InvalidActionException {
        try {
            return http.post(urlExt, headers, body);
        }
        catch (MalformedURLException e) {
            throw new InvalidActionException(EXCEPTION_MESSAGE_CALL);
        }
    }

    private RequestResponse get(String urlExt, Map<String, String> headers) throws InvalidActionException {
        try {
            return http.get(urlExt, headers);
        }
        catch (MalformedURLException e) {
            throw new InvalidActionException(EXCEPTION_MESSAGE_CALL);
        }
    }

    private void handleResult(RequestResponse result) throws InvalidActionException {
        if (result.hasError()) {
            throw new InvalidActionException(EXCEPTION_MESSAGE_RESPONSE);
        }
        else {
            String modelJSON = (String)result.getData();
            // TODO: Do something with this? Or leave for Server Poller?
        }
    }
    
    private String booleanToString(boolean toConvert) {
    	if (toConvert) {
    		return "true";
    	}
    	else {
    		return "false";
    	}
    }

    public String getCurrentGameCookie() {
        return currentGameCookie;
    }

    public void setCurrentGameCookie(String currentGameCookie) {
        this.currentGameCookie = currentGameCookie;
    }

    public String getCurrentPlayerCookie() {
        return currentPlayerCookie;
    }

    public void setCurrentPlayerCookie(String currentPlayerCookie) {
        this.currentPlayerCookie = currentPlayerCookie;
    }

    // --- NON-MOVE API ---

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
     *  @return cookie of user who just logged in
     */
    @Override
    public String userLogin(String username, String password) throws InvalidActionException {
    	String urlExt = "/user/login";
		
		setHeaders();
    	
    	Map<String, String> m = new HashMap<>();
    	m.put("username", username);
    	m.put("password", password);
    	String body = Serializer.serializeNonMoveCall(m);
    	
    	RequestResponse result = post(urlExt, headers, body); 

    	handleResult(result);

        currentPlayerCookie = result.getCookie();
        return result.getCookie();
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
	 *@param username Username of the new player being registered.
     * @param password Password that corresponds to the username of new player being registered.
     * @return Cookie of user that just registered and logged in
     */
    @Override
    public String userRegister(String username, String password) throws InvalidActionException {
    	
    	if (username != null && password != null) {
        	String urlExt = "/user/register";
        	
        	Map<String, String> m = new HashMap<>();
        	m.put("username", username);
        	m.put("password", password);
        	String body = Serializer.serializeNonMoveCall(m);

        	RequestResponse result = post(urlExt, headers, body);
        	handleResult(result);
            currentPlayerCookie = result.getCookie();
            // TODO set current player index here
            return result.getCookie();
    	}
    	return "";
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
    public String gamesList() throws InvalidActionException {
    	String urlExt = "/games/list";
    	
    	setHeaders();
    	
    	RequestResponse result = get(urlExt, headers);
    	if (result.hasError()) {
    		throw new InvalidActionException(EXCEPTION_MESSAGE_CALL);
    	} else {
    		return (String)result.getData();
    	}    	
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
    public String gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts)
    		throws InvalidActionException {
    	String urlExt = "/games/create";
    	
    	setHeaders();
    	
    	Map<String, String> m = new HashMap<>();
    	m.put("randomTiles", booleanToString(randomTiles));
    	m.put("randomNumbers", booleanToString(randomNumbers));
    	m.put("randomPorts", booleanToString(randomPorts));
    	String body = Serializer.serializeNonMoveCall(m);
    	
    	RequestResponse result = post(urlExt, headers, body);
    	if (result.hasError()) {
    		throw new InvalidActionException(EXCEPTION_MESSAGE_CALL);
    	} else {
    		return (String)result.getData();
    	}
    	
    }

    /**
     * Adds the player to the specified game and sets their catan.game cookie.
     *
     * @param gameID ID of the game to join
     * @param c  Player color
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
     * 		3. The server response includes the "set-cookie" response header setting the catan.game HTTP cookie
     *
     * 	If the operation fails:
     * 		1. Server returns 400 error response and body contains an error message.
     * </pre>
     * @return Cookie of game that was just joined
     */
    @Override
    public String gamesJoin(int gameID, CatanColor c) throws InvalidActionException {
    	String urlExt = "/games/join";
    	String color = c.toString(); //TODO: Make sure color is being correctly converted to a string
    	
    	Map<String, String> m = new HashMap<>();
    	m.put("gameID", Integer.toString(gameID));
    	m.put("color", color);
    	String body = Serializer.serializeNonMoveCall(m);
    	
    	setHeaders();
    	
    	RequestResponse result = post(urlExt, headers, body);

    	handleResult(result);

        currentGameCookie = result.getCookie();
        return result.getCookie();
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
     *  If the operation fails:
     * 		1. The server returns an HTTP 400 error message and the response body contains an error message
     * </pre>
     */
    @Override
    public String gameGetModel() throws InvalidActionException {
    	String urlExt = "/game/model";
    	
    	setHeaders();
    	
    	RequestResponse result = get(urlExt, headers);
    	if (result.hasError()) {
    		throw new InvalidActionException(EXCEPTION_MESSAGE_CALL);
    	} else {
    		return (String)result.getData();
    	}
    }

    /**
     * Returns the current state of the game in JSON format.
     *
     * @param version The version number of the model. Used to compare and check if model has been updated.
     * @pre <pre>
     * 	1. The caller has previously logged into the server and joined a game (they have valid catan.game and catan.user HTTP cookies)
     * 	2. If specified, the version number is included as the "version" query parameter in the request URL,
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
    public String gameGetModel(int version) throws InvalidActionException {
    	String urlExt = "/game/model?version=" + version;
    	
    	setHeaders();
    	
    	RequestResponse result = get(urlExt, headers);
    	if (result.hasError()) {
    		throw new InvalidActionException(EXCEPTION_MESSAGE_CALL);
    	} else {
    		return (String)result.getData();
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
    public String gameListAI() throws InvalidActionException {
    	String urlExt = "/game/reset";
    	
    	setHeaders();
    	
    	RequestResponse result = get(urlExt, headers);
    	if (result.hasError()) {
    		throw new InvalidActionException(EXCEPTION_MESSAGE_CALL);
    	} else {
    		return (String)result.getData();
    	} 
    }

    /**
     * Adds an AI player to the current game.
     *
     * @param aiType The AI player to add to the game
     * 
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
    public void gameAddAI(String aiType) throws InvalidActionException {
    	String urlExt = "/game/addAI";
    	
    	setHeaders();
    	
    	Map<String, String> m = new HashMap<>();
    	m.put("AIType", aiType);
    	String body = Serializer.serializeNonMoveCall(m);
    	
    	RequestResponse result = post(urlExt, headers, body);
    	handleResult(result);
    }

    // --- MOVE API ---

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
    public void sendChat(String content) throws InvalidActionException {
        urlExt = "/moves/sendChat";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("content", content);

        String body = Serializer.serializeMoveCall("sendChat", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
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
    public void acceptTrade(boolean willAccept) throws InvalidActionException {
        urlExt = "/moves/acceptTrade";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("willAccept", booleanToString(willAccept));

        String body = Serializer.serializeMoveCall("acceptTrade", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
    }

    /**
     * @param hand The cards being discarded
     * @pre <pre>
     *      Player is logged in
     * 		Player has joined a game
     * 		The status of the client model is 'Discarding'
     * 		You have more than 7 cards
     * 		You have the resources you are discarding
     * </pre>
     */
    @Override
    public void discardCards(Map<ResourceType, Integer> hand) throws InvalidActionException {
        urlExt = "/moves/discardCards";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("discardedCards", Serializer.serializeHand(hand));

        String body = Serializer.serializeMoveCall("discardCards", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
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
    public void rollNumber(int number) throws InvalidActionException {
        urlExt = "/moves/rollNumber";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("number", Integer.toString(number));

        String body = Serializer.serializeMoveCall("rollNumber", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
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
    public void buildRoad(boolean isFree, EdgeLocation roadLocation) throws InvalidActionException {
        urlExt = "/moves/buildRoad";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("roadLocation", Serializer.serializeEdgeLocation(roadLocation));
        attributes.put("free", booleanToString(isFree));

        String body = Serializer.serializeMoveCall("buildRoad", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
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
    public void buildSettlement(boolean isFree, VertexLocation vertexLocation) throws InvalidActionException {
        urlExt = "/moves/buildSettlement";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("vertexLocation", Serializer.serializeVertexLocation(vertexLocation));
        attributes.put("free", booleanToString(isFree));

        String body = Serializer.serializeMoveCall("buildSettlement", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
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
    public void buildCity(VertexLocation vertexLocation) throws InvalidActionException {
        urlExt = "/moves/buildCity";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("vertexLocation", Serializer.serializeVertexLocation(vertexLocation));

        String body = Serializer.serializeMoveCall("buildCity", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
    }

    /**
     * Contact another player and offer to trade cards back and forth.
     *
     * @param offer         Cards you are offering - negative numbers means you receive those cards, positive means you give
     * @param receiverIndex The index of the recipient of the trade offer
     * @pre You have the resources you are offering
     * @post The trade is offered to the other player
     */
    @Override
    public void offerTrade(Map<ResourceType, Integer> offer, int receiverIndex) throws InvalidActionException {
        urlExt = "/moves/offerTrade";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("offer", Serializer.serializeHand(offer));
        attributes.put("receiver", Integer.toString(receiverIndex));

        String body = Serializer.serializeMoveCall("offerTrade", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
    }

    /**
     * Used when built on a port, or when trading to the bank.
     *
     * @param ratio          4 (to 1, when not on a port), 3 (to 1, when on a general port), 2 (to 1, when on a resource port)
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
    public void maritimeTrade(int ratio, ResourceType inputResource, ResourceType outputResource) throws InvalidActionException {
        urlExt = "/moves/maritimeTrade";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("ratio", Integer.toString(ratio));
        attributes.put("inputResource", Serializer.serializeResourceType(inputResource));
        attributes.put("outputResource", Serializer.serializeResourceType(outputResource));

        String body = Serializer.serializeMoveCall("maritimeTrade", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
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
    public void robPlayer(HexLocation location, int victimIndex) throws InvalidActionException {
        urlExt = "/moves/robPlayer";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("victimIndex", Integer.toString(victimIndex));
        attributes.put("location", Serializer.serializeHexLocation(location));

        String body = Serializer.serializeMoveCall("robPlayer", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
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
    public void finishTurn() throws InvalidActionException {
        urlExt = "/moves/finishTurn";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();

        String body = Serializer.serializeMoveCall("finishTurn", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
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
    public void buyDevCard() throws InvalidActionException {
        urlExt = "/moves/buyDevCard";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();

        String body = Serializer.serializeMoveCall("buyDevCard", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
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
    public void playSoldier(HexLocation location, int victimIndex) throws InvalidActionException {
        urlExt = "/moves/Soldier";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("victimIndex", Integer.toString(victimIndex));
        attributes.put("location", Serializer.serializeHexLocation(location));

        String body = Serializer.serializeMoveCall("Soldier", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
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
    public void playYearOfPlenty(ResourceType resource1, ResourceType resource2) throws InvalidActionException {
        urlExt = "/moves/Year_Of_Plenty";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("resource1", Serializer.serializeResourceType(resource1));
        attributes.put("resource2", Serializer.serializeResourceType(resource2));

        String body = Serializer.serializeMoveCall("Year_Of_Plenty", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);    }

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
    public void playRoadBuilding(EdgeLocation location1, EdgeLocation location2) throws InvalidActionException {
        urlExt = "/moves/Road_Building";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("spot1", Serializer.serializeEdgeLocation(location1));
        attributes.put("spot2", Serializer.serializeEdgeLocation(location2));

        String body = Serializer.serializeMoveCall("Road_Building", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
    }

    /**
     * Play a Monopoly card, and collect a specific resource from all other players.
     * Serializes itself, because the order of type/playerIndex/resource is out of whack for the server here.
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
    public void playMonopoly(ResourceType resource) throws InvalidActionException {
        urlExt = "/moves/Monopoly";

        setHeaders();

        // hard coded serialization because of difference of object order
        JsonObject attributes = new JsonObject();
        attributes.addProperty("type", "Monopoly");
        attributes.addProperty("resource", Serializer.serializeResourceType(resource));
        attributes.addProperty("playerIndex", currentPlayerIndex);

        String body = attributes.toString();

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
    }

    /**
     * Play a Monument card, and be awarded a victory point.
     *
     * @pre You have enough monument cards to reach 10 pts and win the game.
     * @post You gained a victory point.
     */
    @Override
    public void playVictoryPoint() throws InvalidActionException {
        urlExt = "/moves/Monument";

        setHeaders();

        Map<String, String> attributes = new HashMap<>();

        String body = Serializer.serializeMoveCall("Monument", currentPlayerIndex, attributes);

        RequestResponse result = post(urlExt, headers, body);

        handleResult(result);
    }
}