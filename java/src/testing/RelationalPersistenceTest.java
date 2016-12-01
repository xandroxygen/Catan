package testing;

import client.data.PlayerInfo;
import org.junit.Test;
import static org.junit.Assert.*;
import plugins.relational.DatabaseHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 */
public class RelationalPersistenceTest {


	@Test
	public void testDatabaseConnection() {

		try (Connection conn = DatabaseHelper.getConnection()) {

			System.out.print("Database Connection established");
			assertTrue(true);
		}
		catch (SQLException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testDatabaseCreation() {

		DatabaseHelper.reset();

		try (Connection connection = DatabaseHelper.getConnection()) {

			ResultSet games = connection.getMetaData().getTables(null, null, "games", null);
			assertTrue(games.next());
		}
		catch (SQLException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testObjectBlobCasting() {

		PlayerInfo test = new PlayerInfo(1,1,"test",null);
		byte[] blob = DatabaseHelper.getBlob(test);

		assertNotNull(blob);

		PlayerInfo cast = (PlayerInfo) DatabaseHelper.getObject(blob);

		assertNotNull(cast);
		assertEquals(test.getName(), cast.getName());
	}
}
