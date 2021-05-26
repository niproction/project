package gui;

import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import common.Exam;
import common.Question;
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
	Exam exam;
	SceneController sceen;
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
		//questionsList.addAll(examControl.quetions);
		questions.setItems(questionsList);
	}
	
	public void handleOnAction(MouseEvent event) {
		if(event.getSource()==submitBtn) {
		//	sumbit();
		}
		else if(event.getSource()==addQuestionBtn) {
			addQuestionToExam();
		}
		
	}
//
//
	private void addQuestionToExam() {
		if(questions.getValue()==null)//if the user didn't choose any question
		{
			errLabel.setText("You must choose question");
			errLabel.setVisible(true);
			return;
		}
		else if(pointsForQuestion.getText()==null)//if the user didn't insert the amount of points in the text
		{
			errLabel.setText("You must insert points for the question");
			errLabel.setVisible(true);
			return;
		}
		//Question tmpQuestion=new Question(questions.getValue(),teacherComments.getText(),studentComments.getText());
		//exam.addQuestion(tmpQuestion);
		
	}
//
//
//	private void sumbit() {
//		ArrayList<Object> parameters=new ArrayList<>();
//		if(exam.getQuestion()==null) {
//			errLabel.setText("You must insert at least one question to the exam");
//			errLabel.setVisible(true);
//			return;
//		}			
//	}

	
}
