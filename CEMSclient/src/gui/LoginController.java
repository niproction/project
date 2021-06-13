package gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import control.ClientControl;
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

	@FXML
	private Button button_settings;

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
		
		load_info(); // load host and ports from saved file

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
		if (event.getCode().toString().equals("ENTER")) {
			System.out.println(event.getCode());
			logIn();
		}
	}

	private void logIn() {
		errorLable.setVisible(false);
		String username_email = txtUserName.getText().toString();
		String password = txtPassword.getText().toString();
		
		if(username_email.length() == 0)
		{
			errorLable.setText("Username/Email field empty");
			errorLable.setVisible(true);
		}
		else if(!isLegalUsernameOrEmail(username_email))
		{
			errorLable.setText("Illigal username or email(must be lowwer case)");
			errorLable.setVisible(true);
		}
		else if (ClientControl.getInstance().isConnected()) {

			
			ArrayList<Object> parameters = new ArrayList<Object>();
			parameters.add(username_email);
			parameters.add(password);

			System.out.println("user/email: " + parameters.get(0) + " pass:" + parameters.get(1));

			DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.LOGIN, parameters, null,
					true);
			System.out.println("tring to send");

			ClientControl.getInstance().accept(dataPacket); // send and wait for response from server

			if (UserControl.ConnectedUser != null) {
				System.out.println("Sdasdasdasda");

				// make animation and than load next page
				SceneController sceen = new SceneController(PageProperties.Page.MAIN_PAGE, ap);
				sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);

				
			} else {
				errorLable.setText(ClientControl.message_recived);
				errorLable.setVisible(true);
			}
			System.out.println("Sdasddsda");
		} else
		{
			errorLable.setText("Can not reach server..");
			errorLable.setVisible(true);
		}
			
		// App_client.chat.GET_client().quit();
	}

	@FXML
	void button_settings_clicked(MouseEvent event) {
		// make animation and than load next page
		SceneController sceen = new SceneController(PageProperties.Page.SETTINGS_LOGIN_PAGE, ap);
		sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
	}

	private void load_info() {
		// lead all information stored in save file
		try {
			BufferedReader br = new BufferedReader(new FileReader("clientinfo.txt"));
			// reads from file and loads it to the inputs
			String temp = br.readLine();
			ClientControl.host = temp == null ? "" : temp;
			temp = br.readLine();
			System.out.println(ClientControl.host);
			ClientControl.port = isRepresentPortNumber(temp) ? Integer.parseInt(temp)<9999? Integer.parseInt(temp) :  5555 : 5555 ;
			System.out.println(ClientControl.port);
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();

		}
	}

	public static boolean isRepresentPortNumber(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();

		if (length == 0) {
			return false;
		}

		if (str.charAt(0) == '-') {
			return false;
		}
		for (int i = 0; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}
	
	
	public static boolean isLegalUsernameOrEmail(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			
			if ( ('a' <= c && c <= 'z') || c == '@' || c == '.' || c=='_' || ('0' <= c && c <= '9')) {
			}
			else
				return false;
		}
		return true;
	}
}
