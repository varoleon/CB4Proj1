package users;

import database.DBManagerAdmin;

public class Admin extends Editor {
	private DBManagerAdmin dbm;
	
	public Admin(int id, String name, String username, String password) {
		super(id, name, username, password);
		this.role = Role.ADMIN;
		this.dbm= new DBManagerAdmin();
	}
	
	public boolean registerUser(String name,String username , String password, Role role) {
		// Register process
		User user =dbm.getUserByUsername(username);
		if (user==null) {
			dbm.insertNewUser(name, username , password, role);
			return true;
		}
		
		return false;
	}
	
	public boolean removeUser(String username) {
		
		if (dbm.isUsernameInUse(username)) {
			dbm.removeUserByUsername(username);
			return true;
		}
		
		return false;
	}
	
}
