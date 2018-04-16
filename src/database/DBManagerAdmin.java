package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManagerAdmin extends DBManagerEditor {
	public DBManagerAdmin() {
		super();
	}

	public int insertNewUser(String name, String username, String password, String role) {
		connect();
		int res = 0;
		String sql = "INSERT INTO users VALUES (NULL, ?, ?, ?, (SELECT id FROM roles WHERE roleTitle=?))";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, username);
			stmt.setString(3, password);
			stmt.setString(4, role);
			res = stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		return res;
	}

	public int removeUserByUsername(String username) {
		connect();
		int res = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE username=?");
			stmt.setString(1, username);
			res = stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return res;
	}

	public int updateUser(String username, String password, String name, String role) {
		connect();
		int res = 0;

		try {
			String sql = "UPDATE users SET users.password= ?, " + "users.name= ?, "
					+ "users.role= (SELECT id FROM roles WHERE roleTitle=?) " + "WHERE users.username=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, password);
			stmt.setString(2, name);
			stmt.setString(3, role);
			stmt.setString(4, username);
			res = stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return res;
	}
}
