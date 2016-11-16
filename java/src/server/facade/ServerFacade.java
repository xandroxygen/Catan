package server.facade;

import java.util.List;
import java.util.Map;

import server.model.ServerGame;
import server.model.ServerModel;
import shared.model.InvalidActionException;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

public class ServerFacade implements IServerFacade {
	
	ServerModel model;
	
	public ServerFacade() {
		model = ServerModel.getInstance();
	}

	@Override
	public int userLogin(String username, String password) throws InvalidActionException {
		if(model.login(username, password)) {
			return 0; // TODO return playerID
		}
		else {
			throw new InvalidActionException("Cannot log in");
		}
	}

	@Override
	public int userRegister(String username, String password) throws InvalidActionException {
		return model.registerUser(username, password);
	}

	@Override
	public List<ServerGame> gamesList() throws InvalidActionException {
		return model.listGames();
	}

	@Override
	public ServerGame gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws InvalidActionException {
		return model.createGame(randomTiles, randomNumbers, randomPorts, name);
	}

	@Override
	public String gamesJoin(int gameID, int playerID, CatanColor color) throws InvalidActionException {
		model.join(playerID, gameID, color);
		return "Success";
	}

	@Override
	public ServerGame gameGetModel(int gameID) throws InvalidActionException {
		return model.listGames().get(gameID);
	}

	@Override
	public ServerGame gameGetModel(int gameID, int version) throws InvalidActionException {
		return model.listGames().get(gameID);
	}

	@Override
	public String[] gameListAI() throws InvalidActionException {
		return model.listAIPlayers();
	}

	@Override
	public Object gameAddAI(int gameID, String aiType) throws InvalidActionException {
		model.addComputerPlayer(gameID);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object sendChat(int gameID, int playerID, String message) throws InvalidActionException {
		model.sendMessage(gameID, playerID, message);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object acceptTrade(int gameID, boolean willAccept) throws InvalidActionException {
		model.acceptTradeOffer(gameID, willAccept);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object discardCards(int gameID, int playerID, Map<ResourceType, Integer> hand)
			throws InvalidActionException {
		
		model.discardCards(gameID, playerID, hand);
	
		return model.listGames().get(gameID);
	}

	@Override
	public Object rollNumber(int gameID, int playerID, int rollValue) throws InvalidActionException {
		model.rollDice(gameID, playerID, rollValue);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object buildRoad(int gameID, int playerID, boolean isFree, EdgeLocation roadLocation)
			throws InvalidActionException {
		if(model.canPlaceRoad(gameID, playerID, isFree, roadLocation)) {
			model.placeRoad(gameID, playerID, isFree, roadLocation);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Error building a road");
		}
	}

	@Override
	public Object buildSettlement(int gameID, int playerID, boolean isFree, VertexLocation vertexLocation)
			throws InvalidActionException {
		if(model.canPlaceSettlement(gameID, playerID, isFree, vertexLocation)) {
			model.placeSettlement(gameID, playerID, isFree, vertexLocation);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Error building a settlement");
		}
	}

	@Override
	public Object buildCity(int gameID, int playerID, VertexLocation location) throws InvalidActionException {
		if(model.canPlaceCity(gameID, playerID, location)) {
			model.placeCity(gameID, playerID, location);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot build a city");
		}
	}

	@Override
	public Object offerTrade(int gameID, int senderID, int receiverID, Map<ResourceType, Integer> offer)
			throws InvalidActionException {
		if(model.canTrade(gameID) && model.canTradeWithPlayer(gameID, senderID, receiverID, offer)) {
			model.makeTradeOffer(gameID, senderID, receiverID, offer);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot make a trade offer");
		}
	}

	@Override
	public Object maritimeTrade(int gameID, int playerID, int ratio, ResourceType inputResource, ResourceType outputResource)
			throws InvalidActionException {
		if(model.canTradeWithBank(gameID, playerID, ratio, inputResource, outputResource)) {
			model.makeMaritimeTrade(gameID, playerID, ratio, inputResource, outputResource);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot do maritime trade");
		}
	}

	@Override
	public Object robPlayer(int gameID, int playerID, HexLocation location, int victimIndex)
			throws InvalidActionException {
		model.robPlayer(gameID, playerID, victimIndex);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object finishTurn(int gameID) throws InvalidActionException {
		model.finishTurn(gameID);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object buyDevCard(int gameID, int playerID) throws InvalidActionException {
		if(model.canBuyDevelopmentCard(gameID, playerID)) {
			model.buyDevelopmentCard(gameID, playerID);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot buy a dev card");
		}
	}

	@Override
	public Object playSoldier(int gameID, int playerID, HexLocation location, int victimIndex)
			throws InvalidActionException {
		if(model.canPlaySoldier(gameID, playerID, location, victimIndex)) {
			model.playSoldierCard(gameID, playerID, location, victimIndex);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot play soldier card");
		}
	}

	@Override
	public Object playYearOfPlenty(int gameID, int playerID, ResourceType resource1, ResourceType resource2)
			throws InvalidActionException {
		if(model.canPlayYearOfPlenty(gameID, playerID, resource1, resource2)) {
			model.playYearOfPleanty(gameID, playerID, resource1, resource2);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot play year of plenty card");
		}
	}

	@Override
	public Object playRoadBuilding(int gameID, int playerID, EdgeLocation location1, EdgeLocation location2)
			throws InvalidActionException {
		if(model.canPlayRoadCard(gameID, playerID, location1, location2)) {
			model.playRoadCard(gameID, playerID, location1, location2);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot play road building card");
		}
	}

	@Override
	public Object playMonopoly(int gameID, int playerID, ResourceType resource) throws InvalidActionException {
		if(model.canPlayMonopolyCard(gameID, playerID, resource)) {
			model.playMonopolyCard(gameID, playerID, resource);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot play monopoly card");
		}
	}

	@Override
	public Object playMonument(int gameID, int playerID) throws InvalidActionException {
		if(model.canPlayMonumentCard(gameID, playerID)) {
			model.playMonumentCard(gameID, playerID);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot play monument card");
		}
	}

	
}
