package gui;

import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import controllers.PageProperties;
import controllers.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class createNewExamController {
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
	private ChoiceBox<String> questions;
	@FXML
	private TextArea teacherComments;
	@FXML
	private TextArea studentComments;
	@FXML
	private Button addQuestionBtn;
	@FXML
	private Button submitBtn;
	@FXML
	private TextField pointsForQuestion;
	@FXML
	private Label totalPointsLabel;
	@FXML
	public void initialize() {
		sceen=new SceneController(PageProperties.Page.CREATE_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		ArrayList<Object> parameters=new ArrayList<>();
		parameters.add(App_client.user);
		DataPacket dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_FIELD_NAME, parameters, null, true);
		App_client.chat.accept(dataPacket);
		if(App_client.fieldName!=null) {
			String fieldName=App_client.fieldName;
			App_client.fieldName=null;
			fieldNameLabel.setText(fieldName);
		}
		errLabel.setVisible(false);
		totalPointsLabel.setText("0");

		DataPacket dataPacket1=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSES, parameters, null, true);
		App_client.chat.accept(dataPacket1);
		System.out.println("111"+dataPacket1.GET_Data_parameters());
	}
	
}
