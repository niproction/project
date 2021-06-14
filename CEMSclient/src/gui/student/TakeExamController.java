package gui.student;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.swing.Timer;

import common.DataPacket;
import common.Exam;
import common.Question;
import common.examInitiated;
import control.ClientControl;
import control.ExamControl;
import control.ExamInitiatedControl;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class TakeExamController {
	SceneController sceen;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // fx:id="ap"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="label_question"
	private Label label_question; // Value injected by FXMLLoader

	@FXML // fx:id="option1"
	private CheckBox option1; // Value injected by FXMLLoader

	@FXML // fx:id="option2"
	private CheckBox option2; // Value injected by FXMLLoader

	@FXML // fx:id="option3"
	private CheckBox option3; // Value injected by FXMLLoader

	@FXML // fx:id="option4"
	private CheckBox option4; // Value injected by FXMLLoader

	@FXML // fx:id="next"
	private Button next; // Value injected by FXMLLoader

	@FXML // fx:id="submitBtn"
	private Button submitBtn; // Value injected by FXMLLoader

	@FXML // fx:id="back"
	private Button back; // Value injected by FXMLLoader

	@FXML // fx:id="testSubmited"
	private Label testSubmited; // Value injected by FXMLLoader

	@FXML // fx:id="label_timer"
	private Label label_timer; // Value injected by FXMLLoader

	@FXML // fx:id="label_questions"
	private Label label_questions; // Value injected by FXMLLoader

	@FXML // fx:id="label_questions_amunt"
	private Label label_questions_amunt; // Value injected by FXMLLoader
	//////////////////////////////////////
	@FXML
	private Button getGuidelinesBtn;
	@FXML
	private AnchorPane GuidelinesTxt;
	
	@FXML
	private Label commentsLbl;
	@FXML
	private Label commentsTxt;

	@FXML
	private AnchorPane ap_exam_stats;

	ArrayList<Question> testQuestions;
	String answers[];
	List<String> answerList;

	private Exam exam;
	private examInitiated examInitiated;
	private int hour, min, sec, index;
	private String endTime;
	private int timeLeft, counter;
	private Timer tm;
/////////////////////////////////////////////////////////////////////
	private String server_time;
	private String skipped_time;
	private String startTime;
	private String initiated_time;

	private String exam_left_time;

	private String exam_duration;
	private int extra_time;
	private String exam_timer;

	private boolean clicked = false;
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert label_question != null
				: "fx:id=\"label_question\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert option1 != null : "fx:id=\"option1\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert option2 != null : "fx:id=\"option2\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert option3 != null : "fx:id=\"option3\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert option4 != null : "fx:id=\"option4\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert next != null : "fx:id=\"next\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert submitBtn != null : "fx:id=\"submitBtn\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert testSubmited != null
				: "fx:id=\"testSubmited\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert label_timer != null
				: "fx:id=\"label_timer\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert label_questions != null
				: "fx:id=\"label_questions\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		assert label_questions_amunt != null
				: "fx:id=\"label_questions_amunt\" was not injected: check your FXML file 'TakeExamPage.fxml'.";
		// hide
		testSubmited.setVisible(false);

		// animate page on load
		sceen = new SceneController(PageProperties.Page.TAKE_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		GuidelinesTxt.setVisible(false);
		//continueExamBtn.setVisible(false);
		//commentsLbl.setVisible(false);
		System.out.println("take exam page loaded");

		examInitiated = ExamInitiatedControl.getExamInitiated();
		initiated_time = examInitiated.getInitiatedDate().substring(11, 19);

		exam_duration = ExamControl.getExam().getDuration();

		System.out.println(initiated_time);
		ExamInitiatedControl.setExamInitiated(null);
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(examInitiated);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_TEST_QUESTIONS,
				parameters, null, true);

		System.out.println("trying to send exam");

		ClientControl.getInstance().accept(dataPacket);
		// will recive the time left for the exam

		String serverCurrentTime = ExamControl.ServerTime;

		System.out.println(serverCurrentTime);

		exam_left_time = timeDiffrance(exam_duration, timeDiffrance(serverCurrentTime, initiated_time));

		label_timer.setText(exam_left_time); // set the left time

		// rostik v10
		// check if already approved extra time to show it in timer..
		if (ExamInitiatedControl.isExtraTimeRecived) {
			ExamInitiatedControl.isExtraTimeRecived = false;
			// ExamInitiatedControl.ExtraTime
			exam_left_time = timeAdd(ExamInitiatedControl.ExtraTime, exam_left_time);
		}
		label_timer.setText(exam_left_time); // set the left time
		// start timer to end the exam on finish
		tm = new Timer(1000, new ActionListener() {
			String timer = exam_left_time;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (ExamInitiatedControl.isExtraTimeRecived) {
							ExamInitiatedControl.isExtraTimeRecived = false;
							timer = timeAdd(ExamInitiatedControl.ExtraTime, timer);
						}
						
						timer = timerCountdown(timer);

						if (timer.equals("00:00:00"))
							submit();

						label_timer.setText(timer);

						if (ExamControl.isExamTerminated()) {
							exam_terminate();
							ExamControl.setExamTerminated(false);
						}
					}
				});
			}
		});
		SceneController.setInsidePageTimerThraed(tm);
		SceneController.getInsidePageTimerThraed().start();
		// rostik v10

		back.setVisible(false);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		// System.out.println(dtf.format(now));
		// dateLabel.setText(dtf.format(now).substring(0, 10));
		startTime = dtf.format(now).substring(11, 19);
		// System.out.println(dateLabel.getText() + "---" + startTime);
		// System.out.println(date+" "+startTime);
		// duration.setText(examInitiated.getTime());

		if (ExamControl.getExam() != null) {
			System.out.println("exam is not null");
			exam = ExamControl.getExam();
			ExamControl.setExam(null);
			commentsTxt.setText(exam.getStudentsComments());
			System.out.println(exam.getStudentsComments());
			testQuestions = exam.getQuestions();
			answers = new String[testQuestions.size()];
			System.out.println("size :" + testQuestions.size());
			label_question.setText(testQuestions.get(0).getInfo());
			option1.setText(testQuestions.get(0).getOption1());
			option2.setText(testQuestions.get(0).getOption2());
			option3.setText(testQuestions.get(0).getOption3());
			option4.setText(testQuestions.get(0).getOption4());
			label_questions_amunt.setText("1/"+testQuestions.size());
			// totalQuestion.setText("" + testQuestions.size());

			//////////////////////////////////////////////////

		} else {
			System.out.println("no exammmmmm");
		}
		// questionInfo.setText(testQuestions.get(1).getInfo());
	}

	private void exam_terminate() {
		System.out.println("tttttttttttttttttttttttttersmdasda<<<<<<<<<<<<<<");
		// hide all the labels
		label_question.setVisible(false);
		option1.setVisible(false);
		option2.setVisible(false);
		option3.setVisible(false);
		option4.setVisible(false);
		next.setVisible(false);
		back.setVisible(false);
		submitBtn.setVisible(false);
		ap_exam_stats.setVisible(false);
		testSubmited.setVisible(true);
		testSubmited.setText("Exam terminated by teacher.");
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

	public void handleAction(MouseEvent event) {
		if (event.getSource() == option1) {
			option2.setSelected(false);
			option3.setSelected(false);
			option4.setSelected(false);
		}
		if (event.getSource() == option2) {
			option1.setSelected(false);
			option3.setSelected(false);
			option4.setSelected(false);
		}
		if (event.getSource() == option3) {
			option1.setSelected(false);
			option2.setSelected(false);
			option4.setSelected(false);
		}
		if (event.getSource() == option4) {
			option1.setSelected(false);
			option2.setSelected(false);
			option3.setSelected(false);
		}
		if (event.getSource() == next) {
			// System.out.println(index);
			back.setVisible(true);

			answers[index++] = option1.isSelected() ? "1"
					: option2.isSelected() ? "2" : option3.isSelected() ? "3" : option4.isSelected() ? "4" : "0";

			// Qnumber.setText(index + 1 + "");

			if (index + 1 == testQuestions.size()) {
				next.setVisible(false);
			}

			label_question.setText(testQuestions.get(index).getInfo());
			option1.setText(testQuestions.get(index).getOption1());
			option2.setText(testQuestions.get(index).getOption2());
			option3.setText(testQuestions.get(index).getOption3());
			option4.setText(testQuestions.get(index).getOption4());
			option1.setSelected(answers[index] == "1");
			option2.setSelected(answers[index] == "2");
			option3.setSelected(answers[index] == "3");
			option4.setSelected(answers[index] == "4");
			/*
			 * option1.setSelected(false); option2.setSelected(false);
			 * option3.setSelected(false); option4.setSelected(false);
			 */
			label_questions_amunt.setText((index+1)+"/"+testQuestions.size());
			System.out.println(answers.length);

		}
		if (event.getSource() == back) {
			next.setVisible(true);
			index--;
			// System.out.println(index);
			option1.setSelected(answers[index] == "1");
			option2.setSelected(answers[index] == "2");
			option3.setSelected(answers[index] == "3");
			option4.setSelected(answers[index] == "4");

			if (index == 0) {
				back.setVisible(false);
			}

			label_question.setText(testQuestions.get(index).getInfo());

			option1.setText(testQuestions.get(index).getOption1());
			option2.setText(testQuestions.get(index).getOption2());
			option3.setText(testQuestions.get(index).getOption3());
			option4.setText(testQuestions.get(index).getOption4());

			label_questions_amunt.setText((index+1)+"/"+testQuestions.size());
		}
		if (event.getSource() == submitBtn) {
			answers[index] = option1.isSelected() ? "1" : option2.isSelected() ? "2" : option3.isSelected() ? "3" :option4.isSelected()? "4":null;
			submit();
		}
		
		
		if (event.getSource() == getGuidelinesBtn) {
			if(!clicked)
			{
				clicked=true;
				submitBtn.setVisible(false);
				ap_exam_stats.setVisible(false);
				GuidelinesTxt.setVisible(true);
				getGuidelinesBtn.setText("Hide comments");
				
			}
			else
			{
				clicked=false;
				GuidelinesTxt.setVisible(false);
				submitBtn.setVisible(true);
				ap_exam_stats.setVisible(true);
				getGuidelinesBtn.setText("Show comments");
				
			}
		}
	}

	public void submit() {
		ArrayList<Object> parameters = new ArrayList<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		endTime = dtf.format(now).substring(11, 19);
		answerList = new ArrayList<String>(Arrays.asList(answers));

		// hide all the labels
		label_question.setVisible(false);
		option1.setVisible(false);
		option2.setVisible(false);
		option3.setVisible(false);
		option4.setVisible(false);
		next.setVisible(false);
		back.setVisible(false);
		ap_exam_stats.setVisible(false);
		submitBtn.setVisible(false);
		testSubmited.setVisible(true);
		getGuidelinesBtn.setVisible(false);
		System.out.println(answerList);

		tm.stop(); // stop the timer

		parameters.add(examInitiated);
		parameters.add(UserControl.ConnectedUser);
		parameters.add(label_timer.getText());
		parameters.add(startTime);
		parameters.add(endTime);
		parameters.add(testQuestions);
		parameters.add(answerList);

		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.ADD_DONE_EXAM, parameters,
				null, true);
		ClientControl.getInstance().accept(dataPacket);
	}

}