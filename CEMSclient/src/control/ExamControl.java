package control;

import java.util.ArrayList;

import common.Exam;
import common.ExtraTimeRequest;
import common.Question;


// TODO: Auto-generated Javadoc
/**
 * The Class ExamControl.
 * this control 
 */
public class ExamControl {
	
	/** The courses names. */
	public static ArrayList<String> coursesNames;
	
	/** The selected course. */
	public static String selectedCourse;
	
	/** The edit exam flag. */
	public static boolean editExamFlag=false;
	
	/** The questions. */
	public static ArrayList<String> questions = new ArrayList<>();
	
	/** The question ID. */
	public static String questionID;
	
	/** The exam. */
	public static Exam exam;
	
	/** The selected course ID. */
	public static String selectedCourseID;
	
	/** The exam ID. */
	public static String examID;
	
	/** The exams. */
	public static ArrayList<Exam> exams;
	
	/** The Server time. */
	public static String ServerTime;
	
	/** The exam initiated time. */
	public static String examInitiatedTime;
	
	/** The exams list. */
	public static ArrayList<Exam> examsList;
	
	/** The extra time request. */
	public static ExtraTimeRequest extraTimeRequest;
	
	
	/** The questions ID to remove. */
	public static ArrayList<String> questionsIDToRemove=new ArrayList<>();
	
	/** The points for question. */
	public static ArrayList<String> pointsForQuestion=new ArrayList<>();
	
	/** The questions ID. */
	public static ArrayList<String> questionsID=new ArrayList<>();
	
	/** The questions in exams. */
	public static ArrayList<Question>questionsInExams=new ArrayList<>();
	
	/** The question not in exams. */
	public static ArrayList<Question> questionNotInExams=new ArrayList<>();


	/** The is exam terminated. */
	//rostik v10
	private static boolean isExamTerminated=false;
	
	/** The is notified about extra time. */
	private static boolean isNotifiedAboutExtraTime = false;
	
	/** The is extra time approved. */
	private static boolean isExtraTimeApproved = false;
	
	
	
	/**
	 * Checks if is exam terminated.
	 *
	 * @return true, if is exam terminated
	 */
	public static boolean isExamTerminated() {
		return isExamTerminated;
	}
	
	/**
	 * Sets the exam terminated.
	 *
	 * @param isExamTerminated the new exam terminated
	 */
	public static void setExamTerminated(boolean isExamTerminated) {
		ExamControl.isExamTerminated = isExamTerminated;
	}
	
	/**
	 * Checks if is notified about extra time.
	 *
	 * @return true, if is notified about extra time
	 */
	public static boolean isNotifiedAboutExtraTime() {
		return isNotifiedAboutExtraTime;
	}
	
	/**
	 * Sets the notified about extra time.
	 *
	 * @param isNotifiedAboutExtraTime the new notified about extra time
	 */
	public static void setNotifiedAboutExtraTime(boolean isNotifiedAboutExtraTime) {
		ExamControl.isNotifiedAboutExtraTime = isNotifiedAboutExtraTime;
	}
	
	/**
	 * Checks if is extra time approved.
	 *
	 * @return true, if is extra time approved
	 */
	public static boolean isExtraTimeApproved() {
		return isExtraTimeApproved;
	}
	
	/**
	 * Sets the extra time approved.
	 *
	 * @param isExtraTimeApproved the new extra time approved
	 */
	public static void setExtraTimeApproved(boolean isExtraTimeApproved) {
		ExamControl.isExtraTimeApproved = isExtraTimeApproved;
	}
	
	/**
	 * Gets the exams list.
	 *
	 * @return the exams list
	 */
	//rostik v10
	public static ArrayList<Exam> getExamsList() {
		return examsList;
	}
	
	/**
	 * Sets the exams list.
	 *
	 * @param examsList the new exams list
	 */
	public static void setExamsList(ArrayList<Exam> examsList) {
		ExamControl.examsList = examsList;
	}
	
	
	/**
	 * Gets the exam ID.
	 *
	 * @return the exam ID
	 */
	public static String getExamID() {
		return examID;
	}
	
	/**
	 * Sets the exam ID.
	 *
	 * @param examID the new exam ID
	 */
	public static void setExamID(String examID) {
		ExamControl.examID = examID;
	}
	
	/**
	 * Gets the exam.
	 *
	 * @return the exam
	 */
	public static Exam getExam() {
		return exam;
	}
	
	/**
	 * Sets the exam.
	 *
	 * @param exam the new exam
	 */
	public static void setExam(Exam exam) {
		ExamControl.exam = exam;
	}
	
	
}
