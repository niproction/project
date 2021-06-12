package gui.principal;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import common.HistogramInfo;
import common.Principal;
import common.Teacher;
import common.User;
import control.ClientControl;
import control.HistogramControl;
import control.PageProperties;
import control.SceneController;
import control.UserControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

// TODO: Auto-generated Javadoc
/**
 * The Class HistogramController.
 */
public class HistogramController {

	/** The resources. */
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	/** The location. */
	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	/** The bar chart. */
	@FXML // fx:id="barChar"
	private BarChart<?, ?> barChart; // Value injected by FXMLLoader

	/** The back button. */
	@FXML // fx:id="backButton"
	private Button backButton; // Value injected by FXMLLoader

	/** The combo box. */
	@FXML // fx:id="combobox"
	private ComboBox<String> comboBox; // Value injected by FXMLLoader

	/** The text field. */
	@FXML // fx:id="textField"
	private Text textField; // Value injected by FXMLLoader

	/** The avg text field. */
	@FXML // fx:id="avgTextField"
	private Text avgTextField; // Value injected by FXMLLoader

	/** The Median text field. */
	@FXML // fx:id="MedianTextField"
	private Text MedianTextField; // Value injected by FXMLLoader

	/** The x. */
	@FXML
	private CategoryAxis X;

	/** The y. */
	@FXML
	private NumberAxis Y;
	
	/** The circle 1. */
	@FXML
	private Circle circle1;

	/** The circle 2. */
	@FXML
	private Circle circle2;

	/** The data series 1. */
	private Series dataSeries1 = new XYChart.Series();
	
	/** The data series 2. */
	private Series dataSeries2 = new XYChart.Series();

	/**
	 * Go back.
	 *
	 * @param event the event
	 */
	@FXML
	void GoBack(ActionEvent event) {
		AnchorPane page = SceneController.getPage(PageProperties.Page.STATISTICAL_REPORTS);
		App_client.pageContainer.setCenter(page);
	}

	/**
	 * On choose.
	 *
	 * @param event the event
	 */
	@FXML
	void OnChoose(ActionEvent event) {
		String examID = comboBox.getValue();
		if (examID.equals("Select Exam ID")) {
			avgTextField.setText("");
			MedianTextField.setText("");
		} else {
			DecimalFormat df = new DecimalFormat("#.##");
			String avg, mdi;
			ArrayList<HistogramInfo> listOfExams;
			if (!HistogramControl.examOfTeacher.isEmpty()) {
				listOfExams = HistogramControl.examOfTeacher;
			} else {
				listOfExams = HistogramControl.CourseExamGradeList;
			}
			for (HistogramInfo tmp : listOfExams) {
				if (examID == tmp.getExamID()) {
					avg = df.format(tmp.getAvg());
					mdi = df.format(tmp.getMedian());
					avgTextField.setText(avg);
					MedianTextField.setText(mdi);

				}

			}
		}
	}

	/**
	 * Initialize.
	 */
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert barChart != null : "fx:id=\"barChar\" was not injected: check your FXML file 'Histogram.fxml'.";
		assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'Histogram.fxml'.";
		assert comboBox != null : "fx:id=\"combobox\" was not injected: check your FXML file 'Histogram.fxml'.";
		assert textField != null : "fx:id=\"textField\" was not injected: check your FXML file 'Histogram.fxml'.";
		assert avgTextField != null : "fx:id=\"avgTextField\" was not injected: check your FXML file 'Histogram.fxml'.";
		assert MedianTextField != null
				: "fx:id=\"MedianTextField\" was not injected: check your FXML file 'Histogram.fxml'.";
		ArrayList<Object> param = new ArrayList<>();
		boolean flagStudent = false;

		Y.setAutoRanging(false);
		Y.setLowerBound(0);
		Y.setUpperBound(100);
		Y.setTickUnit(10);
		// checks if the user connected is teacher		
		if ((UserControl.ConnectedUser) instanceof Teacher) {
			HistogramControl.examsOfuser = new ArrayList<User>();
			HistogramControl.examsOfuser.add((User)(UserControl.ConnectedUser));
			teacherHistogram(param);
			backButton.setVisible(false);
			
		//else if the user is a principal
		} else if (HistogramControl.examsOfCourse.size() != 0) {
			CourseHistogram(param);

		} else if (HistogramControl.examsOfuser.get(0).getRoleType().equals("teacher")) {
			teacherHistogram(param);
		}

