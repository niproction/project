package server;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.IOException;
import java.util.ArrayList;

import common.DataPacket;
import common.Exam;
import common.Principal;
import common.Teacher;
import common.examInitiated;
import gui.serverGUI;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server. also groups notify
 * 
 * @author Rostik kalinski
 * @version 2021
 */
public class CEMSServer extends AbstractServer {
	// Class variables *************************************************
	private serverGUI controller;
	// private ArrayList<> connected_users;
	final public static int DEFAULT_PORT = 5555;
	private static boolean IsConnected = false;

	public static ArrayList<Integer> OnGoingExams = new ArrayList<>(); // list of on going exams..
	public static ArrayList<Group> studentsOfOnGoingExam_Groups = new ArrayList<>(); // the list of Groups of connected
																						// students that committing the
																						// exam OnGoingExams.get(i)

	// private static ArrayList<Exam> OnGoingExams; // list of on going exams..
	public static ArrayList<GroupMember> teachersOfOnGoingExams = new ArrayList<>(); // the list of Groups of connected
																						// students that committing the
																						// exam OnGoingExams.get(i)

	public static Group principals = new Group(); // the Group of connected principles

	public enum WhoToNotify {
		STUDENTS_DOING_THE_SAME_EXAM, ALL_PRINCIPALS, SPECIFIC_TEACHER
	}

	
	
	private void desideWhoToNotify(Notification notification) {
		System.out.println("Notifing with >>> "+notification.getDataPacket().getRequest().toString());
		if (notification.getWho() == WhoToNotify.STUDENTS_DOING_THE_SAME_EXAM) {
			for (int i=0;i<OnGoingExams.size();i++) {
				if(OnGoingExams.get(i) == notification.getIdintifier())
				studentsOfOnGoingExam_Groups.get(i).notifyMembers(notification.getDataPacket()); // will notify all the connected
																						// user that doing exam right
																						// now with dataPacket
			}
		} else if (notification.getWho() == WhoToNotify.SPECIFIC_TEACHER) {
			teachersOfOnGoingExams.get(notification.getIdintifier()).notify(notification.getDataPacket()); // will notify specific teacher with the
																		// dataPacket
		} else if (notification.getWho() == WhoToNotify.ALL_PRINCIPALS) {
			principals.notifyMembers(notification.getDataPacket()); // will notify all the connected principals with the dataPacket
		}
	}

	public CEMSServer(int port, serverGUI controller) {
		super(port);
		this.controller = controller;
	}

	public void Start() {
		int port = 0; // Port to listen on
		try {
			port = getPort(); // Get port from command line
			// IsConnected = true;
		} catch (Throwable t) {
			// IsConnected = false;
			// port = DEFAULT_PORT; // Set port to 5555
		}

		try {
			this.listen(); // Start listening for connections
			IsConnected = true;
		} catch (Exception ex) {
			controller.setInput_logs(controller.getInput_logs().getText() + "ERROR - Could not listen for clients!\n");
			IsConnected = false;
		}
	}

	public void Close() {
		try {
			this.close();
			IsConnected = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		controller.setInput_logs(controller.getInput_logs().getText() + "Message received from " + client + "\n");


		ServerDataPacketHandler handler = new ServerDataPacketHandler(client);
		ArrayList<Object> DATA = handler.CheckRequestExecuteCreateResponce(msg);

		// if the return DataPacket is not null means we need to return an update
				// message to the client
				if (DATA.size() > 0 && (DataPacket) DATA.get(0) != null) {
					System.out.println("tring send response to client");
					try {
						client.sendToClient((DataPacket) DATA.get(0));

						System.out.println("Sending response to client");
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					System.out.println("not generated response DataPacket");
				}

				// message to clients that needed to be notified after some action been taken by
				// other client
				if (DATA.get(1) != null) // check that the data is not null
				{
					System.out.println("tring send response to specific clients");
					try {
						Thread.sleep(50);
						// sendToAllClients(to_be_returend_DataPacket[1]); // send to all clients
						desideWhoToNotify((Notification)DATA.get(1));
					} catch (Exception e) {
						System.out.println("Error not notifing any clientssss");
						e.printStackTrace();
					}
				} else {
					// the array is smaller means that there is
					System.out.println("No notify to other clients needed");
				}
				if (DATA.get(2) != null) // check that the data is not null
				{
					System.out.println("tring send response to specific clients");
					try {
						Thread.sleep(50);
						desideWhoToNotify((Notification) DATA.get(2));
					} catch (Exception e) {
						System.out.println("Error not notifing any clientssss");
						e.printStackTrace();
					}
				} else {
					// the array is smaller means that there is
					System.out.println("No notify to other clients needed");
				}
	}

	public boolean GET_connectionState() {
		return IsConnected;
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		IsConnected = true;
		controller.setInput_logs(
				controller.getInput_logs().getText() + "Server listening for connections on port " + getPort() + "\n");
		controller.logs_scroll();
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		IsConnected = false;
		controller.setInput_logs(
				controller.getInput_logs().getText() + "Server has stopped listening for connections.\n");
		controller.logs_scroll();
	}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		controller.setInput_logs(controller.getInput_logs().getText() + client + " connected\n");
		controller.logs_scroll();
	}

	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("dddddddd");
		controller.setInput_logs(controller.getInput_logs().getText() + client + " disconnected\n");
		controller.logs_scroll();
	}

}
//End of EchoServer class
