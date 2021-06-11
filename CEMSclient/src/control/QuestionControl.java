package control;

import java.util.ArrayList;

import common.Question;

public class QuestionControl {
	
	private static ArrayList<Question> questions=new ArrayList<Question>();
	private static Question spesificQuestion;

	public static Question getSpesificQuestion() {
		return spesificQuestion;
	}

	public static void setSpesificQuestion(Question spesificQuestion) {
		QuestionControl.spesificQuestion = spesificQuestion;
	}

	public static ArrayList<Question> getQuestions() {
		return QuestionControl.questions;
	}

	public static void setQuestions(ArrayList<Question> questions) {
		QuestionControl.questions=questions;
	}

}