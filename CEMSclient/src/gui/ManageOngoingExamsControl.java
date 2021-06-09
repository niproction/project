package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import common.ExtraTimeRequest;
import common.User;
import controllers.ClientController;
import controllers.ClientDataPacketHandler;
import controllers.ManageOngoingExams;
import controllers.UserControl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ManageOngoingExamsControl {
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="ap"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="pic4"
	private ImageView pic4; // Value injected by FXMLLoader

	@FXML // fx:id="text2"
	private Text text2; // Value injected by FXMLLoader

	@FXML // fx:id="pic2"
	private ImageView pic2; // Value injected by FXMLLoader

	@FXML // fx:id="pic3"
	private ImageView pic3; // Value injected by FXMLLoader

	@FXML // fx:id="text3"
	private Text text3; // Value injected by FXMLLoader

	@FXML // fx:id="extra_time_request"
	private TextField extra_time_request; // Value injected by FXMLLoader

	@FXML // fx:id="terminate_exam"
	private Button terminate_exam; // Value injected by FXMLLoader

	@FXML // fx:id="pic1"
	private ImageView pic1; // Value injected by FXMLLoader

	@FXML // fx:id="text1"
	private Text text1; // Value injected by FXMLLoader

	@FXML // fx:id="examField"
	private TextField examField; // Value injected by FXMLLoader

	@FXML // fx:id="sendBtn"
	private Button sendBtn; // Value injected by FXMLLoader

	@FXML // fx:id="text4"
	private Text text4; // Value injected by FXMLLoader

	@FXML // fx:id="commentField"
	private TextField commentField; // Value injected by FXMLLoader

	@FXML // fx:id="status"
	private Label status; // Value injected by FXMLLoader

	/**
	 * Initialize the page.
	 */
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert pic4 != null : "fx:id=\"pic4\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert text2 != null : "fx:id=\"text2\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert pic2 != null : "fx:id=\"pic2\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert pic3 != null : "fx:id=\"pic3\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert text3 != null : "fx:id=\"text3\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert extra_time_request != null: "fx:id=\"extra_time_request\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert terminate_exam != null: "fx:id=\"terminate_exam\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert pic1 != null : "fx:id=\"pic1\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert text1 != null : "fx:id=\"text1\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert examField != null: "fx:id=\"examField\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert sendBtn != null : "fx:id=\"sendBtn\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert text4 != null : "fx:id=\"text4\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert commentField != null: "fx:id=\"commentField\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		
		//Set text for terminate button, set autosize and align to center status(if exams exist for the teacher) label 
		terminate_exam.setText("Terminate exam");
		status.autosize();
		status.setAlignment(Pos.CENTER);

		//Send request to server to get ongoing exam for the teacher if there is one
		DataPacket data = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_ONGOING_EXAM, null,App_client.user.getuid(), true);
		App_client.chat.accept(data);

		//If there is an ongoing exam for the teacher
		if (ManageOngoingExams.isOngoingExams == true) {
			String ongoingExam = "Exam initiated:";
			
			//Append strings of ongoing exam info
			for (String str : ManageOngoingExams.OngoingExam) {
				ongoingExam = ongoingExam + str + " ";
			}

			//Set text and style for exam info field
			examField.setText(ongoingExam);
			examField.setStyle(("-fx-font-weight: bold;-fx-font-size: 9"));
			
			//Event handler for exam termination
			EventHandler<ActionEvent> terminateHandler = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					//Set text and style for exam termination button
					terminate_exam.setText("EXAM TERMINATED");
					terminate_exam.setStyle(("-fx-text-fill: red;-fx-font-weight: bold"));
					
					//Send request to server to get ongoing exam for the teacher if there is one
					DataPacket data = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.TERMINATE_EXAM, null,App_client.user.getuid(), true);
					App_client.chat.accept(data);
				}
			};
			
			//Set action handler
			terminate_exam.setOnAction(terminateHandler);
		}

		//If there is no ongoing exam for the teacher
		else if (ManageOngoingExams.isOngoingExams == false) {
			
			//Delete unnecessary page elements
			ap.getChildren().removeAll(terminate_exam, examField, extra_time_request, text1, text2, text3, text4, pic1,
					pic2, pic3, pic4, sendBtn, commentField);
			
			//Show status label(No exams found) and set it's style
			status.setVisible(true);
			status.setStyle(("-fx-font-weight: bold;-fx-font-size: 26;-fx-text-fill: red"));

		}
	}

}
