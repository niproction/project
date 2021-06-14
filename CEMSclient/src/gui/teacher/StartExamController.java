package gui.teacher;

/**
 * Sample Skeleton for 'startExam.fxml' Controller Class
 */
/**
 * Sample Skeleton for 'startExam.fxml' Controller Class
 */

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import common.Exam;
import common.ExamDone;
import control.ClientControl;
import control.ExamControl;
import control.GetCopyOfExamControl;
import control.ManageOngoingExams;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import gui.TableEntry;
import gui.principal.infoPageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class StartExamController {
	
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="ac"
	private AnchorPane ap; // Value injected by FXMLLoader

	@FXML // fx:id="choicebox_exams"
	private ChoiceBox<Exam> choicebox_exams; // Value injected by FXMLLoader
	@FXML
	private TableView<TableEntry> tableView;
	@FXML
	public TableColumn<TableEntry, ?> colExamID;
	@FXML
	public TableColumn<TableEntry, ?> colDescription;
	@FXML
	public TableColumn<TableEntry, ?> colstartExam;
	@FXML
	public TableColumn<TableEntry, ?> colPassword;
	@FXML
	private TableColumn<TableEntry, ?> colAmountOfQuestions;
	@FXML // fx:id="button_start"
	private Button button_start; // Value injected by FXMLLoader

	@FXML // fx:id="button_start"
	private Label label_message; // Value injected by FXMLLoader
	@FXML
	private TextField textfielf_password;
	private ArrayList<Exam> examList;
	private ArrayList<Integer> amountOfQuestions;
	SceneController sceen;
	private ArrayList<Button> ButtonList=new ArrayList<>();

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert ap != null : "fx:id=\"ac\" was not injected: check your FXML file 'startExam.fxml'.";
		assert choicebox_exams != null
				: "fx:id=\"choicebox_exams\" was not injected: check your FXML file 'startExam.fxml'.";
		assert button_start != null : "fx:id=\"button_start\" was not injected: check your FXML file 'startExam.fxml'.";

		// sceen = new SceneController(PageProperties.Page.START_EXAM, ap);
		// sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		colExamID.setCellValueFactory(new PropertyValueFactory<>("col1"));
		colDescription.setCellValueFactory(new PropertyValueFactory<>("col2"));
		colstartExam.setCellValueFactory(new PropertyValueFactory<>("col3"));
		colPassword.setCellValueFactory(new PropertyValueFactory<>("col4"));
		colAmountOfQuestions.setCellValueFactory(new PropertyValueFactory<>("col5"));
	

		// send datapacket to recive the exams
		ArrayList<Object> parameter = new ArrayList<>();
		parameter.add(UserControl.ConnectedUser);

		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_EXAMS_BY_TEACHER,
				parameter, null, true);

		ClientControl.getInstance().accept(dataPacket);

		examList=ExamControl.exams;
		ExamControl.exams=null;
		amountOfQuestions=ExamControl.amountOfQuestions;
		ExamControl.amountOfQuestions=null;
		tableView.setItems(getRequests());
	}
	int index=0,i=0;
	public ObservableList<TableEntry> getRequests() {
		ObservableList<TableEntry> requests = FXCollections.observableArrayList();
		// LocalDate date = LocalDate.of(1995, 10, 28);
		for (Exam entry : examList) {
			
			final Button start = new Button("Start Exam");
			final TextField passwordField=new TextField();
			try {
				for(int i=0;i<ButtonList.size();i++) {
					if(ButtonList.get(i).equals(start))
						index=i;
				}
				start.setAlignment(Pos.CENTER);
				EventHandler<ActionEvent> startExmHandler = new EventHandler<ActionEvent>() {
					int indexer=index;
					@Override
					public void handle(ActionEvent event) {
						ArrayList<Object> parameter=new ArrayList<>();
						parameter.add(examList.get(indexer));
						parameter.add(UserControl.ConnectedUser);
						parameter.add(passwordField.getText());
						DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.START_EXAM,
								parameter, null, true);
						ClientControl.getInstance().accept(dataPacket);
						AnchorPane page = SceneController.getPage(PageProperties.Page.MANAGE_ONGOING_EXAM);
						App_client.pageContainer.setCenter(page);
					}
				};
				start.setOnAction(startExmHandler);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			requests.add(new TableEntry(entry.getExamID(), entry.getDescription(), start,passwordField,amountOfQuestions.get(i++)));

		}

		return requests;
	}
	
	
}