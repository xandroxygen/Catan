package server.facade;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.Game;
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
	public Game[] gamesList() throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gamesJoin(int gameID, CatanColor color) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gameGetModel() throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String gameGetModel(int version) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] gameListAI(int gameID) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object gameAddAI(int gameID, String aiType) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object sendChat(int gameID, int playerID, String message) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object acceptTrade(int gameID, boolean willAccept) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
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
	public Object discardCards(int gameID, int playerID, HashMap<ResourceType, Integer> hand) throws InvalidActionException {
		return null;
	}

	@Override
	public Object rollNumber(int gameID, int playerID, int rollValue) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object buildRoad(int gameID, int playerID, boolean isFree, EdgeLocation roadLocation)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object buildSettlement(int gameID, int playerID, boolean isFree, VertexLocation vertexLocation)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object buildCity(int gameID, int playerID, VertexLocation vertexLocation) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
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
	public Object offerTrade(int gameID, int senderID, int receiverID, HashMap<ResourceType, Integer> offer) throws InvalidActionException {
		return null;
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
		return null;
	}

	@Override
	public Object robPlayer(int gameID, int playerID, HexLocation location, int victimIndex)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object finishTurn(int gameID) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object buyDevCard(int gameID, int playerID) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object playSoldier(int gameID, int playerID, HexLocation location, int victimIndex)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object playYearOfPlenty(int gameID, int playerID, ResourceType resource1, ResourceType resource2)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object playRoadBuilding(int gameID, int playerID, EdgeLocation location1, EdgeLocation location2)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object playMonopoly(int gameID, int playerID, ResourceType resource) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object playMonument(int gameID, int playerID) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

}
