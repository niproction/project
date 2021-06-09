package control;

import java.util.ArrayList;

import client.App_client;
import common.Course;
import common.DataPacket;
import common.Exam;
import common.ExtraTimeRequest;
import common.Field;
import common.IncomingDataPacketHandler;
import javafx.stage.Stage;
import common.Student;
import common.Teacher;
import common.User;
import common.examInitiated;
import gui.teacher.editQuestionController;
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
	public DataPacket[] CheckRequestExecuteCreateResponce(Object msg) {
		if (msg instanceof DataPacket) {
			System.out.println("recived DataPacket");
			return ParsingDataPacket((DataPacket) msg);
			// return GET_responce_DataPacket();
		} else
			System.out.println("not DataPacket");
		return new DataPacket[] {null,null};
	}

	@Override
	public DataPacket[] ParsingDataPacket(DataPacket dataPacket) {
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
				ClientController.storedDataPacket = dataPacket;
			}
		}
		else if (dataPacket.getRequest() == DataPacket.Request.LOGOUT) {
			System.out.println("User Logoutted");
			UserControl.ConnectedUser = null;
		}
		
		
		
		////////////////////////////////////////////
		// Students requests
		////////////////////////////////////////////

		else if (dataPacket.getRequest() == DataPacket.Request.ADD_DONE_EXAM) {
			if (dataPacket.getData_parameters() != null) {
				String seccess = (String) dataPacket.getData_parameters().get(0);
				App_client.seccess = seccess; // message setter
			} else
				App_client.seccess = null;

		}
		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAM) {
			if (dataPacket.getData_parameters() != null) {
				System.out.println("qqqqq");
				examInitiated exam = (examInitiated) dataPacket.getData_parameters().get(0);
				Exam exam2 = (Exam) dataPacket.getData_parameters().get(1);
				ExamInitiatedControl.setExamInitiated(exam);
				ExamControl.setExam(exam2);
			} else
				ExamInitiatedControl.setExamInitiated(null);

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
			if(UserControl.isDoingExam && UserControl.whatInitiatedExamID == ((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getEiID())
			{
				System.out.println("Storring the ei ID and time to be added");
				ExamInitiatedControl.ExtraTime = ((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getExtraTime();
				System.out.println(" testtt "+((ExtraTimeRequest) dataPacket.getData_parameters().get(0)).getExtraTime());
				ExamInitiatedControl.isExtraTimeRecived = true;
				
				
			}
			else
				System.out.println("this client skipped in exam?"+UserControl.isDoingExam);
			System.out.println("end of Check if the client in exam state");
		}
		
		

		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION) {
			System.out.println("login insert to get question");
			// System.out.println(dataPacket.getData_parameters().get(0));
			if (dataPacket.getData_parameters() != null)
				App_client.Question = (Question) dataPacket.getData_parameters().get(0);

		}
		
		
		else if (dataPacket.getRequest() == DataPacket.Request.GET_FIELD_NAME) {
			if (dataPacket.getData_parameters() != null)
				App_client.fieldName = (String) dataPacket.getData_parameters().get(0);
			System.out.println("got field name " + App_client.fieldName);

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
		
		

		
		
		// rostik
		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAMS_BY_TEACHER) {
			System.out.println("dannnnny");
			ExamControl.exams = (ArrayList<Exam>) dataPacket.getData_parameters().clone();
		}
		
		
		//////////////////////////////
		// Principal requests
		/////////////////////////////////
		
/////////////////////////////////////////////////// MAX////////////////////////////////////////M
		
		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_COURSE)
		{
			CourseControl.courses = (ArrayList<Course>) dataPacket.getData_parameters().clone();
		}
		
		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_FIELD) {
			FieldControl.fields = (ArrayList<Field>) dataPacket.getData_parameters().clone();
		}
		
		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_EXAM) {
			ExamControl.exams = (ArrayList<Exam>) dataPacket.getData_parameters().clone();
		}
		
		else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_QUESTIONS) {
			QuestionControl.questions = (ArrayList<Question>) dataPacket.getData_parameters().clone();
		}
	
		else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME) {
			ExamControl.selectedCourseID = (String) dataPacket.getData_parameters().get(0);
		}
		
		else if (dataPacket.getRequest() == DataPacket.Request.INSERT_EXAM) {
			ExamControl.examID = (String) dataPacket.getData_parameters().get(0);
		}

		
		

		///
		// daniel
		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXTRA_TIME_REQUESTS) {
			PrincipalControl.requests=(ArrayList<ExtraTimeRequest>)dataPacket.getData_parameters().clone();
		}
		else if (dataPacket.getRequest() == DataPacket.Request.EXTRA_TIME_DECISION) {
			
		}
		
		
		
		
		
		

		return new DataPacket[] {Responce_dataPacket, null};
	}

	public DataPacket GET_responce_DataPacket() {
		return Responce_dataPacket;
	}
}