import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    static ArrayList<String> userName=new ArrayList<String>();
    static ArrayList<PrintWriter> printWriters=new ArrayList<PrintWriter>();
     String message;
    public static cipherAlg DES= new cipherAlg();

    public static void main(String[] args) throws Exception {

        //String encryptedMessage= DES.encrypt("Alvine");
       // System.out.println(encryptedMessage);
        System.out.println("Waiting for Clients....");
        ServerSocket ss = new ServerSocket(4099);

        //server should continually wait for incoming clients connections
        while (true)
        {
            Socket soc = ss.accept();
            System.out.println("Connection established");
            Conversationhandler handler= new Conversationhandler(soc);
            handler.start();

        }
    }
}
  class Conversationhandler extends Thread
  {
            Socket socket;
            BufferedReader in;
            PrintWriter out;
            String name;

            public Conversationhandler(Socket socket) throws IOException{
               this.socket=socket;
           }
           public void run()
           {
               try
               {
                   in= new BufferedReader((new InputStreamReader(socket.getInputStream())));
                   out=new PrintWriter(socket.getOutputStream(), true);

                   //loop will go on untill a unique name is entered
                   int count=0;

                   while(true)
                   {
                       if (count > 0) {
                           out.println("User already exists, enter a unique name");
                       }
                       else
                       {
                           out.println("Name required");
                       }
                       name = in.readLine();

                      if(name==null)
                      {
                           return;
                       }
                      if(!ChatServer.userName.contains(name))
                      {
                          ChatServer.userName.add(name);
                          break;
                      }

                        count++;

                   }
                   out.println("Name Accepted" + name);
                   ChatServer.printWriters.add(out);
                   while(true)
                   {
                       String message=in.readLine();

                       if(message==null)
                       {
                           return;
                       }
                       for(PrintWriter writer:ChatServer.printWriters){
                           writer.println(name + ": " + message);
                       }
                   }
               }
               catch (Exception e){
                   System.out.println(e);
               }
           }

        }




