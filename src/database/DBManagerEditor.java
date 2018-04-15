package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManagerEditor extends DBManagerUser {
	public DBManagerEditor() {
		super();
	}

	public int updateMessageById(int id,String body) {
		connect();
		int res=0;
		
		try {
			String sql="UPDATE messages SET messages.body= ? "
					+ "WHERE messages.id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, body);
			stmt.setInt(2, id);

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		disconnect();
		return res;
	}

	private ResultSet fetchSentMsgsByUsername(String username) throws SQLException {
		String sql = "SELECT messages.id, user1.username as sender, user2.username as receiver, messages.body, messages.timestamp "
				+ "FROM messages "
				+ "INNER JOIN users user1 ON sender=user1.id "
				+ "INNER JOIN users user2 ON receiver=user2.id "
				+ "WHERE user1.id=(SELECT id FROM users WHERE username=?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);
		return stmt.executeQuery();
	}

	public void printSentMessages(String username) {
		connect();
		ResultSet rs = null;
		try {
			rs = fetchSentMsgsByUsername(username);
			while (rs.next()) {
				System.out.println("Message id: " + rs.getInt("id"));
				System.out.println("Sent on: " + rs.getString("timestamp"));
				System.out.println("Sender: " + rs.getString("sender") + "\t\tReceiver: " + rs.getString("receiver"));
				System.out.println("\t" + rs.getString("body"));
				System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
	}
	

}
