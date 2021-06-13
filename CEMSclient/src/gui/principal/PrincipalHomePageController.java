package gui.principal;




import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import control.PageProperties.Animation;
import control.ClientControl;
import control.PageProperties;
import control.PrincipalControl;
import control.SceneController;
import control.UserControl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class PrincipalHomePageController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="ap"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="approve"
	private Button approve; // Value injected by FXMLLoader

	@FXML // fx:id="notification"
	private Label notification; // Value injected by FXMLLoader

	@FXML // fx:id="txtGood"
	private Text txtGood; // Value injected by FXMLLoader

	@FXML // fx:id="txtTime"
	private Text txtTime; // Value injected by FXMLLoader

	@FXML // fx:id="timedate"
	private Label timedate; // Value injected by FXMLLoader

	@FXML // fx:id="sunset"
	private ImageView sunset; // Value injected by FXMLLoader

	@FXML // fx:id="sunrise"
	private ImageView sunrise; // Value injected by FXMLLoader

	@FXML // fx:id="moon"
	private ImageView moon; // Value injected by FXMLLoader

	@FXML // fx:id="sun"
	private ImageView sun; // Value injected by FXMLLoader

	@FXML // fx:id="curtime"
	private Label curtime; // Value injected by FXMLLoader

	@FXML // fx:id="examcount"
	private Label examcount; // Value injected by FXMLLoader

	@FXML // fx:id="reqNum"
	private Label reqNum; // Value injected by FXMLLoader

	@FXML // fx:id="txtGood1"
	private Text txtGood1; // Value injected by FXMLLoader

	@FXML
	void reqHandles(ActionEvent event) {
		AnchorPane page = SceneController.getPage(PageProperties.Page.EXTRA_TIME_REQUESTS);
		App_client.pageContainer.setCenter(page);
	}

	/**
	 * Initialize the page.
	 */
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert approve != null : "fx:id=\"approve\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert notification != null
				: "fx:id=\"notification\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert txtGood != null : "fx:id=\"txtGood\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert txtTime != null : "fx:id=\"txtTime\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert timedate != null : "fx:id=\"timedate\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert sunset != null : "fx:id=\"sunset\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert sunrise != null : "fx:id=\"sunrise\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert moon != null : "fx:id=\"moon\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert sun != null : "fx:id=\"sun\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert curtime != null : "fx:id=\"curtime\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert examcount != null
				: "fx:id=\"examcount\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert reqNum != null : "fx:id=\"reqNum\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";
		assert txtGood1 != null : "fx:id=\"txtGood1\" was not injected: check your FXML file 'PrincipalHomePage.fxml'.";

		// Show label on load
		Integer count = 0;
		examcount.setText(count.toString());
		examcount.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-font-style: italic;-fx-font-color:#73255a");

		// Initiate the current time
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		final String inittime = simpleDateFormat.format(new Date());
		curtime.setText(inittime);
		curtime.setStyle(("-fx-font-weight: bold;-fx-font-size: 12;-fx-font-style: italic;"));

		// Get how many exams are running right now
		DataPacket data = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_HOW_MANY_EXAMS, null, null,
				true);
		ClientControl.getInstance().accept(data);

		if (PrincipalControl.HowManyExamsNow != 0) {
			count = PrincipalControl.HowManyExamsNow;
		}
		PrincipalControl.HowManyExamsNow = 0;
		examcount.setText(count.toString());
		examcount.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-font-style: italic;-fx-font-color:#73255a");

		// Get how many requests are pending to approve/disapprove
		data = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_IF_REQUEST, null, null, true);
		ClientControl.getInstance().accept(data);

		count = PrincipalControl.ifRequests;
		PrincipalControl.ifRequests = 0;
		reqNum.setText(count.toString());
		reqNum.setStyle("-fx-font-weight: bold;-fx-font-size: 20;-fx-font-style: italic;-fx-font-color:#73255a");
		reqNum.setVisible(true);

		// If the are requests
		if (count > 0) {
			approve.setVisible(true);

		}

		// Display current time every second
		Thread timerThread = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000); // 1 second
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				final String time = simpleDateFormat.format(new Date());
				Platform.runLater(() -> {
					curtime.setText(time);
					curtime.setStyle(("-fx-font-weight: bold;-fx-font-size: 12;-fx-font-style: italic;"));
				});
			}
		});
		timerThread.start();// start the thread and its ok

		// Check current time for morning/afternoon/evening/night and set relevant label
		// text and picture
		Calendar c = Calendar.getInstance();
		int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
		if (timeOfDay >= 6 && timeOfDay < 12) {
			txtTime.setText("morning");
			sunrise.setVisible(true);
			txtTime.setVisible(true);
		} else if (timeOfDay >= 12 && timeOfDay < 16) {
			txtTime.setText("afternoon");
			sun.setVisible(true);
			txtTime.setVisible(true);
		} else if (timeOfDay >= 16 && timeOfDay < 21) {
			txtTime.setText("evening");
			sunset.setVisible(true);
			txtTime.setVisible(true);
		} else if ((timeOfDay >= 21 && timeOfDay < 24) || (timeOfDay >= 0 && timeOfDay <= 6)) {
			txtTime.setText("night");
			moon.setVisible(true);
			txtTime.setVisible(true);
		}
//Show the date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		timedate.setVisible(true);
		timedate.setStyle(("-fx-font-weight: bold;-fx-font-size: 12;-fx-font-style: italic;"));
		timedate.setText(dtf.format(now));
	}

}