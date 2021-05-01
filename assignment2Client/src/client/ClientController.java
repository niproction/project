// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import client.*;
import common.ChatIF;
import gui.GUIcontroller;
import javafx.collections.FXCollections;
import logic.test;

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
public class ClientController implements ChatIF {
	// Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	public static int DEFAULT_PORT;

	// Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	ChatClient client;
	private GUIcontroller controller;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientConsole UI.
	 *
	 * @param host     The host to connect to.
	 * @param port     The port to connect on.
	 * @param clientUI
	 */
	public ClientController(String host, int port) {
		try {
			client = new ChatClient(host, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}

	// Instance methods ************************************************

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 */
	public void accept(Object list) {
		client.handleMessageFromClientUI(list);
	}

	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void reciveController(GUIcontroller controller) {
		this.controller=controller;
	}
	@Override
	public void display(Object message) {
		System.out.println("> " + message);
		if (message instanceof ArrayList<?>) {
			test t = new test(((ArrayList<String>) message).get(0), ((ArrayList<String>) message).get(1),
					((ArrayList<String>) message).get(2), ((ArrayList<String>) message).get(3),
					((ArrayList<String>) message).get(4));
			System.out.println(t.getTestDuration());
			if (controller.tests.contains(t)) {
				System.out.println("enter 1");
				for (int i = 0; i < controller.tests.size(); i++) {
					if (controller.tests.get(i).equals(t)) {
						System.out.println("enter 2");
						System.out.println(i);
						controller.tests.add(0, t);
						controller.tests.remove(i+1);
					}
				}
			}
			else {
				controller.tests.add(0,t);
			}
			controller.testInfoTable.setItems(controller.tests);
		} else if (((String) message).equals("There is no test with this id")) {
			controller.errorlabel.setVisible(true);
		} else if (((String) message).equals("Update succeeded")) {
			System.out.println(controller.saveValue);
			//controller.arr = new ArrayList<>();
			//controller.arr.add(controller.saveValue);
			controller.updated = true;
			controller.updatesuccededLabel.setVisible(true);
		}
		else if(((String) message).equals("Update failed"))
			controller.errorlabel.setVisible(true);

	}
//End of ConsoleChat class
}
