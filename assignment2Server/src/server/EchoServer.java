package server;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ocsf.server.*;

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
public class EchoServer extends AbstractServer {
	// Class variables *************************************************
	/**
	 * The default port to listen on.
	 */
	gui.serverGUIcontroller gui;
	mysqlConnection conn;
	Connection connection;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port, gui.serverGUIcontroller serverGUIcontroller) {
		super(port);
		this.gui = serverGUIcontroller;
		conn = new mysqlConnection();
		connection = conn.connectToDB();
		try {
			this.listen(); // Start listening for connections
		} catch (Exception ex) {

		}
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Object object;
		object = parsingTheData(msg, client);
		if (object instanceof String) {
			try {
				System.out.println("string");
				client.sendToClient(object);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(object instanceof ArrayList<?>) {
			try {
				System.out.println("arraylist");
				client.sendToClient(object);
				System.out.println("pass");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * this method create the mySQLconnection and check if the message is array list
	 * and send it as one.
	 * 
	 * @param msg The message received from the client.
	 */
	public Object parsingTheData(Object msg, ConnectionToClient client) {
		if (msg instanceof ArrayList<?>) {
			ArrayList<String> arr = (ArrayList<String>) msg;
			if (arr.size() == 1) {
				if (arr.get(0).equals("client has been disconnected")) {
					gui.getConnInfo("client has been disconnected");
					gui.getClientinfo("", "");
					return "client has been disconnected";
				} else if (arr.get(0).equals("client has been connected")) {
					gui.getClientinfo(client.getInetAddress().getHostName(), client.getInetAddress().getHostAddress());
					gui.getConnInfo("client has been connected");
					return "client has been connected";
				} else {
					System.out.println("print update");
					ArrayList<String> sqList = new ArrayList<>();
					String sql = "SELECT * FROM test WHERE id='" + arr.get(0) + "'";
					Statement st;
					ResultSet rs;
					try {
						st = connection.createStatement();
						rs = st.executeQuery(sql);
						if (rs.next()) {// get first result
							sqList.add(rs.getString(1));
							sqList.add(rs.getString(2));
							sqList.add(rs.getString(3));
							sqList.add(rs.getString(4));
							sqList.add(rs.getString(5));
							return sqList;
							/*
							 * String forLog = String.format("%8s %33s %29s %28s %36s", rs.getString(1),
							 * rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)); return
							 * forLog;
							 */
						} else {
							return "There is no test with this id";
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			} else {
				String sql = "UPDATE test SET testDuration='" + arr.get(0) + "'" + "WHERE id='" + arr.get(1) + "'";
				try {
					Statement st = connection.createStatement();
					int success = st.executeUpdate(sql);
					if (success == 1)
						return "Update succeeded";
					else
						return "Update failed";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}

		return null;

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
		gui.getConnInfo("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
		gui.getConnInfo("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	/*
	 * public static void main(String[] args) { int port = 0; // Port to listen on
	 * try { port = Integer.parseInt(args[0]); // Get port from command line } catch
	 * (Throwable t) { port = DEFAULT_PORT; // Set port to 5555 }
	 * 
	 * EchoServer sv = new EchoServer(port);
	 * 
	 * try { sv.listen(); // Start listening for connections } catch (Exception ex)
	 * { System.out.println("ERROR - Could not listen for clients!"); } }
	 */

}
//End of EchoServer class
