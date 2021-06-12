/**
 * Sample Skeleton for 'ViewGrades.fxml' Controller Class
 */

package gui.student;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;

import client.App_client;
import common.DataPacket;
import control.ClientControl;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import control.ViewGradesControl;
import gui.TableEntry;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ViewGradeController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="ap"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="tableOfGrades"
	private TableView<TableEntry> tableOfGrades; // Value injected by FXMLLoader

	@FXML // fx:id="tableColCourseName"
	private TableColumn<TableEntry, ?> tableColCourseName; // Value injected by FXMLLoader

	@FXML // fx:id="tableColExamID"
	private TableColumn<TableEntry, ?> tableColExamID; // Value injected by FXMLLoader

	@FXML // fx:id="tableColGrade"
	private TableColumn<TableEntry, ?> tableColGrade; // Value injected by FXMLLoader

	@FXML // fx:id="tableColPoints"
	private TableColumn<TableEntry, ?> tableColPoints; // Value injected by FXMLLoader
	@FXML
	private Label errLabel;
	SceneController scene;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		errLabel.setVisible(false);
		ViewGradesControl.examsID.clear();
		ViewGradesControl.examsInitID.clear();
		ViewGradesControl.grades.clear();
		ViewGradesControl.coursesName.clear();
		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'ViewGrades.fxml'.";
		assert tableOfGrades != null
				: "fx:id=\"tableOfGrades\" was not injected: check your FXML file 'ViewGrades.fxml'.";
		assert tableColCourseName != null
				: "fx:id=\"tableColCourseName\" was not injected: check your FXML file 'ViewGrades.fxml'.";
		assert tableColExamID != null
				: "fx:id=\"tableColExamID\" was not injected: check your FXML file 'ViewGrades.fxml'.";
		assert tableColGrade != null
				: "fx:id=\"tableColGrade\" was not injected: check your FXML file 'ViewGrades.fxml'.";
		assert tableColPoints != null
				: "fx:id=\"tableColPoints\" was not injected: check your FXML file 'ViewGrades.fxml'.";

		scene = new SceneController(PageProperties.Page.History_Of_Exams_Student, ap);
		scene.AnimateSceen(SceneController.ANIMATE_ON.LOAD);

		tableColCourseName.setCellValueFactory(new PropertyValueFactory("col1"));
		tableColExamID.setCellValueFactory(new PropertyValueFactory("col2"));
		tableColGrade.setCellValueFactory(new PropertyValueFactory("col3"));
		tableColPoints.setCellValueFactory(new PropertyValueFactory("col4"));
		getGrades();
	}

	private void getGrades() {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(UserControl.ConnectedUser.getuID());
		ObservableList<TableEntry> data = FXCollections.observableArrayList();
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_STUDENT_GRADES,
				parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		parameters.clear();
		if(ViewGradesControl.emptyGrades==true)
		{
			ViewGradesControl.emptyGrades=false;
			tableOfGrades.setVisible(false);
			errLabel.setAlignment(Pos.TOP_LEFT);
			errLabel.setVisible(true);
			return;
		}
		for (int i = 0; i < ViewGradesControl.examsID.size(); i++) {
			String courseID = ViewGradesControl.getExamID(i).substring(2, 4);
			System.out.println(courseID);
			parameters.add(i, courseID);
		}

		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSE_NAME_BY_COURSE_ID,
				parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);

		for (int i = 0; i < ViewGradesControl.examsID.size(); i++) {
			final Button getCopyBtn = new Button("Get copy of exam");
			getCopyBtn.setAlignment(Pos.CENTER);
			EventHandler<ActionEvent> copyHandler = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					AnchorPane page = SceneController.getPage(PageProperties.Page.GET_COPY_OF_EXAM);
					App_client.pageContainer.setCenter(page);
				}
			};
			getCopyBtn.setOnAction(copyHandler);
			data.add(new TableEntry(ViewGradesControl.getCourseName(i), ViewGradesControl.getExamsInitID(i),
					ViewGradesControl.getGrade(i), getCopyBtn));
		}
		tableOfGrades.setItems(data);
		TableEntry.customResize(tableOfGrades);
	}

	

}