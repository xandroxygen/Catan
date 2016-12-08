package server.persistence;


public interface IPersistenceProvider {

    /**
     * Clears all information in the the database
     */
    public void clearData();

    public IGameDAO getGameDAO();

    public IUserDAO getUserDAO();

    public void setGameDAO(IGameDAO gameDAO);

    public void setUserDAO(IUserDAO userDAO);
    
    public int returnFive();
}
