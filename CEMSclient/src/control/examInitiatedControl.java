package control;

import common.examInitiated;

public class examInitiatedControl {
	private static examInitiated examInitiated;

	public static examInitiated getExamInitiated() {
		return examInitiated;
	}

	public static void setExamInitiated(examInitiated examInitiated) {
		examInitiatedControl.examInitiated = examInitiated;
	}
	

}
