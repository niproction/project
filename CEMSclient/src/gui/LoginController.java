package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class LoginController implements Initializable {
	@FXML
	private AnchorPane ap;
	@FXML
	private Label lbl_close;
	@FXML
	private TextField txtUserName;
	@FXML
	private TextField txtPassword;
	@FXML
	private Button btnSignIn;
	@FXML
	private Label lblErrors;
	@FXML
	private Label errorLable;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// set sizes
		SceneController.primaryStage.setMinWidth(400);
		SceneController.primaryStage.setMinHeight(405);
		SceneController.primaryStage.setWidth(400);
		SceneController.primaryStage.setHeight(405);

		errorLable.setVisible(false);
		SceneController sceen = new SceneController(PageProperties.Page.LOGIN, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);

	}

	public void handleButtonAction(MouseEvent event) {
		if (event.getSource() == lbl_close) {
			System.exit(0);
		}

		if (event.getSource() == btnSignIn) {
			logIn();
		}
	}

	@FXML
	void check_enter_pressed(KeyEvent event) {
		if(event.getCode().toString().equals("ENTER"))
		{
			System.out.println(event.getCode());
			logIn();
		}
	}

	private void logIn() {
		errorLable.setVisible(false);
		String username_email = txtUserName.getText().toString();
		String password = txtPassword.getText().toString();
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(username_email);
		parameters.add(password);

		System.out.println("user/email: " + parameters.get(0) + " pass:" + parameters.get(1));

		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.LOGIN, parameters, null,
				true);
		System.out.println("tring to send");

		App_client.chat.accept(dataPacket); // send and wait for response from server

		if (UserControl.ConnectedUser != null) {
			System.out.println("Sdasdasdasda");

			// make animation and than load next page
			SceneController sceen = new SceneController(PageProperties.Page.MAIN_PAGE, ap);
			sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);

			SceneController.primaryStage.setMinWidth(840);
			SceneController.primaryStage.setMinHeight(700);
		} else {
			errorLable.setVisible(true);
		}
		System.out.println("Sdasddsda");

		// App_client.chat.GET_client().quit();
	}
}
