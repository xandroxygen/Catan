package client.admin;

import client.data.GameInfo;
import client.data.PlayerInfo;
import client.model.InvalidActionException;
import client.server.IServerProxy;
import client.server.ServerProxy;
import com.google.gson.*;
import shared.definitions.CatanColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Superintendent, in charge of current user and all games.
 * Users may login and register here, and games can be created and joined.
 */
public class GameAdministrator {
    private User currentUser;
    private List<GameInfo> allCurrentGames;
    private IServerProxy server;

    public GameAdministrator() throws InvalidActionException {
        currentUser = null;
        allCurrentGames = new ArrayList<>();
        server = new ServerProxy(); // TODO this will need to change and serverProxy instance needs to be in the model
        fetchGameList(); 
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
            if (currentGame.getPlayers().size() > 3) {

                // game is full
                canJoinGame = false;
            }

            for (PlayerInfo player : currentGame.getPlayers()) {
                if (player.getName().equals(currentUser.getUsername()) ||
                        player.getColor().equals(userColor.toString().toLowerCase())) {
                    // player or color is already in game
                    canJoinGame = false;
                }
            }
        }
        return canJoinGame;
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
            currentUser.isLoggedIn = true;
            currentUser.setCookie(cookie);
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
            currentUser.isLoggedIn = true;
            currentUser.setCookie(cookie);
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
    public void createGame(String gameName, boolean rTiles, boolean rNumbers, boolean rPorts) throws InvalidActionException {
        try {
            server.gamesCreate(gameName, rTiles, rNumbers, rPorts);
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


    // --- HELPER FUNCTIONS ----

    /**
     * Fetches the list of current games from the server.
     * Called before canDo methods to make sure list of games is up to date
     * @throws InvalidActionException with updated message
     */
    private void fetchGameList() throws InvalidActionException {
        try {
            String jsonGames = server.gamesList();
            allCurrentGames = deserializeGameList(jsonGames);
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
        	GameInfo gameDetails = new Gson().fromJson(gameElement, GameInfo.class);
            games.add(gameDetails);
        }
        return games;
    }
    
    public List<GameInfo> getAllCurrentGames() {
    	return allCurrentGames;
    }
}
