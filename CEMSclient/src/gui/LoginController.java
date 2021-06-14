package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
	private AnchorPane ap_login_box;
	@FXML
	private AnchorPane ap_settings_box;
	@FXML
	private TextField txthost;
	@FXML
	private TextField txtport;
	@FXML
	private Label errorLable;
	@FXML
	private Label errorLablesettings;
	@FXML
	private TextField txtHost;

	@FXML
	private TextField txtPort;
	@FXML
	private Button btnsave;
	@FXML
	private Button btnreset;
	@FXML
	private Button button_settings;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// set sizes
		/*
		 * SceneController.primaryStage.setMinWidth(400);
		 * SceneController.primaryStage.setMinHeight(405);
		 * SceneController.primaryStage.setWidth(400);
		 * SceneController.primaryStage.setHeight(405);
		 */
		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'Login.fxml'.";
		assert txthost != null : "fx:id=\"txthost\" was not injected: check your FXML file 'Login.fxml'.";
		assert button_settings != null
				: "fx:id=\"button_settings\" was not injected: check your FXML file 'Login.fxml'.";
		assert ap_login_box != null : "fx:id=\"ap_login_box\" was not injected: check your FXML file 'Login.fxml'.";
		assert txtUserName != null : "fx:id=\"txtUserName\" was not injected: check your FXML file 'Login.fxml'.";
		assert errorLable != null : "fx:id=\"errorLable\" was not injected: check your FXML file 'Login.fxml'.";
		assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'Login.fxml'.";
		assert btnSignIn != null : "fx:id=\"btnSignIn\" was not injected: check your FXML file 'Login.fxml'.";
		assert txtport != null : "fx:id=\"txtport\" was not injected: check your FXML file 'Login.fxml'.";

		SceneController.primaryStage.setMinWidth(855);
		SceneController.primaryStage.setMinHeight(560);
		SceneController.primaryStage.setMaxWidth(855);
		SceneController.primaryStage.setMaxHeight(560);
		SceneController.primaryStage.setWidth(855);
		SceneController.primaryStage.setHeight(560);
		SceneController.primaryStage.setResizable(false);

		errorLable.setVisible(false);
		SceneController sceen = new SceneController(PageProperties.Page.LOGIN, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		ap_settings_box.setVisible(false);
		errorLablesettings.setVisible(false);
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

		if (username_email.length() == 0) {
			errorLable.setText("Username/Email field empty");
			errorLable.setStyle("-fx-text-fill: #FF0000;");
			errorLable.setVisible(true);
		} else if (!isLegalUsernameOrEmail(username_email)) {
			errorLable.setText("Illigal username or email(must be lowwer case)");
			errorLable.setStyle("-fx-text-fill: #FF0000;");
			errorLable.setVisible(true);
		} else if (ClientControl.getInstance().isConnected()) {

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
		} else {
			errorLable.setText("Can not reach server..");
			errorLable.setStyle("-fx-text-fill: #FF0000;");
			errorLable.setVisible(true);
		}
	}

	@FXML
	void button_settings_clicked(MouseEvent event) {
		// make animation and than load next page
		// SceneController sceen = new
		// SceneController(PageProperties.Page.SETTINGS_LOGIN_PAGE, ap);
		// sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);

		ap_login_box.setVisible(false);
		ap_settings_box.setVisible(true);
		button_settings.setVisible(false);
	}

	@FXML
	void button_reset_clicked(MouseEvent event) {
		txtHost.setText("localhost");
		txtPort.setText("5555");
	}

	@FXML
	void button_save_clicked(MouseEvent event) {
		if (save_information_to_file()) // save the host and port information on local file
		{
			// make animation and than load next page
			// SceneController sceen = new SceneController(PageProperties.Page.LOGIN, ap);
			// sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
			ap_settings_box.setVisible(false);
			ap_login_box.setVisible(true);

			button_settings.setVisible(true);
		}
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
			ClientControl.port = isRepresentPortNumber(temp)
					? Integer.parseInt(temp) < 9999 ? Integer.parseInt(temp) : 5555
					: 5555;

			txtHost.setText(ClientControl.host);
			txtPort.setText(String.valueOf(ClientControl.port));
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

			if (('a' <= c && c <= 'z') || c == '@' || c == '.' || c == '_' || ('0' <= c && c <= '9')) {
			} else
				return false;
		}
		return true;
	}

	private boolean save_information_to_file() {
		String host = txtHost.getText();
		String port = txtPort.getText();

		if (isRepresentHost(host) && isRepresentNumber(port) && port.length() <= 4) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("clientinfo.txt"));
				bw.write(host);
				bw.newLine();
				bw.write(port);
				bw.close();
				System.out.println("ok");
				errorLablesettings.setText("");
				errorLablesettings.setVisible(true);
				load_info();
				return true;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLablesettings.setText("Error saving information file");
				errorLablesettings.setVisible(true);
			}
		} else {
			if (!isRepresentHost(host)) {
				errorLablesettings.setText("Invailed host(the sintex: x.x.x.x or localhost)");
				errorLablesettings.setVisible(true);
			}
			if (!isRepresentNumber(port) || port.length() > 4) {
				errorLablesettings.setText("Invailed port(must contain mubers, be less than 9999");
				errorLablesettings.setVisible(true);
			}
		}
		System.out.println("port conatins not only numbers");
		return false;
	}

	public static boolean isRepresentNumber(String str) {
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

	public static boolean isRepresentHost(String str) {
		if (str.equals("localhost"))
			return true;

		if (str == null)
			return false;

		int length = str.length();

		if (length == 0)
			return false;

		String[] numbers = str.split("\\.");
		if (numbers.length < 4)
			return false;

		for (String strr : numbers)
			if (!isRepresentNumber(strr) || strr.length() < 1 || 3 < strr.length())
				return false;

		return true;
	}
}
