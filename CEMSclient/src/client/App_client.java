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

// TODO: Auto-generated Javadoc
/**
 * The Class App_client.
 * the client app witch run the first page of the CEMS project
 */
public class App_client extends Application {
	
	/** The primary stage.
	 *  */
	public static Stage primaryStage = null; 
	
	/** The page container. */
	public static BorderPane pageContainer;

	/** The Question. */
	public static Question Question = null;
	
	/** The field name. */
	public static String fieldName = null;
	
	/** The seccess. */
	public static String seccess = null;
	
	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 */
	@Override
	public void start(Stage primaryStage) {
		SceneController.primaryStage = primaryStage;

		SceneController sceen = new SceneController(primaryStage, PageProperties.Page.LOGIN);
		sceen.LoadSceen(SceneController.ANIMATE_ON.LOAD);

		// SceneController.primaryStage.setMaxWidth(800);
		// SceneController.primaryStage.setMaxHeight(700);
	}

	/**
	 * Stop.
	 * this method update the client control when the client try to log out
	 */ 
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
			UserControl.resetAll();
		}

		if(ClientControl.isConnected())
			// free the connetion with the server and release the instance
			ClientControl.getInstance().destroyInstance();
		
		
		
		System.out.println("stopped");
		System.exit(0);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
