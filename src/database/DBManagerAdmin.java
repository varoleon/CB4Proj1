package database;

import users.Role;

public class DBManagerAdmin extends DBManagerEditor {
	public DBManagerAdmin() {
		super();
	}
	
	public void insertNewUser(String name, String username, String password, Role role) {
		connect();
		String sql = "INSERT INTO USERS VALUES (NULL,'" + name + "','" + username + "','" + password
				+ "',(SELECT id FROM roles WHERE roleTitle='" + role.name() + "'))";
		modifyDB(sql);
		disconnect();
	}

	public void removeUserByUsername(String username) {
		connect();
		String sql = "DELETE FROM USERS WHERE username='" + username + "' ";
		modifyDB(sql);
		disconnect();
	}
}
