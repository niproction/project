/**
 * Sample Skeleton for 'serverGUI.fxml' Controller Class
 */

package gui;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import server.App_server;
import server.CEMSServer;
import server.initTables;
import server.mysqlConnection;

public class serverGUI {

	// mysql input fields
	@FXML
	private TextField input_host;
	@FXML
	private TextField input_username;
	@FXML
	private TextField input_password;
	@FXML
	private TextField input_db_name;
	@FXML
	private TextField input_mysql_port;

	// server input field
	@FXML
	private TextField input_server_port;

	@FXML
	private TextArea input_logs;

	@FXML
	private CheckBox checkbox_autoscroll;

	@FXML
	private Button button_mysql_test_conn;
	@FXML
	private Button button_server_test_conn;
	@FXML
	private Button button_start_server;
	@FXML
	private Button button_stop_server;

	@FXML
	private Button button_create_tables;

	private double scrollPosition;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	// information holder
	private String host;
	private String username;
	private String password;
	private String db_name;
	private String mysql_port;
	private String server_port;
	private boolean Is_auto_scroll_logs;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		// lead all information stored in save file
		try {
			BufferedReader br = new BufferedReader(new FileReader("serverinfo.txt"));
			// reads from file and loads it to the inputs
			String temp = br.readLine();
			host = temp == null ? "" : temp;
			input_host.setText(host);

			temp = br.readLine();
			username = temp == null ? "" : temp;
			input_username.setText(username);

			temp = br.readLine();
			password = temp == null ? "" : temp;
			input_password.setText(password);

			temp = br.readLine();
			db_name = temp == null ? "" : temp;
			input_db_name.setText(db_name);

			temp = br.readLine();
			mysql_port = temp == null ? "" : temp;
			input_mysql_port.setText(mysql_port);

			temp = br.readLine();
			server_port = temp == null ? "" : temp;
			input_server_port.setText(server_port);

			temp = br.readLine();
			Is_auto_scroll_logs = temp == null ? false : temp.equals("yes") ? true : false;
			checkbox_autoscroll.setSelected(Is_auto_scroll_logs);
			// System.out.println(Is_auto_scroll_logs);

			br.close();

			getInput_logs().setText(getInput_logs().getText() + "Loaded information to input fields\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();

		}
		button_create_tables.setVisible(false);
	}

	public TextArea getInput_logs() {
		return input_logs;
	}

	public void ServerStartedButtonsDisable() {
		button_mysql_test_conn.setDisable(true);
		button_server_test_conn.setDisable(true);
		button_start_server.setVisible(false);
	}

	public void ServerClosedButtonsEnable() {
		button_mysql_test_conn.setDisable(false);
		button_server_test_conn.setDisable(false);
		button_start_server.setVisible(true);
	}

	public void setInput_logs(String input_logs) {
		this.input_logs.setText(input_logs);
	}

	@FXML
	void mysql_connection_test_button(MouseEvent event) {
		if (input_host.getText().isEmpty() || input_username.getText().isEmpty() || input_db_name.getText().isEmpty()
				|| input_mysql_port.getText().isEmpty()) {
			getInput_logs().appendText("there is empty field/s related to mysql\n");
			return;
		}

		save_information_to_file();

		mysqlConnection mysql = mysqlConnection.getInstance();
		mysql.connectToDB(input_host.getText(), input_username.getText(), input_password.getText(),
				input_db_name.getText(), input_mysql_port.getText());

		getInput_logs().setText(getInput_logs().getText() + mysql.getMessage());

		if (mysql.getIsConnected()) {
			mysql.disconnetFromDB();
			getInput_logs().setText(getInput_logs().getText() + mysql.getMessage());
		}

		// scroll logs function
		logs_scroll();
	}

	@FXML
	void server_connection_test_button(MouseEvent event) {
		if (input_server_port.getText().isEmpty()) {
			getInput_logs().setText(getInput_logs().getText() + "Port number field left empty\n");
			return;
		} else if (!isStringInt(input_server_port.getText())) {
			getInput_logs().setText(getInput_logs().getText() + "The port number contains invalid letters\n");
			return;
		} else if (0 < Integer.parseInt(input_server_port.getText())
				&& Integer.parseInt(input_server_port.getText()) > 9999) {
			getInput_logs().setText(getInput_logs().getText() + "The port number less than 10,000\n");
			return;
		}

		save_information_to_file(); // save the input into file

		App_server.server = new CEMSServer(Integer.parseInt(input_server_port.getText()), this);
		App_server.server.Start();

		if (App_server.server.GET_connectionState()) {
			App_server.server.Close();
			App_server.server = null;
		}

		// scroll logs function
		logs_scroll();
	}

	public void logs_scroll() {
		Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (checkbox_autoscroll.isSelected()) {
					getInput_logs().setScrollTop(Double.MAX_VALUE);
					scrollPosition = getInput_logs().getScrollTop();
				} else
					getInput_logs().setScrollTop(scrollPosition);
			}
		};
		thread.start();
	}

	public boolean isStringInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
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
		host = input_host.getText();
		username = input_username.getText();
		password = input_password.getText();
		db_name = input_db_name.getText();
		mysql_port = input_mysql_port.getText();
		server_port = input_server_port.getText();
		Is_auto_scroll_logs = checkbox_autoscroll.isSelected();

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("serverinfo.txt"));
			bw.write(host);
			bw.newLine();
			bw.write(username);
			bw.newLine();
			bw.write(password);
			bw.newLine();
			bw.write(db_name);
			bw.newLine();
			bw.write(mysql_port);
			bw.newLine();
			bw.write(server_port);
			bw.newLine();
			bw.write(Is_auto_scroll_logs == true ? "yes" : "no");

			bw.close();

			getInput_logs().setText(getInput_logs().getText() + "Saved inputs to file\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		// }
	}

	@FXML
	void start_server_button(MouseEvent event) {
		// constructor to send the information into the server controller and start the
		// db connnection in the constructer

		mysqlConnection mysqlCon = mysqlConnection.getInstance();
		mysqlCon.connectToDB(input_host.getText(), input_username.getText(), input_password.getText(),
				input_db_name.getText(), input_mysql_port.getText());

		if (mysqlCon.getIsConnected()) {
			App_server.server = new CEMSServer(Integer.parseInt(input_server_port.getText()), this);
			App_server.server.Start();

			if (App_server.server.GET_connectionState()) {
				button_mysql_test_conn.setDisable(true);
				button_server_test_conn.setDisable(true);

				button_start_server.setVisible(false);
				button_start_server.setDisable(true);

				button_stop_server.setVisible(true);
				button_stop_server.setDisable(false);

				button_create_tables.setVisible(true);
				button_create_tables.setDisable(false);
				initTables db = new initTables();
				db.update_isconnected();
				db.tables_reset();// delete to reset the tables

			} else {
				mysqlCon.disconnetFromDB();
			}
		}

		// scroll logs function
		logs_scroll();
	}

	@FXML
	void stop_server_button(MouseEvent event) {
		App_server.server.Close();
		App_server.server = null;
		mysqlConnection.getInstance().setCon(null);

		// scroll logs function
		logs_scroll();

		button_mysql_test_conn.setDisable(false);
		button_server_test_conn.setDisable(false);

		button_stop_server.setVisible(false);
		button_stop_server.setDisable(true);

		button_start_server.setDisable(false);
		button_start_server.setVisible(true);
		
		
	}
	
	
	
	@FXML
	void build_tables_button(MouseEvent event) {
		initTables db = new initTables();
	db.tables_reset();// delete to reset the tables

		button_create_tables.setDisable(true);
	}
}