package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class initTables {
	private Connection con;
	private Statement stmt;

	public initTables(Connection con) {
		this.con = con;
	}

	public void createUsers() {
		String sql = "CREATE TABLE IF NOT EXISTS users (\n" + "	uid int(11) NOT NULL AUTO_INCREMENT,\n"
				+ "	username varchar(25) NOT NULL,\n" + "	password varchar(25) NOT NULL,\n"
				+ "	email varchar(25) NOT NULL,\n" + "	firstName varchar(25) NOT NULL,\n"
				+ "	lastName varchar(25) NOT NULL,\n" + "	ID varchar(25) NOT NULL,\n"
				+ "	roleType varchar(10) NOT NULL,\n" + " fid varchar(25) NOT NULL,\n" + " PRIMARY KEY (uid));";

		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);

			stmt = con.createStatement();
			int rs = stmt.executeUpdate(
					"INSERT INTO users (username, password, email, firstName, LastName, ID, roleType, fid) VALUES ('s', '1111', 's@g.com', 'tomer', 'levi', '212352534', 'student','01')");
			rs = stmt.executeUpdate(
					"INSERT INTO users (username, password, email, firstName, LastName, ID, roleType, fid) VALUES ('t', '1111', 't@g.com', 'Aviv', 'Jibly', '', 'teacher', '02')");
			rs = stmt.executeUpdate(
					"INSERT INTO users (username, password, email, firstName, LastName, ID, roleType, fid) VALUES ('p', '1111', 'p@g.com', 'Elon', 'Musk', '', 'principle', '03')");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createQuestions() {
		String sql = "CREATE TABLE IF NOT EXISTS questions (\n" + "	qid VARCHAR(8) NOT NULL,\n"
				+ "	question text NOT NULL,\n" + "	option1 text NOT NULL,\n" + "	option2 text NOT NULL,\n"
				+ "	option3 text NOT NULL,\n" + "	option4 text NOT NULL,\n" + "	answer INT NOT NULL,\n"
				+ " authorID text NOT NULL,\n" + " PRIMARY KEY (qid));";

		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);

			stmt = con.createStatement();

			String myStatement = " INSERT INTO questions (qid, question, option1, option2, option3, option4, answer, authorID) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement statement = con.prepareStatement(myStatement);
			statement.setString(1, "02000");
			statement.setString(2, "Is it true?");
			statement.setString(3, "Yes");
			statement.setString(4, "No");
			statement.setString(5, "I don't know");
			statement.setString(6, "Maybe");
			statement.setInt(7, 2);
			statement.setString(8, "434232");
			statement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createField() {
		String sql = "CREATE TABLE IF NOT EXISTS field (\n" + "	fid varchar(25) NOT NULL,\n"
				+ "	fieldName varchar(25) NOT NULL,\n" + " PRIMARY KEY (fid));";

		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);

			stmt = con.createStatement();
			int rs = stmt.executeUpdate(
					"INSERT INTO field (fid, fieldName) VALUES ('01', 'Mathematics')");
			rs = stmt.executeUpdate(
					"INSERT INTO field (fid, fieldName) VALUES ('02', 'Physics')");
			rs = stmt.executeUpdate(
					"INSERT INTO field (fid, fieldName) VALUES ('03', 'Art')");
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}

		
	}
	public void createCourses() {
		String sql = "CREATE TABLE IF NOT EXISTS courses (\n" + " cid varchar(5) NOT NULL,\n"
				+ "	courseName varchar(25) NOT NULL,\n" +"fid varchar(25) NOT NULL,\n" +" PRIMARY KEY (cid));";

		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);

			stmt = con.createStatement();
			int rs = stmt.executeUpdate(
					"INSERT INTO courses (cid, courseName,fid) VALUES ('01', 'Algebra','01')");
			rs = stmt.executeUpdate(
					"INSERT INTO courses (cid, courseName,fid) VALUES ('02', 'physica1','02')");
			rs = stmt.executeUpdate(
					"INSERT INTO courses (cid, courseName,fid) VALUES ('03', 'art1','03')");
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}
	}
	public void createExams() {
		String sql="CREATE TABLE IF NOT EXISTS exams (\n" + " eid varchar(6) NOT NULL,\n"
				+" duration varchar(6) NOT NULL,\n"
				+ "	questions SET() NOT NULL,\n" +"teacherComments varchar(100)  NULL,\n" +""
						+ "	pointsPerQuestion SET() NOT NULL,\n" +
				"studentComments varchar(100)  NULL,\n"+" PRIMARY KEY (eid));";
		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);
			stmt=con.createStatement();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	public void questionsinExams() {
		String sql="CREATE TABLE IF NOT EXISTS questionsinExams (\n" + " eid varchar(6) NOT NULL,\n"
				+"	ID varchar(5) NOT NULL,\n"
				+ "	qid varchar(5) NOT NULL,\n" +"pointsPerQuestion varchar(3) NOT NULL\n"+" PRIMARY KEY (ID));";
		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);
			stmt=con.createStatement();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	
}
