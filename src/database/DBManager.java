package database;

import java.sql.CallableStatement;
import java.sql.Connection;
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

	public boolean isUsernameInUse(String username) {
		connect();
		CallableStatement cs;

		boolean returnFlag = false;
		try {
			cs = this.conn.prepareCall("{call getUserByUsername('" + username + "')}");
			ResultSet rs = cs.executeQuery();

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
		CallableStatement cs;
		User userToReturn = null;
		try {
			cs = this.conn.prepareCall("{call getUserByUsername('" + username + "')}");
			ResultSet rs = cs.executeQuery();

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
		String sql = "SELECT password FROM users WHERE id='" + id + "'";

		ResultSet rs = queryDB(sql);
		Boolean returnFlag = false;

		try {
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

	//InsertNewMessage returns auto generated message id
	public int insertNewMessage(int sId, int rId, String body, Timestamp timestamp) {
		connect();
		int mId=0; //message id to be returned
		String sql = "INSERT INTO messages VALUES (NULL,'" + sId + "','" + rId + "','" + body + "','" + timestamp
				+ "')";
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			
			ResultSet mIdrs = stmt.getGeneratedKeys();
			mId=0;
			while(mIdrs.next()){
				mId= mIdrs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		disconnect();
		return mId;
	}

	public void printUsernames() {
		connect();
		CallableStatement cs;
		try {
			cs = this.conn.prepareCall("{call getAllUsernames()}");
			ResultSet rs = cs.executeQuery();

			int i = 0;
			while (rs.next()) {
				System.out.print(String.format("%s\t\t", rs.getString("username")));
				if (++i % 4 == 0) {
					System.out.println();
				}
			}
			System.out.println();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
	}

	public void printIncomeMessages(int id) {
		connect();
		String sql = "SELECT messages.id,users.username,users.name,body,timestamp FROM messages "
				+ "INNER JOIN users ON messages.sender = users.id " + "WHERE receiver=" + id
				+ " ORDER BY timestamp DESC";
		ResultSet rs = queryDB(sql);
		try {
			while (rs.next()) {
				System.out.println("Message id: " + rs.getInt("id"));
				System.out.println("Sent on: " + rs.getString("timestamp"));
				System.out.println("Sender: " + rs.getString("username") + " ( " + rs.getString("name") + " )");
				System.out.println("\t" + rs.getString("body"));
				System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
