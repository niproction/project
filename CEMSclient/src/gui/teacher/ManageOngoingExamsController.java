package gui.teacher;

import java.awt.event.ActionListener;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.swing.Timer;

import common.DataPacket;
import control.ClientControl;
import control.ExamControl;
import control.ExamInitiatedControl;
import control.ManageOngoingExams;
import control.SceneController;
import control.UserControl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ManageOngoingExamsController {
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="ap"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="pic4"
	private ImageView pic4; // Value injected by FXMLLoader

	@FXML // fx:id="text2"
	private Text text2; // Value injected by FXMLLoader

	@FXML // fx:id="pic2"
	private ImageView pic2; // Value injected by FXMLLoader

	@FXML // fx:id="pic3"
	private ImageView pic3; // Value injected by FXMLLoader

	@FXML // fx:id="text3"
	private Text text3; // Value injected by FXMLLoader

	
	@FXML // fx:id="terminate_exam"
	private Button terminate_exam; // Value injected by FXMLLoader

	@FXML // fx:id="pic1"
	private ImageView pic1; // Value injected by FXMLLoader

	@FXML // fx:id="text1"
	private Text text1; // Value injected by FXMLLoader

	@FXML // fx:id="sendBtn"
	private Button sendBtn; // Value injected by FXMLLoader

	@FXML // fx:id="text4"
	private Text text4; // Value injected by FXMLLoader

	@FXML // fx:id="commentField"
	private TextField commentField; // Value injected by FXMLLoader

	@FXML // fx:id="status"
	private Label status; // Value injected by FXMLLoader

	@FXML // fx:id="Timer"
	private Label time; // Value injected by FXMLLoader

	@FXML
	private Label label_exam_info;

	@FXML
	private Label label_message;

	@FXML
	private AnchorPane ap_request_extra_time_box;
	@FXML
	private ChoiceBox<String> minutesChoiceBox;
	@FXML
	private ChoiceBox<String> hourChoiceBox;

	private String exam_left_time = null;
	private String duration = "";
	/**
	 * Initialize the page.
	 */
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert pic4 != null : "fx:id=\"pic4\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert text2 != null : "fx:id=\"text2\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert pic2 != null : "fx:id=\"pic2\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert pic3 != null : "fx:id=\"pic3\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert text3 != null : "fx:id=\"text3\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		
		assert terminate_exam != null
				: "fx:id=\"terminate_exam\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert pic1 != null : "fx:id=\"pic1\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert text1 != null : "fx:id=\"text1\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";

		assert sendBtn != null : "fx:id=\"sendBtn\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert text4 != null : "fx:id=\"text4\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert commentField != null
				: "fx:id=\"commentField\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert time != null : "fx:id=\"Timer\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert label_exam_info != null
				: "fx:id=\"label_exam_info\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";

		// Set text for terminate button, set autosize and align to center status(if
		// exams exist for the teacher) label
		time.setStyle("-fx-text-fill: blue;-fx-font-weight: bold;-fx-font-size: 12");
		Timer tm;
		//terminate_exam.setText("Terminate exam");
		setupDuration();
		
		
		status.autosize();
		status.setAlignment(Pos.CENTER);

		System.out.println("loaded");
		ArrayList<Object> parameter = new ArrayList<Object>();
		parameter.add(UserControl.ConnectedUser);
		// Send request to server to get ongoing exam for the teacher if there is one
		DataPacket data = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_ONGOING_EXAM, parameter, null,
				true);
		ClientControl.getInstance().accept(data);

		// rostik v10
		if (ExamControl.extraTimeRequest != null) {
			System.out.println("sdasdasda");
			if (ExamControl.extraTimeRequest.getIsApproved().equals("waiting")) {
				ap_request_extra_time_box.setVisible(false);
				label_message.setVisible(true);
				label_message.setText("Extra time request waiting principal desition.");
			} else if (ExamControl.extraTimeRequest.getIsApproved().equals("yes")) {
				ap_request_extra_time_box.setVisible(false);
				label_message.setVisible(true);
				label_message.setText("Extra time request been approved by principal.");
				label_message.setStyle("-fx-text-fill: #00C903;");
			} else if (ExamControl.extraTimeRequest.getIsApproved().equals("no")) {
				ap_request_extra_time_box.setVisible(false);
				label_message.setVisible(true);
				label_message.setText("Extra time request been dinaided by principal.");
				label_message.setStyle("-fx-text-fill: #FF0000;");
			} else {
				label_message.setVisible(false);
			}
		} else
			label_message.setVisible(false);

		// If there is an ongoing exam for the teacher
		if (ManageOngoingExams.isOngoingExams != null && ManageOngoingExams.isOngoingExams == true) {
			time.setVisible(true);
			String leftTime = timeDiffrance(ExamControl.ServerTime,
					ExamInitiatedControl.getExamInitiated().getInitiatedDate().substring(11));
			exam_left_time = timeDiffrance(ExamControl.exam.getDuration(), leftTime);

			System.out.println("LEFT TIME: " + exam_left_time);
			time.setText(exam_left_time);

			// rostik v10
			// start timer to end the exam on finish

			tm = new Timer(1000, new ActionListener() {
				String timer = exam_left_time;

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							
							if (ExamControl.isNotifiedAboutExtraTime()) {
								if (ExamControl.isExtraTimeApproved()) {
									timer = timeAdd(ExamControl.extraTimeRequest.getExtraTime(), timer);
									label_message.setText("Extra time request approved by principal");
									label_message.setStyle("-fx-text-fill: #00C903;");
									
								} else {
									label_message.setText("Extra time request denied by principal");
									label_message.setStyle("-fx-text-fill: #FF0000;");
								}
								ExamControl.setNotifiedAboutExtraTime(false);
							}
							
							timer = timerCountdown(timer);

							time.setText(timer);


						}
					});
				}
			});
			SceneController.setInsidePageTimerThraed(tm);
			SceneController.getInsidePageTimerThraed().start();
			// rostik v10

			// leftTime = timeDiffrance(ExamControl.ServerTime,
			// ExamControl.examInitiatedTime);
			System.out.println(leftTime);
			String ongoingExam = "";

			// Set text and style for exam info field
			label_exam_info.setText("exam id : " + ExamControl.exam.getExamID() + " description : "
					+ ExamControl.exam.getDescription());
			label_exam_info.setStyle(("-fx-font-weight: bold;-fx-font-size: 12"));

			
		}

		// If there is no ongoing exam for the teacher
		else if (ManageOngoingExams.isOngoingExams == false) {

			// Delete unnecessary page elements
			ap.getChildren().removeAll(terminate_exam, label_exam_info, hourChoiceBox, minutesChoiceBox, text1, text2, text3, text4,
					pic1, pic2, pic3, pic4, sendBtn, commentField);

			// Show status label(No exams found) and set it's style
			status.setVisible(true);
			status.setStyle(("-fx-font-weight: bold;-fx-font-size: 26;-fx-text-fill: red"));
			ap_request_extra_time_box.setVisible(false);
		}
	}

	@FXML
	void button_send_request_clicked(MouseEvent event) {
		
		
		// bulding duration for the exam
		if (hourChoiceBox.getValue() == null)
			duration += "00:";
		else {
			duration += hourChoiceBox.getValue() + ":";
		}
		if (minutesChoiceBox.getValue() == null)
			duration += "00:";
		else {
			duration += minutesChoiceBox.getValue() + ":";
		}
		duration += "00";//set the seconds
		
		
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(UserControl.ConnectedUser);
		parameters.add(ExamInitiatedControl.getExamInitiated());
		parameters.add(commentField.getText());
		parameters.add(duration);
		// Send request to server to get ongoing exam for the teacher if there is one
		DataPacket data = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.TEACHER_REQUEST_EXTRA_TIME,
				parameters, null, true);
		ClientControl.getInstance().accept(data);

		if (UserControl.RequestForExtraTimeSent) {
			// hide here
			ap_request_extra_time_box.setVisible(false);
			label_message.setText("Request sent to principal and waiting for desition");
			label_message.setVisible(true);
		} else {
			label_message.setText("Problem accured while tring to request extra time");
			label_message.setVisible(true);
		}
	}

	@FXML
	void button_terminate_clicked(MouseEvent event) {
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(UserControl.ConnectedUser);
		parameters.add(ExamInitiatedControl.getExamInitiated());
		DataPacket data = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.TERMINATE_EXAM, parameters, null,
				true);
		ClientControl.getInstance().accept(data);
	}
	
	
	
	
	
	protected void setupDuration() {
		ObservableList<String> hourList = FXCollections.observableArrayList();
		ObservableList<String> minList = FXCollections.observableArrayList();

		//errLabel.setVisible(false);
		for (int i = 0; i < 60; i++) {
			minList.add(Integer.toString(i));
		}
		for (int i = 0; i < 4; i++) {
			hourList.add(Integer.toString(i));
		}

		hourChoiceBox.setItems(hourList);
		minutesChoiceBox.setItems(minList);
	}
	
	
	
	
	

	/// time2 - time1
	public String timeDiffrance(String time2, String time1) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date1 = null;
		Date date2 = null;
		long res = 0;
		String time;

		try {
			date1 = format.parse(time1);
			date2 = format.parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// exam_duration
		res = date2.getTime() - date1.getTime();
		// System.out.println(res);

		time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(res),
				TimeUnit.MILLISECONDS.toMinutes(res) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(res)),
				TimeUnit.MILLISECONDS.toSeconds(res)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(res)));

		return time;
	}

	public static String timeAdd(String time2, String time1) {
		int hour1 = Integer.parseInt(time1.substring(0, 2));
		int min1 = Integer.parseInt(time1.substring(3, 5));
		int sec1 = Integer.parseInt(time1.substring(6, 8));

		int hour2 = Integer.parseInt(time2.substring(0, 2));
		int min2 = Integer.parseInt(time2.substring(3, 5));
		int sec2 = Integer.parseInt(time2.substring(6, 8));

		int result_sec = (sec1 + sec2) % 60;
		int result_min = ((min1 + min2) + (sec1 + sec2) / 60) % 60;
		int result_hour = ((hour1 + hour2) + ((min1 + min2) + (sec1 + sec2) / 60) / 60) % 60;

		return time_int_to_string(result_hour, result_min, result_sec);
	}

	public static String time_int_to_string(int hour, int min, int sec) {
		String Shour = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
		String Smin = min < 10 ? "0" + String.valueOf(min) : String.valueOf(min);
		String Ssec = sec < 10 ? "0" + String.valueOf(sec) : String.valueOf(sec);

		return Shour + ":" + Smin + ":" + Ssec;
	}

	public String timerCountdown(String time) {
		int hour = Integer.parseInt(time.substring(0, 2));
		int min = Integer.parseInt(time.substring(3, 5));
		int sec = Integer.parseInt(time.substring(6, 8));
		sec--;

		if (sec < 0) {
			sec = 59;
			min--;

			if (min < 0) {
				hour--;
			}
		}

		String Shour = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
		String Smin = min < 10 ? "0" + String.valueOf(min) : String.valueOf(min);
		String Ssec = sec < 10 ? "0" + String.valueOf(sec) : String.valueOf(sec);

		return Shour + ":" + Smin + ":" + Ssec;
	}

}