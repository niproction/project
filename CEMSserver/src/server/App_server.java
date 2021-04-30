package server; 

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App_server extends Application {
	public static CEMSServer server;
	public static mysqlConnection mysqlCon;

    @Override
	public void start(Stage primaryStage) {
		
		try {
            Parent root = FXMLLoader.load(getClass().getResource("../gui/serverGUI.fxml"));
            Scene scene = new Scene(root, 492, 598);
        
            primaryStage.setTitle("Server manager");
            primaryStage.setScene(scene);
            primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}