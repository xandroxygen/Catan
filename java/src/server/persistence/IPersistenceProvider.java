package server.persistence;


public interface IPersistenceProvider {

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

    /**
     * Starts the transaction
     */
    public void startTransaction();

    /**
     * ends the transaction
     */
    public void endTransaction();

}
