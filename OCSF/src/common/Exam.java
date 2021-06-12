package common;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("serial")
public class Exam implements Serializable {
	private String examID;
	private Integer authorID;
	private String description;
	private String duration;
	private String teacherComments;
	private String studentsComments;
	private ArrayList<Question> questions;
	private HashMap<String, String> questionToPointsMap=new HashMap<>();
	
	public void addQuestionAndPoints(String question,String points)
	{
		//questions.add(question);
		questionToPointsMap.put(question, points);
	}
	public Exam(){
		
	}

	public Exam(String examID,Integer authorID, String description, String duration, String teacherComments, String studentsComments,ArrayList<Question> questions) {
		this.examID=examID;
		this.authorID = authorID;
		this.description = description;
		this.duration = duration;
		this.teacherComments = teacherComments;
		this.studentsComments = studentsComments;
		this.questions = questions;
		//this.fid=fid;
	}
	public Exam(String examID, int authorID, String description, String duration, String teacherComments,
			String studentsComments) {
		this.examID = examID;
		this.authorID = authorID;
		this.description = description;
		this.duration = duration;
		this.teacherComments = teacherComments;
		this.studentsComments = studentsComments;
	}
//////constructor for manual exam
	public Exam(String examID, int authorID, String description, String duration) {
		this.examID = examID;
		this.authorID = authorID;
		this.description = description;
		this.duration = duration;
	}


	public String getPointsForQuestions(String question) {
		return questionToPointsMap.get(question);
	}
	public Set<String> getMapKey(){

		return  questionToPointsMap.keySet();
	}
	public int getSizeOfMap()
	{
		return questionToPointsMap.size();
	}
	
	
	
	
	public String getExamID() {
		return examID;
	}
	public void setExamID(String examID) {
		this.examID = examID;
	}
	
	public int getAuthorID() {
		return authorID;
	}
	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	
	
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}
	

	@Override
    public String toString() {
        return "ID:"+examID.toString()+" | "+description;
    }
}
