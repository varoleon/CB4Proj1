package p0;

import java.sql.*;

public class Database {
	private static Database db;
	Connection conn=null;
	
	static final String JDBC_DRIVER = Config.JDBC_DRIVER;
	static final String DB_URL = Config.DB_URL;
	
	//Credentials
	static final String USER = Config.USER;
	static final String PASSWORD = Config.PASSWORD;

	private Database() {
		
		try {
			Class.forName(JDBC_DRIVER);
			
			conn= DriverManager.getConnection(DB_URL, USER,PASSWORD);
			
		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		} catch (SQLException e) {
			System.out.println("Unable to conncet");
			e.printStackTrace();
		}
	}

	public static Database getDbInst() {
		if (db == null)
			db = new Database();
		return db;
	}

	public void printUsernames() {
		
		CallableStatement cs;
		try {
			cs = this.conn.prepareCall("{call getAllUsernames()}");
			ResultSet rs = cs.executeQuery();

			int i=0;
			while (rs.next()) {
			    System.out.print(String.format("%s\t\t", rs.getString("username")));
			    if (++i%3==0) {
			    	System.out.println();
			    }
			}
			System.out.println();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public boolean isUsernameInUse(String username) {
		CallableStatement cs;
		try {
			cs = this.conn.prepareCall("{call getUserByUsername('"+username+"')}");
			ResultSet rs = cs.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public User getUserByUsername(String username) {

		CallableStatement cs;
		try {
			cs = this.conn.prepareCall("{call getUserByUsername('"+username+"')}");
			ResultSet rs = cs.executeQuery();
			
			if (!rs.next()) {
				return null;
			}else {
				
				switch (rs.getString("roleTitle")) {
				case "USER":
					return new User(rs.getInt("id"), rs.getString("name"), rs.getString("username"), rs.getString("password"));
				case "EDITOR":
					return new Editor(rs.getInt("id"), rs.getString("name"), rs.getString("username"), rs.getString("password"));
				case "ADMIN":
					return new Admin(rs.getInt("id"), rs.getString("name"), rs.getString("username"), rs.getString("password"));
				default:
					break;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
		
	}

	public boolean checkPassword(int id, String password) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT password FROM users WHERE id='"+id+"'";
			ResultSet rs =stmt.executeQuery(sql);
			
			while (rs.next()) {
				String dbpass=rs.getString(1);
				return (dbpass.equals(password))?true:false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void insertNewUser(String name, String username, String password, Role role) {
		Statement stmt;
		try {
			
			stmt = conn.createStatement();
			String sql ="INSERT INTO USERS VALUES (NULL,'"+name+"','"+username+"','"+password+"',(Select id from roles where roleTitle='"+role.name()+"'))";
			stmt.executeUpdate(sql);
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public void removeUserByUsername (String username) {
		Statement stmt;
		try {
			
			stmt = conn.createStatement();
			String sql ="DELETE FROM USERS WHERE username='"+username+"' ";
			stmt.executeUpdate(sql);
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void insertNewMessage(int sId, int rId, String body, Timestamp timestamp) {
		Statement stmt;
		try {
			
			stmt = conn.createStatement();
			String sql ="INSERT INTO messages VALUES (NULL,'"+sId+"','"+rId+"','"+body+"','"+timestamp+"')";
			stmt.executeUpdate(sql);
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public void printIncomeMessages(int id) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT messages.id,users.username,users.name,body,timestamp FROM messages " + 
					"INNER JOIN users ON messages.sender = users.id " + 
					"WHERE receiver="+id+
					" ORDER BY timestamp DESC";
			ResultSet rs =stmt.executeQuery(sql);
			
			while (rs.next()) {
				System.out.println("Message id: " + rs.getInt("id"));
				System.out.println("Sent on: "+ rs.getString("timestamp"));
				System.out.println("Sender: "+rs.getString("username")+ " ( "+rs.getString("name")+" )");
				System.out.println("\t"+rs.getString("body"));
				System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			}			
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
	


}
