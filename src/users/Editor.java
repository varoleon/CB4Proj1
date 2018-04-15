package users;

import database.DBManagerEditor;

public class Editor extends User {
	private DBManagerEditor dbm;

	public Editor(int id, String name, String username, String password) {
		super(id, name, username, password);
		this.role = Role.EDITOR;
		this.dbm = new DBManagerEditor();
	}
	public int deleteMessage(int id) {
		return dbm.deleteMessageById(id);
	}
	
	public int editMessage(int id,String body) {
		return dbm.updateMessageById(id,body);
	}
	
	public void readSentMsgsOfUser(String username) {
		dbm.printSentMessages(username);
	}

}
