package plugins.relational;


import server.persistence.ICommandDAO;
import server.persistence.IGameDAO;
import server.persistence.IPersistenceProvider;
import server.persistence.IUserDAO;

public class PersistenceProvider implements IPersistenceProvider {

	private DatabaseHelper database;
	private CommandRelationalDAO commandDAO;
	private GameRelationalDAO gameDAO;
	private UserRelationalDAO userDAO;

	public RelationalPersistenceProvider() {

		database = new DatabaseHelper();
		commandDAO = new CommandRelationalDAO();
		gameDAO = new GameRelationalDAO();
		userDAO = new UserRelationalDAO();
	}

	/**
	 * Clears all information in the the database
	 */
	@Override
	public void clearData() {

	}

	/**
	 * @return the GameDAO
	 */
	@Override
	public IGameDAO getGameDAO() {
		return null;
	}

	/**
	 * @return the UserDAO
	 */
	@Override
	public IUserDAO getUserDAO() {
		return null;
	}

	/**
	 * @return the CommandDAO
	 */
	@Override
	public ICommandDAO getCommandDAO() {
		return null;
	}

	/**
	 * Starts the transaction
	 */
	@Override
	public void startTransaction() {

	}

	/**
	 * ends the transaction
	 */
	@Override
	public void endTransaction() {

	}
}
