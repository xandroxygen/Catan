package server.persistence;


public interface IPersistenceProvider {

    public void clearData();

    public void getGameDAO();

    public void getUserDAO();

    public void getCommandDAO();

    public void startTransaction();

    public void endTransaction();

}
