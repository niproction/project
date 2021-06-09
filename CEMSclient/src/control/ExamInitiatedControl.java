package control;

import java.sql.Time;

import common.examInitiated;

public class ExamInitiatedControl {
	private static examInitiated examInitiated;
	
	private static Time timeLeft;
	public static int eiID;
	public static boolean isExtraTimeRecived;
	public static String ExtraTime;
	
	
	
	public static examInitiated getExamInitiated() {
		return examInitiated;
	}

	public static void setExamInitiated(examInitiated examInitiated) {
		ExamInitiatedControl.examInitiated = examInitiated;
	}
}
