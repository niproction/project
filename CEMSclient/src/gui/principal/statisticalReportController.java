package gui.principal;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.App_client;
import common.Course;
import common.DataPacket;
import common.User;
import control.CourseControl;
import control.HistogramControl;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

public class statisticalReportController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="teacherButton"
	private Button teacherButton; // Value injected by FXMLLoader

	@FXML // fx:id="teacherChoiceBox"
	private ChoiceBox<String> teacherChoiceBox; // Value injected by FXMLLoader

	@FXML // fx:id="studentButton"
	private Button studentButton; // Value injected by FXMLLoader

	@FXML // fx:id="studentChoiceBox"
	private ChoiceBox<String> studentChoiceBox; // Value injected by FXMLLoader

	@FXML // fx:id="courseButton"
	private Button courseButton; // Value injected by FXMLLoader

	@FXML // fx:id="courseChoiceBox"
	private ChoiceBox<String> courseChoiceBox; // Value injected by FXMLLoader

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert teacherButton != null
				: "fx:id=\"teacherButton\" was not injected: check your FXML file 'statisticalReport.fxml'.";
		assert teacherChoiceBox != null
				: "fx:id=\"teacherChoiceBox\" was not injected: check your FXML file 'statisticalReport.fxml'.";
		assert studentButton != null
				: "fx:id=\"studentButton\" was not injected: check your FXML file 'statisticalReport.fxml'.";
		assert studentChoiceBox != null
				: "fx:id=\"studentChoiceBox\" was not injected: check your FXML file 'statisticalReport.fxml'.";
		assert courseButton != null
				: "fx:id=\"courseButton\" was not injected: check your FXML file 'statisticalReport.fxml'.";
		assert courseChoiceBox != null
				: "fx:id=\"courseChoiceBox\" was not injected: check your FXML file 'statisticalReport.fxml'.";
		
		teacherChoiceBox.getItems().removeAll(teacherChoiceBox.getItems());
		studentChoiceBox.getItems().removeAll(studentChoiceBox.getItems());
		courseChoiceBox.getItems().removeAll(courseChoiceBox.getItems());
		int i;
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_ALL_TEACHERS, null,
				null, true);
		App_client.chat.accept(dataPacket);// send and wait for response from server
		// recived teachers
		HistogramControl.examsOfuser = new ArrayList<User>();
		HistogramControl.examsOfCourse = new ArrayList<Course>();
		teacherChoiceBox.getItems().add("select");
		i=1;
		for (User tmp : UserControl.teachers) {
			teacherChoiceBox.getItems().add(i+". "+tmp.toString());
			i++;
		}
		teacherChoiceBox.getSelectionModel().select("select");

	
		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_ALL_STUDENTS, null, null, true);
		App_client.chat.accept(dataPacket);// send and wait for response from server
		// recived students
			
		i=1;

		studentChoiceBox.getItems().add("select");
		for (User tmp : UserControl.students) {
			studentChoiceBox.getItems().add(i+". "+tmp.toString());
		i++;
		}
		studentChoiceBox.getSelectionModel().select("select");

		
		
		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_ALL_COURSES, null, null, true);
		App_client.chat.accept(dataPacket);// send and wait for response from server
		// recived course

		i=1;

		courseChoiceBox.getItems().add("select");
		for (Course tmp : CourseControl.courses) {
			courseChoiceBox.getItems().add(i+". "+tmp.getCourseName());
			i++;
		}
		courseChoiceBox.getSelectionModel().select("select");

	}

	@FXML
	void TeacherPicked(ActionEvent event) {
		String tmp=teacherChoiceBox.getValue();
		String tmp1[]=tmp.split(". ");
		int indexOfuser=Integer.parseInt(tmp1[0])-1;
		HistogramControl.examsOfuser.add(UserControl.teachers.get(indexOfuser));
		AnchorPane page = SceneController.getPage(PageProperties.Page.HISTOGRAM);
		App_client.pageContainer.setCenter(page);
	}

	@FXML
	void StudentPicked(ActionEvent event) {
		String tmp=studentChoiceBox.getValue();
		String tmp1[]=tmp.split(". ");

		int indexOfuser=Integer.parseInt(tmp1[0])-1;
		HistogramControl.examsOfuser.add(UserControl.students.get(indexOfuser));
		AnchorPane page = SceneController.getPage(PageProperties.Page.HISTOGRAM);
		App_client.pageContainer.setCenter(page); 
	}

	@FXML
	void CoursePicked(ActionEvent event) {
		String tmp=courseChoiceBox.getValue();
		String tmp1[]=tmp.split(". "); 

		int indexOfCourse=Integer.parseInt(tmp1[0])-1;		
		System.out.println("pressed on COurseseses"+indexOfCourse);
		HistogramControl.examsOfCourse.add(CourseControl.courses.get(indexOfCourse));
		AnchorPane page = SceneController.getPage(PageProperties.Page.HISTOGRAM);
		App_client.pageContainer.setCenter(page);

		

	}

}
