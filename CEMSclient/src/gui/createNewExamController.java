package gui;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import common.Exam;
import controllers.PageProperties;
import controllers.SceneController;
import controllers.examControl;
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
	@FXML
	private TextField pointsForQuestion;
	@FXML
	private Label totalPointsLabel;
	@FXML
	private TextField duration;
	@FXML
	public void initialize() {
		sceen=new SceneController(PageProperties.Page.CREATE_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		ArrayList<Object> parameters=new ArrayList<>();
		parameters.add(App_client.user);
		DataPacket dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_FIELD_NAME, parameters, null, true);
		App_client.chat.accept(dataPacket);
		if(App_client.fieldName!=null) {
			String fieldName=App_client.fieldName;
			App_client.fieldName=null;
			fieldNameLabel.setText(fieldName);
		}
		errLabel.setVisible(false);
		totalPointsLabel.setText("0");
		dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSES, parameters, null, true);
		App_client.chat.accept(dataPacket);
		ObservableList<String> courseList=FXCollections.observableArrayList();
		courseList.addAll(examControl.coursesNames);
		courses.setItems(courseList);
		dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_QUESTION_BY_FIELD_ID, parameters, null, true);
		ObservableList<String> questionsList=FXCollections.observableArrayList();
		App_client.chat.accept(dataPacket);
		questionsList.addAll(examControl.questions);
		questions.setItems(questionsList);
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
			return;
		
		errLabel.setVisible(false);
		parameters.add(questions.getValue());
		DataPacket dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_QUESTION_BY_DESCRIPTION, parameters, null, true);
		App_client.chat.accept(dataPacket);
		exam.addQuestionAndPoints(examControl.questionID, pointsForQuestion.getText());
		totalPoints+=Double.parseDouble(pointsForQuestion.getText());
		totalPointsLabel.setText(String.valueOf(totalPoints));
		pointsForQuestion.setText("");
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
		if(pointsForQuestion.getText().equals(""))//if the user didn't insert the amount of points in the text
		{
			System.out.println("no points");
			errLabel.setText("*You must insert points for the question");
			errLabel.setVisible(true);
			return true;
		}
		double points=	Double.valueOf(pointsForQuestion.getText());
		if(points>=100)
		{
			errLabel.setText("*You must insert less than 100 in points for the question");
			errLabel.setVisible(true);
			return true;
		}
		if(totalPoints+points>100)
		{
			errLabel.setText("*You exceed the 100 points for the exam");
			errLabel.setVisible(true);
			return true;
		}
		if(!isNumeric(pointsForQuestion.getText()))//if the user didn't insert the numbers to points
		{
			System.out.println("invalid characters");
			errLabel.setText("*You must enter only number to points! ");
			errLabel.setVisible(true);
			return true;
		}
		return false;
	}

	protected boolean isNumeric(String text) {
		ParsePosition pos=new ParsePosition(0);
		NumberFormat.getInstance().parse(text,pos);
		return text.length()==pos.getIndex();
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
		App_client.chat.accept(dataPacket);
		exam.setAuthor(App_client.user.getuid());
		exam.setExamID(App_client.user.getfid()+examControl.selectedCourseID);
		exam.setDuration(duration.getText());
		exam.setStudentsComments(studentComments.getText());
		exam.setTeacherComments(teacherComments.getText());
		parameters.add(0, exam);
		dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.INSERT_EXAM, parameters, null, true);
		App_client.chat.accept(dataPacket);
		exam.setExamID(examControl.examID);
		parameters.add(0, exam);
		dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.INSERT_EXAM_QUESTIONS, parameters, null, true);
		App_client.chat.accept(dataPacket);
		errLabel.setText("exam submited");
		errLabel.setVisible(true);
	}

	private boolean checkInvalidInputsForSumbit() {
		if(exam.getSizeOfMap()==0) {
			errLabel.setText("*Exam is empty");
			errLabel.setVisible(true);
			return true;
		}
		if(!totalPointsLabel.getText().equals("100.0"))
		{
			errLabel.setText("*You must have exaclty 100 in the total points");
			errLabel.setVisible(true);
			return true;
		}
		if(duration.getText().equals(""))
		{
			errLabel.setText("*You must enter duration to the exam");
			errLabel.setVisible(true);
			return true;
		}
		if(!isNumeric(duration.getText())) {
			errLabel.setText("*You must enter only number to duration!");
			errLabel.setVisible(true);
			return true;
		}
		return false;
	}

	
}
