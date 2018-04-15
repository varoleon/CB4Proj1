package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManagerEditor extends DBManagerUser {
	public DBManagerEditor() {
		super();
	}
	public int editMessage() {
		return 0;	
	}
	
	
	public int deleteMessageById(int id) {
		connect();
		int res = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM messages WHERE id=?");
			stmt.setInt(1, id);
			res = stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		disconnect();
		return res;
	}
}
