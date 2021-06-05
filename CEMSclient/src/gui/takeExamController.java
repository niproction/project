package gui;

import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.Timer;

import client.App_client;
import common.DataPacket;
import common.Exam;
import common.Question;
import common.examInitiated;
import controllers.PageProperties;
import controllers.SceneController;
import controllers.examControl;
import controllers.examInitiatedControl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.awt.event.ActionEvent;

public class takeExamController {
	SceneController sceen;
	@FXML // fx:id="ap"
	private AnchorPane ap;
	@FXML
	private Label totalQuestion;
	@FXML
	private Label duration;
	@FXML
	private Label Qnumber;
	@FXML
	private Label testSubmited;
	@FXML
	private Button next;
	@FXML
	private Button back;
	ArrayList<Question> testQuestions;
	String answers[];
	List<String> answerList;
	@FXML
	private Label questionInfo;
	@FXML
	private CheckBox option1;
	@FXML
	private CheckBox option2;
	@FXML
	private CheckBox option3;
	@FXML
	private CheckBox option4;
	@FXML
	private Button submitBtn;
	@FXML
	private Label minPass;
	@FXML
	private Label secPass;
	@FXML
	private Label dateLabel;
	@FXML
	private ImageView backImageView;
	@FXML
	private ImageView nextImageView;
	@FXML
	private ImageView submotImageView;
	private Exam exam;
	private examInitiated examInitiated;
	private int min, sec, index;
	private String startTime, endTime;
	private int timeLeft, counter;
	private Timer tm;
	@FXML
	void initialize() {
		System.out.println("take exam page loaded");
		sceen = new SceneController(PageProperties.Page.TAKE_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		examInitiated=examInitiatedControl.getExamInitiated();
		examInitiatedControl.setExamInitiated(null);
		int dur = Integer.parseInt(examInitiated.getTime());
		minPass.setText("0");
		tm = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				counter++;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (min == dur) {
							submit();
						}
						if (counter == 60) {
							counter = 0;
							min++;
							minPass.setText(String.valueOf(min));
						}
						secPass.setText(String.valueOf(counter));
					}
				});

			}
		});
		tm.start();
		testSubmited.setVisible(false);
		back.setVisible(false);
		backImageView.setVisible(false);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		// System.out.println(dtf.format(now));
		dateLabel.setText(dtf.format(now).substring(0, 10));
		startTime = dtf.format(now).substring(11, 19);
		System.out.println(dateLabel.getText() + "---" + startTime);
		// System.out.println(date+" "+startTime);
		duration.setText(examInitiated.getTime());
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(examInitiated);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_TEST_QUESTIONS,
				parameters, null, true);
		System.out.println("trying to send exam");

		App_client.chat.accept(dataPacket);
		if (examControl.getExam() != null) {
			System.out.println("exam is not null");
			exam = examControl.getExam();
			examControl.setExam(null);

			testQuestions = exam.getQuestions();
			answers = new String[testQuestions.size()];

			questionInfo.setText(testQuestions.get(0).getInfo());
			option1.setText(testQuestions.get(0).getOption1());
			option2.setText(testQuestions.get(0).getOption2());
			option3.setText(testQuestions.get(0).getOption3());
			option4.setText(testQuestions.get(0).getOption4());
			Qnumber.setText("1");
			totalQuestion.setText("" + testQuestions.size());

			//////////////////////////////////////////////////

		} else {
			System.out.println("no exammmmmm");
		}
		// questionInfo.setText(testQuestions.get(1).getInfo());
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
			backImageView.setVisible(true);
			answers[index++] = option1.isSelected() ? "1"
					: option2.isSelected() ? "2" : option3.isSelected() ? "3" : option4.isSelected() ? "4" : "0";
			Qnumber.setText(index + 1 + "");
			if (index + 1 == testQuestions.size()) {
				next.setVisible(false);
				nextImageView.setVisible(false);
			}
			questionInfo.setText(testQuestions.get(index).getInfo());
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
			System.out.println(answers.length);

		}
		if (event.getSource() == back) {
			next.setVisible(true);
			nextImageView.setVisible(true);
			index--;
			// System.out.println(index);
			option1.setSelected(answers[index] == "1");
			option2.setSelected(answers[index] == "2");
			option3.setSelected(answers[index] == "3");
			option4.setSelected(answers[index] == "4");
			if (index == 0) {
				back.setVisible(false);
				backImageView.setVisible(false);
			}
			questionInfo.setText(testQuestions.get(index).getInfo());
			option1.setText(testQuestions.get(index).getOption1());
			option2.setText(testQuestions.get(index).getOption2());
			option3.setText(testQuestions.get(index).getOption3());
			option4.setText(testQuestions.get(index).getOption4());
			Qnumber.setText(index + 1 + "");
		}
		if (event.getSource() == submitBtn) {
			answers[index] = option1.isSelected() ? "1" : option2.isSelected() ? "2" : option3.isSelected() ? "3" : "4";
			submit();
		}

	}

	public void submit() {
		ArrayList<Object> parameters=new ArrayList<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		endTime = dtf.format(now).substring(11, 19);
		answerList = new ArrayList<String>(Arrays.asList(answers));
		questionInfo.setVisible(false);
		option1.setVisible(false);
		option2.setVisible(false);
		option3.setVisible(false);
		option4.setVisible(false);
		next.setVisible(false);
		nextImageView.setVisible(false);
		back.setVisible(false);
		backImageView.setVisible(false);
		submitBtn.setVisible(false);
		submotImageView.setVisible(false);
		testSubmited.setVisible(true);
		System.out.println(answerList);
		tm.stop();
		parameters.add(examInitiated);
		parameters.add(App_client.user);
		parameters.add(duration.getText());
		parameters.add(startTime);
		parameters.add(endTime);
		parameters.add(testQuestions);
		parameters.add(answerList);
	
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.ADD_DONE_EXAM, parameters,
				null, true);
		App_client.chat.accept(dataPacket);
	}

}
