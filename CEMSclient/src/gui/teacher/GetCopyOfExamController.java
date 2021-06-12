package gui.teacher;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

import client.App_client;
import common.DataPacket;
import common.Student;
import common.Teacher;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import control.ClientControl;
import control.GetCopyOfExamControl;
import control.ViewGradesControl;
import gui.TableEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

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
	private Button prevPageBtn;

	@FXML
	public void initialize() {

		GetCopyOfExamControl.correctAnswersDescription.clear();
		GetCopyOfExamControl.studentAnswersDescription.clear();
		GetCopyOfExamControl.pointsForQuestion.clear();
		GetCopyOfExamControl.questionsDescription.clear();
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
			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COPY_OF_EXAM, parameters, null,
					true);
			ClientControl.getInstance().accept(dataPacket);
		}

		insertDataToTable();

	}

	private void insertDataToTable() {
		ObservableList<TableEntry> data = FXCollections.observableArrayList();
		for (int i = 0; i < GetCopyOfExamControl.correctAnswersDescription.size(); i++) {
			data.add(new TableEntry(GetCopyOfExamControl.getQuestionDescription(i),
					GetCopyOfExamControl.getStudentAnswerDescription(i),
					GetCopyOfExamControl.getCorrectAnswerDescription(i), GetCopyOfExamControl.getpointsForQuestion(i)));
		}
		double grade = 0;
		for (int i = 0; i < GetCopyOfExamControl.pointsForQuestion.size(); i++) {
			grade += Double.valueOf(GetCopyOfExamControl.getpointsForQuestion(i));
		}
		gradeLabel.setText(String.valueOf(grade));
		tableOfQuestion.setItems(data);
		TableEntry.customResize(tableOfQuestion);
	}

	public void handleOnAction(ActionEvent event) {
		if (UserControl.ConnectedUser instanceof Teacher) {
			System.out.println("teacherrrrrr");
			AnchorPane page = SceneController.getPage(PageProperties.Page.VERIFY_EXAM);
			App_client.pageContainer.setCenter(page);
		} else if (UserControl.ConnectedUser instanceof Student) {
			System.out.println("studenttttttttt");
			AnchorPane page = SceneController.getPage(PageProperties.Page.History_Of_Exams_Student);
			App_client.pageContainer.setCenter(page);
		}
	}

}