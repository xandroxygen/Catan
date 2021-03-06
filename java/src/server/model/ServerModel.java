package server.model;

import client.admin.User;
import server.persistence.Persistence;
//import plugins.serialized.FileSerializer;
import server.persistence.Persistence;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ServerModel class
 */
public class ServerModel {
	private static ServerModel model;
    private ArrayList<User> users;
    private ArrayList<ServerGame> games;
    
    public ServerModel() {
        users = new ArrayList<>();
        games = new ArrayList<>();
    }
    
    public static ServerModel getInstance() {
		if (model == null) {
			model = new ServerModel();
		}
		return model;
	}
    
    /**
     * Checks whether the player can place a city.
     * @param playerIndex the ID of the player who is requesting the move
     * @param location The location of the city.
     * @return result
     */
    public boolean canPlaceCity(int gameId,int playerIndex, VertexLocation location){
    	return games.get(gameId).canPlaceCity(playerIndex, location);
	}

    /**
     * Checks whether the player can place a settlement.
     * @param playerIndex the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the settlement.
     * @return result
     */
    public boolean canPlaceSettlement(int gameId, int playerIndex, boolean free, VertexLocation location){
    	return games.get(gameId).canPlaceSettlement(playerIndex, free, location);
    }

    /**
     * Checks whether the player can place a road.
     * @param playerIndex the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the road.
     * @return result
     */
    public boolean canPlaceRoad(int gameId, int playerIndex, boolean free, EdgeLocation location) {
    	return games.get(gameId).canPlaceRoad(playerIndex, free, location);
    }

    //used for Build Roads DevCard
    public boolean canPlaceRoad(int gameId, int playerIndex, EdgeLocation firstLocation, EdgeLocation location){
    	return games.get(gameId).canPlaceRoad(playerIndex, firstLocation, location);
    }

    /**
     * Checks whether the player can buy a development card.
     * @return result
     */

    public boolean canBuyDevelopmentCard(int gameID, int playerId){
        return true;
    }
    
    /**
     * Checks whether a player can trade at all
     * @return
     */
    public boolean canTrade(int gameID) {
    	return true;
    }

    /**
     * Checks whether the player can trade with another player
     * @param offer list of Resources, Negative numbers mean you get those cards
     * @param recieverPlayerIndex the playerIndex of the offer recipient.
     * @return result
     */
    public boolean canTradeWithPlayer(int gameID, int senderPlayerIndex, int recieverPlayerIndex, Map<ResourceType, Integer> offer){
        return games.get(gameID).canTradeWithPlayer(senderPlayerIndex, recieverPlayerIndex, offer);
    }

    /**
     * Checks whether the player can trade using a port or directly to the bank.
     * @param ratio It must be a 2, 3, or 4.
     * @param inputResource Type of resource you are giving.
     * @param outputResource Type of resource you are receiving.
     * @return result
     */
    public boolean canTradeWithBank(int gameID, int playerId, int ratio, ResourceType inputResource, ResourceType outputResource){
        return true;
    }

    /**
     * Checks whether you can play a soldier development card
     * @param location New robber location.
     * @param victimIndex The playerIndex of the player you wish to rob, or -1 to rob no one.
     * @return result
     */
    public boolean canPlaySoldier(int gameID, int playerId, HexLocation location, int victimIndex) {
        return true;
    }

    /**
     * Checks whether you can play a year of plenty card.
     * @param resource1 The type of the first resource you'd like to receive
     * @param resource2 The type of the second resource you'd like to receive
     * @return result
     */
    public boolean canPlayYearOfPlenty(int gameID, int playerId, ResourceType resource1, ResourceType resource2) {
        return true;
    }

    /**
     * Checks whether a Road card can be played.
     * @param spot1 first edge location of road.
     * @param spot2 second edge location of road.
     * @return result
     */
    public boolean canPlayRoadCard(int gameID, int playerId, EdgeLocation spot1, EdgeLocation spot2) {
        return true;
    }

    /**
     * Checks whether a Monopoly card can be played.
     * @param resource The type of resource desired from other players.
     * @return result
     */
    public boolean canPlayMonopolyCard(int gameID, int playerId, ResourceType resource) {
        return true;
    }

