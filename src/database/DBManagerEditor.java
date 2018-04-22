package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import p0.Message;
import users.User;

public class DBManagerEditor extends DBManagerUser {
	public DBManagerEditor() {
		super();
	}

	public boolean msgIdExists(int id) {
		connect();
		boolean found = false;
		ResultSet rs = fetchPrepared("SELECT COUNT(1) FROM messages WHERE id=?", new Object[] { id });
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT id FROM messages WHERE id=?");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next())
				found = true;
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return found;
	}

	public Message getMsgById(int id) {

		connect();
		String sql = "SELECT * FROM messages WHERE id=?";
		ResultSet rs = fetchPrepared(sql, new Object[] { id });
		Message msgToReturn = null;
		int s = 0, r = 0;
		String b = null;
		Timestamp t = null;
		boolean found = false;
		try {
			if (rs.next()) {
				found = true;
				s = rs.getInt("sender");
				r = rs.getInt("receiver");
				b = rs.getString("body");
				t = rs.getTimestamp("timestamp");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		if (found) {
			User sender = getUserById(s);
			User receiver = getUserById(r);

			msgToReturn = new Message(sender, receiver, b);
			msgToReturn.setId(id);
			msgToReturn.setTimestamp(t);
		}
		return msgToReturn;
	}

	public int updateMessageById(int id, String body) {
		connect();
		int res = 0;

		try {
			String sql = "UPDATE messages SET messages.body= ? " + "WHERE messages.id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			mapParams(stmt, new Object[] { body, id });

			res = stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return res;
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
			e.printStackTrace();
		}

		disconnect();
		return res;
	}

}
