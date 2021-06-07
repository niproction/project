package control;

import common.examInitiated;

public class ExamInitiatedControl {
	private static examInitiated examInitiated;

	public static examInitiated getExamInitiated() {
		return examInitiated;
	}

	public static void setExamInitiated(examInitiated examInitiated) {
		ExamInitiatedControl.examInitiated = examInitiated;
	}
}
