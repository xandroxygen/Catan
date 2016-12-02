package plugins.relational;


import client.admin.User;
import server.persistence.IUserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRelationalDAO implements IUserDAO {

	// --- SQL STATEMENTS ---
	private static final String INSERT = "INSERT INTO users(user) VALUES(?)";
	private static final String SELECT = "SELECT user FROM users";

	/**
	 * Stores a user for the persistent provider
	 *
	 * @param user the user to add to storage
	 */
	@Override
	public void createUser(User user) {

		try(Connection connection = DatabaseHelper.getConnection();
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

		try(Connection connection = DatabaseHelper.getConnection();
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
}