package p0;

public class Admin extends Editor {

	public Admin(int id, String name, String username, String password) {
		super(id, name, username, password);
		this.role = Role.ADMIN;
	}
	
	public boolean registerUser(String name,String username , String password, Role role) {
		Database db= Database.getDbInst(); 
		// Register process
		User user =db.getUserByUsername(username);
		if (user==null) {
			db.insertNewUser(name, username , password, role);
			return true;
		}
		
		return false;
	}
	
	public boolean removeUser(String username) {
		Database db= Database.getDbInst(); 
		
		if (db.isUsernameInUse(username)) {
			db.removeUserByUsername(username);
			return true;
		}
		
		return false;
	}
	
}
