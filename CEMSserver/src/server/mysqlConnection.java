package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class mysqlConnection {
	private static mysqlConnection mysqlcon;
	private Connection con = null;

	private String message;

	private String host;
	private String username;
	private String password;
	private String DB_name;
	private String mysql_port;
	private static boolean IsConnected;

	public mysqlConnection() {
		
	}

	public static mysqlConnection getInstance() {
		if(mysqlcon == null)
			mysqlcon = new mysqlConnection();
		return mysqlcon;
	}
	
	public void connectToDB(String host, String username, String password, String DB_name, String mysql_port) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.DB_name = DB_name;
		this.mysql_port = mysql_port;
		
		message = "";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			message += "Driver definition succeed\n";
			IsConnected = true;
		} catch (Exception ex) {
			/* handle the error */
			message += "Driver definition failed\n";
			IsConnected = false;
		}

		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://" + host + ":" + mysql_port + "/" + DB_name + "?serverTimezone=IST&allowPublicKeyRetrieval=true&useSSL=false",
					username, password);
			message += "SQL connection succeed\n";
			IsConnected = true;
		} catch (SQLException ex) {/* handle any errors */
			message += "SQLException: " + ex.getMessage() + "\n";
			message += "SQLState: " + ex.getSQLState() + "\n";
			message += "VendorError: " + ex.getErrorCode() + "\n";
			IsConnected = false;
		}
	}

	public void disconnetFromDB() {
		message = "";

		if (con != null) {
			try {
				System.out.println("disconnetiong from db");
				con.close();
				System.out.println("disconnected");
				con = null;
				message += "Disconnected secssesfully from MySQL\n";
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("problem disconnecting from db");
			}
		}
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public String getMessage() {
		return message;
	}

	public Boolean getIsConnected() {
		return IsConnected;
	}
}
