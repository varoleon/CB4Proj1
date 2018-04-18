package users;

import database.DBManagerAdmin;

public class Admin extends Editor {
	private DBManagerAdmin dbm;

	public Admin(int id, String name, String username, String password) {
		super(id, name, username, password);
		this.role = Role.ADMIN;
		this.dbm = new DBManagerAdmin();
	}

	public int registerUser(String name, String username, String password, Role role) {
		return dbm.insertNewUser(name, username, password, role.name());
	}

	public int removeUser(String username) {
		if (username.equals(this.username))
			return 0;
		return dbm.removeUserByUsername(username);
	}

	public int updateUser(String username, String password, String name, Role role) {
		return dbm.updateUser(username, password, name, role.name());
	}

}
