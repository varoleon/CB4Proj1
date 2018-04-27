package users;

import app.Menu;
import app.Views;
import database.DBUtils;

public class Admin extends Editor{

	public Admin(int id, String name, String username, String password) {

		super(id,name, username,password);
	}
	
	public boolean registerUser() {
		System.out.print("Username : ");
		String username = Menu.sc.nextLine();

		// check username availability
		if (DBUtils.isUsernameInUse(username)) {
			System.out.println("Error. The username " + username + " already exists");
			return false;
		}
		System.out.print("Password : ");
		String password = Menu.sc.nextLine();
		System.out.print("Name : ");
		String name = Menu.sc.nextLine();
		System.out.print("Role (user/editor/admin): ");
		String role = Menu.sc.nextLine().toUpperCase();
		
		if (!role.equals("ADMIN") ||!role.equals("EDITOR") || !role.equals("USER")) {
			System.out.println("Didn't understand the role. I'll create a USER");
			role = "USER";
		}
		return (DBUtils.insertNewUser(name, username, password, role) > 0) ? true : false;

	}

	public boolean removeUser() {

		DBUtils.printUsernames();
		System.out.print("Select a user, from above list to remove: ");
		String username = Menu.sc.nextLine();
		if (!DBUtils.isUsernameInUse(username)) {
			System.out.println("User " + username + " not found");
			return false;
		} else if (username.equals(this.getUsername())) {
			System.out.println("No you can't delete yourself");
			return false;
		} else {
			System.out.print("Are you sure you want delete user " + username + "? (y/n): ");
			String c = Menu.sc.nextLine();

			if (c.equalsIgnoreCase("y")) {
				return (DBUtils.removeUserByUsername(username) > 0) ? true : false;
			} else {
				System.out.println("Canceled");
				return false;
			}
		}

	}

	public boolean updateUser() {

		DBUtils.printUsernames();
		System.out.print("Select a user, from above list to update: ");
		String username = Menu.sc.nextLine();
		if (!DBUtils.isUsernameInUse(username)) {
			System.out.println("Error. User " + username + " not found");
			return false;
		} else {
			// ask new data for each field
			boolean hasChange = false;
			AbstractUser userToUpdate = DBUtils.getUserByUsername(username);
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
			String role = Menu.sc.nextLine().toUpperCase();
			if (role.equals("")) {
				return false;
			}
			//Role role = mapRole(roleStr);
			if (!role.equals("ADMIN") ||!role.equals("EDITOR") || !role.equals("USER")) {
				System.out.println("Didn't understand the role. Default role: USER");
				role = "USER";
			}
			String currentRole=null;
			if (userToUpdate instanceof SimpleUser) {
				currentRole="USER";
			}else if(userToUpdate instanceof Editor){
				currentRole="EDITOR";
			}else if(userToUpdate instanceof Admin) {
				currentRole="ADMIN";
			}
			
			if (!currentRole.equals(role)){
				hasChange=true;
			}

			if (hasChange) {
				return (DBUtils.updateUser(username, password, name, role) > 0) ? true : false;
			} else {
				return false;
			}
		}
	}
	
	@Override
	public void printMenu() {
		// TODO Auto-generated method stub
		Views.adminMainMenu();
	}


}
