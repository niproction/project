  
package gui.student;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;

import client.App_client;
import common.DataPacket;
import common.examInitiated;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import control.ClientControl;
import control.ExamControl;
import control.ExamInitiatedControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class GetExamController {
	SceneController sceen;
	@FXML // fx:id="ap"
	private AnchorPane ap;
	@FXML
	private JFXPasswordField  passwordfld;
	@FXML
	private Label errorLabel;
	@FXML
	private JFXButton getExamBtn;
	/////////////////////////////////////////
	@FXML
	private JFXPasswordField  idPasswordfld;
	@FXML
	private Label errorLabel2;
	@FXML
	private JFXButton  takeExambtn;
	@FXML
	private Label insertIDlbl;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		// assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file
		// 'Home.fxml'.";
		sceen = new SceneController(PageProperties.Page.GET_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		errorLabel.setVisible(false);
		errorLabel2.setVisible(false);
		idPasswordfld.setVisible(false);
		takeExambtn.setVisible(false);
		insertIDlbl.setVisible(false);

	}

	public void handleButtonAction(MouseEvent event) {
		if (event.getSource() == getExamBtn) {
			System.out.println("password clicked");
			errorLabel.setVisible(false);

			ArrayList<Object> parameters = new ArrayList<>();
			parameters.add(passwordfld.getText());
			parameters.add(UserControl.ConnectedUser);
			DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_EXAM, parameters,
					null, true);
			System.out.println("tring to send");
			ClientControl.getInstance().accept(dataPacket);

			if (ExamInitiatedControl.getExamInitiated() != null) {
				System.out.println("ddddddddddd");
				idPasswordfld.setVisible(true);
				takeExambtn.setVisible(true);
				insertIDlbl.setVisible(true);
			} else {
				passwordfld.setText("");
				errorLabel.setVisible(true);
			}

		}
		if (event.getSource() == takeExambtn) {
			System.out.println("ID clicked");
			errorLabel2.setVisible(false);
			if (UserControl.ID.equals(idPasswordfld.getText()))
				getExam();
			else {
				idPasswordfld.setText("");
				errorLabel2.setVisible(true);
			}
		}

	}

	public void getExam() {

		UserControl.whatInitiatedExamID = ExamInitiatedControl.getExamInitiated().getEiID(); // se the eiID

		if (UserControl.isDoingExam) {
			// load next page
			AnchorPane page = SceneController.getPage(PageProperties.Page.TAKE_EXAM);
			App_client.pageContainer.setCenter(page);
		} else
			errorLabel.setText(UserControl.getCanOpenExam());

	}

}