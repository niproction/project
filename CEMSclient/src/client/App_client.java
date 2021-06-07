package client;	

	
import java.util.ArrayList;

import common.DataPacket;
import common.Question;
import common.User;
import control.ClientController;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App_client extends Application {
	public static Stage primaryStage=null;
	public static ClientController chat=null;
	public static BorderPane pageContainer;	
	
	public static Question Question=null;
	public static String fieldName=null;
	public static String seccess=null;
	

	
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
		// check if the user is still logged in..
		if(UserControl.ConnectedUser != null)
		{
			System.out.println("Try to logout send logout DataPacket");
			ArrayList<Object> parameter = new ArrayList<>();
			parameter.add(UserControl.ConnectedUser);
			DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.LOGOUT, parameter, null, true);
			App_client.chat.accept(dataPacket);// send and wait for response from server
			// will recive message from server and set user to null
		}

		
		chat.GET_client().connectionClosed();//quit();
		System.out.println("stopped");
		System.exit(0);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
