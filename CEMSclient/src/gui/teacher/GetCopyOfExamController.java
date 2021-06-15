package gui.teacher;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

import client.App_client;
import common.DataPacket;
import common.Student;
import common.Teacher;
import control.SceneController;
import control.UserControl;
import control.ClientControl;
import control.GetCopyOfExamControl;
import control.PageProperties;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	private Button showCommentsBtn;
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
	private TextArea teacherComments;
	@FXML
	private TextArea studentComments;
	@FXML
	private AnchorPane apComments;
	@FXML
	private Button prevPageBtn;
	@FXML
	private Button backToCopyBtn;
	@FXML
	private Label teacherCommLabel;

	@FXML
	public void initialize() {

		scene = new SceneController(PageProperties.Page.GET_COPY_OF_EXAM, ap);
		scene.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		ColQuestion.setCellValueFactory(new PropertyValueFactory("col1"));
		ColStudentAnswer.setCellValueFactory(new PropertyValueFactory("col2"));
		ColCorrectAnswer.setCellValueFactory(new PropertyValueFactory("col3"));
		ColPoints.setCellValueFactory(new PropertyValueFactory("col4"));
		GetCopyOfExamControl.correctAnswersDescription.clear();
		GetCopyOfExamControl.studentAnswersDescription.clear();
		GetCopyOfExamControl.pointsForQuestion.clear();
		GetCopyOfExamControl.questionsDescription.clear();
		ArrayList<Object> parameters = new ArrayList<>();
		DataPacket dataPacket;
		if(UserControl.ConnectedUser instanceof Student)
		{
			parameters.add(UserControl.ConnectedUser.getuID());
			for (int i = 0; i < ViewGradesControl.getSize(); i++) {
				parameters.add(1, ViewGradesControl.getExamsInitID(i));
				dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COPY_OF_EXAM, parameters, null,
						true);
				ClientControl.getInstance().accept(dataPacket);
			}
		}
		else if(UserControl.ConnectedUser instanceof Teacher)
		{
			System.out.println("before serverrrr");
			parameters.add(GetCopyOfExamControl.studentID);
			parameters.add(GetCopyOfExamControl.eID);
			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COPY_OF_EXAM, parameters, null,
					true);
			ClientControl.getInstance().accept(dataPacket);
			System.out.println("afterr serverrr");
		}
//		for (int i = 0; i < ViewGradesControl.getSize(); i++) {
//			parameters.add(1, ViewGradesControl.getExamsInitID(i));
//			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COPY_OF_EXAM, parameters, null,
//					true);
//			ClientControl.getInstance().accept(dataPacket);
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
		double grade = 0;
		for (int i = 0; i < GetCopyOfExamControl.pointsForQuestion.size(); i++) {
			grade += Double.valueOf(GetCopyOfExamControl.getpointsForQuestion(i));
		}
		gradeLabel.setText(String.valueOf(grade));
		tableOfQuestion.setItems(data);
		TableEntry.customResize(tableOfQuestion);
	}

	public void handleOnAction(MouseEvent event) {
		if (event.getSource() == prevPageBtn) {
			if (UserControl.ConnectedUser instanceof Teacher) {
				AnchorPane page = SceneController.getPage(PageProperties.Page.VERIFY_EXAM);
				App_client.pageContainer.setCenter(page);
			} else if (UserControl.ConnectedUser instanceof Student) {
				AnchorPane page = SceneController.getPage(PageProperties.Page.History_Of_Exams_Student);
				App_client.pageContainer.setCenter(page);
			}
		} else if (event.getSource() == showCommentsBtn) {
			System.out.println("commmmentssssss");
			ArrayList<Object> parameters = new ArrayList<>();
			parameters.add(GetCopyOfExamControl.eID);
			DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.Get_Comments,
					parameters, null, true);
			ClientControl.getInstance().accept(dataPacket);
			apComments.setVisible(true);
			GetCopyOfExamControl.eID = -1;
			if (UserControl.ConnectedUser instanceof Student) {
				teacherComments.setVisible(false);
				teacherCommLabel.setVisible(false);
				studentComments.setText(GetCopyOfExamControl.studentComm);
				GetCopyOfExamControl.studentComm = null;
			} 
			else if (UserControl.ConnectedUser instanceof Teacher) {
				teacherComments.setText(GetCopyOfExamControl.teacherComm);
				studentComments.setText(GetCopyOfExamControl.studentComm);
				GetCopyOfExamControl.teacherComm = null;
				GetCopyOfExamControl.studentComm = null;
			}
		} else if (event.getSource() == backToCopyBtn) {
			apComments.setVisible(false);
		}
	}

}