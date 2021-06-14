package test;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

import java.awt.Button;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import common.DataPacket;
import common.Exam;
import common.MyFile;
import control.ClientControl;
import control.ExamControl;
import control.UserControl;

public class Controller implements Initializable {
public static File newFile;
	public static ArrayList<Object> parameters;
	static FileChooser fileChooser = new FileChooser();

	@FXML
	private TextArea textArea;

	@FXML
	void save(ActionEvent event) {
//		ImageView imgView = new ImageView("img/save.png");
//		imgView.setFitWidth(20);
//		imgView.setFitHeight(20);
//
//		Button save = new Button("save");
//		//newFile = new File("manuel_exams/" + (String) parameters.get(1));
		//Parent root

//		try {
//			FileOutputStream fos = new FileOutputStream(newFile); 
//			BufferedOutputStream bos = new BufferedOutputStream(fos);
//			
//			fileChooser.setInitialFileName("g.docx");
//			fileChooser.setInitialDirectory(newFile);
//			//fileChooser.setInitialDirectory();
//			bos.flush();
//			fos.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		// Creating a File chooser
		//fileChooser.setTitle("Save");
	//	fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));

		// Show save file dialog
		File file = fileChooser.showSaveDialog(Main.primaryStage);

		//Main.primaryStage.show();

	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
		 //Adding action on the menu item

		
		
		MyFile msg = new MyFile("g.docx");

		try {

			 newFile = new File("C:\\Users\\Max\\git\\project2\\CEMSclient\\src\\test\\g.docx");
			byte[] mybytearray = new byte[(int) newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			msg.initArray(mybytearray.length);
			msg.setSize(mybytearray.length);

			bis.read(msg.getMybytearray(), 0, mybytearray.length);
			parameters.add(0, msg);
			parameters.add(1, "Exam.docx");

		} catch (Exception e) {
			System.out.println("Error send (Files)msg) to Server");
		}

	}

	public Controller() {
		super();
	}
	

	
	
	
	public void saveSystem(File file, String content) {
		try {
			PrintWriter printWriter = new PrintWriter(file);
			printWriter.write(content);
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
