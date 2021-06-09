// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This interface implements the abstract method used to display objects onto
 * the client or server UIs.
 *
 * @author Dr Robert Lagani&egrave;re
 * @author Dr Timothy C. Lethbridge
 * @version July 2000
 */
@SuppressWarnings("serial")
public class DataPacket implements Serializable {
	
	
	public enum SendTo {
		SERVER, CLIENT
	}

	public enum Request {
		LOGIN, SECCSESFULLY_LOGINED, LOGOUT,
		
		READ_EXAMS, ADD_NEW_QUESTION,EDIT_QUESTION,GET_QUESTION,
		
		ADD_EXTRA_TIME_TO_EXAM,
		
		GET_FIELD_NAME,GET_COURSES,GET_QUESTION_BY_FIELD_ID,GET_EXAM,
		GET_INFO_COURSES,
		
		ADD_DONE_EXAM
		
		,GET_EXAMS_BY_TEACHER,START_EXAM,
		
/////////////////////barak
GET_QUESTION_BY_DESCRIPTION,
INSERT_EXAM,
GET_COURSE_ID_BY_COURSE_NAME,
INSERT_EXAM_QUESTIONS,
GET_TEST_QUESTIONS,
INSERT_Manuel_EXAM_FILE,
GET_STUDENT_GRADES,
GET_COURSE_NAME_BY_COURSE_ID,
GET_COPY_OF_EXAM,
		//daniel
GET_ONGOING_EXAM, TERMINATE_EXAM
		,GET_EXTRA_TIME_REQUESTS, EXTRA_TIME_DECISION
		
		,GET_INFO_COURSE,GET_INFO_USERS,GET_INFO_FIELD,GET_INFO_EXAM,GET_INFO_QUESTIONS,CHECK_TOOK_EXAM,GET_FOR_VERIFY, DISAPPROVED_GRADE, APPROVED_GRADE
	}


	private SendTo sendTo;
	private Request request;
	private ArrayList<Object> Data_parameters;
	private String message;
	private Boolean result_boolean;

	public DataPacket() {
		this.sendTo = null;
		this.request = null;
		this.Data_parameters = null;
		// this.dataType =null;
		this.result_boolean = null;
	}

	public DataPacket(SendTo sendTo, Request request, ArrayList<Object> Data_parameters, String message,
			boolean result_boolean) {
		this.sendTo = sendTo;
		this.request = request;
		this.Data_parameters = Data_parameters;
		this.message=message;
		this.result_boolean = result_boolean;
	}
	/*
	 * public DataPacket(SendTo sendTo, Request dataType, ArrayList<?>
	 * Data_parameters, String ReturnBackMessageType, boolean result_boolean){
	 * this.sendTo = sendTo; this.dataType = dataType; //this.mySql_action =
	 * mySql_action; this.Data_parameters = Data_parameters; this.result_boolean =
	 * result_boolean; }
	 */

	
	public SendTo getSendTo() {
		return sendTo;
	}

	public void setSendTo(SendTo sendTo) {
		this.sendTo = sendTo;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public ArrayList<Object> getData_parameters() {
		return Data_parameters;
	}

	public void setData_parameters(ArrayList<Object> data_parameters) {
		Data_parameters = data_parameters;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Boolean getResult_boolean() {
		return result_boolean;
	}

	public void setResult_boolean(Boolean result_boolean) {
		this.result_boolean = result_boolean;
	}
}
