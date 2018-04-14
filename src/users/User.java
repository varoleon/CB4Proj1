package users;

import database.DBManager;
import p0.Message;

public class User {
	protected String name, username, password;
	protected Role role;
	protected int id;
	
	private DBManager dbm;

	public User(int id, String name, String username, String password) {
		super();
		this.id=id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = Role.USER;
		this.dbm = new DBManager();
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
		return "User [name=" + name + ", username=" + username + ", role=" + role + "]";
	}

	public String getPassword() {
		return password;
	}
	
	public Message messageTo(User receiver, String message) {
		Message m =new Message(this, receiver, message);
		m.saveToLog();
		m.saveToSenderReceiverFile();
		m.storeToDb();
		return m;
	}
	
	public void readIncomeMessages() {
		dbm.printIncomeMessages(id);
	}
	

}
