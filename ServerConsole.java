import java.io.*;
import java.util.Scanner;

import client.*;
import common.ChatIF;

public class ServerConsole implements ChatIF{
	
	final public static int DEFAULT_PORT = 5555;

	//Instance variables **********************************************

	EchoServer server;

	Scanner fromConsole; 
	

	public ServerConsole(String host, int port){
		server= new EchoServer(port);
		// Create scanner object to read from console
		fromConsole = new Scanner(System.in); 
	}
	
	@Override
	public void display(String message) {
		System.out.println("SERVER MSG>"+message);
	}

	public void accept() 
	{
		  try
		    {

		      String message;

		      while (true) 
		      {
		        message = fromConsole.nextLine();
		        if(message.contains("#")) {
		        	server.getCommand(message);
		        }
		        server.sendToAllClients("SERVER MSG>"+message);
		        display(message);
		      }
		    } 
		    catch (Exception ex) 
		    {
		      System.out.println
		        ("Unexpected error while reading from console!!!");
		    }
	 
	}
	

	public static void main(String[] args){
		String host = "";
		int port;

		try{
			host = args[0];
		} catch(ArrayIndexOutOfBoundsException e){
			host = "localhost";
		}
		
		try{
			port = Integer.parseInt(args[1]);
		} catch(ArrayIndexOutOfBoundsException e){
			port = DEFAULT_PORT;
		}
		
		ServerConsole chat= new ServerConsole(host, port);
		chat.accept();  //Wait for console data
	}
}

