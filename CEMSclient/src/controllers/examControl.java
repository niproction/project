package controllers;

import java.util.ArrayList;

import common.Exam;


public  class examControl {
	public static ArrayList<String> coursesNames;
	public static String selectedCourse;
	public static ArrayList<String> questions;
	public static String questionID;
	public static Exam exam;
	public static String selectedCourseID;
	public static String examID;
	public static ArrayList<Exam> exams;
	
	
	public static String getExamID() {
		return examID;
	}
	public static void setExamID(String examID) {
		examControl.examID = examID;
	}
	public static Exam getExam() {
		return exam;
	}
	public static void setExam(Exam exam) {
		examControl.exam = exam;
	}
	
	
}
