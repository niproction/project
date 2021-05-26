package gui;

import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import controllers.PageProperties;
import controllers.SceneController;
import controllers.examControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class getExamController {
	SceneController sceen;
	@FXML // fx:id="ap"
	private AnchorPane ap;
	@FXML
	private TextField passwordfld;
	@FXML
	private Label errorLabel;
	@FXML
	private Button getExamBtn;
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		// assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file
		// 'Home.fxml'.";
		sceen = new SceneController(PageProperties.Page.GET_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		errorLabel.setVisible(false);
	}
	public void handleButtonAction(MouseEvent event) {
		if(event.getSource()==getExamBtn) {
			errorLabel.setVisible(false);
			getExam();
		}
		
	}
	public void getExam() {
		ArrayList<Object> parameters=new ArrayList<>();
		parameters.add(passwordfld.getText().toString());
		parameters.add(App_client.user);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_EXAM, parameters, null, true);
		System.out.println("tring to send");
		App_client.chat.accept(dataPacket);
		
		if(examControl.getExam()!=null) {
			SceneController sceen = new SceneController(PageProperties.Page.TAKE_EXAM, ap);
			sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
		}
		else
			errorLabel.setVisible(true);
	}

}
