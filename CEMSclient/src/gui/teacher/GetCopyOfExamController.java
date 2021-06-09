package gui.teacher;

import java.sql.Time;
import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import control.ClientControl;
import control.GetCopyOfExamControl;
import control.ViewGradesControl;
import gui.TableEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class GetCopyOfExamController {
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
	private Button prevPageBtn;

	@FXML
	public void initialize() {
		scene = new SceneController(PageProperties.Page.GET_COPY_OF_EXAM, ap);
		scene.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		ColQuestion.setCellValueFactory(new PropertyValueFactory("col1"));
		ColStudentAnswer.setCellValueFactory(new PropertyValueFactory("col2"));
		ColCorrectAnswer.setCellValueFactory(new PropertyValueFactory("col3"));
		ColPoints.setCellValueFactory(new PropertyValueFactory("col4"));
		ArrayList<Object> parameters = new ArrayList<>();
		DataPacket dataPacket;
		parameters.add(UserControl.ConnectedUser.getuID());
		for (int i = 0; i < ViewGradesControl.getSize(); i++) {
			parameters.add(1, ViewGradesControl.getExamsInitID(i));
			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COPY_OF_EXAM,
					parameters, null, true);
			ClientControl.getInstance().accept(dataPacket);
		}
//		if(dataPacket==null)
//		{
//			
//		}
		insertDataToTable();

	}

	private void insertDataToTable() {
		ObservableList<TableEntry> data = FXCollections.observableArrayList();
		for (int i = 0; i < GetCopyOfExamControl.correctAnswersDescription.size(); i++) {
			data.add(new TableEntry(GetCopyOfExamControl.getQuestionDescription(i),
					GetCopyOfExamControl.getStudentAnswerDescription(i),
					GetCopyOfExamControl.getCorrectAnswerDescription(i), GetCopyOfExamControl.getpointsForQuestion(i)));
		}
		tableOfQuestion.setItems(data);
		GetCopyOfExamControl.correctAnswersDescription.clear();
		GetCopyOfExamControl.studentAnswersDescription.clear();
		GetCopyOfExamControl.pointsForQuestion.clear();
		GetCopyOfExamControl.questionsDescription.clear();
	}
	
	public void handleOnAction(MouseEvent event) {
		if(event.getSource()==prevPageBtn)
		{
			
		}
	}
}