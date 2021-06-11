package client;

import java.util.ArrayList;

import common.DataPacket;
import common.Question;
import common.Student;
import common.User;
import control.ClientControl;
import control.ClientControl;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App_client extends Application {
	public static Stage primaryStage = null;
	
	public static BorderPane pageContainer;

	public static Question Question = null;
	public static String fieldName = null;
	public static String seccess = null;

	@Override
	public void start(Stage primaryStage) {
		SceneController.primaryStage = primaryStage;

		SceneController sceen = new SceneController(primaryStage, PageProperties.Page.LOGIN);
		sceen.LoadSceen(SceneController.ANIMATE_ON.LOAD);

		// SceneController.primaryStage.setMaxWidth(800);
		// SceneController.primaryStage.setMaxHeight(700);
	}

	@Override
	public void stop() {
		// check if there is on going exam
		if (UserControl.ConnectedUser != null && UserControl.ConnectedUser instanceof Student && UserControl.isDoingExam) {
			System.out.println("Try to logout send logout DataPacket");
			ArrayList<Object> parameter = new ArrayList<>();
			parameter.add(UserControl.ConnectedUser);
			DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.LOGOUT, parameter, null,
					true);
			ClientControl.getInstance().accept(dataPacket);// send and wait for response from server
			// will recive message from server and set user to null in the clientDataPacketHandeler
		}

		// check if the user is still logged in..
		if (UserControl.ConnectedUser != null) {
			System.out.println("Try to logout send logout DataPacket");
			ArrayList<Object> parameter = new ArrayList<>();
			parameter.add(UserControl.ConnectedUser);
			DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.LOGOUT, parameter, null,
					true);
			ClientControl.getInstance().accept(dataPacket);// send and wait for response from server
			// will recive message from server and set user to null
		}

		if(ClientControl.isConnected())
			// free the connetion with the server and release the instance
			ClientControl.getInstance().destroyInstance();
		
		
		
		System.out.println("stopped");
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
