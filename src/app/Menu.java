package app;

import java.util.Scanner;

import database.DBUtils;
import users.AbstractUser;
import users.Admin;
import users.Editor;
import users.SimpleUser;


public class Menu {
	public static final Scanner sc = new Scanner(System.in);
	
	private AbstractUser loggedInUser = null;
	private boolean isLoggedIn = false;
	private boolean terminate = false;

	public Menu() {
	}

	// Welcome and prompts for login
	public void startMenu() {
		Views.welcome();

		while (true) {
			if (terminate)
				break;
			System.out.print("Give \"login\" or \"exit\" : ");
			String inp = sc.nextLine();

			if (inp.equalsIgnoreCase("exit")) {
				break;
			} else if (inp.equalsIgnoreCase("login")) {

				if (login()) {
					System.out.printf("\nHello %s. You are logged in.\n", loggedInUser.getName());
					mainMenu();
				} else {
					System.out.println("Login Failure");
				}

			} else {
				System.out.println("Unknown command.");
			}
		}
	}

	private boolean login() {
		// Login process
		System.out.print("Username : ");
		String username = sc.nextLine();
		System.out.print("Password : ");
		String password = sc.nextLine();

		AbstractUser user = DBUtils.getUserByUsername(username);
		if (user != null) {
			if (user.getPassword().equals(password)) {
				loggedInUser = user;
				isLoggedIn = true;
				return true;
			} else {
				System.out.println("Wrong Password for user " + username);
			}
		} else {
			System.out.println("User " + username + " not found.");
		}

		return false;
	}

	private void logout() {
		loggedInUser = null;
		isLoggedIn = false;
	}

	public void mainMenu() {
		while (true) {
			// only logged in users are allowed
			if (!isLoggedIn)
				break;

			loggedInUser.printMenu();

			System.out.print("Enter> ");
			String inp = sc.nextLine();
			if (inp.equalsIgnoreCase("exit")) {
				terminate = true;
				break;
			}
			// Menu operations
			menuOperations(inp);
		}

	}


	public void sendMsgOp() {
		System.out.println("---Send New Message---");

		if (loggedInUser.sendMessage()) {
			System.out.println("Your message has been sent successfully.");
		} else {
			System.out.println("Receiver not found");
		}

	}

	private void registerUsersOp() {

		System.out.println("---Register New User---");

		Admin admin = (Admin) loggedInUser;

		if (admin.registerUser()) {
			System.out.println("User registered succesfully");
		} else {
			System.out.println("Error. No registration");
		}
	}

	private void removeUserOp() {
		System.out.println("---Remove User---");

		Admin admin = (Admin) loggedInUser;
		if (admin.removeUser()) {
			System.out.println("User deleted.");
		} else {
			System.out.println("Error. No deletion");
		}
	}

	private void deleteMsgOp() {
		System.out.println("---Delete Message---");

		Editor editor = (Editor) loggedInUser;
		if (editor.deleteMessage()) {
			System.out.println("Message deleted");
		} else {
			System.out.println("Error. No message deleted");
		}

	}

	private void updateUserOp() {
		System.out.println("---Update User---");

		Admin admin = (Admin) loggedInUser;
		if (admin.updateUser()) {
			System.out.println("User updated succesfully");
		} else {
			System.out.println("No update");
		}
	}


	private void editMessageOp() {
		System.out.println("---Edit Messages---");

		Editor editor = (Editor) loggedInUser;

		if (editor.editMessage()) {
			System.out.println("Message edited");
		} else {
			System.out.println("No editing");
		}

	}

	private void menuOperations(String inp) {
		switch (inp) {
		case "1":
			// Send message to someone
			sendMsgOp();
			break;
		case "2":
			// Received messages
			System.out.println("---Received Messages---");
			loggedInUser.readReceivedMsgs();
			break;
		case "3":
			// Sent messages
			System.out.println("---Sent Messages---");
			loggedInUser.readSentMsgs();
			break;
		case "4":
			// Edit messages
			if (loggedInUser instanceof SimpleUser) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			editMessageOp();
			break;
		case "5":
			// Delete message
			if (loggedInUser instanceof SimpleUser) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			deleteMsgOp();
			break;
		case "6":
			// Register new user
			if (!(loggedInUser instanceof Admin)) {
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			registerUsersOp();
			break;
		case "7":
			// Remove user
			if (!(loggedInUser instanceof Admin)){
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			removeUserOp();
			break;
		case "8":
			// Update users
			if (!(loggedInUser instanceof Admin)){
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			updateUserOp();
			break;
		case "l":
			// logout
			logout();
			break;
		default:
			System.out.println("Unknown command.");
			break;
		}
	}
	
	
	public void exitMenu() {
		System.out.println("Bye Bye!");
		sc.close();
	}

}
