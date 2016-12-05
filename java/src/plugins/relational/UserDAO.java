package plugins.relational;


import client.admin.User;
import server.persistence.IUserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

	// --- SQL STATEMENTS ---
	private static final String INSERT = "INSERT INTO users(user) VALUES(?)";
	private static final String SELECT = "SELECT user FROM users";

	private String databaseName;

	public UserDAO() {
		databaseName = DatabaseHelper.DEFAULT_DATABASE;
	}

	/**
	 * Stores a user for the persistent provider
	 *
	 * @param user the user to add to storage
	 */
	@Override
	public void createUser(User user) {

		try(Connection connection = DatabaseHelper.getConnection(databaseName);
			PreparedStatement statement = connection.prepareStatement(INSERT)) {

			statement.setBytes(1, DatabaseHelper.getBlob(user));
			statement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns list of users from the persistent provider
	 *
	 * @return list of users
	 */
	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<>();

		try(Connection connection = DatabaseHelper.getConnection(databaseName);
			Statement statement = connection.createStatement()) {

			ResultSet result = statement.executeQuery(SELECT);

			while (result.next()) {
				byte[] blob = result.getBytes("user");
				User user = (User) DatabaseHelper.getObject(blob);
				users.add(user);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * @return the database being used
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * Used for mocking purposes to define a different database.
	 * @param databaseName
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
}
