package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.App_client;
import common.Principal;
import common.Student;
import common.Teacher;
import controllers.PageProperties;
import controllers.SceneController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class mainController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="button_menu"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="button_menu"
	private ImageView button_menu; // Value injected by FXMLLoader

	@FXML
	private Label label_bar_welcome;

	@FXML
	private Label label_bar_roletype;

	@FXML
	private BorderPane page_box;

	@FXML
	private Pane box_left;
	@FXML
	private Pane box_right;

	@FXML // This method is called sby the FXMLLoader when initialization is complete
	void initialize() {
		// animate page on load
		SceneController sceen = new SceneController(PageProperties.Page.HOME, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);

		ChangeListener<Number> listener = new ChangeListener<Number>() {
			private Point2D stageSize = null;
			private Point2D previousStageSize = new Point2D(SceneController.primaryStage.getWidth(),
					SceneController.primaryStage.getHeight());

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if (stageSize == null) {
					Platform.runLater(() -> {
						System.out.printf("Old: (%.1f, %.1f); new: (%.1f, %.1f)%n", previousStageSize.getX(),
								previousStageSize.getY(), stageSize.getX(), stageSize.getY());
						previousStageSize = stageSize;
						box_left.setMinSize((stageSize.getX() - 800) / 2, 100);
						box_right.setMinSize((stageSize.getX() - 800) / 2, 100);

						stageSize = null;
					});

				}
				stageSize = new Point2D(SceneController.primaryStage.getWidth(),
						SceneController.primaryStage.getHeight());
			}
		};

		SceneController.primaryStage.widthProperty().addListener(listener);
		SceneController.primaryStage.heightProperty().addListener(listener);

		if (App_client.user instanceof Student) {
			label_bar_welcome.setText("Welcome back" + App_client.user.GET_firstName()+" "+App_client.user.GET_lastName());
			label_bar_roletype.setText("(Student)");
		} else if (App_client.user instanceof Teacher) {
			label_bar_welcome.setText("Welcome back" + App_client.user.GET_firstName()+" "+App_client.user.GET_lastName());
			label_bar_roletype.setText("(Teacher)");
		} else if (App_client.user instanceof Principal) {
			label_bar_welcome.setText("Welcome back" + App_client.user.GET_firstName()+" "+App_client.user.GET_lastName());
			label_bar_roletype.setText("(Principal)");
		}
	}

	@FXML
	void button_menu_clicked(MouseEvent event) {
		System.out.println("clicked");
		Pane page = SceneController.getPage(PageProperties.Page.ADD_NEW_QUESTION);
		// Pane screen = object.Sc();
		page_box.setCenter(page);
		System.out.println(page_box.getCenter());
		SceneController.primaryStage.setMinWidth(800);
		SceneController.primaryStage.setMinHeight(700);
	}

	@FXML
	void button_menu_enterd(MouseEvent event) {
		System.out.println("entered");
		// button_menu.
	}

	@FXML
	void button_menu_exited(MouseEvent event) {
		System.out.println("exited");
	}

	@FXML
	void button_logout_clicked(MouseEvent event) {
		App_client.user = null;

		// make animation and than load page
		SceneController sceen = new SceneController(PageProperties.Page.LOGIN, ap);
		sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
	}

	@FXML
	void button_logout_entered(MouseEvent event) {

	}

	@FXML
	void button_logout_exited(MouseEvent event) {

	}

}
