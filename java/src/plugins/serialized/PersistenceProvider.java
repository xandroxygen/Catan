package plugins.serialized;

import server.persistence.ICommandDAO;
import server.persistence.IGameDAO;
import server.persistence.IPersistenceProvider;
import server.persistence.IUserDAO;

public class PersistenceProvider implements IPersistenceProvider{

	@Override
	public void clearData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IGameDAO getGameDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUserDAO getUserDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICommandDAO getCommandDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int returnFive() {
		return 5;
	}
}
