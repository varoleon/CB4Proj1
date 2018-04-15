package p0;

import java.util.Scanner;

import users.Admin;
import users.Editor;
import users.Role;
import users.User;

public class Menu {
	private Scanner sc;
	private Login loginObj;
	private boolean terminate = false;

	public Menu() {
		sc = new Scanner(System.in);
		loginObj = new Login();
	}

	// Welcome and prompts for login
	public void startMenu() {
		Views.welcome();

		while (true) {
			if (terminate)
				break;
			System.out.print("Give \"login\" or \"exit\" : ");
			String choice = sc.nextLine();

			if (choice.equalsIgnoreCase("exit")) {
				break;
			} else if (choice.equalsIgnoreCase("login")) {

				if (isLoginSuccess()) {
					System.out.printf("Hello %S. You are logged in.\n", loginObj.getNameLoggedInUser());
					mainMenu();
				} else {
					System.out.println(loginObj.getErrorMessage());
				}

			} else {
				System.out.println("Unknown command.");
			}
		}
	}

	// Login Screen , returns true if login is successful
	private boolean isLoginSuccess() {
		System.out.print("Username : ");
		String username = sc.nextLine();
		System.out.print("Password : ");
		String pass = sc.nextLine();

		return loginObj.login(username, pass);
	}

	public void mainMenu() {
		while (true) {
			// only logged in users are allowed
			if (!loginObj.isLoggedIn())
				break;

			// Print menu according to logged in user role
			printMainMenu();

			System.out.print("Enter> ");
			String choice = sc.nextLine();
			if (choice.equalsIgnoreCase("exit")) {
				terminate = true;
				break;
			}
			// Menu operations
			menuOperations(choice);
		}

	}

	private void printMainMenu() {
		switch (loginObj.getRoleLoggedInUser()) {
		case ADMIN:
			Views.adminMainMenu();
			break;
		case EDITOR:
			Views.editorMainMenu();
			break;
		case USER:
			Views.userMainMenu();
			break;
		default:
			break;
		}
	}

	public void sendMsgOp() {
		loginObj.getDBManager().printUsernames();
		System.out.println("Choose from the above list");
		System.out.print("Receiver (username): ");
		String username = sc.nextLine();

		User receiver = loginObj.getDBManager().getUserByUsername(username);

		// check the existence of receiver
		if (receiver != null) {
			System.out.print("Your Message: ");
			String m = sc.nextLine();
			System.out.println(loginObj.getLoggedInUser().messageTo(receiver, m));
			System.out.println("Your message has been sent successfully.");
		} else {
			System.out.println("Receiver not found");
		}
	}

	private void registerUsers() {

		System.out.println("---Register New User---");
		System.out.print("Username : ");
		String username = sc.nextLine();

		// check username availability
		if (!loginObj.getDBManager().isUsernameInUse(username)) {

			System.out.print("Password : ");
			String password = sc.nextLine();
			System.out.print("Name : ");
			String name = sc.nextLine();
			System.out.print("Role : ");
			String roleStr = sc.nextLine().toUpperCase();
			Role role = Role.USER;
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
			default:
				System.out.println("Didn't understand the role. I'll create a USER");
			}

			Admin admin = (Admin) loginObj.getLoggedInUser();
			if (admin.registerUser(name, username, password, role) >0) {
				System.out.println("User " + username + " registered succesfully");
			}else {
				System.out.println("Error. No registration");
			}
			
		} else {
			System.out.println("Error. The username " + username + " already exists");
		}
	}

	private void removeUser() {
		System.out.println("---Remove User---");
		
		loginObj.getDBManager().printUsernames();
		System.out.print("Select a user, from above list to remove: ");
		String username = sc.nextLine();
		if (!loginObj.getDBManager().isUsernameInUse(username)) {
			System.out.println("Error. User "+username+" not found");
		}else {	
			System.out.print("Are you sure you want delete user " + username + "? (y/n): ");
			String c = sc.nextLine();
	
			if (c.equalsIgnoreCase("y")) {
				Admin admin = (Admin) loginObj.getLoggedInUser();
				if (admin.removeUser(username)>0) {
					System.out.println("User " + username + " deleted.");
				}else {
					System.out.println("Error. No deletion");
				}
			}else {
				System.out.println("Canceled");
			}
		}
	}
	
	private void deleteMsgOp() {
		System.out.println("---Delete Message---");
		System.out.print("Id of message: ");
		int id = Integer.parseInt(sc.nextLine());
		
		System.out.print("Are you sure? (y/n): ");
		String c = sc.nextLine();

		if (c.equalsIgnoreCase("y")) {
			Editor editor = (Editor) loginObj.getLoggedInUser();
			if(editor.deleteMessage(id)>0) {
				System.out.println("Message deleted");
			}else {
				System.out.println("Error. Message id "+id+" not found");
			}
		}else {
			System.out.println("Deletion canceled");
		}
	}

	private void menuOperations(String choice) {
		switch (choice) {
		case "r":
			// Register new user
			if (loginObj.getRoleLoggedInUser() == Role.USER || loginObj.getRoleLoggedInUser() == Role.EDITOR) {
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			registerUsers();
			break;
		case "rm":
			if (loginObj.getRoleLoggedInUser() == Role.USER || loginObj.getRoleLoggedInUser() == Role.EDITOR) {
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			removeUser();
			break;
		case "u":
			if (loginObj.getRoleLoggedInUser() == Role.USER || loginObj.getRoleLoggedInUser() == Role.EDITOR) {
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			// Manage users
			break;
		case "e":
			if (loginObj.getRoleLoggedInUser() == Role.USER) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			// Edit messages
			break;
		case "del":
			//Delete message
			if (loginObj.getRoleLoggedInUser() == Role.USER) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			deleteMsgOp();
			break;
		case "s":
			// Send message to someone
			sendMsgOp();
			break;
		case "i":
			// Income messages
			loginObj.getLoggedInUser().readIncomeMessages();
			break;
		case "l":
			loginObj.logout();
			break;
		default:
			System.out.println("Unknown command.");
			break;
		}
	}

}