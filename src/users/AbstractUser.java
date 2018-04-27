package users;

import java.sql.Timestamp;


import app.Menu;
import app.Message;
import database.DBUtils;
import interfaces.Menuable;
import interfaces.User;

public abstract class AbstractUser implements User, Menuable {
	private int id;
	private String name, username, password;
	
	public AbstractUser() {
		// TODO Auto-generated constructor stub
	}

	public AbstractUser(int id, String name, String username, String password) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean sendMessage() {
		DBUtils.printUsernames();
		
		System.out.print("Receiver (username): ");
		String username = Menu.sc.nextLine();
		
		AbstractUser receiver = DBUtils.getUserByUsername(username);
		if (receiver != null) {
			System.out.print("Your Message: ");
			String m = Menu.sc.nextLine();
			System.out.println(messageTo(receiver, m));
			DBUtils.disconnect();
			return true;
		}
		
		DBUtils.disconnect();
		return false;
	}

	private Message messageTo(AbstractUser receiver, String messageBody) {
		Message m = new Message(this, receiver, messageBody);

		// store to db must run first to get message id from db
		m.setId(storeMsgToDB(receiver.id, m.getBody(), m.getTimestamp()));
		m.saveToLog();
		m.saveToSenderReceiverFile();
		return m;
	}

	private int storeMsgToDB(int rId, String body, Timestamp timestamp) {
		// TODO Auto-generated method stub
		System.out.println (this.id +" "+rId+" "+body+" "+timestamp);
		DBUtils.insertNewMessage(this.id, rId, body, timestamp);
		return 0;
	}
}
