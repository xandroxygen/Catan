package server.persistence;


import client.admin.User;
import server.command.Command;
import server.model.ServerGame;

import java.util.List;

public class PersistenceProvider {
    private IUserDAO userDao;
    private IGameDAO gameDao;
    private ICommandDAO commandDao;

    /**
     * Plays a monument devCard.
     * @pre <pre>
     * 		Pre-Conditions
     * 		</pre>
     * @post <pre>
     * 		Post-Condition
     * 		</pre>
     */
    public void clearData(){

    }

    /**
     * Stores a user for the persistent provider
     * @param user the user to add to storage
     */
    public void createUser(User user){}

    /**
     * Returns list of users from the persistent provider
     * @return list of users
     */
    public List<User> getUsers(){
        return null;
    }

    /**
     * Stores a game into the persistent provider
     * @param game the game to add to storage
     */
    public void createGame(ServerGame game){}

    /**
     * Returns list of games from the persistent provider
     */
    public List<ServerGame> getGames(){
        return null;
    }

    /**
     * Stores command into the persistent provider
     */
    public void addCommand(){}

    /**
     * Returns a list of commands from the persistent provider
     * @param gameID id of the game
     * @return list of commands
     */
    public List<Command> getCommands(int gameID){
        return null;
    }


    public void getGameDAO(){

    }

    public void getUserDAO(){

    }

    public void getCommandDAO(){

    }

    public void startTransaction(){

    }

    public void endTransaction(){

    }

    private void saveGame(){

    }

    public void populateModel(){

    }

    private void loadUsers(){

    }

    private void loadGames(){

    }

    private void loadGame(){

    }
}
