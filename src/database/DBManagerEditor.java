package database;

public class DBManagerEditor extends DBManager {
	public DBManagerEditor() {
		super();
	}
	public int editMessage() {
		return 0;	
	}
	
	public int deleteMessageById(int id) {
		connect();
		String sql = "DELETE FROM messages WHERE id=" + id;
		int res = modifyDB(sql);
		disconnect();
		return res;
	}
}
