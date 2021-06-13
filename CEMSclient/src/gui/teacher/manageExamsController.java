package gui.teacher;

import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import common.Exam;
import control.ClientControl;
import control.ExamControl;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import gui.TableEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class manageExamsController {
	SceneController sceen;
	@FXML // fx:id="ap"
	private AnchorPane ap;
	@FXML
	private BorderPane page_box;
	private ArrayList<Exam> examList;
	@FXML
	private TableView<TableEntry> tableView;
	@FXML
	public TableColumn<TableEntry, ?> examIDcol;
	@FXML
	public TableColumn<TableEntry, ?> examDescriptioncol;
	@FXML
	public TableColumn<TableEntry, ?> editExamcol;
	public Button createNewExam;
	@FXML
	public void initialize() {
		sceen = new SceneController(PageProperties.Page.MANAGE_EXAMS, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		// tableView.getColumns().addAll(colExamID,colStudentID,colStartTime,colEndTime,colGrade,colGetCopy,colStatus);
		examIDcol.setCellValueFactory(new PropertyValueFactory<>("col1"));
		examDescriptioncol.setCellValueFactory(new PropertyValueFactory<>("col2"));
		editExamcol.setCellValueFactory(new PropertyValueFactory<>("col3"));
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(UserControl.ConnectedUser);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.MANAGE_EXAMS,
				parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);

		if (ExamControl.getExamsList() != null) {
			System.out.println("workkkkkkkk");
			examList = ExamControl.getExamsList();
			ExamControl.setExamsList(null);
		} else
			System.out.println("doesnt work");
		tableView.setItems(getRequests());
	}
	public ObservableList<TableEntry> getRequests() {
		ObservableList<TableEntry> requests = FXCollections.observableArrayList();
		// LocalDate date = LocalDate.of(1995, 10, 28);
		for (Exam entry : examList) {

			// final Button getcopy=new Button();
			Button editExamBtn = new Button("Edit");

			// getcopy.setAlignment(Pos.CENTER);
			// setAlignment
			editExamBtn.setAlignment(Pos.CENTER);

			// goes to the questions of this exam
			EventHandler<ActionEvent> ttt = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("Approved");
					ExamControl.setExamID(entry.getExamID());
					ExamControl.editExamFlag=true;
					// UserControl.setCanManage(1);
					AnchorPane page = SceneController.getPage(PageProperties.Page.EDIT_EXAMS);
					App_client.pageContainer.setCenter(page);
				}
			};
			editExamBtn.setOnAction(ttt);

			requests.add(new TableEntry(entry.getExamID(), entry.getDescription(), editExamBtn));

		}

		return requests;
	}

	public void handleOnAction(MouseEvent event) {
		if (event.getSource() == createNewExam) {
			AnchorPane page = SceneController.getPage(PageProperties.Page.CHOSSE_EXAM_TYPE);
			App_client.pageContainer.setCenter(page);
		}
	}

}