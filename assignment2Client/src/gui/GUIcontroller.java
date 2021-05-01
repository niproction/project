package gui;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import client.*;
import common.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.test;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class GUIcontroller {

	/*
	 * public GUIcontroller(String host, int port) { try { client = new
	 * ChatClient(host, port, this); } catch (IOException exception) {
	 * System.out.println("Error: Can't setup connection!" +
	 * " Terminating client."); System.exit(1); } }
	 */
	// Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;
	public String sql;
	@FXML
	private Button change_test_dur_butten;
	@FXML
	private TextField get_test_info_txt;
	@FXML
	private TextField change_test_dur_txt;
	@FXML
	private Button get_test_info_button;
	@FXML
	private Button get_conn_info;
	@FXML
	private TextArea log_area_txt;
	@FXML
	private TextField get_new_dur_txt;
	@FXML
	private Button exit_client_button;
	@FXML
	public TableView<test> testInfoTable;
	@FXML
	private TableColumn<test, String> idColumn;
	@FXML
	private TableColumn<test, String> professionColumn;
	@FXML
	private TableColumn<test, String> courseColumn;
	@FXML
	private TableColumn<test, String> testDurationColumn;
	@FXML
	private TableColumn<test, String> pointsColumn;
	@FXML
	public Label errorlabel;
	@FXML
	public Label updatesuccededLabel;
	
	public ObservableList<test> tests = FXCollections.observableArrayList();
	public String saveValue;
	public ArrayList<String> arr;
	public static boolean updated = false;
	// Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */

	@FXML
	void initialize() {
		errorlabel.setVisible(false);
		updatesuccededLabel.setVisible(false);
		idColumn.setCellValueFactory(new PropertyValueFactory<test, String>("id"));
		professionColumn.setCellValueFactory(new PropertyValueFactory<test, String>("profession"));
		courseColumn.setCellValueFactory(new PropertyValueFactory<test, String>("course"));
		testDurationColumn.setCellValueFactory(new PropertyValueFactory<test, String>("testDuration"));
		pointsColumn.setCellValueFactory(new PropertyValueFactory<test, String>("points"));
		arr = new ArrayList<>();
		arr.add("client has been connected");
		appClient.chat.accept(arr);
		appClient.chat.reciveController(this);
	}
	// Constructors ****************************************************

	public void start(Stage stage) {
		try {
			Parent root=FXMLLoader.load(getClass().getResource("/gui/GUI.fxml"));
			stage.initStyle(StageStyle.DECORATED);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("app client");
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public String getTestInfoNum() {
		return (get_test_info_txt.getText().toString());
	}

	public String getChangeTestNum() {
		return change_test_dur_txt.getText().toString();
	}

	public String getDuration() {
		return get_new_dur_txt.getText().toString();
	}


	
	public void handleButtonOnAction(MouseEvent event) {
		updated = false;
		arr = new ArrayList<>();
		errorlabel.setVisible(false);
		updatesuccededLabel.setVisible(false);
		if (event.getSource() == get_test_info_button) {
			arr.add(getTestInfoNum());
			appClient.chat.accept(arr);
		}

		else if (event.getSource() == change_test_dur_butten) {
			arr.add(getDuration());
			arr.add(getChangeTestNum());
			saveValue = getChangeTestNum();
			appClient.chat.accept(arr);
			if (updated) {
				System.out.println(getChangeTestNum()+"fffff");
				arr=new ArrayList<>();
				arr.add(getChangeTestNum());
				appClient.chat.accept(arr);
			}
		} else if (event.getSource() == exit_client_button) {
			arr.add("client has been disconnected");
			appClient.chat.accept(arr);
			System.exit(0);
		}
	}


}
//End of ConsoleChat class
