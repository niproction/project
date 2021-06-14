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
import common.HistogramInfo;
import common.IncomingDataPacketHandler;
import common.MyFile;
import common.Principal;
import common.Question;
import common.Student;
import common.Teacher;
import common.User;
import common.Course;
import common.examInitiated;
import ocsf.server.ConnectionToClient;
import server.CEMSServer.WhoToNotify;

public class ServerDataPacketHandler implements IncomingDataPacketHandler {
	private static int edID = 1;
	private ConnectionToClient client;

	public ServerDataPacketHandler(ConnectionToClient client) {
		this.client = client;
	}

	@Override
	public ArrayList<Object> CheckRequestExecuteCreateResponce(Object msg) {
		if (msg instanceof DataPacket && ((DataPacket) msg).getSendTo() == DataPacket.SendTo.SERVER)
			return ParsingDataPacket((DataPacket) msg);
		else
			System.out.println("not instance of");
		return null;
	}

	@Override
	public ArrayList<Object> ParsingDataPacket(DataPacket dataPacket) {
		DataPacket Responce_dataPacket = null;
		Notification notification1 = null;
		Notification notification2 = null;
		DataPacket Responce_Specific_Clients_dataPacket = null;
		Integer Identifier = null;
		WhoToNotify whoToNotify = null;

		/////////////////////////////////////////////
		// general requests
		////////////////////////////////////////////////

		if (dataPacket.getRequest() == DataPacket.Request.LOGIN) {
			if (dataPacket.getData_parameters().get(0) instanceof String
					&& dataPacket.getData_parameters().get(1) instanceof String) {
				Statement stmt;
				try {
					boolean userExists = false, emailExists = false;
					stmt = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs_user_check = stmt.executeQuery("SELECT * from users WHERE username='"
							+ (String) (dataPacket.getData_parameters().get(0)) + "'");
					if (rs_user_check.next())
						userExists = true;

					ResultSet rs_email_check = stmt.executeQuery("SELECT * from users WHERE username='"
							+ (String) (dataPacket.getData_parameters().get(0)) + "'");
					if (rs_email_check.next())
						emailExists = true;

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
						} else {
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

								// rostik v10
								CEMSServer.teachersOfOnGoingExams.add(new GroupMember(pass_user, client)); // add
								// rostik v10 // teachers

							} else if (roleType.equals("principle")) {
								System.out.println("detected principal user");
								Principal pass_user = new Principal(rs.getInt(1), rs.getString(2), rs.getString(3),
										rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(9));
								parameter.add(pass_user);

								// rostik v10
								CEMSServer.principals.add(new GroupMember(pass_user, client)); // add principle and its
								// rostik v10 // cliet information
							} else {
								System.out.println("detected Problem");
							}

							Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN,
									parameter, "", true); // create DataPacket that contains true to indicate that the
															// user
															// information is correct

							// update the isConnected column to YES to indicate that the user is in the
							// system
							PreparedStatement ps = mysqlConnection.getInstance().getCon()
									.prepareStatement("UPDATE users SET isConnected=? WHERE uID=?");

							ps.setString(1, "YES");
							ps.setString(2, rs.getString(1));
							int success = ps.executeUpdate();
						}
						System.out.println("end search");
					} else {
						if (!userExists || !emailExists)
							Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN,
									null, "Username/email doesnt exists", false); // create DataPacket user information
																					// is inccorect...
						else
							Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN,
									null, "Incorrect password", false); // create DataPacket user information is
																		// inccorect...
					}

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

				// rostik v10
				if (dataPacket.getData_parameters().get(0) instanceof Teacher) {
					for (int i = 0; i < CEMSServer.teachersOfOnGoingExams.size(); i++) {
						if (CEMSServer.teachersOfOnGoingExams
								.equals((Teacher) dataPacket.getData_parameters().get(0))) {
							CEMSServer.teachersOfOnGoingExams.remove(i);
							System.out.println("Teacher removed from array");
						}
					}
				} else if (dataPacket.getData_parameters().get(0) instanceof Principal) {
					CEMSServer.principals
							.detach(new GroupMember((Principal) dataPacket.getData_parameters().get(0), client));
				}
				// rostik v10

				System.out.println("user logout");

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGOUT, null, "",
						true); // create DataPacket user information is inccorect...
			} catch (SQLException e) {
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGOUT, null, "",
						false); // create DataPacket user information is inccorect...
				e.printStackTrace();

			}
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////

		///////////////////////////////
		// Student requests
		//////////////////////////////////////////////////
		else if (dataPacket.getRequest() == DataPacket.Request.CHECK_FOR_GRADES) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			int uID = (int) dataPacket.getData_parameters().get(0);
			int count = 0;
			Statement stmt;

			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT COUNT(*) FROM exams_done WHERE uID='" + uID + "' " + "AND isApproved='APPROVED';");
				if (rs.next()) {
					count = Integer.parseInt(rs.getString(1));
				}
				System.out.println(count);

				parameters.add(count);

			} catch (SQLException e) {
				e.printStackTrace();
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.CHECK_FOR_GRADES,
						parameters, "no", false); // create DataPacket user information is inccorect...
			}

			Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.CHECK_FOR_GRADES,
					parameters, "yes", true); // create DataPacket user information is inccorect...
		} else if (dataPacket.getRequest() == DataPacket.Request.CHECK_FOR_EXAM) {
			System.out.println("hihi");
			ArrayList<Object> parameters = new ArrayList<Object>();
			int uID = ((User) dataPacket.getData_parameters().get(0)).getuID();
			System.out.println(uID);
			int count = 0;
			Statement stmt;

			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				// ResultSet rs = stmt.executeQuery("SELECT * from exams_initiated WHERE
				// uID='" + uID + "' ;");

				// if (rs.next()) {

				count++;
				// }
				parameters.add(count);
				if (count != 0) {

					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.CHECK_FOR_EXAM,
							parameters, "yes exams", true);
				} else {

					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.CHECK_FOR_EXAM,
							parameters, "no exams", false);
				}

			}

			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}

		} else if (dataPacket.getRequest() == DataPacket.Request.CHECK_FOR_EXAM) {

			ArrayList<Object> parameters = new ArrayList<Object>();
			int uID = ((int) dataPacket.getData_parameters().get(0));
			Statement stmt;

			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT * from exams_initiated WHERE uID = '" + uID + "' AND isFinished ='started' ;");

				if (rs.next()) {
					System.out.println("yes");
					parameters.add(true);
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.CHECK_FOR_EXAM,
							parameters, "yes exams", true);

				} else {
					System.out.println("false");
					parameters.add(false);
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.CHECK_FOR_EXAM,
							parameters, "no exams", false);
				}
			} catch (SQLException e) {
				parameters.add(false);
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.CHECK_FOR_EXAM,
						parameters, "no exams", false);
				e.printStackTrace();
			}
		}
		// nissan
		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAM) {
			System.out.println("Entered GET_EXAM");
			String ID = "";
			User user = (User) dataPacket.getData_parameters().get(1);
			Statement st;
			try {
				st = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs2 = st.executeQuery("SELECT ID from users WHERE uid='" + user.getuID() + "'");
				if (rs2.next()) {
					ID = rs2.getString(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
						parameter.add(ID);
						Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM,
								parameter, null, true);

						// add this user and its client information to group of students that attending
						// the same exam(ongoing exam)
						Group.addMemberToGroup(new GroupMember(((User) dataPacket.getData_parameters().get(1)), client),
								examInitiated.getEiID());

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

					ResultSet rs2 = stmt
							.executeQuery("SELECT studentComments from exams WHERE eID='" + exam.geteID() + "'");
					System.out.println("select initiated exam");

					if (rs2.next()) {
						exam2.setStudentsComments(rs2.getString(1));
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("error getting students comments");
				}
				ArrayList<Object> parameter = new ArrayList<Object>();
				parameter.add(exam2);

				parameter.add(currentTime); // the server time

				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs1 = stmt.executeQuery(
						"SELECT * FROM extra_time_requests WHERE eiID='" + exam.getEiID() + "' AND isApproved='yes';");
				ExtraTimeRequest extraTimeRequest = null;
				if (rs1.next()) {
					System.out.println("<<<<<<<<<<found extra time");
					extraTimeRequest = new ExtraTimeRequest(rs1.getInt(1), rs1.getInt(2), rs1.getString(3),
							rs1.getString(4), rs1.getString(5), null, null, null);
				}

				parameter.add(extraTimeRequest); // return back the

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
		else if (dataPacket.getRequest() == DataPacket.Request.Get_Comments) {
			Responce_dataPacket = getComments(dataPacket);
		}
		/////////////////////////////////
		// Teacher requests
		///////////////////////////////////////////////

		else if (dataPacket.getRequest() == DataPacket.Request.HOW_MANY_VERIFY) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			int uID = (int) dataPacket.getData_parameters().get(0);
			int count = 0;
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * from exams_done ;");
				while (rs.next()) {
					if ((rs.getString(7)).equals("WAITING")) {
						count++;
					}
				}

				System.out.println("how many to verify: " + count);

				parameters.add(count);

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.HOW_MANY_VERIFY,
						parameters, null, true); // create DataPacket

			} catch (SQLException e) {
				e.printStackTrace();
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.HOW_MANY_VERIFY,
						parameters, "no", false); // create DataPacket
			}

		} else if (dataPacket.getRequest() == DataPacket.Request.EDIT_GRADE) {
			String uID = (String) dataPacket.getData_parameters().get(0);
			String grade = (String) dataPacket.getData_parameters().get(1);
			String comments = (String) dataPacket.getData_parameters().get(2);
			String examID = (String) dataPacket.getData_parameters().get(3);
			try {
				PreparedStatement ps = mysqlConnection.getInstance().getCon()
						.prepareStatement("UPDATE exams_done SET grade=?, gradeComments=? WHERE uID=? AND edID=?");

				ps.setString(1, grade);
				ps.setString(2, comments);
				ps.setString(3, uID);
				ps.setString(4, examID);
				int success = ps.executeUpdate();
				ArrayList<Object> parameters = new ArrayList<>();
				parameters.add(success);
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.EDIT_GRADE,
						parameters, "yes exams", true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (dataPacket.getRequest() == DataPacket.Request.ONGOING_TO_MANAGE) {

			ArrayList<Object> parameters = new ArrayList<Object>();
			int uID = (int) dataPacket.getData_parameters().get(0);
			int count = 0;
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT * from exams_initiated WHERE uID='" + uID + "' AND isFinished='started';");
				if (rs.next()) {
					// parameters.add(true);
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.ONGOING_TO_MANAGE,
							null, null, true); // create DataPacket
				} else {

					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.ONGOING_TO_MANAGE,
							null, "false", false); // create DataPacket user information is inccorect...
				}

			} catch (SQLException e) {
				e.printStackTrace();
				parameters.add(false);
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.ONGOING_TO_MANAGE,
						null, null, false); // create DataPacket user information is inccorect...
			}
		}

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
			ArrayList<Integer> isCorrectAnswer = new ArrayList<>();
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

						} else
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
					if (isCorrectAnswer.get(i) == 1)
						statement2.setString(4, "CORRECT");
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
			ArrayList<Question> questionList = new ArrayList<Question>();
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery("SELECT * from questions WHERE (authorID= '" + user.getuID() + "')");
				ArrayList<Object> parameter = new ArrayList<Object>();
				while (rs.next()) {

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

					questionList.add(question);

				}
				parameter.add(questionList);
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

		else if (dataPacket.getRequest() == DataPacket.Request.GET_TEACHER_QUESTIONS) {
			User user = (User) dataPacket.getData_parameters().get(0);
			ArrayList<Question> questionsList = new ArrayList<>();
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();

				ResultSet rs = stmt.executeQuery("SELECT * from questions WHERE authorID='" + user.getuID() + "'");

				while (rs.next()) {
					System.out.println("found teacher question data");
					Question question = new Question();
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

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("teacher problem-=-=");
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.TEACHER_REQUEST_EXTRA_TIME) {
			// ArrayList<Object> parameters = new ArrayList<>();

			// String myStatement = " INSERT INTO exams_done (eiID, uID, duration,
			// startTime, endTime, isApproved, grade) VALUES (?,?,?,?,?,?,?)";
			// PreparedStatement statement;
			try {
				String myStatement = " INSERT INTO extra_time_requests (uID, eiID, comment,extraTime, isApproved) VALUES (?,?,?,?,?)";
				PreparedStatement statement = mysqlConnection.getInstance().getCon().prepareStatement(myStatement);
				statement.setInt(1, ((User) dataPacket.getData_parameters().get(0)).getuID());
				statement.setInt(2, ((examInitiated) dataPacket.getData_parameters().get(1)).getEiID());
				statement.setString(3, (String) dataPacket.getData_parameters().get(2));
				statement.setString(4, (String) dataPacket.getData_parameters().get(3));
				statement.setString(5, "waiting");
				statement.executeUpdate();

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
						DataPacket.Request.TEACHER_REQUEST_EXTRA_TIME, null, "", true); // create DataPacket that
																						// contains true to indicate
				// rostik v10
				// notify all the principals that about the extra time request
				Responce_Specific_Clients_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
						DataPacket.Request.NOTIFY_PRINCIPALS_ABOUT_EXTRA_TIME_REQUEST, null, "", true);
				Identifier = 0;
				whoToNotify = WhoToNotify.ALL_PRINCIPALS;
				notification1 = new Notification(whoToNotify, Identifier, Responce_Specific_Clients_dataPacket);
				// rostik v10 // is correct
			} catch (SQLException e) {
				e.printStackTrace();
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
						DataPacket.Request.TEACHER_REQUEST_EXTRA_TIME, null, "get exception", false); // create
																										// DataPacket
																										// that contains
																										// true to
																										// indicate that
																										// the user
																										// information

			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSES) {
			Responce_dataPacket = getCourses(dataPacket);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_FIELD_ID) {
			Responce_dataPacket = getQuestionsByFieldID(dataPacket);
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

		else if (dataPacket.getRequest() == DataPacket.Request.TEACHER_REQUEST_EXTRA_TIME) {
			ArrayList<Object> parameters = new ArrayList<Object>();

		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_ONGOING_EXAM) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			int uID = ((User) dataPacket.getData_parameters().get(0)).getuID();

			Statement stmt;
			String fID, cID = null, fieldName = null, courseName = null;

			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT * from exams_initiated WHERE uID='" + uID + "' AND isFinished='started';");

				if (rs.next()) {
					System.out.println("FOUND ONGOING EXAM\n");
					String eID = rs.getString(2);

					java.util.Date dt = new java.util.Date();
					SimpleDateFormat dateandtimeFormat = new SimpleDateFormat("HH:mm:ss");
					String currentTime = dateandtimeFormat.format(dt);

					examInitiated examInitiated = new examInitiated();
					examInitiated.setEiID(rs.getInt(1));
					examInitiated.seteID(rs.getString(2));
					examInitiated.setuID(rs.getInt(3));
					examInitiated.setPassword(rs.getString(4));
					examInitiated.setInitiatedDate(rs.getString(5));

					fID = (rs.getString(2).substring(0, 2));
					cID = (rs.getString(2).substring(2, 4));
					System.out.println(fID + cID);
					Statement stmt2 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs2 = stmt2.executeQuery("SELECT fieldName  from fields WHERE fID='" + fID + "'; ");
					if (rs2.next()) {
						fieldName = rs2.getString(1);
					}
					System.out.println(fieldName);

					Statement stmt3 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs3 = stmt3.executeQuery("SELECT courseName  from courses WHERE cID='" + cID + "'; ");
					if (rs3.next()) {
						courseName = rs3.getString(1);
					}
					System.out.println(courseName);

					Statement stmt6 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs6 = stmt6
							.executeQuery("SELECT description, duration  from exams WHERE eID='" + eID + "'; ");
					String des = null;
					String duration = null;
					Exam exam = null;

					if (rs6.next()) {
						exam = new Exam(null, null, rs6.getString(1), rs6.getString(2), null, null, null);
						System.out.println("added");
					}
					// rostik v10
					Statement stmt7 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs7 = stmt7.executeQuery("SELECT isApproved from extra_time_requests WHERE uID='" + uID
							+ "' AND eiID='" + rs.getInt(1) + "';");

					ExtraTimeRequest extra = null;
					if (rs7.next()) {
						System.out.println("found extra time request");
						extra = new ExtraTimeRequest(0, 0, null, null, rs7.getString(1), null, null, null);
						// requestextratime = rs7.getString(1);
					}

					parameters.add(currentTime);
					parameters.add(fieldName);
					parameters.add(courseName);
					parameters.add(examInitiated);
					parameters.add(exam);
					parameters.add(extra);
					// rostik v10

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
				// stmt =
				// mysqlConnection.getInstance().getCon().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				// ResultSet.CONCUR_UPDATABLE);
				// stmt.executeUpdate("DELETE from exams_initiated WHERE uid='" + uID + "'
				// isFinished='started';");
				PreparedStatement ps = mysqlConnection.getInstance().getCon()
						.prepareStatement("UPDATE exams_initiated SET isFinished=? WHERE eiID=? AND isFinished=?");

				ps.setString(1, "terminated");
				ps.setInt(2, ((examInitiated) dataPacket.getData_parameters().get(1)).getEiID());
				ps.setString(3, "started");
				int success = ps.executeUpdate();

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.TERMINATE_EXAM, null,
						"", true);

				// rostik v10
				// notify all the principals that about the extra time request
				Responce_Specific_Clients_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
						DataPacket.Request.NOTIFY_STUDENTS_DOING_SAME_EXAM_TERMINATE, null, "", true);
				Identifier = ((examInitiated) dataPacket.getData_parameters().get(1)).getEiID();
				whoToNotify = WhoToNotify.STUDENTS_DOING_THE_SAME_EXAM;
				notification1 = new Notification(whoToNotify, Identifier, Responce_Specific_Clients_dataPacket);
				// rostik v10

			} catch (SQLException e) {
				e.printStackTrace();
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.TERMINATE_EXAM, null,
						"Error accured", false);
			}
		}
		/////////////////////////////////////////////// DANIEL///////////////////////////////////////////////

		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME) {
			Responce_dataPacket = getCourseID(dataPacket);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAM_QUESTIONID_BY_EID) {
			Responce_dataPacket = getExamQuestion(dataPacket);
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

///////////////////////////////////////////////////MAX-UPDATE1////////////////////////////////////////M

		else if (dataPacket.getRequest() == DataPacket.Request.GET_ALL_TEACHERS) {
			Statement statement;
			try {
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE roleType='teacher';");
				ArrayList<Object> teachers = new ArrayList<Object>();

				while (rs.next()) {

					teachers.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
				}

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_ALL_TEACHERS,
						teachers, "", true); // create
			} catch (SQLException e) {
				e.printStackTrace();
				return null;

			}

		} else if (dataPacket.getRequest() == DataPacket.Request.GET_ALL_STUDENTS) {
			Statement statement;
			try {
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE roleType='student';");
				ArrayList<Object> students = new ArrayList<Object>();
				while (rs.next()) {
					students.add((new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8))));
				}
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_ALL_STUDENTS,
						students, "", true); // create

			} catch (SQLException e) {
				e.printStackTrace();
				return null;

			}

		} else if (dataPacket.getRequest() == DataPacket.Request.GET_ALL_COURSES) {
			Statement statement;
			try {
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM courses;");
				ArrayList<Object> courseNames = new ArrayList<Object>();
				while (rs.next())
					courseNames.add(new Course(rs.getString(1), rs.getString(2), rs.getString(3)));
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_ALL_COURSES,
						courseNames, "", true); // create

			} catch (SQLException e) {
				e.printStackTrace();
				return null;

			}
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_TEACHER_GRADES) {
			Statement statement;
			Statement statement1;
			Statement statement2;
			ResultSet rs1, rs, rs2;
			String eID;
			@SuppressWarnings("unchecked")
			ArrayList<Double> grades1 = new ArrayList<Double>();

			ArrayList<User> teacher = (ArrayList<User>) dataPacket.getData_parameters().get(0);
			try {
				System.out.println("the userID" + teacher.get(0).getuID());
				statement = mysqlConnection.getInstance().getCon().createStatement();
				statement1 = mysqlConnection.getInstance().getCon().createStatement();
				statement2 = mysqlConnection.getInstance().getCon().createStatement();
				ArrayList<Object> params = new ArrayList<Object>();
				boolean flag = false;

				rs = statement.executeQuery("SELECT eID FROM exams WHERE (authorID='" + teacher.get(0).getuID() + "');");
				
				while (rs.next()) {
					flag = false;

					System.out.println("data collected sucssefully");

					// System.out.println(rs.getString(2) + "im in whileee");
					rs1 = statement1.executeQuery("SELECT eiID ,eID\n" + " FROM exams_initiated WHERE (eID='"
							+ rs.getString(1) + "')ORDER BY eID ASC;");
					ArrayList<Double> grades = new ArrayList<Double>();
					while (rs1.next()) {
						// String eID=rs1.getString(1);
						rs2 = statement2.executeQuery(
								"SELECT grade \n" + " FROM exams_done WHERE (eiID='" + rs1.getString(1) + "');");
						while (rs2.next()) {
							flag = true;
							grades.add(Double.valueOf(rs2.getString(1)));
						}
						if (flag) {// indicets if there is gardes in this exam
							eID = rs1.getString(2);
							if (rs1.next())
								if (!rs1.getString(2).equals(eID)) {
									System.out.println(rs1.getString(2) + "    " + eID);
									rs1.previous();
									params.add(new HistogramInfo(grades, rs.getString(1)));
								} else
									rs1.previous();
							else
								params.add(new HistogramInfo(grades, rs.getString(1)));

						}
					}
//					if(!flag) {
//						params.add(null);
//					}
				
					
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
							DataPacket.Request.GET_TEACHER_GRADES, params, "", true); // create

				}
				if(params.size()==0)
					params.add(new HistogramInfo(grades1, "nothing"));
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
							DataPacket.Request.GET_TEACHER_GRADES, params, "", true); // create
			
			} catch (SQLException e) {
				e.printStackTrace();
				return null;

			}
		} else if (dataPacket.getRequest() == DataPacket.Request.MANAGE_EXAMS) {
			User user = (User) dataPacket.getData_parameters().get(0);
			Statement statement;
			try {
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM exams WHERE authorID='" + user.getuID() + "';");
				ArrayList<Exam> examsList = new ArrayList<>();
				while (rs.next()) {
					Exam exam = new Exam();
					exam.setExamID(rs.getString(1));
					exam.setDescription(rs.getString(3));
					examsList.add(exam);
				}
				ArrayList<Object> parameters = new ArrayList<>();
				parameters.add(examsList);
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.MANAGE_EXAMS,
						parameters, "", true); // create

			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}

		} else if (dataPacket.getRequest() == DataPacket.Request.GET_STUDENT_GRADES_AND_COURSE) {
			Statement statement;
			Statement statement1;
			Statement statement2;

			String eID, cID;
			ResultSet rs1, rs, rs2;
			@SuppressWarnings("unchecked")
			ArrayList<User> student = (ArrayList<User>) dataPacket.getData_parameters().get(0);
			try {
				System.out.println("the userID" + student.get(0).getuID());
				statement = mysqlConnection.getInstance().getCon().createStatement();
				statement1 = mysqlConnection.getInstance().getCon().createStatement();
				statement2 = mysqlConnection.getInstance().getCon().createStatement();

				rs = statement.executeQuery(
						"SELECT eiID,grade FROM exams_done WHERE (uID='" + student.get(0).getuID() + "');");
				ArrayList<Object> params = new ArrayList<Object>();
				ArrayList<Double> grades = new ArrayList<Double>();
				ArrayList<String> names = new ArrayList<String>();
				while (rs.next()) {
					System.out.println(rs.getString(2) + "im in whileee" + rs.getString(1));
					rs1 = statement1
							.executeQuery("SELECT eID FROM exams_initiated WHERE (eiID='" + rs.getString(1) + "');");
					if (rs1.next()) {
						eID = rs1.getString(1);
//System.out.println(eID);
//
						cID = eID.substring(2, 4);
//System.out.println(cID);
						rs2 = statement2.executeQuery("SELECT courseName FROM courses WHERE (cID='" + cID + "');");
						if (rs2.next()) {
							System.out.println(rs2.getString(1));
							grades.add(Double.valueOf(rs.getString(2)));
							names.add(rs2.getString(1));
						}
						params.add(new HistogramInfo(names, grades));

					}
				}

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
						DataPacket.Request.GET_STUDENT_GRADES_AND_COURSE, params, "", true); // create

			} catch (SQLException e) {
				e.printStackTrace();
				return null;

			}

		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_GRADES) {
			Statement statement;
			Statement statement1;
			ResultSet rs1, rs, rs2;
			ArrayList<Course> courses = (ArrayList<Course>) dataPacket.getData_parameters().get(0);
			try {
				statement = mysqlConnection.getInstance().getCon().createStatement();
				statement1 = mysqlConnection.getInstance().getCon().createStatement();

				rs = statement.executeQuery("SELECT eID,eiID FROM exams_initiated ;");// ORDER BY eID ASC;
				ArrayList<Object> params = new ArrayList<Object>();
				System.out.println("im before while");

				while (rs.next()) {
					ArrayList<Double> grades = new ArrayList<Double>();
					System.out.println(rs.getString(1).substring(2, 4) + "in whileeeeeeee");

					if (rs.getString(1).substring(2, 4).equals(courses.get(0).getCourseID())) {
						System.out.println("im in the ifff");
						rs1 = statement1
								.executeQuery("SELECT grade FROM exams_done WHERE eiId='" + rs.getString(2) + "'");
						while (rs1.next()) {
							grades.add(Double.valueOf(rs1.getString(1)));
						}
						if (grades.size() != 0)
							params.add(new HistogramInfo(grades, rs.getString(1)));
					}
				}

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_COURSE_GRADES,
						params, "", true); // create

			} catch (SQLException e) {
				e.printStackTrace();
				return null;

			}

		} else if (dataPacket.getRequest() == DataPacket.Request.GET_HOW_MANY_EXAMS) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			int count = 0;
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM exams_initiated");
				if (rs.next()) {
					count = Integer.parseInt(rs.getString(1));

				}

				if (count == 0) {
					parameters.add(0);
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
							DataPacket.Request.GET_HOW_MANY_EXAMS, parameters, "no", false);
				} else {
					parameters.add(count);

					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
							DataPacket.Request.GET_HOW_MANY_EXAMS, parameters, "yes", true);
				}
			} catch (SQLException e) {
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_HOW_MANY_EXAMS,
						null, "Error accured", false);
				e.printStackTrace();
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_IF_REQUEST) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			int count = 0;
			System.out.println("im hereeeeee");
			Statement stmt;
			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * from extra_time_requests");
				while (rs.next()) {
					if (rs.getString(5).equals("waiting")) {
						count++;
					}
				}
				if (count == 0) {
					parameters.add(0);
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_IF_REQUEST,
							parameters, "no", false);
				} else {
					parameters.add(count);

					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_IF_REQUEST,
							parameters, "yes", true);
				}
			} catch (SQLException e) {
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_IF_REQUEST, null,
						"Error accured", false);
				e.printStackTrace();
			}
		}
