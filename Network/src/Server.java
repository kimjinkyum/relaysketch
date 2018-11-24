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

public class Server {

   static int asequence = 0, bsequence = 0, AC = 0, BC = 0;

   /* The relay sketch server port number- coonet client */
   static int Acheck = 0, Bcheck = 0;
   static Socket send = new Socket();
   private static final int PORT = 5880;
   static int chattingcheck = 0;
   static OutputStream in3 = null;
   static FileOutputStream out3 = null;
   static FileInputStream fin2;

   static int ateamout = 0, bteamout = 0;
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

   
      random_category();//진겸
      
      ServerSocket listener = new ServerSocket(PORT);
      // 해당된 포트로 들어올 수 있게 함

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
               } // if(name ==null) 문 종료

               synchronized (users) {

                  if (!team_a.containsKey(name) && !team_b.containsKey(name)) {
                     String temp = null;

                     out.println("<SUBMITTEAM>");
                     team = in.readLine();

                     if (team.equals("A")) {
                        team_a.put(name, out);
                        name = "<A> " + name;
                     } // if(team==A)종료

                     if (team.equals("B")) {

                        team_b.put(name, out);
                        name = "<B> " + name;

                     } // if(team==b )종료
                     out.println("<GAMEFRAME>");
                     /* Print new user info in chat room except new user's chat room */

                     for (PrintWriter writer : users.values()) {
                        writer.println("MESSAGE " + "***" + name + "님이 입장하셨습니다. ***");
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
                           writer.println("<GIVECATEGORY>");//진겸
                           writer.println(category[choice_category]);//진겸
                           
                           if (asequence == 0) {

                              writer.println("<GIVEWORD>");//진겸
                              writer.println(choice_word[indexword_a]);//진겸
                              indexword_a++;//진겸
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
                           writer.println("<GIVECATEGORY>");//진겸
                           writer.println(category[choice_category]);//진겸
                           
                           if (bsequence == 0) 
                           {
                              writer.println("<GIVEWORD>");//진겸
                              writer.println(choice_word[indexword_b]);//진겸
                              indexword_b++;//진겸
                              writer.println("<SEND> ");
                              writer.println("<START>");
                              chattingcheck = 1;
                           }//if(bsequence==0)
                           bsequence++;
                        }//for(team_b)
                     }//if(all in one)
                     break;
                  }//if문 (이름 확인)

               }//싱크로

            }//while문
            
            // Now that a successful name has been chosen, add the
            // socket's print writer to the set of all writers so
            // this client can receive broadcast messages.
            out.println("<NAMEACCEPTED>");

            users.put(name, out);
            // Accept messages from this client and broadcast them.
            // Ignore other clients that cannot be broadcasted to.
            // then if it is match whisper format then send distinct user, not all
           int aregame = 0, bregame = 0;
            while (true) 
            {
               // 순서를 배정해주기
               String input="";
                 int akk = 0, bkk = 0;
                 if (aregame == 1) {
                      for (PrintWriter writer : team_a.values()) 
                      {
                         if(aregame==1) {
                         writer.println("<GIVEWORD>");//진겸
                         writer.println(choice_word[indexword_a]);//진겸
                         indexword_a++;//진겸
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
                         if(bregame==1) {writer.println("<GIVEWORD>");//진겸
                         writer.println(choice_word[indexword_b]);//진겸
                         indexword_b++;//진겸
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
               if(input.substring(0,9).equals("<Aanswer>"))//대답
               {
                   System.out.println(input.substring(9));
                   aregame=1;
                   if (input.substring(9).equals((choice_word[indexword_a-1]))) {
                      System.out.println("CORRECT");
                   }//if(유저 답이 맞을때) 
                  else 
                  {
                     System.out.println("INCORRECT");
                  }//else (유저 답 틀릴때)
                   String result=is_answer(input.substring(9),indexword_a-1,"A");
                   out.println(result);
                   out.println(score_a+":"+score_b);
                   
               }
               
               if(input.substring(0,9).equals("<Banswer>"))
               {
                   System.out.println(input.substring(9));
                   bregame=1;
                   if (input.substring(9).equals((choice_word[indexword_b-1]))) {
                      System.out.println("CORRECT");
                   }//if(유저 답이 맞을때) 
                  else 
                  {
                     System.out.println("INCORRECT");
                  }//else (유저 답 틀릴때)
                   String result=is_answer(input.substring(9),indexword_b-1,"B");
                   out.println(result);
                   out.println(score_b+":"+score_a);

               }
               
               }
               if (input.equals("<send>")) {
                  int wow = 0;
                  int wow1 = 0;
                  int wow3 = 0;
                  int wow4 = 0;
                  // Ateam get the picture from client.
                  for (PrintWriter writer : team_a.values()) {
                  if (name.startsWith("<A> ")) {

                     if (AC == akk) {
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
                               }//for(데이터 읽어오는거)
                               System.out.println("count : " + count);
                               if (count == temp) {
                                  System.out.println(count);
                                  wow = 1;
                               }//if(다 왔는지 확인)
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
                               }//while데이터
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
                               }//데이터 쓰기

                               System.out.println("wow1 = " + wow1);
                               System.out.println("dfadsf");

                               int ch1 = dout1.readInt();
                               System.out.println("end : " + ch1);
                               soc.close();
                               System.out.println("hidy");
                               if (ch1 == 1) {
                                  wow1 = 1;
                               }//if문

                            }//while(wow==0 인지 체크)
                            
                            if(akk==2) {
                                 System.out.println("===="+choice_word[indexword_a-1]);
                               writer.println("<AANSWERSHEET>");
                              }
                            else {
                               writer.println("<SEND>");
                               writer.println("<START>");
                              
                               }
                            //else(AC==2) 아닐때
                            AC++;
                            break;  
                         }//if(Ac=akk+1)

                        akk++;
                     }//teamA 의 print Writer
                  }//if A팀일때
                  
                  // Bteam get the picture from client.
                  if (name.startsWith("<B> ")) {
                     for (PrintWriter writer : team_b.values()) {
                        if (BC == bkk) {

                           while (wow3 == 0) {
                              chattingcheck = 1;
                              ServerSocket soc = new ServerSocket(11111);

                              int count = 0;
                              writer.println("<CANVAS>");
                              System.out.println("Server start");
                              send = soc.accept();
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
                                 }//if 데이터 길이 확인
                                 System.out.println(len);
                              }//for문 데이터 읽기
                              System.out.println("count : " + count);
                              if (count == temp) {
                                 System.out.println(count);
                                 wow3 = 1;
                              }//if 다왔는지 확인
                              System.out.println("wow = " + wow3);
                              System.out.println("dfadsf");
                              out2.flush();
                              out2.close();
                              writer.println("<out>");
                              soc.close();
                              System.out.println("hidy");

                           }//while(wow==3)
                           chattingcheck = 0;

                        }//if(BC=bkk)
                        System.out.println("bkk : " + bkk);
                        System.out.println("ACC : " + BC);

                        if (BC + 1 == bkk) {
                           chattingcheck = 1;
                           // give picture to client.

                           while (wow4 == 0) {
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
                              }//while(데이터 읽기)
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
                              }//for문 데이터 보내기

                              System.out.println("wow1 = " + wow4);
                              System.out.println("dfadsf");

                              int ch1 = dout1.readInt();
                              System.out.println("end : " + ch1);
                              soc.close();
                              System.out.println("hidy");
                              if (ch1 == 1) {
                                 wow4 = 1;
                              }//if확인하는거

                           }//while(wow4==0) 확인
                           if (bkk == 2) {
                               String useranswer;
                              out.println("<BANSWERSHEET>");
                           
                           
                           }//if(BC==1)- 마지막사람인지 확인 
                           else {
                              writer.println("<SEND>");
                              writer.println("<START>");
                              BC++;
                              break;
                           }//else(BC!=1)-마지막 사람 X
                          
                        }//if(BC==bkk+1)확인

                        bkk++;
                     }//for(B팀 writer)

                  }//if(네임이 B일떄)
               }//if(클라이언트에ㅓ send보낼때)
               if (input == null) 
               {

                  return;
               }
               System.out.println("anjrh "+input);

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
                        }//for문 (printwriter-a)
                        break;
                     }//이름 A팀으로 시작
                     if (name.startsWith("<B> ")) {
                        for (PrintWriter writer : team_b.values()) {
                           writer.println("MESSAGE " + name + ": " + input);
                        }//for(printwriter-b)
                        break;
                     }//if 이름 B팀으로 시작
                  }//큰 for문
               }//if(Chatting check==0 -채팅 허용)
            }//완전 큰 while문!

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
               }//if(이름 A팀)
               if (name.startsWith("<B> ")) 
               {
                  team_b.remove(name);
                  bteamout--;
               }//if(이름 B팀)
            }//if(name!=null)
            if (out != null) 
            {
               users.remove(out);
               /* The exit user info(name) broadcast all user. */
               for (PrintWriter writer : users.values()) {
                  writer.println("MESSAGE " + "***" + name + "님이 퇴장하셨습니다. ***");
               }//for(Printwriter-퇴장소식)
            }//if(out!=null)

            try {
               socket.close();

            } catch (IOException e) {

            }//catch
         }//finally
      }//run 메소드
   }//handler class
   
   
   private static void random_category() throws IOException 
   {   
      read_file();
      Random random=new Random();
      choice_category=random.nextInt(6);//0~5
      random_word();
   }//진겸

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
   }//진겸
   private static String is_answer(String answer, int number,String team)/*number은 몇번째 진행중인디.*/ 
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
     return "<ANSWERWORNG>"+choice_word[number];
   }//진겸
   private static void read_file() throws IOException {

      FileInputStream input = new FileInputStream("C:\\2-2\\category.txt");// 진겸
      InputStreamReader reader = new InputStreamReader(input, "UTF-8");
      BufferedReader read = new BufferedReader(reader);

      for (int i = 0; i < 6; i++) {
         category[i] = read.readLine();
         String temp[] = read.readLine().split(",");

         for (int j = 0; j < temp.length; j++) {
            word[i][j] = temp[j];
         }
      }
   }//진겸
}
