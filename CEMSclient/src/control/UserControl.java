package control;

import java.util.ArrayList;

import common.User;

public class UserControl {
	public static User ConnectedUser; // hold's the connected user
	public static String canOpenExam;
	
	
	
	public static ArrayList<String> names;
	public static ArrayList<User> user; // Create an ArrayList object
	public static ArrayList<User> teachers; // Create an ArrayList object
	public static ArrayList<User> students; // Create an ArrayList object
	
	
	public static boolean isDoingExam = false; // user status about if he doing exam right now
	public static int whatInitiatedExamID = -1; // user status about if he doing exam right now
	
	
	public static boolean RequestForExtraTimeSent;
	
	public static String getCanOpenExam() {
		return canOpenExam;
	}

	public static void setCanOpenExam(String canOpenExam) {
		UserControl.canOpenExam = canOpenExam;
	}


	public UserControl() {
	}
	
}
