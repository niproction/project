package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Course implements Serializable  {
	public String courseID;
	public String courseName;
	public String fieldID;

	public Course(String courseID, String fieldID, String courseName) {
		this.courseID = courseID;
		this.fieldID = fieldID;
		this.courseName = courseName;

	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getFieldID() {
		return fieldID;
	}

	public void setFieldID(String fieldID) {
		this.fieldID = fieldID;
	}
}
