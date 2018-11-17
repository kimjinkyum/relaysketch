import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import java.net.InetAddress;

import java.net.ServerSocket;

import java.net.Socket;

import java.util.HashMap;
//import InetAddress;

import javax.imageio.ImageIO;

public class Server{
   static  DataInputStream dis=null;
     static FileOutputStream fos=null;
     static BufferedOutputStream bos=null;

static int asequence=0,bsequence=0,AC=0,BC=0;



   /*The relay sketch server port number- coonet client */
 static int Acheck=0,Bcheck=0;
 static Socket send=new Socket();
   private static final int PORT = 5880;
    /*The string is the key(users)of clients in the chat room
     * so that we can check that new clients are not registering name already in use.
     *Then mapping users(users+team) and printwriter.
     * users format=<A>name / <B>name
     *
     * */
    private static HashMap<String, PrintWriter>users=new HashMap<String, PrintWriter>();
   private static HashMap<String, PrintWriter>team_a=new HashMap<String, PrintWriter>();  
   private static HashMap<String, PrintWriter>team_b=new HashMap<String, PrintWriter>();  

   public static void main(String[] args) throws Exception {
      System.out.println("The relay sketch game server is running.");

     //System.out.println(InetAddress.getLocalHost());

      /*try {
         InetAddressip ip=InetAddress.getLocalHost(); 
         System.out.println("Host Name="+ip.getHostName);
         
      }catch(Exception e) {
         System.out.println(e);
         
      }*/
      
       ServerSocket listener = new ServerSocket(PORT);
       //해당된 포트로 들어올 수 있게 함
       
           try {
               while (true) {
                   new Handler(listener.accept()).start();
               }
           } finally {
               listener.close();
           }
       }    
   
   private static class Handler extends Thread {
            private String name;
            private Socket socket;
            private BufferedReader in;
            private PrintWriter out;
            private String team;

            /**

             * Constructs a handler thread, squirreling away the socket.

             * All the interesting work is done in the run method.

             */

            public Handler(Socket socket) {

                this.socket = socket;

            }

            public String getName(String name) 

            {

               return this.name;

            }

            public String getTeam(String team) 

            {

               return this.team;

            }

