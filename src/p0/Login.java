package p0;

import database.DBManager;
import users.Role;
import users.User;

public class Login {
	private String errorMessage = null;
	private User loggedInUser = null;
	private boolean isLoggedIn = false;

	private DBManager dbm = new DBManager();

	Login() {
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getNameLoggedInUser() {
		return loggedInUser.getName();
	}

	public Role getRoleLoggedInUser() {
		return loggedInUser.getRole();
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public boolean login() {
		// Login process
		System.out.print("Username : ");
		String username = Menu.sc.nextLine();
		System.out.print("Password : ");
		String password = Menu.sc.nextLine();

		User user = dbm.getUserByUsername(username);
		if (user != null) {
			if (user.getPassword().equals(password)) {
				loggedInUser = user;
				isLoggedIn = true;
				return true;
			} else {
				errorMessage = "Wrong Password for user " + username;
			}
		} else {
			errorMessage = "User " + username + " not found.";
		}

		return false;

	}

	public void logout() {
		loggedInUser = null;
		isLoggedIn = false;
	}

}
