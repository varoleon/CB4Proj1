package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import users.Admin;
import users.Editor;
import users.User;

public class DBManager {
	protected Connection conn;

	protected void connect() {
		conn = DBConnection.getInstance().getConnection();
	}

	protected void disconnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected ResultSet queryDB(String sql) {
		ResultSet rs = null;
		if (conn != null) {
			try {
				Statement stmt;
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return rs;
	}

	protected int modifyDB(String sql) {
		int result = 0;
		if (conn != null) {
			try {
				Statement stmt = conn.createStatement();
				result = stmt.executeUpdate(sql);
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	private ResultSet fetchUsernamesByUsername(String username) throws SQLException {
		String sql = "SELECT users.id, users.name, users.username, users.password, roles.roleTitle "
				+ "FROM project1.users " + "INNER JOIN roles ON users.role = roles.id " + "WHERE users.username = ? ";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);

		return stmt.executeQuery();
	}

	private ResultSet fetchPasswordByUserId(int id) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE id=?");
		stmt.setInt(1, id);
		return stmt.executeQuery();
	}

	private ResultSet fetchIncomeMsgsByUserId(int id) throws SQLException {
		String sql = "SELECT messages.id,users.username,users.name,body,timestamp " + "FROM messages "
				+ "INNER JOIN users ON messages.sender = users.id " + "WHERE receiver=? " + "ORDER BY timestamp ASC";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		return stmt.executeQuery();
	}

	public boolean isUsernameInUse(String username) {
		connect();
		ResultSet rs = null;
		boolean returnFlag = false;
		try {
			rs = fetchUsernamesByUsername(username);

			if (rs.next()) {
				returnFlag = true;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return returnFlag;
	}

	public User getUserByUsername(String username) {
		connect();
		ResultSet rs;
		User userToReturn = null;
		try {
			rs = fetchUsernamesByUsername(username);

			if (!rs.next()) {
				return null;
			} else {
				switch (rs.getString("roleTitle")) {
				case "USER":
					userToReturn = new User(rs.getInt("id"), rs.getString("name"), rs.getString("username"),
							rs.getString("password"));
					break;
				case "EDITOR":
					userToReturn = new Editor(rs.getInt("id"), rs.getString("name"), rs.getString("username"),
							rs.getString("password"));
					break;
				case "ADMIN":
					userToReturn = new Admin(rs.getInt("id"), rs.getString("name"), rs.getString("username"),
							rs.getString("password"));
					break;
				default:
					break;
				}
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return userToReturn;
	}

	public boolean checkPassword(int id, String password) {
		connect();
		ResultSet rs = null;
		Boolean returnFlag = false;

		try {
			rs = fetchPasswordByUserId(id);
			while (rs.next()) {
				if (rs.getString(1).equals(password)) {
					returnFlag = true;
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return returnFlag;
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

	public void printUsernames() {
		connect();
		ResultSet rs = queryDB("SELECT username FROM users");
		try {

			int i = 0;
			while (rs.next()) {
				System.out.print(String.format("%s\t\t", rs.getString("username")));
				if (++i % 4 == 0) {
					System.out.println();
				}
			}
			if (i % 4 != 0) {
				System.out.println();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
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
