package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import app.Config;

public class DBManagerUser extends DBManager {

	public DBManagerUser() {
		super();
	}

	public boolean isUsernameInUse(String username) {
		connect();
		boolean returnFlag = false;
		ResultSet rs = fetchPrepared("SELECT COUNT(1) FROM users WHERE username=?", new Object[] { username });
		try {
			if (rs.next()) {
				returnFlag = rs.getInt(1) == 1 ? true : false;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return returnFlag;
	}

	public void printUsernamesInCols() {
		connect();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT username FROM users");
			ResultSet rs = stmt.executeQuery();

			int i = 0;
			while (rs.next()) {
				System.out.print(String.format("%s" + Config.TABS, rs.getString("username")));
				if (++i % Config.COLS == 0) {
					System.out.println();
				}
			}
			if (i % Config.COLS != 0) {
				System.out.println();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
	}

	// InsertNewMessage returns auto generated message id
	public int insertNewMessage(int sId, int rId, String body, Timestamp timestamp) {
		connect();
		int mId = 0; // message id to be returned
		String sql = "INSERT INTO messages VALUES (NULL, ?, ?, ?,?)";

		try {
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			mapParams(stmt, new Object[] { sId, rId, body, timestamp });

			stmt.executeUpdate();

			ResultSet mIdrs = stmt.getGeneratedKeys();
			mId = 0;
			while (mIdrs.next()) {
				mId = mIdrs.getInt(1);
			}
			stmt.close();
			mIdrs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return mId;
	}

	private ResultSet fetchSentMsgsByUsername(String username) throws SQLException {
		String sql = "SELECT messages.id, user1.username as sender, user2.username as receiver, messages.body, messages.timestamp "
				+ "FROM messages " + "INNER JOIN users user1 ON sender=user1.id "
				+ "INNER JOIN users user2 ON receiver=user2.id "
				+ "WHERE user1.id=(SELECT id FROM users WHERE username=?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);
		return stmt.executeQuery();
	}

	private ResultSet fetchReceivedMsgsByUsername(String username) throws SQLException {
		String sql = "SELECT messages.id, user1.username as sender, user2.username as receiver, messages.body, messages.timestamp "
				+ "FROM messages " + "INNER JOIN users user1 ON sender=user1.id "
				+ "INNER JOIN users user2 ON receiver=user2.id "
				+ "WHERE user2.id=(SELECT id FROM users WHERE username=?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);
		return stmt.executeQuery();
	}

	public void printMessages(String username, boolean received) {
		connect();
		try {
			ResultSet rs;
			if (received) {
				rs = fetchReceivedMsgsByUsername(username);
			} else {
				rs = fetchSentMsgsByUsername(username);
			}
			if (!rs.next()) {
				System.out.println("0 " + ((received) ? "received" : "sent") + " messages");
			} else {
				rs.beforeFirst();
				while (rs.next()) {
					System.out.println("Message id: " + rs.getInt("id"));
					System.out.println("Sent on: " + rs.getString("timestamp"));
					System.out.println("Sender: " + rs.getString("sender") + "\tReceiver: " + rs.getString("receiver"));
					System.out.println("\t" + rs.getString("body"));
					System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
	}

}
