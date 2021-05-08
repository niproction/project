package common;

import java.io.Serializable;

public class Principle extends User implements Serializable {
	private Question[] questions;
	private Exam[] exams;
	
	
	public Principle(String username, String password, String email, String firstName, String lastName) {
		super(username,password,email,firstName,lastName);
		
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
