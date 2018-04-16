package p0;

public class Config {

	////////////////////////
	//// DataBase
	public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static String DB_URL = "jdbc:mysql://localhost:5000/project1?autoReconnect=true&useSSL=false";
	// Credentials
	public static String USER = "root";
	public static String PASSWORD = "admin";

	////////////////////////
	//// Files
	public static String MESSAGE_LOG = "C:\\Users\\Leonidas\\Desktop\\MessageLog.txt";
	public static String CONVERSATION_FOLDER = "C:\\Users\\Leonidas\\Desktop\\conversations\\";
}
