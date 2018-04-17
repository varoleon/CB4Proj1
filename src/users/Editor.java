package users;

import database.DBManagerEditor;
import p0.Message;

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

	public Message getMessage(int id) {
		return dbm.getMsgById(id);
	}

	public int editMessage(Message m, String body) {
		int res = dbm.updateMessageById(m.getId(), body);
		
		//update instance
		m.setBody( body );
		// Set the this.username as editor
		m.setEditedBy(username);
		m.saveToLog();
		m.saveToSenderReceiverFile();
		return res;
	}

	public void readSentMsgsOfUser(String username) {
		dbm.printSentMessages(username);
	}

}
