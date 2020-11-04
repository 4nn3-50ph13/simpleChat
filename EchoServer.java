// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  Boolean isFirstConnection;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
    isFirstConnection = true;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient (Object msg, ConnectionToClient client) {
	  Scanner logID = new Scanner(System.in); 
	  String loginID = null;

	  loginID = logID.nextLine();
	  
	  if(isFirstConnection) {
		  if(loginID.length() > 6 && loginID.substring(0, 6).equals("#login") ) {
			  loginID = loginID.substring(6);
			  client.setInfo("Login ID", loginID);
			  isFirstConnection = false;
		  }
		  logID.close();
	  } else if (loginID.length() > 6 && loginID.substring(0, 6).equals("#login") ) {
		  System.out.println("ERROR : User already logged in, improper command.\nTerminating client.");
		  try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	  
    System.out.println("Message received: " + msg + " from " + client.getInfo("Login ID"));
    this.sendToAllClients(msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client connected : " + client.getName());
  }

  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client disconnected : " + client.getName());
  }


  protected void getCommand(String message){
	  
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
		  try
		    {
		      close();
		    }
		    catch(IOException e) {}
		    
		    System.exit(0);
	  } else if(command.equals("#stop")) {
		  stopListening();
		   
	  } else if(command.equals("#close")) {
		  try
		    {
		      close();
		      
		    }
		    catch(IOException e) {}
	  } else if(command.equals("#setport")) {
		  if(getNumberOfClients() == 0 && message != null) {
		    	setPort(Integer.parseInt(message));
		    } else {
		    	System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOON grrr");
		    }
	  } else if(command.equals("#start")) {
		  try {
			  if (!isListening()) {
				  listen();
			  }
		} catch (IOException e) {
			e.printStackTrace();
		}
	  } else if(command.equals("#getport")) {
			System.out.println(getPort());
	  }
  }
}
//End of EchoServer class
