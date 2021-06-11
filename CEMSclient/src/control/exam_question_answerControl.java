package control;

import java.util.ArrayList;

import common.exam_question_answer;

public class exam_question_answerControl {
	private static ArrayList<exam_question_answer> list;

	public static ArrayList<exam_question_answer> getList() {
		return list;
	}

	public static void setList(ArrayList<exam_question_answer> list) {
		exam_question_answerControl.list = list;
	}
	

}