    /**
     * Checks whether a Monument card can be played.
     * @return result
     */
    public boolean canPlayMonumentCard(int gameID, int playerId) {
        return true;
    }
    
    public boolean canCreateGame(String gameName) {
    	for (ServerGame g : games) {
    		if (g.getGameName().equals(gameName)) {
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * Places a City in the Game from the given gameID for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The city location is where you currently have a settlement, You have the required resources (2 wheat, 3 ore; 1 city)
     * @post You lost the resources required to build a city (2 wheat, 3 ore, 1 city), The city is on the map at the specified location, You got a settlement back on the desired location
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move.
     * @param location The location of the city.
     */
    public void placeCity(int gameID, int playerIndex, VertexLocation location){
        games.get(gameID).placeCity(playerIndex, location);
    }

    /**
     * Places a Settlement in the Game from the given gameID for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The settlement location is open, The settlement location is not on water, The settlement location is connected to one of your roads except during setup, You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement cannot be placed adjacent to another settlement
     * @post You lost the resources required to build a settlement (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement is on the map at the specified location
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the settlement.
     */
    public void placeSettlement(int gameID, int playerIndex, boolean free, VertexLocation location) {
    	games.get(gameID).placeSettlement(playerIndex,free,location);
    }

    /**
     * Places a Road in the Game from the given gameID for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The road location is open, The road location is connected to another road owned by the player, The road location is not on water, You have the required resources (1 wood, 1 brick; 1 road), Setup round: Must be placed by settlement owned by the player with no adjacent road.
     * @post You lost the resources required to build a road (1 wood, 1 brick - 1 road), The road is on the map at the specified location, If applicable, longest road has been awarded to the player with the longest road
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the road.
     */

    public void placeRoad(int gameID, int playerIndex, boolean free, EdgeLocation location) {
    	games.get(gameID).placeRoad(playerIndex,free,location);
    }

    /**
     * Buys a development card if the given player.
     * @pre It's your turn, You have the required resources (1 ore, 1 wheat, 1 sheep), There are dev cards left in the deck.
     * @post You have a new card; If it is a monument card, it has been added to your old devCard hand, If it is a non­monument card, it has been added to your new devCard hand (unplayable this turn)
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     */
    public void buyDevelopmentCard(int gameID, int playerIndex){
        games.get(gameID).buyDevelopmentCard(playerIndex);
    }

    /**
     * Places a soldier development card and moves the robber into the given location and robs the victim player if there is one
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		The robber is not being kept in the same location
     * 		If a player is being robbed (i.e., victimIndex != ­1)
     * 		The player being robbed has resource cards
     * 		</pre>
     * @post <pre>
     * 		The robber is in the new location
     * 		The player being robbed (if any) gave you one of his resource cards (randomly selected)
     * 		If applicable, largest army has been awarded to the player who has played the most soldier cards
     * 		You are not allowed to play other development cards during this turn (except for monument cards, which may still be played)
     * 	    Can not play another DevCard with the exception of the Monument Card
     * 		</pre>
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     * @param location the new robber location.
     * @param victimIndex The playerIndex of the player you wish to rob, or -1 to rob no one.
     */
    public void playSoldierCard(int gameID, int playerIndex, HexLocation location, int victimIndex){
        games.get(gameID).playSoldierCard(playerIndex, location, victimIndex);
    }

    /**
     * Plays a year of plenty devCard.
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		Two specified resources are in the bank.
     * 		</pre>
     * @post <pre>
     * 		You gained the two specified resources
     * 	    Can not play another DevCard with the exception of the Monument Card
     * 		</pre>
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     * @param resource1 The type of the first resource you'd like to receive
     * @param resource2 The type of the second resource you'd like to receive
     */
    public void playYearOfPleanty(int gameID, int playerIndex, ResourceType resource1, ResourceType resource2){
        games.get(gameID).playYearOfPlenty(playerIndex, resource1, resource2);
    }

    /**
     * Plays a road card.
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		The first road location (spot1) is connected to one of your roads.
     * 		The second road location (spot2) is connected to one of your roads or to the first road location (spot1)
     * 		Neither road location is on water
     * 		You have at least two unused roads
     * 		</pre>
     * @post <pre>
     * 		You have two fewer unused roads
     * 		Two new roads appear on the map at the specified locations
     * 		If applicable, longest road has been awarded to the player with the longest road
     * 	    Can not play another DevCard with the exception of the Monument Card
     * 		</pre>
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     * @param spot1 first edge location of road.
     * @param spot2 second edge location of road.
     */
    public void playRoadCard(int gameID, int playerIndex, EdgeLocation spot1, EdgeLocation spot2){
        games.get(gameID).playRoadCard(playerIndex, spot1, spot2);
    }

    /**
     * Plays a monopoly devCard.
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		</pre>
     * @post <pre>
     * 		All of the other players have given you all of their resource cards of the specified type
     * 	    Can not play another DevCard with the exception of the Monument Card
     * 		</pre>
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     * @param resource The type of resource desired from other players.
     */
    public void playMonopolyCard(int gameID, int playerIndex, ResourceType resource){
        games.get(gameID).playMonopolyCard(playerIndex, resource);
    }

    /**
     * Plays a monument devCard.
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		You have enough monument cards to win the game (i.e., reach 10 victory points)
     * 		</pre>
     * @post <pre>
     * 		You gained a victory point
     * 		</pre>
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     */
    public void playMonumentCard(int gameID, int playerIndex){
        games.get(gameID).playMonumentCard(playerIndex);
    }

    /**
     * Checks whether the player can roll the dice
     * @pre It's their turn, and the model status is ROLLING
     * @post distributes the correct amount of resources corresponding to the roll for every player
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     * @param rollValue the value that was rolled
     */
    public void rollDice(int gameID, int playerIndex,  int rollValue){
        games.get(gameID).rollDice(playerIndex, rollValue);
    }

    /**
     * Sends a chat message.
     * @post  The chat contains your message at the end.
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     * @param message the message the player wishes to send.
     */
    public void sendMessage(int gameID, int playerIndex, String message){
        games.get(gameID).sendMessage(playerIndex, message);
    }

    /**
     * Make a trade offer to another player.
     * @param gameID the ID of the game from which the request was made.
     * @param senderPlayerIndex Player offering the trade
     * @param receiverPlayerIndex Player being offered the trade
     * @param offer hand of cards to trade
     */
    public void makeTradeOffer(int gameID, int senderPlayerIndex, int receiverPlayerIndex, Map<ResourceType, Integer> offer){
        games.get(gameID).makeTradeOffer(senderPlayerIndex, receiverPlayerIndex, offer);
    }

    /**
     * Accept the TradeOffer currently on the table.
     * @param gameID the ID of the game from which the request was made.
     */
    public void acceptTradeOffer(int gameID, boolean willAccept){
        games.get(gameID).acceptTradeOffer(willAccept);
    }

    /**
     * Accept the TradeOffer currently on the table.
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     */
    public void makeMaritimeTrade(int gameID, int playerIndex, int ratio, ResourceType inputResource, ResourceType outputResource) {
        games.get(gameID).makeMaritimeTrade(playerIndex,ratio,inputResource,outputResource);
    }

    /**
     * Adds a player to the game.
     * @param gameID the ID of the game from which the request was made.
     * @param playerId the ID of the player who is requesting the move
     * @param color
     */
    public void addPlayer(int gameID, int playerId, CatanColor color){
    	User user = users.get(playerId);
        games.get(gameID).addPlayer(playerId,user.getUsername(),color);
    }

    /**
     * Adds an computer player to the game.
     * * * @pre <pre>
     * 		There are less then four players in the current game
     * 		</pre>
     * * @post <pre>
     *      there is a new computer player added to the game given by the gameID
     * 		</pre>
     * @param gameID the ID of the game from which the request was made.
     */
    public void addAIPlayer(int gameID){
        games.get(gameID).addAIPlayer();
    }

    /**
     * Ends the current players turn
     * * * @pre <pre>
     *     is called by the player whose turn it is
     *     the player is in the playing state
     * 		</pre>
     * * @post <pre>
     *     the current players turn is over and the next players is starting the game
     * 		</pre>
     * @param gameID the ID of the game from which the request was made.
     */
    public void finishTurn(int gameID){
        games.get(gameID).finishTurn();
    }

    /**
     * Robs a player
     * * * @pre <pre>
     *
     * 		</pre>
     * * @post <pre>
     *      Robs 1 resource from the victim player
     * 		</pre>
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     * @param victimIndex .
     * @param location 
     */
    public void robPlayer(int gameID, int playerIndex, int victimIndex, HexLocation location){
        games.get(gameID).robPlayer(playerIndex, victimIndex, location);
    }

    /**
     * Discards the given resources from the given player
     * * * @pre <pre>
     *      The player has sufficient resources to be able to discard
     * 		</pre>
     * * @post <pre>
     *      the given resources are discarded
     * 		</pre>
     * @param gameID the ID of the game from which the request was made.
     * @param playerIndex the ID of the player who is requesting the move
     */
    public void discardCards(int gameID, int playerIndex, Map<ResourceType, Integer> discardCards){
        games.get(gameID).discardCards(playerIndex, discardCards);
    }

    /**
     * Lists out all types of AI Players for a particular game
     * * @post <pre>
     *      If there are AI players it will return an array of their corresponding types
     * 		</pre>
     */
    public String[] listAIPlayers(){
        return new String[]{"LARGEST_ARMY"};
    }

    /**
     * Creates a new Game
     * * * @pre <pre>
     *
     * 		</pre>
     * * @post <pre>
     *      adds a new game to the list of games
     * 		</pre>
     */
    public ServerGame createGame(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String gameName) {
    	int id = games.size();
    	ServerGame game = new ServerGame(randomTiles,randomNumbers,randomPorts,gameName,id);
    	games.add(id,game);
        return game;
    }
    
    /**
     * Joins the user into specified game
     * @pre <pre>
     * 		Player is logged in
     * 		Player is already in game or there is room left in the game
     * 		The game id is valid
     * 		The color is valid
     * 		</pre>
     * @post <pre>
     * 		Returns 200
     * 		Player is now in game
     * 		Server responds with Set-cookie header
     */
    public void join(int playerId, int gameId, CatanColor color) {
    	User user = users.get(playerId);
        games.get(gameId).addPlayer(playerId,user.getUsername(),color);
    }

    /**
     * Returns an array of all of the games
     * * @post <pre>
     *      Returns an array of all of the games
     * 		</pre>
     */
    public ArrayList<ServerGame> listGames(){
    	return games;
    }
    
    /**
     * Returns the updated game
     * 
     * @param gameId the id of the updated game to retrieve
     * @param version the version number for comparing if the game has been updated since the last call
     * @return
     */
    public ServerGame getUpdatedGame(int gameId, int version) {
    	if(games.get(gameId).getVersion() > version)
    		return games.get(gameId);
    	else
    		return null;
    }

    /**
     * Adds a new user to the game.
     * * @pre <pre>
     * 		The username does not already exist
     * 		</pre>
     * * @post <pre>
     * 		There is a new User registered
     * 	    The new User is logged in
     * 		</pre>
     * @param username The username of the new User.
     * @param password The password of the new User
     * @return int playerId of new user
     */
    public int registerUser(String username, String password) {
        for (User tempUser : users) {
            if(tempUser.getUsername().equals(username)){
                return -1;
            }
        }
        User newUser = new User(username, password);
        users.add(newUser);
        Persistence.getInstance().getUserDAO().createUser(newUser);
        return users.size() - 1;
    }

    /**
     * Adds a new user to the game.
     * * @post <pre>
     * 	    The User is logged in if he already exists
     * 		</pre>
     * @param username The username of the User.
     * @param password The password of the User
     * @return true if login succeeded false if incorrect username/password were given
     */
    public int login(String username, String password){
    	int id = 0;
        for (User tempUser : users) {
            if(tempUser.getUsername().equals(username) && tempUser.getPassword().equals(password)){
                tempUser.setLoggedIn(true);
                return id;
            }
            id++;
        }
        return -1;
    }

    public ServerGame getGames(int i) {
        return games.get(i);
    }
    
    public List<User> getUsers() {
    	return users;
    }

	public void setUsers(ArrayList<User> users2) {
		users = users2;
	}
	
	public void setGames(ArrayList<ServerGame> games) {
		this.games = games;
	}
}
