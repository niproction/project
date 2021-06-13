package control;

import java.util.ArrayList;

public class GetCopyOfExamControl {
	public static ArrayList  <String> questionsDescription = new ArrayList<>();
	public static ArrayList<String> correctAnswersDescription=new ArrayList<>();
	public static ArrayList<String> studentAnswersDescription=new ArrayList<>();
	public static ArrayList<String> pointsForQuestion=new ArrayList<>();
	public static int eID;
	public static Boolean emptyHistory;
	public static String teacherComm;
	public static int studentID;
	public static String studentComm;
	public static String getQuestionDescription(int i)
	{
		return questionsDescription.get(i);
	}
	
	public static String getCorrectAnswerDescription(int i)
	{
		return correctAnswersDescription.get(i);
	}
	
	public static String getStudentAnswerDescription(int i)
	{
		return studentAnswersDescription.get(i);
	}
	
	public static String getpointsForQuestion(int i)
	{
		return pointsForQuestion.get(i);
	}
}