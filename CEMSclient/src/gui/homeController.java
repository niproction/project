/**
 * Sample Skeleton for 'Home.fxml' Controller Class
 */

package gui;

import java.net.URL;
import java.util.ResourceBundle;

import control.PageProperties;
import control.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class homeController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="ap"
    private AnchorPane ap; // Value injected by FXMLLoader
    @FXML
    private Button addNewQuestion;
    @FXML
    private Button logOutButton;
    @FXML
    private Button exitButton;
    SceneController sceen;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'Home.fxml'.";
        //sceen = new SceneController(PageProperties.Page.HomePage_Student, ap);
		//sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
    }
    public void handleOnAction(MouseEvent event) {
    	if(event.getSource()==addNewQuestion) {
    		SceneController sceen = new SceneController(PageProperties.Page.ADD_NEW_QUESTION, ap);
			sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
    	}
    	else if(event.getSource()==logOutButton) {
    		SceneController sceen = new SceneController(PageProperties.Page.LOGIN, ap);
			sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
    	}
    	else if(event.getSource()==exitButton) {
    		System.exit(0);
    	}
    }
}
