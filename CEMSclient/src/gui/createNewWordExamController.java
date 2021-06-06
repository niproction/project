package gui;

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
import client.App_client;
import common.DataPacket;
import controllers.PageProperties;
import controllers.PageProperties.Page;
import controllers.SceneController;
import controllers.examControl;
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
public class createNewWordExamController extends createNewExamController {
	private File examFile;
	SceneController sceen;
	@FXML // fx:id="ap"
    private AnchorPane ap;
	@FXML
	private Label errLabel;
	@FXML
	private Label fieldNameLabel;
	@FXML
	private ChoiceBox<String> courses;
	@FXML
	private Button submitBtn;
	@FXML
	private Button uploadExamBtn;
	@FXML
	private TextField duration;
	@FXML
	private ScrollBar scrollbar;
	@FXML
	public void initialize() 
	{
		sceen=new SceneController(PageProperties.Page.Create_MANUEL_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		errLabel.setVisible(false);
		ArrayList<Object> parameters=new ArrayList<>();
		parameters.add(App_client.user);
		DataPacket dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_FIELD_NAME, parameters, null, true);
		App_client.chat.accept(dataPacket);
		if(App_client.fieldName!=null) {
			fieldNameLabel.setText(App_client.fieldName);
			App_client.fieldName=null;
		}
		dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSES, parameters, null, true);
		App_client.chat.accept(dataPacket);
		ObservableList<String> courseList=FXCollections.observableArrayList();
		courseList.addAll(examControl.coursesNames);
		courses.setItems(courseList);
	}
	
	
	public void handleOnAction(MouseEvent event) {
		 if(event.getSource()==uploadExamBtn) {
			 uploadExam();
		}
		 else if(event.getSource()==submitBtn)
		 {
			 sumbit();
		 }
		
	}


	private void sumbit() {
		ArrayList<Object> parameters=new ArrayList<>();
		if(checkInvalidInputsForSumbit()==true)
		{
			return;
		}
		errLabel.setVisible(false);
		parameters.add(courses.getValue());
		DataPacket dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSE_ID_BY_COURSE_NAME, parameters, null, true);
		App_client.chat.accept(dataPacket);
		exam.setAuthor(App_client.user.getuid());
		exam.setExamID(App_client.user.getfid()+examControl.selectedCourseID);
		exam.setDuration(duration.getText());
		parameters.add(0, exam);
		dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.INSERT_EXAM, parameters, null, true);
		App_client.chat.accept(dataPacket);
		exam.setExamID(examControl.examID);
		 MyFile msg= new MyFile(examFile.getName());		 
		 String LocalfilePath=examFile.getPath();	
		  try{

			      File newFile = new File (LocalfilePath);
			      byte [] mybytearray  = new byte [(int)newFile.length()];
			      FileInputStream fis = new FileInputStream(newFile);
			      BufferedInputStream bis = new BufferedInputStream(fis);			  
			      msg.initArray(mybytearray.length);
			      msg.setSize(mybytearray.length);
			      
			      bis.read(msg.getMybytearray(),0,mybytearray.length);
			     parameters.add(0, msg);
			     parameters.add(1, exam.getExamID()+".docx");
			      dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.INSERT_Manuel_EXAM_FILE, parameters, null, true);
			      App_client.chat.accept(dataPacket);
			    }
			catch (Exception e) {
				System.out.println("Error send (Files)msg) to Server");
			}
			errLabel.setText(examFile.getName()+" submited");
			errLabel.setVisible(true);
	}


	private boolean checkInvalidInputsForSumbit() {
		if(duration.getText().equals(""))
		{
			errLabel.setText("*You must enter duration to the exam");
			errLabel.setVisible(true);
			return true;
		}
		if(!super.isNumeric(duration.getText())) {
			errLabel.setText("*You must enter only number to duration!");
			errLabel.setVisible(true);
			return true;
		}
		return false;
	}
	
	



	private void uploadExam() {
		Window window = null;
		FileChooser fileChooser=new FileChooser();
		fileChooser.setTitle("choose exam");
		examFile=fileChooser.showOpenDialog(window);
	}
}
