package control;

import java.util.ArrayList;

import common.User;

public class UserControl {
	public static User ConnectedUser; // hold's the connected user
	public static String canOpenExam;
	public static int canManage=0;
	
	public static String getCanOpenExam() {
		return canOpenExam;
	}

	public static void setCanOpenExam(String canOpenExam) {
		UserControl.canOpenExam = canOpenExam;
	}

	public static int getCanManage() {
		return canManage;
	}

	public static void setCanManage(int canManage) {
		UserControl.canManage = canManage;
	}

	public static boolean isDoingExam = false; // user status about if he doing exam right now
	public static int whatInitiatedExamID = -1; // user status about if he doing exam right now
	
	
	
	
	public static ArrayList<User> user; // Create an ArrayList object

	public UserControl() {
	}
	
}
