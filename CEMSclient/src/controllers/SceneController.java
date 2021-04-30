package controllers;

import java.io.IOException;

import controllers.FxmlSceen1.Animation;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;


public class SceneController {
    public static Stage primaryStage;
    private FxmlSceen sceenPage;
    private AnchorPane rootContainer; // the main container of the scene elements butons, box, checkbox and etc..

    public enum ANIMATE_ON{
        LOAD, UNLOAD
    };


    /* ------------------- constractors -----------------------------------*/
    
    public SceneController(FxmlSceen sceenPage) {
        this.sceenPage = sceenPage;
    }
    public SceneController(AnchorPane rootContainer) {
        this.rootContainer = rootContainer;
    }
    public SceneController(Stage primaryStage, FxmlSceen sceenPage) {
        this.primaryStage = primaryStage;
        this.sceenPage = sceenPage;
    }

    public SceneController(FxmlSceen sceenPage, AnchorPane rootContainer) {
        this.sceenPage = sceenPage;
        this.rootContainer = rootContainer;
    }





    
    public void LoadSceen(ANIMATE_ON animate) {
        animation(true, animate);
    }

    public void AnimateSceen(ANIMATE_ON animate)
    {
        animation(false, animate);
    }




    private void animation(boolean nextSceneload, ANIMATE_ON animate){
        if( (sceenPage.GET_ON_Scene_Load_Animation() == Animation.FADE_IN && animate == ANIMATE_ON.LOAD) ||  (sceenPage.GET_ON_Scene_Unload_Animation() == Animation.FADE_IN && animate == ANIMATE_ON.UNLOAD))
        {
            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(1000));
            fadeTransition.setNode(rootContainer);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            
            if(nextSceneload){
                fadeTransition.setOnFinished(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent arg0) {
                        // TODO Auto-generated method stub
                            loadNextScene();
                    }
                });
            }
            fadeTransition.play();
        }
        else if( (sceenPage.GET_ON_Scene_Load_Animation() == Animation.FADE_OUT  && animate == ANIMATE_ON.LOAD)  || (sceenPage.GET_ON_Scene_Unload_Animation() == Animation.FADE_OUT  && animate == ANIMATE_ON.UNLOAD) )
        {
            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(1000));
            fadeTransition.setNode(rootContainer);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);

            if(nextSceneload){
                fadeTransition.setOnFinished(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent arg0) {
                        // TODO Auto-generated method stub
                            loadNextScene();
                    }
                });
            }
            fadeTransition.play();
        }
        else if(sceenPage.GET_ON_Scene_Load_Animation() == Animation.SLIDE_UP){
            /// need to be changed
            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(1000));
            fadeTransition.setNode(rootContainer);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);

            if(nextSceneload){
                fadeTransition.setOnFinished(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent arg0) {
                        // TODO Auto-generated method stub
                            loadNextScene();
                    }
                });
            }
            fadeTransition.play();
        }
        else if(sceenPage.GET_ON_Scene_Load_Animation() == Animation.SLIDE_DOWN){
            /// need to be changed
            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(1000));
            fadeTransition.setNode(rootContainer);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);

            if(nextSceneload){
                fadeTransition.setOnFinished(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent arg0) {
                        // TODO Auto-generated method stub
                            loadNextScene();
                    }
                });
            }
            fadeTransition.play();
        }
        else if(sceenPage.GET_ON_Scene_Load_Animation() == Animation.SLIDE_LEFT){
            /// need to be changed
            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(1000));
            fadeTransition.setNode(rootContainer);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);

            if(nextSceneload){
                fadeTransition.setOnFinished(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent arg0) {
                        // TODO Auto-generated method stub
                            loadNextScene();
                    }
                });
            }
            fadeTransition.play();
        }
        else if(sceenPage.GET_ON_Scene_Load_Animation() == Animation.SLIDE_RIGHT){
            /// need to be changed
            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(1000));
            fadeTransition.setNode(rootContainer);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);

            if(nextSceneload){
                fadeTransition.setOnFinished(new EventHandler<ActionEvent>(){
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


    private void loadNextScene(){
        try {
            System.out.println(sceenPage.GET_FxmlFile());
            Parent root = FXMLLoader.load( getClass().getResource(sceenPage.GET_FxmlFile()) );
			Scene scene = new Scene(root);
            //primaryStage.close();
            ///primaryStage.initStyle(StageStyle.DECORATED);
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
