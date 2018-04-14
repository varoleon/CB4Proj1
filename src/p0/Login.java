package p0;

public class Login {
	private String errorMessage=null;
	private User loggedInUser =null;
	private boolean isLoggedIn=false;	
	private Database db;

	Login(){
		db=Database.getDbInst();
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String getNameLoggedInUser() {
		return loggedInUser.name;
	}
	public Role getRoleLoggedInUser() {
		return loggedInUser.role;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public boolean login(String username, String password) {
		// Login process
		User user =db.getUserByUsername(username);
		if (user!=null) {
			if(db.checkPassword(user.id,password)) {
				loggedInUser= user;
				isLoggedIn= true;
				return true;
			}else {
				errorMessage="Wrong Password for user "+user.username;
			}
		}else {
			errorMessage="User "+username+" not found.";
		}
		
		return false;
		
	}
	
	public void logout() {
		loggedInUser=null;
		isLoggedIn=false;
	}

}
