package users;

import app.Menu;
import database.DBAccessAdmin;

public class Admin extends Editor {
	private DBAccessAdmin dao;

	public Admin(int id, String name, String username, String password) {
		super(id, name, username, password);
		this.role = Role.ADMIN;
		this.dao = new DBAccessAdmin();
	}

	public boolean registerUser() {
		System.out.print("Username : ");
		String username = Menu.sc.nextLine();

		// check username availability
		if (dao.isUsernameInUse(username)) {
			System.out.println("Error. The username " + username + " already exists");
			return false;
		}
		System.out.print("Password : ");
		String password = Menu.sc.nextLine();
		System.out.print("Name : ");
		String name = Menu.sc.nextLine();
		System.out.print("Role (user/editor/admin): ");
		String roleStr = Menu.sc.nextLine().toUpperCase();
		Role role = mapRole(roleStr);
		if (role == null) {
			System.out.println("Didn't understand the role. I'll create a USER");
			role = Role.USER;
		}
		return (dao.insertNewUser(name, username, password, role.name()) > 0) ? true : false;

	}

	public boolean removeUser() {

		dao.printUsernamesInCols();
		System.out.print("Select a user, from above list to remove: ");
		String username = Menu.sc.nextLine();
		if (!dao.isUsernameInUse(username)) {
			System.out.println("User " + username + " not found");
			return false;
		} else if (username.equals(this.username)) {
			System.out.println("No you can't delete yourself");
			return false;
		} else {
			System.out.print("Are you sure you want delete user " + username + "? (y/n): ");
			String c = Menu.sc.nextLine();

			if (c.equalsIgnoreCase("y")) {
				return (dao.removeUserByUsername(username) > 0) ? true : false;
			} else {
				System.out.println("Canceled");
				return false;
			}
		}

	}

	public boolean updateUser() {

		dao.printUsernamesInCols();
		System.out.print("Select a user, from above list to update: ");
		String username = Menu.sc.nextLine();
		if (!dao.isUsernameInUse(username)) {
			System.out.println("Error. User " + username + " not found");
			return false;
		} else {
			// ask new data for each field
			boolean hasChange = false;
			User userToUpdate = dao.getUserByUsername(username);
			System.out.println("Current values");
			System.out.println("\t" + userToUpdate);

			System.out.print("New Password (Press enter to skip this change): ");
			String password = Menu.sc.nextLine();
			if (password.equals(""))
				password = userToUpdate.getPassword();
			else
				hasChange = true;

			System.out.print("New Name (Press enter to skip this change): ");
			String name = Menu.sc.nextLine();
			if (name.equals(""))
				name = userToUpdate.getName();
			else
				hasChange = true;
			System.out.print("New Role (user/editor/admin or Press enter to skip this change): ");
			String roleStr = Menu.sc.nextLine().toUpperCase();
			if (roleStr.equals("")) {
				return false;
			}
			Role role = mapRole(roleStr);
			if (role == null) {
				System.out.println("Didn't understand the role. Default role: USER");
				role = Role.USER;
			}

			if (userToUpdate.getRole() != role) {
				hasChange = true;
			}

			if (hasChange) {
				return (dao.updateUser(username, password, name, role.name()) > 0) ? true : false;
			} else {
				return false;
			}
		}
	}

	private Role mapRole(String roleStr) {
		Role role = null;
		switch (roleStr) {
		case "ADMIN":
			role = Role.ADMIN;
			break;
		case "EDITOR":
			role = Role.EDITOR;
			break;
		case "USER":
			role = Role.USER;
			break;
		}
		return role;
	}

}
