package gui.teacher;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import client.App_client;
import common.DataPacket;
import common.ExamDone;
import common.Question;
import control.ClientControl;
import control.ExamInitiatedControl;
import control.PageProperties;
import control.QuestionControl;
import control.SceneController;
import control.UserControl;
import control.examDoneControl;
import gui.TableEntry;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ManageQuestionsController {
	SceneController sceen;
	@FXML // fx:id="ap"
	private AnchorPane ap;
	@FXML
	private BorderPane page_box;
	private ArrayList<Question> questionslist;
	@FXML
	private TableView<TableEntry> tableView;
	@FXML
	public TableColumn<TableEntry, ?> questionIDcol;
	@FXML
	public TableColumn<TableEntry, ?> questionInfocol;
	@FXML
	public TableColumn<TableEntry, ?> option1col;
	@FXML
	public TableColumn<TableEntry, ?> option2col;
	@FXML
	public TableColumn<TableEntry, ?> option3col;
	@FXML
	public TableColumn<TableEntry, ?> option4col;
	@FXML
	public TableColumn<TableEntry, ?> answercol;
	@FXML
	public TableColumn<TableEntry, ?> editQuestioncol;
	@FXML
	private Button newQuestion;

	@FXML
	public void initialize() {
		sceen = new SceneController(PageProperties.Page.MANAGE_QUESTIONS, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		// tableView.getColumns().addAll(colExamID,colStudentID,colStartTime,colEndTime,colGrade,colGetCopy,colStatus);
		questionIDcol.setCellValueFactory(new PropertyValueFactory<>("col1"));
		questionInfocol.setCellValueFactory(new PropertyValueFactory<>("col2"));
		option1col.setCellValueFactory(new PropertyValueFactory<>("col3"));
		option2col.setCellValueFactory(new PropertyValueFactory<>("col4"));
		option3col.setCellValueFactory(new PropertyValueFactory<>("col5"));
		option4col.setCellValueFactory(new PropertyValueFactory<>("col6"));
		answercol.setCellValueFactory(new PropertyValueFactory<>("col7"));
		editQuestioncol.setCellValueFactory(new PropertyValueFactory<>("col8"));
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(UserControl.ConnectedUser);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_TEACHER_QUESTIONS,
				parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);

		if (QuestionControl.getQuestions() != null) {
			System.out.println("workkkkkkkk");
			questionslist = QuestionControl.getQuestions();
			QuestionControl.setQuestions(null);
		} else
			System.out.println("doesnt work");
		tableView.setItems(getRequests());
	}

	public ObservableList<TableEntry> getRequests() {
		ObservableList<TableEntry> requests = FXCollections.observableArrayList();
		// LocalDate date = LocalDate.of(1995, 10, 28);
		for (Question entry : questionslist) {

			// final Button getcopy=new Button();
			Button editQuestionBtn = new Button("Edit");

			// getcopy.setAlignment(Pos.CENTER);
			// setAlignment
			editQuestionBtn.setAlignment(Pos.CENTER);

			// goes to the questions of this exam
			EventHandler<ActionEvent> ttt = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("Approved");
					QuestionControl.setSpesificQuestion(entry);
					// UserControl.setCanManage(1);
					AnchorPane page = SceneController.getPage(PageProperties.Page.EDIT_QUESTION);
					App_client.pageContainer.setCenter(page);
				}
			};
			editQuestionBtn.setOnAction(ttt);

			requests.add(new TableEntry(entry.getqID(), entry.getInfo(), entry.getOption1(), entry.getOption2(),
					entry.getOption3(), entry.getOption4(), entry.getAnswer(), editQuestionBtn));

		}

		return requests;
	}

	public void handleOnAction(MouseEvent event) {
		if (event.getSource() == newQuestion) {
			AnchorPane page = SceneController.getPage(PageProperties.Page.ADD_NEW_QUESTION);
			App_client.pageContainer.setCenter(page);
		}
	}

}
