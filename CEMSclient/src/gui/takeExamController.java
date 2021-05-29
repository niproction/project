package gui;

import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import common.Exam;
import common.Question;
import controllers.PageProperties;
import controllers.SceneController;
import controllers.examControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class takeExamController {
	SceneController sceen;
	@FXML // fx:id="ap"
	private AnchorPane ap;
	@FXML
	private Label totalQuestion;
	@FXML
	private Label duration;
	@FXML
	private Button next;
	ArrayList<Question> testQuestions;
	@FXML
	private Label questionInfo;
	@FXML
	private Label checkLabel;

	private Exam exam;

	@FXML
	void initialize() {
		System.out.println("take exam page loaded");
		sceen = new SceneController(PageProperties.Page.TAKE_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		duration.setText(examControl.getExam().getDuration());
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(examControl.getExam());
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_TEST_QUESTIONS,
				parameters, null, true);
		System.out.println("trying to send exam");
		
		App_client.chat.accept(dataPacket);
		if (examControl.getExam() != null) {
			exam = examControl.getExam();
			examControl.setExam(null);
			testQuestions = exam.getQuestions();
			//System.out.println(testQuestions.get(0).getInfo());
		}
		else {
			System.out.println("no exammmmmm");
		}
		// questionInfo.setText(testQuestions.get(1).getInfo());
	}

	public void handleAction(MouseEvent event) {
		if (event.getSource() == next) {
			System.out.println("fdsfdsfsdafadsfdsafdsafdas");
		}

	}

}
