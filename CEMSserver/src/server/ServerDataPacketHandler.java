package server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import common.DataPacket;
import common.Exam;
import common.IncomingDataPacketHandler;
import common.MyFile;
import common.Principal;
import common.Question;
import common.Student;
import common.Teacher;
import common.User;
import common.courses;

public class ServerDataPacketHandler implements IncomingDataPacketHandler {

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
							Student pass_user = new Student(rs.getString(1), rs.getString(2), rs.getString(3),
									rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(9));
							parameter.add(pass_user);
						} else if (roleType.equals("teacher")) {
							System.out.println("detected teacher user");
							Teacher pass_user = new Teacher(rs.getString(1), rs.getString(2), rs.getString(3),
									rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(9));
							System.out.println(rs.getString(7) + "ffffffffffffffff");
							parameter.add(pass_user);
						} else if (roleType.equals("principle")) {
							System.out.println("detected principal user");
							Principal pass_user = new Principal(rs.getString(1), rs.getString(2), rs.getString(3),
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
						+ "AND (uID= '" + user.getuid() + "')");
				System.out.println(questionID + "< looking for in in DB");
				if (rs.next()) {

					System.out.println("found question");
					System.out.println(rs.getString(2));

					ArrayList<Object> parameter = new ArrayList<Object>();
					// Object pass_user=null;
					Question question = new Question();
					question.setId(rs.getString(1));
					question.setAutor(rs.getString(2));
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
						"update questions set qID=?,uID=?,question=?,option1=?,option2=?,option3=?,option4=?,answer=? where qID =?");

				ps.setString(1, question.getId());
				ps.setString(2, user.getuid());
				ps.setString(3, question.getInfo());
				ps.setString(4, question.getOption1());
				ps.setString(5, question.getOption2());
				ps.setString(6, question.getOption3());
				ps.setString(7, question.getOption4());
				ps.setString(8, question.getAnswer());
				ps.setString(9, question.getId());
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
				String query = "select count(*) from questions where qID like '" + question.getId() + "%'";
				// Executing the query
				ResultSet rs2 = stmt2.executeQuery(query);
				// Retrieving the result
				rs2.next();
				int count = rs2.getInt(1);
				count++;
				String myStatement = " INSERT INTO questions (qid, uID, question, option1, option2, option3, option4, answer) VALUES (?,?,?,?,?,?,?,?)";
				PreparedStatement statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
				String idCounter = count < 10 ? "00" + count : count < 100 ? "0" + count : "" + count;
				statement.setString(1, question.getId() + idCounter);
				statement.setString(2, user.getuid());
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

		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAM) {
			System.out.println("entered exams");
			String password = (String) dataPacket.getData_parameters().get(0);
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery("SELECT * from exams WHERE password='" + password + "'");

				if (rs.next()) {

					System.out.println("found exam");

					ArrayList<Object> parameter = new ArrayList<Object>();
					// Object pass_user=null;
					Exam exam = new Exam();
					exam.setExamID(rs.getString(1));
					exam.setDuration(rs.getString(2));
					exam.setAuthor(rs.getString(3));
					exam.setTeacherComments(rs.getString(4));
					exam.setStudentsComments(rs.getString(5));
					exam.setPassword(rs.getString(6));
					parameter.add(exam);
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM,
							parameter, "", true);
				} else
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM, null,
							"", true);
			} catch (Exception e) {
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM, null, "",
						true);
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_TEST_QUESTIONS) {
			Exam exam = (Exam) dataPacket.getData_parameters().get(0);
			String examID = exam.getExamID();
			System.out.println("the test id is:" + examID);
			ArrayList<Question> questionsfortest = new ArrayList<Question>();
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery("SELECT  questions.qID, questions.question, questions.option1,"
						+ " questions.option2, questions.option3,"
						+ " questions.option4, questions.answer, questions.uID \"\r\n"
						+ "				+ \"FROM exams INNER JOIN exam_questions ON exams.eID=exam_questions.eID"
						+ " INNER JOIN questions ON exam_questions.qID=questions.qID WHERE exams.eID='" + examID
						+ "';");

				while (rs.next()) {

					System.out.println("found exammmmmmm");
					Question question = new Question();
					question.setId(rs.getString(1));
					question.setInfo(rs.getString(2));
					question.setOption1(rs.getString(3));
					question.setOption2(rs.getString(4));
					question.setOption3(rs.getString(5));
					question.setOption4(rs.getString(6));
					question.setAnswer(rs.getString(7));
					question.setAutor(rs.getString(8));
					questionsfortest.add(question);
					System.out.println(question.getId() + "aaaaaaaaa");
				}

			} catch (Exception e) {
				e.printStackTrace();
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_TEST_QUESTIONS,
						null, "", true);
			}
			exam.setQuestions(questionsfortest);
			ArrayList<Object> parameter = new ArrayList<Object>();
			parameter.add(exam);
			Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_TEST_QUESTIONS,
					parameter, "", true);

		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSES) {
			Responce_dataPacket = getCourses(dataPacket);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_FIELD_ID) {
			Responce_dataPacket = getQuestionsByFieldID(dataPacket);
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_DESCRIPTION) {
			Responce_dataPacket = getQuestionIdByDescription(dataPacket);
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_STUDENT_GRADES) {
			Responce_dataPacket = getStudentGradeAndExamID(dataPacket);
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_USERS) {
			try {
				Statement statement;
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * from users");
				ArrayList<Object> users = new ArrayList<Object>(); // Create an ArrayList object
				while (rs.next()) {
					users.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getString(7)));
				}
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_INFO_USERS, users,
						"", true); // create
			}

			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}

		} else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME) {
			Responce_dataPacket = getCourseID(dataPacket);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME) {
			Responce_dataPacket = getCourseID(dataPacket);
		} else if (dataPacket.getRequest() == DataPacket.Request.INSERT_Manuel_EXAM_FILE) {
			Responce_dataPacket = insertManuelExamFile(dataPacket);
		} else if (dataPacket.getRequest() == DataPacket.Request.INSERT_EXAM) {
			Responce_dataPacket = insertExam(dataPacket);
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_NAME_BY_COURSE_ID) {
			Responce_dataPacket = getCourseNameByCourseID(dataPacket);
		} else if (dataPacket.getRequest() == DataPacket.Request.INSERT_EXAM_QUESTIONS) {
			Responce_dataPacket = insertExamQuestion(dataPacket);
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_COPY_OF_EXAM) {
			Responce_dataPacket = getCopyOfExam(dataPacket);
		}

		return Responce_dataPacket;
	}

	private DataPacket getCopyOfExam(DataPacket dataPacket) {
		Statement statement;
		ArrayList<String> questionsID = new ArrayList<>();
		ArrayList<String> studentAnswersIndexes = new ArrayList<>();
		ArrayList<String> questionsDescription = new ArrayList<>();
		ArrayList<String> correctAnswersDescription=new ArrayList<>();
		ArrayList<String> studentAnswersDescription=new ArrayList<>();
		ArrayList<String> pointsForQuestion=new ArrayList<>();
		String eiID = (String) dataPacket.getData_parameters().get(1);
		String studentID = (String) dataPacket.getData_parameters().get(0);
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement.executeQuery(
					"SELECT edID from exams_done where (uid='" + studentID + "' and eiID='" + eiID + "');");
			if (rs.next()) {
				System.out.println(rs.getString(1));
				String edID = rs.getString(1);// added edID
				questionsID = getQuestionsIDbyedID(edID);
				studentAnswersIndexes = getStudentAnswersIndexes(questionsID, edID);
				ArrayList<String> answersDescriptions = getCorrectAndStudentAnswersDescription(studentAnswersIndexes, questionsID,pointsForQuestion);// even indexes are the correct answers descriptions
																														// odd indexes are student descriptions
				for (int i = 0; i < answersDescriptions.size(); i++) {
					if(i%2==0)
						correctAnswersDescription.add(questionsDescription.get(i));
					else
						studentAnswersDescription.add(questionsDescription.get(i));
				}
				questionsDescription = getQuestiosnDescription(questionsID);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		ArrayList<Object> parameters=new ArrayList<>();
		parameters.add(questionsDescription);
		parameters.add(studentAnswersDescription);
		parameters.add(correctAnswersDescription);
		parameters.add(pointsForQuestion);
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_COPY_OF_EXAM, parameters, "", true);

	}

	private ArrayList<String> getQuestiosnDescription(ArrayList<String> questionsID) {
		ArrayList<String> questionsDescription = new ArrayList<>();
		Statement statement;
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			for (int i = 0; i < questionsID.size(); i++) {
				ResultSet rs = statement.executeQuery(
						"SELECT question from questions where (qID='" + questionsDescription.get(i) + "');");
				if (rs.next()) {
					questionsDescription.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return questionsDescription;
	}

	private ArrayList<String> getCorrectAndStudentAnswersDescription(ArrayList<String> studentAnswersIndexes,
			ArrayList<String> questionsID, ArrayList<String> pointsForQuestion) {
		Statement statement;
		ArrayList<String> answersDescriptions = new ArrayList<>();
		for (int i = 0; i < questionsID.size(); i++) {
			try {
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT answer,option,points" + studentAnswersIndexes.get(i)
						+ " from questions where (qID='" + questionsID.get(i) + "');");
				if (rs.next()) {
					answersDescriptions.add(rs.getString(1));// correct answer description
					answersDescriptions.add(rs.getString(2));// student answer description
					if(rs.getString(1)==rs.getString(2))
					{
						pointsForQuestion.add(rs.getString(3));
					}
					else
					{
						pointsForQuestion.add("0");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return answersDescriptions;
	}

	private ArrayList<String> getStudentAnswersIndexes(ArrayList<String> questionsID, String edID) {
		ArrayList<String> studentAnswersIndex = new ArrayList<>();
		Statement statement;
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			for (int i = 0; i < questionsID.size(); i++) {
				ResultSet rs = statement.executeQuery("SELECT answer from exam_questions_answer where (qID='"
						+ questionsID.get(i) + "' AND edID='" + edID + "');");
				if (rs.next()) {
					studentAnswersIndex.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return studentAnswersIndex;
	}

	private ArrayList<String> getQuestionsIDbyedID(String edID) {
		ArrayList<String> questionsID = new ArrayList<>();
		Statement statement;
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement.executeQuery("SELECT qID from exam_questions_answer where (edID='" + edID + "');");
			while (rs.next()) {
				System.out.println(rs.getString(1));
				questionsID.add(rs.getString(1));// added edID
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return questionsID;
	}

	private DataPacket getCourseNameByCourseID(DataPacket dataPacket) {
		Statement statement;
		ArrayList<Object> parameter = new ArrayList<Object>();
		ArrayList<String> coursesID = (ArrayList<String>) dataPacket.getData_parameters().clone();
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			for (int i = 0; i < coursesID.size(); i++) {
				ResultSet rs = statement
						.executeQuery("SELECT courseName from courses WHERE (cID='" + coursesID.get(i) + "') ");
				if (rs.next()) {
					System.out.println(rs.getString(1));
					parameter.add(rs.getString(1));// added courses names
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_COURSE_NAME_BY_COURSE_ID, parameter, "",
				true);
	}

	private DataPacket getStudentGradeAndExamID(DataPacket dataPacket) {
		Statement statement;
		ArrayList<Object> parameter = new ArrayList<Object>();
		String userID = ((String) dataPacket.getData_parameters().get(0));
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement.executeQuery("SELECT exams_initiated.eID,exams_done.grade,exams_initiated.eiID"
					+ " from  exams_initiated  INNER JOIN exams_done ON exams_initiated.eiID=exams_done.eiID "
					+ "WHERE (exams_done.uID='" + userID + "'); ");

			while (rs.next()) {
				parameter.add(rs.getString(1));// eID
				parameter.add(rs.getString(2));// grade
				parameter.add(rs.getString(3));// eiID
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_STUDENT_GRADES, parameter, "", true);
	}

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

	private DataPacket insertManuelExamFile(DataPacket dataPacket) {

		int fileSize = ((MyFile) dataPacket.getData_parameters().get(0)).getSize();
		File newFile = new File("manuel_exams/" + (String) dataPacket.getData_parameters().get(1));
		System.out.println(dataPacket.getData_parameters().get(1));
		System.out.println("feesfsf   " + newFile.getName());
		try {
			FileOutputStream fos = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(((MyFile) dataPacket.getData_parameters().get(0)).getMybytearray(), 0,
					((MyFile) dataPacket.getData_parameters().get(0)).getSize());
			bos.flush();
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.INSERT_Manuel_EXAM_FILE, null, "", true);
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
				String myStatement = " INSERT INTO exams (eID, duration, author, teacherComments, studentComments) VALUES (?,?,?,?,?)";
				PreparedStatement statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
				String idCounter = count < 10 ? "0" + count : count < 100 ? "00" + count : "" + count;
				statement.setString(1, exam.getExamID() + idCounter);
				statement.setString(2, exam.getDuration());
				statement.setString(3, exam.getAuthorID());
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
					.executeQuery("SELECT qid from questions WHERE (question='" + questionDescription + "')");
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
					.executeQuery("SELECT question from questions WHERE qid like '" + user.getfid() + "%'");
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
