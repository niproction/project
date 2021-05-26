package gui;

import client.App_client;
import common.Exam;
import controllers.PageProperties;
import controllers.SceneController;
import controllers.examControl;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class takeExamController {
	SceneController sceen;
	@FXML // fx:id="ap"
    private AnchorPane ap;
	@FXML
	private Label totalQuestion;
	@FXML
	private Label duration;
	
	
	private Exam exam;
	
	@FXML
	void initialize() {
		exam=examControl.getExam();
		examControl.setExam(null);
		duration.setText(exam.getDuration());
		
	}
	
	

}
