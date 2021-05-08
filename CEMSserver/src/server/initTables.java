package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class initTables {
	private Connection con;
	private Statement stmt;
	
	public initTables(Connection con)
	{
		this.con=con;
	}
	public void createUsers()
	{
		String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	uid int(11) NOT NULL AUTO_INCREMENT,\n"
                + "	username varchar(25) NOT NULL,\n"
                + "	password varchar(25) NOT NULL,\n"
                + "	email varchar(25) NOT NULL,\n"
                + "	roleType varchar(10) NOT NULL,\n"
                + " PRIMARY KEY (uid));";
        
        try {
        	Statement stmt = con.createStatement();
            // create a new table
            stmt.execute(sql);
            
            stmt=con.createStatement();
			int rs=stmt.executeUpdate("INSERT INTO users (username, password, email, roleType) VALUES ('s', '1111', 's@g.com','student')");
			rs=stmt.executeUpdate("INSERT INTO users (username, password, email, roleType) VALUES ('t', '1111', 't@g.com','teacher')");
			rs=stmt.executeUpdate("INSERT INTO users (username, password, email, roleType) VALUES ('p', '1111', 'p@g.com','principle')");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public void createQuestions()
	{
		String sql = "CREATE TABLE IF NOT EXISTS questions (\n"
                + "	qid INT NOT NULL AUTO_INCREMENT,\n"
                + "	question text NOT NULL,\n"
                + "	option1 text NOT NULL,\n"
                + "	option2 text NOT NULL,\n"
                + "	option3 text NOT NULL,\n"
                + "	option4 text NOT NULL,\n"
                + "	answer INT NOT NULL,\n"
                + " PRIMARY KEY (qid));";
        
        try {
        	Statement stmt = con.createStatement();
            // create a new table
            stmt.execute(sql);
            
            stmt=con.createStatement();
			//int rs=stmt.executeUpdate("INSERT INTO questions (question, option1, option2, option3, option4, answer) VALUES ('Is it true?', 'Yes', 'No','I don\'t know', 'Maybe', 3)");
			String myStatement = " INSERT INTO questions (question, option1, option2, option3, option4, answer) VALUES (?,?,?,?,?,?)";
			PreparedStatement statement= con.prepareStatement   (myStatement );
			statement.setString(1,"Is it true?");
			statement.setString(2,"Yes");
			statement.setString(3,"No");
			statement.setString(4,"I don't know");
			statement.setString(5,"Maybe");
			statement.setInt(6,3);
			statement.executeUpdate();
			
			
			myStatement = " INSERT INTO questions (question, option1, option2, option3, option4, answer) VALUES (?,?,?,?,?,?)";
			statement= con.prepareStatement   (myStatement );
			statement.setString(1,"Is it true?, again");
			statement.setString(2,"Yes");
			statement.setString(3,"No");
			statement.setString(4,"I don't know");
			statement.setString(5,"Maybe");
			statement.setInt(6,2);
			statement.executeUpdate();
			
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
}
