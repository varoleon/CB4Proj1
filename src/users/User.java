package users;

import java.sql.Timestamp;

import app.Menu;
import app.Message;
import database.DBAccessUser;

public class User {
	protected String name, username, password;
	protected Role role;
	protected int id;

	private DBAccessUser dao;

	public User(int id, String name, String username, String password) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = Role.USER;
		this.dao = new DBAccessUser();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public int getId() {
		return id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Username:" + username + ", Password:" + password + ", Name:" + name + ", Role:" + role;
	}

	public String getPassword() {
		return password;
	}

	public boolean sendMessage() {
		System.out.println("\nSelect receiver from the following list");
		dao.printUsernamesInCols();

		System.out.print("Receiver (username): ");
		String username = Menu.sc.nextLine();

		User receiver = dao.getUserByUsername(username);
		if (receiver != null) {
			System.out.print("Your Message: ");
			String m = Menu.sc.nextLine();
			System.out.println(messageTo(receiver, m));
			return true;
		} else {
			return false;
		}
	}

	private Message messageTo(User receiver, String messageBody) {
		Message m = new Message(this, receiver, messageBody);

		// store to db must run first to get message id from db
		m.setId(storeMsgToDB(receiver.id, m.getBody(), m.getTimestamp()));
		m.saveToLog();
		m.saveToSenderReceiverFile();
		return m;
	}

	public int storeMsgToDB(int rId, String body, Timestamp timestamp) {
		return dao.insertNewMessage(id, rId, body, timestamp);
	}

	public void readReceivedMessages() {
		dao.printMessages(username, true); // true for received
	}

	public void readSentMessages() {
		dao.printMessages(username, false); // false for sent
	}

}
