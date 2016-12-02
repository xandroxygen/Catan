package plugins.relational;


import server.persistence.ICommandDAO;
import server.persistence.IGameDAO;
import server.persistence.IPersistenceProvider;
import server.persistence.IUserDAO;

public class PersistenceProvider implements IPersistenceProvider {
	@Override
	public void clearData() {

	}

	@Override
	public IGameDAO getGameDAO() {
		return null;
	}

	@Override
	public IUserDAO getUserDAO() {
		return null;
	}

	@Override
	public ICommandDAO getCommandDAO() {
		return null;
	}

	@Override
	public void startTransaction() {

	}

	@Override
	public void endTransaction() {

	}

	@Override
	public int returnFive() {
		// TODO Auto-generated method stub
		return 5;
	}
}
