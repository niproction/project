package gui.teacher;

import java.util.ArrayList;

import client.App_client;
import common.DataPacket;
import common.Exam;
import common.Question;
import control.PageProperties;
import control.QuestionControl;
import control.SceneController;
import control.UserControl;
import control.QuestionControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class editQuestionController {
	SceneController sceen;
	// ObservableList<String>
	// field=FXCollections.observableArrayList("Chemistry","Art","Mathematics","Physics");
	@FXML // fx:id="ap"
	private AnchorPane ap;
	@FXML
	private Button backHomebtn;

	@FXML
	private TextArea questionInfotxt;
	@FXML
	private TextField option1txt;
	@FXML
	private TextField option2txt;
	@FXML
	private TextField option3txt;
	@FXML
	private TextField option4txt;

	@FXML
	private Pane answer1;
	@FXML
	private Pane answer2;
	@FXML
	private Pane answer3;
	@FXML
	private Pane answer4;

	@FXML
	private Button saveBtn;
	@FXML
	private Button clearBtn;
	@FXML
	private Label answerErrorLbl;
	// @FXML
	// private ChoiceBox<String> fieldBox;
	@FXML
	private Label seccessLabel;
	@FXML
	private TextField questionIDtxt;
	@FXML
	private Button questionIDBtn;
	@FXML
	private Label questionLabel;
	private boolean canUpdate = false;  
	@FXML
	private ChoiceBox<String> questionsIDBox=new ChoiceBox<>();
	private ArrayList<Question> questionList;
	Question question;
 
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		// assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file
		// 'Home.fxml'.";
		sceen = new SceneController(PageProperties.Page.EDIT_QUESTION, ap);
		sceen.AnimateSceen(SceneController.ANIMATE_ON.LOAD);
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(UserControl.ConnectedUser);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_QUESTION,
				parameters, null, true);
		System.out.println("trying to get the question");
		App_client.chat.accept(dataPacket);
		if( QuestionControl.getQuestions()!=null) {
			System.out.println("got all the questions");
			questionList=QuestionControl.getQuestions();
			QuestionControl.setQuestions(null);
		}
		ObservableList<String> questions=FXCollections.observableArrayList();
		for( Question i:questionList) 
			questions.add(i.getqID());
		questionsIDBox.setItems(questions);
		questionsIDBox.setOnAction((event) -> {
		    int selectedIndex = questionsIDBox.getSelectionModel().getSelectedIndex();
		    Object selectedItem = questionsIDBox.getSelectionModel().getSelectedItem();

		    System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
		    System.out.println("   ChoiceBox.getValue(): " + questionsIDBox.getValue());
		    questionInfotxt.setText(questionList.get(selectedIndex).getInfo());
			option1txt.setText(questionList.get(selectedIndex).getOption1());
			option2txt.setText(questionList.get(selectedIndex).getOption2());
			option3txt.setText(questionList.get(selectedIndex).getOption3());
			option4txt.setText(questionList.get(selectedIndex).getOption4());
			canUpdate=true;
			question =questionList.get(selectedIndex);
		});
		
		answerErrorLbl.setVisible(false);
		seccessLabel.setVisible(false);
		// fieldBox.setValue("Mathematics");
		// fieldBox.setItems(field);
		
	}

	public void handleOnAction(MouseEvent event) {
		answerErrorLbl.setVisible(false);
		seccessLabel.setVisible(false);
		if (event.getSource() == backHomebtn) {
			// SceneController sceen = new SceneController(PageProperties.Page.HOME, ap);

			sceen.LoadSceen(SceneController.ANIMATE_ON.UNLOAD);
		}
		if (event.getSource() == saveBtn) {
			if (canUpdate) {
				answerErrorLbl.setVisible(false);
				seccessLabel.setVisible(false);
				save();
				question=null;
				//App_client.Question=null;
				canUpdate=false;
				
			}
			else {
				answerErrorLbl.setText("You must choose question first");
				answerErrorLbl.setVisible(true);
				seccessLabel.setVisible(false);
			}
		}
		if (event.getSource() == clearBtn) {
			answerErrorLbl.setVisible(false);
			seccessLabel.setVisible(false);
			clear();
		}
		

	}

	public void clear() {
		questionInfotxt.setText("");
		option1txt.setText("");
		option2txt.setText("");
		option3txt.setText("");
		option4txt.setText("");
	}

	public void save() {
		ArrayList<Object> parameters = new ArrayList<>();
		if (question.getAnswer() == null) {
			answerErrorLbl.setText("You must choose an answer!");
			answerErrorLbl.setVisible(true);
			return;
		} else if (questionInfotxt.getText().toString().equals("") || option1txt.getText().toString().equals("")
				|| option2txt.getText().toString().equals("") || option3txt.getText().toString().equals("")
				|| option4txt.getText().toString().equals("")) {
			answerErrorLbl.setText("You must fill all the parameters!");
			answerErrorLbl.setVisible(true);
			return;
		}

		
		question.setInfo(questionInfotxt.getText());
		question.setOption1(option1txt.getText().toString());
		question.setOption2(option2txt.getText().toString());
		question.setOption3(option3txt.getText().toString());
		question.setOption4(option4txt.getText().toString());
		// already have the answer
		parameters.add(question);
		parameters.add(UserControl.ConnectedUser);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.EDIT_QUESTION, parameters,
				null, true);
		System.out.println("try create new question");
		seccessLabel.setVisible(true);
		clear();
		App_client.chat.accept(dataPacket);

	}
	

	@FXML
	void set_correct_answer_1(MouseEvent event) {
		answerErrorLbl.setVisible(false);
		seccessLabel.setVisible(false);
		answer1.setStyle("-fx-background-color: rgba(91, 218, 35, 1); -fx-background-radius: 10;");
		answer2.getStyleClass().add("option");
		answer3.getStyleClass().add("option");
		answer4.getStyleClass().add("option");
		question.setAnswer("1");
	}

	@FXML
	void set_correct_answer_2(MouseEvent event) {
		answerErrorLbl.setVisible(false);
		seccessLabel.setVisible(false);
		answer2.setStyle("-fx-background-color: rgba(91, 218, 35, 1); -fx-background-radius: 10;");
		answer1.getStyleClass().clear();
		answer1.getStyleClass().add("option");
		answer3.getStyleClass().clear();
		answer3.getStyleClass().add("option");
		answer4.getStyleClass().clear();
		answer4.getStyleClass().add("option");
		question.setAnswer("2");
	}

	@FXML
	void set_correct_answer_3(MouseEvent event) {
		answerErrorLbl.setVisible(false);
		seccessLabel.setVisible(false);
		answer3.setStyle("-fx-background-color: rgba(91, 218, 35, 1); -fx-background-radius: 10;");
		answer1.getStyleClass().clear();
		answer1.getStyleClass().add("option");
		answer2.getStyleClass().clear();
		answer2.getStyleClass().add("option");
		answer4.getStyleClass().clear();
		answer4.getStyleClass().add("option");
		question.setAnswer("3");
	}

	@FXML
	void set_correct_answer_4(MouseEvent event) {
		answerErrorLbl.setVisible(false);
		seccessLabel.setVisible(false);
		answer4.setStyle("-fx-background-color: rgba(91, 218, 35, 1); -fx-background-radius: 10;");
		answer1.getStyleClass().clear();
		answer1.getStyleClass().add("option");
		answer2.getStyleClass().clear();
		answer2.getStyleClass().add("option");
		answer3.getStyleClass().clear();
		answer3.getStyleClass().add("option");
		question.setAnswer("4");
	}

}
