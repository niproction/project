package control;

import java.io.IOException;

import common.ChatIF;
import common.DataPacket;

// TODO: Auto-generated Javadoc
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
	
	/** The Constant DEFAULT_PORT.
	 *  */
	final public static int DEFAULT_PORT = 5555;
	
	/** The host. */
	public static String host = "localhost";
	
	/** The port. */
	public static int port = 5555;
	
	/** check if the client is connected. */
	private static boolean isConnected = false;
	
	/** The client. */
	private static CEMSClient client = null;

	/** The await response. */
	public static boolean awaitResponse;
	
	/** The message recived. */
	public static String message_recived;

	/** The client chat. */
	private static ClientControl clientChat = null;
	// chat = new ClientController("localhost", 5555);

	
	/**
	 * Instantiates a new client control.
	 */
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
	
	/**
	 * Gets the single instance of ClientControl.
	 *using singelton for sending objects from the client to server
	 * @return single instance of ClientControl
	 */
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
	
	/**
	 * Destroy instance.
	 */
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
	
	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public static boolean isConnected() {
		return isConnected;
	}

	
	
	/**
	 * Accept.
	 *
	 * @param dataPacket the data packet
	 */
	public void accept(DataPacket dataPacket) { 
		if (dataPacket != null) { 
			try {
				client.handleMessageFromClientUI(dataPacket);
			} catch (Exception ex) {
				System.out.println("Unexpected error while reading from console!");
			}
		}
	}

	/**
	 * Display.
	 *
	 * @param message the message
	 */
	public void display(String message) {
		System.out.println("> " + message);
	}

	/**
	 * GE T client.
	 *
	 * @return the CEMS client
	 */
	public static CEMSClient GET_client() {
		return client;
	}
}
//End of ConsoleChat class
