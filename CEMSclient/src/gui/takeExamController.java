package gui;

import client.App_client;
import common.Exam;
import controllers.PageProperties;
import controllers.SceneController;
import controllers.examControl;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class takeExamController {
	SceneController sceen;
	@FXML // fx:id="ap"
    private AnchorPane ap;
	
	private Exam exam;
	
	@FXML
	void initialize() {
		sceen = new SceneController(PageProperties.Page.TAKE_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		exam=examControl.getExam();
		examControl.setExam(null);
		
	}
	

}
