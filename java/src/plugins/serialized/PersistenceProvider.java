package plugins.serialized;

import server.persistence.ClassLoader;
import server.persistence.*;

public class PersistenceProvider implements IPersistenceProvider {
	private IGameDAO gameDAO;
	private IUserDAO userDAO;
	
	private final String  FILE_PATH = "java\\src\\plugins\\serialized";
	private final String GAME_DAO_PATH = "plugins.serialized.GameDAO";
	private final String USER_DAO_PATH = "plugins.serialized.UserDAO";
	private final String COMMAND_DAO_PATH = "plugins.serialized.CommandDAO";

	IPersistenceProvider persistenceProvider;

	public PersistenceProvider() {
		buildDAOs();
	}
	
	private void buildDAOs() {
		try {
			Class<?> c = ClassLoader.loadClass(FILE_PATH, GAME_DAO_PATH);
			this.gameDAO = (IGameDAO) c.newInstance();
			
			c = ClassLoader.loadClass(FILE_PATH, USER_DAO_PATH);
			this.userDAO = (IUserDAO) c.newInstance();
			
			c = ClassLoader.loadClass(FILE_PATH, COMMAND_DAO_PATH);
			this.gameDAO.setCommandDAO((ICommandDAO) c.newInstance());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clearData() {
		this.gameDAO.reset();
		this.userDAO.reset();
	}

	@Override
	public IGameDAO getGameDAO() {
		return gameDAO;
	}

	@Override
	public IUserDAO getUserDAO() {
		return userDAO;
	}

	@Override
	public void setGameDAO(IGameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}

	@Override
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public int returnFive() {
		return 5;
	}
}
