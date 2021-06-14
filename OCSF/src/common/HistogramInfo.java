package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("serial")
public class HistogramInfo implements Serializable {
	private ArrayList<Double> grades;
	ArrayList<String> coursesName;
	private ArrayList<Integer> counterList;
	private String examID;
	private String courseName;
	private double grade;
	private int gradesCounter[]=new int[9];


	public HistogramInfo(ArrayList<Double> grades, String examID) {
		// super();
		this.grades = grades;
		this.examID = examID;
	}

	public HistogramInfo(ArrayList<String> coursesName, ArrayList<Double> grades) {
		// super();
		this.coursesName = coursesName;
		this.grades = grades;
	}
//	public HistogramInfo(double grade, String courseName) {
//		// super();
//		this.grade = grade;
//		this.courseName = courseName;
//	}

	public ArrayList<String> getCoursesName() {
		return coursesName;
	}

	public void setCoursesName(ArrayList<String> coursesName) {
		this.coursesName = coursesName;
	}

	public ArrayList<Double> getGrades() {
		return grades;
	}

	public void setGrades(ArrayList<Double> grades) {
		this.grades = grades;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public double getGrade() {
		return grade;
	}

	public String getExamID() {
		return examID;
	}

	public void setExamID(String examID) {
		this.examID = examID;
	}

	public double getAvg() {
		double sum = 0;
		for (double tmp : grades)
			sum += tmp;
		return sum / grades.size();
	}

	public double getMedian() {

		int size = grades.size();
		Arrays.sort(grades.toArray());
		double median = 0;
		int i = 0;
		if ((size) % 2 == 1) {
			median = grades.get((size + 1) / 2 - 1);
		} else {
			median = (grades.get(size / 2) + (grades.get((size / 2) - 1))) / 2;
		}
		return median;

	}

	public int[] getCounterList() {
		Arrays.sort(grades.toArray());
		for (Double tmp : grades) {
			if (tmp < 55)
				gradesCounter[0]++;
			else if (tmp < 65)
				gradesCounter[1]++;
			else if (tmp < 70)
				gradesCounter[2]++;
			else if (tmp < 75)
				gradesCounter[3]++;
			else if (tmp < 80)
				gradesCounter[4]++;
			else if (tmp < 85)
				gradesCounter[5]++;
			else if (tmp < 90)
				gradesCounter[6]++;
			else if (tmp < 95)
				gradesCounter[7]++;
			else
				gradesCounter[8]++;

		}
		return gradesCounter;
	}

	
}
