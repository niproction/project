
package gui.principal;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.App_client;
import common.DataPacket;
import common.HistogramInfo;
import control.HistogramControl;
import control.PageProperties;
import control.SceneController;
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
import javafx.scene.text.Text;

public class HistogramController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="barChar"
	private BarChart<?, ?> barChart; // Value injected by FXMLLoader

	@FXML // fx:id="backButton"
	private Button backButton; // Value injected by FXMLLoader

	@FXML // fx:id="combobox"
	private ComboBox<String> comboBox; // Value injected by FXMLLoader

	@FXML // fx:id="textField"
	private Text textField; // Value injected by FXMLLoader

	@FXML // fx:id="avgTextField"
	private Text avgTextField; // Value injected by FXMLLoader

	@FXML // fx:id="MedianTextField"
	private Text MedianTextField; // Value injected by FXMLLoader

	@FXML
	private CategoryAxis X;

	@FXML
	private NumberAxis Y;
	
	private Series dataSeries1 = new XYChart.Series();
	private Series dataSeries2 = new XYChart.Series();
	@FXML
	void GoBack(ActionEvent event) {
		AnchorPane page = SceneController.getPage(PageProperties.Page.STATISTICAL_REPORTS);
		App_client.pageContainer.setCenter(page);
	}
	@FXML
	void OnChoose(ActionEvent event) {
		String examID = comboBox.getValue();
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

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert barChart != null : "fx:id=\"barChar\" was not injected: check your FXML file 'Histogram.fxml'.";
		assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'Histogram.fxml'.";
		assert comboBox != null : "fx:id=\"combobox\" was not injected: check your FXML file 'Histogram.fxml'.";
		assert textField != null : "fx:id=\"textField\" was not injected: check your FXML file 'Histogram.fxml'.";
		assert avgTextField != null : "fx:id=\"avgTextField\" was not injected: check your FXML file 'Histogram.fxml'.";
		assert MedianTextField != null: "fx:id=\"MedianTextField\" was not injected: check your FXML file 'Histogram.fxml'.";
		ArrayList<Object> param = new ArrayList<>();	
		
		Y.setAutoRanging(false);
		Y.setLowerBound(0);
		Y.setUpperBound(100);
		Y.setTickUnit(10);

		if (HistogramControl.examsOfCourse.size() != 0) {
			CourseHistogram( param);

		} else if (HistogramControl.examsOfuser.get(0).getRoleType().equals("teacher")) {
			teacherHistogram( param);
		}

		else if (HistogramControl.examsOfuser.get(0).getRoleType().equals("student")) {
			studentHistogram(param);
		}
		barChart.setStyle("-fx-font-size: " + 12 + "px;-fx-font-weight:bold");

		barChart.setLegendVisible(false);

		barChart.getData().add(dataSeries1);
		barChart.getData().add(dataSeries2);

		for (Node n : barChart.lookupAll(".default-color0.chart-bar")) 
			n.setStyle("-fx-bar-fill: #73255a;");
		
		for(Node n:barChart.lookupAll(".default-color1.chart-bar")) 
            n.setStyle("-fx-bar-fill: #3acd3f;");
        
		if (dataSeries1.getData().size() == 1)
			barChart.setCategoryGap(300);
	}

	void CourseHistogram( ArrayList<Object> param) {
		textField.setText(HistogramControl.examsOfCourse.get(0).getCourseName());
		param.add(HistogramControl.examsOfCourse);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_COURSE_GRADES, param,
				null, true);
		App_client.chat.accept(dataPacket); // send and wait for response from server

		for (HistogramInfo tmp : HistogramControl.CourseExamGradeList) {
			dataSeries1.getData().add(new XYChart.Data(tmp.getExamID(), tmp.getAvg()));
			dataSeries2.getData().add(new XYChart.Data(tmp.getExamID(), tmp.getMedian()));

			comboBox.getItems().add(tmp.getExamID());
			System.out.println(tmp.getAvg());
		}
	}

	void teacherHistogram( ArrayList<Object> param) {
		textField.setText(HistogramControl.examsOfuser.get(0).toString());
		param.add(HistogramControl.examsOfuser);
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_TEACHER_GRADES, param,
				null, true);
		App_client.chat.accept(dataPacket); // send and wait for response from server

		for (HistogramInfo tmp : HistogramControl.examOfTeacher) {
			dataSeries1.getData().add(new XYChart.Data(tmp.getExamID(), tmp.getAvg()));
			dataSeries2.getData().add(new XYChart.Data(tmp.getExamID(), tmp.getMedian()));

			comboBox.getItems().add(tmp.getExamID());
		}
	}

	void studentHistogram( ArrayList<Object> param) {
		textField.setText(HistogramControl.examsOfuser.get(0).toString());
		param.add(HistogramControl.examsOfuser);
		System.out.println("im in student1");
		DataPacket dataPacket = new DataPacket(DataPacket.SendTo.SERVER, DataPacket.Request.GET_STUDENT_GRADES, param,
				null, true);
		App_client.chat.accept(dataPacket); // send and wait for response from server

		for (int i=0;i<HistogramControl.examOfStudent.size();i++) {
			System.out.println("im the shit");
			dataSeries1.getData().add(new XYChart.Data(HistogramControl.examOfStudent.get(0).getCoursesName().get(i).toString(), Double.valueOf(HistogramControl.examOfStudent.get(0).getGrades().get(i).toString())));  
			//dataSeries2.getData().add(new XYChart.Data(HistogramControl.examOfStudent.get(i).getCoursesName().get(0).toString(), Double.valueOf(HistogramControl.examOfStudent.get(0).getGrades().get(i).toString())));
			comboBox.getItems().add(HistogramControl.examOfStudent.get(i).getCoursesName().get(0).toString());
		}
		
		avgTextField.setText(String.valueOf(HistogramControl.examOfStudent.get(0).getAvg()));
		MedianTextField.setText(String.valueOf(HistogramControl.examOfStudent.get(0).getMedian()));
		Y.setLabel("Grade");
		X.setLabel("Course");
		comboBox.setVisible(false);
	}

}
