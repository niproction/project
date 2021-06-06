package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Timer;

import client.App_client;
import common.DataPacket;
import controllers.PageProperties;
import controllers.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class loginController implements Initializable {
	@FXML
	private AnchorPane ap;
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
	@FXML
	private Label errorLable;
	@FXML
	public Label timerrr;
	Timer timer;
	Timer tm;
	int counter = 0;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// set sizes
		SceneController.primaryStage.setMinWidth(400);
		SceneController.primaryStage.setMinHeight(405);
		SceneController.primaryStage.setWidth(400);
		SceneController.primaryStage.setHeight(405);

		errorLable.setVisible(false);
		SceneController sceen = new SceneController(PageProperties.Page.LOGIN, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		/*try {
			// "G11.docx"
			List<String> bytes = new ArrayList<String>();

			bytes = Files.readAllLines(Paths.get("G11.docx"));
			System.out.println("Ste1.");
			// File get = new File("G11.docx");
			FileWriter myWriter = new FileWriter("filename.docx");
//myWriter.write(get.rea);
			System.out.println("Ste2.");
			for (String string : bytes) {
				myWriter.write(string);
				myWriter.w
			}
			// myWriter.write("Files in Java might be tricky, but it is fun enough!");
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}*/

		// setTimer();
		/*
		 * Platform.runLater(new Runnable() {
		 * 
		 * @Override public void run() { // output.setText(input.getText()); //while
		 * (true) { try { Thread.sleep(1000); counter++;
		 * timerrr.setText(String.valueOf(counter)); } catch (InterruptedException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); } //} } });
		 */

		tm = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) { // TODO Auto-generated
				counter++;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						timerrr.setText(String.valueOf(counter));
					}
				});

			}
		});
		tm.start();

		/*
		 * tm = new Timer(1000,new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { // TODO Auto-generated
		 * method stub counter++; timerrr.setText(String.valueOf(counter));
		 * 
		 * } }); tm.start();
		 */

		/*
		 * tm = new Timer(1000,new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { // TODO Auto-generated
		 * method stub counter++; timerrr.setText(String.valueOf(counter));
		 * 
		 * } }); tm.start();
		 */
	}

	int interval;

	/*
	 * public void setTimer() { timer = new Timer();
	 * Platform.setImplicitExit(false); timer.scheduleAtFixedRate(new TimerTask() {
	 * public void run() { if(interval > 0) {
	 * 
	 * Platform.runLater(() ->
	 * timerrr.setText("Time to read the question: "+interval));
	 * System.out.println(interval); interval--; } else timer.cancel(); } },
	 * 100,100); }
	 */
	public void handleButtonAction(MouseEvent event) {
		if (event.getSource() == lbl_close) {
			System.exit(0);
		}

		if (event.getSource() == btnSignIn) {
			logIn();
		}
	}

	private void logIn() {
		errorLable.setVisible(false);
		String username_email = txtUserName.getText().toString();
		String password = txtPassword.getText().toString();
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(username_email);
		parameters.add(password);

		System.out.println("user/email: " + parameters.get(0) + " pass:" + parameters.get(1));

		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.LOGIN, parameters, null,
				true);
		System.out.println("tring to send");

		App_client.chat.accept(dataPacket); // send and wait for response from server

		if (App_client.user != null) {
			System.out.println("Sdasdasdasda");

			// make animation and than load next page
			SceneController sceen = new SceneController(PageProperties.Page.Main, ap);
			sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);

			SceneController.primaryStage.setMinWidth(840);
			SceneController.primaryStage.setMinHeight(700);
		} else {
			errorLable.setVisible(true);
		}
		System.out.println("Sdasddsda");

		// App_client.chat.GET_client().quit();
	}
}
