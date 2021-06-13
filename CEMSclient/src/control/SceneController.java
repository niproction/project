package control;

import java.io.IOException;
import java.net.URL;

import javax.swing.Timer;
import javax.swing.text.View;

import control.PageProperties.Animation;
import control.PageProperties.Page;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneController {
	public static Stage primaryStage;
	private Page sceenPage;
	private AnchorPane rootContainer; // the main container of the scene elements butons, box, checkbox and etc..
	// private static AnchorPane view;

	private Pane element;

	// rostik v10
	private static Timer InsidePageTimerThraed = null;

	public static Timer getInsidePageTimerThraed() {
		return InsidePageTimerThraed;
	}

	public static void setInsidePageTimerThraed(Timer insidePageTimerThraed) {
		InsidePageTimerThraed = insidePageTimerThraed;
	}

	public enum ANIMATE_ON {
		LOAD, UNLOAD
	};

	/* ------------------- constractors ----------------------------------- */

	public SceneController(Page sceenPage) {
		this.sceenPage = sceenPage;
	}

	public SceneController(AnchorPane rootContainer) {
		this.rootContainer = rootContainer;
	}

	public SceneController(Stage primaryStage, Page sceenPage) {
		this.primaryStage = primaryStage;
		this.sceenPage = sceenPage;
	}

	public SceneController(Page sceenPage, AnchorPane rootContainer) {
		this.sceenPage = sceenPage;
		this.rootContainer = rootContainer;
	}

	public SceneController(Pane element) {
		this.element = element;
	}

	public void LoadSceen(ANIMATE_ON animate) {
		animation(true, animate);
	}

	public void AnimateSceen(ANIMATE_ON animate) {
		animation(false, animate);
	}

	public void AnimateElement(ANIMATE_ON animate) {
		animation(false, animate);
	}

	public static AnchorPane getPage(Page page) {
		AnchorPane view = null;
		// rostik v10
		// close inside page time if there is any that runnig
		if (InsidePageTimerThraed != null) {
			InsidePageTimerThraed.stop();
			InsidePageTimerThraed = null;
		}

		try {
			URL file = LoadInsidePage.class.getResource(page.GET_FxmlFile());
			if (file == null) {
				throw new java.io.FileNotFoundException("file not found");
			}
			view = new FXMLLoader().load(file);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("No page found");
			e.printStackTrace();
		}

		return view;
	}

	private void animation(boolean nextSceneload, ANIMATE_ON animate) {
		if ((sceenPage.GET_ON_Scene_Load_Animation() == Animation.FADE_IN && animate == ANIMATE_ON.LOAD)
				|| (sceenPage.GET_ON_Scene_Unload_Animation() == Animation.FADE_IN && animate == ANIMATE_ON.UNLOAD)) {
			FadeTransition fadeTransition = new FadeTransition();
			fadeTransition.setDuration(Duration.millis(1000));
			fadeTransition.setNode(rootContainer);
			fadeTransition.setFromValue(0);
			fadeTransition.setToValue(1);

			if (nextSceneload) {
				fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						loadNextScene();
					}
				});
			}
			fadeTransition.play();
		} else if ((sceenPage.GET_ON_Scene_Load_Animation() == Animation.FADE_OUT && animate == ANIMATE_ON.LOAD)
				|| (sceenPage.GET_ON_Scene_Unload_Animation() == Animation.FADE_OUT && animate == ANIMATE_ON.UNLOAD)) {
			FadeTransition fadeTransition = new FadeTransition();
			fadeTransition.setDuration(Duration.millis(1000));
			fadeTransition.setNode(rootContainer);
			fadeTransition.setFromValue(1);
			fadeTransition.setToValue(0);

			if (nextSceneload) {
				fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						loadNextScene();
					}
				});
			}
			fadeTransition.play();
		} else if (sceenPage.GET_ON_Scene_Load_Animation() == Animation.SLIDE_UP) {
			/// need to be changed
			FadeTransition fadeTransition = new FadeTransition();
			fadeTransition.setDuration(Duration.millis(1000));
			fadeTransition.setNode(rootContainer);
			fadeTransition.setFromValue(1);
			fadeTransition.setToValue(0);

			if (nextSceneload) {
				fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						loadNextScene();
					}
				});
			}
			fadeTransition.play();
		} else if (sceenPage.GET_ON_Scene_Load_Animation() == Animation.SLIDE_DOWN) {
			/// need to be changed
			FadeTransition fadeTransition = new FadeTransition();
			fadeTransition.setDuration(Duration.millis(1000));
			fadeTransition.setNode(rootContainer);
			fadeTransition.setFromValue(1);
			fadeTransition.setToValue(0);

			if (nextSceneload) {
				fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						loadNextScene();
					}
				});
			}
			fadeTransition.play();
		} else if (sceenPage.GET_ON_Scene_Load_Animation() == Animation.SLIDE_LEFT) {
			/// need to be changed
			FadeTransition fadeTransition = new FadeTransition();
			fadeTransition.setDuration(Duration.millis(1000));
			fadeTransition.setNode(rootContainer);
			fadeTransition.setFromValue(1);
			fadeTransition.setToValue(0);

			if (nextSceneload) {
				fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						loadNextScene();
					}
				});
			}
			fadeTransition.play();
		} else if (sceenPage.GET_ON_Scene_Load_Animation() == Animation.SLIDE_RIGHT) {
			/// need to be changed
			FadeTransition fadeTransition = new FadeTransition();
			fadeTransition.setDuration(Duration.millis(1000));
			fadeTransition.setNode(rootContainer);
			fadeTransition.setFromValue(1);
			fadeTransition.setToValue(0);

			if (nextSceneload) {
				fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						loadNextScene();
					}
				});
			}
			fadeTransition.play();
		}
	}

	private void loadNextScene() {
		// rostik v10
		// close inside page time if there is any that runnig
		if (InsidePageTimerThraed != null) {
			InsidePageTimerThraed.stop();
			InsidePageTimerThraed = null;
		}
		try {
			System.out.println(sceenPage.GET_FxmlFile());
			Parent root = FXMLLoader.load(getClass().getResource(sceenPage.GET_FxmlFile()));
			Scene scene = new Scene(root);
			// primaryStage.close();
			/// primaryStage.initStyle(StageStyle.DECORATED);
			primaryStage.setTitle(sceenPage.GET_Title());
			primaryStage.setScene(scene);
			System.out.println("opened new sceen");
			primaryStage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
