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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
	}

	@FXML
	void button_reset_clicked(MouseEvent event) {

	}

	@FXML
	void button_save_clicked(MouseEvent event) {

	}

	private void save_information_to_file() {
		/*
		 * if( ! host.equals(input_host.getText()) || !
		 * username.equals(input_username.getText()) || !
		 * password.equals(input_password.getText()) || !
		 * db_name.equals(input_db_name.getText()) || !
		 * mysql_port.equals(input_mysql_port.getText()) || !
		 * server_port.equals(input_server_port.getText()) || Is_auto_scroll_logs !=
		 * checkbox_autoscroll.isSelected()) {
		 */
		String host = textfield_host.getText();
		String port = textfield_port.getText();
		

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("clientinfo.txt"));
			bw.write(host);
			bw.newLine();
			bw.write(port);
			

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		// }
	}
}
