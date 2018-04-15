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
			stmt.setInt(1, sId);
			stmt.setInt(2, rId);
			stmt.setString(3, body);
			stmt.setTimestamp(4, timestamp);

			stmt.executeUpdate();

			ResultSet mIdrs = stmt.getGeneratedKeys();
			mId = 0;
			while (mIdrs.next()) {
				mId = mIdrs.getInt(1);
			}
			
			stmt.close();
			mIdrs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		disconnect();
		return mId;
	}
	
	private ResultSet fetchIncomeMsgsByUserId(int id) throws SQLException {
		String sql = "SELECT messages.id,users.username,users.name,body,timestamp " + "FROM messages "
				+ "INNER JOIN users ON messages.sender = users.id " + "WHERE receiver=? " + "ORDER BY timestamp ASC";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		return stmt.executeQuery();
	}
	
	public void printIncomeMessages(int id) {
		connect();
		ResultSet rs = null;
		try {
			rs = fetchIncomeMsgsByUserId(id);
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
