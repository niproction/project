package gui.teacher;

/**
 * Sample Skeleton for 'startExam.fxml' Controller Class
 */
/**
 * Sample Skeleton for 'startExam.fxml' Controller Class
 */

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import common.Exam;
import control.ExamControl;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class StartExamController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="ac"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="choicebox_exams"
	private ChoiceBox<Exam> choicebox_exams; // Value injected by FXMLLoader

	@FXML // fx:id="button_start"
	private Button button_start; // Value injected by FXMLLoader

	@FXML // fx:id="button_start"
	private Label label_message; // Value injected by FXMLLoader

	SceneController sceen;

	
	
	ObservableList<Exam> examsList = FXCollections.observableArrayList();
	
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert ap != null : "fx:id=\"ac\" was not injected: check your FXML file 'startExam.fxml'.";
		assert choicebox_exams != null
				: "fx:id=\"choicebox_exams\" was not injected: check your FXML file 'startExam.fxml'.";
		assert button_start != null : "fx:id=\"button_start\" was not injected: check your FXML file 'startExam.fxml'.";

		sceen = new SceneController(PageProperties.Page.START_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);

		label_message.setVisible(true);
		label_message.setText("");

		// send datapacket to recive the exams
		ArrayList<Object> parameter = new ArrayList<>();
		parameter.add(UserControl.ConnectedUser);
		
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_EXAMS_BY_TEACHER,
				parameter, null, true);

		App_client.chat.accept(dataPacket);

		
		
		examsList.addAll(ExamControl.exams);
		
		System.out.print(examsList.get(0).toString());

		// Set the list of Course items to the ChoiceBox
		choicebox_exams.setItems(examsList);
		choicebox_exams.setValue(examsList.get(0));
	}

	
	
	@FXML
	void button_exam_clicked(MouseEvent event) {
		String examID;
		examID = choicebox_exams.getValue().getExamID();
		
		
		//choicebox_exams.getValue();
		ArrayList<Object> parameter = new ArrayList<>();
		
		
		
		java.util.Date dt = new java.util.Date();

		SimpleDateFormat dateandtimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = dateandtimeFormat.format(dt);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		String Time = timeFormat.format(dt);
		
		parameter.add(choicebox_exams.getValue());
		parameter.add(UserControl.ConnectedUser);
		parameter.add(Time);
		parameter.add("1234");
		parameter.add(currentTime);
		parameter.add("started");
		
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.START_EXAM, parameter, null,
				true);

		App_client.chat.accept(dataPacket);

		
		
		for (int i = 0; i < examsList.size(); i++) {
			if( examsList.get(i).equals(choicebox_exams.getValue()) )
				examsList.remove(i);
		}
		
		//choicebox_exams.getValue();
		
		choicebox_exams.setItems(examsList);
		choicebox_exams.setValue(examsList.get(0));
		// Create the ChoiceBox
		// ChoiceBox<Exam> cbCourses = new ChoiceBox<>();

		// Sample list of Courses
		/*
		 * StringProperty courseName = new SimpleStringProperty();
		 * 
		 * public Course(String courseName) { this.courseName.set(courseName); }
		 */

		// Now, let's add sample data to our list
		// for (int i = 0; i < examControl.exams.size(); i++) {

		// }

		label_message.setText("Exam started");
		button_start.setDisable(true);
	}
}