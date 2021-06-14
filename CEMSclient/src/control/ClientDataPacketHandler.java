package control;

import java.util.ArrayList;

import client.App_client;
import common.Course;
import common.DataPacket;
import common.Exam;
import common.ExamDone;
import common.ExtraTimeRequest;
import common.Field;
import common.HistogramInfo;
import common.IncomingDataPacketHandler;
import javafx.stage.Stage;
import common.Student;
import common.Teacher;
import common.User;
import common.examInitiated;

import common.Principal;
import common.Question;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientDataPacketHandler.
 */
public class ClientDataPacketHandler implements IncomingDataPacketHandler {
	
	/** The Responce data packet. */
	private DataPacket Responce_dataPacket;

	/**
	 * Instantiates a new client data packet handler.
	 */
	public ClientDataPacketHandler() {
	}

	/**
	 * Instantiates a new client data packet handler.
	 *
	 * @param primaryStage the primary stage
	 * @param controller the controller
	 */
	public ClientDataPacketHandler(Stage primaryStage, Object controller) {
		// this.controller = controller;
		// this.primaryStage = primaryStage;
		// if(primaryStage == null)
		// System.out.println("prob");
	} 

	/**
	 * Check request execute create responce.
	 *
	 * @param the msg that the client try sending to the server
	 * @return the array list
	 */
	@Override
	public ArrayList<Object> CheckRequestExecuteCreateResponce(Object msg) {
		if (msg instanceof DataPacket) {
			System.out.println("recived DataPacket");
			return ParsingDataPacket((DataPacket) msg);
			// return GET_responce_DataPacket();
		} else
			System.out.println("not DataPacket");
		return null;
	}

