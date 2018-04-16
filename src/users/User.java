package users;


import java.sql.Timestamp;

import database.DBManagerUser;
import p0.Message;

public class User {
	protected String name, username, password;
	protected Role role;
	protected int id;
	
	private DBManagerUser dbm;

	public User(int id, String name, String username, String password) {
		super();
		this.id=id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = Role.USER;
		this.dbm = new DBManagerUser();
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
		return "Username:" + username + ", Password:" + password + ", Name:"+name+", Role:" + role;
	}

	public String getPassword() {
		return password;
	}
	
	public Message messageTo(User receiver, String messageBody) {
		Message m =new Message(this, receiver, messageBody);
		//store to db must run first to get message id from db
		storeMsgToDB(receiver.id, m.getBody() , m.getTimestamp());
		m.saveToLog();
		m.saveToSenderReceiverFile();
		return m;
	}
	
	public int storeMsgToDB(int rId, String body, Timestamp timestamp ) {
		return dbm.insertNewMessage(id, rId, body, timestamp);
	}
	
	public void readReceivedMessages() {
		dbm.printReceivedMessages(id);
	}
	
	

}
