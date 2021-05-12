package gui;

import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import controllers.PageProperties;
import controllers.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class addNewQuestionController {
		SceneController sceen;
		@FXML // fx:id="ap"
	    private AnchorPane ap;
		@FXML
		private Button backHomebtn;
		@FXML
		private TextField questionID;
		@FXML
		private TextField questionInfotxt;
		@FXML
		private TextField option1txt;
		@FXML
		private TextField option2txt;
		@FXML
		private TextField option3txt;
		@FXML
		private TextField option4txt;
		@FXML
		private TextField answertxt;
		@FXML
		private Button saveBtn;
		@FXML
		private Button clearBtn;
	@FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'Home.fxml'.";
        sceen = new SceneController(PageProperties.Page.ADD_NEW_QUESTION, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
    }
	public void handleOnAction(MouseEvent event) {
		if(event.getSource()==backHomebtn) {
			//SceneController sceen = new SceneController(PageProperties.Page.HOME, ap);
			sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
		}
		if(event.getSource()==saveBtn) {
			save();
		}
		
	}
	public void save() {
		ArrayList<Object> parameters=new ArrayList<>();
		String id=questionID.getText().toString();
		String info=questionInfotxt.getText().toString();
		String op1=option1txt.getText().toString();
		String op2=option2txt.getText().toString();
		String op3=option3txt.getText().toString();
		String op4=option4txt.getText().toString();
		String answer=answertxt.getText().toString();
		parameters.add(id);
		parameters.add(info);
		parameters.add(op1);
		parameters.add(op2);
		parameters.add(op3);
		parameters.add(op4);
		parameters.add(answer);
		DataPacket dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.ADD_NEW_QUESTION, parameters, null, true);
		System.out.println("try create new question");
		App_client.chat.accept(dataPacket);
		SceneController sceen = new SceneController(PageProperties.Page.HomePage_Teacher, ap);
		sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
	}
	
	
	
	
	
	
	
	
	
	@FXML
    void exithover_answer_1(MouseEvent event) {

    }

    @FXML
    void exithover_answer_2(MouseEvent event) {

    }

    @FXML
    void exithover_answer_3(MouseEvent event) {

    }

    @FXML
    void exithover_answer_4(MouseEvent event) {

    }

   

    @FXML
    void hover_answer_1(MouseEvent event) {

    }

    @FXML
    void hover_answer_2(MouseEvent event) {

    }

    @FXML
    void hover_answer_3(MouseEvent event) {

    }

    @FXML
    void hover_answer_4(MouseEvent event) {

    }

    @FXML
    void set_correct_answer_1(MouseEvent event) {

    }

    @FXML
    void set_correct_answer_2(MouseEvent event) {

    }

    @FXML
    void set_correct_answer_3(MouseEvent event) {

    }

    @FXML
    void set_correct_answer_4(MouseEvent event) {

    }
	
	
	
	
	
	
}