	/**
	 * Parsing data packet.
	 *this mathod parsing the data coming from the server and send the new data to the correct control
	 * @param the data packet is the object we use for parsing the data from the server
	 * @return the array list
	 */
	@Override
	public ArrayList<Object> ParsingDataPacket(DataPacket dataPacket) {
		Responce_dataPacket = null;

		////////////////////////////////////////////////////////////////////////////
		// general requests
		///////////////////////////////////////////////////////////////////////////////

		if (dataPacket.getRequest() == DataPacket.Request.LOGIN) {
			if (dataPacket.getResult_boolean()) {
				System.out.println("user corrent");
				if (dataPacket.getData_parameters().get(0) instanceof Student) {
					UserControl.ConnectedUser = (Student) (dataPacket.getData_parameters().get(0));
					System.out.println("user setted to student");
				} else if (dataPacket.getData_parameters().get(0) instanceof Teacher) {
					UserControl.ConnectedUser = (Teacher) (dataPacket.getData_parameters().get(0));
					System.out.println("user setted to teacher");
				} else if (dataPacket.getData_parameters().get(0) instanceof Principal) {
					UserControl.ConnectedUser = (Principal) (dataPacket.getData_parameters().get(0));
					System.out.println("user setted to principle");
				}

			} else {
				System.out.println("incorrect user");
				ClientControl.message_recived = dataPacket.getMessage();
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.LOGOUT) {
			System.out.println("User Logoutted");
			UserControl.ConnectedUser = null;
			UserControl.setNotipications(0);
			PrincipalControl.setExtraTimeRequest_Recived(false);
			ExamControl.setExamTerminated(false);
		}

		
		
		
		// rostik v10
				/////////////////////////
				// notify requests
				else if (dataPacket.getRequest() == DataPacket.Request.NOTIFY_PRINCIPALS_ABOUT_EXTRA_TIME_REQUEST) {
					System.out.println("request >>>>>>>>>>>> NOTIFY_PRINCIPALS_ABOUT_EXTRA_TIME_REQUEST");
					UserControl.setNotipications(UserControl.getNotipications() + 1);

					PrincipalControl.setExtraTimeRequest_Recived(true);
				}
				else if (dataPacket.getRequest() == DataPacket.Request.NOTIFY_TEACHER_ABOUT_EXTRA_TIME_REQUEST) {
					System.out.println("request >>>>>>>>>>>> NOTIFY_TEACHER_ABOUT_EXTRA_TIME_REQUEST");
					UserControl.setNotipications(UserControl.getNotipications() + 1);

					ExamControl.setNotifiedAboutExtraTime(true);
					ExamControl.setExtraTimeApproved(((ExtraTimeRequest)dataPacket.getData_parameters().get(0)).getIsApproved().equals("yes"));
					ExamControl.extraTimeRequest = (ExtraTimeRequest)dataPacket.getData_parameters().get(0);
					//UserControl.setNotipications(UserControl.getNotipications()+1);
				}
				else if (dataPacket.getRequest() == DataPacket.Request.NOTIFY_STUDENTS_DOING_SAME_EXAM_TERMINATE) {
					System.out.println("request >>>>>>>>>>>> NOTIFY_STUDENTS_DOING_SAME_EXAM_TERMINATE");
					//UserControl.setNotipications(UserControl.getNotipications() + 1);
					System.out.println("sdasdasdas");
					ExamControl.setExamTerminated(true);
					//ExamControl.setExtraTimeApproved(((ExtraTimeRequest)dataPacket.getData_parameters().get(0)).getIsApproved().equals("yes"));
					//UserControl.setNotipications(UserControl.getNotipications()+1);
				}
				else if (dataPacket.getRequest() == DataPacket.Request.NOTIFY_STUDENTS_OF_THIS_EXAM_ABOUT_EXTRA_TIME) {
					// need to check for student state..
					System.out.println("request >>>>>>>>>>>> NOTIFY_STUDENTS_OF_THIS_EXAM_ABOUT_EXTRA_TIME");
					if (UserControl.isDoingExam
							&& UserControl.whatInitiatedExamID == ((ExtraTimeRequest) dataPacket.getData_parameters().get(0))
									.getEiID()) {
						System.out.println("Storring the ei ID and time to be added");
						ExamInitiatedControl.ExtraTime = ((ExtraTimeRequest) dataPacket.getData_parameters().get(0))
								.getExtraTime();
						System.out.println(
								" testtt " + ((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getExtraTime());
						ExamInitiatedControl.isExtraTimeRecived = true;

					} else
						System.out.println("this client skipped in exam?" + UserControl.isDoingExam);
					System.out.println("end of Check if the client in exam state");
				}
				// rostik v10

		
		
		
		
		////////////////////////////////////////////
		// Students requests
		////////////////////////////////////////////
		/**
		 * CHECK_FOR_EXAM-check if there is an ongoing exam running right now
		 */
		else if (dataPacket.getRequest() == DataPacket.Request.CHECK_FOR_EXAM) {
			if ((int) dataPacket.getData_parameters().get(0) == 1) {
				UserControl.ongoingExam = 1;
			} else {
				UserControl.ongoingExam = 0;
			}
			
		}
		/**
		 * ADD_DONE_EXAM-check if the exams done added to the data base
		 * and notify the client 
		 */
		else if (dataPacket.getRequest() == DataPacket.Request.ADD_DONE_EXAM) {
			if (dataPacket.getData_parameters() != null) {
				String seccess = (String) dataPacket.getData_parameters().get(0);
				App_client.seccess = seccess; // message setter
			} else
				App_client.seccess = null;

		}
		/**
		 * get the correct exam from the server and send to the right class
		 */

		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAM) {
			if (dataPacket.getResult_boolean()) {

				System.out.println("qqqqq");
				examInitiated exam = (examInitiated) dataPacket.getData_parameters().get(0);
				Exam exam2 = (Exam) dataPacket.getData_parameters().get(1);
				ExamInitiatedControl.setExamInitiated(exam);
				ExamControl.setExam(exam2);
				UserControl.ID=(String) dataPacket.getData_parameters().get(2);

				UserControl.isDoingExam = true; // stated exam

			} else {
				System.out.println("no exam");
				ExamInitiatedControl.setExamInitiated(null);
				UserControl.setCanOpenExam((String) dataPacket.getMessage());
			}

		}
		/**
		 * GET_TEST_QUESTIONS-get all the tests questions from the server and update the right class with the questions
		 */
		else if (dataPacket.getRequest() == DataPacket.Request.GET_TEST_QUESTIONS) {
			System.out.println("get test questions");
			if (dataPacket.getData_parameters() != null) {
				System.out.println("insert get test questions");
				Exam exam = (Exam) dataPacket.getData_parameters().get(0);
				ExamControl.setExam(exam);

				ExamControl.ServerTime = (String) dataPacket.getData_parameters().get(1); // the server current time..

				//rostik v10
				if(dataPacket.getData_parameters().size() == 3 && dataPacket.getData_parameters().get(2) != null) {
					System.out.println("Storring the ei ID and time to be added");
					ExamInitiatedControl.ExtraTime = ((ExtraTimeRequest) dataPacket.getData_parameters().get(2))
							.getExtraTime();
					System.out.println(
							" testtt " + ((ExtraTimeRequest) dataPacket.getData_parameters().get(2)).getExtraTime());
					ExamInitiatedControl.isExtraTimeRecived = true;

				
					System.out.println("this client skipped in exam?" + UserControl.isDoingExam);
					System.out.println("end of Check if the client in exam state");
				}
				//rostik v10
				System.out.println("setted");
			} else {
				System.out.println("problemmmm");
				ExamControl.setExam(null);
			}
		}
		/**
		 * 
		 */
		else if (dataPacket.getRequest() == DataPacket.Request.ADD_EXTRA_TIME_TO_EXAM) {
			// need to check for student state..
			System.out.println("Check if the client in exam state");
			if (UserControl.isDoingExam
					&& UserControl.whatInitiatedExamID == ((ExtraTimeRequest) dataPacket.getData_parameters().get(0))
							.getEiID()) {
				System.out.println("Storring the ei ID and time to be added");
				ExamInitiatedControl.ExtraTime = ((ExtraTimeRequest) dataPacket.getData_parameters().get(0))
						.getExtraTime();
				System.out.println(
						" testtt " + ((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getExtraTime());
				ExamInitiatedControl.isExtraTimeRecived = true;

			} else
				System.out.println("this client skipped in exam?" + UserControl.isDoingExam);
			System.out.println("end of Check if the client in exam state");
		}
		/**
		 * GET_QUESTION-get spesific question from the data base and send for edit question class
		 */
		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION) {
			System.out.println("login insert to get question");

			if (dataPacket.getData_parameters() != null)
				QuestionControl.setQuestions((ArrayList<Question>) dataPacket.getData_parameters().get(0));
		}
		/**
		 * 
		 */
		else if (dataPacket.getRequest() == DataPacket.Request.GET_FIELD_NAME) {
			if (dataPacket.getData_parameters() != null) {
				System.out.println("$$$$$$$$$$$$got field name " + (String) dataPacket.getData_parameters().get(0));
				App_client.fieldName = (String) dataPacket.getData_parameters().get(0);
			}

		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_STUDENT_GRADES) {
			if (dataPacket.getData_parameters() == null) {
				ViewGradesControl.emptyGrades = true;
			}

			else {
				for (int i = 0; i < dataPacket.getData_parameters().size(); i += 3) {
					ViewGradesControl.addExamsID((String) dataPacket.getData_parameters().get(i));// examID in order to
																									// get
																									// the course name
					ViewGradesControl.addGrade((Integer) dataPacket.getData_parameters().get(i + 1));// Grade
					ViewGradesControl.addExamsInitID((Integer) dataPacket.getData_parameters().get(i + 2));// examInitID
				}
			}

		} else if (dataPacket.getRequest() == DataPacket.Request.CHECK_FOR_GRADES) {
			if ((int) dataPacket.getData_parameters().get(0) != 0) {
				StudentControl.pendingGrades = (int) dataPacket.getData_parameters().get(0);
			} else {
				StudentControl.pendingGrades = 0;
			}
		}
		/**
		 * ADD_DONE_EXAM-get notification if the test was subbmited
		 */
		else if (dataPacket.getRequest() == DataPacket.Request.ADD_DONE_EXAM) {
			if (dataPacket.getData_parameters() != null) {
				String seccess = (String) dataPacket.getData_parameters().get(0);
				App_client.seccess = seccess; // message setter
			} else
				App_client.seccess = null;

		}
		 else if (dataPacket.getRequest() == DataPacket.Request.GET_HOW_MANY_EXAMS) {
				PrincipalControl.HowManyExamsNow = (int) dataPacket.getData_parameters().get(0);
			}
		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAM) {
			if (dataPacket.getResult_boolean()) {

				System.out.println("qqqqq");
				examInitiated exam = (examInitiated) dataPacket.getData_parameters().get(0);
				Exam exam2 = (Exam) dataPacket.getData_parameters().get(1);
				ExamInitiatedControl.setExamInitiated(exam);
				ExamControl.setExam(exam2);

				UserControl.isDoingExam = true; // stated exam

			} else {
				System.out.println("no exam");
				ExamInitiatedControl.setExamInitiated(null);
				UserControl.setCanOpenExam((String) dataPacket.getMessage());
			}
			/**
			 * DISAPPROVED_GRADE-notify to the teacher that the approve/disapprove has been submitted
			 */
		} else if (dataPacket.getRequest() == DataPacket.Request.DISAPPROVED_GRADE) {
			String msg = (String) dataPacket.getData_parameters().get(0);
			System.out.println(msg);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.APPROVED_GRADE) {
			String msg = (String) dataPacket.getData_parameters().get(0);
			System.out.println(msg);
		}
		/**
		 * GET_FOR_VERIFY-getting the needed exams for letting the teacher verify them
		 */

		else if (dataPacket.getRequest() == DataPacket.Request.GET_FOR_VERIFY) {
			if (dataPacket.getData_parameters() != null) {
				System.out.println("got the exam done");
				examDoneControl.setExamDoneLIst((ArrayList<ExamDone>) dataPacket.getData_parameters().get(0));
			}
		}
		/**
		 * 
		 */
		else if (dataPacket.getRequest() == DataPacket.Request.HOW_MANY_VERIFY) {
			TeacherControl.verifyAmount = (int) dataPacket.getData_parameters().get(0);
		}
		else if (dataPacket.getRequest() == DataPacket.Request.ONGOING_TO_MANAGE) {
			TeacherControl.manage = dataPacket.getResult_boolean();
		}
		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSES) {
			ExamControl.coursesNames = (ArrayList<String>) dataPacket.getData_parameters().clone();

		}
		//fixxx
		//else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_FIELD_ID11) {
		//	ExamControl.questions = (ArrayList<String>) dataPacket.getData_parameters().clone();

		//}
		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_FIELD_ID) {
			for (int i = 0; i < dataPacket.getData_parameters().size(); i++) {
				if(i%2==0)
				{
					System.out.println("quest:"+(String) dataPacket.getData_parameters().get(i));
					ExamControl.questions.add((String) dataPacket.getData_parameters().get(i));
				}
				else {
					System.out.println("qID:"+(String) dataPacket.getData_parameters().get(i));
					ExamControl.questionsID.add((String) dataPacket.getData_parameters().get(i));
				}
			}

		}
		
		
		

		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_DESCRIPTION) {
			ExamControl.questionID = (String) dataPacket.getData_parameters().get(0);

		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_USERS) {
			UserControl.user = (ArrayList<User>) dataPacket.getData_parameters().clone();
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME) {
			ExamControl.selectedCourseID = (String) dataPacket.getData_parameters().get(0);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_COPY_OF_EXAM) {
			if (dataPacket.getData_parameters() == null) {
				System.out.println("fasfjasfnkjanjssaffafaf");
				GetCopyOfExamControl.emptyHistory = true;

			} else {
				GetCopyOfExamControl.emptyHistory = false;
				GetCopyOfExamControl.questionsDescription
						.addAll((ArrayList<String>) dataPacket.getData_parameters().get(0));
				GetCopyOfExamControl.studentAnswersDescription
						.addAll((ArrayList<String>) dataPacket.getData_parameters().get(1));
				GetCopyOfExamControl.correctAnswersDescription
						.addAll((ArrayList<String>) dataPacket.getData_parameters().get(2));
				GetCopyOfExamControl.pointsForQuestion
						.addAll((ArrayList<String>) dataPacket.getData_parameters().get(3));
			}
		} else if (dataPacket.getRequest() == DataPacket.Request.Get_Comments) {
			System.out.println("in the clienttttttt");
			GetCopyOfExamControl.teacherComm = (String) dataPacket.getData_parameters().get(0);
			System.out.println(GetCopyOfExamControl.teacherComm);
			GetCopyOfExamControl.studentComm = (String) dataPacket.getData_parameters().get(1);
		}

