package server;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.IOException;
import java.util.ArrayList;

import common.DataPacket;
import gui.serverGUI;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class CEMSServer extends AbstractServer {
	// Class variables *************************************************
	private serverGUI controller;
	private ArrayList connected_users;
	final public static int DEFAULT_PORT = 5555;
	private static boolean IsConnected = false;



	public CEMSServer(int port, serverGUI controller) {
		super(port);
		this.controller = controller;
	}


	/*public CEMSServer(serverGUI server_gui, int port, String host,String username,String password,String db_name,String mysql_port) {
		super(port);
		this.server_gui = server_gui;
		con=new mysqlConnection(host, username, password, db_name, mysql_port);
		String msg = con.connectToDB();
		server_gui.setInput_logs(server_gui.getInput_logs().getText()+msg);
		if(con.getIsConnected())
			server_gui.ServerStartedButtonsDisable();
	}*/

	

	public void Start() {
		int port = 0; // Port to listen on
		try {
			port = getPort(); // Get port from command line
			//IsConnected = true;
		} catch (Throwable t) {
			//IsConnected = false;
			//port = DEFAULT_PORT; // Set port to 5555
		}

		try {
			this.listen(); // Start listening for connections
			IsConnected = true;
		} catch (Exception ex) {
			controller.setInput_logs(controller.getInput_logs().getText()+"ERROR - Could not listen for clients!\n");
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
		controller.setInput_logs(controller.getInput_logs().getText()+"Message received from " + client+"\n");

		ServerDataPacketHandler handler = new ServerDataPacketHandler(App_server.mysqlCon.getCon());
		DataPacket to_be_returend_DataPacket = handler.CheckRequestExecuteCreateResponce(msg);
	

		// if the return DataPacket is not null means we need to return an update message to the client
		if(to_be_returend_DataPacket != null)
		{
			System.out.println("Sending response to client");
			try {
				client.sendToClient(to_be_returend_DataPacket);
				System.out.println("Sending response to client");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public boolean GET_connectionState()
	{
		return IsConnected;
	}

	

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		IsConnected = true;
		controller.setInput_logs(controller.getInput_logs().getText()+"Server listening for connections on port " + getPort()+"\n");
		controller.logs_scroll();
	}


	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		IsConnected = false;
		controller.setInput_logs(controller.getInput_logs().getText()+"Server has stopped listening for connections.\n");
		controller.logs_scroll();
	}
	@Override
	protected void clientConnected(ConnectionToClient client)
	{
		controller.setInput_logs(controller.getInput_logs().getText()+client+" connected\n");
		controller.logs_scroll();
	}
	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("dddddddd");
		controller.setInput_logs(controller.getInput_logs().getText()+client+" disconnected\n");
		controller.logs_scroll();
	}
	
}
//End of EchoServer class
