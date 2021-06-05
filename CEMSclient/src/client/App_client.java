package client;	

	
import common.Question;
import common.User;
import controllers.ClientController;
import controllers.PageProperties;
import controllers.SceneController;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App_client extends Application {
	public static Stage primaryStage=null;
	public static ClientController chat=null;
	 public static BorderPane pageContainer;
	public static User user=null;
	public static Question Question=null;
	public static String fieldName=null;
	public static String seccess=null;
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
		System.out.println("stopped");
		System.exit(0);
	}
	public static void main(String[] args) {
		launch(args);
	}
}
