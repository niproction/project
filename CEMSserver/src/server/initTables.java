package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class initTables {
	private Connection con;
	private Statement stmt;

	public initTables() {
		this.con = mysqlConnection.getInstance().getCon();
	}

	public void tables_reset() {
		delete_tables();
		create_tables();
	}

	public void create_tables() {
		table_users();
		table_fiels();
		table_courses();
		table_exams();
		table_questions();
		table_exam_questions();
		table_exams_initiated();
		table_exams_done();
		table_exam_questions_answer();
	}

	public void delete_tables() {

		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS users;");
			stmt.executeUpdate("DROP TABLE IF EXISTS fields;");
			stmt.executeUpdate("DROP TABLE IF EXISTS courses;");
			stmt.executeUpdate("DROP TABLE IF EXISTS questions;");
			stmt.executeUpdate("DROP TABLE IF EXISTS exams;");
			stmt.executeUpdate("DROP TABLE IF EXISTS exam_questions;");
			stmt.executeUpdate("DROP TABLE IF EXISTS exams_initiated;");
			stmt.executeUpdate("DROP TABLE IF EXISTS exams_done;");
			stmt.executeUpdate("DROP TABLE IF EXISTS exam_questions_answer;");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.print("problem to delete tables");
		}

	}
	
	public void update_isconnected() {
		try {
			PreparedStatement ps = mysqlConnection.getInstance().getCon().prepareStatement(
					"update users set isConnected=?");
			ps.setString(1, "NO");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void table_users() {
		String sql = "CREATE TABLE IF NOT EXISTS users (\n" + "	uID int(11) NOT NULL AUTO_INCREMENT,\n"
				+ "	username varchar(25) NOT NULL,\n" + "	password varchar(25) NOT NULL,\n"
				+ "	email varchar(25) NOT NULL,\n" + "	firstName varchar(25) NOT NULL,\n"
				+ "	lastName varchar(25) NOT NULL,\n" + "	ID varchar(25) NOT NULL,\n"
				+ "	roleType varchar(10) NOT NULL,\n" + " fID varchar(25) NOT NULL,\n"
				+ " isConnected varchar(3) NOT NULL,\n" + " PRIMARY KEY (uID));";

		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);

			stmt = con.createStatement();
			int rs = stmt.executeUpdate(
					"INSERT INTO users (username, password, email, firstName, LastName, ID, roleType, fID, isConnected) VALUES ('s', '1111', 's@g.com', 'tomer', 'levi', '212352534', 'student','01', 'NO')");
			rs = stmt.executeUpdate(
					"INSERT INTO users (username, password, email, firstName, LastName, ID, roleType, fID, isConnected) VALUES ('t', '1111', 't@g.com', 'Aviv', 'Jibly', '', 'teacher', '02', 'NO')");
			rs = stmt.executeUpdate(
					"INSERT INTO users (username, password, email, firstName, LastName, ID, roleType, fID, isConnected) VALUES ('p', '1111', 'p@g.com', 'Elon', 'Musk', '', 'principle', '03', 'NO')");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void table_fiels() {
		String sql = "CREATE TABLE IF NOT EXISTS fields (\n" + " fID varchar(5) NOT NULL,\n"
				+ "	fieldName varchar(25) NOT NULL,\n" + " PRIMARY KEY (fID));";

		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);

			stmt = con.createStatement();
			int rs = stmt.executeUpdate("INSERT INTO fields (fID, fieldName) VALUES ('01', 'Mathematics')");
			rs = stmt.executeUpdate("INSERT INTO fields (fID, fieldName) VALUES ('02', 'Physics')");
			rs = stmt.executeUpdate("INSERT INTO fields (fID, fieldName) VALUES ('03', 'Art')");
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}

	}

	private void table_courses() {
		String sql = "CREATE TABLE IF NOT EXISTS courses (\n" + " cID varchar(5) NOT NULL,\n"
				+ " fID varchar(5) NOT NULL,\n" + "	courseName varchar(25) NOT NULL,\n" + " PRIMARY KEY (cID));";

		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);

			stmt = con.createStatement();
			int rs = stmt.executeUpdate("INSERT INTO courses (cID, fID, courseName) VALUES ('01', '01', 'Algebra')");
			rs = stmt.executeUpdate("INSERT INTO courses (cID, fID, courseName) VALUES ('02', '02', 'physica1')");
			rs = stmt.executeUpdate("INSERT INTO courses (cID, fID, courseName) VALUES ('03', '03', 'art1')");
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}
	}

	private void table_exams() {
		String sql = "CREATE TABLE IF NOT EXISTS exams (\n" + " eID varchar(10) NOT NULL,\n"
				 + " duration varchar(6) NOT NULL,\n"+
				"author varchar(40) NOT NULL,\n"+
				 " teacherComments TEXT  NULL,\n" + "" + " studentComments TEXT  NULL,\n" + "password TEXT  NULL, \n"+
				 "PRIMARY KEY (eID));";
		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);
			stmt = con.createStatement();
			
			String myStatement = " INSERT INTO exams (eID,  duration,author, teacherComments, studentComments, password) VALUES (?,?,?,?,?,?)";
			PreparedStatement statement = con.prepareStatement(myStatement);
			statement.setString(1, "02000");
			statement.setString(2, "02:30");
			statement.setString(3, "mark berman");
			statement.setString(4, "hi lo");
			statement.setString(5, "hola hola");
			statement.setString(6, "1234");
			statement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void table_questions() {
		String sql = "CREATE TABLE IF NOT EXISTS questions (\n" + "	qID VARCHAR(8) NOT NULL,\n"
				+ " uID text NOT NULL,\n" + "	question text NOT NULL,\n" + "	option1 text NOT NULL,\n"
				+ "	option2 text NOT NULL,\n" + "	option3 text NOT NULL,\n" + "	option4 text NOT NULL,\n"
				+ "	answer INT NOT NULL,\n" + " PRIMARY KEY (qID));";

		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);

			stmt = con.createStatement();

			String myStatement = " INSERT INTO questions (qID, uID, question, option1, option2, option3, option4, answer) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement statement = con.prepareStatement(myStatement);
			statement.setString(1, "02000");
			statement.setInt(7, 2);
			statement.setString(2, "Is it true?");
			statement.setString(3, "Yes");
			statement.setString(4, "No");
			statement.setString(5, "I don't know");
			statement.setString(6, "Maybe");
			statement.setString(8, "434232");
			statement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void table_exam_questions() {
		String sql = "CREATE TABLE IF NOT EXISTS exam_questions (\n" + " eID varchar(10) NOT NULL,\n"
				+ "	qID varchar(5) NOT NULL,\n" + " points varchar(4) NOT NULL);";
		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);
			stmt = con.createStatement();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void table_exams_initiated() {
		String sql = "CREATE TABLE IF NOT EXISTS exams_initiated (\n" + " eiID varchar(7) NOT NULL,\n"
				+ "	eID varchar(7) NOT NULL,\n" + "	uID varchar(5) NOT NULL,\n" + " time TIME NOT NULL);";
		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);
			stmt = con.createStatement();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void table_exams_done() {
		String sql = "CREATE TABLE IF NOT EXISTS exams_done (\n" + " edID varchar(7) NOT NULL,\n"
				+ " eiID varchar(7) NOT NULL,\n" + " uID varchar(6) NOT NULL,\n" + " duration varchar(6) NOT NULL,\n"
				+ " startTime varchar(6) NOT NULL,\n" + " endTime varchar(6) NOT NULL);";
		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);
			stmt = con.createStatement();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void table_exam_questions_answer() {
		String sql = "CREATE TABLE IF NOT EXISTS exam_questions_answer (\n" + " edID varchar(7) NOT NULL,\n"
				+ " qID varchar(6) NOT NULL,\n" + " answer varchar(6) NOT NULL);";
		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);
			stmt = con.createStatement();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
