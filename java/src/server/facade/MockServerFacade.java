package server.facade;

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
	public String userLogin(String username, String password) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String userRegister(String username, String password) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public Object discardCards(int gameID, int playerID, Map<ResourceType, Integer> hand)
			throws InvalidActionException {
		// TODO Auto-generated method stub
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

	@Override
	public Object offerTrade(int gameID, int senderID, int receiverID, Map<ResourceType, Integer> offer)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object maritimeTrade(int ratio, ResourceType inputResource, ResourceType outputResource)
			throws InvalidActionException {
		// TODO Auto-generated method stub
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
