package controllers;

import java.util.ArrayList;

public class viewGradesControl {
	public static ArrayList<String> grades=new ArrayList<>();
	public static ArrayList<String> examsID=new ArrayList<>();
	public static ArrayList<String> coursesName=new ArrayList<>();
	public static ArrayList<String> examsInitID=new ArrayList<>();
	
	//get data by index
	public static String getExamID(int i)
	{
		return examsID.get(i);
	}
	public static String getCourseName(int i)
	{
		return coursesName.get(i);
	}
	public static String getGrade(int i)
	{
		return grades.get(i);
	}
	
	public static String getExamsInitID(int i)
	{
		return examsInitID.get(i);
	}
	//add data by index
	public static void addExamsID(String examID)
	{
		examsID.add(examID);
	}
	public static void addGrade(String grade)
	{
		grades.add(grade);
	}
	public static void addCourseName(String courseName) {
		coursesName.add(courseName);
	}
	public static void addExamsInitID(String examInitID)
	{
		examsInitID.add(examInitID);
	}
	
	//get size
	public static int getSize()
	{
		return examsInitID.size();
	}
}
