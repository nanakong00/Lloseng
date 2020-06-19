// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
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

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  {
    super(port);
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    if (msg.toString().startsWith("#login")) {
      String[] message = msg.toString().split(" ");
      if (message.length != 0) {
        if (client.getInfo("loginID") == null) {
          client.setInfo("loginID", message[1]);
          this.sendToAllClients(client.getInfo("loginID") + " has logged on.");
        } else {
          try {
            client.sendToClient("Username cannot be changed.");
          } catch (IOException e) {
            //
          }
        }
      }
    } else {
      if (client.getInfo("loginID") == null) {
        try {
          client.sendToClient("Error: No username provided.");
          client.close();
        } catch (IOException e) {
          //
        }
      } else {
        System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
        this.sendToAllClients(client.getInfo("loginID") + "> " + msg);
      }
    }
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }


  public void handleMessageFromServerUI(String message)
{
  if (message.startsWith("#")) {
    String[] cmd = message.split(" ");
    switch (cmd[0]) {
      case "#quit":
        try {
          close();
          System.out.println("Connection terminated.");
        } catch (IOException e) {
          System.exit(0);
        }
        break;
      case "#stop":
        this.sendToAllClients("Server has stopped listening for new clients.");
        this.stopListening();
        break;
      case "#close":
        try {
          this.close();
          System.out.println("Connection terminated.");
        } catch (IOException e) {
          System.out.println("Unable to terminate connection.");
        }
        break;
      case "#setport":
        if (!isListening()) {
          this.setPort(Integer.parseInt(cmd[1]));
          System.out.println("New port has been set.");
        } else {
          System.out.println("Server must be closed.");
        }
        break;
      case "#start":
        if (!isListening()) {
          try {
            this.listen();
          } catch (IOException e) {
            System.out.println("Error to listen");
          }
        } else {
          System.out.println("Server must be stopped.");
        }
        break;
      case "#getport":
        System.out.println("Current port: " + this.getPort());
        break;
    }
  } else {
      this.sendToAllClients("SERVER MSG> " + message);
    }
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

  //for 5c
  protected void clientConnected(ConnectionToClient client){
    System.out.println("Client connect sucessful...");
  }
  synchronized protected void clientDisconnected(ConnectionToClient client){
    System.out.println("Client connect failed...");
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
}
//End of EchoServer class
