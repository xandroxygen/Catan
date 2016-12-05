package server.facade;

import server.model.ServerGame;
import server.model.ServerModel;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.InvalidActionException;

import java.util.List;
import java.util.Map;

//import plugins.serialized.FileSerializer;

public class ServerFacade implements IServerFacade {
	
	private ServerModel model;
	
	public ServerFacade() {
		model = ServerModel.getInstance();
//		FileSerializer.readPlayers();
//		FileSerializer.readGames();
	}

	public ServerModel getModel() {
		return model;
	}

	@Override
	public int userLogin(String username, String password) throws InvalidActionException {
		int id = model.login(username, password);
		if(id >= 0) {
			return id; // TODO return playerID
		}
		else {
			throw new InvalidActionException("Cannot log in");
		}
	}

	@Override
	public int userRegister(String username, String password) throws InvalidActionException {
		int ID = model.registerUser(username, password);
		if(ID == -1){
			throw new InvalidActionException("User Already Exists");
		}
		return ID;
	}

	@Override
	public List<ServerGame> gamesList() throws InvalidActionException {
		return model.listGames();
	}

	@Override
	public ServerGame gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws InvalidActionException {
		if (!model.canCreateGame(name)) {
			throw new InvalidActionException("Game name already exists");
		}
		return model.createGame(randomTiles, randomNumbers, randomPorts, name);
	}

	@Override
	public String gamesJoin(int gameID, int playerIndex, CatanColor color) throws InvalidActionException {
		model.join(playerIndex, gameID, color);
		return "Success";
	}

	@Override
	public ServerGame gameGetModel(int gameID) throws InvalidActionException {
		return model.listGames().get(gameID);
	}

	@Override
	public ServerGame gameGetModel(int gameID, int version) throws InvalidActionException {
		return model.getUpdatedGame(gameID, version);
	}

	@Override
	public String[] gameListAI() throws InvalidActionException {
		return model.listAIPlayers();
	}

	@Override
	public Object gameAddAI(int gameID, String aiType) throws InvalidActionException {
		model.addAIPlayer(gameID);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object sendChat(int gameID, int playerIndex, String message) throws InvalidActionException {
		model.sendMessage(gameID, playerIndex, message);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object acceptTrade(int gameID, boolean willAccept) throws InvalidActionException {
		model.acceptTradeOffer(gameID, willAccept);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object discardCards(int gameID, int playerIndex, Map<ResourceType, Integer> hand)
			throws InvalidActionException {
		
		model.discardCards(gameID, playerIndex, hand);
	
		return model.listGames().get(gameID);
	}

	@Override
	public Object rollNumber(int gameID, int playerIndex, int rollValue) throws InvalidActionException {
		model.rollDice(gameID, playerIndex, rollValue);
		
			return model.listGames().get(gameID);
	}

	@Override
	public Object buildRoad(int gameID, int playerIndex, boolean isFree, EdgeLocation roadLocation)
			throws InvalidActionException {
		if(model.canPlaceRoad(gameID, playerIndex, isFree, roadLocation)) {
			model.placeRoad(gameID, playerIndex, isFree, roadLocation);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Error building a road");
		}
	}

	@Override
	public Object buildSettlement(int gameID, int playerIndex, boolean isFree, VertexLocation vertexLocation)
			throws InvalidActionException {
		if(model.canPlaceSettlement(gameID, playerIndex, isFree, vertexLocation)) {
			model.placeSettlement(gameID, playerIndex, isFree, vertexLocation);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Error building a settlement");
		}
	}

	@Override
	public Object buildCity(int gameID, int playerIndex, VertexLocation location) throws InvalidActionException {
		if(model.canPlaceCity(gameID, playerIndex, location)) {
			model.placeCity(gameID, playerIndex, location);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot build a city");
		}
	}

	@Override
	public Object offerTrade(int gameID, int senderIndex, int receiverIndex, Map<ResourceType, Integer> offer)
			throws InvalidActionException {
		if(model.canTrade(gameID) && model.canTradeWithPlayer(gameID, senderIndex, receiverIndex, offer)) {
			model.makeTradeOffer(gameID, senderIndex, receiverIndex, offer);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot make a trade offer");
		}
	}

	@Override
	public Object maritimeTrade(int gameID, int playerIndex, int ratio, ResourceType inputResource, ResourceType outputResource)
			throws InvalidActionException {
		if(model.canTradeWithBank(gameID, playerIndex, ratio, inputResource, outputResource)) {
			model.makeMaritimeTrade(gameID, playerIndex, ratio, inputResource, outputResource);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot do maritime trade");
		}
	}

	@Override
	public Object robPlayer(int gameID, int playerIndex, HexLocation location, int victimIndex)
			throws InvalidActionException {
		model.robPlayer(gameID, playerIndex, victimIndex, location);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object finishTurn(int gameID) throws InvalidActionException {
		model.finishTurn(gameID);
		
		return model.listGames().get(gameID);
	}

	@Override
	public Object buyDevCard(int gameID, int playerIndex) throws InvalidActionException {
		if(model.canBuyDevelopmentCard(gameID, playerIndex)) {
			model.buyDevelopmentCard(gameID, playerIndex);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot buy a dev card");
		}
	}

	@Override
	public Object playSoldier(int gameID, int playerIndex, HexLocation location, int victimIndex)
			throws InvalidActionException {
		if(model.canPlaySoldier(gameID, playerIndex, location, victimIndex)) {
			model.playSoldierCard(gameID, playerIndex, location, victimIndex);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot play soldier card");
		}
	}

	@Override
	public Object playYearOfPlenty(int gameID, int playerIndex, ResourceType resource1, ResourceType resource2)
			throws InvalidActionException {
		if(model.canPlayYearOfPlenty(gameID, playerIndex, resource1, resource2)) {
			model.playYearOfPleanty(gameID, playerIndex, resource1, resource2);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot play year of plenty card");
		}
	}

	@Override
	public Object playRoadBuilding(int gameID, int playerIndex, EdgeLocation location1, EdgeLocation location2)
			throws InvalidActionException {
		if(model.canPlayRoadCard(gameID, playerIndex, location1, location2)) {
			model.playRoadCard(gameID, playerIndex, location1, location2);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot play road building card");
		}
	}

	@Override
	public Object playMonopoly(int gameID, int playerIndex, ResourceType resource) throws InvalidActionException {
		if(model.canPlayMonopolyCard(gameID, playerIndex, resource)) {
			model.playMonopolyCard(gameID, playerIndex, resource);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot play monopoly card");
		}
	}

	@Override
	public Object playMonument(int gameID, int playerIndex) throws InvalidActionException {
		if(model.canPlayMonumentCard(gameID, playerIndex)) {
			model.playMonumentCard(gameID, playerIndex);
			
			return model.listGames().get(gameID);
		}
		else {
			throw new InvalidActionException("Cannot play monument card");
		}
	}

	
}
