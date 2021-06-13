package gui.principal;

/**
 * Sample Skeleton for 'Untitled' Controller Class
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.DataPacket;
import common.ExtraTimeRequest;
import control.ClientControl;
import control.PrincipalControl;
import control.UserControl;
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
import javafx.scene.layout.HBox;

public class ExtraTimeRequestsController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="ap"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="ExtraTimeTable"
	private TableView<TableEntry> ExtraTimeTable; // Value injected by FXMLLoader
	@FXML // fx:id="ExtraTimeTable"
	private TableView<TableEntry> tab; // Value injected by FXMLLoader

	@FXML // fx:id="ExtraTimeTable"
	private TableView<TableEntry> tab1; // Value injected by FXMLLoader

	@FXML // fx:id="TeacherName"
	private TableColumn<TableEntry, ?> TeacherName; // Value injected by FXMLLoader

	@FXML // fx:id="Field"
	private TableColumn<TableEntry, ?> Field; // Value injected by FXMLLoader

	@FXML // fx:id="Course"
	private TableColumn<TableEntry, ?> Course; // Value injected by FXMLLoader

	@FXML // fx:id="ExamId"
	private TableColumn<TableEntry, ?> ExamId; // Value injected by FXMLLoader

	// @FXML // fx:id="RequestTime"
	// private TableColumn<TableEntry, ?> RequestTime; // Value injected by
	// FXMLLoader

	@FXML // fx:id="ExtraTime"
	private TableColumn<TableEntry, ?> ExtraTime; // Value injected by FXMLLoader

	@FXML // fx:id="ExtraTime"
	private TableColumn<TableEntry, ?> Comment; // Value injected by FXMLLoader

	@FXML // fx:id="Status"
	private TableColumn<TableEntry, ?> Status; // Value injected by FXMLLoader

	@FXML // This method is called by the FXMLLoader when initialization is complete

	void initialize() {

		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'Untitled'.";
		assert ExtraTimeTable != null : "fx:id=\"ExtraTimeTable\" was not injected: check your FXML file 'Untitled'.";
		assert TeacherName != null : "fx:id=\"TeacherName\" was not injected: check your FXML file 'Untitled'.";
		assert Field != null : "fx:id=\"Field\" was not injected: check your FXML file 'Untitled'.";
		assert Course != null : "fx:id=\"Course\" was not injected: check your FXML file 'Untitled'.";
		assert ExamId != null : "fx:id=\"ExamId\" was not injected: check your FXML file 'Untitled'.";
		// assert RequestTime != null : "fx:id=\"RequestTime\" was not injected: check
		// your FXML file 'Untitled'.";
		assert ExtraTime != null : "fx:id=\"ExtraTime\" was not injected: check your FXML file 'Untitled'.";
		assert Comment != null : "fx:id=\"ExtraTime\" was not injected: check your FXML file 'Untitled'.";
		assert Status != null : "fx:id=\"Status\" was not injected: check your FXML file 'Untitled'.";

		DataPacket data = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_EXTRA_TIME_REQUESTS, null,
				null, true);
		ClientControl.getInstance().accept(data);

		TeacherName.setCellValueFactory(new PropertyValueFactory("col1"));
		Field.setCellValueFactory(new PropertyValueFactory("col2"));
		Course.setCellValueFactory(new PropertyValueFactory("col3"));
		ExamId.setCellValueFactory(new PropertyValueFactory("col4"));

		ExtraTime.setCellValueFactory(new PropertyValueFactory("col5"));
		Comment.setCellValueFactory(new PropertyValueFactory("col6"));
		Status.setCellValueFactory(new PropertyValueFactory("col7"));

		ExtraTimeTable.setItems(getRequests());
		// ExtraTimeTable.getSortOrder().add(RequestTime);
	}

	ExtraTimeRequest entry = null;

	public ObservableList<TableEntry> getRequests() {
		ObservableList<TableEntry> requests = FXCollections.observableArrayList();

		for (int i = 0; i < PrincipalControl.requests.size(); i++) {
			entry = PrincipalControl.requests.get(i);
			// for (ExtraTimeRequest entry : PrincipalControl.requests) {
			System.out.println(entry.getEiID());
			// create a HBox
			final HBox status = new HBox();
			;
			try {

				// setAlignment
				status.setAlignment(Pos.CENTER);

				// create a label
				Label label = new Label("Approve?");

				// add label to hbox
				status.getChildren().add(label);

				Button Y = new Button("Y");
				Button N = new Button("N");

				EventHandler<ActionEvent> YHandler = new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Approved");
						label.setText("Approved");
						label.setStyle(("-fx-text-fill: green;-fx-font-weight: bold"));
						status.getChildren().remove(2);
						status.getChildren().remove(1);

						ExtraTimeRequest respond = new ExtraTimeRequest(0, entry.getEiID(), null, entry.getExtraTime(),
								"yes", null, null, null);
						ArrayList<Object> parameter = new ArrayList<Object>();
						parameter.add(respond);
						DataPacket datapacket = new DataPacket(DataPacket.SendTo.SERVER,
								DataPacket.Request.EXTRA_TIME_DECISION, parameter, null, true);
						ClientControl.getInstance().accept(datapacket);

					}
				};
				EventHandler<ActionEvent> NHandler = new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Denied");
						label.setText("Denied");
						label.setStyle(("-fx-text-fill: red;-fx-font-weight: bold"));
						status.getChildren().remove(2);
						status.getChildren().remove(1);
						ExtraTimeRequest respond = new ExtraTimeRequest(0, entry.getEiID(), null, entry.getExtraTime(),
								"no", null, null, null);
						ArrayList<Object> parameter = new ArrayList<Object>();
						parameter.add(respond);
						DataPacket datapacket = new DataPacket(DataPacket.SendTo.SERVER,
								DataPacket.Request.EXTRA_TIME_DECISION, parameter, null, true);
						ClientControl.getInstance().accept(datapacket);
					}
				};
				Y.setOnAction(YHandler);
				N.setOnAction(NHandler);

				// add buttons to HBox
				status.getChildren().add(Y);
				status.getChildren().add(N);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			requests.add(
					new TableEntry(UserControl.user.get(i).getFirstName() + " " + UserControl.user.get(i).getLastName(),
							entry.getFieldName(), entry.getCourseName(), entry.geteID(), entry.getExtraTime(),
							entry.getComment(), status));

		}

		return requests;
	}
}