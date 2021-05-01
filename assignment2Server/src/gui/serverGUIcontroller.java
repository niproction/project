package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.EchoServer;

public class serverGUIcontroller {
	final public static int DEFAULT_PORT = 5555;
	@FXML
	private Button listenButten;
	@FXML
	private TextArea clientNameLog;
	@FXML
	private TextArea clientAdressLog;
	@FXML
	private TextArea infoLog;
	@FXML
	private Button exitButten;
	
	public void start(Stage stage) {
		try {
			Parent root=FXMLLoader.load(getClass().getResource("GUIserver.fxml"));
			stage.initStyle(StageStyle.DECORATED);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("app server");
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void handleButtonOnAction(MouseEvent event) {
		if (event.getSource() == listenButten) {
			EchoServer sv = new EchoServer(DEFAULT_PORT,this);
			
		}
		if(event.getSource()==exitButten) {
			System.exit(0);
		}
		
	}
	public void getClientinfo(String clientName,String clientAdress) {
		clientNameLog.setText(clientName);
		clientAdressLog.setText(clientAdress);
	}
	public void getConnInfo(String info) {
		infoLog.appendText(info+"\n");
	}

}
