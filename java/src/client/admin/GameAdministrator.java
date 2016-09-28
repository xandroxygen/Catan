package client.admin;

import client.admin.User;
import client.model.Game;
import client.model.InvalidActionException;
import shared.definitions.CatanColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Superintendent, in charge of current user and all games.
 * Users may login and register here, and games can be created and joined.
 */
public class GameAdministrator {
    private User currentUser;
    private List<GameDetails> allCurrentGames;

    public GameAdministrator() {
        currentUser = null;
        allCurrentGames = new ArrayList<>();
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
     */
    public boolean canRegister(String username, String password) {
        if (username != null && password != null) {
            // check if user isn't registered already ie in all current games, there are no users with that name
            return true;
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
        return false;
    }

    /**
     * Checks whether logged-in user can join game.
     * @param gameID ID of game to join
     * @param userColor Color of player in game
     * @pre User is logged in; User is already in game OR there is space in game for them; game ID is valid; color is valid
     * @return true if user can join game
     */
    public boolean canJoinGame(int gameID, CatanColor userColor) {
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

    }

    /**
     * Registers a user with the Catan game.
     * @pre Should only be called once canRegister has returned true.
     * @param username
     * @param password
     * @throws InvalidActionException
     */
    public void register(String username, String password) throws InvalidActionException {

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

    }

    /**
     * Joins existing game with the color provided.
     * @pre Should only be called once canJoinGame has returned true.
     * @param gameID ID of game to join.
     * @param userColor Color that user wants to be.
     * @throws InvalidActionException
     */
    public void joinGame(int gameID, CatanColor userColor) throws InvalidActionException {

    }


    // --- HELPER FUNCTIONS ----

    /**
     * Fetches the list of current games from the server.
     * @throws InvalidActionException
     */
    private void fetchGameList() throws InvalidActionException {

    }

    /**
     * Deserializes the list of games and returns list of Game objects.
     * TODO: Game should actually be separate class GameInfo or something, since the json isn't a game model but info
     * about the game itself.
     */
    private List<GameDetails> deserializeGameList(String jsonList) {

        return null;
    }

}
