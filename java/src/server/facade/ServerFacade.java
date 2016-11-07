package server.facade;

import java.util.HashMap;
import java.util.Map;

import server.model.ServerModel;
import shared.model.InvalidActionException;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.Game;

public class ServerFacade implements IServerFacade {
	
	ServerModel model;
	
	public ServerFacade() {
		model = new ServerModel();
	}

	@Override
	public String userLogin(String username, String password) throws InvalidActionException {
		if(model.login(username, password)) {
			return "Success";
		}
		else {
			throw new InvalidActionException("Error");
		}
	}

	@Override
	public String userRegister(String username, String password) throws InvalidActionException {
		model.registerUser(username, password);
		return "Success";
	}

	@Override
	public Game[] gamesList() throws InvalidActionException {
		return model.listGames();
	}

	@Override
	public String gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws InvalidActionException {
		model.createGame(name);
		//TODO: what will be returned here?
		return null;
	}

	@Override
	public String gamesJoin(int gameID, CatanColor color) throws InvalidActionException {
		//TODO: need a canDo here?
		//"Success"
		//"The player could not be added to the specified game."
		return null;
	}

	@Override
	public String gameGetModel() throws InvalidActionException {
		// TODO: return model
		return null;
	}

	@Override
	public String gameGetModel(int version) throws InvalidActionException {
		// TODO return model
		return null;
	}

	@Override
	public String[] gameListAI(int gameID) throws InvalidActionException {
		return model.listAIPlayers(gameID);
	}

	@Override
	public Object gameAddAI(int gameID, String aiType) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object sendChat(int gameID, int playerID, String message) throws InvalidActionException {
		// TODO need a canDo here to check if for game cookie?
		
		//"The catan.game HTTP cookie is missing.  You must join a game before calling this method."
		return null;
	}

	@Override
	public Object acceptTrade(int gameID, boolean willAccept) throws InvalidActionException {
		model.acceptTradeOffer(gameID, willAccept);
		
		//"Command execution failed.
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
		
		return null;
	}

	@Override
	public Object buildSettlement(int gameID, int playerID, boolean isFree, VertexLocation vertexLocation)
			throws InvalidActionException {
		if(model.canPlaceSettlement(playerID, isFree, vertexLocation)) {
			model.placeSettlement(gameID, playerID, isFree, vertexLocation);
			return("200");
		}
		else {
			throw new InvalidActionException("");
		}
	}

	@Override
	public Object buildCity(int gameID, int playerID, VertexLocation vertexLocation) throws InvalidActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object offerTrade(int gameID, int senderID, int receiverID, HashMap<ResourceType, Integer> offer)
			throws InvalidActionException {
		if(model.canTrade() && model.canTradeWithPlayer(senderID, receiverID, offer)) {
			model.makeTradeOffer(gameID, senderID, receiverID, offer);
			//return a model
			return  null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	@Override
	public Object maritimeTrade(int gameID, int playerID, int ratio, ResourceType inputResource, ResourceType outputResource)
			throws InvalidActionException {
		if(model.canTradeWithBank(playerID, ratio, inputResource, outputResource)) {
			model.makeMaritimeTrade(gameID, playerID);
			//return a model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	@Override
	public Object robPlayer(int gameID, int playerID, HexLocation location, int victimIndex)
			throws InvalidActionException {
		//TODO: does there need to be a canDo here?
		model.robPlayer(gameID, playerID, victimIndex);
		return null;
	}

	@Override
	public Object finishTurn(int gameID) throws InvalidActionException {
		model.finishTurn(gameID);
		//return the model		
		return null;
		
	}

	@Override
	public Object buyDevCard(int gameID, int playerID) throws InvalidActionException {
		if(model.canBuyDevelopmentCard(playerID)) {
			model.buyDevelopmentCard(gameID, playerID);
			//return the model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
		
	}

	@Override
	public Object playSoldier(int gameID, int playerID, HexLocation location, int victimIndex)
			throws InvalidActionException {
		if(model.canPlaySoldier(playerID, location, victimIndex)) {
			model.playSoldierCard(gameID, playerID, location, victimIndex);
			//return model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	@Override
	public Object playYearOfPlenty(int gameID, int playerID, ResourceType resource1, ResourceType resource2)
			throws InvalidActionException {
		if(model.canPlayYearOfPlenty(playerID, resource1, resource2)) {
			model.playYearOfPleanty(gameID, playerID, resource1, resource2);
			//TODO: return model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	@Override
	public Object playRoadBuilding(int gameID, int playerID, EdgeLocation location1, EdgeLocation location2)
			throws InvalidActionException {
		if(model.canPlayRoadCard(playerID, location1, location2)) {
			model.playRoadCard(gameID, playerID, location1, location2);
			//TODO return model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	@Override
	public Object playMonopoly(int gameID, int playerID, ResourceType resource) throws InvalidActionException {
		if(model.canPlayMonopolyCard(playerID, resource)) {
			model.playMonopolyCard(gameID, playerID, resource);
			//TODO return model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	@Override
	public Object playMonument(int gameID, int playerID) throws InvalidActionException {
		if(model.canPlayMonumentCard(playerID)) {
			model.playMonumentCard(gameID, playerID);
			//TODO: return model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	
}
