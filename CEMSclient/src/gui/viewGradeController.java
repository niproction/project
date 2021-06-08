package gui;

import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import common.DataPacket.SendTo;
import controllers.PageProperties;
import controllers.SceneController;
import controllers.viewGradesControl;
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
import javafx.scene.layout.AnchorPane;

public class viewGradeController {
	@FXML
	SceneController scene;
	@FXML
	private AnchorPane ap;
	@FXML
	private TableView<TableEntry> tableOfGrades;
	@FXML
	TableColumn<TableEntry, ?> tableColCourseName = null;
	@FXML
	TableColumn<TableEntry, ?> tableColExamID = null;
	@FXML
	TableColumn<TableEntry, ?> tableColGrade = null;
	@FXML
	TableColumn<TableEntry, ?> tableColPoints = null;

	@FXML
	void initialize() {
		scene = new SceneController(PageProperties.Page.History_Of_Exams_Student, ap);
		scene.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		tableColCourseName.setCellValueFactory(new PropertyValueFactory("col1"));
		tableColExamID.setCellValueFactory(new PropertyValueFactory("col2"));
		tableColGrade.setCellValueFactory(new PropertyValueFactory("col3"));
		tableColPoints.setCellValueFactory(new PropertyValueFactory("col4"));
		getGrades();
	}

	private void getGrades() {
		ArrayList<Object> parameters=new ArrayList<>();
		parameters.add(App_client.user.getuid());
		ObservableList<TableEntry> data = FXCollections.observableArrayList();
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_STUDENT_GRADES, parameters, null, true);
		App_client.chat.accept(dataPacket);
		for (int i = 0; i < viewGradesControl.examsID.size(); i++) {
			String courseID=viewGradesControl.getExamID(i).substring(2, 4);
			System.out.println(courseID);
			parameters.add(i,courseID);
		}
		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSE_NAME_BY_COURSE_ID, parameters, null, true);
		App_client.chat.accept(dataPacket);
		for (int i = 0; i < viewGradesControl.examsID.size(); i++) {
			final Button getCopyBtn=new Button("Get copy of exam");
			getCopyBtn.setAlignment(Pos.CENTER);
			EventHandler<ActionEvent> copyHandler=new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("^^^^ "+viewGradesControl.getExamsInitID(0));
					AnchorPane page = SceneController.getPage(PageProperties.Page.GET_COPY_OF_EXAM);
					App_client.pageContainer.setCenter(page);
				}
			};
			getCopyBtn.setOnAction(copyHandler);
			data.add(new TableEntry(viewGradesControl.getCourseName(i), viewGradesControl.getExamsInitID(i), viewGradesControl.getGrade(i),getCopyBtn));
		}
		tableOfGrades.setItems(data);
	}
}
