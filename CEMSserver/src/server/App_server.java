package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App_server extends Application {
	public static CEMSServer server;

	@Override
	public void start(Stage primaryStage) {
		System.out.println("Server manager starting");
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../gui/serverGUI.fxml"));
			Scene scene = new Scene(root, 492, 598);
			
			System.out.println("Server manager started");
			
			primaryStage.setTitle("Server manager");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		System.out.println("Server manager exited");
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}
}