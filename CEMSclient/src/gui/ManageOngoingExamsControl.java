package gui;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ManageOngoingExamsControl {





	    @FXML // ResourceBundle that was given to the FXMLLoader
	    private ResourceBundle resources;

	    @FXML // URL location of the FXML file that was given to the FXMLLoader
	    private URL location;

	    @FXML // fx:id="ap"
	    private AnchorPane ap; // Value injected by FXMLLoader

	    @FXML // fx:id="Timer"
	    private Label Timer; // Value injected by FXMLLoader

	    @FXML // fx:id="extra_time_request"
	    private TextField extra_time_request; // Value injected by FXMLLoader

	    @FXML // fx:id="terminate_exam"
	    private Button terminate_exam; // Value injected by FXMLLoader

	    @FXML // fx:id="examField"
	    private TextField examField; // Value injected by FXMLLoader

	    @FXML // This method is called by the FXMLLoader when initialization is complete
	    void initialize() {
	        assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
	        assert Timer != null : "fx:id=\"Timer\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
	        assert extra_time_request != null : "fx:id=\"extra_time_request\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
	        assert terminate_exam != null : "fx:id=\"terminate_exam\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
	        assert examField != null : "fx:id=\"examField\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";

	    }
	

}

