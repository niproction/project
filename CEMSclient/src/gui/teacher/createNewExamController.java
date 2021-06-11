package gui.teacher;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import common.Exam;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import control.ClientControl;
import control.ExamControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class createNewExamController {
	Exam exam=new Exam();
	SceneController sceen;
	private double totalPoints=0;
	@FXML // fx:id="ap"
    private AnchorPane ap;
	@FXML
	private Label errLabel;
	@FXML
	private Label fieldNameLabel;
	@FXML
	private ChoiceBox<String> courses;
	@FXML
	private ChoiceBox<String> questions;
	@FXML
	private TextArea teacherComments;
	@FXML
	private TextArea studentComments;
	@FXML
	private Button addQuestionBtn;
	@FXML
	private Button submitBtn;
//	@FXML
//	private TextField pointsForQuestion;
	@FXML
	private Label totalPointsLabel;
	@FXML
	private TextField nameOfExam;
	@FXML
	private ChoiceBox<String> minutesChoiceBox;
	@FXML
	private ChoiceBox<String> secondsChoiceBox;
	@FXML
	private ChoiceBox<String> hourChoiceBox;
	@FXML
	private ChoiceBox<String> pointsForQuestion;
	@FXML
	public void initialize() {
		sceen=new SceneController(PageProperties.Page.CREATE_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		setupDuration();
		setupPoints();
		ArrayList<Object> parameters=new ArrayList<>();
		parameters.add(UserControl.ConnectedUser);
		DataPacket dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_FIELD_NAME, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		if(App_client.fieldName!=null) {
			String fieldName=App_client.fieldName;
			App_client.fieldName=null;
			fieldNameLabel.setText(fieldName);
		}
		errLabel.setVisible(false);
		totalPointsLabel.setText("0");
		dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSES, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		ObservableList<String> courseList=FXCollections.observableArrayList();
		courseList.addAll(ExamControl.coursesNames);
		courses.setItems(courseList);
		dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_QUESTION_BY_FIELD_ID, parameters, null, true);
		ObservableList<String> questionsList=FXCollections.observableArrayList();
		ClientControl.getInstance().accept(dataPacket);
		questionsList.addAll(ExamControl.questions);
		questions.setItems(questionsList);
	}
	
	private void setupPoints() {
		ObservableList<String> pointLsist = FXCollections.observableArrayList();
		for (int i = 1; i <= 100; i++) {
			pointLsist.add(Integer.toString(i));
		}
		pointsForQuestion.setItems(pointLsist);
	}

	protected void setupDuration() {
		ObservableList<String> hourList = FXCollections.observableArrayList();
		ObservableList<String> minAndSecondList = FXCollections.observableArrayList();
		

		errLabel.setVisible(false);
		for (int i = 0; i < 60; i++) {
			minAndSecondList.add(Integer.toString(i));
		}
		for (int i = 0; i < 24; i++) {
			hourList.add(Integer.toString(i));
		}


		hourChoiceBox.setItems(hourList);
		minutesChoiceBox.setItems(minAndSecondList);
		secondsChoiceBox.setItems(minAndSecondList);

	}

	public void handleOnAction(MouseEvent event) {
		 if(event.getSource()==addQuestionBtn) {
			 addQuestionToExam();
		}
		 else if(event.getSource()==submitBtn)
		 {
			 sumbit();
		 }
		
	}


	private void addQuestionToExam() {
		ArrayList<Object> parameters=new ArrayList<>();
		if(checkInvalidInputsForAddQuestion()==true)
		{
			return;
		}
		errLabel.setVisible(false);
		parameters.add(questions.getValue());
		DataPacket dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_QUESTION_BY_DESCRIPTION, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		System.out.println(ExamControl.questionID+"      "+pointsForQuestion.getValue());
		exam.addQuestionAndPoints(ExamControl.questionID, pointsForQuestion.getValue());
		System.out.println("frommm mapppp   "+exam.getPointsForQuestions(ExamControl.questionID));
		totalPoints+=Double.parseDouble(pointsForQuestion.getValue());
		System.out.println("total points:"+totalPoints);
		totalPointsLabel.setText(String.valueOf(totalPoints));
	}

	private boolean checkInvalidInputsForAddQuestion() {

		if(courses.getValue()==null)
		{
			System.out.println("no course");
			errLabel.setText("*You must choose course");
			errLabel.setVisible(true);
			return true;
		}
		if(questions.getValue()==null)//if the user didn't choose any question
		{
			System.out.println("no question");
			errLabel.setText("*You must choose question");
			errLabel.setVisible(true);
			return true;
		}
		if(pointsForQuestion.getValue()==null)//if the user didn't insert the amount of points in the text
		{
			System.out.println("no points");
			errLabel.setText("*You must insert points for the question");
			errLabel.setVisible(true);
			return true;
		}
		double points=	Double.valueOf(pointsForQuestion.getValue());


		if(totalPoints+points>100)
		{
			errLabel.setText("*You exceed the 100 points for the exam");
			errLabel.setVisible(true);
			return true;
		}
		if(points>100)
		{
			errLabel.setText("*You must insert less than 100 in points for the question");
			errLabel.setVisible(true);
			return true;
		}


		return false;
	}



	private void sumbit() {
		ArrayList<Object> parameters=new ArrayList<>();
		if(checkInvalidInputsForSumbit()==true)
		{
			return;
		}
		errLabel.setVisible(false);
		parameters.add(courses.getValue());
		DataPacket dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		String duration="";
		//bulding duration for the exam
		if(hourChoiceBox.getValue()==null)
			duration+="00:";
		else {
			duration+=hourChoiceBox.getValue()+":";
		}
		if(minutesChoiceBox.getValue()==null)
			duration+="00:";
		else {
			duration+=minutesChoiceBox.getValue()+":";
		}
		if(secondsChoiceBox.getValue()==null)
			duration+="00";
		else {
			duration+=secondsChoiceBox.getValue();
		}
		exam.setAuthorID(UserControl.ConnectedUser.getuID());
		exam.setExamID(UserControl.ConnectedUser.getfid()+ExamControl.selectedCourseID);
		exam.setDuration(duration);
		exam.setDescription(nameOfExam.getText());
		exam.setStudentsComments(studentComments.getText());
		exam.setTeacherComments(teacherComments.getText());
		parameters.clear();
		parameters.add(exam);
		dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.INSERT_EXAM, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		exam.setExamID(ExamControl.examID);
		parameters.clear();
		parameters.add(exam);
		dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.INSERT_EXAM_QUESTIONS, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		errLabel.setText("exam submited");
		errLabel.setVisible(true);
	}

	private boolean checkInvalidInputsForSumbit() {
		if(exam.getSizeOfMap()==0) {
			errLabel.setText("*Exam is empty");
			errLabel.setVisible(true);
			return true;
		}
		if(nameOfExam.getText().equals(""))
		{
			System.out.println("empty name of the exam");
			errLabel.setText("*You must enter name for the exam! ");
			errLabel.setVisible(true);
			return true;
		}
		if(!totalPointsLabel.getText().equals("100.0"))
		{
			errLabel.setText("*You must have exaclty 100 in the total points");
			errLabel.setVisible(true);
			return true;
		}
		if (hourChoiceBox.getValue()==null && minutesChoiceBox.getValue()==null  && secondsChoiceBox.getValue()==null) {
			errLabel.setText("*You must enter duration to the exam");
			errLabel.setVisible(true);
			return true;
		}

		if(nameOfExam.getText().equals(""))
		{
			errLabel.setText("*You must name for the exam");
			errLabel.setVisible(true);
			return true;
		}
		return false;
	}

	
}