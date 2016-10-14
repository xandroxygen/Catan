package client.admin;

import client.data.GameInfo;
import client.data.PlayerInfo;
import client.model.InvalidActionException;
import client.model.Model;
import client.server.IServerProxy;
import client.server.ServerPoller;

import com.google.gson.*;
import shared.definitions.CatanColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Superintendent, in charge of current user and all games.
 * Users may login and register here, and games can be created and joined.
 */
public class GameAdministrator extends Observable{
    private User currentUser;
    private GameInfo currentGame;
	private List<GameInfo> allCurrentGames;
    private IServerProxy server;
    private static GameAdministrator gameAdministrator;
    private ServerPoller poller;
    private boolean isSettingUp = true;

    private GameAdministrator() throws InvalidActionException {
        currentUser = new User();
        allCurrentGames = new ArrayList<>();
       	server = Model.getInstance().getServer();
        fetchGameList(); 
    }

    public static GameAdministrator getInstance() throws InvalidActionException {
        if (gameAdministrator == null) {
            gameAdministrator = new GameAdministrator();
        }
        return gameAdministrator;
    }
    
    /**
     * For testing
     * 
     * Set current user for testing on canCreateGame
     * Set server to MockServer to have access to a list of games
     */
    public GameAdministrator(User user, IServerProxy server) throws InvalidActionException {
        currentUser = user;
        allCurrentGames = new ArrayList<>();
       	this.server = server; // TODO this will need to change and serverProxy instance needs to be in the model
       	
        fetchGameList(); 
    }

    /**
     * Checks whether user can login with given username and password.
     * @param username String of name user wishes to register.
     * @param password Desired password.
     * @pre Username and password are not null
     * @return true if user can login
     */
    public boolean canLogin(String username, String password) {
        return (username != null && password != null);
    }

    /**
     * Checks whether user can register a given username.
     * @param username String of name user wishes to register.
     * @param password Desired password.
     * @pre Username and password are not null; Username is not registered already
     * @return true if user can register
     * @throws InvalidActionException if fetching game list fails
     */
    public boolean canRegister(String username, String password) throws InvalidActionException {
        if (username != null && password != null) {
            boolean nameIsNotTaken = true;
            fetchGameList();
            for (GameInfo game : allCurrentGames) {
                for (PlayerInfo player : game.getPlayers()) {
                    if (player.getName().equals(username)) {
                        nameIsNotTaken = false;
                    }
                }
            }
            return nameIsNotTaken;
        }
        return false;
    }

    /**
     * Checks whether logged-in user can create game.
     * @pre User is logged in; game name is not null; random options are valid boolean
     * @param gameName name of the game
     * @param rTiles true if tiles should be randomized
     * @param rNumbers true if numbers should be randomized
     * @param rPorts true if ports should be randomized
     * @return true if user can create game
     */
    public boolean canCreateGame(String gameName, boolean rTiles, boolean rNumbers, boolean rPorts) {
        boolean canCreateGame = true;
        canCreateGame = currentUser.isLoggedIn;
        canCreateGame = (gameName != null) && canCreateGame;
        // booleans are valid booleans by default?
        return canCreateGame;
    }

    /**
     * Checks whether logged-in user can join game.
     * @param gameID ID of game to join
     * @param userColor Color of player in game
     * @pre User is logged in; User is already in game OR there is space in game for them; game ID is valid; color is valid
     * @return true if user can join game
     * @throws InvalidActionException if fetching game list fails
     */
    public boolean canJoinGame(int gameID, CatanColor userColor) throws InvalidActionException {

        boolean canJoinGame = true;
        canJoinGame = currentUser.isLoggedIn;
        fetchGameList();

        if (gameID >= allCurrentGames.size() || gameID < 0) {

            // invalid gameID
            canJoinGame = false;
        }
        else {

        	GameInfo currentGame = allCurrentGames.get(gameID);
        	
            if (currentGame.getPlayers().size() > 3 && !alreadyInGame()) {

                // game is full
                canJoinGame = false;
            }

            for (PlayerInfo player : currentGame.getPlayers()) {
                if (player.getColor().equals(userColor.toString().toLowerCase()) && !isUsersColor(player)) {
                    // player or color is already in game
                    canJoinGame = false;
                }
            }
        }
        return canJoinGame;
    }
    
