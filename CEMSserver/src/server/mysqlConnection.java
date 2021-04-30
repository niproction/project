package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class mysqlConnection {
	private Connection conn;
	private String message;

	private String host;
	private String username;
	private String password;
	private String DB_name;
	private String mysql_port;

	private static boolean IsConnected;


	public mysqlConnection(String host, String username, String password, String DB_name, String mysql_port)
	{
		this.host = host;
		this.username = username;
		this.password = password;
		this.DB_name = DB_name;
		this.mysql_port = mysql_port;
	}

	public String getMessage()
	{
		return message;
	}
	
	public boolean GET_connectionState()
	{
		return IsConnected;
	}

	public void connectToDB() {
		message="";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			message+="Driver definition succeed\n";
			IsConnected = true;
		} catch (Exception ex) {
			/* handle the error */
			message+="Driver definition failed\n";
			IsConnected = false;
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+mysql_port+"/"+DB_name+"?serverTimezone=IST", username, password);
			message +="SQL connection succeed\n";
			IsConnected = true;
		} catch (SQLException ex) {/* handle any errors */
			message += "SQLException: " + ex.getMessage()+"\n";
			message+="SQLState: " + ex.getSQLState()+"\n";
			message+="VendorError: " + ex.getErrorCode()+"\n";
			IsConnected = false;
		}
		//return message;
	}

	public void disconnetFromDB()
	{
		message="";
		try {
			// Do stuff

		} //catch (SQLException ex) {
			// Exception handling stuff
			
		//}
		 finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
					message+="Disconnected secssesfully from MySQL\n";
				} catch (SQLException e) { /* Ignored */}
			}
		}
	}
	public Connection getCon() {
		return conn;
	}
	public Boolean getIsConnected() {
		return IsConnected;
	}
}
