package gui;

import java.util.ArrayList;

import javax.swing.event.ChangeListener;

import client.App_client;
import common.DataPacket;
import common.Exam;
import controllers.PageProperties;
import controllers.SceneController;
import controllers.examControl;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

public class editExamController {
	SceneController sceen;
	@FXML // fx:id="ap"
    private AnchorPane ap;
	@FXML
	private ChoiceBox<String> examID=new ChoiceBox<>();
	private ArrayList<Exam> examList;
	
	
	@FXML
	public void initialize() {
		ap.setMaxWidth(0);
		sceen=new SceneController(PageProperties.Page.EDIT_EXAM, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		ArrayList<Object> parameters=new ArrayList<>();
		parameters.add(App_client.user);
		DataPacket dataPacket=new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.EDIT_EXAM, parameters, null, true);
		App_client.chat.accept(dataPacket);
		if(examControl.getExamslist()!=null) {
			examList=examControl.getExamslist();
			examControl.setExamslist(null);
			ObservableList<String> exams=FXCollections.observableArrayList();
			for( Exam i:examList) 
				exams.add(i.getExamID());
			examID.setItems(exams);
			examID.setOnAction((event) -> {
			    int selectedIndex = examID.getSelectionModel().getSelectedIndex();
			    Object selectedItem = examID.getSelectionModel().getSelectedItem();

			    System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
			    System.out.println("   ChoiceBox.getValue(): " + examID.getValue());
			    
			});
			
		}
	
	}

}