		else if (HistogramControl.examsOfuser.get(0).getRoleType().equals("student")) {
			studentHistogram(param);
			flagStudent = true;
		}
		barChart.setStyle("-fx-font-size: " + 12 + "px;-fx-font-weight:bold");

		barChart.setLegendVisible(false);

		barChart.getData().add(dataSeries1);
		if (flagStudent == false)
			barChart.getData().add(dataSeries2);

		for (Node n : barChart.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill: #c381ae;");
			n.setOpacity(0.7);
		}
		for (Node n : barChart.lookupAll(".default-color1.chart-bar")) {
			n.setStyle("-fx-bar-fill: #91bc93;");
			n.setOpacity(0.7);
		}

		if (dataSeries1.getData().size() == 1)
			barChart.setCategoryGap(300);

		comboBox.getItems().add("Select Exam ID");
		comboBox.getSelectionModel().select("Select Exam ID");
	}

	
	/**
	 * Course histogram.
	 *
	 * @param param the param
	 */
	void CourseHistogram(ArrayList<Object> param) {
		textField.setText(HistogramControl.examsOfCourse.get(0).getCourseName());
		param.add(HistogramControl.examsOfCourse);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSE_GRADES, param,
				null, true);
		ClientControl.getInstance().accept(dataPacket); // send and wait for response from server

		for (HistogramInfo tmp : HistogramControl.CourseExamGradeList) {
			dataSeries1.getData().add(new XYChart.Data(tmp.getExamID(), tmp.getAvg()));
			dataSeries2.getData().add(new XYChart.Data(tmp.getExamID(), tmp.getMedian()));
			comboBox.getItems().add(tmp.getExamID());
			System.out.println(tmp.getAvg());
		}
	}

	/**
	 * Teacher histogram.
	 *
	 * @param param the param
	 */
	void teacherHistogram(ArrayList<Object> param) {
		if ((UserControl.ConnectedUser)instanceof Teacher) {
			textField.setText(UserControl.ConnectedUser.toString());
			//HistogramControl.examsOfuser.add(UserControl.ConnectedUser);
			System.out.println(HistogramControl.examsOfuser.size());
			
		}

		else {
			textField.setText(HistogramControl.examsOfuser.get(0).toString());
			
		}
		System.out.println(HistogramControl.examsOfuser.get(0).getFirstName());
		param.add(HistogramControl.examsOfuser);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_TEACHER_GRADES, param,
				null, true);
		ClientControl.getInstance().accept(dataPacket); // send and wait for response from server

		for (HistogramInfo tmp : HistogramControl.examOfTeacher) {
			dataSeries1.getData().add(new XYChart.Data(tmp.getExamID(), tmp.getAvg()));
			dataSeries2.getData().add(new XYChart.Data(tmp.getExamID(), tmp.getMedian()));
			comboBox.getItems().add(tmp.getExamID());
		}
	}

	/**
	 * Student histogram.
	 *
	 * @param param the param
	 */
	void studentHistogram(ArrayList<Object> param) {
		textField.setText(HistogramControl.examsOfuser.get(0).toString());
		param.add(HistogramControl.examsOfuser);
		System.out.println("im in student1");
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER,
				DataPacket.Request.GET_STUDENT_GRADES_AND_COURSE, param, null, true);
		ClientControl.getInstance().accept(dataPacket); // send and wait for response from server

		for (int i = 0; i < HistogramControl.examOfStudent.size(); i++) {
			System.out.println("im the shit");
			dataSeries1.getData()
					.add(new XYChart.Data(HistogramControl.examOfStudent.get(0).getCoursesName().get(i).toString(),
							Double.valueOf(HistogramControl.examOfStudent.get(0).getGrades().get(i).toString())));		
			comboBox.getItems().add(HistogramControl.examOfStudent.get(i).getCoursesName().get(0).toString());
		}

		avgTextField.setText(String.valueOf(HistogramControl.examOfStudent.get(0).getAvg()));
		MedianTextField.setText(String.valueOf(HistogramControl.examOfStudent.get(0).getMedian()));
		Y.setLabel("Grade");
		X.setLabel("Course");
		comboBox.setVisible(false);
		circle1.setVisible(false);
		circle2.setVisible(false);

	}

}