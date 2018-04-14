package users;

import database.DBManagerAdmin;

public class Admin extends Editor {
	private DBManagerAdmin dbm;
	
	public Admin(int id, String name, String username, String password) {
		super(id, name, username, password);
		this.role = Role.ADMIN;
		this.dbm= new DBManagerAdmin();
	}
	
	public int registerUser(String name,String username , String password, Role role) {
		// Register process
		return dbm.insertNewUser(name, username , password, role);
	}
	
	public int removeUser(String username) {	
		return dbm.removeUserByUsername(username);
	}
	
}
