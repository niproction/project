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
		table_extra_time_request();
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
	private void table_extra_time_request() {
		String sql="CREATE TABLE `db_cems`.`extra_time_requests` (\r\n"
				+ "  `uID` INT NOT NULL,\r\n"
				+ "  `eiID` VARCHAR(5) NULL,\r\n"
				+ "  `comment` TEXT NULL,\r\n"
				+ "  `extraTime` TIME NULL,\r\n"
				+ "  `isApproved` VARCHAR(7) NULL,\r\n"
				+ "  PRIMARY KEY (`uID`));";
		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);
			stmt = con.createStatement();
		}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}
	//max edited

	private void table_exams() {
		String sql = "CREATE TABLE IF NOT EXISTS exams (\n" 
				+ " eID varchar(10) NOT NULL,\n"
				+"authorID int(10) NOT NULL,\n"
				+ " description TEXT  NULL,\n" 
				+ " duration varchar(6) NOT NULL,\n"
				+ " teacherComments TEXT  NULL,\n" 
				 + " studentComments TEXT  NULL,\n" 
				+ "password TEXT  NULL, \n"+
				 "PRIMARY KEY (eID));";
		
		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);
			stmt = con.createStatement();
			
			String myStatement = " INSERT INTO exams (eID,authorID,description,  duration, teacherComments, studentComments, password) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement statement = con.prepareStatement(myStatement);
			statement.setString(1, "020010");
			statement.setInt(2, 2);
			statement.setString(3, "this is a math exam end of semester");
			statement.setString(4, "02:30");
			statement.setString(5, "hi lo");
			statement.setString(6, "hola hola");
			statement.setString(7, "1234");
			statement.executeUpdate();
			PreparedStatement statement1 = con.prepareStatement(myStatement);
			statement1.setString(1, "020011");
			statement1.setInt(2, 2);
			statement1.setString(3, "this is a test of this shit ");
			statement1.setString(4, "hi");
			statement1.setString(5, "hi lo");
			statement1.setString(6, "hola hola");
			statement1.setString(7, "byebye");
			statement1.executeUpdate();
			PreparedStatement statement2 = con.prepareStatement(myStatement);
			statement2.setString(1, "020012");
			statement2.setInt(2, 2);
			statement2.setString(3, "this is a test of this shit ");
			statement2.setString(4, "hi");
			statement2.setString(5, "hi lo");
			statement2.setString(6, "hola hola");
			statement2.setString(7, "byebye");
			statement2.executeUpdate();
			PreparedStatement statement3 = con.prepareStatement(myStatement);
			statement3.setString(1, "020013");
			statement3.setInt(2, 2);
			statement3.setString(3, "this is a test of this shit ");
			statement3.setString(4, "hi");
			statement3.setString(5, "hi lo");
			statement3.setString(6, "hola hola");
			statement3.setString(7, "byebye");
			statement3.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	//max edited

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
			statement.setString(1, "02001");
			statement.setInt(2, 2);
			statement.setString(3, "Is it true?");
			statement.setString(4, "Yes");
			statement.setString(5, "No");
			statement.setString(6, "I don't know");
			statement.setString(7, "Maybe");
			statement.setString(8, "4");
			statement.executeUpdate();
			PreparedStatement statement1 = con.prepareStatement(myStatement);
			statement1.setString(1, "02002");
			statement1.setInt(2, 2);
			statement1.setString(3, "daniel is gever?");
			statement1.setString(4, "no");
			statement1.setString(5, "yes");
			statement1.setString(6, "pipi");
			statement1.setString(7, "khdg");
			statement1.setString(8, "2");
			statement1.executeUpdate();
			PreparedStatement statement2 = con.prepareStatement(myStatement);
			statement2.setString(1, "02003");
			statement2.setInt(2, 2);
			statement2.setString(3, "daniel is gever?");
			statement2.setString(4, "no");
			statement2.setString(5, "yes");
			statement2.setString(6, "pipi");
			statement2.setString(7, "khdg");
			statement2.setString(8, "2");
			statement2.executeUpdate();
			PreparedStatement statement3 = con.prepareStatement(myStatement);
			statement3.setString(1, "4");
			statement3.setInt(2, 2);
			statement3.setString(3, "daniel is gever?");
			statement3.setString(4, "no");
			statement3.setString(5, "yes");
			statement3.setString(6, "pipi");
			statement3.setString(7, "khdg");
			statement3.setString(8, "2");
			statement3.executeUpdate();
			PreparedStatement statement4= con.prepareStatement(myStatement);
			statement4.setString(1, "5");
			statement4.setInt(2, 2);
			statement4.setString(3, "daniel is gever?");
			statement4.setString(4, "no");
			statement4.setString(5, "yes");
			statement4.setString(6, "pipi");
			statement4.setString(7, "khdg");
			statement4.setString(8, "2");
			statement4.executeUpdate();
			PreparedStatement statement5= con.prepareStatement(myStatement);
			statement5.setString(1, "020103");
			statement5.setInt(2, 2);
			statement5.setString(3, "daniel is gever?");
			statement5.setString(4, "no");
			statement5.setString(5, "yes");
			statement5.setString(6, "pipi");
			statement5.setString(7, "khdg");
			statement5.setString(8, "2");
			statement5.executeUpdate();
			PreparedStatement statement6 = con.prepareStatement(myStatement);
			statement6.setString(1, "021003");
			statement6.setInt(2, 2);
			statement6.setString(3, "daniel is gever?");
			statement6.setString(4, "no");
			statement6.setString(5, "yes");
			statement6.setString(6, "pipi");
			statement6.setString(7, "khdg");
			statement6.setString(8, "2");
			statement6.executeUpdate();
			PreparedStatement statement7 = con.prepareStatement(myStatement);
			statement7.setString(1, "0200543");
			statement7.setInt(2, 2);
			statement7.setString(3, "daniel is gever?");
			statement7.setString(4, "no");
			statement7.setString(5, "yes");
			statement7.setString(6, "pipi");
			statement7.setString(7, "khdg");
			statement7.setString(8, "2");
			statement7.executeUpdate();
			PreparedStatement statement8 = con.prepareStatement(myStatement);
			statement8.setString(1, "0204503");
			statement8.setInt(2, 2);
			statement8.setString(3, "daniel is gever?");
			statement8.setString(4, "no");
			statement8.setString(5, "yes");
			statement8.setString(6, "pipi");
			statement8.setString(7, "khdg");
			statement8.setString(8, "2");
			statement8.executeUpdate();
			PreparedStatement statement9 = con.prepareStatement(myStatement);
			statement9.setString(1, "0200613");
			statement9.setInt(2, 2);
			statement9.setString(3, "daniel is gever?");
			statement9.setString(4, "no");
			statement9.setString(5, "yes");
			statement9.setString(6, "pipi");
			statement9.setString(7, "khdg");
			statement9.setString(8, "2");
			statement9.executeUpdate();
			PreparedStatement statement21 = con.prepareStatement(myStatement);
			statement21.setString(1, "0208603");
			statement21.setInt(2, 2);
			statement21.setString(3, "daniel is gever?");
			statement21.setString(4, "no");
			statement21.setString(5, "yes");
			statement21.setString(6, "pipi");
			statement21.setString(7, "khdg");
			statement21.setString(8, "2");
			statement21.executeUpdate();
			PreparedStatement statement22 = con.prepareStatement(myStatement);
			statement22.setString(1, "02458003");
			statement22.setInt(2, 2);
			statement22.setString(3, "daniel is gever?");
			statement22.setString(4, "no");
			statement22.setString(5, "yes");
			statement22.setString(6, "pipi");
			statement22.setString(7, "khdg");
			statement22.setString(8, "2");
			statement22.executeUpdate();
			PreparedStatement statement32 = con.prepareStatement(myStatement);
			statement32.setString(1, "0200983");
			statement32.setInt(2, 2);
			statement32.setString(3, "daniel is gever?");
			statement32.setString(4, "no");
			statement32.setString(5, "yes");
			statement32.setString(6, "pipi");
			statement32.setString(7, "khdg");
			statement32.setString(8, "2");
			statement32.executeUpdate();
			PreparedStatement statement42 = con.prepareStatement(myStatement);
			statement42.setString(1, "0462003");
			statement42.setInt(2, 2);
			statement42.setString(3, "daniel is gever?");
			statement42.setString(4, "no");
			statement42.setString(5, "yes");
			statement42.setString(6, "pipi");
			statement42.setString(7, "khdg");
			statement42.setString(8, "2");
			statement42.executeUpdate();
			PreparedStatement statement25 = con.prepareStatement(myStatement);
			statement25.setString(1, "1002003");
			statement25.setInt(2, 2);
			statement25.setString(3, "daniel is gever?");
			statement25.setString(4, "no");
			statement25.setString(5, "yes");
			statement25.setString(6, "pipi");
			statement25.setString(7, "khdg");
			statement25.setString(8, "2");
			statement25.executeUpdate();
			PreparedStatement statement26 = con.prepareStatement(myStatement);
			statement26.setString(1, "1102003");
			statement26.setInt(2, 2);
			statement26.setString(3, "daniel is gever?");
			statement26.setString(4, "no");
			statement26.setString(5, "yes");
			statement26.setString(6, "pipi");
			statement26.setString(7, "khdg");
			statement26.setString(8, "2");
			statement26.executeUpdate();
			PreparedStatement statement27 = con.prepareStatement(myStatement);
			statement27.setString(1, "1302003");
			statement27.setInt(2, 2);
			statement27.setString(3, "daniel is gever?");
			statement27.setString(4, "no");
			statement27.setString(5, "yes");
			statement27.setString(6, "pipi");
			statement27.setString(7, "khdg");
			statement27.setString(8, "2");
			statement27.executeUpdate();
			PreparedStatement statement28 = con.prepareStatement(myStatement);
			statement28.setString(1, "01552003");
			statement28.setInt(2, 2);
			statement28.setString(3, "daniel is gever?");
			statement28.setString(4, "no");
			statement28.setString(5, "yes");
			statement28.setString(6, "pipi");
			statement28.setString(7, "khdg");
			statement28.setString(8, "2");
			statement28.executeUpdate();
			

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
//max edited
	private void table_exam_questions() {
		String sql = "CREATE TABLE IF NOT EXISTS exam_questions (\n" + " eID varchar(10) NOT NULL,\n"
				+ "	qID varchar(5) NOT NULL,\n" + " points varchar(4) NOT NULL);";
		try {
			Statement stmt = con.createStatement();
			// create a new table
			stmt.execute(sql);
			stmt = con.createStatement();
			String myStatement = " INSERT INTO exam_questions (eID, qID, points) VALUES (?,?,?)";
			PreparedStatement statement = con.prepareStatement(myStatement);
			statement.setString(1, "020010");
			statement.setString(2, "02001");
			statement.setString(3, "50");
			statement.executeUpdate();
			PreparedStatement statement1 = con.prepareStatement(myStatement);
			statement1.setString(1, "020010");
			statement1.setString(2, "02002");
			statement1.setString(3, "50");
			statement1.executeUpdate();
			PreparedStatement statement2 = con.prepareStatement(myStatement);
			statement2.setString(1, "020011");
			statement2.setString(2, "02002");
			statement2.setString(3, "50");
			statement2.executeUpdate();
			PreparedStatement statement3 = con.prepareStatement(myStatement);
			statement3.setString(1, "020011");
			statement3.setString(2, "02003");
			statement3.setString(3, "20");
			statement3.executeUpdate();
			PreparedStatement statement4 = con.prepareStatement(myStatement);
			statement4.setString(1, "020011");
			statement4.setString(2, "02001");
			statement4.setString(3, "30");
			statement4.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void table_exams_initiated() {
		String sql = "CREATE TABLE IF NOT EXISTS exams_initiated (\n" + " eiID varchar(7) NOT NULL,\n"
				+ "	eID varchar(7) NOT NULL,\n" + "	uID varchar(5) NOT NULL,\n" + " time varchar(3) NOT NULL);";
		try {
			Statement stmt = con.createStatement();
			stmt.execute(sql);
			stmt = con.createStatement();
			String myStatement = " INSERT INTO exams_initiated (eiID,eID, uID, time) VALUES (?,?,?,?)";
			PreparedStatement statement = con.prepareStatement(myStatement);
			statement.setString(1, "1");
			statement.setString(2, "020111");
			statement.setString(3, "2");
			statement.setString(4, "60");
			statement.executeUpdate();
			PreparedStatement statement1 = con.prepareStatement(myStatement);
			statement1.setString(1, "2");
			statement1.setString(2, "020212");
			statement1.setString(3, "2");
			statement1.setString(4, "60");
			statement1.executeUpdate();
			PreparedStatement statement2 = con.prepareStatement(myStatement);
			statement2.setString(1, "3");
			statement2.setString(2, "020112");
			statement2.setString(3, "2");
			statement2.setString(4, "60");
			statement2.executeUpdate();
			PreparedStatement statement3 = con.prepareStatement(myStatement);
			statement3.setString(1, "4");
			statement3.setString(2, "020013");
			statement3.setString(3, "2");
			statement3.setString(4, "60");
			statement3.executeUpdate();
			PreparedStatement statement4 = con.prepareStatement(myStatement);
			statement4.setString(1, "5");
			statement4.setString(2, "020011");
			statement4.setString(3, "2");
			statement4.setString(4, "60");
			statement4.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void table_exams_done() {
		//Barak:added grade and is approved 
		String sql = "CREATE TABLE IF NOT EXISTS exams_done (\n" + " edID varchar(7) NOT NULL,\n"
				+ " eiID varchar(7) NOT NULL,\n" + " uID varchar(6) NOT NULL,\n" + " duration varchar(6) NOT NULL,\n"
				+ " startTime varchar(6) NOT NULL,\n" + " endTime varchar(6) NOT NULL,\n"+"isApproved varchar(6) NULL,\n"
				+"grade INT NULL);";
		
		try {
			Statement stmt = con.createStatement();
			String myStatement = " INSERT INTO exams_done (edID,eiId,uID,duration,startTime,endTime,isApproved,grade)  VALUES (?,?,?,?,?,?,?,?)";			
			stmt.execute(sql);
			stmt = con.createStatement();
			PreparedStatement statement = con.prepareStatement(myStatement);
			statement.setString(1, "1");
			statement.setString(2, "1");
			statement.setString(3, "1");
			statement.setString(4, "50");
			statement.setString(5, "50");
			statement.setString(6, "50");
			statement.setString(7, "50");
			statement.setString(8, "100");
			statement.executeUpdate();
			PreparedStatement statement1 = con.prepareStatement(myStatement);
			statement1.setString(1, "2");
			statement1.setString(2, "1");
			statement1.setString(3, "4");
			statement1.setString(4, "50");
			statement1.setString(5, "50");
			statement1.setString(6, "50");
			statement1.setString(7, "50");
			statement1.setString(8, "70");
			statement1.executeUpdate();
			PreparedStatement statement2 = con.prepareStatement(myStatement);
			statement2.setString(1, "3");
			statement2.setString(2, "1");
			statement2.setString(3, "5");
			statement2.setString(4, "50");
			statement2.setString(5, "50");
			statement2.setString(6, "50");
			statement2.setString(7, "50");
			statement2.setString(8, "30");
			statement2.executeUpdate();	
			PreparedStatement statement3 = con.prepareStatement(myStatement);
			statement3.setString(1, "4");
			statement3.setString(2, "2");
			statement3.setString(3, "1");
			statement3.setString(4, "50");
			statement3.setString(5, "50");
			statement3.setString(6, "50");
			statement3.setString(7, "50");
			statement3.setString(8, "95");
			statement3.executeUpdate();
			PreparedStatement statement4 = con.prepareStatement(myStatement);
			statement4.setString(1, "5");
			statement4.setString(2, "2");
			statement4.setString(3, "4");
			statement4.setString(4, "50");
			statement4.setString(5, "50");
			statement4.setString(6, "50");
			statement4.setString(7, "50");
			statement4.setString(8, "30");
			statement4.executeUpdate();
			PreparedStatement statement5 = con.prepareStatement(myStatement);
			statement5.setString(1, "6");
			statement5.setString(2, "2");
			statement5.setString(3, "5");
			statement5.setString(4, "50");
			statement5.setString(5, "50");
			statement5.setString(6, "50");
			statement5.setString(7, "50");
			statement5.setString(8, "30");
			statement5.executeUpdate();	
			PreparedStatement statement6 = con.prepareStatement(myStatement);
			statement6.setString(1, "5");
			statement6.setString(2, "3");
			statement6.setString(3, "4");
			statement6.setString(4, "50");
			statement6.setString(5, "50");
			statement6.setString(6, "50");
			statement6.setString(7, "50");
			statement6.setString(8, "90");
			statement6.executeUpdate();
			PreparedStatement statement7 = con.prepareStatement(myStatement);
			statement7.setString(1, "6");
			statement7.setString(2, "3");
			statement7.setString(3, "5");
			statement7.setString(4, "50");
			statement7.setString(5, "50");
			statement7.setString(6, "50");
			statement7.setString(7, "50");
			statement7.setString(8, "80");
			statement7.executeUpdate();			
	
	
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