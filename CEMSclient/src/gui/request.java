package gui;

import java.sql.Date;
import java.time.LocalDate;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



public class request {
	Stage stage;

	
	
	
	
	
	
	private String TeacherName, Field, CourseId;
	private HBox Status;
	private LocalDate RequestTimestamp;
	private Integer ExamId,ExtraTime;
	public request(String teacherName, String field, String courseId, HBox status, LocalDate requestTimestamp, Integer examId,
			Integer extraTime) {
		TeacherName = teacherName;
		Field = field;
		CourseId = courseId;
		Status = status;
		RequestTimestamp = requestTimestamp;
		ExamId = examId;
		ExtraTime = extraTime;
		
		
		
		
		
		
	}
	public String getTeacherName() {
		return TeacherName;
	}
	public void setTeacherName(String teacherName) {
		TeacherName = teacherName;
	}
	public String getField() {
		return Field;
	}
	public void setField(String field) {
		Field = field;
	}
	public String getCourseId() {
		return CourseId;
	}
	public void setCourseId(String courseId) {
		CourseId = courseId;
	}
	public HBox getStatus() {
		return Status;
	}
	public void setStatus(HBox status) {
		Status = status;
	}
	public LocalDate getRequestTimestamp() {
		return RequestTimestamp;
	}
	public void setRequestTimestamp(LocalDate requestTimestamp) {
		RequestTimestamp = requestTimestamp;
	}
	public Integer getExamId() {
		return ExamId;
	}
	public void setExamId(Integer examId) {
		ExamId = examId;
	}
	public Integer getExtraTime() {
		return ExtraTime;
	}
	public void setExtraTime(Integer extraTime) {
		ExtraTime = extraTime;
	}
	
	
	





	
	
}
