package server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import common.ExamDone;
import common.ExtraTimeRequest;
import common.Field;
import common.IncomingDataPacketHandler;
import common.MyFile;
import common.Principal;
import common.Question;
import common.Student;
import common.Teacher;
import common.User;
import common.Course;
import common.examInitiated;
import common.exam_question_answer;

public class ServerDataPacketHandler implements IncomingDataPacketHandler {
	private static int edID = 1;

	@Override
	public DataPacket[] CheckRequestExecuteCreateResponce(Object msg) {
		if (msg instanceof DataPacket && ((DataPacket) msg).getSendTo() == DataPacket.SendTo.SERVER)
			return ParsingDataPacket((DataPacket) msg);
		else
			System.out.println("not instance of");
		return new DataPacket[] { null, null };
	}

	@Override
	public DataPacket[] ParsingDataPacket(DataPacket dataPacket) {
		DataPacket Responce_dataPacket = null;
		DataPacket Responce_toAll_dataPackert = null;

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
							Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN,
									null, "This user is already in the system", false);
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
			Statement stmt, stmt1, stmt2;

			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery(
						"SELECT * from exams_initiated WHERE password='" + password + "' AND isFinished='started'");
				System.out.println("select initiated exam");

				if (rs.next()) {
					stmt1 = mysqlConnection.getInstance().getCon().createStatement();

					// chek if the student already attend this exam
					ResultSet rs1 = stmt1.executeQuery("SELECT * from exams_done WHERE eiID='" + rs.getInt(1)
							+ "' AND uID='" + ((User) dataPacket.getData_parameters().get(1)).getuID() + "'");
					System.out.println("selected initiated exam");

					if (rs1.next()) {
						System.out.println("found exam");
						System.out.println("student already made this exam");
						Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM,
								null, "already attend this exam", false);

						System.out.println("Made response packet false");
					} else {
						System.out.println("student didnt made this exam yet");
						ArrayList<Object> parameter = new ArrayList<Object>();
						// Object pass_user=null;
						Exam exam = new Exam();
						System.out.println("ttt1");
						examInitiated examInitiated = new examInitiated();
						examInitiated.setEiID(rs.getInt(1));
						examInitiated.seteID(rs.getString(2));
						examInitiated.setuID(rs.getInt(3));
						examInitiated.setPassword(rs.getString(4));
						examInitiated.setInitiatedDate(rs.getString(5));

						// System.out.println(rs.getString(5).toString());
						System.out.println("tttttt1");
						parameter.add(examInitiated);

						stmt2 = mysqlConnection.getInstance().getCon().createStatement();

						ResultSet rs2 = stmt2
								.executeQuery("SELECT * from exams WHERE eID='" + examInitiated.geteID() + "'");
						System.out.println("tttttttttt1");

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
								parameter, null, true);

						System.out.println("Made response packet");
					}

					System.out.println("Made response packe2t");
				} else
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM, null,
							null, false);

			} catch (Exception e) {
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM, null, null,
						false);
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

			// calculate the time left for the exam
			String timeleft = null;
			java.util.Date dt = new java.util.Date();
			SimpleDateFormat dateandtimeFormat = new SimpleDateFormat("HH:mm:ss");
			String currentTime = dateandtimeFormat.format(dt);
			// Stringexam.getTime()
			// timeleft

			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery(
						"SELECT  questions.qID, questions.authorID, questions.question, questions.option1,"
								+ " questions.option2, questions.option3,"
								+ " questions.option4, questions.answer FROM exams INNER JOIN exam_questions ON exams.eID=exam_questions.eID"
								+ " INNER JOIN questions ON exam_questions.qID=questions.qID WHERE exams.eID='" + examID
								+ "';");

				while (rs.next()) {

					System.out.println("found exammmmmmm");
					Question question = new Question();
					question.setqID(rs.getString(1));
					// question.setAuthorID(rs.getString(2));
					question.setInfo(rs.getString(3));
					question.setOption1(rs.getString(4));
					question.setOption2(rs.getString(5));
					question.setOption3(rs.getString(6));
					question.setOption4(rs.getString(7));
					question.setAnswer(rs.getString(8));

					questionsfortest.add(question);
					System.out.println(question.getqID() + "aaaaaaaaa");
				}

				exam2.setQuestions(questionsfortest);
				try {
					stmt = mysqlConnection.getInstance().getCon().createStatement();

					ResultSet rs2 = stmt.executeQuery(
							"SELECT studentComments from exams WHERE eID='" + exam.geteID() + "'");
					System.out.println("select initiated exam");

					if (rs2.next()) {
						exam2.setStudentsComments(rs2.getString(1));
					}
				}catch (Exception e) {
					e.printStackTrace();
					System.out.println("error getting students comments");
				}

				ArrayList<Object> parameter = new ArrayList<Object>();
				parameter.add(exam2);

				parameter.add(currentTime); // the server time

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

