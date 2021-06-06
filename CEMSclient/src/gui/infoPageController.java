/**
 * Sample Skeleton for 'infoPage.fxml' Controller Class
 */

package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import common.Exam;
import common.Field;
import common.Question;
import common.User;
import common.courses;
import controllers.CourseControl;
import controllers.FieldControl;
import controllers.PageProperties;
import controllers.QuestionControl;
import controllers.SceneController;
import controllers.UserControl;
import controllers.examControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class infoPageController {
	SceneController scene;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="ap"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="ComboBox"
	private ComboBox<String> comboBox; // Value injected by FXMLLoader

	@FXML
	private TableView<TableEntry> tableOfInfo;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'infoPage.fxml'.";
		scene = new SceneController(PageProperties.Page.INFO_PAGE, ap);
		scene.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("select", "users", "courses", "fields", "exams","questions");
		comboBox.getSelectionModel().select("select");
	}
	//this method is called when the user chooses what info he want to watch
	@FXML
	void OnChoose(ActionEvent event) {
		ObservableList<TableEntry> data = FXCollections.observableArrayList();
		tableOfInfo.getColumns().clear();
		System.out.println("changed to" + comboBox.getValue());
		DataPacket dataPacket = null;
		TableColumn<TableEntry, ?> tc1 = null;
		TableColumn<TableEntry, ?> tc2 = null;
		TableColumn<TableEntry, ?> tc3 = null;
		TableColumn<TableEntry, ?> tc4 = null;
		TableColumn<TableEntry, ?> tc5 = null;
		TableColumn<TableEntry, ?> tc6 = null;
		TableColumn<TableEntry, ?> tc7 = null;
		TableColumn<TableEntry, ?> tc8 = null;

		System.out.println(comboBox.getValue() + "fdsafafsafsfafaa");
		switch (comboBox.getValue()) {
		//This is the code when the user selcets "users" it shows the user a table of all the user in the system
		case "users":
			tc1 = new TableColumn<>("user id");
			tc2 = new TableColumn<>("username");
			tc3 = new TableColumn<>("password");
			tc4 = new TableColumn<>("email");
			tc5 = new TableColumn<>("firstName");
			tc6 = new TableColumn<>("lastName");
			tc7 = new TableColumn<>("ID");
			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_USERS, null, null, true);
			App_client.chat.accept(dataPacket);// send and wait for response from server
			tc1.setCellValueFactory(new PropertyValueFactory("col1"));
			tc2.setCellValueFactory(new PropertyValueFactory("col2"));
			tc3.setCellValueFactory(new PropertyValueFactory("col3"));
			tc4.setCellValueFactory(new PropertyValueFactory("col4"));
			tc5.setCellValueFactory(new PropertyValueFactory("col5"));
			tc6.setCellValueFactory(new PropertyValueFactory("col6"));
			tc7.setCellValueFactory(new PropertyValueFactory("col7"));
			tableOfInfo.getColumns().addAll(tc1, tc2, tc3, tc4, tc5, tc6, tc7);
			for (User tmpUser : UserControl.user) {
				data.add(new TableEntry(tmpUser.getuid(), tmpUser.getUsername(), tmpUser.getPassword(),
						tmpUser.getEmail(), tmpUser.getFirstName(), tmpUser.getLastName(), tmpUser.getfid()));
			}
			break;
		case "courses":
			tc1 = new TableColumn<>("Course Id");
			tc2 = new TableColumn<>("Field Id");
			tc3 = new TableColumn<>("Course Name");
			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_COURSE, null, null, true);
			App_client.chat.accept(dataPacket);// send and wait for response from server
			tc1.setCellValueFactory(new PropertyValueFactory("col1"));
			tc2.setCellValueFactory(new PropertyValueFactory("col2"));
			tc3.setCellValueFactory(new PropertyValueFactory("col3"));
			tableOfInfo.getColumns().addAll(tc1, tc2, tc3);
			for (courses tmpCourse : CourseControl.courses)
				data.add(new TableEntry(tmpCourse.getCourseID(), tmpCourse.getFieldID(), tmpCourse.getCourseName()));
			break;

		case "fields":
			tc1 = new TableColumn<>("Field ID");
			tc2 = new TableColumn<>("Field Name");
			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_FIELD, null, null, true);
			App_client.chat.accept(dataPacket);// send and wait for response from server
			tc1.setCellValueFactory(new PropertyValueFactory("col1"));
			tc2.setCellValueFactory(new PropertyValueFactory("col2"));
			tableOfInfo.getColumns().addAll(tc1, tc2);
			for (Field tmpField : FieldControl.fields)
				data.add(new TableEntry(tmpField.getFiledId(), tmpField.getFiledName()));
			break;

		case "exams":
			tc1 = new TableColumn<>("exam ID");
			tc2 = new TableColumn<>("author ID");
			tc3 = new TableColumn<>("description");
			tc4 = new TableColumn<>("duration");
			tc5 = new TableColumn<>("teacher comment");
			tc6 = new TableColumn<>("student comment");
			tc7 = new TableColumn<>("questions");

			ArrayList<Button> openQuestionsButtonList =new ArrayList<Button>();
					//new Button("Show Questions");

			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_EXAM, null, null, true);
			App_client.chat.accept(dataPacket);// send and wait for response from server
			tc1.setCellValueFactory(new PropertyValueFactory("col1"));
			tc2.setCellValueFactory(new PropertyValueFactory("col2"));
			tc3.setCellValueFactory(new PropertyValueFactory("col3"));
			tc4.setCellValueFactory(new PropertyValueFactory("col4"));
			tc5.setCellValueFactory(new PropertyValueFactory("col5"));
			tc6.setCellValueFactory(new PropertyValueFactory("col6"));
			tc7.setCellValueFactory(new PropertyValueFactory("col7"));

			tableOfInfo.getColumns().addAll(tc1, tc2, tc3, tc4, tc5, tc6, tc7);
			
			for (Exam tmpExam : examControl.exams) {
				openQuestionsButtonList.add(new Button("Show Questions"));
				EventHandler<ActionEvent> openTheQuestions = new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						tableOfInfo.getColumns().clear();
						TableColumn<TableEntry, ?> tc1 = new TableColumn<>("question ID");
						TableColumn<TableEntry, ?> tc2 = new TableColumn<>("author ID");
						TableColumn<TableEntry, ?> tc3 = new TableColumn<>("question");
						TableColumn<TableEntry, ?> tc4 = new TableColumn<>("option 1");
						TableColumn<TableEntry, ?> tc5 = new TableColumn<>("option 2");
						TableColumn<TableEntry, ?> tc6 = new TableColumn<>("option 3");
						TableColumn<TableEntry, ?> tc7 = new TableColumn<>("option 4");
						TableColumn<TableEntry, ?> tc8 = new TableColumn<>("answer");
						tc1.setCellValueFactory(new PropertyValueFactory("col1"));
						tc2.setCellValueFactory(new PropertyValueFactory("col2"));
						tc3.setCellValueFactory(new PropertyValueFactory("col3"));
						tc4.setCellValueFactory(new PropertyValueFactory("col4"));
						tc5.setCellValueFactory(new PropertyValueFactory("col5"));
						tc6.setCellValueFactory(new PropertyValueFactory("col6"));
						tc7.setCellValueFactory(new PropertyValueFactory("col7"));
						tc8.setCellValueFactory(new PropertyValueFactory("col8"));
						ObservableList<TableEntry> data1 = FXCollections.observableArrayList();						
						tableOfInfo.getColumns().addAll(tc1, tc2, tc3, tc4, tc5, tc6, tc7, tc8);
						for (Question questionInExam : tmpExam.getQuestions()) {
							data1.add(new TableEntry(questionInExam.getId(), questionInExam.getAuthorID(),
									questionInExam.getInfo(), questionInExam.getOption1(), questionInExam.getOption2(),questionInExam.getOption3(), questionInExam.getOption4(),	
									questionInExam.getAnswer()));
						}
						tableOfInfo.setItems(data1);
					}
				};
								
				openQuestionsButtonList.get(openQuestionsButtonList.size()-1).setOnAction(openTheQuestions);
				
				
				data.add(new TableEntry(tmpExam.getExamID(), tmpExam.getAuthorID(), tmpExam.getDescription(),
						tmpExam.getDuration(), tmpExam.getTeacherComments(), tmpExam.getStudentsComments(),
						openQuestionsButtonList.get(openQuestionsButtonList.size()-1)));		
			}
				break;

			
		case "questions":
			
			dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_QUESTIONS, null, null, true);
			App_client.chat.accept(dataPacket);// send and wait for response from server
			
			
			tc1 = new TableColumn<>("question id");
			tc2 = new TableColumn<>("author ID");
			tc3 = new TableColumn<>("question:");
			tc4 = new TableColumn<>("option 1");
			tc5 = new TableColumn<>("option 2");
			tc6 = new TableColumn<>("option 3");
			tc7 = new TableColumn<>("option 4");
			tc8 = new TableColumn<>("the answer number is");
			tc1.setCellValueFactory(new PropertyValueFactory("col1"));
			tc2.setCellValueFactory(new PropertyValueFactory("col2"));
			tc3.setCellValueFactory(new PropertyValueFactory("col3"));
			tc4.setCellValueFactory(new PropertyValueFactory("col4"));
			tc5.setCellValueFactory(new PropertyValueFactory("col5"));
			tc6.setCellValueFactory(new PropertyValueFactory("col6"));
			tc7.setCellValueFactory(new PropertyValueFactory("col7"));
			tc8.setCellValueFactory(new PropertyValueFactory("col8"));

			tableOfInfo.getColumns().addAll(tc1, tc2, tc3, tc4, tc5, tc6, tc7,tc8);
			
			
			
			for (Question tmpQuestion : QuestionControl.questions) {
				data.add(new TableEntry(tmpQuestion.getId(), tmpQuestion.getAuthorID(), tmpQuestion.getInfo(),
						tmpQuestion.getOption1(), tmpQuestion.getOption2(), tmpQuestion.getOption3(), tmpQuestion.getOption4(),tmpQuestion.getAnswer()));
			}

			break;
		default:

		}
		tableOfInfo.setItems(data);

	}
}
