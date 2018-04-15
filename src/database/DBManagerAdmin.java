package database;

import users.Role;

public class DBManagerAdmin extends DBManagerEditor {
	public DBManagerAdmin() {
		super();
	}
	
	public int insertNewUser(String name, String username, String password, Role role) {
		connect();
		String sql = "INSERT INTO USERS VALUES (NULL,'" + name + "','" + username + "','" + password
				+ "',(SELECT id FROM roles WHERE roleTitle='" + role.name() + "'))";
		int res = modifyDB(sql);
		disconnect();
		return res;
	}

	public int removeUserByUsername(String username) {
		connect();
		String sql = "DELETE FROM USERS WHERE username='" + username + "' ";
		int res = modifyDB(sql);
		disconnect();
		return res;
	}
}
