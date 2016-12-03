package plugins.serialized;

import server.persistence.ICommandDAO;
import server.persistence.IGameDAO;
import server.persistence.IPersistenceProvider;
import server.persistence.IUserDAO;

public class PersistenceProvider implements IPersistenceProvider{

	private GameDAO gameDAO;
	private CommandDAO commandDAO;
	private UserDAO userDAO;

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
	public void setGameDAO(IGameDAO gameDAO) {
		this.gameDAO = (GameDAO) gameDAO;
	}

	@Override
	public void setCommandDAO(ICommandDAO commandDAO) {
		this.commandDAO = (CommandDAO) commandDAO;
	}

	@Override
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = (UserDAO) userDAO;
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
