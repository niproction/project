package control;

import java.util.ArrayList;

import common.ExtraTimeRequest;
import common.User;

public class UserControl {
	public static User ConnectedUser; // hold's the connected user
	public static String canOpenExam;
	
	
	
	public static ArrayList<String> names;
	public static ArrayList<User> user; // Create an ArrayList object
	public static ArrayList<User> teachers; // Create an ArrayList object
	public static ArrayList<User> students; // Create an ArrayList object
	public static String ID;
	
	public static boolean isDoingExam = false; // user status about if he doing exam right now
	public static int whatInitiatedExamID = -1; // user status about if he doing exam right now
	
	
	public static boolean RequestForExtraTimeSent;
	
	
	
	public static ArrayList<ExtraTimeRequest> requests;
	public static int ifRequests;
	public static int HowManyExamsNow;


	
	public static int ongoingExam;
	

	//rostik v10
	private static int Notipications = 0;
	
	
	public static int getNotipications() {
		return Notipications;
	}

	public static void setNotipications(int notipications) {
		Notipications = notipications;
	}
	//rostik v10
	
	public static String getCanOpenExam() {
		return canOpenExam;
	}

	public static void setCanOpenExam(String canOpenExam) {
		UserControl.canOpenExam = canOpenExam;
	}


	public UserControl() {
	}
	
	
	
	public static void resetAll() {
		ConnectedUser=null;
		canOpenExam=null;
		names=null;
		user=null;
		teachers=null;
		students=null;
		requests=null;
	}
	
}
