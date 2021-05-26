package controllers;

import java.util.ArrayList;

import common.Exam;


public  class examControl {
	public static ArrayList<String> coursesNames;
	private static Exam exam;
	public static Exam getExam() {
		return exam;
	}
	public static void setExam(Exam exam) {
		examControl.exam = exam;
	}
	
	
}
