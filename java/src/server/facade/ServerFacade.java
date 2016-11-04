package server.facade;

import java.util.Map;

import client.model.InvalidActionException;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

public class ServerFacade implements IServerFacade{

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
	public String gamesList() throws InvalidActionException {
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
	public String gameListAI() throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void gameAddAI(String aiType) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendChat(String message) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptTrade(boolean willAccept) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discardCards(Map<ResourceType, Integer> hand) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollNumber(int number) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildRoad(boolean isFree, EdgeLocation roadLocation) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildSettlement(boolean isFree, VertexLocation vertexLocation) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildCity(VertexLocation vertexLocation) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerTrade(Map<ResourceType, Integer> offer, int receiverIndex) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void maritimeTrade(int ratio, ResourceType inputResource, ResourceType outputResource)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void robPlayer(HexLocation location, int victimIndex) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishTurn() throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buyDevCard() throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSoldier(HexLocation location, int victimIndex) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playYearOfPlenty(ResourceType resource1, ResourceType resource2) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playRoadBuilding(EdgeLocation location1, EdgeLocation location2) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playMonopoly(ResourceType resource) throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playVictoryPoint() throws InvalidActionException {
		// TODO Auto-generated method stub
		
	}

}
