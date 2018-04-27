package users;

import app.Views;
import database.DBUtils;

public class SimpleUser extends AbstractUser{

	public SimpleUser(int id, String name, String username, String password) {
		super(id, name, username, password);
	}

	@Override
	public void readReceivedMsgs() {
		// TODO Auto-generated method stub
		DBUtils.printMessages(this.getUsername(), true);//true for received
	}

	@Override
	public void readSentMsgs() {
		DBUtils.printMessages(this.getUsername(), false);
	}

	@Override
	public void printMenu() {
		// TODO Auto-generated method stub
		Views.userMainMenu();
	}

}
