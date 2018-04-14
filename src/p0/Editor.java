package p0;

public class Editor extends User {

	public Editor(int id, String name, String username, String password) {
		super(id, name, username, password);
		this.role = Role.EDITOR;
	}
	public void editMessage() {}

}
