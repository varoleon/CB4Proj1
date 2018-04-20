package p0;

import java.util.Scanner;

import users.Admin;
import users.Editor;
import users.Role;


public class Menu {
	public static final Scanner sc = new Scanner(System.in);
	private Login loginObj;
	private boolean terminate = false;


	public Menu() {
		
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

				if (loginObj.login()) {
					System.out.printf("\nHello %s. You are logged in.\n", loginObj.getNameLoggedInUser());
					mainMenu();
				} else {
					System.out.println(loginObj.getErrorMessage());
				}

			} else {
				System.out.println("Unknown command.");
			}
		}
	}

	public void mainMenu() {
		while (true) {
			// only logged in users are allowed
			if (!loginObj.isLoggedIn())
				break;

			// Print menu according to logged in user role
			printMainMenu(loginObj.getRoleLoggedInUser());

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
		
		if (loginObj.getLoggedInUser().sendMessage()) {
			System.out.println("Your message has been sent successfully.");
		}else {
			System.out.println("Receiver not found");
		}

		
	}

	private void registerUsersOp() {

		System.out.println("---Register New User---");
		
		Admin admin = (Admin) loginObj.getLoggedInUser();
		
		if (admin.registerUser()) {
			System.out.println("User registered succesfully");
		} else {
			System.out.println("Error. No registration");
		}
	}

	private void removeUserOp() {
		System.out.println("---Remove User---");

		Admin admin = (Admin) loginObj.getLoggedInUser();
		if (admin.removeUser()) {
			System.out.println("User deleted.");
		} else {
			System.out.println("Error. No deletion");
		}
	}

	private void deleteMsgOp() {
		System.out.println("---Delete Message---");
		
		Editor editor = (Editor) loginObj.getLoggedInUser();
		if (editor.deleteMessage()) {
			System.out.println("Message deleted");
		} else {
			System.out.println("Error. No message deleted");
		}
	
	}

	private void updateUserOp() {
		System.out.println("---Update User---");
		
		Admin admin = (Admin) loginObj.getLoggedInUser();
		if (admin.updateUser()) {
			System.out.println("User updated succesfully");
		} else {
			System.out.println("No update");
		}
	}
	private void showReceivedMsgOfAUserOp() {
		System.out.println("---Received Messages of User---");

		Editor editor = (Editor) loginObj.getLoggedInUser();
		editor.readReceivedMsgsOfUser();
	}

	private void showSentMsgOfAUserOp() {
		System.out.println("---Sent Messages of User---");

		Editor editor = (Editor) loginObj.getLoggedInUser();
		editor.readSentMsgsOfUser();
	}

	private void editMessageOp() {
		System.out.println("---Edit Messages---");

		Editor editor = (Editor) loginObj.getLoggedInUser();

		if (editor.editMessage()) {
			System.out.println("Message edited");
		}
		else {
			System.out.println("No editing");
		}

	}

	private void menuOperations(String choice) {
		switch (choice) {
		case "1":
			// Send message to someone
			sendMsgOp();
			break;
		case "2":
			// Received messages
			System.out.println("---Received Messages---");
			loginObj.getLoggedInUser().readReceivedMessages();
			break;
		case "3":
			// Sent messages
			System.out.println("---Sent Messages---");
			loginObj.getLoggedInUser().readSentMessages();
			break;
			
		case "4":
			// Show anyone's received messages
			if (loginObj.getRoleLoggedInUser() == Role.USER) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			showReceivedMsgOfAUserOp();
			break;
			
		case "5":
			// Show anyone's sent messages
			if (loginObj.getRoleLoggedInUser() == Role.USER) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			showSentMsgOfAUserOp();
			break;	
		case "6":
			// Edit messages
			if (loginObj.getRoleLoggedInUser() == Role.USER) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			editMessageOp();
			break;
		case "7":
			// Delete message
			if (loginObj.getRoleLoggedInUser() == Role.USER) {
				System.out.println("No access to this command.You are not Admin or Editor");
				break;
			}
			deleteMsgOp();
			break;
		case "8":
			// Register new user
			if (loginObj.getRoleLoggedInUser() == Role.USER || loginObj.getRoleLoggedInUser() == Role.EDITOR) {
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			registerUsersOp();
			break;
		case "9":
			// Remove user
			if (loginObj.getRoleLoggedInUser() == Role.USER || loginObj.getRoleLoggedInUser() == Role.EDITOR) {
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			removeUserOp();
			break;
		case "10":
			// Update users
			if (loginObj.getRoleLoggedInUser() == Role.USER || loginObj.getRoleLoggedInUser() == Role.EDITOR) {
				System.out.println("No access to this command.You are not Admin");
				break;
			}
			updateUserOp();
			break;
		case "l":
			// logout
			loginObj.logout();
			break;
		default:
			System.out.println("Unknown command.");
			break;
		}
	}

}
