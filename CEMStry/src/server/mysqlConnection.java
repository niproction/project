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

	public void connectToDB() {
		message="";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			message+="Driver definition succeed\n";
		} catch (Exception ex) {
			/* handle the error */
			message+="Driver definition failed\n";
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+mysql_port+"/"+DB_name+"?serverTimezone=IST", username, password);
			message +="SQL connection succeed"+"\n";
		} catch (SQLException ex) {/* handle any errors */
			message += "SQLException: " + ex.getMessage()+"\n";
			message+="SQLState: " + ex.getSQLState()+"\n";
			message+="VendorError: " + ex.getErrorCode()+"\n";
		}
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
					message+="Disconnected secssesfully from MySQL\n";
				} catch (SQLException e) { /* Ignored */}
			}
		}
	}
	public Connection getCon() {
		return conn;
	}
	public void saveUserToDB(ArrayList<String> userInfo) {

		Statement stmt;
		PreparedStatement pstmt;
		
		/*try {
			stmt = conn.createStatement();
			pstmt= conn.prepareStatement("INSERT INTO users (ID,username,Department,Tel) VALUES (?,?,?,?);");
			pstmt.setString(1,userInfo.get(0));
			pstmt.setString(2, userInfo.get(1));
			pstmt.setString(3, userInfo.get(2));
			pstmt.setString(4, userInfo.get(3));
			int rs =pstmt.executeUpdate();
			if(rs==1)
				System.out.print("inserted");
				
		} catch (SQLException e) {
			e.printStackTrace();
		}*/

	}
	public ResultSet excuteQuery(String sqlQuery) {
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			resultSet=preparedStatement.executeQuery();
			return resultSet;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	

}
