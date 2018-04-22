package users;

import database.DBManagerEditor;
import p0.Menu;
import p0.Message;

public class Editor extends User {
	private DBManagerEditor dbm;

	public Editor(int id, String name, String username, String password) {
		super(id, name, username, password);
		this.role = Role.EDITOR;
		this.dbm = new DBManagerEditor();
	}

	public boolean deleteMessage() {
		System.out.print("Id of message: ");
		try {
			int id = Integer.parseInt(Menu.sc.nextLine());
			if (!dbm.msgIdExists(id)) {
				System.out.println("Message id " + id + " not found");
				return false;
			}

			System.out.print("Are you sure you want to delete it? (y/n): ");
			String c = Menu.sc.nextLine();
			if (c.equalsIgnoreCase("y")) {
				return (dbm.deleteMessageById(id) > 0) ? true : false;
			} else {
				System.out.println("Deletion canceled");
			}
		} catch (Exception e) {
			System.out.println("Invalid input.");
		}
		return false;
	}

	public boolean editMessage() {
		System.out.println("To find a message id ,check the log file or run the i or o command");
		System.out.print("Give message Id (or press enter to skip): ");
		String c = Menu.sc.nextLine();

		if (c.equals("")) {
			System.out.println("Canceled");
			return false;
		}

		try {
			int id = Integer.parseInt(c);
			Message m = dbm.getMsgById(id);
			if (m == null) {
				System.out.println("Message id not found");
				return false;
			}
			System.out.println(m);
			System.out.print("New message (press enter to skip): ");
			String body = Menu.sc.nextLine();

			if (body.equals("")) {
				System.out.println("Editing canceled");
				return false;
			}

			if (dbm.updateMessageById(m.getId(), body) > 0) {
				// update instance
				m.setBody(body);
				// Set the this.username as editor
				m.setEditedBy(username);
				m.saveToLog();
				m.saveToSenderReceiverFile();

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("You should entered a number");
		}
		return false;
	}

	public void readReceivedMsgsOfUser() {
		dbm.printUsernamesInCols();
		System.out.print("Select a user, from above list to read his received messages: ");
		String username = Menu.sc.nextLine();
		if (!dbm.isUsernameInUse(username)) {
			System.out.println("Error. User " + username + " not found");
		} else {
			dbm.printMessages(username, true); // true for received
		}

	}

	public void readSentMsgsOfUser() {
		dbm.printUsernamesInCols();
		System.out.print("Select a user, from above list to read his sent messages: ");
		String username = Menu.sc.nextLine();
		if (!dbm.isUsernameInUse(username)) {
			System.out.println("Error. User " + username + " not found");
		} else {
			dbm.printMessages(username, false); // false for sent
		}

	}

}
