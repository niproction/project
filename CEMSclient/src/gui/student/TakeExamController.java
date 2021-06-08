package gui.student;

import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.Timer;

import client.App_client;
import common.DataPacket;
import common.Exam;
import common.Question;
import common.examInitiated;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import control.ExamControl;
import control.ExamInitiatedControl;
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
	private Timer skiped_time;
	private String startTime;
	private Timer initiated_time;
	private Timer exam_left_time;
	private int exam_duration;
	private int extra_time;
	private Timer exam_timer;

	

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
		//hide
		testSubmited.setVisible(false);
		
		
		
		// animate page on load
		sceen = new SceneController(PageProperties.Page.TAKE_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		
		System.out.println("take exam page loaded");
		
		
		examInitiated = ExamInitiatedControl.getExamInitiated();
		ExamInitiatedControl.setExamInitiated(null);

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(examInitiated);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_TEST_QUESTIONS,
				parameters, null, true);
		
		System.out.println("trying to send exam");

		App_client.chat.accept(dataPacket);
		// will recive the time left for the exam
		
		
		
		
		
		// exam_duration = Integer.parseInt(examInitiated.getTime());

		//minPass.setText("0");
		//secPass.setText("0");
		/*
		 * tm = new Timer(1000, new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) { counter++;
		 * Platform.runLater(new Runnable() {
		 * 
		 * @Override public void run() { if (min == exam_duration) { submit(); } if
		 * (counter == 60) { counter = 0; min++; minPass.setText(String.valueOf(min)); }
		 * secPass.setText(String.valueOf(counter)); } });
		 * 
		 * } }); tm.start()
		 */
		
		
		
		back.setVisible(false);
		

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		// System.out.println(dtf.format(now));
		//dateLabel.setText(dtf.format(now).substring(0, 10));
		startTime = dtf.format(now).substring(11, 19);
		//System.out.println(dateLabel.getText() + "---" + startTime);
		// System.out.println(date+" "+startTime);
		//duration.setText(examInitiated.getTime());
		

		

		if (ExamControl.getExam() != null) {
			System.out.println("exam is not null");
			exam = ExamControl.getExam();
			ExamControl.setExam(null);

			testQuestions = exam.getQuestions();
			answers = new String[testQuestions.size()];
			System.out.println("size :" + testQuestions.size());
			label_question.setText(testQuestions.get(0).getInfo());
			option1.setText(testQuestions.get(0).getOption1());
			option2.setText(testQuestions.get(0).getOption2());
			option3.setText(testQuestions.get(0).getOption3());
			option4.setText(testQuestions.get(0).getOption4());
			label_questions_amunt.setText("1/sda");
			//totalQuestion.setText("" + testQuestions.size());

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
			

			answers[index++] = option1.isSelected() ? "1"
					: option2.isSelected() ? "2" : option3.isSelected() ? "3" : option4.isSelected() ? "4" : "0";
			
			
			
			//Qnumber.setText(index + 1 + "");
			
			
			
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
			
			label_questions_amunt.setText(index + 1 + "");
		}
		if (event.getSource() == submitBtn) {
			answers[index] = option1.isSelected() ? "1" : option2.isSelected() ? "2" : option3.isSelected() ? "3" : "4";
			submit();
		}

	}

	public void submit() {
		ArrayList<Object> parameters = new ArrayList<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		endTime = dtf.format(now).substring(11, 19);
		answerList = new ArrayList<String>(Arrays.asList(answers));
		
		//hide all the labels
		label_question.setVisible(false);
		option1.setVisible(false);
		option2.setVisible(false);
		option3.setVisible(false);
		option4.setVisible(false);
		next.setVisible(false);
		back.setVisible(false);
		submitBtn.setVisible(false);
		testSubmited.setVisible(true);
		
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
		App_client.chat.accept(dataPacket);
	}

}