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
	public static String host = "localhost";
	public static int port = 5555;
	private static boolean isConnected = false;
	
	private static CEMSClient client = null;

	public static boolean awaitResponse;
	public static String message_recived;

	private static ClientControl clientChat = null;
	// chat = new ClientController("localhost", 5555);

	
	private ClientControl() {
		try {
			System.out.println(host+ " "+port);
			client = new CEMSClient(host, port, this);
			isConnected = true;
		} catch (IOException exception) {
			isConnected = false;
			client=null;
			this.clientChat=null;
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			// System.exit(1);
			
			//clientChat=null;
		}
	}
	
	public static ClientControl getInstance() {
		if (client == null && !isConnected)
		{
			clientChat = new ClientControl();
			System.out.println("in");
		}
		else {
			System.out.println("dddd"+clientChat);
		}
		return clientChat;
	}
	
	// metod to destroy the connection and the instance
	public static void destroyInstance() {
		if (clientChat != null)
		{
			if(client != null)
				client.connectionClosed();// quit();
			clientChat = null;
			client = null;
			isConnected = false;
			System.out.println("Disconnected from server");
		}
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
