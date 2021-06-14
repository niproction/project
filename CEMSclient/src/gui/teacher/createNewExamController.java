package gui.teacher;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;

import com.jfoenix.controls.JFXComboBox;

import client.App_client;
import common.DataPacket;
import common.Exam;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import gui.TableEntry;
import control.ClientControl;
import control.ExamControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class createNewExamController {
	Exam exam = new Exam();
	SceneController sceen;
	private double totalPoints = 0;
	@FXML
	private AnchorPane apSubmitedExam;
	@FXML
	private Button createNewExamBtn;
	@FXML
	private Button goToStartExamBtn;
	@FXML // fx:id="ap"
	private AnchorPane apComments;
	@FXML
	private TableView<TableEntry> chooseQuestionsTable;
	@FXML
	private TableView<TableEntry> examQuestionTable;
	@FXML
	private TableColumn<TableEntry, ?> questionToAddCol;
	@FXML
	private TableColumn<TableEntry, ?> pointsToAddCol;
	@FXML
	private TableColumn<TableEntry, ?> addQuestionCol;
	@FXML
	private TableColumn<TableEntry, ?> questionAtExamCol;
	@FXML
	private TableColumn<TableEntry, ?> pointsAtExamCol;
	@FXML
	private TableColumn<TableEntry, ?> removeQuestionCol;
	@FXML
	private Label errLabel;
	@FXML
	private Label fieldNameLabel;
	@FXML
	private JFXComboBox<String> courses;
	@FXML
	private TextArea teacherComments;
	@FXML
	private TextArea studentComments;
	@FXML
	private Button submitBtn;
	@FXML
	private Button backToExamBtn;
	@FXML
	private Label totalPointsLabel;
	@FXML
	private TextField nameOfExam;
	@FXML
	private JFXComboBox<String> minutesChoiceBox;
	@FXML
	private JFXComboBox<String> hourChoiceBox;
	private int indexAtBank;
	private int indexAtExam;
	private ArrayList<Button> removeBtnList = new ArrayList<>();
	private ArrayList<Button> btnList = new ArrayList<>();
	private ObservableList<TableEntry> dataBank = FXCollections.observableArrayList();
	ObservableList<TableEntry> questionBank = FXCollections.observableArrayList();
	@FXML
	private AnchorPane ap;
	@FXML
	private Button addCommentsBtn;
	@FXML
	public void initialize() {
		sceen = new SceneController(PageProperties.Page.CREATE_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		questionToAddCol.setCellValueFactory(new PropertyValueFactory("col1"));
		pointsToAddCol.setCellValueFactory(new PropertyValueFactory("col2"));
		addQuestionCol.setCellValueFactory(new PropertyValueFactory("col3"));
		questionAtExamCol.setCellValueFactory(new PropertyValueFactory("col1"));
		pointsAtExamCol.setCellValueFactory(new PropertyValueFactory("col2"));
		removeQuestionCol.setCellValueFactory(new PropertyValueFactory("col3"));
		ExamControl.questions.clear();
		apComments.setVisible(false);
		setupDuration();
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(UserControl.ConnectedUser);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_FIELD_NAME, parameters,
				null, true);
		ClientControl.getInstance().accept(dataPacket);
		if (App_client.fieldName != null) {
			String fieldName = App_client.fieldName;
			App_client.fieldName = null;
			fieldNameLabel.setText(fieldName);
		}
		errLabel.setVisible(false);
		totalPointsLabel.setText("0");
		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSES, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		ObservableList<String> courseList = FXCollections.observableArrayList();
		courseList.addAll(ExamControl.coursesNames);
		courses.setItems(courseList);
		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_QUESTION_BY_FIELD_ID, parameters,
				null, true);
		ObservableList<String> questionsList = FXCollections.observableArrayList();
		ClientControl.getInstance().accept(dataPacket);
		questionsList.addAll(ExamControl.questions);
//		 ObservableList<TableEntry> questionBank =
//		 FXCollections.observableArrayList();
		insertQuestionToChooseQuestionTable(questionsList, questionBank);
	}

	private void insertQuestionToChooseQuestionTable(ObservableList<String> questionsList,
			ObservableList<TableEntry> questionBank) {

		for (int i = 0; i < ExamControl.questions.size(); i++) {
			addButtonAndTextFieldToTable(questionBank,i);
		}
		chooseQuestionsTable.setItems(questionBank);
		TableEntry.customResize(chooseQuestionsTable);
	}

	private void addButtonAndTextFieldToTable(ObservableList<TableEntry> questionBank,int i) {
		final Button addQuestioBtn = new Button("Add");
		final TextField pointsTextField = new TextField();
		addQuestioBtn.setAlignment(Pos.CENTER);
		btnList.add(addQuestioBtn);
		EventHandler<ActionEvent> addHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (checkInvalidInputsForAddQuestion(pointsTextField.getText()) == true) {
					return;
				}
				for (int j = 0; j < btnList.size(); j++) {// in order to find the correct row in the tableView
					if (btnList.get(j).equals(addQuestioBtn)) {
						indexAtBank = j;
						btnList.remove(indexAtBank);
						break;
					}
				}
				addQuestionToExam(pointsTextField.getText(), (String) questionBank.get(indexAtBank).getCol1());
				questionBank.remove(indexAtBank);
				chooseQuestionsTable.setItems(questionBank);
				TableEntry.customResize(chooseQuestionsTable);
			}

		};
		addQuestioBtn.setOnAction(addHandler);
		questionBank.add(new TableEntry(ExamControl.questions.get(i), pointsTextField, addQuestioBtn));
	}

	private void addQuestionToExam(String pointsForQuestion, String question) {
		ArrayList<Object> parameters = new ArrayList<>();
		if (checkInvalidInputsForAddQuestion(pointsForQuestion) == true) {
			return;
		}
		errLabel.setVisible(false);
		parameters.add(question);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_QUESTION_BY_DESCRIPTION,
				parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		System.out.println(ExamControl.questionID + "      " + pointsForQuestion);
		exam.addQuestionAndPoints(ExamControl.questionID, pointsForQuestion);
		totalPoints += Double.parseDouble(pointsForQuestion);
		totalPointsLabel.setText(String.valueOf(totalPoints));

		insertDataOfQuestionToExam(question, pointsForQuestion);
	}

	private void insertDataOfQuestionToExam(String question, String pointsForQuestion) {
		final Button removeQuestioBtn = new Button("Remove");
		final Label pointsLabel = new Label(pointsForQuestion);
		removeQuestioBtn.setAlignment(Pos.CENTER);
		removeBtnList.add(removeQuestioBtn);
		EventHandler<ActionEvent> removeHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (int j = 0; j < removeBtnList.size(); j++) {// in order to find the correct row in the tableView
					if (removeBtnList.get(j).equals(removeQuestioBtn))
						indexAtExam = j;
				}
				removeQuestionFromExam(indexAtExam, pointsForQuestion);
			}

		};
		removeQuestioBtn.setOnAction(removeHandler);
		dataBank.add(new TableEntry(question, pointsLabel, removeQuestioBtn));
		examQuestionTable.setItems(dataBank);
		TableEntry.customResize(examQuestionTable);
	}

	protected void removeQuestionFromExam(int i, String pointsForQuestion) {
		totalPoints -= Double.valueOf(pointsForQuestion);
		totalPointsLabel.setText(String.valueOf(totalPoints));
		removeBtnList.remove(i);
		String question = (String) dataBank.get(i).getCol1();
		dataBank.remove(i);
		final Button addQuestioBtn = new Button("Add");
		final TextField pointsTextField = new TextField();
		addQuestioBtn.setAlignment(Pos.CENTER);
		btnList.add(addQuestioBtn);
		EventHandler<ActionEvent> addHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (checkInvalidInputsForAddQuestion(pointsTextField.getText()) == true) {
					return;
				}
				for (int j = 0; j < btnList.size(); j++) {// in order to find the correct row in the tableView
					if (btnList.get(j).equals(addQuestioBtn)) {
						indexAtBank = j;
						btnList.remove(indexAtBank);
						break;
					}
				}
				addQuestionToExam(pointsTextField.getText(), (String) questionBank.get(indexAtBank).getCol1());
				questionBank.remove(indexAtBank);
				chooseQuestionsTable.setItems(questionBank);
				TableEntry.customResize(chooseQuestionsTable);
			}

		};
		addQuestioBtn.setOnAction(addHandler);
		questionBank.add(new TableEntry(question, pointsTextField, addQuestioBtn));
		chooseQuestionsTable.setItems(questionBank);
		TableEntry.customResize(chooseQuestionsTable);
		examQuestionTable.setItems(dataBank);
		TableEntry.customResize(examQuestionTable);
	}

	public void handleOnAction(MouseEvent event) {
		if(event.getSource()==submitBtn)
		{
			submit();
		}
		else if(event.getSource()==addCommentsBtn)
		{
			addComments();
		}
		else if(event.getSource()==backToExamBtn)
		{
			backToExam();
		}
		else if(event.getSource()==createNewExamBtn)
		{
			AnchorPane page = SceneController.getPage(PageProperties.Page.CREATE_EXAM);
			App_client.pageContainer.setCenter(page);
		}
		else if(event.getSource()==goToStartExamBtn)
		{
			AnchorPane page = SceneController.getPage(PageProperties.Page.START_EXAM);
			App_client.pageContainer.setCenter(page);
		}
	}

	private void backToExam() {
		apComments.setVisible(false);
	}

	private void addComments() {
		apComments.setVisible(true);
	}

	private boolean checkInvalidInputsForAddQuestion(String pointsForQuestion) {

		if (courses.getValue() == null) {
			System.out.println("no course");
			errLabel.setText("*You must choose course");
			errLabel.setVisible(true);
			return true;
		}
		if (pointsForQuestion.equals(""))// if the user didn't insert the amount of points in the text
		{
			System.out.println("no points");
			errLabel.setText("*You must insert points for the question");
			errLabel.setVisible(true);
			return true;
		}
		if (!isNumeric(pointsForQuestion))// if the user didn't insert the numbers to points
		{
			System.out.println("invalid characters");
			errLabel.setText("*You must enter only number to points! ");
			errLabel.setVisible(true);
			return true;
		}
		double points = Double.valueOf(pointsForQuestion);

		if (totalPoints + points > 100) {
			errLabel.setText("*You exceed the 100 points for the exam");
			errLabel.setVisible(true);
			return true;
		}
		if (points > 100) {
			errLabel.setText("*You must insert less than 100 in points for the question");
			errLabel.setVisible(true);
			return true;
		}

		return false;
	}

	private void submit() {
		ArrayList<Object> parameters = new ArrayList<>();
		if (checkInvalidInputsForSumbit() == true) {
			return;
		}
		errLabel.setVisible(false);
		parameters.add(courses.getValue());
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER,
				DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		String duration = "";
		// bulding duration for the exam
		if (hourChoiceBox.getValue() == null)
			duration += "00:";
		else {
			duration += hourChoiceBox.getValue() + ":";
		}
		if (minutesChoiceBox.getValue() == null)
			duration += "00:";
		else {
			duration += minutesChoiceBox.getValue() + ":";
		}
		duration += "00";//set the seconds
		exam.setAuthorID(UserControl.ConnectedUser.getuID());
		exam.setExamID(UserControl.ConnectedUser.getfid() + ExamControl.selectedCourseID);
		exam.setDuration(duration);
		exam.setDescription(nameOfExam.getText());
		exam.setStudentsComments(studentComments.getText());
		exam.setTeacherComments(teacherComments.getText());
		parameters.clear();
		parameters.add(exam);
		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.INSERT_EXAM, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		exam.setExamID(ExamControl.examID);
		parameters.clear();
		parameters.add(exam);
		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.INSERT_EXAM_QUESTIONS, parameters,
				null, true);
		ClientControl.getInstance().accept(dataPacket);
