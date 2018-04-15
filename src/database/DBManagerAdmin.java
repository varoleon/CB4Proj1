package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import users.Role;

public class DBManagerAdmin extends DBManagerEditor {
	public DBManagerAdmin() {
		super();
	}
	
	public int insertNewUser(String name, String username, String password, Role role) {
		connect();
		int res=0;
		String sql = "INSERT INTO users VALUES (NULL, ?, ?, ?, (SELECT id FROM roles WHERE roleTitle=?))";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, username);
			stmt.setString(3, password);
			stmt.setString(4, role.name());
			res = stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		disconnect();
		return res;
	}
}
