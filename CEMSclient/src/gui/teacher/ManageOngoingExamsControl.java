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

import client.App_client;
import common.DataPacket;
import common.ExtraTimeRequest;
import common.User;
import control.ClientController;
import control.ClientDataPacketHandler;
import control.ExamControl;
import control.ExamInitiatedControl;
import control.ManageOngoingExams;
import control.UserControl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ManageOngoingExamsControl {
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

	@FXML // fx:id="extra_time_request"
	private TextField extra_time_request; // Value injected by FXMLLoader

	@FXML // fx:id="terminate_exam"
	private Button terminate_exam; // Value injected by FXMLLoader

	@FXML // fx:id="pic1"
	private ImageView pic1; // Value injected by FXMLLoader

	@FXML // fx:id="text1"
	private Text text1; // Value injected by FXMLLoader

	@FXML // fx:id="examField"
	private TextField examField; // Value injected by FXMLLoader

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

	private String exam_left_time = null;

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
		assert extra_time_request != null
				: "fx:id=\"extra_time_request\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert terminate_exam != null
				: "fx:id=\"terminate_exam\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert pic1 != null : "fx:id=\"pic1\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert text1 != null : "fx:id=\"text1\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert examField != null
				: "fx:id=\"examField\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert sendBtn != null : "fx:id=\"sendBtn\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert text4 != null : "fx:id=\"text4\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert commentField != null
				: "fx:id=\"commentField\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		assert time != null : "fx:id=\"Timer\" was not injected: check your FXML file 'ManageOngoingExams.fxml'.";
		// Set text for terminate button, set autosize and align to center status(if
		// exams exist for the teacher) label
time.setStyle("-fx-text-fill: blue;-fx-font-weight: bold;-fx-font-size: 12");
		Timer tm;
		terminate_exam.setText("Terminate exam");
		status.autosize();
		status.setAlignment(Pos.CENTER);

		System.out.println("loaded");
		ArrayList<Object> parameter = new ArrayList<Object>();
		parameter.add(UserControl.ConnectedUser);
		// Send request to server to get ongoing exam for the teacher if there is one
		DataPacket data = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_ONGOING_EXAM, parameter, null,
				true);
		App_client.chat.accept(data);

		// If there is an ongoing exam for the teacher
		if (ManageOngoingExams.isOngoingExams != null && ManageOngoingExams.isOngoingExams == true) {

			time.setVisible(true);
			exam_left_time = timeDiffrance(ExamControl.ServerTime, ExamControl.examInitiatedTime);
			exam_left_time = timeDiffrance(ManageOngoingExams.OngoingExam.get(7), exam_left_time);
			System.out.println("LEFT TIME: " + exam_left_time);
time.setText(exam_left_time);
			// start timer to end the exam on finish
			tm = new Timer(1000, new ActionListener() {
				String timer = exam_left_time;

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (ExamInitiatedControl.isExtraTimeRecived) {
								ExamInitiatedControl.isExtraTimeRecived = false;
								// ExamInitiatedControl.ExtraTime
								timer = timeAdd(ExamInitiatedControl.ExtraTime, timer);
								// ExamInitiatedControl.eiID =0;
							}

							timer = timerCountdown(timer);

							time.setText(timer);
						}
					});
				}
			});
			tm.start();

			String leftTime = timeDiffrance(ExamControl.ServerTime, ExamControl.examInitiatedTime);
			System.out.println(leftTime);
			String ongoingExam = "Exam initiated: ";

			// Append strings of ongoing exam info
			int i = 0;
			for (String str : ManageOngoingExams.OngoingExam) {
				if (i == 5) {
					break;
				}
				ongoingExam = ongoingExam + str + " ";
				i++;
			}

			// Set text and style for exam info field
			examField.setText(ongoingExam);
			examField.setStyle(("-fx-font-weight: bold;-fx-font-size: 12"));

			// Event handler for exam termination
			EventHandler<ActionEvent> terminateHandler = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Set text and style for exam termination button
					terminate_exam.setText("EXAM TERMINATED");
					terminate_exam.setStyle(("-fx-text-fill: red;-fx-font-weight: bold"));

					// UserControl.ConnectedUser.getuid()
					// Send request to server to get ongoing exam for the teacher if there is one
					DataPacket data = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.TERMINATE_EXAM,
							parameter, null, true);
					App_client.chat.accept(data);
				}
			};

			// Set action handler
			terminate_exam.setOnAction(terminateHandler);
		}

		// If there is no ongoing exam for the teacher
		else if (ManageOngoingExams.isOngoingExams == false) {

			// Delete unnecessary page elements
			ap.getChildren().removeAll(terminate_exam, examField, extra_time_request, text1, text2, text3, text4, pic1,
					pic2, pic3, pic4, sendBtn, commentField);

			// Show status label(No exams found) and set it's style
			status.setVisible(true);
			status.setStyle(("-fx-font-weight: bold;-fx-font-size: 26;-fx-text-fill: red"));

		}
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