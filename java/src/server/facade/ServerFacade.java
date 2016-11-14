package server.facade;

import java.util.HashMap;

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
	public int userLogin(String username, String password) throws InvalidActionException {
		if(model.login(username, password)) {
			return 0; // TODO return playerID
		}
		else {
			throw new InvalidActionException("Error");
		}
	}

	@Override
	public int userRegister(String username, String password) throws InvalidActionException {
		//TODO: need a canDo here?
		return model.registerUser(username, password);
	}

	@Override
	public Game[] gamesList() throws InvalidActionException {
		return model.listGames();
	}

	@Override
	public String gamesCreate(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws InvalidActionException {
		model.createGame(randomTiles, randomNumbers, randomPorts, name);
		
		//TODO: what will be returned here?
		/* 
		 * Swagger page return type
		 *{
			 "title": "asdfs",
			 "id": 3,
			 "players": [
			    {},
			    {},
			    {},
			    {}
			 ]
		  } 
		 */
		return null;
	}

	@Override
	public String gamesJoin(int gameID, CatanColor color) throws InvalidActionException {
		//TODO: need a canDo here?
		
		//if(model.joinGame()) {
		//	return "Success";
		//}
		//else {
		//	return "The player could not be added to the specified game.";	
		//}
		
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
		// TODO: do we need a version number in the model?
		return null;
	}

	@Override
	public String[] gameListAI(int gameID) throws InvalidActionException {
		return model.listAIPlayers(gameID);
	}

	@Override
	public Object gameAddAI(int gameID, String aiType) throws InvalidActionException {
		model.addComputerPlayer(gameID);
		
		//TODO: return model
		return null;
	}

	@Override
	public Object sendChat(int gameID, int playerID, String message) throws InvalidActionException {
		model.sendMessage(gameID, playerID, message);
		
		//TODO: return model
		return null;
	}

	@Override
	public Object acceptTrade(int gameID, boolean willAccept) throws InvalidActionException {
		model.acceptTradeOffer(gameID, willAccept);
		
		//TODO: return model
		
		//TODO: need another return type?
		return null;
	}

	@Override
	public Object discardCards(int gameID, int playerID, HashMap<ResourceType, Integer> hand)
			throws InvalidActionException {
		model.discardCards(gameID, playerID, hand);
		//TODO: return the model
		return null;
	}

	@Override
	public Object rollNumber(int gameID, int playerID, int rollValue) throws InvalidActionException {
		model.rollDice(gameID, playerID, rollValue);
		//TODO: return the model
		return null;
	}

	@Override
	public Object buildRoad(int gameID, int playerID, boolean isFree, EdgeLocation roadLocation)
			throws InvalidActionException {
		if(model.canPlaceRoad(gameID, playerID, isFree, roadLocation)) {
			model.placeRoad(gameID, playerID, isFree, roadLocation);
			//TODO: return model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	@Override
	public Object buildSettlement(int gameID, int playerID, boolean isFree, VertexLocation vertexLocation)
			throws InvalidActionException {
		if(model.canPlaceSettlement(gameID, playerID, isFree, vertexLocation)) {
			model.placeSettlement(gameID, playerID, isFree, vertexLocation);
			//TODO return model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	@Override
	public Object buildCity(int gameID, int playerID, VertexLocation location) throws InvalidActionException {
		if(model.canPlaceCity(gameID, playerID, location)) {
			model.placeCity(gameID, playerID, location);
			//TODO: return a model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	@Override
	public Object offerTrade(int gameID, int senderID, int receiverID, HashMap<ResourceType, Integer> offer)
			throws InvalidActionException {
		if(model.canTrade(gameID) && model.canTradeWithPlayer(gameID, senderID, receiverID, offer)) {
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
		if(model.canTradeWithBank(gameID, playerID, ratio, inputResource, outputResource)) {
			model.makeMaritimeTrade(gameID, playerID, ratio, inputResource, outputResource);
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
		//TODO: return the model		
		return null;
	}

	@Override
	public Object buyDevCard(int gameID, int playerID) throws InvalidActionException {
		if(model.canBuyDevelopmentCard(gameID, playerID)) {
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
		if(model.canPlaySoldier(gameID, playerID, location, victimIndex)) {
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
		if(model.canPlayYearOfPlenty(gameID, playerID, resource1, resource2)) {
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
		if(model.canPlayRoadCard(gameID, playerID, location1, location2)) {
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
		if(model.canPlayMonopolyCard(gameID, playerID, resource)) {
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
		if(model.canPlayMonumentCard(gameID, playerID)) {
			model.playMonumentCard(gameID, playerID);
			//TODO: return model
			return null;
		}
		else {
			throw new InvalidActionException("");
		}
	}

	
}
