package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class initTables {
	private Connection con;
	private Statement stmt;
	
	public initTables(Connection con)
	{
		
	}
	public void createUsers()
	{
		String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	uid integer PRIMARY KEY,\n"
                + "	username varchar(25) NOT NULL,\n"
                + "	password varchar(25) NOT NULL,\n"
                + "	email varchar(25) NOT NULL,\n"
                + "	typeRole varchar(10) NOT NULL\n"
                + ");";
        
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
                + "	qid integer PRIMARY KEY,\n"
                + "	question text NOT NULL,\n"
                + "	option1 text NOT NULL,\n"
                + "	option2 text NOT NULL,\n"
                + "	option3 text NOT NULL,\n"
                + "	option4 text NOT NULL,\n"
                + "	answer int NOT NULL\n"
                + ");";
        
        try {
        	Statement stmt = con.createStatement();
            // create a new table
            stmt.execute(sql);
            
            stmt=con.createStatement();
			int rs=stmt.executeUpdate("INSERT INTO questions (question, option1, option2, option3, option4, answer) VALUES ('Is it true?', 'Yes', 'No','I don't know', 'Maybe', 3)");
			rs=stmt.executeUpdate("INSERT INTO questions (question, option1, option2, option3, option4, answer) VALUES ('Is it true?, again', 'Yes', 'No','I don't know', 'Maybe', 2)");
			
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
}