            /**

             * Services this thread's client by repeatedly requesting a

             * screen name until a unique one has been submitted, then

             * acknowledges the name and registers the output stream for

             * the client in a global set, then repeatedly gets inputs and

             * broadcasts them.

             */
            public void run() {
                try {
                    // Create character streams for the socket.
                   in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                      out = new PrintWriter(socket.getOutputStream(), true);


                    
                    // Request a name from this client.  Keep requesting until
                    // a name is submitted that is not already used.  Note that
                    // checking for the existence of a name and adding the name
                    // must be done while locking the set of users.
                    while (true) {
                       /**/
                       out.println("SUBMITNAME");
                        name = in.readLine();
                        if (name == null) {
                            return;
                         }
                        
                        synchronized (users) {
                        
                           if (!team_a.containsKey(name)&&!team_b.containsKey(name)) {
                               String temp = null;
                               
                               out.println("SUBMITTEAM");
                               team=in.readLine();
                               
                               if(team.equals("A")) { 
                                  team_a.put(name,out);
                                  name="<A> "+name;
                               }
                                  
                                  

                               if(team.equals("B")) {
                                  
                                  team_b.put(name, out);
                                   name="<B> "+name;
                                
                                  }

                               /*Print new user info in chat room except new user's chat room*/

                               for (PrintWriter writer : users.values()){
                                  writer.println("MESSAGE " +"***"+name+"님이 입장하셨습니다. ***");
                               }
                               users.put(name,out);
                                break;
                            }
                           
                        }
                    }

                    // Now that a successful name has been chosen, add the
                    // socket's print writer to the set of all writers so
                    // this client can receive broadcast messages.
                    out.println("NAMEACCEPTED");

                    users.put(name, out);
                    // Accept messages from this client and broadcast them.
                    // Ignore other clients that cannot be broadcasted to.
                    // then if it is match whisper format then send distinct user, not all
                    int akk=0,bkk=0;
                    while (true) {
                        //순서를 배정해주기
                        if(team_a.size()==3&&Acheck==0) {
                             for(PrintWriter writer : team_a.values())
                              {
                                  if(asequence==0) {
                                        writer.println("SEND ");
                                       writer.println("<START>");
                                               
                                  }
                                    writer.println("MESSAGE "+"sequence : "+asequence);
                                    asequence++;
                                    
                              }
                             Acheck=1;
                            }
                             
                        if(team_b.size()==3&&Bcheck==0) {
                             for(PrintWriter writer : team_b.values())
                              {  
                                if(bsequence==0) {
                                     writer.println("SEND ");
                                    writer.println("<START>");
                                  
                                }
                                writer.println("MESSAGE "+"sequence : "+bsequence);
                              
                                bsequence++;
                            }
                             Bcheck=1;
                             }
 
                        Image k=null;
                        String input = in.readLine();
                        if(input.equals("<send>"))
                        { int wow=0;
                            if(name.startsWith("<A> ")) {
                            
                               for(PrintWriter writer : team_a.values())
                                  {  
                                  if(AC==akk)
                                   { 
                                  
                                  
                                  while(wow==0) {
                                   ServerSocket soc=new ServerSocket(11111);

                                 int count=0;
                                    writer.println("<CANVAS>");
                                     System.out.println("Server start");
                                     send=soc.accept();
                                     System.out.println("Client accept");
                                     InputStream in2 =null;
                                     FileOutputStream out2=null;
                                     
                                     in2=send.getInputStream();
                                     DataInputStream din=new DataInputStream(in2);
                                   
                                       int data=din.readInt();
                                         File file=new File("C:\\2-2\\kkkk.png");
                                        out2=new FileOutputStream(file);
                                        
                                        byte[] buffer = new byte[5000];
                                        int len;
                                        int temp;
                                        temp=data;
                                        System.out.println(data);
                                        for(;data>0;data--)
                                        {
                                           len=in2.read(buffer);
                                           out2.write(buffer,0,len);
                                           String queue=in.readLine();
                                           if(Integer.toString(len).equals(queue))
                                           {
                                              count++;
                                              System.out.println("hiyo");
                                           }
                                            System.out.println(len);
                                        }
                                        System.out.println("count : "+count);
                                        if(count==temp)
                                        {   
                                           System.out.println(count);
                                          wow=1; 
                                        }
                                        System.out.println("wow = "+wow);
                                       System.out.println("dfadsf");
                                        out2.flush();
                                         out2.close();
                                      writer.println("<out>");
                                      soc.close();
                                      System.out.println("hidy");
                                        
                                   }
                                  
                                  
                                  
                                  }
                                  System.out.println("akk : "+akk);
                                   System.out.println("ACC : "+AC);
                                   
                                  if(AC+1==akk)
                                  {  
                                     writer.println("SEND");
                                    writer.println("<START>");
                                    AC++;
                                  break;
                                  }
                                  
                                  akk++;
                                  }
                                 }
                            if(name.startsWith("<B> ")) {
                                for(PrintWriter writer : team_b.values())
                                 {   if(BC==akk)
                                 { 
                                   ServerSocket soc=new ServerSocket(11111);
                                     
                                  writer.println("<CANVAS>");
                                   System.out.println("Server start");
                                   send=soc.accept();
                                   System.out.println("Client accept");
                                   InputStream in2 =null;
                                   FileOutputStream out2=null;
                                   
                                   in2=send.getInputStream();
                                   DataInputStream din=new DataInputStream(in2);
                                 
                                     int data=din.readInt();
                                       File file=new File("C:\\2-2\\kkkk.png");
                                      out2=new FileOutputStream(file);
                                      
                                      byte[] buffer = new byte[5000];
                                      int len;
                                      System.out.println(data);
                                      for(;data>0;data--)
                                      {
                                         len=in2.read(buffer);
                                         out2.write(buffer,0,len);
                                          System.out.println(len);
                                      }
                                     System.out.println("dfadsf");
                                      out2.flush();
                                       out2.close();
                                    writer.println("<out>");
                                    soc.close();
                                    System.out.println("hidy");
                                
                                 }
                                System.out.println("bkk : "+bkk);
                                 System.out.println("ACC : "+BC);
                                 
                                if(BC+1==bkk)
                                {  
                                   writer.println("SEND");
                                  writer.println("<START>");
                                  BC++;
                                break;
                                }
                                
                                bkk++;
                                }
                               
                        }        
                            }                
                        if (input == null){

                           return;
                        }
                        
                        //pop up the drowing board to first man of team.
                       
                        
                        /*braodcast message all user but same team!*/
                           for(HashMap.Entry<String,PrintWriter> entry : users.entrySet()){
                             if(name.startsWith("<A> ")) {
                                for (PrintWriter writer : team_a.values()){
                                      writer.println("MESSAGE " +name+": "+input);
                                   }
                                break;
                             }
                             if(name.startsWith("<B> ")){
                                for (PrintWriter writer : team_b.values()){
                                      writer.println("MESSAGE " +name+": "+input);
                                   }
                                break;
                             }
                          }
                        }
                } catch (IOException e) {
                    System.out.println(e);
                } finally {
                    // This client is going down!  Remove its name and its print

                   // writer from the sets, and close its socket.
                    if (name != null) {
                        users.remove(name);
                    }
                    if (out != null) {
                        users.remove(out);
                        /*The exit user info(name) broadcast all user. */
                        for (PrintWriter writer : users.values()){
                          writer.println("MESSAGE " + "***"+name+"님이 퇴장하셨습니다. ***");
                       }
                    }

                    try {
                       socket.close();

                    } catch (IOException e) {

                    }
                }
            }
        }
    }