		///////////////////////////////////////////////////////////
		/// teacher requests
		///////////////////////////////////////
		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_NAME_BY_COURSE_ID) {
			for (int i = 0; i < dataPacket.getData_parameters().size(); i++) {
				System.out.println("ahfkhfa    " + (String) dataPacket.getData_parameters().get(i));
				ViewGradesControl.addCourseName((String) dataPacket.getData_parameters().get(i));
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAMS_BY_TEACHER) {
			System.out.println("dannnnny");
			ExamControl.exams = (ArrayList<Exam>) dataPacket.getData_parameters().clone();
		} else if (dataPacket.getRequest() == DataPacket.Request.TEACHER_REQUEST_EXTRA_TIME) {
			System.out.println("request extra time delivered");
			UserControl.RequestForExtraTimeSent = dataPacket.getResult_boolean();
			
		}
		else if (dataPacket.getRequest() == DataPacket.Request.EDIT_GRADE) {
			if (dataPacket.getData_parameters() != null) {
				System.out.println("zibi");
				StudentControl.gradeChanged=(int)dataPacket.getData_parameters().get(0);
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_TEACHER_QUESTIONS) {
			if (dataPacket.getData_parameters() != null) {
				QuestionControl.setQuestions((ArrayList<Question>) dataPacket.getData_parameters().get(0));
				System.out.println("working 555");
			} else
				System.out.println("not working 000");

		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_ONGOING_EXAM) {
			ManageOngoingExams.isOngoingExams = dataPacket.getResult_boolean();

			if (ManageOngoingExams.isOngoingExams) {

				ExamInitiatedControl.setExamInitiated((examInitiated) dataPacket.getData_parameters().get(3));
				ExamControl.exam = (Exam) dataPacket.getData_parameters().get(4);
				ExamControl.ServerTime = (String) dataPacket.getData_parameters().get(0);


				if (dataPacket.getData_parameters().size() == 6 && dataPacket.getData_parameters().get(5) != null)
					ExamControl.extraTimeRequest = (ExtraTimeRequest)dataPacket.getData_parameters().get(5);
			
			}
		} else if (dataPacket.getRequest() == DataPacket.Request.MANAGE_EXAMS) {
			if (dataPacket.getData_parameters() != null) {
				ExamControl.setExamsList((ArrayList<Exam>) dataPacket.getData_parameters().get(0));
				System.out.println("succeded get exams");
			} else
				System.out.println("not succeded get exams");

		}

		else if (dataPacket.getRequest() == DataPacket.Request.TERMINATE_EXAM) {
			//rostik v10
			//ManageOngoingExams.OngoingExam = (ArrayList<String>) dataPacket.getData_parameters().clone();
		}
		else if(dataPacket.getRequest()==DataPacket.Request.GET_EXAM_QUESTIONID_BY_EID)
		{
			ExamControl.questionsInExams=(ArrayList<Question>)dataPacket.getData_parameters().get(0);
			ExamControl.questionNotInExams=(ArrayList<Question>)dataPacket.getData_parameters().get(1);
		}
		
		
		//////////////////////////////
		// Principal requests
		/////////////////////////////////
		else if (dataPacket.getRequest() == DataPacket.Request.GET_IF_REQUEST) {
			UserControl.ifRequests = (int) dataPacket.getData_parameters().get(0);
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_COURSE) {
			CourseControl.courses = (ArrayList<Course>) dataPacket.getData_parameters().clone();
		} else if (dataPacket.getRequest() == DataPacket.Request.CHECK_TOOK_EXAM) {
			if (dataPacket.getData_parameters() != null)
				UserControl.setCanOpenExam((String) dataPacket.getData_parameters().get(0));
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_FIELD) {
			FieldControl.fields = (ArrayList<Field>) dataPacket.getData_parameters().clone();
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_EXAM) {
			ExamControl.exams = (ArrayList<Exam>) dataPacket.getData_parameters().clone();
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_QUESTIONS) {
			QuestionControl.setQuestions((ArrayList<Question>) dataPacket.getData_parameters().clone());
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME) {
			ExamControl.selectedCourseID = (String) dataPacket.getData_parameters().get(0);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.INSERT_EXAM) {
			ExamControl.examID = (String) dataPacket.getData_parameters().get(0);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXTRA_TIME_REQUESTS) {
			PrincipalControl.requests = new ArrayList<ExtraTimeRequest>();
			UserControl.user = new ArrayList<User>();
			for (int i = 0; i < dataPacket.getData_parameters().size(); i++) {
				if (i % 2 == 0)
					PrincipalControl.requests.add((ExtraTimeRequest) dataPacket.getData_parameters().get(i));
				else
					UserControl.user.add((User) dataPacket.getData_parameters().get(i));
			}
			// PrincipalControl.requests = (ArrayList<ExtraTimeRequest>)
			// dataPacket.getData_parameters().clone();
		} else if (dataPacket.getRequest() == DataPacket.Request.EXTRA_TIME_DECISION) {
			System.out.println("Decision acsepted");
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_ALL_TEACHERS) {
			if (dataPacket.getData_parameters() != null) {
				UserControl.teachers = ((ArrayList<User>) dataPacket.getData_parameters().clone());

			}
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_ALL_STUDENTS) {
			UserControl.students = (ArrayList<User>) dataPacket.getData_parameters().clone();
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_ALL_COURSES) {
			CourseControl.courses = (ArrayList<Course>) dataPacket.getData_parameters().clone();
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_TEACHER_GRADES) {
			HistogramControl.examOfTeacher = (ArrayList<HistogramInfo>) dataPacket.getData_parameters().clone();
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_STUDENT_GRADES_AND_COURSE) {
			HistogramControl.examOfStudent = (ArrayList<HistogramInfo>) dataPacket.getData_parameters().clone();
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_GRADES) {
			HistogramControl.CourseExamGradeList = (ArrayList<HistogramInfo>) dataPacket.getData_parameters().clone();
			HistogramControl.examOfTeacher = new ArrayList<HistogramInfo>();

		}

		///////////////////////////////////////////////////////////////////////////////
		// generate the list to return to the dataPackets
		ArrayList<Object> ToBeReturened = new ArrayList<Object>();
		ToBeReturened.add(Responce_dataPacket);
		return ToBeReturened;
	}

	/**
	 * GE T responce data packet.
	 *
	 * @return the data packet
	 */
	public DataPacket GET_responce_DataPacket() {
		return Responce_dataPacket;
	}
}
