package app;

import java.util.Scanner;

import database.DBManager;
import users.Admin;
import users.Editor;
import users.Role;
import users.User;

public class Menu {
	public static final Scanner sc = new Scanner(System.in);
	
	private DBManager dbm;
	private User loggedInUser = null;
	private boolean isLoggedIn = false;
	
	private boolean terminate = false;


	public Menu() {
		dbm = new DBManager();
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

		User user = dbm.getUserByUsername(username);
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

			// Print menu according to logged in user role
			printMainMenu(loggedInUser.getRole());

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

	private void printMainMenu(Role role) {
		switch (role) {
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

	private void showReceivedMsgOfAUserOp() {
		System.out.println("---Received Messages of User---");

		Editor editor = (Editor) loggedInUser;
		editor.readReceivedMsgsOfUser();
	}

	private void showSentMsgOfAUserOp() {
		System.out.println("---Sent Messages of User---");

		Editor editor = (Editor) loggedInUser;
		editor.readSentMsgsOfUser();
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
			loggedInUser.readReceivedMessages();
			break;
		case "3":
			// Sent messages
			System.out.println("---Sent Messages---");
			loggedInUser.readSentMessages();
			break;

		case "4":
			// Show anyone's received messages
			if (loggedInUser.getRole() == Role.USER) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			showReceivedMsgOfAUserOp();
			break;

		case "5":
			// Show anyone's sent messages
			if (loggedInUser.getRole() == Role.USER) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			showSentMsgOfAUserOp();
			break;
		case "6":
			// Edit messages
			if (loggedInUser.getRole() == Role.USER) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			editMessageOp();
			break;
		case "7":
			// Delete message
			if (loggedInUser.getRole() == Role.USER) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			deleteMsgOp();
			break;
		case "8":
			// Register new user
			if (loggedInUser.getRole() == Role.USER || loggedInUser.getRole() == Role.EDITOR) {
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			registerUsersOp();
			break;
		case "9":
			// Remove user
			if (loggedInUser.getRole() == Role.USER || loggedInUser.getRole() == Role.EDITOR) {
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			removeUserOp();
			break;
		case "10":
			// Update users
			if (loggedInUser.getRole() == Role.USER || loggedInUser.getRole() == Role.EDITOR) {
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
