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
                + "	firstName varchar(25) NOT NULL,\n"
                + "	lastName varchar(25) NOT NULL,\n"
                + "	ID varchar(25) NOT NULL,\n"
                + "	roleType varchar(10) NOT NULL,\n"
                + " PRIMARY KEY (uid));";
        
        try {
        	Statement stmt = con.createStatement();
            // create a new table
            stmt.execute(sql);
            
            stmt=con.createStatement();
			int rs=stmt.executeUpdate("INSERT INTO users (username, password, email, firstName, LastName, ID, roleType) VALUES ('s', '1111', 's@g.com', 'tomer', 'levi', '212352534', 'student')");
			rs=stmt.executeUpdate("INSERT INTO users (username, password, email, firstName, LastName, ID, roleType) VALUES ('t', '1111', 't@g.com', 'Aviv', 'Jibly', '', 'teacher')");
			rs=stmt.executeUpdate("INSERT INTO users (username, password, email, firstName, LastName, ID, roleType) VALUES ('p', '1111', 'p@g.com', 'Elon', 'Musk', '', 'principle')");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public void createQuestions()
	{
		String sql = "CREATE TABLE IF NOT EXISTS questions (\n"
                + "	qid VARCHAR(8) NOT NULL,\n"
                + "	question text NOT NULL,\n"
                + "	option1 text NOT NULL,\n"
                + "	option2 text NOT NULL,\n"
                + "	option3 text NOT NULL,\n"
                + "	option4 text NOT NULL,\n"
                + "	answer INT NOT NULL,\n"
                + " authorID text NOT NULL,\n"
                + " PRIMARY KEY (qid));";
        
        try {
        	Statement stmt = con.createStatement();
            // create a new table
            stmt.execute(sql);
            
            stmt=con.createStatement();
			
			String myStatement = " INSERT INTO questions (qid, question, option1, option2, option3, option4, answer, authorID) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement statement= con.prepareStatement   (myStatement );
			statement.setString(1, "02000");
			statement.setString(2,"Is it true?");
			statement.setString(3,"Yes");
			statement.setString(4,"No");
			statement.setString(5,"I don't know");
			statement.setString(6,"Maybe");
			statement.setInt(7,2);
			statement.setString(8, "434232");
			statement.executeUpdate();
			
			
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	/*CREATE TABLE `db_cems`.`timerequests` (
			  `uid` INT NOT NULL,
			  `timeupdate` TIME NULL,
			  `reqtime` DATETIME NULL,
			  `approval` INT NULL,
			  `approvaltime` DATETIME NULL,
			  PRIMARY KEY (`uid`));*/

}
