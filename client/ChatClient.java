// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String loginID;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) throws IOException {
    super(host, port); //Call the superclass constructor
    this.loginID = loginID;
    this.clientUI = clientUI;
    openConnection();
  }
  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message) {

	  if (message.contains("#")){
		  getCommand(message.substring(message.indexOf('#')));
	  }
	  if (!(message==null||message.equals(""))) {
		  try
		    {
		      sendToServer(message);
		    }
		  catch(IOException e)
		    {
		      clientUI.display("Could not send message to server.  Terminating client.");
		      quit();
		    }
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    
    System.exit(0);
  }

  @Override
  protected void connectionClosed() {

	  System.out.println("The server is disconnected.");
  	}

  @Override
  public void connectionException(Exception exception) {

	  System.out.println("The server ???.");
  }
  
  private void getCommand(String message){
	  
	  String command = message;
	  if(command.contains(" ")) {
		  command = command.split(" ")[0];
		  treatCommand(command, command.split(" ")[1]);
		  message = message.substring(message.indexOf(" "+1));
		  if (message.contains("#")) {
			  getCommand(message.substring(message.indexOf("#")));
		  }
	  } else {
		  treatCommand(command, null);
	  }
  }
  
  private void treatCommand(String command, String message) {
	  if(command.equals("#quit")) {
		  quit();
	  } else if(command.equals("#logoff")) {
		  try
		    {
		      closeConnection();
		    }
		    catch(IOException e) {}
		    
	  } else if(command.equals("#sethost")) {
		    if(!isConnected() && message != null) {
		    	setHost(message);
		    } else {
		    	System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOON grrr");
		    }
	  } else if(command.equals("#setport")) {
		  if(!isConnected() && message != null) {
		    	setPort(Integer.parseInt(message));
		    } else {
		    	System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOON grrr");
		    }
	  } else if(command.equals("#login")) {
		  try {
			openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  } else if(command.equals("#gethost")) {
		  System.out.println(getHost());
	  } else if(command.equals("#getport")) {
		  System.out.println(getPort());
	  }
  }
}
//End of ChatClient class
