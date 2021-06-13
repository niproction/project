package gui;

/**
 * Sample Skeleton for 'SettingsLoginPage.fxml' Controller Class
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import control.ClientControl;
import control.PageProperties;
import control.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class SettingsLoginPageController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="ap"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="button_save"
	private Button button_save; // Value injected by FXMLLoader

	@FXML // fx:id="button_reset"
	private Button button_reset; // Value injected by FXMLLoader

	@FXML // fx:id="textfield_host"
	private TextField textfield_host; // Value injected by FXMLLoader

	@FXML // fx:id="textfield_port"
	private TextField textfield_port; // Value injected by FXMLLoader

	@FXML // fx:id="textfield_host"
	private Label label_message; // Value injected by FXMLLoader

	
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'SettingsLoginPage.fxml'.";
		assert button_save != null
				: "fx:id=\"button_save\" was not injected: check your FXML file 'SettingsLoginPage.fxml'.";
		assert button_reset != null
				: "fx:id=\"button_reset\" was not injected: check your FXML file 'SettingsLoginPage.fxml'.";
		assert textfield_host != null
				: "fx:id=\"textfield_host\" was not injected: check your FXML file 'SettingsLoginPage.fxml'.";
		assert textfield_port != null
				: "fx:id=\"textfield_port\" was not injected: check your FXML file 'SettingsLoginPage.fxml'.";

		
		ClientControl.destroyInstance(); // kill the connection to server if made any
		
		
		// lead all information stored in save file
		try {
			BufferedReader br = new BufferedReader(new FileReader("clientinfo.txt"));
			// reads from file and loads it to the inputs
			String temp = br.readLine();
			String host = temp == null ? "" : temp;
			textfield_host.setText(host);

			temp = br.readLine();
			String port = temp == null ? "" : temp;
			textfield_port.setText(port);

			// System.out.println(Is_auto_scroll_logs);

			br.close();

			// getInput_logs().setText(getInput_logs().getText() + "Loaded information to
			// input fields\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		label_message.setVisible(false);
		
		SceneController sceen = new SceneController(PageProperties.Page.SETTINGS_LOGIN_PAGE, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
	}

	@FXML
	void button_reset_clicked(MouseEvent event) {
		textfield_host.setText("localhost");
		textfield_port.setText("5555");
	}

	@FXML
	void button_save_clicked(MouseEvent event) {
		if (save_information_to_file()) // save the host and port information on local file
		{
			// make animation and than load next page
			SceneController sceen = new SceneController(PageProperties.Page.LOGIN, ap);
			sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
		}
	}

	private boolean save_information_to_file() {
		String host = textfield_host.getText();
		String port = textfield_port.getText();

		if (isRepresentHost(host) && isRepresentNumber(port) && port.length()<=4) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("clientinfo.txt"));
				bw.write(host);
				bw.newLine();
				bw.write(port);
				bw.close();
				System.out.println("ok");
				label_message.setText("");
				label_message.setVisible(true);
				return true;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				label_message.setText("Error saving information file");
				label_message.setVisible(true);
			}
		}
		else
		{
			if(!isRepresentHost(host))
			{
				label_message.setText("Invailed host(the sintex: x.x.x.x or localhost)");
				label_message.setVisible(true);
			}
			if(!isRepresentNumber(port) || port.length()>4)
			{	
				label_message.setText("Invailed port(must contain mubers, be less than 9999");
				label_message.setVisible(true);
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
		if(numbers.length<4)
			return false;
		
		for (String strr : numbers)
			if(!isRepresentNumber(strr) || strr.length() < 1 || 3 < strr.length())
				return false;
		
		return true;
	}
}
