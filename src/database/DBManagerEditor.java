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
	
	public Message getMsgById(int id) {
		
		connect();
		String sql = "SELECT * FROM messages WHERE id=?";
		ResultSet rs = fetchPrepared(sql, new Object[] {id});
		Message msgToReturn=null;
		int s=0,r=0;
		String b=null;
		Timestamp t=null;
		try {
			if (rs.next()) {
				s=rs.getInt("sender");
				r=rs.getInt("receiver");
				b=rs.getString("body");
				t=rs.getTimestamp("timestamp");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		
		User sender=getUserById(s);
		User receiver = getUserById(r);

		msgToReturn= new Message(sender,receiver, b);
		msgToReturn.setId(id);
		msgToReturn.setTimestamp(t);
		
		return msgToReturn;
	}
	

	public int updateMessageById(int id,String body) {
		connect();
		int res=0;
		
		try {
			String sql="UPDATE messages SET messages.body= ? "
					+ "WHERE messages.id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			mapParams(stmt, new Object[] {body,id});

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
	
	//TODO make change to fetch data as is in database to make new message object
	//and need a query to call a view
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
		try {
			ResultSet rs = fetchSentMsgsByUsername(username);
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
