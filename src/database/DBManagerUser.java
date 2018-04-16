package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DBManagerUser extends DBManager {

	public DBManagerUser() {
		super();
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

	public void printReceivedMessages(int id) {
		connect();
		String sql = "SELECT messages.id,users.username,users.name,body,timestamp " + "FROM messages "
				+ "INNER JOIN users ON messages.sender = users.id " + "WHERE receiver=? " + "ORDER BY timestamp ASC";
		ResultSet rs = fetchPrepared(sql, new Object[] { id });
		try {

			while (rs.next()) {
				System.out.println("Message id: " + rs.getInt("id"));
				System.out.println("Sent on: " + rs.getString("timestamp"));
				System.out.println("Sender: " + rs.getString("username") + " ( " + rs.getString("name") + " )");
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
