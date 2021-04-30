package client;

	
import common.user;
import controllers.ClientController;
import controllers.FxmlSceen;
import controllers.SceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class App_client extends Application {
	public static Stage primaryStage;
	public static ClientController chat=null;
	
	public static user user=null;
	//public static user user=null;
	//public static user user=null;

	@Override
	public void start(Stage primaryStage) {
		SceneController.primaryStage = primaryStage;
		chat= new ClientController("localhost", 5555);
	
		SceneController sceen = new SceneController(primaryStage, FxmlSceen.LOGIN);
		sceen.LoadSceen(SceneController.ANIMATE_ON.LOAD);
	}

	@Override
	public void stop(){
		chat.GET_client().connectionClosed();//quit();
		Platform.exit();
		System.out.println("Sdas");
	}
	public static void main(String[] args) {
		launch(args);
	}
}
