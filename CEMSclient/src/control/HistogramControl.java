package control;

import java.util.ArrayList;
import java.util.Arrays;

import common.Course;
import common.HistogramInfo;
import common.User;

public class HistogramControl {
	public static ArrayList<User> examsOfuser;
	public static ArrayList<Course> examsOfCourse;

	// public static ArrayList<String> ;
	public static ArrayList<HistogramInfo> examOfTeacher;
	public static ArrayList<HistogramInfo> examOfStudent;
	public static ArrayList<HistogramInfo> CourseExamGradeList;
	
	/** The Avgexam of teacher. */
	public static ArrayList<HistogramInfo> AvgexamOfTeacher;

	public HistogramControl() {

	}

	public static String getAvgSt() {
		double sum = 0;
		double[] grades = new double[examOfStudent.size()]; 

		for (HistogramInfo tmp : examOfStudent) {
			sum += tmp.getGrade();
		}
		sum = sum / examOfStudent.size();
		return Double.toString(sum);
	}

	public static String getMedianSt() {
		double sum = 0, median = 0;
		int size = examOfStudent.size(), i = 0; 
		double[] grades = new double[size];
		for (HistogramInfo tmp : examOfStudent) {
			grades[i] += tmp.getGrade();
			i++;
		}
		Arrays.sort(grades);

		if (examOfStudent.size() % 2 == 1) {
			median = grades[examOfStudent.size() / 2 - 1];
		} else {
			median = (grades[examOfStudent.size() / 2 - 1] + grades[examOfStudent.size() / 2]) / 2;
		}
		return Double.toString(median);
	}
}