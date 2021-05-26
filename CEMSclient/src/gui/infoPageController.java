/**
 * Sample Skeleton for 'infoPage.fxml' Controller Class
 */

package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import common.User;
import controllers.PageProperties;
import controllers.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class infoPageController {
	SceneController scene;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="ap"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="ComboBox"
	private ComboBox<String> comboBox; // Value injected by FXMLLoader

	@FXML
	private TableView<User> tableOfInfo;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'infoPage.fxml'.";
		scene = new SceneController(PageProperties.Page.INFO_PAGE, ap);
		scene.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("select", "users", "courses", "fields");
		comboBox.getSelectionModel().select("select");
	}

	@FXML
	void OnChoose(ActionEvent event) {
		//set a initilisor
		tableOfInfo.getColumns().clear();
		System.out.println("changed to" + comboBox.getValue());
		DataPacket dataPacket = null;
		switch (comboBox.getValue()) {
		case "users":
			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_USERS, null, null, true);
			App_client.chat.accept(dataPacket);// send and wait for response from server
			TableColumn tc1=new TableColumn("user id");
			TableColumn tc2=new TableColumn("username"); 
			TableColumn tc3=new TableColumn("password"); 
			TableColumn tc4=new TableColumn("email"); 
			TableColumn tc5=new TableColumn("firstName"); 
			TableColumn tc6=new TableColumn("lastName"); 
			TableColumn tc7=new TableColumn("ID"); 
			tableOfInfo.getColumns().addAll(tc1,tc2,tc3,tc4,tc5,tc6,tc7);
			System.out.println("im here");
			break;
		// case

		}

		App_client.chat.accept(dataPacket); // send and wait for response from server

	}
}
