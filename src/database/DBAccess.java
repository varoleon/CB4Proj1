package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import users.Admin;
import users.Editor;
import users.User;

public class DBAccess {
	protected Connection conn;

	protected void connect() {
		conn = DBConnection.getInstance().getConnection();
	}

	protected void disconnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void mapParams(PreparedStatement ps, Object... args) throws SQLException {
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

	protected ResultSet fetchPrepared(String sql, Object[] args) {
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

	public User getUserByUsername(String username) {
		connect();
		User userToReturn = null;
		String sql = "SELECT users.id, users.name, users.username, users.password, roles.roleTitle "
				+ "FROM project1.users " + "INNER JOIN roles ON users.role = roles.id " + "WHERE users.username = ? ";
		ResultSet rs = fetchPrepared(sql, new Object[] { username });

		try {
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
					System.out.println("Error. Invalid role");
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

	public User getUserById(int id) {
		connect();
		ResultSet rs;
		User userToReturn = null;
		String sql = "SELECT users.id, users.name, users.username, users.password, roles.roleTitle "
				+ "FROM project1.users " + "INNER JOIN roles ON users.role = roles.id " + "WHERE users.id = ? ";
		rs = fetchPrepared(sql, new Object[] { id });

		try {
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

}
