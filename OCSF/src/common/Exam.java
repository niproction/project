package common;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class Exam implements Serializable {
	private String examID;
	private String duration;
	private String teacherComments;
	private String studentsComments;
	private String password;
	private String author;
	private ArrayList<Question> questions;
	public String getExamID() {
		return examID;
	}
	public void setExamID(String examID) {
		this.examID = examID;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getTeacherComments() {
		return teacherComments;
	}
	public void setTeacherComments(String teacherComments) {
		this.teacherComments = teacherComments;
	}
	public String getStudentsComments() {
		return studentsComments;
	}
	public void setStudentsComments(String studentsComments) {
		this.studentsComments = studentsComments;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}
	

	
}
