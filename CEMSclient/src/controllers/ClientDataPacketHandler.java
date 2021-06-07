package controllers;

import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import common.Exam;
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
	public DataPacket CheckRequestExecuteCreateResponce(Object msg) {
		if (msg instanceof DataPacket) {
			System.out.println("recived DataPacket");
			return ParsingDataPacket((DataPacket) msg);
			// return GET_responce_DataPacket();
		} else
			System.out.println("not DataPacket");
		return null;
	}

	@Override
	public DataPacket ParsingDataPacket(DataPacket dataPacket) {
		Responce_dataPacket = null;

		if (dataPacket.getRequest() == DataPacket.Request.LOGIN) {
			if (dataPacket.getResult_boolean()) {
				System.out.println("user corrent");
				if (dataPacket.getData_parameters().get(0) instanceof Student) {
					App_client.user = (Student) (dataPacket.getData_parameters().get(0));
					System.out.println("user setted to student");
				} else if (dataPacket.getData_parameters().get(0) instanceof Teacher) {
					App_client.user = (Teacher) (dataPacket.getData_parameters().get(0));
					System.out.println("user setted to teacher");
				} else if (dataPacket.getData_parameters().get(0) instanceof Principal) {
					App_client.user = (Principal) (dataPacket.getData_parameters().get(0));
					System.out.println("user setted to principle");
				} 

			} else
			{
				System.out.println("incorrect user");
				ClientController.storedDataPacket = dataPacket;
			}
		}
		else if (dataPacket.getRequest() == DataPacket.Request.ADD_DONE_EXAM) {
			if(dataPacket.getData_parameters()!=null) {
				String seccess=(String)dataPacket.getData_parameters().get(0);
				App_client.seccess=seccess;
			}
			else
				App_client.seccess=null;
			
		}
		else if (dataPacket.getRequest() == DataPacket.Request.GET_EXAM) {
			if(dataPacket.getData_parameters()!=null) {
				System.out.println("qqqqq");
				examInitiated exam= (examInitiated) dataPacket.getData_parameters().get(0);
				Exam exam2=(Exam) dataPacket.getData_parameters().get(1);
				examInitiatedControl.setExamInitiated(exam);
				examControl.setExam(exam2);
			}
			else
				examInitiatedControl.setExamInitiated(null);
			
		}
		else if (dataPacket.getRequest() == DataPacket.Request.GET_TEST_QUESTIONS) {
			System.out.println("get test questions");
			if(dataPacket.getData_parameters()!=null) {
				System.out.println("insert get test questions");
				Exam exam=(Exam)dataPacket.getData_parameters().get(0);
				System.out.println(exam.getExamID()+"4444");
				examControl.setExam(exam);
			}
			else {
				System.out.println("problemmmm");
				examControl.setExam(null);
			}
			
		}

		else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION) {
			System.out.println("login insert to get question");
			// System.out.println(dataPacket.getData_parameters().get(0));
			if (dataPacket.getData_parameters() != null)
				App_client.Question = (Question) dataPacket.getData_parameters().get(0);
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_FIELD_NAME) {
			if (dataPacket.getData_parameters() != null)
				App_client.fieldName = (String) dataPacket.getData_parameters().get(0);
			System.out.println("got field name " + App_client.fieldName);
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSES) {
			examControl.coursesNames=(ArrayList<String>) dataPacket.getData_parameters().clone();
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_FIELD_ID) {
			examControl.questions = (ArrayList<String>) dataPacket.getData_parameters().clone();
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_QUESTION_BY_DESCRIPTION) {
			examControl.questionID = (String) dataPacket.getData_parameters().get(0);
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_INFO_USERS) {
			UserControl.user = (ArrayList<User>) dataPacket.getData_parameters().clone();
		} else if (dataPacket.getRequest() == DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME) {
			examControl.selectedCourseID = (String) dataPacket.getData_parameters().get(0);
		}
		else if (dataPacket.getRequest() == DataPacket.Request.INSERT_EXAM) {
			examControl.examID = (String) dataPacket.getData_parameters().get(0);
		}
		
		return Responce_dataPacket;
	}

	public DataPacket GET_responce_DataPacket() {
		return Responce_dataPacket;
	}
}
