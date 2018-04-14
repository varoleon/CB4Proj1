package users;

import database.DBManagerEditor;

public class Editor extends User {
	private DBManagerEditor dbm;

	public Editor(int id, String name, String username, String password) {
		super(id, name, username, password);
		this.role = Role.EDITOR;
		this.dbm = new DBManagerEditor();
	}
	
	public void editMessage() {
		dbm.editMessage();
	}

}
