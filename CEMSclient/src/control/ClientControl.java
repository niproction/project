package control;

import java.io.IOException;

import common.ChatIF;
import common.DataPacket;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientControl implements ChatIF {
	final public static int DEFAULT_PORT = 5555;
	public static String host = null;
	public static int port = 5555;
	public static boolean isConnected = false;
	
	
	

	private static CEMSClient client = null;

	public static boolean awaitResponse;
	// public static DataPacket storedDataPacket;

	private static ClientControl clientChat = null;
	// chat = new ClientController("localhost", 5555);

	
	private ClientControl() {
		try {
			client = new CEMSClient(host, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			// System.exit(1);
			isConnected = false;
			//clientChat=null;
		}
	}

	public static ClientControl getInstance() {
		if (clientChat == null)
			clientChat = new ClientControl();
		return clientChat;
	}
	
	public static boolean isConnected() {
		return isConnected;
	}

	
	
	public void accept(DataPacket dataPacket) {
		if (dataPacket != null) {
			try {
				client.handleMessageFromClientUI(dataPacket);
			} catch (Exception ex) {
				System.out.println("Unexpected error while reading from console!");
			}
		}
	}

	public void display(String message) {
		System.out.println("> " + message);
	}

	public static CEMSClient GET_client() {
		return client;
	}
}
//End of ConsoleChat class
