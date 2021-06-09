package control;
import java.util.ArrayList;

import common.ExamDone;

public class examDoneControl {
	private static ExamDone examDone;
	private static ArrayList<ExamDone> examDoneLIst;
	public static ArrayList<ExamDone> getExamDoneLIst() {
		return examDoneLIst;
	}

	public static void setExamDoneLIst(ArrayList<ExamDone> examDoneLIst) {
		examDoneControl.examDoneLIst = examDoneLIst;
	}

	public static ExamDone getExamDone() {
		return examDone;
	}

	public static void setExamDone(ExamDone examDone) {
		examDoneControl.examDone = examDone;
	}
	

}