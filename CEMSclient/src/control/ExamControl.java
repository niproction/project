package control;

import java.util.ArrayList;

import common.Exam;
import common.ExtraTimeRequest;
import common.Question;


public class ExamControl {
	public static ArrayList<String> coursesNames;
	public static String selectedCourse;
	public static boolean editExamFlag=false;
	public static ArrayList<String> questions = new ArrayList<>();
	public static String questionID;
	public static Exam exam;
	public static String selectedCourseID;
	public static String examID;
	public static ArrayList<Exam> exams=new ArrayList<>();
	
	public static String ServerTime;
	public static String examInitiatedTime;
	public static ArrayList<Exam> examsList;
	public static ExtraTimeRequest extraTimeRequest;
	
	
	public static ArrayList<String> questionsIDToRemove=new ArrayList<>();
	public static ArrayList<String> pointsForQuestion=new ArrayList<>();
	public static ArrayList<String> questionsID=new ArrayList<>();
	public static ArrayList<Question>questionsInExams=new ArrayList<>();
	public static ArrayList<Question> questionNotInExams=new ArrayList<>();


	//rostik v10
	private static boolean isExamTerminated=false;
	private static boolean isNotifiedAboutExtraTime = false;
	private static boolean isExtraTimeApproved = false;
	public static ArrayList<Integer> amountOfQuestions;
	
	
	
	public static boolean isExamTerminated() {
		return isExamTerminated;
	}
	public static void setExamTerminated(boolean isExamTerminated) {
		ExamControl.isExamTerminated = isExamTerminated;
	}
	public static boolean isNotifiedAboutExtraTime() {
		return isNotifiedAboutExtraTime;
	}
	public static void setNotifiedAboutExtraTime(boolean isNotifiedAboutExtraTime) {
		ExamControl.isNotifiedAboutExtraTime = isNotifiedAboutExtraTime;
	}
	public static boolean isExtraTimeApproved() {
		return isExtraTimeApproved;
	}
	public static void setExtraTimeApproved(boolean isExtraTimeApproved) {
		ExamControl.isExtraTimeApproved = isExtraTimeApproved;
	}
	//rostik v10
	public static ArrayList<Exam> getExamsList() {
		return examsList;
	}
	public static void setExamsList(ArrayList<Exam> examsList) {
		ExamControl.examsList = examsList;
	}
	
	
	public static String getExamID() {
		return examID;
	}
	public static void setExamID(String examID) {
		ExamControl.examID = examID;
	}
	public static Exam getExam() {
		return exam;
	}
	public static void setExam(Exam exam) {
		ExamControl.exam = exam;
	}
	
	
}
