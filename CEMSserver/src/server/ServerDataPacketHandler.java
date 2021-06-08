package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import common.DataPacket;
import common.Exam;
import common.ExtraTimeRequest;
import common.Field;
import common.IncomingDataPacketHandler;
import common.Principal;
import common.Question;
import common.Student;
import common.Teacher;
import common.User;
import common.Course;
import common.examInitiated;

public class ServerDataPacketHandler implements IncomingDataPacketHandler {
	private static int edID = 1;

	@Override
	public DataPacket CheckRequestExecuteCreateResponce(Object msg) {
		if (msg instanceof DataPacket && ((DataPacket) msg).getSendTo() == DataPacket.SendTo.SERVER)
			return ParsingDataPacket((DataPacket) msg);
		else
			System.out.println("not instance of");
		return null;
	}

	@Override
	public DataPacket ParsingDataPacket(DataPacket dataPacket) {
		DataPacket Responce_dataPacket = null;

		/////////////////////////////////////////////
		// general requests
		////////////////////////////////////////////////

		if (dataPacket.getRequest() == DataPacket.Request.LOGIN) {
			if (dataPacket.getData_parameters().get(0) instanceof String
					&& dataPacket.getData_parameters().get(1) instanceof String) {
				Statement stmt;
				try {
					stmt = mysqlConnection.getInstance().getCon().createStatement();

					ResultSet rs = stmt.executeQuery(
							"SELECT * from users WHERE (username='" + (String) (dataPacket.getData_parameters().get(0))
									+ "' OR email='" + (String) (dataPacket.getData_parameters().get(0))
									+ "') AND password='" + (String) (dataPacket.getData_parameters().get(1)) + "'");

					System.out.println(dataPacket.getData_parameters().get(0) + "< looking for in in DB");

					if (rs.next()) {

						System.out.println("found");
						System.out.println(rs.getString(2));

						// check isCOnnected column if user already connected so send error message to
						// the client
						if (rs.getString(10).equals("YES")) {
							System.out.print("user already connected");
							return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN, null,
									"This user is already in the system", false);
						}

						ArrayList<Object> parameter = new ArrayList<Object>();
						// Object pass_user=null;
						String roleType = rs.getString(8);

						if (roleType.equals("student")) {
							System.out.println("detected student user");
							Student pass_user = new Student(rs.getInt(1), rs.getString(2), rs.getString(3),
									rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(9));
							parameter.add(pass_user);
						} else if (roleType.equals("teacher")) {
							System.out.println("detected teacher user");
							Teacher pass_user = new Teacher(rs.getInt(1), rs.getString(2), rs.getString(3),
									rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(9));
							System.out.println(rs.getString(7) + "ffffffffffffffff");
							parameter.add(pass_user);
						} else if (roleType.equals("principle")) {
							System.out.println("detected principal user");
							Principal pass_user = new Principal(rs.getInt(1), rs.getString(2), rs.getString(3),
									rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(9));
							parameter.add(pass_user);
						} else {
							System.out.println("detected Problem");
						}

						Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN,
								parameter, "", true); // create DataPacket that contains true to indicate that the user
														// information is correct

						// update the isConnected column to YES to indicate that the user is in the
						// system
						PreparedStatement ps = mysqlConnection.getInstance().getCon()
								.prepareStatement("UPDATE users SET isConnected=? WHERE uID=?");

						ps.setString(1, "YES");
						ps.setString(2, rs.getString(1));
						int success = ps.executeUpdate();

						System.out.println("end search");
					} else
						Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN, null,
								"", false); // create DataPacket user information is inccorect...
				} catch (SQLException e) {
					e.printStackTrace();
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN, null, "",
							false); // create DataPacket user information is inccorect...
				}
			} else
				System.out.println("not instance of");

		}

		else if (dataPacket.getRequest() == DataPacket.Request.LOGOUT) {

			System.out.println("dsadasdas " + ((User) dataPacket.getData_parameters().get(0)).getuID());
			try {
				PreparedStatement ps = mysqlConnection.getInstance().getCon()
						.prepareStatement("UPDATE users SET isConnected=? WHERE uID=?");

				ps.setString(1, "NO");
				ps.setString(2, String.valueOf(((User) dataPacket.getData_parameters().get(0)).getuID()));

				int success = ps.executeUpdate();

				System.out.println("user logout");

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGOUT, null, "",
						true); // create DataPacket user information is inccorect...
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
	
		
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////////

		
		
		
		///////////////////////////////
		// Student requests
		//////////////////////////////////////////////////
		
		
		
		
		
		

		// nissan
		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAM) {
			System.out.println("Entered GET_EXAM");
			
			String password = (String) dataPacket.getData_parameters().get(0);
			Statement stmt;
			
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery("SELECT * from exams_initiated WHERE password='" + password + "'");

				if (rs.next()) {

					System.out.println("found exam");

					ArrayList<Object> parameter = new ArrayList<Object>();
					// Object pass_user=null;
					Exam exam = new Exam();

					examInitiated examInitiated = new examInitiated();
					examInitiated.setEiID(rs.getString(1));
					examInitiated.seteID(rs.getString(2));
					examInitiated.setuID(rs.getString(3));
					examInitiated.setTime(rs.getString(4));
					examInitiated.setPassword(rs.getString(5));
					parameter.add(examInitiated);
					Statement stmt2 = mysqlConnection.getInstance().getCon().createStatement();

					ResultSet rs2 = stmt2
							.executeQuery("SELECT * from exams WHERE eID='" + examInitiated.geteID() + "'");

					if (rs2.next()) {
						System.out.println("sss");
						exam.setExamID(rs2.getString(1));
						exam.setAuthorID(rs2.getInt(2));
						exam.setDescription(rs2.getString(3));
						exam.setDuration(rs2.getString(4));
						exam.setTeacherComments(rs2.getString(5));
						exam.setStudentsComments(rs2.getString(6));
						parameter.add(exam);
					}
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM,
							parameter, "", true);
					System.out.println("Made response packet");
				} else
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM, null,
							"", true);
			} catch (Exception e) {
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM, null, "",
						true);
			}
		}
		

		else if (dataPacket.getRequest() == DataPacket.Request.GET_TEST_QUESTIONS) {
			System.out.println("Entered GET_TEST_QUESTIONS");
			
			
			Exam exam2 = new Exam();
			examInitiated exam = (examInitiated) dataPacket.getData_parameters().get(0);
			String examID = exam.geteID();
			System.out.println("the test id is:" + examID);
			ArrayList<Question> questionsfortest = new ArrayList<Question>();
			Statement stmt;

			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery(
						"SELECT  questions.qID, questions.authorID, questions.question, questions.option1,"
								+ " questions.option2, questions.option3,"
								+ " questions.option4, questions.answer FROM exams INNER JOIN exam_questions ON exams.eID=exam_questions.eID"
								+ " INNER JOIN questions ON exam_questions.qID=questions.qID WHERE exams.eID='" + examID+"';");

				while (rs.next()) {

					System.out.println("found exammmmmmm");
					Question question = new Question();
					question.setqID(rs.getString(1));
					//question.setAuthorID(rs.getString(2));
					question.setInfo(rs.getString(2));
					question.setOption1(rs.getString(3));
					question.setOption2(rs.getString(4));
					question.setOption3(rs.getString(5));
					question.setOption4(rs.getString(6));
					question.setAnswer(rs.getString(7));
					
					questionsfortest.add(question);
					System.out.println(question.getqID() + "aaaaaaaaa");
				}

				exam2.setQuestions(questionsfortest);
				
				ArrayList<Object> parameter = new ArrayList<Object>();
				parameter.add(exam2);
				
				// calculate the time left for the exam
				Time timeleft=null;
				
				parameter.add(timeleft);
				
				
				
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_TEST_QUESTIONS,
						parameter, "", true);
				
				System.out.println("Made response packet");
			} catch (Exception e) {
				e.printStackTrace();
				// Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
				// DataPacket.Request.GET_TEST_QUESTIONS,
				// null, "", true);
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		/////////////////////////////////
		// Teacher requests
		///////////////////////////////////////////////

		////// rostik
		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAMS_BY_TEACHER) {
			User user = (User) dataPacket.getData_parameters().get(0);
			int userID = user.getuID();
			System.out.println("the user id is:" + userID);
			ArrayList<Object> ExamsByTeacher = new ArrayList<>();
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery("SELECT * FROM exams WHERE authorID='" + userID + "';");

				while (rs.next()) {
					System.out.println(rs.getString(1));
					ExamsByTeacher.add(new Exam(rs.getString(1), 0, rs.getString(3), null, null, null, null));
				}
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAMS_BY_TEACHER,
						ExamsByTeacher, null, true);

			} catch (SQLException e) {
				e.printStackTrace();
				// Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
				// DataPacket.Request.GET_EXAMS_BY_TEACHER, null, null, false);
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.START_EXAM) {
			Exam exam = (Exam) dataPacket.getData_parameters().get(0);
			Statement stmt;
			
			// set the date and time
			java.util.Date dt = new java.util.Date();
			SimpleDateFormat dateandtimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDatAndTime = dateandtimeFormat.format(dt);
			
			try {
				
				String myStatement = " INSERT INTO exams_initiated (eID, uID, password, initiatedDate, isFinished) VALUES (?,?,?,?,?)";
				PreparedStatement statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
				
				statement.setString(1, ((Exam) dataPacket.getData_parameters().get(0)).getExamID());
				statement.setInt(2, ((User) dataPacket.getData_parameters().get(1)).getuID());
				statement.setString(3, (String)dataPacket.getData_parameters().get(2)); // the password for the exam
				statement.setString(4, currentDatAndTime);
				statement.setString(5, "started");
				statement.executeUpdate();
				
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.START_EXAM,
						null, null, true);
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				// Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
				// DataPacket.Request.GET_EXAMS_BY_TEACHER, null, null, false);
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.ADD_DONE_EXAM) {

			String eiID = ((examInitiated) dataPacket.getData_parameters().get(0)).getEiID();
			int uID = ((User) dataPacket.getData_parameters().get(1)).getuID();
			String duration = (String) dataPacket.getData_parameters().get(2);
			String startTime = (String) dataPacket.getData_parameters().get(3);
			String endTime = (String) dataPacket.getData_parameters().get(4);
			String isAprroved = "WAITING";
			ArrayList<Question> testQuestions = (ArrayList<Question>) dataPacket.getData_parameters().get(5);
			ArrayList<String> answers = (ArrayList<String>) dataPacket.getData_parameters().get(6);
			int grade = 0;
			for (int i = 0; i < testQuestions.size(); i++) {
				Statement stmt;
				Statement stmt2;
				try {
					stmt = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs = stmt.executeQuery(
							"SELECT answer from questions WHERE (qID='" + testQuestions.get(i).getqID() + "') ");
					if (rs.next()) {

						System.out.println("found question name");
						if (rs.getString(1).equals(answers.get(i))) {
							stmt2 = mysqlConnection.getInstance().getCon().createStatement();
							ResultSet rs2 = stmt2.executeQuery("SELECT points from exam_questions WHERE (qID='"
									+ testQuestions.get(i).getqID() + "') ");
							if (rs2.next()) {
								grade = Integer.parseInt(rs2.getString(1));
								System.out.println(grade + "--" + testQuestions.get(i).getqID());
							}

						}
					} else
						System.out.println("problemmmm");
				} catch (Exception e) {
					System.out.println("problemmmm22222");
				}
			}
			String myStatement = " INSERT INTO exams_done (eiID, uID, duration, startTime, endTime, isApproved, grade) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement statement;
			try {
				statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
				statement.setString(1, eiID);
				statement.setInt(2, uID);
				statement.setString(3, duration);
				statement.setString(4, startTime);
				statement.setString(5, endTime);
				statement.setString(6, isAprroved);
				statement.setString(7, grade + "");

				statement.executeUpdate();
				ArrayList<Object> parameter = new ArrayList<Object>();
				// Object pass_user=null;
				parameter.add("success");
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.ADD_DONE_EXAM,
						parameter, "", true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < testQuestions.size(); i++) {
				String myStatement2 = " INSERT INTO exam_questions_answer (edID, qID, answer) VALUES (?,?,?)";
				PreparedStatement statement2;
				try {
					statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
					String edIDString = edID + "";
					statement.setString(1, edIDString);
					statement.setString(2, testQuestions.get(i).getqID());
					statement.setString(3, answers.get(i));

					statement.executeUpdate();
					System.out.println("succeseded insert exam-quesiton-answer");

				} catch (Exception e) {
					System.out.println("problemmmm666666");
				}
			}
			edID++;

		} else if (dataPacket.getRequest() == DataPacket.Request.GET_FIELD_NAME) {
			User user = ((User) dataPacket.getData_parameters().get(0));
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				System.out.println(user.getfid());
				ResultSet rs = stmt.executeQuery("SELECT * from fields WHERE (fID='" + user.getfid() + "') ");

				if (rs.next()) {

					System.out.println("found field name");
					System.out.println(rs.getString(2));

					ArrayList<Object> parameter = new ArrayList<Object>();
					// Object pass_user=null;
					parameter.add(rs.getString(2));
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_FIELD_NAME,
							parameter, "", true);
				} else
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_FIELD_NAME,
							null, "", true);
			} catch (Exception e) {
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_FIELD_NAME, null,
						"", true);
			}

		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION) {
			System.out.println("get questionnnnn");
			String questionID = (String) dataPacket.getData_parameters().get(0);
			User user = (User) dataPacket.getData_parameters().get(1);
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery("SELECT * from questions WHERE (qID='" + questionID + "') "
						+ "AND (authorID= '" + user.getuID() + "')");
				System.out.println(questionID + "< looking for in in DB");
				if (rs.next()) {

					System.out.println("found question");
					System.out.println(rs.getString(2));

					ArrayList<Object> parameter = new ArrayList<Object>();
					// Object pass_user=null;
					Question question = new Question();
					question.setqID(rs.getString(1));
					question.setAuthorID(rs.getInt(2));
					question.setInfo(rs.getString(3));
					question.setOption1(rs.getString(4));
					question.setOption2(rs.getString(5));
					question.setOption3(rs.getString(6));
					question.setOption4(rs.getString(7));
					question.setAnswer(rs.getString(8));

					parameter.add(question);
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_QUESTION,
							parameter, "", true);
				} else
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_QUESTION,
							null, "", true);
			} catch (Exception e) {
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_QUESTION, null,
						"", true);
			}

		}

		else if (dataPacket.getRequest() == DataPacket.Request.EDIT_QUESTION) {
			ArrayList<Object> parameters = new ArrayList<>();
			System.out.println("got into edit question");
			Question question = (Question) dataPacket.getData_parameters().get(0);
			User user = (User) dataPacket.getData_parameters().get(1);
			PreparedStatement ps;
			try {
				ps = mysqlConnection.getInstance().getCon().prepareStatement(
						"UPDATE questions SET qID=?,uID=?,question=?,option1=?,option2=?,option3=?,option4=?,answer=? where qID =?");

				ps.setString(1, question.getqID());
				ps.setInt(2, user.getuID());
				ps.setString(3, question.getInfo());
				ps.setString(4, question.getOption1());
				ps.setString(5, question.getOption2());
				ps.setString(6, question.getOption3());
				ps.setString(7, question.getOption4());
				ps.setString(8, question.getAnswer());
				ps.setString(9, question.getqID());
				int success = ps.executeUpdate();
				parameters.add(question);
				if (success == 1)
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.EDIT_QUESTION,
							parameters, "", true);
				else
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.EDIT_QUESTION,
							null, "", true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.ADD_NEW_QUESTION) {
			Statement stmt2;

			Question question = (Question) dataPacket.getData_parameters().get(0);
			User user = (User) dataPacket.getData_parameters().get(1);
			try {
				stmt2 = mysqlConnection.getInstance().getCon().createStatement();
				String query = "select count(*) from questions where qID like '" + question.getqID() + "%'";
				// Executing the query
				ResultSet rs2 = stmt2.executeQuery(query);
				// Retrieving the result
				rs2.next();
				int count = rs2.getInt(1);
				count++;
				String myStatement = " INSERT INTO questions (qid, authorID, question, option1, option2, option3, option4, answer) VALUES (?,?,?,?,?,?,?,?)";
				PreparedStatement statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
				String idCounter = count < 10 ? "00" + count : count < 100 ? "0" + count : "" + count;
				statement.setString(1, question.getqID() + idCounter);
				statement.setInt(2, user.getuID());
				statement.setString(3, question.getInfo());
				statement.setString(4, question.getOption1());
				statement.setString(5, question.getOption2());
				statement.setString(6, question.getOption3());
				statement.setString(7, question.getOption4());
				statement.setString(8, question.getAnswer());

				statement.executeUpdate();
				System.out.println("question has been saved");

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.ADD_NEW_QUESTION,
						null, "", true); // create DataPacket that contains true to indicate that the user information
											// is correct

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
		


		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSES) {
			Responce_dataPacket = getCourses(dataPacket);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_FIELD_ID) {
			Responce_dataPacket = getQuestionsByFieldID(dataPacket);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_DESCRIPTION) {
			System.out.println("before method getQuestionDescription");
			Responce_dataPacket = getQuestionIdByDescription(dataPacket);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_USERS) {
			try {
				Statement statement;
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * from users");
				ArrayList<Object> users = new ArrayList<Object>(); // Create an ArrayList object

				while (rs.next()) {
					users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
							rs.getString(6), rs.getString(7)));
				}
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_INFO_USERS, users,
						"", true); // create
			}

			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME) {
			Responce_dataPacket = getCourseID(dataPacket);
		}
		
		else if (dataPacket.getRequest() == DataPacket.Request.INSERT_EXAM) {
			Responce_dataPacket = insertExam(dataPacket);
		}
		
		else if (dataPacket.getRequest() == DataPacket.Request.INSERT_EXAM_QUESTIONS) {
			Responce_dataPacket = insertExamQuestion(dataPacket);
		}
		
		
		
		
		
		

		//////////////////
		// principal requests
		/////////////////////////////////////////// MAX////////////////////////////////////////M

		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_USERS) {// INFO PRINCIPEL
			try {
				Statement statement;
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * from users");
				ArrayList<Object> users = new ArrayList<Object>(); // Create an ArrayList object
				while (rs.next())
					users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
							rs.getString(6), rs.getString(7)));

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_INFO_USERS, users,
						"", true); // create
			}

			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_COURSE) {// INFO PRINCIPEL
			try {
				Statement statement;
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * from courses");
				ArrayList<Object> coursesList = new ArrayList<Object>(); // Create an ArrayList object
				while (rs.next())
					coursesList.add(new Course(rs.getString(1), rs.getString(2), rs.getString(3)));
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_INFO_COURSE,
						coursesList, "", true); // create
			}

			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_FIELD) {// INFO PRINCIPEL
			try {
				Statement statement;
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * from fields");
				ArrayList<Object> filedList = new ArrayList<Object>(); // Create an ArrayList object
				while (rs.next())
					filedList.add(new Field(rs.getString(1), rs.getString(2)));
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_INFO_FIELD,
						filedList, "", true); // create
			}

			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_EXAM) {// INFO PRINCIPEL
			try {
				Statement statement;
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * from exams");
				ArrayList<Object> examList = new ArrayList<Object>(); // Create an ArrayList object
				while (rs.next()) {
					Statement statement1;
					statement1 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rsQ = statement1.executeQuery(
							"SELECT  exam_questions.eID,exam_questions.qID,questions.question,questions.option1,questions.option2,questions.option3,questions.option4,questions.answer, exam_questions.points FROM exam_questions INNER JOIN questions ON exam_questions.qID=questions.qID WHERE exam_questions.eID='"
									+ rs.getString(1) + "';");
					ArrayList<Question> questionsList = new ArrayList<Question>();

					while (rsQ.next()) {
						questionsList
								.add(new Question(rsQ.getString(1), rsQ.getInt(2), rsQ.getString(3), rsQ.getString(4),
										rsQ.getString(5), rsQ.getString(6), rsQ.getString(7), rsQ.getString(8)));
					}
					examList.add(new Exam(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), questionsList));

				}
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_INFO_EXAM,
						examList, "", true); // create
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_QUESTIONS) {// INFO PRINCIPEL

			ArrayList<Object> questionsList = new ArrayList<Object>(); // Create an ArrayList object
			Statement statement;
			System.out.println("sasas1");

			try {
				System.out.println("sasas2");
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * from questions");
				System.out.println("sasas3");
				while (rs.next()) {
					questionsList.add(new Question(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
				}

				System.out.println("sasas4");
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_INFO_QUESTIONS,
						questionsList, "", true); // create

			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		
		
		
		
		
		
		
		// daniel
		///////////////////
		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXTRA_TIME_REQUESTS) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			Statement stmt;

			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * from extra_time_requests WHERE isApproved='waiting' ;");

				while (rs.next()) {

					System.out.println("found extra time request");
					// System.out.println(rs.getString(2));

					// Object pass_user=null;
					Statement stmt2 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs2 = stmt2
							.executeQuery("SELECT firstName, lastName from users WHERE uID='" + rs.getInt(1) + "'; ");
					String full_name = null;
					Statement stmt3 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs3 = stmt3.executeQuery("SELECT fID from users WHERE uID='" + rs.getInt(1) + "'; ");

					String field = null;
					while (rs2.next()) {
						full_name = rs2.getString(1) + " " + rs2.getString(2);
					}
					while (rs3.next()) {
						field = rs3.getString(1);

					}
					Statement stmt4 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs4 = stmt4.executeQuery("SELECT fieldName  from fields WHERE fID='" + field + "'; ");
					while (rs4.next()) {

						field = rs4.getString(1);
					}

					Statement stmt5=mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs5 = stmt5.executeQuery("SELECT eID  from exams_initiated WHERE eiID='" + rs.getString(2)+ "'; ");
					String eID = null;
					String cID = null;
					while (rs5.next()) {

					eID = rs5.getString(1);
					System.out.println(eID);
					cID=eID.substring(2,4);
					System.out.println(cID);
					}
					Statement stmt6=mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs6 = stmt6.executeQuery("SELECT courseName  from courses WHERE cID='" + cID+ "'; ");
					String courseName = null;
					while (rs6.next()) {

						courseName = rs6.getString(1);
				
					}
					System.out.println(courseName);
					ExtraTimeRequest request = new ExtraTimeRequest(full_name, rs.getString(2), rs.getString(3), rs.getTime(4),
							rs.getString(5),courseName,eID,field);
				//	System.out.println(request.toString());

					parameters.add(request);

					//System.out.println(request.toString());
				}

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
						DataPacket.Request.GET_EXTRA_TIME_REQUESTS, parameters, "", true);

			} catch (Exception e) {
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
						DataPacket.Request.GET_EXTRA_TIME_REQUESTS, null, "", true);
			}
		}
		
		// daniel
		///////////////////
		else if(dataPacket.getRequest() == DataPacket.Request.EXTRA_TIME_DECISION) {
		
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				System.out.println(((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getIsApproved());
			int success=	 stmt.executeUpdate("UPDATE extra_time_requests SET isApproved = '"+((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getIsApproved()+"' WHERE eiID='"+((ExtraTimeRequest)dataPacket.getData_parameters().get(0)).getEiID()+ "';");
			System.out.println(success+"\n");
				 Responce_dataPacket=new DataPacket(DataPacket.SendTo.CLIENT,
							DataPacket.Request.EXTRA_TIME_DECISION, null, "", true);;
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
///////////////////////////////////////////////////MAX////////////////////////////////////////M

		return Responce_dataPacket;
	}

	///////////////////////////////////////////////////

	private DataPacket getCourseID(DataPacket dataPacket) {
		Statement statement;
		ArrayList<Object> parameter = new ArrayList<Object>();
		String courseName = (String) dataPacket.getData_parameters().get(0);
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement.executeQuery("SELECT cID from courses WHERE (courseName='" + courseName + "') ");
			if (rs.next()) {
				parameter.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME, parameter, "",
				true);
	}

	private DataPacket insertExamQuestion(DataPacket dataPacket) {
		Statement stmt;
		System.out.println("in insert exam method");
		Exam exam = (Exam) dataPacket.getData_parameters().get(0);
		try {
			stmt = mysqlConnection.getInstance().getCon().createStatement();
			// Retrieving the result
			for (String qID : exam.getMapKey()) {
				System.out.println("current question in the exam^^^ " + exam.getPointsForQuestions(qID));
				System.out.println(qID + "&&& " + exam.getSizeOfMap());
				String myStatement = " INSERT INTO exam_questions (eID, qID, points) VALUES (?,?,?)";
				PreparedStatement statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
				statement.setString(1, exam.getExamID());
				statement.setString(2, qID);
				statement.setString(3, exam.getPointsForQuestions(qID));
				statement.executeUpdate();

			}
			return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.INSERT_EXAM_QUESTIONS, null, "", true);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private DataPacket insertExam(DataPacket dataPacket) {
		Statement stmt;
		Exam exam = (Exam) dataPacket.getData_parameters().get(0);
		try {
			stmt = mysqlConnection.getInstance().getCon().createStatement();
			String query = "select count(*) from exams where eID like '" + exam.getExamID() + "%'";
			System.out.println(query);
			// Executing the query
			ResultSet rs = stmt.executeQuery(query);
			// Retrieving the result
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println(count);
				count++;
				String myStatement = " INSERT INTO exams (eID,authorID, duration, teacherComments, studentComments) VALUES (?,?,?,?,?)";
				PreparedStatement statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
				String idCounter = count < 10 ? "0" + count : count < 100 ? "00" + count : "" + count;
				statement.setString(1, exam.getExamID() + idCounter);
				statement.setInt(2, exam.getAuthorID());
				statement.setString(3, exam.getDuration());
				statement.setString(4, exam.getTeacherComments());
				statement.setString(5, exam.getStudentsComments());
				statement.executeUpdate();
				ArrayList<Object> parameter = new ArrayList<Object>();
				parameter.add(exam.getExamID() + idCounter);
				return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.INSERT_EXAM, parameter, "", true);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	private DataPacket getQuestionIdByDescription(DataPacket dataPacket) {
		String questionDescription = (String) dataPacket.getData_parameters().get(0);
		Statement stmt;
		try {
			stmt = mysqlConnection.getInstance().getCon().createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT qID from questions WHERE (question='" + questionDescription + "')");
			if (rs.next()) {
				ArrayList<Object> parameter = new ArrayList<Object>();
				parameter.add(rs.getString(1));
				return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_QUESTION_BY_DESCRIPTION,
						parameter, "", true);
			} else
				return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_QUESTION_BY_DESCRIPTION, null,
						"", true);
		} catch (SQLException e) {
			return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_QUESTION_BY_DESCRIPTION, null, "",
					true);
		}
	}

	private DataPacket getQuestionsByFieldID(DataPacket dataPacket) {
		Statement statement;
		ArrayList<Object> parameter = new ArrayList<Object>();
		User user = ((User) dataPacket.getData_parameters().get(0));
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT question from questions WHERE qID like '" + user.getfid() + "%'");
			while (rs.next()) {
				System.out.println("found question:" + rs.getString(1));
				parameter.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_QUESTION_BY_FIELD_ID, parameter, "",
				true);
	}

	private DataPacket getCourses(DataPacket dataPacket) {
		Statement statement;
		ArrayList<Object> parameter = new ArrayList<Object>();
		User user = ((User) dataPacket.getData_parameters().get(0));
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT courseName from courses WHERE (fid='" + user.getfid() + "') ");
			while (rs.next()) {
				System.out.println("found course name:" + rs.getString(1));
				parameter.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_COURSES, parameter, "", true);
	}

}
