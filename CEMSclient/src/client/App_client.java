package client;	

	
import common.User;
import controllers.ClientController;
import controllers.PageProperties;
import controllers.SceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.stage.Stage;

public class App_client extends Application {
	public static Stage primaryStage=null;
	public static ClientController chat=null;
	
	public static User user=null;
	//public static user user=null;
	//public static user user=null;

	@Override
	public void start(Stage primaryStage) {
		SceneController.primaryStage = primaryStage;
		chat= new ClientController("localhost", 5555);
	
		SceneController sceen = new SceneController(primaryStage, PageProperties.Page.LOGIN);
		sceen.LoadSceen(SceneController.ANIMATE_ON.LOAD);
		
		//SceneController.primaryStage.setMaxWidth(800);
		//SceneController.primaryStage.setMaxHeight(700);
	}

	@Override
	public void stop(){
		chat.GET_client().connectionClosed();//quit();
		//Platform.exit();
		System.out.println("stopped");
	}
	public static void main(String[] args) {
		launch(args);
	}
}
