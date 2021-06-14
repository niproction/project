package gui.teacher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import javax.swing.colorchooser.ColorChooserComponentFactory;

import com.jfoenix.controls.JFXComboBox;

import client.App_client;
import common.DataPacket;
import common.Exam;
import control.PageProperties;
import control.PageProperties.Page;
import control.SceneController;
import control.UserControl;
import control.ClientControl;
import control.ExamControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import common.MyFile;

public class CreateNewWordExamController extends createNewExamController {
	private File examFile;
	SceneController sceen;
	@FXML // fx:id="ap"
	private AnchorPane ap;
	@FXML
	private Label errLabel;
	@FXML
	private Label fieldNameLabel;
	@FXML
	private JFXComboBox<String> courses;
	@FXML
	private Button submitBtn;
	@FXML
	private Button uploadExamBtn;
	@FXML
	private TextField nameOfExam;
	@FXML
	private ScrollBar scrollbar;
	@FXML
	private JFXComboBox<String> hourChoiceBox;
	@FXML
	private JFXComboBox<String> minutesChoiceBox;
	@FXML
	private JFXComboBox<String> secondsChoiceBox;
	private Exam exam;
	@FXML
	public void initialize() {
		sceen = new SceneController(PageProperties.Page.Create_MANUEL_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		super.setupDuration();
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(UserControl.ConnectedUser);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_FIELD_NAME, parameters,
				null, true);
		ClientControl.getInstance().accept(dataPacket);
		if (App_client.fieldName != null) {
			fieldNameLabel.setText(App_client.fieldName);
			App_client.fieldName = null;
		}
		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSES, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		ObservableList<String> courseList = FXCollections.observableArrayList();
		courseList.addAll(ExamControl.coursesNames);
		courses.setItems(courseList);
	}

	public void handleOnAction(MouseEvent event) {
		if (event.getSource() == uploadExamBtn) {
			uploadExam();
		} else if (event.getSource() == submitBtn) {
			sumbit();
		}

	}

	private void sumbit() {
		ArrayList<Object> parameters = new ArrayList<>();
		if (checkInvalidInputsForSumbit() == true) {
			return;
		}
		errLabel.setVisible(false);
		parameters.add(courses.getValue());
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER,
				DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		String duration="";
		//bulding duration for the exam
		if(hourChoiceBox.getValue()==null)
			duration+="00:";
		else {
			duration+=hourChoiceBox.getValue()+":";
		}
		if(minutesChoiceBox.getValue()==null)
			duration+="00:";
		else {
			duration+=minutesChoiceBox.getValue()+":";
		}
		if(secondsChoiceBox.getValue()==null)
			duration+="00";
		else {
			duration+=secondsChoiceBox.getValue();
		}
		exam=new Exam(UserControl.ConnectedUser.getfid() + ExamControl.selectedCourseID, UserControl.ConnectedUser.getuID(), nameOfExam.getText(), duration);
		parameters.clear();
		parameters.add(exam);
		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.INSERT_EXAM, parameters, null, true);
		ClientControl.getInstance().accept(dataPacket);
		exam.setExamID(ExamControl.examID);
		MyFile msg = new MyFile(examFile.getName());
		String LocalfilePath = examFile.getPath();
		try {

			File newFile = new File(LocalfilePath);
			byte[] mybytearray = new byte[(int) newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			msg.initArray(mybytearray.length);
			msg.setSize(mybytearray.length);

			bis.read(msg.getMybytearray(), 0, mybytearray.length);
			parameters.add(0, msg);
			parameters.add(1, exam.getExamID() + ".docx");
			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.INSERT_Manuel_EXAM_FILE,
					parameters, null, true);
			ClientControl.getInstance().accept(dataPacket);
		} catch (Exception e) {
			System.out.println("Error send (Files)msg) to Server");
		}
		errLabel.setText(examFile.getName() + " submited");
		errLabel.setVisible(true);
	}

	private boolean checkInvalidInputsForSumbit() {
		if(courses.getValue()==null)
		{
			System.out.println("no course");
			errLabel.setText("*You must choose course");
			errLabel.setVisible(true);
			return true;
		}
		if (hourChoiceBox.getValue()==null && minutesChoiceBox.getValue()==null  && secondsChoiceBox.getValue()==null) {
			errLabel.setText("*You must enter duration to the exam");
			errLabel.setVisible(true);
			return true;
		}
		if(nameOfExam.getText().equals(""))
		{
			System.out.println("empty name of the exam");
			errLabel.setText("*You must enter name for the exam! ");
			errLabel.setVisible(true);
			return true;
		}
		if(examFile==null)
		{
			System.out.println("empty exam");
			errLabel.setText("*You must upload exam! ");
			errLabel.setVisible(true);
			return true;
		}
		return false;
	}

	private void uploadExam() {
		Window window = null;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("choose exam");
		examFile = fileChooser.showOpenDialog(window);
		errLabel.setText(examFile.getName() + " uploaded");
		errLabel.setVisible(true);
	}
}