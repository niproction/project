package control;

import java.util.ArrayList;

public class ViewGradesControl {
	public static ArrayList<Integer> grades=new ArrayList<>();
	public static ArrayList<String> examsID=new ArrayList<>();
	public static ArrayList<String> coursesName=new ArrayList<>();
	public static ArrayList<Integer> examsInitID=new ArrayList<>();
	public static Boolean emptyGrades=false;
	//get data by index
	public static String getExamID(int i)
	{
		return examsID.get(i);
	}
	public static String getCourseName(int i)
	{
		return coursesName.get(i);
	}
	public static int getGrade(int i)
	{
		return grades.get(i);
	}
	
	public static int getExamsInitID(int i)
	{
		return examsInitID.get(i);
	}
	//add data by index
	public static void addExamsID(String examID)
	{
		examsID.add(examID);
	}
	public static void addGrade(int grade)
	{
		grades.add(grade);
	}
	public static void addCourseName(String courseName) {
		coursesName.add(courseName);
	}
	public static void addExamsInitID(int examInitID)
	{
		examsInitID.add(examInitID);
	}
	
	//get size
	public static int getSize()
	{
		return examsInitID.size();
	}
}