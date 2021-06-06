package gui;

import client.App_client;
import controllers.PageProperties;
import controllers.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class getExamType {
	SceneController sceen;
	@FXML // fx:id="ap"
	private AnchorPane ap;
	@FXML
	private Button wordExamBtn;
	@FXML
	private Button onlineExamBtn;

	@FXML
	public void initialize() {

		sceen = new SceneController(PageProperties.Page.CHOSSE_EXAM_TYPE, ap);
		sceen.AnimateElement(SceneController.ANIMATE_ON.LOAD);
	}

	public void handleOnAction(MouseEvent event) {
		if (event.getSource() == wordExamBtn) {
			AnchorPane page = SceneController.getPage(PageProperties.Page.Create_MANUEL_EXAM);
			App_client.pageContainer.setCenter(page);
		} else if (event.getSource() == onlineExamBtn) {
			AnchorPane page = SceneController.getPage(PageProperties.Page.CREATE_EXAM);
			App_client.pageContainer.setCenter(page);
		}

	}
}
