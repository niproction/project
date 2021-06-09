package gui;

import java.io.Serializable;
import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import common.ExamDone;
import controllers.PageProperties;
import controllers.SceneController;
import controllers.UserControl;
import controllers.examDoneControl;
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

public class verifyExamController {
	SceneController sceen;
	@FXML // fx:id="ap"
	private AnchorPane ap;
	private ArrayList<ExamDone> examDoneList;
	@FXML
	private TableView<TableEntry> tableView;
	@FXML
	public TableColumn<TableEntry, ?> colExamID;
	@FXML
	public TableColumn<TableEntry, ?> colStudentID;
	@FXML
	public TableColumn<TableEntry, ?> colStartTime;
	@FXML
	public TableColumn<TableEntry, ?> colEndTime;
	@FXML
	public TableColumn<TableEntry, ?> colGrade;
	@FXML
	public TableColumn<TableEntry, ?> colGetCopy;
	@FXML
	public TableColumn<TableEntry, ?> colStatus;
	@FXML
	public void initialize (){
		sceen = new SceneController(PageProperties.Page.VERIFY_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		//tableView.getColumns().addAll(colExamID,colStudentID,colStartTime,colEndTime,colGrade,colGetCopy,colStatus);
		
		colExamID.setCellValueFactory(new PropertyValueFactory<>("col1"));
		colStudentID.setCellValueFactory(new PropertyValueFactory<>("col2"));
		colStartTime.setCellValueFactory(new PropertyValueFactory<>("col3"));
		colEndTime.setCellValueFactory(new PropertyValueFactory<>("col4"));
		colGrade.setCellValueFactory(new PropertyValueFactory<>("col5"));
		colGetCopy.setCellValueFactory(new PropertyValueFactory<>("col6"));
		colStatus.setCellValueFactory(new PropertyValueFactory<>("col7"));
		ArrayList<Object> parameters=new ArrayList<Object>();
		parameters.add(App_client.user);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_FOR_VERIFY,
				parameters, null, true);
		App_client.chat.accept(dataPacket);
		if( examDoneControl.getExamDoneLIst()!=null) {
			System.out.println("workkkkkkkk");
			examDoneList=examDoneControl.getExamDoneLIst();
			examDoneControl.setExamDoneLIst(null);
		}
		else
			System.out.println("doesnt work");
		tableView.setItems(getRequests());
	}
	
	public ObservableList<TableEntry> getRequests() {
		ObservableList<TableEntry> requests = FXCollections.observableArrayList();
		//LocalDate date = LocalDate.of(1995, 10, 28);
			for (ExamDone entry : examDoneList) {
				// create a HBox
				final HBox status =  new HBox();
				final Button getCopy=new Button("get copy");
				//final Button getcopy=new Button();
				try {
					//getcopy.setAlignment(Pos.CENTER);
					// setAlignment
					status.setAlignment(Pos.CENTER);
					getCopy.setAlignment(Pos.CENTER);

					// create a label
					Label label = new Label("Approve?");

					// add label to hbox
					status.getChildren().add(label);

					Button Y = new Button("Y");
					Button N = new Button("N");
					
					EventHandler<ActionEvent> YHandler = new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							System.out.println("Approved" );
							label.setText("Approved");
							label.setStyle(("-fx-text-fill: green;-fx-font-weight: bold"));
							status.getChildren().remove(2);
							status.getChildren().remove(1);
							
							String edID=entry.getEdID();
							ArrayList<Object> parameter = new ArrayList<Object>();
							parameter.add(edID);
							DataPacket datapacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.APPROVED_GRADE, parameter, null, true);
							App_client.chat.accept(datapacket);
							
						}
					};
					EventHandler<ActionEvent> NHandler = new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							System.out.println("Denied" );
						label.setText("Denied");
						label.setStyle(("-fx-text-fill: red;-fx-font-weight: bold"));
						status.getChildren().remove(2);
						status.getChildren().remove(1);
						String edID=entry.getEdID();
						ArrayList<Object> parameter = new ArrayList<Object>();
						parameter.add(edID);
						DataPacket datapacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.DISAPPROVED_GRADE, parameter, null, true);
						App_client.chat.accept(datapacket);
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
				requests.add(new TableEntry(entry.getEiID(),entry.getuID() ,entry.getStartTime(),entry.getEndTime(), entry.getGrade(),getCopy,status));
				
			}
	
			
		return requests;
	}
	
}
