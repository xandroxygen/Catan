package server.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import server.model.ServerGame;
import server.model.ServerModel;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.InvalidActionException;

public class MockServerFacade implements IServerFacade{

	@Override
	public int userLogin(String username, String password) throws InvalidActionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int userRegister(String username, String password) throws InvalidActionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<ServerGame> gamesList() throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerGame gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gamesJoin(int gameID, int playerID, CatanColor color) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ServerGame gameGetModel(int version) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the current state of the game in JSON format.
	 *
	 * @param gameID
	 * @param version The version number of the model. Used to compare and check if model has been updated.  @throws InvalidActionException
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
	 * <p>
	 *  If the operation fails:
	 *  	 1. The server returns an HTTP 400 error message and the response body contains an error message
	 *  </pre>
	 */
	@Override
	public ServerGame gameGetModel(int gameID, int version) throws InvalidActionException {
		return null;
	}

	/**
	 * Returns a list of supported AI player types.
	 *
	 * @return All AI player types for a particular game
	 * @throws InvalidActionException
	 * @pre <pre>
	 * If the operation succeeds
	 * 		 1. The server returns an HTTP 200 response message
	 * 		 2. The body contains a JSON string array enumerating the different types of AI players.
	 * 		   These are values that may be passed to the gameAddAI method
	 * <p>
	 *  If the operation fails:
	 * 		 1. The server returns an HTTP 400 error message and the response body contains an error message
	 * </pre>
	 */
	@Override
	public String[] gameListAI() throws InvalidActionException {
		return new String[0];
	}

	@Override
	public Object gameAddAI(int gameID, String aiType) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object sendChat(int gameID, int playerID, String message) throws InvalidActionException {
		return message;
	}

	@Override
	public Object acceptTrade(int gameID, boolean willAccept) throws InvalidActionException {
		return willAccept;
	}

	/**
	 * @param gameID   the ID of the game from which the request was made
	 * @param playerID The ID of the player requesting the move
	 * @param hand     The cards being discarded
	 * @pre <pre>
	 *      Player is logged in
	 * 		Player has joined a game
	 * 		The status of the client model is 'Discarding'
	 * 		You have more than 7 cards
	 * 		You have the resources you are discarding
	 * </pre>
	 */
	@Override
	public Object discardCards(int gameID, int playerID, Map<ResourceType, Integer> hand) throws InvalidActionException {
		return hand.get(ResourceType.BRICK);
	}

	@Override
	public Object rollNumber(int gameID, int playerID, int rollValue) throws InvalidActionException {
		return rollValue;
	}

	@Override
	public Object buildRoad(int gameID, int playerID, boolean isFree, EdgeLocation roadLocation)
			throws InvalidActionException {
		return isFree;
	}

	@Override
	public Object buildSettlement(int gameID, int playerID, boolean isFree, VertexLocation vertexLocation)
			throws InvalidActionException {
		return isFree;
	}

	@Override
	public Object buildCity(int gameID, int playerID, VertexLocation vertexLocation) throws InvalidActionException {
		return vertexLocation.getDir();
	}

	/**
	 * Contact another player and offer to trade cards back and forth.
	 *
	 * @param gameID
	 * @param senderID
	 * @param receiverID The index of the recipient of the trade offer
	 * @param offer      Cards you are offering - negative numbers means you receive those cards, positive means you give   @pre You have the resources you are offering
	 * @post The trade is offered to the other player
	 */
	@Override
	public Object offerTrade(int gameID, int senderID, int receiverID, Map<ResourceType, Integer> offer) throws InvalidActionException {
		return offer.get(ResourceType.BRICK);
	}

	/**
	 * Used when built on a port, or when trading to the bank.
	 *
	 * @param gameID
	 * @param playerID
	 * @param ratio          4 (to 1, when not on a port), 3 (to 1, when on a general port), 2 (to 1, when on a resource port)
	 * @param inputResource  What is given
	 * @param outputResource What is received
	 *                       @pre <pre>
	 *                            Player is logged in
	 *                       		Player has joined a game
	 *                       		You have the required resources
	 *                       		If ratio is less than 4, you are built on the correct port
	 *                       </pre>
	 * @post <pre>
	 * 		The offered resources are in the bank
	 * 		You have the requested resource.
	 * </pre>
	 */
	@Override
	public Object maritimeTrade(int gameID, int playerID, int ratio, ResourceType inputResource, ResourceType outputResource) throws InvalidActionException {
		return inputResource;
	}

	@Override
	public Object robPlayer(int gameID, int playerID, HexLocation location, int victimIndex)
			throws InvalidActionException {
		return victimIndex;
	}

	@Override
	public Object finishTurn(int gameID) throws InvalidActionException {
		return gameID;
	}

	@Override
	public Object buyDevCard(int gameID, int playerID) throws InvalidActionException {
		return playerID;
	}

	@Override
	public Object playSoldier(int gameID, int playerID, HexLocation location, int victimIndex)
			throws InvalidActionException {
		return victimIndex;
	}

	@Override
	public Object playYearOfPlenty(int gameID, int playerID, ResourceType resource1, ResourceType resource2)
			throws InvalidActionException {
		return resource1;
	}

	@Override
	public Object playRoadBuilding(int gameID, int playerID, EdgeLocation location1, EdgeLocation location2)
			throws InvalidActionException {
		return playerID;
	}

	@Override
	public Object playMonopoly(int gameID, int playerID, ResourceType resource) throws InvalidActionException {
		return resource;
	}

	@Override
	public Object playMonument(int gameID, int playerID) throws InvalidActionException {
		return playerID;
	}

}
