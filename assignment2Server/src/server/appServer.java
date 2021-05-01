package server;
	
import gui.serverGUIcontroller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class appServer extends Application {
	
	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  } // end main
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub				  		
		serverGUIcontroller aFrame = new serverGUIcontroller(); // create StudentFrame
		 
		aFrame.start(primaryStage);
	}
}
