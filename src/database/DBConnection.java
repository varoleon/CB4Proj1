package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import p0.Config;

public class DBConnection {
	private static DBConnection instance;
	private Connection conn = null;
	private static final String JDBC_DRIVER = Config.JDBC_DRIVER;
	private static final String DB_URL = Config.DB_URL;
	// Credentials
	private static final String USER = Config.USER;
	private static final String PASSWORD = Config.PASSWORD;

	private DBConnection() {
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		} catch (SQLException e) {
			System.out.println("Unable to conncet");
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return conn;
	}

	public static DBConnection getInstance() {
		try {
			if (instance == null) {
				instance = new DBConnection();
			} else if (instance.getConnection().isClosed()) {
				instance = new DBConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return instance;
	}

}
