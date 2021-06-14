// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package control;

import java.io.IOException;
import java.util.ArrayList;

import common.ChatIF;
import common.DataPacket;
import ocsf.client.AbstractClient;

public class CEMSClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */ 
	ChatIF clientUI;
	private static CEMSClient client = null;
	public static Boolean awaitResponse = false;

	public CEMSClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
	}

	// static method to create instance of Singleton class
	public CEMSClient getInstance(String host, int port, ChatIF clientUI, Object Conntroller) {
		if (client == null) {
			try {
				client = new CEMSClient(host, port, clientUI);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return client;
	}

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		// awaitResponse = false; // reset to false untill the responce will come back

		System.out.println("Recived data from server");
		ClientDataPacketHandler handler = new ClientDataPacketHandler();
		ArrayList<Object> to_be_returend_DataPacket = handler.CheckRequestExecuteCreateResponce(msg);
		System.out.println("after parsing");

		awaitResponse = false;

		// if there is message to respond
		if ((DataPacket)to_be_returend_DataPacket.get(0) != null) {
			try {
				sendToServer((DataPacket)to_be_returend_DataPacket.get(0));
			} catch (IOException e) {
				System.out.println("Could not send message to server.  Terminating client.");
				clientUI.display("Could not send message to server.  Terminating client.");
				quit();
			}
		}
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param string The message from the UI.
	 */
	public void handleMessageFromClientUI(DataPacket dataPacket) {
		try {
			System.out.println("sending");
			awaitResponse = true;
			sendToServer(dataPacket);

			while (awaitResponse) {
				System.out.println("wait to responce");
				try {
					System.out.println("wait to responce");
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not send message to server.  Terminating client.");
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	public void connectionClosed() {
		System.out.println("dsdas");
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class
