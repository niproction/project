package client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class loginController implements Initializable {
	
	@FXML
	private Label lbl_close;
	@FXML
	private TextField txtUserName;
	@FXML
	private TextField txtPassword;
	@FXML
	private Button btnSignIn;
	@FXML
	private Label lblErrors;
	
	public void handleButtonAction(MouseEvent event) {
		if(event.getSource()==lbl_close) {
			System.exit(0);
		}
		if(event.getSource()==btnSignIn) {
			logIn();
			//login here
			/*if(logIn().equals("Success")) {
				try {
					Node node=(Node) event.getSource();
					Stage stage=(Stage) node.getScene().getWindow();
					stage.setMaximized(true);
					stage.close();
					
					Scene scene=new Scene(FXMLLoader.load(getClass().getResource("Home.fxml")));
					stage.setScene(scene);
					stage.show();
					
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}*/
				
		}
		
	}
	
	@Override
	public void initialize(URL url,ResourceBundle rb) {
		//TODO
	}
	
	
	private String logIn()
	{
		String email=txtUserName.getText().toString();
		String password=txtPassword.getText().toString();
		//query
		String sql="SELECT * FROM admins Where email = " + email + " AND password = " + password;
		 ClientConsole chat= new ClientConsole("localhost", 5555);
		    chat.accept(sql);  //Wait for console data
		/*if(!resultSet.next()) 
		{
			lblErrors.setTextFill(Color.TOMATO);
			lblErrors.setText("Enter Correct Email/Password");
			System.err.println("Wrong Logins--///");
			return "Error";
		}
		else
		{
			lblErrors.setTextFill(Color.GREEN);
			lblErrors.setText("Login Successful..Redirecting..");
			System.out.println("Successful Login");
			return "Success";
		}*/
		return null;
	}
	

}
