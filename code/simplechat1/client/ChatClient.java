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

  public ChatClient(String loginID,String host, int port, ChatIF clientUI)
    throws IOException
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
    this.sendToServer("#login:" + loginID);
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
  public void handleMessageFromClientUI(String message)
  {
    if(message.startWith("#")){
      String cmd = message.split(" ");
      switch (cmd[0]) {
        case "#quit":
          clientUI.display("Terminating connection.");
          quit();
          break;
        case "#logoff":
          try{
            clientUI.display("logging off.");
            closeConnection();
          }catch(IOException e){
            System.out.println(" Error logging off.");
          }
        case "#sethost":
          if(! isConnected()){
            this.setHost(cmd[1]);
            clientUI.display("New host has been set.");
          }else{
            System.out.println("Must be logged off!");
          }
          break;
        case "#setport":
          if(! isConnected()){
            this.setPort(Integer.parseInt(cmd[1]));
            ClientUI.display("New Port has benn set.");
          }else{
            System.out.println("Must be logged off.");
          }
          break;
        case "#login":
          try{
            openConnection();
          }catch (IOException e){
            System.out.println("Must not be connected.");
          }
          break;
        case "#gethost":
          clientUI.display("Current host: " + this.getHost());
          break;
        case "#getport":
          ClientUI.display("Current port: " + this.getPort());
          break;
      }
    }else{
      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display
          ("Could not send message to server.  Terminating client.");
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

  public void connectionClosed(){
    try{
      if(!isConnected()){
        closeConnection();
      }
    }catch(IOException e){
      connectionException(e);
    }
  }
  protected void connectionException(Exception exception){
    System.out.println("The serve has shut down, qutting...");
    System.exit(0);
  }
}
//End of ChatClient class