///////////////////////////////////////////////////MAX-UPDATE1////////////////////////////////////////M

		// daniel
		///////////////////
		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXTRA_TIME_REQUESTS) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			Statement stmt;
			String field = null;
			// String Full_name=null;

			try {
				stmt = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * from extra_time_requests WHERE isApproved='waiting' ;");

				while (rs.next()) {

					System.out.println("found extra time request");
					// System.out.println(rs.getString(2));
					User user = null;
					// Object pass_user=null;
					Statement stmt2 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs2 = stmt2
							.executeQuery("SELECT firstName, lastName from users WHERE uID='" + rs.getInt(1) + "'; ");
					if (rs2.next()) {
						user = new User(rs.getInt(1), null, null, null, rs2.getString(1), rs2.getString(2), null);
						System.out.println("found user");
					}
					// String full_name = null;
					Statement stmt3 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs3 = stmt3.executeQuery("SELECT fID from users WHERE uID='" + rs.getInt(1) + "'; ");

					int uID = rs.getInt(1);

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
					parameters.add(user);
					System.out.println(user.toString());

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

					// rostik v10
					Responce_Specific_Clients_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
							DataPacket.Request.NOTIFY_STUDENTS_OF_THIS_EXAM_ABOUT_EXTRA_TIME, parameters, "", true);
					Identifier = ((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getEiID();
					System.out.println(Identifier + "dsd");
					whoToNotify = WhoToNotify.STUDENTS_DOING_THE_SAME_EXAM;

					notification1 = new Notification(whoToNotify, Identifier, Responce_Specific_Clients_dataPacket);

					parameters.clear();
					parameters.add(dataPacket.getData_parameters().get(0)); // ExtraTimeRequest
					// parameters.add((ExtraTimeRequest) dataPacket.getData_parameters().get(0))
					Responce_Specific_Clients_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
							DataPacket.Request.NOTIFY_TEACHER_ABOUT_EXTRA_TIME_REQUEST, parameters, "", true);
					Identifier = ((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getuID(); // teacher uID
					System.out.println("teacher uID : " + Identifier);
					whoToNotify = WhoToNotify.SPECIFIC_TEACHER;

					notification2 = new Notification(whoToNotify, Identifier, Responce_Specific_Clients_dataPacket);

					// rostik v10
				} else {
					parameters.add(dataPacket.getData_parameters().get(0)); // ExtraTimeRequest
					// parameters.add((ExtraTimeRequest) dataPacket.getData_parameters().get(0))
					Responce_Specific_Clients_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT,
							DataPacket.Request.NOTIFY_TEACHER_ABOUT_EXTRA_TIME_REQUEST, parameters, "", true);
					Identifier = ((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getuID(); // teacher uID
					System.out.println("teacher uID : " + Identifier);
					whoToNotify = WhoToNotify.SPECIFIC_TEACHER;

					notification1 = new Notification(whoToNotify, Identifier, Responce_Specific_Clients_dataPacket);

				}

			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

///////////////////////////////////////////////////MAX////////////////////////////////////////M

///////////////////////////////////////////////////////////////////////////////
// generate the list to return to the dataPackets and groups info
		ArrayList<Object> ToBeReturened = new ArrayList<Object>();
		ToBeReturened.add(Responce_dataPacket);
		ToBeReturened.add(notification1);
		ToBeReturened.add(notification2);

		return ToBeReturened;
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
		int eiID = (Integer) dataPacket.getData_parameters().get(1);
		int studentID = (Integer) dataPacket.getData_parameters().get(0);
		System.out.println("studID:" + studentID + "eiID:" + eiID);
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement.executeQuery("SELECT edID from exams_done where (uid='" + studentID
					+ "' and eiID='" + eiID + "'and isApproved='APPROVED');");
			if (rs.next()) {
				int edID = rs.getInt(1);// added edID
				questionsID = getQuestionsIDbyedID(edID);
				studentAnswersIndexes = getStudentAnswersIndexes(questionsID, edID);
				ArrayList<String> answersDescriptions = getCorrectAndStudentAnswersDescription(studentAnswersIndexes,
						questionsID, pointsForQuestion);// even indexes are the correct answers descriptions
				// odd indexes are student descriptions
				for (int i = 0; i < answersDescriptions.size(); i++) {
					if (i % 2 == 0)
						correctAnswersDescription.add(answersDescriptions.get(i));
					else
						studentAnswersDescription.add(answersDescriptions.get(i));
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
		System.out.println(questionsDescription.get(0) + "      " + studentAnswersDescription.get(0) + "     "
				+ correctAnswersDescription.get(0) + "   " + pointsForQuestion.get(0));
		parameters.add(questionsDescription);
		parameters.add(studentAnswersDescription);
		parameters.add(correctAnswersDescription);
		parameters.add(pointsForQuestion);
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_COPY_OF_EXAM, parameters, "", true);

	}

	private DataPacket getComments(DataPacket dataPacket) {
		int tmpeID = (Integer) dataPacket.getData_parameters().get(0);
		String eID = String.valueOf(tmpeID);
		System.out.println("eid:" + eID);
		ArrayList<Object> parameter = new ArrayList<>();
		Statement statement;
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();

			ResultSet rs = statement
					.executeQuery("SELECT teacherComments,studentComments FROM exams WHERE eID='" + eID + "';");
			if (rs.next()) {
				parameter.add(rs.getString(1));// teacher comments
				parameter.add(rs.getString(2));// student comments
				System.out.println("commm:" + rs.getString(1));
			} else {
				parameter.add("");
				parameter.add("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.Get_Comments, parameter, "", true);
	}

	private ArrayList<String> getQuestiosnDescription(ArrayList<String> questionsID) {
		ArrayList<String> questionsDescription = new ArrayList<>();
		System.out.println("lasttttt methodddddd");
		Statement statement;
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			for (int i = 0; i < questionsID.size(); i++) {
				ResultSet rs = statement
						.executeQuery("SELECT question FROM questions WHERE qID='" + questionsID.get(i) + "';");
				if (rs.next()) {
					questionsDescription.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("question:" + questionsDescription.get(0));
		return questionsDescription;
	}

	private DataPacket getExamQuestion(DataPacket dataPacket) {
		Statement statement;
		ArrayList<Object> parameter = new ArrayList<Object>();
		ArrayList<Question> questionInExam = new ArrayList<Question>();
		ArrayList<Question> questionNotExam = new ArrayList<Question>();
		String eID = (String) dataPacket.getData_parameters().get(0);
		User user = (User) dataPacket.getData_parameters().get(1);
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement.executeQuery(
					"SELECT exam_questions.qID,exam_questions.points,questions.question from exam_questions INNER JOIN questions ON questions.qID=exam_questions.qID where exam_questions.eID='"
							+ eID + "';");
			while (rs.next()) {
				Question question = new Question();
				question.setqID(rs.getString(1));
				question.setPoints(rs.getString(2));
				question.setInfo(rs.getString(3));
				questionInExam.add(question);
			}

			Statement statement2 = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs2 = statement
					.executeQuery("SELECT question,qID from questions WHERE qID like '" + user.getfid() + "%' ;");
			int flag = 0;
			while (rs2.next()) {
				flag = 0;
				Question questionnot = new Question();
				for (Question question : questionInExam) {
					if (question.getqID().equals(rs2.getString(2)))
						flag = 1;
				}
				if (flag == 0) {
					questionnot.setInfo(rs2.getString(1));
					questionnot.setqID(rs2.getString(2));
					questionNotExam.add(questionnot);
				}

			}
			parameter.add(questionInExam);
			parameter.add(questionNotExam);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_EXAM_QUESTIONID_BY_EID, parameter, "",
				true);

	}

	private ArrayList<String> getCorrectAndStudentAnswersDescription(ArrayList<String> studentAnswersIndexes,
			ArrayList<String> questionsID, ArrayList<String> pointsForQuestion) {
		Statement statement;
		String points;
		ArrayList<String> answersDescriptions = new ArrayList<>();
		for (int i = 0; i < questionsID.size(); i++) {
			try {
				statement = mysqlConnection.getInstance().getCon().createStatement();
				ResultSet rs = statement.executeQuery("SELECT questions.answer,questions.option"
						+ studentAnswersIndexes.get(i) + ",exam_questions.points"
						+ " from questions INNER JOIN exam_questions ON questions.qID=exam_questions.qID where (exam_questions.qID='"
						+ questionsID.get(i) + "');");
				if (rs.next()) {
					String studentAnswer = rs.getString(2);
					points = rs.getString(3);
					Statement statment1 = mysqlConnection.getInstance().getCon().createStatement();
					ResultSet rs1 = statment1.executeQuery("SELECT option" + rs.getString(1)
							+ " FROM questions WHERE qID='" + questionsID.get(i) + "';");// get correct answer

					if (rs1.next()) {
						answersDescriptions.add(rs1.getString(1));// correct answer description
						answersDescriptions.add(studentAnswer);// student answer description
						if (rs1.getString(1).equals(studentAnswer)) {
							pointsForQuestion.add(points);
						} else {
							pointsForQuestion.add("0");
						}
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

	private ArrayList<String> getStudentAnswersIndexes(ArrayList<String> questionsID, int edID) {
		ArrayList<String> studentAnswersIndex = new ArrayList<>();
		Statement statement;
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			for (int i = 0; i < questionsID.size(); i++) {
				System.out.println("someeeeeeee   " + questionsID.get(i) + "       " + edID);
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

	private ArrayList<String> getQuestionsIDbyedID(int edID) {
		ArrayList<String> questionsID = new ArrayList<>();
		Statement statement;
		try {
			System.out.println("edID:   " + edID);
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement.executeQuery("SELECT qID from exam_questions_answer where (edID='" + edID + "');");
			while (rs.next()) {
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
					+ "WHERE (exams_done.uID='" + userID + "'and exams_done.isApproved='APPROVED'); ");
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
			return new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_STUDENT_GRADES, null, "", true);
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
				String myStatement = " INSERT INTO exams (eID,authorID,description, duration, teacherComments, studentComments) VALUES (?,?,?,?,?,?)";
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
					false);
		}
	}

	private DataPacket getQuestionsByFieldID(DataPacket dataPacket) {
		Statement statement;
		ArrayList<Object> parameter = new ArrayList<Object>();
		User user = ((User) dataPacket.getData_parameters().get(0));
		try {
			statement = mysqlConnection.getInstance().getCon().createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT question,qID from questions WHERE qID like '" + user.getfid() + "%'");
			while (rs.next()) {
				System.out.println("found question:" + rs.getString(1));
				parameter.add(rs.getString(1));
				parameter.add(rs.getString(2));
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
