package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
public static Stage primaryStage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage=primaryStage;
		Parent root=FXMLLoader.load(getClass().getResource("testFXML.fxml"));
		Scene s=new Scene(root,300,275);
		primaryStage.setScene(s);
		primaryStage.show();
//		imgView.setFitWidth(20);
//		imgView.setFitHeight(20);
//		Menu file = new Menu("File");
//		MenuItem item = new MenuItem("Save", imgView); 
	//	file.getItems().addAll(item);
		// Creating a File chooser
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