//		errLabel.setText("exam submited");
//		errLabel.setVisible(true);
		AnchorPane page = SceneController.getPage(PageProperties.Page.MANAGE_EXAMS);
		App_client.pageContainer.setCenter(page);
	}

	private boolean checkInvalidInputsForSumbit() {
		if (exam.getSizeOfMap() == 0) {
			errLabel.setText("*Exam is empty");
			errLabel.setVisible(true);
			return true;
		}
		if (nameOfExam.getText().equals("")) {
			System.out.println("empty name of the exam");
			errLabel.setText("*You must enter name for the exam! ");
			errLabel.setVisible(true);
			return true;
		}
		if (!totalPointsLabel.getText().equals("100.0")) {
			errLabel.setText("*You must have exaclty 100 in the total points");
			errLabel.setVisible(true);
			return true;
		}
		if (hourChoiceBox.getValue() == null && minutesChoiceBox.getValue() == null) {
			errLabel.setText("*You must enter duration to the exam");
			errLabel.setVisible(true);
			return true;
		}

		if (nameOfExam.getText().equals("")) {
			errLabel.setText("*You must name for the exam");
			errLabel.setVisible(true);
			return true;
		}
		return false;
	}

	protected void setupDuration() {
		ObservableList<String> hourList = FXCollections.observableArrayList();
		ObservableList<String> minList = FXCollections.observableArrayList();

		errLabel.setVisible(false);
		for (int i = 0; i < 60; i++) {
			minList.add(Integer.toString(i));
		}
		for (int i = 0; i < 6; i++) {
			hourList.add(Integer.toString(i));
		}

		hourChoiceBox.setItems(hourList);
		minutesChoiceBox.setItems(minList);
	}

	protected boolean isNumeric(String text) {
		ParsePosition pos = new ParsePosition(0);
		NumberFormat.getInstance().parse(text, pos);
		return text.length() == pos.getIndex();
	}

}