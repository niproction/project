package server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App_server extends Application {
	public static CEMSServer server;
	private File lockFile = null;
	private FileChannel lockChannel = null;
	private FileLock lock = null;

	@Override
	public void start(Stage primaryStage) {

		disableMultipleAppSesstions();

		System.out.println("Server manager starting");
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/serverGUI.fxml"));
			Scene scene = new Scene(root, 492, 598);

			System.out.println("Server manager started");

			primaryStage.setTitle("Server manager");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		try {
			lock.release();
			lockChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lockFile.delete();
		System.out.println("Server manager exited");
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void disableMultipleAppSesstions() {
		try {
			lockFile = new File("server.lock");
			if (lockFile.exists())
				lockFile.delete();
			FileOutputStream lockFileOS = new FileOutputStream(lockFile);
			lockFileOS.close();
			lockChannel = new RandomAccessFile(lockFile, "rw").getChannel();
			lock = lockChannel.tryLock();
			if (lock == null)
				throw new Exception("Unable to obtain lock");
		} catch (Exception e) {
			System.out.println("Another Server App Already Running..");
			//e.printStackTrace();
			System.exit(0);
		}
	}
}