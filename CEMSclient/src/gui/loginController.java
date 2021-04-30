package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import controllers.FxmlSceen;
import controllers.SceneController;
import controllers.SceneController.ANIMATE_ON;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class loginController implements Initializable {
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


	@Override
	public void initialize(URL url,ResourceBundle rb)
	{
	}

	public void handleButtonAction(MouseEvent event) {
		if(event.getSource()==lbl_close) {
			System.exit(0);
		}
		
		if(event.getSource()==btnSignIn) {
			logIn();
		}
	}
	

	private void logIn()
	{
		String email=txtUserName.getText().toString();
		String password=txtPassword.getText().toString();
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(email);
		parameters.add(password);
		
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.LOGIN, parameters, null, true);
		System.out.println("tring to send");

		App_client.chat.accept(dataPacket);  //Wait for console data



		if(App_client.user != null)
		{
			System.out.println("Sdasdasdasda");
			SceneController sceen = new SceneController(FxmlSceen.HOME, ap);
			sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
		}
		System.out.println("Sdasddsda");
		
		//App_client.chat.GET_client().quit();
	}
}
