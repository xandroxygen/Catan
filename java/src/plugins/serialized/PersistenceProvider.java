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
			
//			File file = new File("java\\src\\plugins\\relational");
//			URL[] jarUrl = new URL[]{new URL("file:"+file.getAbsolutePath())};
//			URLClassLoader urlClassLoader = new URLClassLoader(jarUrl);
//			
//			Class<?> g = Class.forName("plugins.serialized.GameDAO", true, urlClassLoader);
//			this.gameDAO = (IGameDAO) g.newInstance();
//			
//			Class<?> u = Class.forName("plugins.serialized.UserDAO", true, urlClassLoader);
//			this.userDAO = (IUserDAO) u.newInstance();
//			
//			Class<?> c = Class.forName("plugins.serialized.CommandDAO", true, urlClassLoader);
//			this.commandDAO = (ICommandDAO) c.newInstance();
//			
//			this.commandDAO.getCommandCount();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startTransaction() {
//		try {
//			dbHelper.startTransaction();
//		}
//		catch {
//			dbHelper.endTransaction(false);
//		}
	}

	@Override
	public void endTransaction() {
//		try {
//			dbHelper.endTransaction(true);
//		}
//		catch {
//			dbHelper.endTransaction(false);
//		}		
	}

	@Override
	public void clearData() {
		// TODO Auto-generated method stub
		
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
	public ICommandDAO getCommandDAO() {
		return gameDAO.getCommandDAO();
	}

	@Override
	public void setGameDAO(IGameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}

	@Override
	public void setCommandDAO(ICommandDAO commandDAO) {
		this.gameDAO.setCommandDAO(commandDAO);
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
