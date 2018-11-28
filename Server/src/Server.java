import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.net.InetAddress;

import java.net.ServerSocket;

import java.net.Socket;

import java.util.HashMap;
//import InetAddress;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Server {

   static int asequence = 0, bsequence = 0, AC = 0, BC = 0;
   static String[] names1=new String[6];
   static int howmuch=0;
   static int APASScheck=0,BPASScheck=0;
   /* The relay sketch server port number- coonet client */
   static int Acheck = 0, Bcheck = 0;
   static Socket send2 = new Socket();
   
   private static final int PORT = 5880;
   static int chattingcheck = 0;
   static OutputStream in3 = null;
   static FileOutputStream out3 = null;

   static OutputStream in6 = null;
   static FileOutputStream out6 = null;
   

   static int ateamout = 0, bteamout = 0;
   static int a=0,b=0;//�ҿ�

   /*
    * The string is the key(users)of clients in the chat room so that we can check
    * that new clients are not registering name already in use. Then mapping
    * users(users+team) and printwriter. users format=<A>name / <B>name
    *
    */
   private static HashMap<String, PrintWriter> users = new HashMap<String, PrintWriter>();
   private static HashMap<String, PrintWriter> team_a = new HashMap<String, PrintWriter>();
   private static HashMap<String, PrintWriter> team_b = new HashMap<String, PrintWriter>();
   
   private static String word[][] = new String[6][30];
   private static String category[] = new String[6];
   private static int score_a=0;
   private static int score_b=0;
   private static int choice_category;
   private static String choice_word[]=new String[30];
   private static int size_word=0;
   private static int indexword_a=0;
   private static int indexword_b=0;
   public static void main(String[] args) throws Exception {
      System.out.println("The relay sketch game server is running.");

   
      random_category();//����
      
      ServerSocket listener = new ServerSocket(PORT);
      // �ش�� ��Ʈ�� ���� �� �ְ� ��

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
      private BufferedReader in5;

      /**
       * 
       * Constructs a handler thread, squirreling away the socket.
       * 
       * All the interesting work is done in the run method.
       * 
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
       * 
       * Services this thread's client by repeatedly requesting a
       * 
       * screen name until a unique one has been submitted, then
       * 
       * acknowledges the name and registers the output stream for
       * 
       * the client in a global set, then repeatedly gets inputs and
       * 
       * broadcasts them.
       * 
       */
      @SuppressWarnings("resource")
      public void run() {
         try {
            // Create character streams for the socket.
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Request a name from this client. Keep requesting until
            // a name is submitted that is not already used. Note that
            // checking for the existence of a name and adding the name
            // must be done while locking the set of users.
            while (true) {
               out.println("<SUBMITNAME>");
               name = in.readLine();
               if (name == null) 
               {
                  return;
               } // if(name ==null) �� ����

               synchronized (users) {

                  if (!team_a.containsKey(name) && !team_b.containsKey(name)) {
                     String temp = null;

                     out.println("<SUBMITTEAM>");
                     team = in.readLine();


                     if (team.equals("A")) {
                         if(a>=3) {
                            team_b.put(name, out);

                            name = "<B> " + name;
                            b++;
                         }
                            
                         else {
                         team_a.put(name, out);

                         name = "<A> " + name;
                         a++;//�ҿ�
                         }
                         System.out.println("\na: "+a+"\n");
                      } // if(team==A)����



                      if (team.equals("B")) {

                         if(b>=3) {
                            team_a.put(name, out);

                            name = "<A> " + name;
                            a++;//�ҿ�
                            }
                         else {
                         team_b.put(name, out);

                         name = "<B> " + name;
                         b++;//�ҿ�
                         }

                      } // if(team==b )����

                     out.println("<GAMEFRAME>");
                     /* Print new user info in chat room except new user's chat room */

                     for (PrintWriter writer : users.values()) {
                        writer.println("MESSAGE " + "***" + name + "���� �����ϼ̽��ϴ�. ***");
                     } // for(printwriter)
                     users.put(name, out);
                     
                     if (users.size() + ateamout
                           + bteamout == 3/* &&team_a.size()+ateamout==3&&team_b.size()+bteamout==3 */) 
                     {
                        for (PrintWriter writer : users.values()) 
                        {
                           writer.println("<ALLIN>");
                        }//for
                        for (PrintWriter writer : team_a.values()) {
                           int aseq = asequence + 1;
                           writer.println("<SEQUENCE>" + aseq);
                           writer.println("<GIVECATEGORY>");//����
                           writer.println(category[choice_category]);//����
                           
                           if (asequence == 0) {

                              writer.println("<GIVEWORD>");//����
                              writer.println(choice_word[indexword_a]);//����
                              indexword_a++;//����
                              writer.println("<SEND> ");
                              writer.println("<START>");
                              chattingcheck = 1;
                           }//if(aseuqeunce==0)

                           asequence++;

                        }//for(team a)
                        for (PrintWriter writer : team_b.values()) 
                        {
                           int bseq = bsequence + 1;

                           writer.println("<SEQUENCE>" + bseq);
                           writer.println("<GIVECATEGORY>");//����
                           writer.println(category[choice_category]);//����
                           
                           if (bsequence == 0) 
                           {
                              writer.println("<GIVEWORD>");//����
                              writer.println(choice_word[indexword_b]);//����
                              indexword_b++;//����
                              writer.println("<SEND> ");
                              writer.println("<START>");
                              chattingcheck = 1;
                           }//if(bsequence==0)
                           bsequence++;
                        }//for(team_b)
                     }//if(all in one)
                     break;
                  }//if�� (�̸� Ȯ��)

               }//��ũ��

            }//while��
            
            // Now that a successful name has been chosen, add the
            // socket's print writer to the set of all writers so
            // this client can receive broadcast messages.
            out.println("<NAMEACCEPTED>");
            names1[howmuch]=name;//�����//�����//�����
            howmuch++;
            
            users.put(name, out);
            for(PrintWriter writer : users.values())
            {  
               writer.println("<TEAMVIEW>");
               writer.println(users.size());
                for(int i=0; i<howmuch; i++) {
               writer.println(names1[i]);
                }
            }
            // Accept messages from this client and broadcast them.
            // Ignore other clients that cannot be broadcasted to.
            // then if it is match whisper format then send distinct user, not all
           int aregame = 0, bregame = 0;
            while (true) 
            {
               // ������ �������ֱ�
               String input="";
                 int akk = 0, bkk = 0;
                 if (aregame == 1) {
                      for (PrintWriter writer : team_a.values()) 
                      {
                         if(aregame==1) {
                         writer.println("<GIVEWORD>");//����
                         writer.println(choice_word[indexword_a]);//����
                         indexword_a++;//����
                         writer.println("<SEND> ");
                         writer.println("<START>");
                         AC = 0;}
                         aregame=0;
                         
                      }//for (printWritera)
                      input = in.readLine();
                   }//if(aregame==1)
                 else if (bregame == 1) {
                      for (PrintWriter writer : team_b.values()) 
                      {
                         if(bregame==1) {writer.println("<GIVEWORD>");//����
                         writer.println(choice_word[indexword_b]);//����
                         indexword_b++;//����
                         writer.println("<SEND> ");
                         writer.println("<START>");
                         BC = 0;}
                         bregame=0;
                         
                      }//for(printwriter b)
                      input = in.readLine();
                   }//if( bregame==1) 
                 else
                 {
                    input=in.readLine();
                 }
                 System.out.println("END");
               Image k = null;

               System.out.println("END");
               if(input.length()>9) {
               if(input.substring(0,9).equals("<Aanswer>"))//���
               {
                   System.out.println(input.substring(9));
                   aregame=1;
                   if (input.substring(9).equals((choice_word[indexword_a-1]))) {
                      System.out.println("CORRECT");
                   }//if(���� ���� ������) 
                  else 
                  {
                     System.out.println("INCORRECT");
                  }//else (���� �� Ʋ����)
                   String result=is_answer(input.substring(9),indexword_a-1,"A");
                   out.println(result);
                   
                   
                   for (PrintWriter writer : team_a.values()) 
                   {
                      writer.println("<SCORE>");
                      writer.println(score_a+":"+score_b);
                         
                   }   
                   
                   APASScheck=0;
                   
               }
               
               if(input.substring(0,9).equals("<Banswer>"))
               {
                   System.out.println(input.substring(9));
                   bregame=1;
                   if (input.substring(9).equals((choice_word[indexword_b-1]))) {
                      System.out.println("CORRECT");
                   }//if(���� ���� ������) 
                  else 
                  {
                     System.out.println("INCORRECT");
                  }//else (���� �� Ʋ����)
                   String result=is_answer(input.substring(9),indexword_b-1,"B");
                   out.println(result);
                   for (PrintWriter writer : team_b.values()) 
                   {
                      writer.println("<SCORE>");
                      writer.println(score_b+":"+score_a);
                         
                   }
                   BPASScheck=0;
               }
               
               }
               if (input.equals("<send>")) {
            	   System.out.println("inbaby");
                  int wow = 0;
                  int wow1 = 0;
                  int wow3 = 0;
                  int wow4 = 0;
                  // Ateam get the picture from client.
                  for (PrintWriter writer : team_a.values()) {
                  if (name.startsWith("<A> ")) {
                	 
                	  Socket send = new Socket();
                	  FileInputStream fin2;
                     if (AC == akk) {
                    	 if(AC==0)
                         {
                        	 APASScheck=1; //signal to stop sending "nothing"
                         }   
                    	 while (wow == 0) {
                               chattingcheck = 1;
                               ServerSocket soc2 = new ServerSocket(11111);
                               
                               int count = 0;
                               writer.println("<CANVAS>");
                               System.out.println("Server start");
                               send = soc2.accept();
                               System.out.println("Client accept");
                               InputStream in2 = null;
                               FileOutputStream out2 = null;

                               in2 = send.getInputStream();
                               DataInputStream din = new DataInputStream(in2);

                               int data = din.readInt();
                               File file = new File("C:\\2-2\\server_get.png");
                               out2 = new FileOutputStream(file);

                               byte[] buffer = new byte[5000];
                               int len;
                               int temp;
                               temp = data;
                               System.out.println(data);
                               for (; data > 0; data--) {
                                  len = in2.read(buffer);
                                  out2.write(buffer, 0, len);
                                  String queue = in.readLine();
                                  if (Integer.toString(len).equals(queue)) {
                                     count++;
                                     System.out.println("hiyo");
                                  }//if(len)
                                  System.out.println(len);
                               }//for(������ �о���°�)
                               System.out.println("count : " + count);
                               if (count == temp) {
                                  System.out.println(count);
                                  wow = 1;
                               }//if(�� �Դ��� Ȯ��)
                               System.out.println("wow = " + wow);
                               System.out.println("dfadsf");
                               out2.flush();
                               out2.close();
                               writer.println("<out>");
                               soc2.close();
                               System.out.println("hidy");

                            }//while(wow==0)
                            chattingcheck = 0;
                         
                         }//if(ac=akk)
                         
                        System.out.println("akk : " + akk);
                         System.out.println("ACC : " + AC);
                         if (AC + 1 == akk) {
                            
                            chattingcheck = 1;
                            // give picture to client.

                            while (wow1 == 0) {
                               chattingcheck = 1;
                               ServerSocket soc = new ServerSocket(22222);

                               int count = 0;
                               writer.println("<RECEIVE>");
                               System.out.println("Server start");
                               send = soc.accept();
                               System.out.println("Client accept");

                               in3 = send.getOutputStream();
                               DataOutputStream din = new DataOutputStream(in3);
                               DataInputStream dout1 = new DataInputStream(send.getInputStream());

                               fin2 = new FileInputStream(new File("C:\\2-2\\server_get.png"));

                               byte[] buffer2 = new byte[5000];
                               int len;
                               int data = 0;
                               while ((len = fin2.read(buffer2)) > 0) {
                                  data++;
                               }//while������
                               din.writeInt(data);
                               fin2.close();
                               int temp;
                               temp = data;
                               System.out.println(data);
                               fin2 = new FileInputStream(new File("C:\\2-2\\server_get.png"));

                               for (; data > 0; data--) {
                                  len = fin2.read(buffer2);
                                  in3.write(buffer2, 0, len);
                                  writer.println(len);
                                  System.out.println(len);
                               }//������ ����

                               System.out.println("wow1 = " + wow1);
                               System.out.println("dfadsf");

                               int ch1 = dout1.readInt();
                               System.out.println("end : " + ch1);
                               soc.close();
                               System.out.println("hidy");
                               if (ch1 == 1) {
                                  wow1 = 1;
                               }//if��
System.out.println("akk : "+akk);
                            }//while(wow==0 ���� üũ)
                            
                            if(akk==2) {
                                 System.out.println("===="+choice_word[indexword_a-1]);
                               writer.println("<AANSWERSHEET>");
                              }
                            else {
                               writer.println("<SEND>");
                               writer.println("<START>");
                              
                               }
                            //else(AC==2) �ƴҶ�
                            AC++;
                            break;  
                         }//if(Ac=akk+1)

                        akk++;
                     }//teamA �� print Writer
                  }//if A���϶�
                  
                  // Bteam get the picture from client.
                  if (name.startsWith("<B> ")) {
                	  FileInputStream fin3;
                	 
                     for (PrintWriter writer : team_b.values()) {
                        if (BC == bkk) {
                        	 if(BC==0)
                             {
                            	 BPASScheck=1;
                             }   	
                           while (wow3 == 0) {
                              chattingcheck = 1;
                              ServerSocket soc = new ServerSocket(33333);

                              int count = 0;
                              writer.println("<BCANVAS>");
                              System.out.println("Server start");
                              send2 = soc.accept();
                              System.out.println("Client accept");
                              InputStream in4 = null;
                              FileOutputStream out4 = null;

                              in4 = send2.getInputStream();
                              DataInputStream din = new DataInputStream(in4);

                              int data = din.readInt();
                              File file = new File("C:\\2-2\\server_getB.png");
                              out4 = new FileOutputStream(file);

                              byte[] buffer = new byte[5000];
                              int len;
                              int temp;
                              temp = data;
                              System.out.println(data);
                              for (; data > 0; data--) {
                                 len = in4.read(buffer);
                                 out4.write(buffer, 0, len);
                                 String queue = in.readLine();
                                 if (Integer.toString(len).equals(queue)) {
                                    count++;
                                    System.out.println("hiyo");
                                 }//if ������ ���� Ȯ��
                                 System.out.println(len);
                              }//for�� ������ �б�
                              System.out.println("count : " + count);
                              if (count == temp) {
                                 System.out.println(count);
                                 wow3 = 1;
                              }//if �ٿԴ��� Ȯ��
                              System.out.println("wow = " + wow3);
                              System.out.println("dfadsf");
                              out4.flush();
                              out4.close();
                              writer.println("<out>");
                              soc.close();
                              System.out.println("hidy");

                           }//while(wow==3)
                           chattingcheck = 0;

                        }//if(BC=bkk)
                        System.out.println("bkk : " + bkk);
                        System.out.println("BCC : " + BC);

                        if (BC + 1 == bkk) {
                           chattingcheck = 1;
                           // give picture to client.

                           while (wow4 == 0) {
                              chattingcheck = 1;
                              ServerSocket soc = new ServerSocket(44444);

                              int count = 0;
                              writer.println("<BRECEIVE>");
                              System.out.println("Server start");
                              send2 = soc.accept();
                              System.out.println("Client accept");

                              in6 = send2.getOutputStream();
                              DataOutputStream din2 = new DataOutputStream(in6);
                              DataInputStream dout2 = new DataInputStream(send2.getInputStream());

                              fin3 = new FileInputStream(new File("C:\\2-2\\server_getB.png"));

                              byte[] buffer2 = new byte[5000];
                              int len;
                              int data = 0;
                              while ((len = fin3.read(buffer2)) > 0) {
                                 data++;
                              }//while(������ �б�)
                              din2.writeInt(data);
                              fin3.close();
                              int temp;
                              temp = data;
                              System.out.println(data);
                              fin3 = new FileInputStream(new File("C:\\2-2\\server_getB.png"));

                              for (; data > 0; data--) {
                                 len = fin3.read(buffer2);
                                 in6.write(buffer2, 0, len);
                                 writer.println(len);
                                 System.out.println(len);
                              }//for�� ������ ������

                              System.out.println("wow1 = " + wow4);
                              System.out.println("dfadsf");

                              int ch1 = dout2.readInt();
                              System.out.println("end : " + ch1);
                              soc.close();
                              System.out.println("hidy");
                              if (ch1 == 1) {
                                 wow4 = 1;
                              }//ifȮ���ϴ°�

                           }//while(wow4==0) Ȯ��
                              if(bkk==2) {
                                   System.out.println("===="+choice_word[indexword_b-1]);
                                 writer.println("<BANSWERSHEET>");
                                }
                           
                           //if(BC==1)- ������������� Ȯ�� 
                           else {
                              writer.println("<SEND>");
                              writer.println("<START>");
                              
                           }//else(BC!=1)-������ ��� X
                              BC++;
                              break;  
                        }//if(BC==bkk+1)Ȯ��
                        
                        bkk++;
                     }//for(B�� writer)

                  }//if(������ B�ϋ�)
               }//if(Ŭ���̾�Ʈ���� send������)
               if(input.equals("<PASS>")) //when the pass button is pressed
               {
            	  
            	   if (name.startsWith("<A> ")) {
            	    for (PrintWriter writer : team_a.values()) 
                    {                       
                        writer.println("<GIVEWORD>");//����
                        writer.println(choice_word[indexword_a]);//����
                        indexword_a++;//����
                        break;    
                    }//send the next word to first person.
            	    }
            	   else if(name.startsWith("<B> "))
            			   {
            		    for (PrintWriter writer : team_b.values()) 
                       {                       
                             writer.println("<GIVEWORD>");//����
                             writer.println(choice_word[indexword_b]);//����
                             indexword_b++;//����
                             break;  
            			   }
               }
               if (input == null) 
               {

                  return;
               }
               System.out.println("anjrh "+input);
               if(APASScheck==0) {
               for (PrintWriter writer : team_a.values()) 
               {                       
                   writer.println("nothing");//����
                   break;    
               }}
               if(BPASScheck==0) {
               for (PrintWriter writer : team_b.values()) 
               {                       
                 writer.println("nothing");
                 break;
       	       } }} // make client not to wait input.
               // pop up the drowing board to first man of team.

               if (chattingcheck == 0) {
                  if (input.equals("<send>")) 
                  {
                     continue;
                  }
                  /* braodcast message all user but same team! */
                  for (HashMap.Entry<String, PrintWriter> entry : users.entrySet()) {
                     if (name.startsWith("<A> ")) {
                        for (PrintWriter writer : team_a.values()) {
                           writer.println("MESSAGE " + name + ": " + input);
                        }//for�� (printwriter-a)
                        break;
                     }//�̸� A������ ����
                     if (name.startsWith("<B> ")) {
                        for (PrintWriter writer : team_b.values()) {
                           writer.println("MESSAGE " + name + ": " + input);
                        }//for(printwriter-b)
                        break;
                     }//if �̸� B������ ����
                  }//ū for��
               }//if(Chatting check==0 -ä�� ���)
            }//���� ū while��!

         }//try
         catch (IOException e) {
            System.out.println(e);
         } finally 
         {
            // This client is going down! Remove its name and its print

            // writer from the sets, and close its socket.
            if (name != null) {
               users.remove(name);
               if (name.startsWith("<A> ")) {
                  team_a.remove(name);
                  ateamout--;
               }//if(�̸� A��)
               if (name.startsWith("<B> ")) 
               {
                  team_b.remove(name);
                  bteamout--;
               }//if(�̸� B��)
            }//if(name!=null)
            if (out != null) 
            {
               users.remove(out);
               /* The exit user info(name) broadcast all user. */
               for (PrintWriter writer : users.values()) {
                  writer.println("MESSAGE " + "***" + name + "���� �����ϼ̽��ϴ�. ***");
               }//for(Printwriter-����ҽ�)
            }//if(out!=null)

            try {
               socket.close();

            } catch (IOException e) {

            }//catch
         }//finally
      }//run �޼ҵ�
   }//handler class
   
   
   private static void random_category() throws IOException 
   {   
      read_file();
      Random random=new Random();
      choice_category=random.nextInt(6);//0~5
      random_word();
   }//����

   private static void random_word() 
   {
      
      for(int i=0;i<word[choice_category].length;i++) 
      {
         if(word[choice_category][i]==null)
            break;
         size_word++;
      }
      String temp[]=new String[size_word];
      int random[]=new int[size_word];
      Random r=new Random();
      for(int i=0;i<size_word;i++) 
      {
         random[i]=r.nextInt(size_word);
         for(int j=0;j<i;j++) 
         {
            if(random[i]==random[j])
               i--;
         }
      }
      for(int i=0;i<size_word;i++) 
      {
         choice_word[i]=word[choice_category][random[i]];
         
      }
   }//����
   private static String is_answer(String answer, int number,String team)/*number�� ���° �������ε�.*/ 
   {
      if(answer.equalsIgnoreCase(choice_word[number])) 
      {
         if(team.startsWith("A")) 
         {
            score_a++;
         }
         else 
         { 
            score_b++;
         }
         return "<ANSWERCORRECT>";
      }
      return "<ANSWERWRONG>"+choice_word[number];
   }//����
   private static void read_file() throws IOException {

      FileInputStream input = new FileInputStream("C:\\2-2\\category.txt");// ����
      InputStreamReader reader = new InputStreamReader(input, "UTF-8");
      BufferedReader read = new BufferedReader(reader);

      for (int i = 0; i < 6; i++) {
         category[i] = read.readLine();
         String temp[] = read.readLine().split(",");

         for (int j = 0; j < temp.length; j++) {
            word[i][j] = temp[j];
         }
      }
   }//����
}