    /**
     * Ensures that the current game has room for an AI to be added to it
     */
    public boolean canAddAI() {
    	try {
			fetchGameList();
			return currentGame != null && currentGame.getPlayers().size() < 4;
		} catch (InvalidActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    	
    }

    /**
     * Logs a user into the Catan game.
     * @pre Should only be called once canLogin has returned true.
     * @param username
     * @param password
     * @throws InvalidActionException if login fails for any reason.
     */
    public void login(String username, String password) throws InvalidActionException {
        try {

            String cookie = server.userLogin(username, password);
            currentUser.setUsername(username);
            currentUser.setPassword(password);
            currentUser.isLoggedIn = true;
            currentUser.setCookie(cookie);
            currentUser.createLocalPlayer();
            poller = new ServerPoller(server);
           	poller.start();
        }
        catch (InvalidActionException e) {
            e.message = "Login failed.";
            throw e;
        }

    }

    /**
     * Registers a user with the Catan game.
     * @pre Should only be called once canRegister has returned true.
     * @param username
     * @param password
     * @throws InvalidActionException
     */
    public void register(String username, String password) throws InvalidActionException {
        try {
            String cookie = server.userRegister(username, password);
            currentUser.setUsername(username);
            currentUser.setPassword(password);
            currentUser.isLoggedIn = true;
            currentUser.setCookie(cookie);
            currentUser.createLocalPlayer();
            
            poller = new ServerPoller(server);
           	poller.start();
        }
        catch (InvalidActionException e) {
            e.message = "Register failed";
            throw e;
        }
    }

    /**
     * Creates a new game with name and options passed in.
     * @pre Should only be called once canCreateGame has returned true.
     * @param gameName name of the game
     * @param rTiles true if tiles should be randomized
     * @param rNumbers true if numbers should be randomized
     * @param rPorts true if ports should be randomized
     * @throws InvalidActionException
     */
    public GameInfo createGame(String gameName, boolean rTiles, boolean rNumbers, boolean rPorts) throws InvalidActionException {
        try {
            String gameInfo = server.gamesCreate(gameName, rTiles, rNumbers, rPorts);
            JsonObject newModel = new JsonParser().parse(gameInfo).getAsJsonObject();
            
            int id  = newModel.get("id").getAsInt();
            String title = newModel.get("title").getAsString();
            GameInfo g = new GameInfo(title, id);
            return g;
        }
        catch (InvalidActionException e) {
            e.message = "Game creation failed.";
            throw e;
        }
    }

    /**
     * Joins existing game with the color provided.
     * Sets the game cookie for the server to use.
     * @pre Should only be called once canJoinGame has returned true.
     * @param gameID ID of game to join.
     * @param userColor Color that user wants to be.
     * @throws InvalidActionException
     */
    public void joinGame(int gameID, CatanColor userColor) throws InvalidActionException {
        try {
            String cookie = server.gamesJoin(gameID, userColor);
            for (GameInfo game : allCurrentGames) {
                if (gameID == game.getId()) {
                    game.setCookie(cookie);
                }
            }
        }
        catch (InvalidActionException e) {
            e.message = "Joining the game failed.";
            throw e;
        }
    }

    /**
     * Gets the list of possible AI Types from the server
     * @pre Player is logged in, has the right cookie, and has joined a game
     */
    public String[] getAIList() {
    	try {
    		// Get type list from the server
			String response = server.gameListAI();
			JsonArray listAIArray = new JsonParser().parse(response).getAsJsonArray();
			
			// Convert response json into an array
			ArrayList<String> types = new ArrayList<>();
			for (JsonElement aIType : listAIArray) {
				types.add(aIType.getAsString());
			}
			String[] values = new String[types.size()];
			return types.toArray(values);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * Adds an AI to the current game
     * @throws InvalidActionException 
     * @param aIType the type of AI to add
     */
    public void addAI(String aIType) throws InvalidActionException {
    	if (canAddAI()) {
    		server.gameAddAI(aIType);
    	}
    }

    // --- HELPER FUNCTIONS ----

    /**
     * Fetches the list of current games from the server.
     * Called before canDo methods to make sure list of games is up to date
     * @throws InvalidActionException with updated message
     */
    public void fetchGameList() throws InvalidActionException {
        try {
            String jsonGames = server.gamesList();
            allCurrentGames = deserializeGameList(jsonGames);
            
            // If currently in a game, ensure the variable is updated
            if (currentGame != null) {
				currentGame = allCurrentGames.get(currentGame.getId());
            }
            
            setChanged();
            notifyObservers(allCurrentGames);
        }
        catch (InvalidActionException e) {
            e.message = "Fetch of games list failed.";
            throw e;
        }
    }

    /**
     * Deserializes the list of games and returns list of Game objects.
     * @param jsonList JSON string of list of games
     * @return list of GameDetail objects
     */
    private List<GameInfo> deserializeGameList(String jsonList) {
        List<GameInfo> games = new ArrayList<>();
        JsonArray jsonGames = new JsonParser().parse(jsonList).getAsJsonArray();
        for (JsonElement gameElement : jsonGames) {
            games.add(new GameInfo(gameElement.getAsJsonObject()));
        }
        return games;
    }
    
    /**
     * Checks if the current user is already in the game he is trying to join
     * @return true if already a member of the game, false if not
     */
    private boolean alreadyInGame() {
    	for(PlayerInfo p : currentGame.getPlayers()) {
    		if(currentUser.getUsername().equals(p.getName())) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    private boolean isUsersColor(PlayerInfo player) {
    	if(currentUser.getLocalPlayer().getColor().equals(player.getColor().toString().toLowerCase())) {
    		return true;
    	}
    	else {
    		return false;
    	}	
    }
    
    public List<GameInfo> getAllCurrentGames() {
    	return allCurrentGames;
    }
    
    public GameInfo getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(GameInfo currentGame) {
		this.currentGame = currentGame;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isSettingUp() {
		return isSettingUp && currentUser != null;
	}

	public void setSettingUp(boolean isSettingUp) {
		this.isSettingUp = isSettingUp;
	}
}
