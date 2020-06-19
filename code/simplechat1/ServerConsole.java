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
 public class ServerConsole implements ChatIF{

   //Class variables *************************************************

   /**
    * The default port to connect on.
    */
   final public static int DEFAULT_PORT = 5555;

   //Instance variables **********************************************


   EchoServer server;


   //Constructors ****************************************************

   /**
    * Constructs an instance of the ServerConsole UI.
    *
    * @param host The host to connect to.
    * @param port The port to connect on.
    */
   public ServerConsole(int port)
   {
     server = new EchoServer(port);
     try{
       server.listen();
     }
     catch(IOException exception){
       System.out.println("Error: canot setup connection") + "Terminating client.";
       System.exit(1);
     }
   }


   //Instance methods ************************************************

   /**
    * This method waits for input from the console.  Once it is
    * received, it sends it to the client's message handler.
    */
   public void accept()
   {
     try
     {
       BufferedReader fromConsole =
         new BufferedReader(new InputStreamReader(System.in));
       String message;

       while (true)
       {
         message = fromConsole.readLine();
         server.handleMessageFromClientUI(message);
       }
     }
     catch (Exception ex)
     {
       System.out.println
         ("Unexpected error while reading from console!");
     }
   }

   /**
    * This method overrides the method in the ChatIF interface.  It
    * displays a message onto the screen.
    *
    * @param message The string to be displayed.
    */
   public void display(String message)
   {
     System.out.println("Server MSG>" + message);
   }


   //Class methods ***************************************************

   /**
    * This method is responsible for the creation of the Client UI.
    *
    * @param args[0] The host to connect to.
    */
   public static void main(String[] args)
   {
     int port = 0;  //The port number
     // e5b
     try {
       port = Interger.parseInt(args[0]);
     }catch(ArrayIndexOutOfBoundsException e){
       port = DEFAULT_PORT;
     }


     ServerConsole chat= new ServerConsole(port);
     chat.accept();  //Wait for console data
   }
 }

 //End of ServerConsole class
