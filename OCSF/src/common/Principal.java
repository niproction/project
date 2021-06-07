package common;

import java.io.Serializable;

public class Principal extends User implements Serializable {
	private Question[] questions;
	private Exam[] exams;
	
	
	public Principal(int uID, String username, String password, String email, String firstName, String lastName,String fid) {
		super(uID,username,password,email,firstName,lastName,fid);
		
	}
	
	public Question[] GET_questions() {
		return questions;
	}

	public Exam[] GET_exams() {
		return exams;
	}
	
	public void SET_questions(Question[] questions) {
		this.questions=questions;
	}

	public void SET_exams(Exam[] exams) {
		this.exams=exams;
	}
}
