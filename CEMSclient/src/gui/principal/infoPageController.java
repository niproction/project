/**
 * Sample Skeleton for 'infoPage.fxml' Controller Class
 */

package gui.principal;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import common.Exam;
import common.Field;
import common.Question;
import common.User;
import common.Course;
import control.ClientControl;
import control.CourseControl;
import control.FieldControl;
import control.PageProperties;
import control.QuestionControl;
import control.SceneController;
import control.UserControl;
import gui.TableEntry;
import control.ExamControl;
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
    private Button backButton;
	
	@FXML
	private TableView<TableEntry> tableOfInfo;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'infoPage.fxml'.";
		scene = new SceneController(PageProperties.Page.INFO_PAGE, ap);
		scene.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("select", "users", "courses", "fields", "exams", "questions");
		comboBox.getSelectionModel().select("select");
		backButton.setVisible(false);
	}

	// this method is called when the user chooses what info he want to watch
	@SuppressWarnings("unchecked")
	@FXML
	void OnChoose(ActionEvent event) {
		ObservableList<TableEntry> data = FXCollections.observableArrayList();
		tableOfInfo.getColumns().clear();
		System.out.println("changed to" + comboBox.getValue());
		DataPacket dataPacket = null;
		ArrayList<TableColumn<TableEntry, ?>> tc = new ArrayList<TableColumn<TableEntry, ?>>();
		ArrayList<String> columnsNames = new ArrayList<String>();

		// when the user selects one of the option of the comboBox
		// it will enter the specific mathod that depends on the options
		switch (comboBox.getValue()) {
		// all the user in the system
		case "users":
			generateUserTable(tc, data, dataPacket, columnsNames);
			break;
		case "courses":
			generateCourseTable(tc, data, dataPacket, columnsNames);
			break;
		case "fields":
			generateFieldTable(tc, data, dataPacket, columnsNames);
			break;
		case "exams":
			generateExamTable(tc, data, dataPacket, columnsNames);
			break;
		case "questions":
			generateQuestionTable(tc, data, dataPacket, columnsNames);
			break;
		}
		tableOfInfo.setItems(data);
	}

	void generateUserTable(ArrayList<TableColumn<TableEntry, ?>> tc, ObservableList<TableEntry> data,
			DataPacket dataPacket, ArrayList<String> columnsNames) {
		String[] columnsNamesArray = { "user id", "username", "password", "email", "firstName", "lastName", "ID" };
		for (String tmp : columnsNamesArray) {
			backButton.setVisible(false);

			columnsNames.add(tmp);
		}
		buildColumns(tc, columnsNames);

		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_USERS, null, null, true);
		ClientControl.getInstance().accept(dataPacket);// send and wait for response from server
		for (User tmpUser : UserControl.user) {
			data.add(new TableEntry(tmpUser.getuID(), tmpUser.getUsername(), tmpUser.getPassword(), tmpUser.getEmail(),
					tmpUser.getFirstName(), tmpUser.getLastName(), tmpUser.getfid()));

		}
	}

	void generateCourseTable(ArrayList<TableColumn<TableEntry, ?>> tc, ObservableList<TableEntry> data,
			DataPacket dataPacket, ArrayList<String> columnsNames) {
		backButton.setVisible(false);

		String[] columnsNamesArray = { "Course Id", "Field Id", "Course Name" };
		for (String tmp : columnsNamesArray) {
			columnsNames.add(tmp);
		}
		buildColumns(tc, columnsNames);

		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_COURSE, null, null, true);
		ClientControl.getInstance().accept(dataPacket);// send and wait for response from server

		for (Course tmpCourse : CourseControl.courses)
			data.add(new TableEntry(tmpCourse.getCourseID(), tmpCourse.getFieldID(), tmpCourse.getCourseName()));
	}

	void generateFieldTable(ArrayList<TableColumn<TableEntry, ?>> tc, ObservableList<TableEntry> data,
			DataPacket dataPacket, ArrayList<String> columnsNames) {
		backButton.setVisible(false);

		String[] columnsNamesArray = { "Field ID", "Field Name" };
		for (String tmp : columnsNamesArray) {
			columnsNames.add(tmp);
		}
		buildColumns(tc, columnsNames);
		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_FIELD, null, null, true);
		ClientControl.getInstance().accept(dataPacket);// send and wait for response from server

		for (Field tmpField : FieldControl.fields)
			data.add(new TableEntry(tmpField.getFiledId(), tmpField.getFiledName()));
	}

	void generateExamTable(ArrayList<TableColumn<TableEntry, ?>> tc, ObservableList<TableEntry> data,
			DataPacket dataPacket, ArrayList<String> columnsNames) {
		backButton.setVisible(false);
		String[] columnsNamesArray = { "exam ID", "author ID", "description", "duration", "teacher comment",
				"student comment", "questions" };
		for (String tmp : columnsNamesArray) {
			columnsNames.add(tmp);
		}
		buildColumns(tc, columnsNames);

		ArrayList<Button> openQuestionsButtonList = new ArrayList<Button>();
		// new Button("Show Questions");

		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_EXAM, null, null, true);
		ClientControl.getInstance().accept(dataPacket);// send and wait for response from server

		for (Exam tmpExam : ExamControl.exams) {
			openQuestionsButtonList.add(new Button("Show Questions"));

			//goes to the questions of this exam
			EventHandler<ActionEvent> openTheQuestions = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					backButton.setVisible(true);				

					tableOfInfo.getColumns().clear();
					ArrayList<TableColumn<TableEntry, ?>> tc1 = new ArrayList<TableColumn<TableEntry, ?>>();
					ArrayList<String> columnsNames = new ArrayList<String>();
					String[] columnsNamesArray = { "question id", "author ID", "question", "option 1", "option 2",
							"option 3", "option 4", "the answer number is" };
					for (String tmp : columnsNamesArray) {
						columnsNames.add(tmp);
					}
					buildColumns(tc1, columnsNames);
					ObservableList<TableEntry> data1 = FXCollections.observableArrayList();
					for (Question questionInExam : tmpExam.getQuestions()) {
						data1.add(new TableEntry(questionInExam.getqID(), questionInExam.getAuthorID(),
								questionInExam.getInfo(), questionInExam.getOption1(), questionInExam.getOption2(),
								questionInExam.getOption3(), questionInExam.getOption4(), questionInExam.getAnswer()));
					}
					tableOfInfo.setItems(data1);
				}
			};

			openQuestionsButtonList.get(openQuestionsButtonList.size() - 1).setOnAction(openTheQuestions);

			data.add(new TableEntry(tmpExam.getExamID(), tmpExam.getAuthorID(), tmpExam.getDescription(),
					tmpExam.getDuration(), tmpExam.getTeacherComments(), tmpExam.getStudentsComments(),
					openQuestionsButtonList.get(openQuestionsButtonList.size() - 1)));
		}
	}

	void generateQuestionTable(ArrayList<TableColumn<TableEntry, ?>> tc, ObservableList<TableEntry> data,
			DataPacket dataPacket, ArrayList<String> columnsNames) {
		backButton.setVisible(false);

		dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_INFO_QUESTIONS, null, null, true);
		ClientControl.getInstance().accept(dataPacket);// send and wait for response from server
		String[] columnsNamesArray = { "question id", "author ID", "question", "option 1", "option 2", "option 3",
				"option 4", "the answer number is" };

		for (String tmp : columnsNamesArray) {
			columnsNames.add(tmp);
		}
		buildColumns(tc, columnsNames);

		for (Question tmpQuestion : QuestionControl.getQuestions()) {
			data.add(new TableEntry(tmpQuestion.getqID(), tmpQuestion.getAuthorID(), tmpQuestion.getInfo(),
					tmpQuestion.getOption1(), tmpQuestion.getOption2(), tmpQuestion.getOption3(),
					tmpQuestion.getOption4(), tmpQuestion.getAnswer()));
		}

	}

	void buildColumns(ArrayList<TableColumn<TableEntry, ?>> tc, ArrayList<String> columnsNames) {
		int i = 0;
		int j = 1;
		for (String tmp : columnsNames) {
			tc.add(new TableColumn<>(tmp));
			tc.get(i).setCellValueFactory(new PropertyValueFactory("col" + j));
			tableOfInfo.getColumns().add(tc.get(i));
			i++;
			j++;
		}

	}
	
    @FXML
    void goBackToExamTable(ActionEvent event) {
    	ObservableList<TableEntry> data2 = FXCollections.observableArrayList();
		ArrayList<String> columnsNames1 = new ArrayList<String>();
		tableOfInfo.getColumns().clear();
		backButton.setVisible(false);
		DataPacket dataPacket = new DataPacket();
		ArrayList<TableColumn<TableEntry, ?>> tc2 = new ArrayList<TableColumn<TableEntry, ?>>();
		generateExamTable(tc2, data2, dataPacket, columnsNames1);
		tableOfInfo.setItems(data2);
    }



}