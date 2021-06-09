package controllers;

import java.util.ArrayList;

import common.Question;

public class questionControl {
	private static ArrayList<Question> questions=new ArrayList<Question>();

	public static ArrayList<Question> getQuestions() {
		return questionControl.questions;
	}

	public static void setQuestions(ArrayList<Question> questions) {
		questionControl.questions=questions;
	}

}
