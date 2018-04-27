package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import app.Message;
import users.AbstractUser;
import users.Admin;
import users.Editor;
import users.SimpleUser;



public class DBUtils {

	private static Connection conn=null;

	public DBUtils() {
		// TODO Auto-generated constructor stub
	}
	public static void connect() {
		conn = DBConnection.getInstance().getConnection();
	}

	public static void disconnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void mapParams(PreparedStatement ps, Object... args) throws SQLException {
		if (args!=null) {
			int i = 1;
			for (Object arg : args) {
				if (arg instanceof Timestamp)
					ps.setTimestamp(i++, (Timestamp) arg);
				else if (arg instanceof Integer) {
					ps.setInt(i++, (Integer) arg);
				} else if (arg instanceof Long) {
					ps.setLong(i++, (Long) arg);
				} else if (arg instanceof Double) {
					ps.setDouble(i++, (Double) arg);
				} else if (arg instanceof Float) {
					ps.setFloat(i++, (Float) arg);
				} else {
					ps.setString(i++, (String) arg);
				}
			}
		}
	}

	public static ResultSet executePrepared(String sql, Object[] args) {
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			mapParams(stmt, args);
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static List<Map<String, Object>> getFields(ResultSet rs) throws SQLException{
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		while (rs.next()) {
		    Map<String, Object> columns = new LinkedHashMap<String, Object>();

		    for (int i = 1; i <= columnCount; i++) {
		        columns.put(metaData.getColumnLabel(i), rs.getObject(i));
		    }

		    rows.add(columns);
		}
		
		return rows;	
	}
	
	public static void printUsernames() {
		connect();
		System.out.println("\nSelect receiver from the following list");
		ResultSet rs = executePrepared("SELECT username FROM users", null);
		//List<Map<String, Object>> result;
		try {
			while (rs.next()) {
				System.out.println( rs.getString(1));
			}		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
	}
	
	public static AbstractUser getUserByUsername(String username) {
		connect();
		AbstractUser userToReturn=null;
		String sql = "SELECT users.id, users.name, users.username, users.password, roles.roleTitle "
				+ "FROM project1.users " + "INNER JOIN roles ON users.role = roles.id " + "WHERE users.username = ? ";
		ResultSet rs = DBUtils.executePrepared(sql, new Object[] { username });
		
		try {
			int id = 0;
			String name = null,password = null,role = null;
			while (rs.next()) {
				id = rs.getInt("id");
				name = rs.getString("name");
				password = rs.getString("password");
				role = rs.getString("roleTitle");
			}
			if (role!=null) {
				switch (role) {
				case "USER":
					userToReturn = new SimpleUser(id, name, username, password);
					break;
				case "EDITOR":
					userToReturn = new Editor(id, name, username, password);
				case "ADMIN":
					userToReturn = new Admin(id, name, username, password);
					break;
	
				default:
					break;
				}
			}	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		return userToReturn;
	}
	
	public static AbstractUser getUserById(int id) {
		connect();
		ResultSet rs;
		AbstractUser userToReturn = null;
		String sql = "SELECT users.id, users.name, users.username, users.password, roles.roleTitle "
				+ "FROM project1.users " + "INNER JOIN roles ON users.role = roles.id " + "WHERE users.id = ? ";
		rs = executePrepared(sql, new Object[] { id });

		try {
			if (!rs.next()) {
				return null;
			} else {
				switch (rs.getString("roleTitle")) {
				case "USER":
					userToReturn = new SimpleUser(rs.getInt("id"), rs.getString("name"), rs.getString("username"),
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
	
	// InsertNewMessage returns auto generated message id
	public static int insertNewMessage(int sId, int rId, String body, Timestamp timestamp) {
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
	
	public static void printMessages(String username, boolean received) {
		connect();
		String sqlSent = "SELECT messages.id, user1.username as sender, user2.username as receiver, messages.body, messages.timestamp "
				+ "FROM messages " + "INNER JOIN users user1 ON sender=user1.id "
				+ "INNER JOIN users user2 ON receiver=user2.id "
				+ "WHERE user1.id=(SELECT id FROM users WHERE username=?)";
		
		String sqlReceived = "SELECT messages.id, user1.username as sender, user2.username as receiver, messages.body, messages.timestamp "
				+ "FROM messages " + "INNER JOIN users user1 ON sender=user1.id "
				+ "INNER JOIN users user2 ON receiver=user2.id "
				+ "WHERE user2.id=(SELECT id FROM users WHERE username=?)";
		
		try {
			ResultSet rs;
			if (received) {
				rs= executePrepared(sqlReceived, new Object[] {username});
			} else {
				rs= executePrepared(sqlSent, new Object[] {username});
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
	
	public static boolean isUsernameInUse(String username) {
		connect();
		boolean returnFlag = false;
		ResultSet rs = executePrepared("SELECT COUNT(1) FROM users WHERE username=?", new Object[] { username });
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
	
	public static boolean msgIdExists(int id) {
		connect();
		boolean found = false;
		ResultSet rs = executePrepared("SELECT COUNT(1) FROM messages WHERE id=?", new Object[] { id });
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

	public static Message getMsgById(int id) {

		connect();
		String sql = "SELECT * FROM messages WHERE id=?";
		ResultSet rs = executePrepared(sql, new Object[] { id });
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
			AbstractUser sender = getUserById(s);
			AbstractUser receiver = getUserById(r);

			msgToReturn = new Message(sender, receiver, b);
			msgToReturn.setId(id);
			msgToReturn.setTimestamp(t);
		}
		return msgToReturn;
	}

	public static int updateMessageById(int id, String body) {
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

	public static int deleteMessageById(int id) {
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

	public static int insertNewUser(String name, String username, String password, String role) {
		connect();
		int res = 0;
		String sql = "INSERT INTO users VALUES (NULL, ?, ?, ?, (SELECT id FROM roles WHERE roleTitle=?))";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, username);
			stmt.setString(3, password);
			stmt.setString(4, role);
			res = stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		return res;
	}

	public static int removeUserByUsername(String username) {
		connect();
		int res = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE username=?");
			stmt.setString(1, username);
			res = stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return res;
	}

	public static int updateUser(String username, String password, String name, String role) {
		connect();
		int res = 0;

		try {
			String sql = "UPDATE users SET users.password= ?, " + "users.name= ?, "
					+ "users.role= (SELECT id FROM roles WHERE roleTitle=?) " + "WHERE users.username=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, password);
			stmt.setString(2, name);
			stmt.setString(3, role);
			stmt.setString(4, username);
			res = stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		return res;
	}


}
