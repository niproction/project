package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class mainController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="button_menu"
    private ImageView button_menu; // Value injected by FXMLLoader


    @FXML
    void button_menu_clicked(MouseEvent event) {
    	System.out.println("clicked");
    }

    @FXML
    void button_menu_enterd(MouseEvent event) {
    	System.out.println("entered");
    }

    @FXML
    void button_menu_exited(MouseEvent event) {
    	System.out.println("exited");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }
}