///////////start barak
		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_FIELD_ID) {
			Responce_dataPacket = getQuestionsByFieldID(dataPacket);

		} else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_DESCRIPTION) {
			Responce_dataPacket = getQuestionIdByDescription(dataPacket);

		} else if (dataPacket.getRequest() == DataPacket.Request.GET_STUDENT_GRADES) {
			System.out.println("ajksfbajkfbasjkfbajk");
			Responce_dataPacket = getStudentGradeAndExamID(dataPacket);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME) {
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
		} ///////////// end barak

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
				statement.setString(3, (String) dataPacket.getData_parameters().get(2)); // the password for the exam
				statement.setString(4, currentDatAndTime);
				statement.setString(5, "started");
				statement.executeUpdate();

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.START_EXAM, null,
						null, true);

			} catch (SQLException e) {
				e.printStackTrace();
				// Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
				// DataPacket.Request.GET_EXAMS_BY_TEACHER, null, null, false);
			}
		} else if (dataPacket.getRequest() == DataPacket.Request.CHECK_TOOK_EXAM) {
			examInitiated examInitiated = (examInitiated) dataPacket.getData_parameters().get(0);
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * from exams_done WHERE (eiID='" + examInitiated.getEiID()
						+ "') AND (uID='" + examInitiated.getuID() + "')");
				if (rs.next()) {
					ArrayList<Object> parameters = new ArrayList<>();
					parameters.add("Exam All Ready Submitted");
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.CHECK_TOOK_EXAM,
							parameters, null, true);
				} else {
					ArrayList<Object> parameters = new ArrayList<>();
					parameters.add("open");
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.CHECK_TOOK_EXAM,
							parameters, null, true);

				}
			} catch (Exception e) {
				System.out.println("errorrrrr");
				e.printStackTrace();
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.ADD_DONE_EXAM) {

			int eiID = ((examInitiated) dataPacket.getData_parameters().get(0)).getEiID();
			int uID = ((User) dataPacket.getData_parameters().get(1)).getuID();
			String duration = (String) dataPacket.getData_parameters().get(2);
			String startTime = (String) dataPacket.getData_parameters().get(3);
			String endTime = (String) dataPacket.getData_parameters().get(4);
			String isAprroved = "WAITING";
			ArrayList<Question> testQuestions = (ArrayList<Question>) dataPacket.getData_parameters().get(5);
			ArrayList<String> answers = (ArrayList<String>) dataPacket.getData_parameters().get(6);
			ArrayList<Integer> isCorrectAnswer=new ArrayList<>();
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
							isCorrectAnswer.add(1);
							stmt2 = mysqlConnection.getInstance().getCon().createStatement();
							ResultSet rs2 = stmt2.executeQuery("SELECT points from exam_questions WHERE (qID='"
									+ testQuestions.get(i).getqID() + "') ");
							if (rs2.next()) {
								grade += Integer.parseInt(rs2.getString(1));
								System.out.println(grade + "--" + testQuestions.get(i).getqID());
							}

						}
						else
							isCorrectAnswer.add(0);
					} else
						System.out.println("problemmmm");
				} catch (Exception e) {
					System.out.println("problemmmm22222");
				}
			}
			String myStatement = " INSERT INTO exams_done (eiID, uID, duration, startTime, endTime, isApproved, grade, isCheating) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement statement;
			try {
				statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
				statement.setInt(1, eiID);
				statement.setInt(2, uID);
				statement.setString(3, duration);
				statement.setString(4, startTime);
				statement.setString(5, endTime);
				statement.setString(6, isAprroved);
				statement.setString(7, grade + "");
				statement.setString(8, "NOT CHEATING");
				statement.executeUpdate();
				ArrayList<Object> parameter = new ArrayList<Object>();
				// Object pass_user=null;
				parameter.add("success");
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.ADD_DONE_EXAM,
						parameter, "", true);
			} catch (SQLException e) {
				e.printStackTrace();
			} //////////////////////////////////////////////////

			for (int i = 0; i < testQuestions.size(); i++) {
				String myStatement2 = " INSERT INTO exam_questions_answer (edID, qID, answer,isCorrect) VALUES (?,?,?,?)";
				PreparedStatement statement2;
				try {
					statement2 = mysqlConnection.getInstance().getCon().prepareStatement(myStatement2);
					statement2.setString(1, edID + "");
					statement2.setString(2, testQuestions.get(i).getqID());
					statement2.setString(3, answers.get(i));
					if(isCorrectAnswer.get(i)==1)
						statement2.setString(4,"CORRECT");
					else
						statement2.setString(4, "INCORRECT");

					statement2.executeUpdate();
					System.out.println("succeseded insert exam-quesiton-answer");

				} catch (Exception e) {
					System.out.println("problemmmm666666");
				}
			}
			edID++;

		} else if (dataPacket.getRequest() == DataPacket.Request.GET_FIELD_NAME) {
			System.out.println("insideee serverrrrr");
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
			User user = (User) dataPacket.getData_parameters().get(0);
			Question qu=(Question)dataPacket.getData_parameters().get(1);
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery("SELECT * from questions WHERE (qID= '"+qu.getqID()+"'");
				ArrayList<Object> parameter = new ArrayList<Object>();
				if (rs.next()) {

					System.out.println("found question");
					System.out.println(rs.getString(2));

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
				}
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_QUESTION,
						parameter, "", true);
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
						"update questions set qID=?,authorID=?,question=?,option1=?,option2=?,option3=?,option4=?,answer=? where qID =?");

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
		} else if (dataPacket.getRequest() == DataPacket.Request.APPROVED_GRADE) {
			String edID = (String) dataPacket.getData_parameters().get(0);
			try {
				PreparedStatement ps = mysqlConnection.getInstance().getCon()
						.prepareStatement("UPDATE exams_done SET isApproved=? WHERE edID=?");

				ps.setString(1, "APPROVED");
				ps.setString(2, edID);
				int success = ps.executeUpdate();
				ArrayList<Object> parameters = new ArrayList<Object>();
				parameters.add("success");
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.APPROVED_GRADE,
						parameters, "", true);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (dataPacket.getRequest() == DataPacket.Request.DISAPPROVED_GRADE) {
			String edID = (String) dataPacket.getData_parameters().get(0);
			try {
				PreparedStatement ps = mysqlConnection.getInstance().getCon()
						.prepareStatement("UPDATE exams_done SET isApproved=? WHERE edID=?");

				ps.setString(1, "DISAPPROVED");
				ps.setString(2, edID);
				int success = ps.executeUpdate();
				ArrayList<Object> parameters = new ArrayList<Object>();
				parameters.add("success");
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.DISAPPROVED_GRADE,
						parameters, "", true);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (dataPacket.getRequest() == DataPacket.Request.GET_FOR_VERIFY) {
			System.out.println("insert get for verify");
			User user = (User) dataPacket.getData_parameters().get(0);
			String eiID = "";
			ArrayList<ExamDone> examDoneLIst = new ArrayList<ExamDone>();
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery("SELECT eiID from exams_initiated WHERE uID='" + user.getuID() + "'");

				if (rs.next()) {
					System.out.println("found eiID=" + eiID);
					eiID = rs.getString(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("verify error 1");
			}
			Statement st, st2, st3;
			try {
				st = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = st.executeQuery("SELECT edID from exams_done WHERE eiID='" + eiID + "'");
				ArrayList<String> edIDList = new ArrayList<>();
				while (rs.next()) {
					edIDList.add(rs.getString(1));
					System.out.println("found edID " + rs.getString(1));
				}

				st2 = mysqlConnection.getInstance().getCon().createStatement();
				st3 = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs2 = st2.executeQuery(
						"SELECT t1.edID FROM exam_questions_answer as t1 inner join exam_questions_answer as t2 ON\r\n"
								+ " t1.edID!=t2.edID AND t1.qID=t2.qID WHERE t1.answer=t2.answer AND t1.isCorrect='INCORRECT' AND"
								+ " t2.isCorrect= 'INCORRECT';");

				while (rs2.next()) {
					System.out.println("theres two equels answer from " + rs2.getString(1));
					if (edIDList.contains(rs2.getString(1))) {
						PreparedStatement ps = mysqlConnection.getInstance().getCon()
								.prepareStatement("UPDATE exams_done SET isCheating=? WHERE edID=?");

						ps.setString(1, "CHEATING");
						ps.setString(2, rs2.getString(1));
						int success = ps.executeUpdate();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("verify error 1");
			}
			Statement stmt2;
			try {
				stmt2 = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs2 = stmt2.executeQuery(
						"SELECT * from exams_done WHERE eiID='" + eiID + "' AND " + "isApproved='WAITING'");

				while (rs2.next()) {
					System.out.println("found exam_done data");
					ExamDone examDone = new ExamDone();
					examDone.setEdID(rs2.getString(1));
					examDone.setEiID(rs2.getString(2));
					examDone.setuID(rs2.getString(3));
					String tmpS = (rs2.getString(4));
					examDone.setStartTime(rs2.getString(5));
					examDone.setEndTime(rs2.getString(6));
					tmpS = (rs2.getString(7));
					examDone.setGrade(rs2.getString(8));
					examDone.setIsCheating(rs2.getString(9));
					examDoneLIst.add(examDone);
				}

				ArrayList<Object> parameters = new ArrayList<>();
				parameters.add(examDoneLIst);
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_FOR_VERIFY,
						parameters, "", true);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("verify error 2");
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
		else if (dataPacket.getRequest() == DataPacket.Request.GET_TEACHER_QUESTIONS) {
			User user=(User)dataPacket.getData_parameters().get(0);
			ArrayList<Question> questionsList=new ArrayList<>();
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery(
						"SELECT * from questions WHERE authorID='" + user.getuID()+"'");

				while (rs.next()) {
					System.out.println("found teacher question data");
					Question question=new Question();
					question.setqID(rs.getString(1));
					question.setAuthorID(rs.getInt(2));
					question.setInfo(rs.getString(3));
					question.setOption1(rs.getString(4));
					question.setOption2(rs.getString(5));
					question.setOption3(rs.getString(6));
					question.setOption4(rs.getString(7));
					question.setAnswer(rs.getString(8));
					questionsList.add(question);
				}
				ArrayList<Object> parameters = new ArrayList<>();
				parameters.add(questionsList);
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_TEACHER_QUESTIONS,
						parameters, "", true);
				
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("teacher problem-=-=");
			}
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

		/////////////////////////////////////////////// DANIEL///////////////////////////////////////////////
		else if (dataPacket.getRequest() == DataPacket.Request.GET_ONGOING_EXAM) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			int uID = ((User) dataPacket.getData_parameters().get(0)).getuID();

			Statement stmt;
			String fID, cID = null, fieldName = null, courseName = null, full_name = null;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * from exams_initiated WHERE uID='" + uID + "' ;");

				if (rs.next()) {
					System.out.println("FOUND ONGOING EXAM\n");
					parameters.add(rs.getString(1) + ",");

					fID = (rs.getString(2).substring(0, 2));
					cID = (rs.getString(2).substring(2, 4));
					System.out.println(fID + cID);
					Statement stmt2 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs2 = stmt2.executeQuery("SELECT fieldName  from fields WHERE fID='" + fID + "'; ");
					if (rs2.next()) {
						fieldName = rs2.getString(1);
					}
					System.out.println(fieldName);
					parameters.add(fieldName + ",");

					Statement stmt3 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs3 = stmt3.executeQuery("SELECT courseName  from courses WHERE cID='" + cID + "'; ");
					if (rs3.next()) {
						courseName = rs3.getString(1);
					}
					System.out.println(courseName);
					parameters.add(courseName + ",");
					Statement stmt4 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs4 = stmt4.executeQuery(
							"SELECT firstName, lastName from users WHERE uID='" + rs.getString(3) + "'; ");
					if (rs4.next()) {
						full_name = rs4.getString(1) + " " + rs4.getString(2);
					}
					System.out.println(full_name);
					parameters.add(full_name + ",");
					System.out.println(rs.getString(4));
					parameters.add("Time: " + rs.getString(4));

					System.out.println(parameters);
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_ONGOING_EXAM,
							parameters, "yes exams", true);
				} else {
					parameters.add("");
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_ONGOING_EXAM,
							parameters, "no exams", false);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.TERMINATE_EXAM) {
			Statement stmt;

			ArrayList<Object> parameters = new ArrayList<Object>();
			int uID = ((User) dataPacket.getData_parameters().get(0)).getuID();
			parameters.add("");
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				stmt.executeUpdate("DELETE  from exams_initiated WHERE uid='" + uID + "' ;");

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.TERMINATE_EXAM,
						parameters, "", true);

			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}

		}
		/////////////////////////////////////////////// DANIEL///////////////////////////////////////////////

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
			String field = null;
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
					// String full_name = null;
					Statement stmt3 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs3 = stmt3.executeQuery("SELECT fID from users WHERE uID='" + rs.getInt(1) + "'; ");

					int uID = rs.getInt(1);

					/*
					 * while (rs2.next()) { //full_name = rs2.getString(1) + " " + rs2.getString(2);
					 * 
					 * }
					 */
					while (rs3.next()) {
						field = rs3.getString(1);

					}
					Statement stmt4 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs4 = stmt4.executeQuery("SELECT fieldName  from fields WHERE fID='" + field + "'; ");
					while (rs4.next()) {

						field = rs4.getString(1);
					}

					Statement stmt5 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs5 = stmt5
							.executeQuery("SELECT eID  from exams_initiated WHERE eiID='" + rs.getString(2) + "'; ");
					String eID = null;
					String cID = null;
					while (rs5.next()) {

						eID = rs5.getString(1);
						System.out.println(eID);
						cID = eID.substring(2, 4);
						System.out.println(cID);
					}
					Statement stmt6 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs6 = stmt6.executeQuery("SELECT courseName  from courses WHERE cID='" + cID + "'; ");
					String courseName = null;
					while (rs6.next()) {

						courseName = rs6.getString(1);

					}
					System.out.println(courseName);
					ExtraTimeRequest request = new ExtraTimeRequest(uID, rs.getInt(2), rs.getString(3), rs.getString(4),
							rs.getString(5), courseName, eID, field);
					// System.out.println(request.toString());

					parameters.add(request);

					// System.out.println(request.toString());
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
		else if (dataPacket.getRequest() == DataPacket.Request.EXTRA_TIME_DECISION) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			Statement stmt;

			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				System.out.println(((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getIsApproved());

				int success = stmt.executeUpdate("UPDATE extra_time_requests SET isApproved = '"
						+ ((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getIsApproved() + "' WHERE eiID='"
						+ ((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getEiID() + "';");
				System.out.println(success + "\n");

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.EXTRA_TIME_DECISION,
						null, "", true);

				// if approved the extra time so send the exam eiID to indicate to which exam to
				// add the extra time
				if (((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getIsApproved().equals("yes")) {
					System.out.println("Approved the extra time");
					// send responce to all the clientssss
					// add the exam id and the time of the extand
					// exam initiated ID, exam extra time
					parameters.add(((ExtraTimeRequest) dataPacket.getData_parameters().get(0))); // return back the
																									// request

					Responce_toAll_dataPackert = new DataPacket(DataPacket.SendTo.CLIENT,
							DataPacket.Request.ADD_EXTRA_TIME_TO_EXAM, parameters, "", true);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

///////////////////////////////////////////////////MAX////////////////////////////////////////M

		// return the 2 DataPackets
		return new DataPacket[] { Responce_dataPacket, Responce_toAll_dataPackert };
	}

	///////////////////////////////////////////////////

//////////////////////start barak
	private DataPacket getCopyOfExam(DataPacket dataPacket) {
		Statement statement;
		ArrayList<String> questionsID = new ArrayList<>();
		ArrayList<String> studentAnswersIndexes = new ArrayList<>();
		ArrayList<String> questionsDescription = new ArrayList<>();
		ArrayList<String> correctAnswersDescription = new ArrayList<>();
		ArrayList<String> studentAnswersDescription = new ArrayList<>();
		ArrayList<String> pointsForQuestion = new ArrayList<>();
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
				ArrayList<String> answersDescriptions = getCorrectAndStudentAnswersDescription(studentAnswersIndexes,
						questionsID, pointsForQuestion);// even indexes are the correct answers descriptions
				// odd indexes are student descriptions
				for (int i = 0; i < answersDescriptions.size(); i++) {
					if (i % 2 == 0)
						correctAnswersDescription.add(questionsDescription.get(i));
					else
						studentAnswersDescription.add(questionsDescription.get(i));
				}
				questionsDescription = getQuestiosnDescription(questionsID);

			} else {
				return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_COPY_OF_EXAM, null, "", true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_COPY_OF_EXAM, null, "", true);
		}
		ArrayList<Object> parameters = new ArrayList<>();
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
					if (rs.getString(1) == rs.getString(2)) {
						pointsForQuestion.add(rs.getString(3));
					} else {
						pointsForQuestion.add("0");
					}
				} else {
					return null;
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
		System.out.println("njnkjbkjbkj   " + coursesID.get(0));
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			for (int i = 0; i < coursesID.size(); i++) {
				ResultSet rs = statement
						.executeQuery("SELECT courseName from courses WHERE (cID='" + coursesID.get(i) + "') ");
				if (rs.next()) {
					System.out.println("sacbhkbbacb    " + rs.getString(1));
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
		System.out.print("############# ");
		int userID = ((int) dataPacket.getData_parameters().get(0));

		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement.executeQuery("SELECT exams_initiated.eID,exams_done.grade,exams_initiated.eiID"
					+ " from  exams_initiated  INNER JOIN exams_done ON exams_initiated.eiID=exams_done.eiID "
					+ "WHERE (exams_done.uID='" + userID + "'); ");
			int emptyRs = 0;
			while (rs.next()) {
				emptyRs = 1;
				parameter.add(rs.getString(1));// eID
				parameter.add(rs.getInt(2));// grade
				parameter.add(rs.getInt(3));// eiID
			}
			if (emptyRs == 0) {
				return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_STUDENT_GRADES, null, "", true);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_STUDENT_GRADES, parameter, "", true);
	}
//////////////end barak 

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
				String myStatement = " INSERT INTO exams (eID,authorID, description,duration, teacherComments, studentComments) VALUES (?,?,?,?,?,?)";
				PreparedStatement statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
				String idCounter = count < 10 ? "0" + count : count < 100 ? "00" + count : "" + count;
				statement.setString(1, exam.getExamID() + idCounter);
				statement.setInt(2, exam.getAuthorID());
				statement.setString(3, exam.getDescription());
				statement.setString(4, exam.getDuration());
				statement.setString(5, exam.getTeacherComments());
				statement.setString(6, exam.getStudentsComments());
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

}
