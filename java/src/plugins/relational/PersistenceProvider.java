package plugins.relational;


import server.persistence.ICommandDAO;
import server.persistence.IGameDAO;
import server.persistence.IPersistenceProvider;
import server.persistence.IUserDAO;

public class PersistenceProvider implements IPersistenceProvider {

	private GameDAO gameDAO;
	private CommandDAO commandDAO;
	private UserDAO userDAO;

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
