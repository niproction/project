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

public class ClientDataPacketHandler implements IncomingDataPacketHandler {
	private DataPacket Responce_dataPacket;

	public ClientDataPacketHandler() {
	}

	public ClientDataPacketHandler(Stage primaryStage, Object controller) {
		// this.controller = controller;
		// this.primaryStage = primaryStage;
		// if(primaryStage == null)
		// System.out.println("prob");
	}

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
		}

		////////////////////////////////////////////
		// Students requests
		////////////////////////////////////////////
		else if (dataPacket.getRequest() == DataPacket.Request.CHECK_FOR_EXAM) {
			if ((int) dataPacket.getData_parameters().get(0) == 1) {
				UserControl.ongoingExam = 1;
			} else {
				UserControl.ongoingExam = 0;
			}
		}
		else if (dataPacket.getRequest() == DataPacket.Request.ADD_DONE_EXAM) {
			if (dataPacket.getData_parameters() != null) {
				String seccess = (String) dataPacket.getData_parameters().get(0);
				App_client.seccess = seccess; // message setter
			} else
				App_client.seccess = null;

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

		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_TEST_QUESTIONS) {
			System.out.println("get test questions");
			if (dataPacket.getData_parameters() != null) {
				System.out.println("insert get test questions");
				Exam exam = (Exam) dataPacket.getData_parameters().get(0);
				ExamControl.setExam(exam);

				ExamControl.ServerTime = (String) dataPacket.getData_parameters().get(1); // the server current time..

				System.out.println("setted");
			} else {
				System.out.println("problemmmm");
				ExamControl.setExam(null);
			}
		}

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

		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION) {
			System.out.println("login insert to get question");

			if (dataPacket.getData_parameters() != null)
				QuestionControl.setQuestions((ArrayList<Question>) dataPacket.getData_parameters().get(0));
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_FIELD_NAME) {
			if (dataPacket.getData_parameters() != null) {
				System.out.println("$$$$$$$$$$$$got field name " + (String) dataPacket.getData_parameters().get(0));
				App_client.fieldName = (String) dataPacket.getData_parameters().get(0);
			}

		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_STUDENT_GRADES) {
			for (int i = 0; i < dataPacket.getData_parameters().size(); i += 3) {
				ViewGradesControl.addExamsID((String) dataPacket.getData_parameters().get(i));// examID in order to get
																								// the course name
				ViewGradesControl.addGrade((Integer) dataPacket.getData_parameters().get(i + 1));// Grade
				ViewGradesControl.addExamsInitID((Integer) dataPacket.getData_parameters().get(i + 2));// examInitID
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.DISAPPROVED_GRADE) {
			String msg = (String) dataPacket.getData_parameters().get(0);
			System.out.println(msg);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.APPROVED_GRADE) {
			String msg = (String) dataPacket.getData_parameters().get(0);
			System.out.println(msg);
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_FOR_VERIFY) {
			if (dataPacket.getData_parameters() != null) {
				System.out.println("got the exam done");
				examDoneControl.setExamDoneLIst((ArrayList<ExamDone>) dataPacket.getData_parameters().get(0));
			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSES) {
			ExamControl.coursesNames = (ArrayList<String>) dataPacket.getData_parameters().clone();

		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_FIELD_ID) {
			ExamControl.questions = (ArrayList<String>) dataPacket.getData_parameters().clone();

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
			GetCopyOfExamControl.questionsDescription = (ArrayList<String>) dataPacket.getData_parameters().get(0);
			GetCopyOfExamControl.studentAnswersDescription = (ArrayList<String>) dataPacket.getData_parameters().get(1);
			GetCopyOfExamControl.correctAnswersDescription = (ArrayList<String>) dataPacket.getData_parameters().get(2);
			GetCopyOfExamControl.pointsForQuestion = (ArrayList<String>) dataPacket.getData_parameters().get(3);
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

				if (dataPacket.getData_parameters().size() == 6)
					ExamControl.extraTimeRequest = (String) dataPacket.getData_parameters().get(5);

			}
		}

		else if (dataPacket.getRequest() == DataPacket.Request.TERMINATE_EXAM) {
			ManageOngoingExams.OngoingExam = (ArrayList<String>) dataPacket.getData_parameters().clone();
		}

		//////////////////////////////
		// Principal requests
		/////////////////////////////////
		else if (dataPacket.getRequest() == DataPacket.Request.GET_IF_REQUEST) {
			UserControl.ifRequests = (int) dataPacket.getData_parameters().get(0);
		}
		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_COURSE) {
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
			for(int i=0;i<dataPacket.getData_parameters().size();i++)
			{
				if(i%2==0)
					PrincipalControl.requests.add( (ExtraTimeRequest) dataPacket.getData_parameters().get(i));
				else
					UserControl.user.add((User) dataPacket.getData_parameters().get(i));
			}
			//PrincipalControl.requests = (ArrayList<ExtraTimeRequest>) dataPacket.getData_parameters().clone();
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

	public DataPacket GET_responce_DataPacket() {
		return Responce_dataPacket;
	}
}
