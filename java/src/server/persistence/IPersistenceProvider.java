package server.persistence;


public interface IPersistenceProvider {

    /**
     * @return the singleton IPersistenceProvider
     */
    public IPersistenceProvider getInstance();

    /**
     * Clears all information in the the database
     */
    public void clearData();

    /**
     * @return the GameDAO
     */
    public IGameDAO getGameDAO();

    /**
     * @return the UserDAO
     */
    public IUserDAO getUserDAO();

    /**
     * @return the CommandDAO
     */
    public ICommandDAO getCommandDAO();

    public void setGameDAO(IGameDAO gameDAO);

    public void setCommandDAO(ICommandDAO commandDAO);

    public void setUserDAO(IUserDAO userDAO);

    /**
     * Starts the transaction
     */
    public void startTransaction();

    /**
     * ends the transaction
     */
    public void endTransaction();
    
    public int returnFive();
}
