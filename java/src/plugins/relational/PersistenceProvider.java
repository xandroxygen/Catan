package plugins.relational;


import server.persistence.ClassLoader;
import server.persistence.ICommandDAO;
import server.persistence.IGameDAO;
import server.persistence.IPersistenceProvider;
import server.persistence.IUserDAO;

public class PersistenceProvider implements IPersistenceProvider {

	private IGameDAO gameDAO;
	private IUserDAO userDAO;

	private static final String  FILE_PATH = "java\\src\\plugins\\relational";
	private static final String GAME_DAO_PATH = "plugins.relational.GameDAO";
	private static final String USER_DAO_PATH = "plugins.relational.UserDAO";
	private static final String COMMAND_DAO_PATH = "plugins.relational.CommandDAO";

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
		DatabaseHelper.reset();
	}

	@Override
	public IGameDAO getGameDAO() {
		return this.gameDAO;
	}

	public IUserDAO getUserDAO() {
		return this.userDAO;
	}
	
	public void setGameDAO(IGameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}

	@Override
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@Override
	public int returnFive() {
		// TODO Auto-generated method stub
		return 5;
	}
}
