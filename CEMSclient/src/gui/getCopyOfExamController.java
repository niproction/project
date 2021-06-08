package gui;

import java.sql.Time;
import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import controllers.PageProperties;
import controllers.SceneController;
import controllers.getCopyOfExamControl;
import controllers.viewGradesControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class getCopyOfExamController {
	@FXML
	SceneController scene;
	@FXML
	private AnchorPane ap;
	@FXML
	private TableView<TableEntry> tableOfQuestion;
	@FXML
	private TableColumn<TableEntry, ?> ColQuestion = null;
	@FXML
	private TableColumn<TableEntry, ?> ColStudentAnswer = null;
	@FXML
	private TableColumn<TableEntry, ?> ColCorrectAnswer = null;
	@FXML
	private TableColumn<TableEntry, ?> ColPoints = null;
	@FXML
	private Label gradeLabel;
	@FXML
	private Label examLabel;

	@FXML
	public void initialize() {
		scene = new SceneController(PageProperties.Page.GET_COPY_OF_EXAM, ap);
		scene.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		ColQuestion.setCellValueFactory(new PropertyValueFactory("col1"));
		ColStudentAnswer.setCellValueFactory(new PropertyValueFactory("col2"));
		ColCorrectAnswer.setCellValueFactory(new PropertyValueFactory("col3"));
		ColPoints.setCellValueFactory(new PropertyValueFactory("col4"));
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(App_client.user.getuid());
		for (int i = 0; i < viewGradesControl.getSize(); i++) {
			parameters.add(1, viewGradesControl.getExamsInitID(i));
			DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COPY_OF_EXAM,
					parameters, null, true);
			App_client.chat.accept(dataPacket);
		}
		insertDataToTable();

	}

	private void insertDataToTable() {
		ObservableList<TableEntry> data = FXCollections.observableArrayList();
		for (int i = 0; i < getCopyOfExamControl.correctAnswersDescription.size(); i++) {
			data.add(new TableEntry(getCopyOfExamControl.getQuestionDescription(i),
					getCopyOfExamControl.getStudentAnswerDescription(i),
					getCopyOfExamControl.getCorrectAnswerDescription(i), getCopyOfExamControl.getpointsForQuestion(i)));
		}
		tableOfQuestion.setItems(data);
		getCopyOfExamControl.correctAnswersDescription.clear();
		getCopyOfExamControl.studentAnswersDescription.clear();
		getCopyOfExamControl.pointsForQuestion.clear();
		getCopyOfExamControl.questionsDescription.clear();
	}
}
